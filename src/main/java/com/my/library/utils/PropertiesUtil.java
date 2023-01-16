package com.my.library.utils;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {
    private static final Properties PROPERTIES = new Properties();
    private static final String PROPERTIES_FILE_NAME = "database.properties";

    static{
        loadProperties();
    }

    private PropertiesUtil() {
    }

    private static void loadProperties(){
        try(var inputStream =  PropertiesUtil.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME)){
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }
}
