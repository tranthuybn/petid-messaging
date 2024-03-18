/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.supperapp.apigw.messaging.process.confgis;

import java.util.Map;

/**
 *
 * @author truonglq
 */
public class ProcessConfigInfo {
    private String nodeName;
    private long monitorSleepTime = 10; //seconds

    private Map<String, ExecutorTaskConfigInfo> executorConfigs;
    private Map<String, ThreadProcessConfigInfo> processConfigs;

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public long getMonitorSleepTime() {
        return monitorSleepTime;
    }

    public void setMonitorSleepTime(long monitorSleepTime) {
        this.monitorSleepTime = monitorSleepTime;
    }

    public Map<String, ExecutorTaskConfigInfo> getExecutorConfigs() {
        return executorConfigs;
    }

    public void setExecutorConfigs(Map<String, ExecutorTaskConfigInfo> executorConfigs) {
        this.executorConfigs = executorConfigs;
    }

    public Map<String, ThreadProcessConfigInfo> getProcessConfigs() {
        return processConfigs;
    }

    public void setProcessConfigs(Map<String, ThreadProcessConfigInfo> processConfigs) {
        this.processConfigs = processConfigs;
    }

    public ExecutorTaskConfigInfo getExecutorConfig(String executorName) {
        return this.executorConfigs != null ? this.executorConfigs.get(executorName) : null;
    }
}
