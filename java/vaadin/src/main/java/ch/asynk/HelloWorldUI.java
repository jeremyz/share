package ch.asynk;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PreserveOnRefresh
@Title("Hello!!")
@Theme("mytheme")
public class HelloWorldUI extends UI
{
    private static final long serialVersionUID = 511085335415683713L;
    private static Logger logger = null;

    @Override
    protected void init(VaadinRequest request)
    {
        logger = LoggerFactory.getLogger(this.getClass());
        VerticalLayout content = new VerticalLayout();
        setContent(content);

        content.addComponent(new Label("Hello World using mytheme"));
        Button btn = new Button("Push Me!", new ThemeResource("icons/home.png"));
        btn.addClickListener(new ClickListener() {
            private static final long serialVersionUID = 5808429544582385114L;
            @Override
            public void buttonClick(ClickEvent event) {
                Notification.show("Pushed!");
                System.out.println("System.out");
                System.err.println("System.err");
                logger.trace("trace");
                logger.debug("debug");
                logger.info("info");
                logger.warn("warn");
                logger.error("error");
            }
        });
        content.addComponent(btn);
    }
}
