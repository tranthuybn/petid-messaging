package vn.supperapp.apigw.messaging.restful.models;

import org.slf4j.Logger;
import vn.supperapp.apigw.messaging.beans.AppPushNotificationInfo;
import vn.supperapp.apigw.messaging.utils.CommonUtils;

import java.util.Arrays;
import java.util.Map;


public class MessageRequest extends BaseRequest {
    private String channel;
    private String receiverObj;
    private String refId;
    private String defaultContent;
    private Map<String, String> titles;
    private Map<String, String> contents;
    private String sender;
    private String receiver;
    private AppPushNotificationInfo appPushNotificationInfo;
    private boolean pushNotification;
    private String preferLanguage;
    private boolean saveNotificationLog = true;
    private Long notificationType;
    private String accountId;
    private boolean sendSms = true;
    private boolean sendLastLoginDevice = true;
    private String transactionId;
    private String objType;


    public Map<String, String> getContents() {
        return contents;
    }

    public void setContents(Map<String, String> contents) {
        this.contents = contents;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getReceiverObj() {
        return receiverObj;
    }

    public void setReceiverObj(String receiverObj) {
        this.receiverObj = receiverObj;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getDefaultContent() {
        return defaultContent;
    }

    public void setDefaultContent(String defaultContent) {
        this.defaultContent = defaultContent;
    }

    public Map<String, String> getTitles() {
        return titles;
    }

    public void setTitles(Map<String, String> titles) {
        this.titles = titles;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public AppPushNotificationInfo getAppPushNotificationInfo() {
        return appPushNotificationInfo;
    }

    public void setAppPushNotificationInfo(AppPushNotificationInfo appPushNotificationInfo) {
        this.appPushNotificationInfo = appPushNotificationInfo;
    }

    public boolean isPushNotification() {
        return pushNotification;
    }

    public void setPushNotification(boolean pushNotification) {
        this.pushNotification = pushNotification;
    }

    public String getPreferLanguage() {
        return preferLanguage;
    }

    public void setPreferLanguage(String preferLanguage) {
        this.preferLanguage = preferLanguage;
    }

    public boolean isSaveNotificationLog() {
        return saveNotificationLog;
    }

    public void setSaveNotificationLog(boolean saveNotificationLog) {
        this.saveNotificationLog = saveNotificationLog;
    }

    public Long getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(Long notificationType) {
        this.notificationType = notificationType;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public boolean isSendSms() {
        return sendSms;
    }

    public void setSendSms(boolean sendSms) {
        this.sendSms = sendSms;
    }

    public boolean isSendLastLoginDevice() {
        return sendLastLoginDevice;
    }

    public void setSendLastLoginDevice(boolean sendLastLoginDevice) {
        this.sendLastLoginDevice = sendLastLoginDevice;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getObjType() {
        return objType;
    }

    public void setObjType(String objType) {
        this.objType = objType;
    }

    public String getReceiverContent(String language) {
        if (this.contents == null || this.contents.isEmpty()) {
            return null;
        }

        String tmp = this.contents.get(language);
        if (CommonUtils.isNullOrEmpty(language)) {
            String[] tmps = this.contents.keySet().toArray(new String[]{});
            Arrays.sort(tmps);
            tmp = this.contents.get(tmps[0]);
        }
        return tmp;
    }

    public String getApsTitle(String language) {
        if (this.appPushNotificationInfo == null || this.appPushNotificationInfo.getTitles() == null || this.appPushNotificationInfo.getTitles().isEmpty()) {
            return "VNPet";
        }
        String tmp = this.appPushNotificationInfo.getTitles().get(language);
        if (CommonUtils.isNullOrEmpty(language)) {
            String[] tmps = this.appPushNotificationInfo.getTitles().keySet().toArray(new String[]{});
            Arrays.sort(tmps);
            tmp = this.appPushNotificationInfo.getTitles().get(tmps[0]);
        }
        return tmp;
    }
}
