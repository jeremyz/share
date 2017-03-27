package ch.asynk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Daddy
{
    private static final long serialVersionUID = 1L;

    public static final String SESSION_STATUS = "SESSION_STATUS";

    private static final Logger logger;

    static {
        logger = LoggerFactory.getLogger(Daddy.class);
        warn("beware Daddy is up");
    }

    public static void trace(String msg)    { logger.trace(msg); }
    public static void debug(String msg)    { logger.debug(msg); }
    public static void warn(String msg)     { logger.warn(msg); }
    public static void info(String msg)     { logger.info(msg); }
    public static void error(String msg)    { logger.error(msg); }
    public static void error(String msg, Exception e) { logger.error(String.format("%s : %s", msg, e.getMessage())); }
}
