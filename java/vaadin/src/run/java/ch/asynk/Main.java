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
    public MyVaadinJettyServer(int port, Class<? extends VaadinServlet> servletClass, Class<? extends UI> uiClass, String webappDirectory)
        throws IOException, InstantiationException, IllegalAccessException
    {
        super(port);

        createIfDoesntExists(webappDirectory);
        WebAppContext context = new WebAppContext(webappDirectory, "/");
        context.addServlet(buildVaadinServlet(servletClass.newInstance(), uiClass), "/*");
        setHandler(context);
    }

    private ServletHolder buildVaadinServlet(VaadinServlet servlet, Class<? extends UI> uiClass)
    {
        ServletHolder servletHolder = new ServletHolder(servlet);
        servletHolder.setInitParameter(VaadinServlet.SERVLET_PARAMETER_UI_PROVIDER, DefaultUIProvider.class.getName());
        servletHolder.setInitParameter("UI", uiClass.getName());
        return servletHolder;
    }

    private void createIfDoesntExists(String directory) throws IOException
    {
        Path path = Paths.get(directory);
        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }
    }
}

public class Main
{
    public static void main(String[] args) throws Exception
    {
        String webRoot = System.getProperty("WEBROOT");
        if (webRoot == null) webRoot = "./src/main/WebContent/";
        new MyVaadinJettyServer(8081, HelloWorldServlet.class, HelloWorld.class, webRoot).start();
    }
}
