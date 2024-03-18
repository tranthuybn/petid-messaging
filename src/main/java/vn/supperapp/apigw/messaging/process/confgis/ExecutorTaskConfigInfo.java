package vn.supperapp.apigw.messaging.process.confgis;

import java.util.Map;

public class ExecutorTaskConfigInfo {
    private String monitorName;
    private int coreSize = 100;
    private int maxSize = 150;
    private int keepAliveTime = 30;
    private int queueSize = 100;
    private int maxActiveTaskAllow = 10;
    private Boolean enabled = false;
    private Map<String, Object> configurations;

    public String getMonitorName() {
        return monitorName;
    }

    public void setMonitorName(String monitorName) {
        this.monitorName = monitorName;
    }

    public int getCoreSize() {
        return coreSize;
    }

    public void setCoreSize(int coreSize) {
        this.coreSize = coreSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public int getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(int keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public int getMaxActiveTaskAllow() {
        return maxActiveTaskAllow;
    }

    public void setMaxActiveTaskAllow(int maxActiveTaskAllow) {
        this.maxActiveTaskAllow = maxActiveTaskAllow;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Map<String, Object> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(Map<String, Object> configurations) {
        this.configurations = configurations;
    }

    public Object getConfig(String key) {
        return this.configurations != null ? this.configurations.get(key) : null;
    }

    public String getConfigAsString(String key) {
        return this.configurations != null ? String.valueOf(this.configurations.get(key)) : null;
    }

    public Double getConfigAsDouble(String key) {
        return this.configurations != null && this.configurations.get(key) != null ? Double.valueOf(this.configurations.get(key).toString()) : null;
    }

    public Long getConfigAsLong(String key) {
        return this.configurations != null && this.configurations.get(key) != null ? Long.valueOf(this.configurations.get(key).toString()) : null;
    }

    public int getConfigAsInt(String key) {
        return this.configurations != null && this.configurations.get(key) != null ? Integer.parseInt(this.configurations.get(key).toString()) : null;
    }
}
