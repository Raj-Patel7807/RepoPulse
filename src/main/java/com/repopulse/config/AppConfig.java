package com.repopulse.config;

import java.util.Properties;
import java.io.InputStream;

public class AppConfig {
    private static Properties props = new Properties();

    static {
        try {
            InputStream input = AppConfig.class
                    .getClassLoader()
                    .getResourceAsStream("config.properties");
            props.load(input);
        } catch(Exception e) {
            throw new RuntimeException("Failed to load config...!!!");
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}
