package vn.supperapp.apigw.messaging.apps;

import vn.supperapp.apigw.messaging.process.ProcessManager;
import org.openide.util.Exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

public class EAppManager {
    private static final Logger logger = LoggerFactory.getLogger(EAppManager.class);

    private static final String HTTP_APP_CONFIG_PATH = "./application.yml";

    private static volatile EAppManager instance;
    private static Object mutex = new Object();

    private EApplication eApp;
//    private SmsConfigInfo smsConfigInfo;
    private boolean isRunning = false;

    public static EAppManager shared() {
        if (instance == null) {
            synchronized (mutex) {
                if (instance == null) {
                    instance = new EAppManager();
                }
            }
        }
        return instance;
    }

    private void startHttpApp() {
        logger.info("#startHttpApp - Start HTTP/HTTPS application");
        try {
            String[] systemParams = new String[] {"server", HTTP_APP_CONFIG_PATH};
            eApp = new EApplication();
            eApp.run(systemParams);

        } catch (Exception ex) {
            logger.error("#startHttpApp - Error: ", ex);
            System.exit(0);
        }
    }

    private void startProcessApp() {
        //Load config
        try {
            ProcessManager.shared().start();
        } catch (Exception ex) {
            logger.error("loadConfig - ERROR: ", ex);
            Exceptions.printStackTrace(ex);
            System.exit(0); //DO NOT START INCASE LOAD CONFIG ERROR
        }

    }

    public void start() {
        logger.info("#start starting process");
        if (!isRunning) {
            synchronized (mutex) {
                if (!isRunning) {
                    startHttpApp();
                    startProcessApp();
                    isRunning = true;
                }
            }
        }
    }

    public void stop() {
        try {
            logger.info("#stop stopping process");
            ProcessManager.shared().stop();
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("#stop - ERROR: ", ex);
        }
    }

}
