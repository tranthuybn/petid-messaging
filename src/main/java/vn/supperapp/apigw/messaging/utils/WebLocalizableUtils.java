/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.supperapp.apigw.messaging.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class WebLocalizableUtils {
    protected Logger logger = LoggerFactory.getLogger(WebLocalizableUtils.class);

    public static final String MESSAGE_FILE_PATH_FORMAT = "./web.localizable.%s.properties";
    
    private static final Map<String, Properties> props = new ConcurrentHashMap<>();

    private static volatile WebLocalizableUtils instance;
    private static Object mutex = new Object();

    public WebLocalizableUtils() {
    }

    public static WebLocalizableUtils shared() {
        if (instance == null) {
            synchronized (mutex) {
                if (instance == null) {
                    instance = new WebLocalizableUtils();
                    instance.loadConfig();
                }
            }
        }
        return instance;
    }
    
    private void loadConfig() {
        logger.info("#loadConfig - Start to load configuration from file");
        try {
            for (Language lg : Language.values()) {
                String path = String.format(MESSAGE_FILE_PATH_FORMAT, lg.key());

                Properties propEn = new Properties();
                propEn.load(new FileInputStream(new File(path)));
                props.put(lg.key(), propEn);
            }
            //ResourceBundle labels = ResourceBundle.getBundle("message", Locale.);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("#loadConfig - ERROR: ", ex);
        }
    }
    
    public String getString(String key, String language) {
        Properties prop = props.get(language);
        if (prop != null) {
            return prop.getProperty(key);
        }
        return "";
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
