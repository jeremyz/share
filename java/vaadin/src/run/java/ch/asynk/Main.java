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
    public VaadinJettyServer(int port, String webappDirectory)
    {
        super();
        configure(port);
        buildWebapp(webappDirectory);
    }

    abstract protected void configure(int port);

    protected void buildWebapp(String webappDirectory)
    {

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
    public HttpVaadinJettyServer(int port, String webappDirectory) { super(port, webappDirectory); }

    @Override
    protected void configure(int port)
    {
        System.out.println("http://127.0.0.1:" + port + "/hello");
        addConnector(httpConnector(port));
    }
}

class HttpsVaadinJettyServer extends VaadinJettyServer
{
    public HttpsVaadinJettyServer(int port, String webappDirectory) { super(port, webappDirectory); }

    @Override
    protected void configure(int port)
    {
        System.out.println("https://127.0.0.1:" + port + "/hello");
        addConnector(httpsConnector(port));
    }
}
class HttpHttpsVaadinJettyServer extends HttpsVaadinJettyServer
{
    public HttpHttpsVaadinJettyServer(int port, String webappDirectory) { super(port, webappDirectory); }

    @Override
    protected void configure(int port)
    {
        System.out.println("http://127.0.0.1:" + port + "/hello");
        System.out.println("https://127.0.0.1:" + (port + 1) + "/hello");
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

        new HttpVaadinJettyServer(port, webRoot).start();
    }
}
