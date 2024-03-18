package vn.supperapp.apigw.messaging.configs.smsgw;

import java.util.Map;
import java.util.Set;

public class SmsGwProfileInfo {
    private String profileName;
    private String profileMode;
    private String url;
    private String username;
    private String password;
    private String alias;
    private String shortCode;
    private Map<String, Object> othersConfigs;

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getProfileMode() {
        return profileMode;
    }

    public void setProfileMode(String profileMode) {
        this.profileMode = profileMode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public Map<String, Object> getOthersConfigs() {
        return othersConfigs;
    }

    public void setOthersConfigs(Map<String, Object> othersConfigs) {
        this.othersConfigs = othersConfigs;
    }
}
