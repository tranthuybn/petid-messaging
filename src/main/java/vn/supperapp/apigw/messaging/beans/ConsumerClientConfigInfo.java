package vn.supperapp.apigw.messaging.beans;

import java.util.List;

public class ConsumerClientConfigInfo {
    private String name;
    private String apiKey;
    private String validity;
    private List<String> ipRestrictions;
    private boolean checkSmsReceiverWhitelist = false;
    private List<String> smsReceiverWhitelist;
    private String smsProfileName;
    private String firebaseProfileName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public List<String> getIpRestrictions() {
        return ipRestrictions;
    }

    public void setIpRestrictions(List<String> ipRestrictions) {
        this.ipRestrictions = ipRestrictions;
    }

    public boolean isCheckSmsReceiverWhitelist() {
        return checkSmsReceiverWhitelist;
    }

    public void setCheckSmsReceiverWhitelist(boolean checkSmsReceiverWhitelist) {
        this.checkSmsReceiverWhitelist = checkSmsReceiverWhitelist;
    }

    public List<String> getSmsReceiverWhitelist() {
        return smsReceiverWhitelist;
    }

    public void setSmsReceiverWhitelist(List<String> smsReceiverWhitelist) {
        this.smsReceiverWhitelist = smsReceiverWhitelist;
    }

    public String getSmsProfileName() {
        return smsProfileName;
    }

    public void setSmsProfileName(String smsProfileName) {
        this.smsProfileName = smsProfileName;
    }

    public String getFirebaseProfileName() {
        return firebaseProfileName;
    }

    public void setFirebaseProfileName(String firebaseProfileName) {
        this.firebaseProfileName = firebaseProfileName;
    }

    public boolean checkReceiverInWhitelist(String receiver) {
        return this.smsReceiverWhitelist != null && this.smsReceiverWhitelist.contains(receiver);
    }
}
