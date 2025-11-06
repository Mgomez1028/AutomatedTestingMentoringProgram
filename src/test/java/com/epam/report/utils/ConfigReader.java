package com.epam.report.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Properties properties = new Properties();
    private static final String DEFAULT_ENV = "dev";

    static {
        loadProperties();
    }

    private static void loadProperties() {
        String environment = System.getProperty("env", DEFAULT_ENV);
        String path = String.format("config/%s.properties", environment);

        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream(path)) {
            if (input == null) {
                throw new RuntimeException("Config file not found for environment: " + environment);
            }
            properties.load(input);
            System.out.println("Loaded configuration for environment: " + environment);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config file: " + e.getMessage(), e);
        }
    }

    public static String get(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Missing property key: " + key);
        }
        return value.trim();
    }
}
