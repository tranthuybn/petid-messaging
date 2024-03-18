package vn.supperapp.apigw.messaging.configs.smsgw;

import java.util.Map;

public class SmsGwConfig {
    private int maxTotal = 500;
    private int maxConnectionPerHost = 500;
    private int connectionTimeout = 60000;
    private int connectionRequestTimeout = 60000;
    private int soTimeout = 60000;
    private int idleTimeout = 30000;
    private Map<String, SmsGwProfileInfo> smsgwProfiles;

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getMaxConnectionPerHost() {
        return maxConnectionPerHost;
    }

    public void setMaxConnectionPerHost(int maxConnectionPerHost) {
        this.maxConnectionPerHost = maxConnectionPerHost;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public int getSoTimeout() {
        return soTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    public int getIdleTimeout() {
        return idleTimeout;
    }

    public void setIdleTimeout(int idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    public Map<String, SmsGwProfileInfo> getSmsgwProfiles() {
        return smsgwProfiles;
    }

    public void setSmsgwProfiles(Map<String, SmsGwProfileInfo> smsgwProfiles) {
        this.smsgwProfiles = smsgwProfiles;
    }

    public SmsGwProfileInfo getProfile(String profileName) {
        return this.smsgwProfiles != null ? this.smsgwProfiles.get(profileName) : null;
    }
}
