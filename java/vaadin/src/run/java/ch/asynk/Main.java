package ch.asynk;

import org.vaadin.jetty.VaadinJettyServer;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        new VaadinJettyServer(8080, HelloWorld.class, "./src/main/WebContent/").start();
    }
}
