package vn.supperapp.apigw.messaging.apps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EAppStop {

    private static final Logger logger = LoggerFactory.getLogger(EAppStop.class);

    public static void main(String[] args) {
        StringBuilder lg = new StringBuilder();
        lg.append("\n#################################################################################################");
        lg.append("\n#                     STOP APPLICATION                                                          ");
        lg.append("\n#################################################################################################");
        lg.append("\n");
        logger.info(lg.toString());
        try {
            EAppManager.shared().stop();
        } catch (Exception ex) {
            logger.info("#main - Error to STOP app: ", ex);
        }
    }
}
