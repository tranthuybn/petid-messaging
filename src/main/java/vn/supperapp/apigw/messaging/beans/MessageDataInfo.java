package vn.supperapp.apigw.messaging.beans;

import vn.supperapp.apigw.messaging.db.dto.EndUsers;

import java.util.List;
import java.util.Map;

public class MessageDataInfo {
    private Long messageLogId;
    private String refId;
    private String sender;
    private String receiver;
    private String contentType;
    private String content;
    private String contentLanguage;
    private boolean isUnicode;
    private boolean sendSms;

    private String apsTitle;
    private List<EndUsers> appUsers;
    private Map<String, String> apsData;

    private Map<String, String> transId;

    private String channel;

    public Long getMessageLogId() {
        return messageLogId;
    }

    public void setMessageLogId(Long messageLogId) {
        this.messageLogId = messageLogId;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
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

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentLanguage() {
        return contentLanguage;
    }

    public void setContentLanguage(String contentLanguage) {
        this.contentLanguage = contentLanguage;
    }

    public boolean isUnicode() {
        return isUnicode;
    }

    public void setUnicode(boolean unicode) {
        isUnicode = unicode;
    }

    public boolean isSendSms() {
        return sendSms;
    }

    public void setSendSms(boolean sendSms) {
        this.sendSms = sendSms;
    }

    public String getApsTitle() {
        return apsTitle;
    }

    public void setApsTitle(String apsTitle) {
        this.apsTitle = apsTitle;
    }

    public List<EndUsers> getAppUsers() {
        return appUsers;
    }

    public void setAppUsers(List<EndUsers> appUsers) {
        this.appUsers = appUsers;
    }

    public Map<String, String> getApsData() {
        return apsData;
    }

    public void setApsData(Map<String, String> apsData) {
        this.apsData = apsData;
    }

    public Map<String, String> getTransId() {
        return transId;
    }

    public void setTransId(Map<String, String> transId) {
        this.transId = transId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
