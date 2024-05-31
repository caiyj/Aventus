package com.alibaba.aventus.logging;

import com.alibaba.aventus.logging.log4j2.Log4J2AventusLogging;
import com.alibaba.aventus.logging.logback.LogbackAventusLogging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AventusLogging {

    public static final Logger aventus = LoggerFactory.getLogger("Aventus");

    public static final Logger extensionLogger;

    public static final Logger flowLogger;

    static {
        initLoggingSystem();
        extensionLogger = LoggerFactory.getLogger("extension");
        flowLogger = LoggerFactory.getLogger("flow");
    }


    private static void initLoggingSystem() {
        try {
            Class.forName("ch.qos.logback.classic.Logger");
            LogbackAventusLogging logging = new LogbackAventusLogging();
            logging.loadConfiguration();
            aventus.info("init AventusLoggingSystem:[logback] success.");
            return;
        } catch (ClassNotFoundException ignored) {
        } catch (Throwable t) {
            aventus.error("init AventusLoggingSystem:[logback] error.", t);
        }

        try {
            Class.forName("org.apache.logging.log4j.Logger");
            Log4J2AventusLogging logging = new Log4J2AventusLogging();
            logging.loadConfiguration();
            aventus.info("init AventusLoggingSystem:[log4j2] success.");
            return;
        } catch (ClassNotFoundException ignored) {
        } catch (Throwable t) {
            aventus.error("init AventusLoggingSystem:[log4j2] error.", t);
        }

        aventus.warn("init AventusLoggingSystem failed, using system [default] logger.");
    }
}
