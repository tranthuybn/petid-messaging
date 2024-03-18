package vn.supperapp.apigw.messaging.beans;

import java.util.Map;
import java.util.Set;

public class AppConfigInfo {
    private String releaseMode;
    private String appPrivateKey;
    private String appPublicKey;
    private Set<String> clientRestriction;
    private Map<String, Object> othersConfigs;
    private Map<String, ConsumerClientConfigInfo> consumers;

    public Map<String, Object> getOthersConfigs() {
        return othersConfigs;
    }

    public void setOthersConfigs(Map<String, Object> othersConfigs) {
        this.othersConfigs = othersConfigs;
    }

    public String getReleaseMode() {
        return releaseMode;
    }

    public void setReleaseMode(String releaseMode) {
        this.releaseMode = releaseMode;
    }

    public String getAppPrivateKey() {
        return appPrivateKey;
    }

    public void setAppPrivateKey(String appPrivateKey) {
        this.appPrivateKey = appPrivateKey;
    }

    public String getAppPublicKey() {
        return appPublicKey;
    }

    public void setAppPublicKey(String appPublicKey) {
        this.appPublicKey = appPublicKey;
    }

    public Set<String> getClientRestriction() {
        return clientRestriction;
    }

    public void setClientRestriction(Set<String> clientRestriction) {
        this.clientRestriction = clientRestriction;
    }

    public Map<String, ConsumerClientConfigInfo> getConsumers() {
        return consumers;
    }

    public void setConsumers(Map<String, ConsumerClientConfigInfo> consumers) {
        this.consumers = consumers;
    }

}
