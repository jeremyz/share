package ch.asynk;

import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.SessionDestroyEvent;
import com.vaadin.server.SessionDestroyListener;
import com.vaadin.server.VaadinServlet;

@WebServlet(value = "/*", asyncSupported = true)
@VaadinServletConfiguration(productionMode = false, ui = ch.asynk.HelloWorldUI.class, closeIdleSessions = true)
public class HelloWorldServlet extends VaadinServlet implements SessionInitListener, SessionDestroyListener
{
    private static final long serialVersionUID = 511085337415583793L;
    @Override
    protected void servletInitialized() throws ServletException {
        super.servletInitialized();
        getService().addSessionInitListener(this);
        getService().addSessionDestroyListener(this);
    }

    @Override
    public void sessionInit(SessionInitEvent event) throws ServiceException
    {
        event.getSession().setLocale(new java.util.Locale("fr", "CH"));
        event.getSession().setAttribute(Daddy.SESSION_STATUS, "unknown");
        System.err.println("sessionInit");
    }

    @Override
    public void sessionDestroy(SessionDestroyEvent event) {
        System.err.println("sessionDestroy");
    }
}
