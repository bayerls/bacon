package de.uop.code.cubemerging.util;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class Properties {
    private static final String RESOURCE_NAME = "application.properties";
    private static Properties instance;

    private PropertiesConfiguration config;

    private Properties() {
        try {
            this.config = new PropertiesConfiguration(RESOURCE_NAME);
        } catch (ConfigurationException e) {
            throw new RuntimeException("Failed to load properties file: " + RESOURCE_NAME);
        }
    }

    public synchronized static Properties getInstance() {
        if (instance == null) {
            instance = new Properties();
        }

        return instance;
    }

    public String getSameAsServiceProvider() {
        return config.getString("sameAsServiceProvider");
    }

    public String getDisambiguationServiceProvider() {
        return config.getString("disambiguationServiceProvider");
    }

    public String getTripleStore() {
        return config.getString("tripleStore");
    }

    public String getStoreageServiceProvider() {
        return config.getString("storeageServiceProvider");
    }



}
