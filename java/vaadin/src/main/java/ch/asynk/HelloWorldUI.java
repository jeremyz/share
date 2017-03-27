package ch.asynk;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import ch.asynk.ui.Icons;
import ch.asynk.ui.ViewMain;

@PreserveOnRefresh
@Theme("mytheme")
@Title("Hello!!")
public class HelloWorldUI extends UI
{
    private static final long serialVersionUID = 1L;

    private static final String HOME = "Home";
    private static final String ABOUT = "About";

    private Navigator navigator;

    @Override
    protected void init(VaadinRequest request)
    {
        final VerticalLayout vl = new VerticalLayout();
        vl.setSizeFull();
        vl.setMargin(true);
        setContent(vl);

        final MenuBar menu = createMenuBar();

        final HorizontalSplitPanel hsplit = new HorizontalSplitPanel();
        hsplit.setSplitPosition(80f);
        hsplit.addComponent(createLeftPanel());
        hsplit.addComponent(createRightPanel());

        vl.addComponents(menu, hsplit);
        vl.setExpandRatio(menu, 0);
        vl.setExpandRatio(hsplit, 1);

        navigateToHome();
    }

    private Component createLeftPanel()
    {
        final VerticalLayout vl = new VerticalLayout();
        vl.addStyleName("margin-top");
        vl.addStyleName("margin-right");
        navigator = new Navigator(this, vl);
        navigator.addView(HOME, ViewMain.class);
        return vl;
    }

    private Component createRightPanel()
    {
        final VerticalLayout vl = new VerticalLayout();
        vl.setMargin(true);
        vl.addStyleName("mybg");
        // vl.addStyleName("margin-top");
        // vl.addStyleName("margin-right");
        vl.addComponent(new Label("Hello World using mytheme"));
        Button btn = new Button("Push Me!", Icons.home);
        btn.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 1L;
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Notification.show("Pushed!");
                Daddy.trace("trace");
                Daddy.debug("debug");
                Daddy.info("info");
                Daddy.warn("warn");
                Daddy.error("error");
            }
        });
        vl.addComponent(btn);
        return vl;
    }

    private MenuBar createMenuBar()
    {
        Command menuCommand = new Command() {
            private static final long serialVersionUID = 1L;
            @Override
            public void menuSelected(MenuItem selectedItem) {
                String itemText = selectedItem.getText();
                if (itemText.equals(HOME)) {
                    navigateToHome();
                } else if (itemText.equals(ABOUT)) {
                    showAbout();
                } else {
                    Daddy.error("unhandeled MenuItem : " + itemText);
                }
            }
        };

        final MenuBar menu = new MenuBar();
        menu.addItem(HOME, Icons.home, menuCommand);
        menu.addItem(ABOUT, Icons.help, menuCommand);
        MenuBar.MenuItem login = menu.addItem((String) UI.getCurrent().getSession().getAttribute(Daddy.SESSION_STATUS), menuCommand);
        login.setStyleName("menuRight");
        menu.setWidth("100%");

        return menu;
    }

    public void navigateToHome()    { navigator.navigateTo(HOME); }

    private void showAbout()
    {
        final Window about = new Window(ABOUT);
        about.setContent( new CustomLayout(ABOUT) );
        about.setHeight("50%");
        about.setWidth("600px");
        about.center();
        about.setModal(true);
        UI.getCurrent().addWindow(about);
    }
}
