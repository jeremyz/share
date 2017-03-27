package ch.asynk.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

public class ViewMain extends TabSheet implements View
{
    private static final long serialVersionUID = 1L;

    public ViewMain()
    {
        addStyleName("framed");
        addTab(content("0"), "Tab 0", Icons.home, 0);
        addTab(content("1"), "Tab 1");
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
}
