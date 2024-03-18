/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.supperapp.apigw.messaging.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import vn.supperapp.apigw.messaging.process.business.BaseProcessBusiness;
import vn.supperapp.apigw.messaging.process.confgis.ExecutorTaskConfigInfo;
import vn.supperapp.apigw.messaging.process.confgis.ProcessConfigInfo;
import vn.supperapp.apigw.messaging.process.confgis.ThreadProcessConfigInfo;
import vn.supperapp.apigw.messaging.process.tasks.BaseTask;
import vn.supperapp.apigw.messaging.utils.CommonUtils;
import vn.supperapp.apigw.messaging.process.ProcessMonitorThread;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author truonglq
 */
public class ProcessManager {
    protected Logger logger = LoggerFactory.getLogger(ProcessManager.class.getName());
    private static final String PROCESS_CONFIG_PATH = "./config-process.yml";

    private static volatile ProcessManager instance;
    private static Object mutex = new Object();

    public ProcessManager() {
    }

    public static ProcessManager shared() {
        if (instance == null) {
            synchronized (mutex) {
                if (instance == null) {
                    instance = new ProcessManager();
                    instance.loadConfigurations();
                }
            }
        }
        return instance;
    }

    public void loadConfigurations() {
        logger.info("#loadConfigurations - load config");
        try {
            logger.info("#loadConfigurations - load SMS CONFIG INFO");

            Yaml yml = new Yaml();
            InputStream is = new FileInputStream(PROCESS_CONFIG_PATH);
            this.configs = yml.loadAs(is, ProcessConfigInfo.class);

        } catch (Exception ex) {
            logger.error("#loadConfigurations - Errors: ", ex);
//            System.exit(0);
        }
    }

    //region - Properties
    private ProcessMonitorThread monitor;
    private ProcessConfigInfo configs;

//    final List<ThreadPoolExecutor> executorList = new ArrayList<>();
    final List<BaseProcessBusiness> processList = new ArrayList<>();

    //endregion - Properties

    public void start() {
        logger.info("#start");
        if (instance == null || this.configs == null) {
//            throw new Exception("configure method builder only use by shared() object");
            return;
        }
        try {
            logger.info("#start - set config");

            logger.info("#start - Start monitor thread");
            monitor = new ProcessMonitorThread(this.configs.getMonitorSleepTime());
            Thread thread = new Thread(monitor);
            thread.start();

            logger.info("#start - Start executor thread pool");
            if (this.configs.getExecutorConfigs() != null && !this.configs.getExecutorConfigs().isEmpty()) {
                this.configs.getExecutorConfigs().forEach((k, v) -> {
                    logger.info("#start - Init ThreadPoolExcecutor & add to monitor");
                    ThreadPoolExecutor executor = new ThreadPoolExecutor(
                            v.getCoreSize(),
                            v.getMaxSize(),
                            v.getKeepAliveTime(),
                            TimeUnit.SECONDS,
                            new LinkedBlockingQueue<>(v.getQueueSize())
                    );
                    monitor.addOrUpdateExecutorForMonitor(v.getMonitorName(), executor);
//                    executorList.add(executor);
                });
            }

            logger.info("#start - Start Business process");
            if (this.configs.getProcessConfigs() != null && !this.configs.getProcessConfigs().isEmpty()) {
                ClassLoader loader = ClassLoader.getSystemClassLoader();
                this.configs.getProcessConfigs().forEach((k,v) -> {
                    if (v.getEnabled() && !CommonUtils.isNullOrEmpty(v.getBusinessClass())) {
                        try {
                            Class<?> clazz = loader.loadClass(v.getBusinessClass());
                            BaseProcessBusiness business = (BaseProcessBusiness) clazz.getConstructor(ThreadProcessConfigInfo.class).newInstance(v);
                            business.start();
                            processList.add(business);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            logger.info("#start - Start business error: ", ex);
                        }
                    }
                });
            }

            logger.info("#start - START PROCESS SUCCESS");
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("#start ERROR", ex);
        }
    }

    public void stop() {
        logger.info("#stop");
        try {
            logger.info("#stop - payoutSenderThreadpool");
            if (monitor != null) {
                monitor.stopAllThreadPool();
            }

            processList.stream().forEach(p -> {
                try {
                    p.stop();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            instance = null;
        } catch (Exception ex) {
            logger.error("#stop ERROR", ex);
        }
    }

    public void executeTask(String executorName, BaseTask task) {
        try {
            ExecutorTaskConfigInfo executorConfig = this.configs.getExecutorConfig(executorName);
            if (executorConfig == null) {
                logger.info("#executeTask - CONFIG IS NULL");
                return;
            }

            ThreadPoolExecutor threadPoolExecutor = this.monitor.getThreadPoolExecutor(executorConfig.getMonitorName());
            if (threadPoolExecutor == null) {
                logger.info("#executeTask - {} is null", executorConfig.getMonitorName());
                return;
            }

            if (threadPoolExecutor == null || threadPoolExecutor.isTerminated()) {
                logger.info("#executeTask - Thread pool INVALID");
                return;
            }
            if (threadPoolExecutor.getActiveCount() >= executorConfig.getMaxActiveTaskAllow()) {
                logger.info("#executeTask - Thread pool OVER MAXIMUM ACTIVE COUNT");
                return;
            }

            task.setConfig(executorConfig);
            threadPoolExecutor.execute(task);
        } catch (Exception ex) {
            logger.error("#executeTask - ERROR: ", ex);
        }
    }

}
