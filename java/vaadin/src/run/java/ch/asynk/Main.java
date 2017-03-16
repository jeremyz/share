package ch.asynk;

import org.vaadin.jetty.VaadinJettyServer;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        String webRoot = System.getProperty("WEBROOT");
        if (webRoot == null) webRoot = "./src/main/WebContent/";
        new VaadinJettyServer(8080, HelloWorld.class, webRoot).start();
    }
}
