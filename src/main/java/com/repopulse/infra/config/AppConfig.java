package com.repopulse.infra.config;

import com.repopulse.infra.exception.AppException;

import java.io.InputStream;
import java.util.Properties;

public class AppConfig {
    private static Properties props = new Properties();

    static {
        try {
            InputStream input = AppConfig.class.getClassLoader().getResourceAsStream("config.properties");
            if(input == null) {
                throw new AppException("Missing `config.properties`. Create `src/main/resources/config.properties` (you can copy `config.properties.example`).");
            }
            props.load(input);
        } catch(Exception e) {
            if(e instanceof AppException) throw (AppException) e;
            throw new AppException("Failed to load configuration. Please check `config.properties`.", e);
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}
