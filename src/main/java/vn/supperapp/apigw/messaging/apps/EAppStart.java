package vn.supperapp.apigw.messaging.apps;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EAppStart {
    private static final Logger logger = LoggerFactory.getLogger(EAppStart.class);

    public static void main(String[] args) {
        PropertyConfigurator.configure("../etc/log4j.properties");
        StringBuilder lg = new StringBuilder();
        lg.append("\n#################################################################################################");
        lg.append("\n#                     START APPLICATION                                                          ");
        lg.append("\n#################################################################################################");
        lg.append("\n");
        logger.info(lg.toString());
        try {
            EAppManager.shared().start();
        } catch (Exception ex) {
            logger.info("#main - Error to START app: ", ex);
        }
    }

}
