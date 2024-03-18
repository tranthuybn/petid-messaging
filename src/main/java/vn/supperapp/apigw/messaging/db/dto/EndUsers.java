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
 * @author taink
 */
@Entity
@Table(name = "\"EndUsers\"")
public class EndUsers implements Serializable {

    public static final String TABLE_NAME = "EndUsers";
    public static final String SEQ_NAME = "APP_USER_SEQ";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"Id\"", nullable = false)
    private Long id;

    @Column(name = "\"Msisdn\"", length = 20, nullable = false)
    private String msisdn;

    @Column(name = "\"FullName\"", length = 200)
    private String fullName;

    @Column(name = "\"AllowMultipleDevice\"")
    private Integer allowMultipleDevice;

    @Column(name = "\"AllowChangeDevice\"")
    private Integer allowChangeDevice;

    @Column(name = "\"NumLoginFail\"")
    private Integer numLoginFail;

    @Column(name = "\"CheckLoginFailTime\"")
    private Date checkLoginFailTime;

    @Column(name = "\"LastLoginTime\"")
    private Date lastLoginTime;

    @Column(name = "\"CreateTime\"", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date createTime;

    @Column(name = "\"Status\"")
    private Long status;

    @Column(name = "\"ExpiredToken\"", length = 2000)
    private String expiredToken;

    @Column(name = "\"ClientType\"")
    private Long clientType;

    @Column(name = "\"Language\"", length = 10)
    private String language;

    @Column(name = "\"ConsumerQrcode\"", columnDefinition = "TEXT")
    private String consumerQrcode;

    @Column(name = "\"MerchantQrcode\"", columnDefinition = "TEXT")
    private String merchantQrcode;

    @Column(name = "\"RoleId\"")
    private Long roleId;

    @Column(name = "\"AvatarUrl\"", columnDefinition = "TEXT")
    private String avatarUrl;

    @Column(name = "\"KycStatus\"")
    private Long kycStatus;

    @Column(name = "\"OrgId\"")
    private Long orgId;

    @Column(name = "\"Password\"", length = 200)
    private String password;

    @Column(name = "\"Email\"", length = 50)
    private String email;

    @Column(name = "\"ProvinceId\"")
    private Integer provinceId;

    @Column(name = "\"DistrictId\"")
    private Integer districtId;

    @Column(name = "\"WardId\"")
    private Integer wardId;

    @Column(name = "\"Address\"", length = 50)
    private String address;

    @Column(name = "\"IdNumber\"", length = 50)
    private String idNumber;

    @Column(name = "\"OtpType\"", length = 10)
    private String otpType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getAllowMultipleDevice() {
        return allowMultipleDevice;
    }

    public void setAllowMultipleDevice(Integer allowMultipleDevice) {
        this.allowMultipleDevice = allowMultipleDevice;
    }

    public Integer getAllowChangeDevice() {
        return allowChangeDevice;
    }

    public void setAllowChangeDevice(Integer allowChangeDevice) {
        this.allowChangeDevice = allowChangeDevice;
    }

    public Integer getNumLoginFail() {
        return numLoginFail;
    }

    public void setNumLoginFail(Integer numLoginFail) {
        this.numLoginFail = numLoginFail;
    }

    public Date getCheckLoginFailTime() {
        return checkLoginFailTime;
    }

    public void setCheckLoginFailTime(Date checkLoginFailTime) {
        this.checkLoginFailTime = checkLoginFailTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getExpiredToken() {
        return expiredToken;
    }

    public void setExpiredToken(String expiredToken) {
        this.expiredToken = expiredToken;
    }

    public Long getClientType() {
        return clientType;
    }

    public void setClientType(Long clientType) {
        this.clientType = clientType;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getConsumerQrcode() {
        return consumerQrcode;
    }

    public void setConsumerQrcode(String consumerQrcode) {
        this.consumerQrcode = consumerQrcode;
    }

    public String getMerchantQrcode() {
        return merchantQrcode;
    }

    public void setMerchantQrcode(String merchantQrcode) {
        this.merchantQrcode = merchantQrcode;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Long getKycStatus() {
        return kycStatus;
    }

    public void setKycStatus(Long kycStatus) {
        this.kycStatus = kycStatus;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public Integer getWardId() {
        return wardId;
    }

    public void setWardId(Integer wardId) {
        this.wardId = wardId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getOtpType() {
        return otpType;
    }

    public void setOtpType(String otpType) {
        this.otpType = otpType;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    @Transient
    private int unread;

}
