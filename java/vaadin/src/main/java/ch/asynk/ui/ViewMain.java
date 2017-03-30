package ch.asynk.ui;

import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import ch.asynk.security.U2fConnector;

public class ViewMain extends TabSheet implements View, U2fConnector.U2fListener
{
    private static final long serialVersionUID = 1L;

    private static final String U2F_TITLE = "FIDO U2F (Universal 2nd factor)";

    private final Window u2fWindow;
    private final U2fConnector u2fConnector;

    private enum Action { U2F_REGISTER, U2F_AUTHENTICATE }

    public ViewMain()
    {
        u2fWindow = u2fWindow();
        u2fConnector = new U2fConnector();
        addStyleName("framed");
        addTab(u2fComponent("register", Action.U2F_REGISTER), "Register", Icons.u2flogo48);
        addTab(u2fComponent("authenticate", Action.U2F_AUTHENTICATE), "Authenticate", Icons.u2f32);
        setSelectedTab(2);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        System.err.println(this.getClass().getName()+"::enter");
    }

    private VerticalLayout content(String s)
    {
        VerticalLayout ly = new VerticalLayout();
        ly.addComponent(new Label(" Hi, this is " + s));
        return ly;
    }

    private Component u2fComponent(final String title, Action action)
    {
        final TextField tf = new TextField();
        tf.setRequired(true);
        tf.setInputPrompt("username");
        tf.addValidator(new RegexpValidator("^(?=.{6,20}$)[a-zA-Z][a-zA-Z0-9#@.]+[a-zA-Z]$", "invalid username"));

        final ViewMain instance = this;
        final Button bt = new Button(title.toLowerCase());
        bt.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 1L;
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    tf.validate();
                    String userId = tf.getValue();
                    tf.setValue("");
                    if (action == Action.U2F_REGISTER)
                        u2fConnector.sendRegisterRequest(userId, instance);
                    else if (action == Action.U2F_AUTHENTICATE)
                        u2fConnector.startAuthentication(userId, instance);
                } catch (com.yubico.u2f.exceptions.U2fBadConfigurationException e) {
                    Notification.show(U2F_TITLE, e.getMessage(), Notification.Type.ERROR_MESSAGE);
                } catch (Exception e) {
                    Notification.show("invalid user name");
                }
            }
        });

        Panel panel = new Panel(title);
        panel.setWidth("450px");
        final GridLayout ly = new GridLayout(2,2);
        ly.setMargin(true);
        ly.setSpacing(true);
        panel.setContent(ly);
        ly.addComponent(new Image(null, Icons.u2flock), 0, 0, 0, 1);
        ly.addComponent(tf, 1, 0);
        ly.addComponent(bt,1,1);
        ly.setComponentAlignment(bt, Alignment.MIDDLE_CENTER);

        VerticalLayout vl = new VerticalLayout();
        vl.setMargin(true);
        vl.setSizeFull();
        vl.addComponent(panel);
        vl.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
        return vl;
    }

    public void u2fCallback(U2fConnector.U2fAction action, String msg)
    {
        Notification n = null;
        switch (action) {
            case REGISTRATION_PENDING:
            case AUTHENTICATION_PENDING:
                UI.getCurrent().addWindow(u2fWindow);
                break;
            case REGISTRATION_SUCCESS:
                msg = "Your token has been successfully registred.";
                break;
            case REGISTRATION_FAILURE:
                msg = "Registration failed with error : " + msg;
                break;
            case AUTHENTICATION_SUCCESS:
                msg = "You have been successfully authenticated.";
                break;
            case AUTHENTICATION_FAILURE:
                msg = "Authentication failed with error : " + msg;
                break;
        }
        switch (action) {
            case REGISTRATION_SUCCESS:
            case AUTHENTICATION_SUCCESS:
                n = new Notification(U2F_TITLE, msg, Notification.Type.HUMANIZED_MESSAGE);
                n.setIcon(Icons.thumbUp);
                break;
            case REGISTRATION_FAILURE:
            case AUTHENTICATION_FAILURE:
                n = new Notification(U2F_TITLE, msg, Notification.Type.ERROR_MESSAGE);
                n.setIcon(Icons.thumbDown);
                break;
        }
        if (n != null) {
            n.setDelayMsec(5000);
            u2fWindow.close();
            n.show(Page.getCurrent());
        }
    }

    private Window u2fWindow()
    {
        Window w = new Window(U2F_TITLE);
        final VerticalLayout vy = new VerticalLayout();
        vy.setMargin(true);
        vy.setSpacing(true);
        vy.addComponent(new Label("Insert your u2f token and click on it."));
        vy.addComponent(new Image(null, Icons.u2flock));
        w.setModal(true);
        w.setClosable(false);
        w.setContent(vy);
        w.setResizable(false);
        w.setWidth("280px");
        w.setHeight("220px");
        w.center();
        return w;
    }
}
