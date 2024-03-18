package vn.supperapp.apigw.messaging.configs.firebase;

import java.util.Map;

public class FirebaseConfig {
    private String credentialFile;
    private String databaseUrl;
    private String credentialFileAgent;
    private String databaseUrlAgent;
    private String icon;
    private String sound;
    private long ttl;
    private String color;

    public String getCredentialFile() {
        return credentialFile;
    }

    public void setCredentialFile(String credentialFile) {
        this.credentialFile = credentialFile;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public void setDatabaseUrl(String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }

    public String getCredentialFileAgent() {
        return credentialFileAgent;
    }

    public void setCredentialFileAgent(String credentialFileAgent) {
        this.credentialFileAgent = credentialFileAgent;
    }

    public String getDatabaseUrlAgent() {
        return databaseUrlAgent;
    }

    public void setDatabaseUrlAgent(String databaseUrlAgent) {
        this.databaseUrlAgent = databaseUrlAgent;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
