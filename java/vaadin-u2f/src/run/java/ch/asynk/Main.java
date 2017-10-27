package ch.asynk;

import com.vaadin.server.DefaultUIProvider;
import com.vaadin.server.UIProvider;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// https://github.com/eclipse/jetty.project/blob/jetty-9.3.x/examples/embedded/src/main/java/org/eclipse/jetty/embedded/LikeJettyXml.java

abstract class VaadinJettyServer extends Server
{
    public VaadinJettyServer(int port, final String webappDirectory, final String contextPath)
    {
        super();
        configure(port, contextPath);
        buildWebapp(webappDirectory, contextPath);
    }

    abstract protected void configure(int port, final String contextPath);

    protected void buildWebapp(final String webappDirectory, final String contextPath)
    {

        WebAppContext context = new WebAppContext(webappDirectory, contextPath);

        context.addServlet(buildVaadinServlet(new HelloWorldServlet(), HelloWorldUI.class), "/*");

        setHandler(context);
    }

    protected void say(int port, boolean https, final String contextPath)
    {
        System.out.println(String.format("%s://localhost:%d%s", (https ? "https" : "http"), port, contextPath));
    }

    private ServletHolder buildVaadinServlet(VaadinServlet servlet, Class<? extends UI> uiClass)
    {
        ServletHolder servletHolder = new ServletHolder(servlet);
        servletHolder.setInitParameter(VaadinServlet.SERVLET_PARAMETER_UI_PROVIDER, DefaultUIProvider.class.getName());
        if (uiClass != null) servletHolder.setInitParameter("UI", uiClass.getName());
        return servletHolder;
    }

    protected Connector httpConnector(int port)
    {
        ServerConnector connector = new ServerConnector(this);
        connector.setPort(port);
        return connector;
    }

    protected Connector httpsConnector(int port)
    {
        HttpConfiguration httpConf = new HttpConfiguration();
        httpConf.addCustomizer(new SecureRequestCustomizer());

        SslContextFactory sslContextFactory = new SslContextFactory();
        // keytool -keystore src/run/resources/keystore.jks -genkey -alias asynk.ch -keyalg RSA -keysize 2048
        sslContextFactory.setKeyStorePath(VaadinJettyServer.class.getResource("/keystore.jks").toExternalForm());
        sslContextFactory.setKeyStorePassword("123456");
        sslContextFactory.setKeyManagerPassword("123456");
        ServerConnector connector = new ServerConnector(this,
                new SslConnectionFactory(sslContextFactory, "http/1.1"),
                new HttpConnectionFactory(httpConf));
        connector.setPort(port);
        return connector;
    }
}

class HttpVaadinJettyServer extends VaadinJettyServer
{
    public HttpVaadinJettyServer(int port, final String webappDirectory, final String contextPath) { super(port, webappDirectory, contextPath); }

    @Override
    protected void configure(int port, final String contextPath)
    {
        say(port, false, contextPath);
        addConnector(httpConnector(port));
    }
}

class HttpsVaadinJettyServer extends VaadinJettyServer
{
    public HttpsVaadinJettyServer(int port, final String webappDirectory, final String contextPath) { super(port, webappDirectory, contextPath); }

    @Override
    protected void configure(int port, final String contextPath)
    {
        say(port, true, contextPath);
        addConnector(httpsConnector(port));
    }
}
class HttpHttpsVaadinJettyServer extends HttpsVaadinJettyServer
{
    public HttpHttpsVaadinJettyServer(int port, final String webappDirectory, final String contextPath) { super(port, webappDirectory, contextPath); }

    @Override
    protected void configure(int port, final String contextPath)
    {
        say(port, false, contextPath);
        say(port + 1, true, contextPath);
        this.setConnectors(new Connector[] { httpConnector(port), httpsConnector(port + 1) });
    }
}

public class Main
{
    public static void main(String[] args) throws Exception
    {
        int port = 8666;
        String webRoot = System.getProperty("WEBROOT");
        if (webRoot == null) webRoot = "./src/main/WebContent";

        new HttpsVaadinJettyServer(port, webRoot, "/test").start();
    }
}
