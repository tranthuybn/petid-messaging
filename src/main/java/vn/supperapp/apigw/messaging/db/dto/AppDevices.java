/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.supperapp.apigw.messaging.db.dto;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author luand
 */
@Entity
@Table(name = "\"AppDevices\"")
public class AppDevices implements Serializable {

    public static final String TABLE_NAME = "APP_DEVICE";
    public static final String SEQ_NAME = "APP_DEVICE_SEQ";

    public AppDevices() {
    }

    @Id
    @Column(name = "\"Id\"", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "\"EndUserId\"")
    private Long endUserId;

    @Column(name = "\"Msisdn\"")
    private String msisdn;

    @Column(name = "\"DeviceId\"")
    private String deviceId;

    @Column(name = "\"DeviceModel\"")
    private String deviceModel;

    @Column(name = "\"OsName\"")
    private String osName;

    @Column(name = "\"OsVersion\"")
    private String osVersion;

    @Column(name = "\"AppVersion\"")
    private String appVersion;

    @Column(name = "\"CreateTime\"")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime = new Date();

    @Column(name = "\"UpdateTime\"")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    @Column(name = "\"Status\"")
    private Integer status;

    @Column(name = "\"UseBiometric\"")
    private Integer useBiometric = 0;

    @Column(name = "\"ClientType\"")
    private Integer clientType;

    @Column(name = "\"LastLoginTime\"")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginTime;

    @Column(name = "\"LastLogoutTime\"")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLogoutTime;

    @Column(name = "\"LastSyncTime\"")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastSyncTime;

    @Column(name = "\"FirebaseToken\"")
    private String firebaseToken;

    @Column(name = "\"AppPrivateKey\"")
    private String appPrivateKey;

    @Column(name = "\"AppPublicKey\"")
    private String appPublicKey;

    @Column(name = "\"AccessToken\"")
    private String accessToken;

    @Column(name = "\"RefreshToken\"")
    private String refreshToken;

    @Column(name = "\"Language\"")
    private String language;

    @Column(name = "\"SignatureKey\"")
    private String signatureKey;

    @Column(name = "\"ClientPrivateKey\"")
    private String clientPrivateKey;

    @Column(name = "\"ClientPublicKey\"")
    private String clientPublicKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEndUserId() {
        return endUserId;
    }

    public void setEndUserId(Long endUserId) {
        this.endUserId = endUserId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getUseBiometric() {
        return useBiometric;
    }

    public void setUseBiometric(Integer useBiometric) {
        this.useBiometric = useBiometric;
    }

    public Integer getClientType() {
        return clientType;
    }

    public void setClientType(Integer clientType) {
        this.clientType = clientType;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Date getLastLogoutTime() {
        return lastLogoutTime;
    }

    public void setLastLogoutTime(Date lastLogoutTime) {
        this.lastLogoutTime = lastLogoutTime;
    }

    public Date getLastSyncTime() {
        return lastSyncTime;
    }

    public void setLastSyncTime(Date lastSyncTime) {
        this.lastSyncTime = lastSyncTime;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSignatureKey() {
        return signatureKey;
    }

    public void setSignatureKey(String signatureKey) {
        this.signatureKey = signatureKey;
    }

    public String getClientPrivateKey() {
        return clientPrivateKey;
    }

    public void setClientPrivateKey(String clientPrivateKey) {
        this.clientPrivateKey = clientPrivateKey;
    }

    public String getClientPublicKey() {
        return clientPublicKey;
    }

    public void setClientPublicKey(String clientPublicKey) {
        this.clientPublicKey = clientPublicKey;
    }
}
