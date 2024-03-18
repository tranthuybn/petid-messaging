package vn.supperapp.apigw.messaging.process.tasks;

import org.slf4j.Logger;
import vn.supperapp.apigw.messaging.process.confgis.ExecutorTaskConfigInfo;

public abstract class BaseTask implements Runnable {
    protected Logger logger;
    protected ExecutorTaskConfigInfo config;

    public ExecutorTaskConfigInfo getConfig() {
        return config;
    }

    public void setConfig(ExecutorTaskConfigInfo config) {
        this.config = config;
    }
}
