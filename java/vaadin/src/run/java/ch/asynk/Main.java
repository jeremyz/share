package ch.asynk;

import com.vaadin.server.DefaultUIProvider;
import com.vaadin.server.UIProvider;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class MyVaadinJettyServer extends Server
{
    public MyVaadinJettyServer(int port, String webappDirectory) throws IOException
    {
        super(port);

        WebAppContext context = new WebAppContext(webappDirectory, "/");

        context.addServlet(buildVaadinServlet(new VaadinServlet(), null), "/*");
        context.addServlet(buildVaadinServlet(new HelloWorldServlet(), HelloWorldUI.class), "/hello/*");

        setHandler(context);
    }

    private ServletHolder buildVaadinServlet(VaadinServlet servlet, Class<? extends UI> uiClass)
    {
        ServletHolder servletHolder = new ServletHolder(servlet);
        servletHolder.setInitParameter(VaadinServlet.SERVLET_PARAMETER_UI_PROVIDER, DefaultUIProvider.class.getName());
        if (uiClass != null) servletHolder.setInitParameter("UI", uiClass.getName());
        return servletHolder;
    }
}

public class Main
{
    public static void main(String[] args) throws Exception
    {
        String webRoot = System.getProperty("WEBROOT");
        if (webRoot == null) webRoot = "./src/main/WebContent";
        System.out.println("http://127.0.0.1:8666/hello");
        new MyVaadinJettyServer(8666, webRoot).start();
    }
}
