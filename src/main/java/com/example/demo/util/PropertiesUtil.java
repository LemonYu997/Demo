package com.example.demo.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertiesUtil {
    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
    private static String properiesName = "olmgs.properties";
    private static Properties props;

    static {
        props = new Properties();
        try {
            props.load(new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream(properiesName), "UTF-8"));
        } catch (IOException e) {
            logger.error("config file load error", e);
        }
    }

    public static String getProperty(String key) {
        String value = props.getProperty(key.trim());
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return value.trim();
    }

    public static String getProperty(String key, String defaultValue) {
        String value = "";
        InputStream is = null;
        value = getProperty(key, value, is);
        if (null == value || value.equals("")) {
            return defaultValue;
        }
        return value;
    }

    private static String getProperty(String key, String value, InputStream is) {
        try {
            is = PropertiesUtil.class.getClassLoader().getResourceAsStream(properiesName);
            Properties p = new Properties();
            p.load(is);
            value = p.getProperty(key);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                logger.error(e.getMessage(), e);
            }
        }
        return value;
    }
}