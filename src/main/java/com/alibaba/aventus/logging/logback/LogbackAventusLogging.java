package com.alibaba.aventus.logging.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.alibaba.aventus.logging.ResourceUtils;
import org.slf4j.LoggerFactory;

/**
 * Support for Logback version 1.0.8 or higher
 *
 * @author <a href="mailto:huangxiaoyu1018@gmail.com">hxy1991</a>
 * @author <a href="mailto:hujun3@xiaomi.com">hujun</a>
 * @since 0.9.0
 */
public class LogbackAventusLogging {

    private static final String LETTER_LOGBACK_LOCATION = "classpath:aventus-logback.xml";

    public void loadConfiguration() {
        LoggerContext context = loadingConfiguration();
        if (context.getObject("RECONFIGURE_ON_CHANGE_TASK") != null && context.getCopyOfListenerList().stream().noneMatch(listener -> listener instanceof LoggerContextListener)) {
            context.addListener(new LoggerContextListener());
        }
    }

    private LoggerContext loadingConfiguration() {
        try {
            LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
            JoranConfiguratorAdapter configAdapter = new JoranConfiguratorAdapter();
            configAdapter.setContext(context);
            configAdapter.configure(ResourceUtils.getResourceUrl(LETTER_LOGBACK_LOCATION));
            return context;
        } catch (Exception e) {
            throw new IllegalStateException("Could not initialize Logback Letter logging from " + LETTER_LOGBACK_LOCATION, e);
        }
    }

    class LoggerContextListener implements ch.qos.logback.classic.spi.LoggerContextListener {
        @Override
        public boolean isResetResistant() {
            return true;
        }

        @Override
        public void onReset(LoggerContext context) {
            loadingConfiguration();
        }

        @Override
        public void onStart(LoggerContext context) {
        }

        @Override
        public void onStop(LoggerContext context) {
        }

        @Override
        public void onLevelChange(Logger logger, Level level) {
        }
    }

}
