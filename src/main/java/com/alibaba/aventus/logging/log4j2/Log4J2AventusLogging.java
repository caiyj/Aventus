package com.alibaba.aventus.logging.log4j2;

import com.alibaba.aventus.logging.ResourceUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.LoggerConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

/**
 * Support for Log4j version 2.7 or higher
 *
 * @author <a href="mailto:huangxiaoyu1018@gmail.com">hxy1991</a>
 * @since 0.9.0
 */
public class Log4J2AventusLogging {

    private static final String LETTER_LOG4J2_LOCATION = "classpath:aventus-log4j2.xml";

    private static final String FILE_PROTOCOL = "file";

    public void loadConfiguration() {
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        Configuration configuration = context.getConfiguration();

        Configuration newConfiguration = loadConfiguration(context, LETTER_LOG4J2_LOCATION);
        newConfiguration.start();

        // append logger and appender to configuration
        for (Appender appender : newConfiguration.getAppenders().values()) {
            configuration.addAppender(appender);
        }
        for (Map.Entry<String, LoggerConfig> entry : newConfiguration.getLoggers().entrySet()) {
            configuration.addLogger(entry.getKey(), entry.getValue());
        }
        context.updateLoggers();
    }

    private Configuration loadConfiguration(LoggerContext context, String location) {
        try {
            URL url = ResourceUtils.getResourceUrl(location);
            ConfigurationSource source = getConfigurationSource(url);
            // since log4j 2.7 getConfiguration(LoggerContext context, ConfigurationSource source)
            return ConfigurationFactory.getInstance().getConfiguration(context, source);
        } catch (Exception e) {
            throw new IllegalStateException("Could not initialize Log4J2 logging from " + location, e);
        }
    }

    private ConfigurationSource getConfigurationSource(URL url) throws IOException {
        InputStream stream = url.openStream();
        if (FILE_PROTOCOL.equals(url.getProtocol())) {
            return new ConfigurationSource(stream, ResourceUtils.getResourceAsFile(url));
        } else {
            return new ConfigurationSource(stream, url);
        }
    }
}
