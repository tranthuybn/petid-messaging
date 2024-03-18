package vn.supperapp.apigw.messaging.process.business;

import vn.supperapp.apigw.messaging.process.ProcessThreadMX;
import vn.supperapp.apigw.messaging.process.confgis.ThreadProcessConfigInfo;

import javax.management.NotCompliantMBeanException;

public abstract class BaseProcessBusiness extends ProcessThreadMX {

    protected volatile boolean isRunning = false;
    protected long sleepTime = 5;

    protected ThreadProcessConfigInfo config;

    public BaseProcessBusiness(ThreadProcessConfigInfo config) throws NotCompliantMBeanException {
        super(config.getThreadName());
        try {
            registerAgent(String.format("%s:name=%s", config.getThreadName(), config.getThreadName()));

            this.config = config;
            this.sleepTime = config.getIntervalTime();
        } catch (Exception ex) {
            logger.error("#BaseProcessBusiness - ERROR: ", ex);
        }
    }

    protected abstract void process();
}
