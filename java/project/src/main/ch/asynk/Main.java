package ch.asynk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main
{
    private static Logger logger = null;

    public static void main(String [] args )
    {
        logger = LoggerFactory.getLogger(Main.class);
        System.out.println("System.out");
        System.err.println("System.err");
        logger.trace("trace");
        logger.debug("debug");
        logger.info("info");
        logger.warn("warn");
        logger.error("error");
        System.exit(0);
    }
}
