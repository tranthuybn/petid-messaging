package vn.supperapp.apigw.messaging.process.confgis;

import java.util.Map;

public class ThreadProcessConfigInfo {
    private String threadName;
    private String businessClass;
    private Boolean enabled = false;
    private int intervalTime;

    private Map<String, Object> configurations;

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public String getBusinessClass() {
        return businessClass;
    }

    public void setBusinessClass(String businessClass) {
        this.businessClass = businessClass;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public int getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(int intervalTime) {
        this.intervalTime = intervalTime;
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
