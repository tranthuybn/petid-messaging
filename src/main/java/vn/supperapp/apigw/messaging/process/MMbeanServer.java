package vn.supperapp.apigw.messaging.process;

import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;

public class MMbeanServer {
    public static synchronized MBeanServer getInstance() {
        return ManagementFactory.getPlatformMBeanServer();
    }

    protected MMbeanServer() {
    }
}
