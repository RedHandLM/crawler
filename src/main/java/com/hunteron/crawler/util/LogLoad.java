package com.hunteron.crawler.util;

import com.hunteron.crawler.configuration.RegularParser;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.FileInputStream;

public class LogLoad {
    public static void log4j2() {
        ConfigurationFactory.ConfigurationSource source;
        String path = RegularParser.class.getClassLoader().getResource("log4j2.xml").getPath();
        try {
            source = new ConfigurationFactory.ConfigurationSource(new FileInputStream(path));
            Configurator.initialize(null, source);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
