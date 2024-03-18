/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.supperapp.apigw.messaging.utils;

import vn.supperapp.apigw.messaging.utils.enums.Language;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author truonglq
 */
public class LanguageUtils {
    
    public static final String MESSAGE_FILE_PATH_FORMAT = "message.%s.properties";
    
    private static final Map<String, Properties> props = new ConcurrentHashMap<>();
    
    static {
        try {
            for (Language lg : Language.values()) {
                String messageFilePath =  String.format(MESSAGE_FILE_PATH_FORMAT, lg.key());
                Properties propEn = new Properties();
                propEn.load(new FileInputStream(new File(messageFilePath)));
                props.put(lg.key(), propEn);
            }
            //ResourceBundle labels = ResourceBundle.getBundle("message", Locale.);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void init() {}
    
    public static String getString(String key, String language) {
        Properties prop = props.get(language);
        if (prop != null) {
            return prop.getProperty(key);
        }
        return "";
    }
    
    @Deprecated
    public static String getMessage(String key) {
        Properties propEn = props.get(Language.defaultLanguage().key());
        String k = propEn.getProperty(key);
        return k;
    }
    
    public static String getMessage(String key, String language) {
        Properties prop = props.get(language);
        if (prop == null) {
            prop = props.get(Language.defaultLanguage().key());
        }
        if (prop != null) {
            return prop.getProperty(key);
        }
        return "";
    }

}
