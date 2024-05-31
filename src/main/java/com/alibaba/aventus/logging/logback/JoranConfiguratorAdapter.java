package com.alibaba.aventus.logging.logback;

import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.event.SaxEvent;
import ch.qos.logback.core.joran.spi.JoranException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * ensure that Letter configuration does not affect user configuration savepoints and  scanning url.
 *
 * @author <a href="mailto:hujun3@xiaomi.com">hujun</a>
 * @see <a href="https://github.com/alibaba/nacos/issues/6999">#6999</a>
 */
public class JoranConfiguratorAdapter extends JoranConfigurator {

    /**
     * ensure that Letter configuration does not affect user configuration savepoints.
     *
     * @param eventList safe data
     */
    @Override
    public void registerSafeConfiguration(List<SaxEvent> eventList) {
    }


    /**
     * ensure that Letter configuration does not affect user configuration scanning url.
     *
     * @param url config url
     * @throws Exception e
     */
    public void configure(URL url) throws Exception {
        try {
            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            try (InputStream is = connection.getInputStream()) {
                doConfigure(is);
            }
        } catch (IOException e) {
            String errMsg = "Could not open URL [" + url + "].";
            addError(errMsg, e);
            throw new JoranException(errMsg, e);
        }
    }
}
