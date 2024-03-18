/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.supperapp.apigw.messaging.process;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

/**
 *
 * @author truonglq
 */
public class ProcessMonitorThread implements Runnable {
    protected Logger logger = LoggerFactory.getLogger(ProcessMonitorThread.class.getName());

    private long sleepTime = 5*1000; //Milliseconds
    private Map<String, ThreadPoolExecutor> executors = new ConcurrentHashMap<>();
    private boolean running = true;

    public ProcessMonitorThread(long sleepTime) {
        this.sleepTime = sleepTime * 1000;
    }

    public void shutdown() {
        this.running = false;
    }
    
    public void addOrUpdateExecutorForMonitor(String name, ThreadPoolExecutor executor) {
        ThreadPoolExecutor oldExecutor = executors.get(name);
        if (oldExecutor != null) {
            oldExecutor.shutdown();
        }
        executors.put(name, executor);
    }

    public ThreadPoolExecutor getThreadPoolExecutor(String name) {
        return executors != null ? executors.get(name) : null;
    }

    public void stopAllThreadPool() {
        try {
            executors.forEach((k,v) -> {
                try {
                    v.shutdown();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    logger.error("#executors - Shutdown ERROR: ", ex);
                }
            });
        } catch (Exception ex) {
            logger.error("#stopAllThreadPool - ERROR: ", ex);
        }
    }
    
    @Override
    public void run() {
        while (running) {
            if (executors == null || executors.isEmpty()) {
                logger.info("#run - No thread to monitor");
            } else {
                for (Map.Entry<String, ThreadPoolExecutor> thread : executors.entrySet()) {
                    String threadName = thread.getKey();
                    ThreadPoolExecutor executor = thread.getValue();
                    logger.info(String.format("#%s: [%d/%d] Active: %d, Completed: %d, Task: %d, isShutdown: %s, isTerminated: %s",
                            threadName,
                            executor.getPoolSize(),
                            executor.getCorePoolSize(),
                            executor.getActiveCount(),
                            executor.getCompletedTaskCount(),
                            executor.getTaskCount(),
                            executor.isShutdown(),
                            executor.isTerminated()));
                }
            }
            try {
                Thread.sleep(this.sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
