package vn.supperapp.apigw.messaging.db.dto;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "\"NotificationLog\"")
public class NotificationLog implements Serializable {
    public static final String TABLE_NAME = "NOTIFICATION_LOG";
    public static final String SEQ_NAME = "NOTIFICATION_LOG_SEQ";

    @Id
    @Column(name = "\"Id\"", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_log_generator")
    @SequenceGenerator(name = "notification_log_generator", allocationSize = 1, sequenceName = "NOTIFICATION_LOG_SEQ")
    private Long id;
    @Column(name = "\"RefId\"")
    private String refId;

    @Column(name = "\"EndUserId\"")
    private Long endUserId;

    @Column(name = "\"Msisdn\"", nullable = false)
    private String msisdn;

    @Column(name = "\"Status\"")
    private Long status; //0: New; 1: Read

    @Column(name = "\"ClientType\"", nullable = false)
    private int clientType = -1;

    @Column(name = "\"ObjType\"")
    private String objType;

    @Column(name = "\"NotificationType\"")
    private Long notificationType;

    @Column(name = "\"Title\"")
    private String title; //JSON data (localizable)

    @Column(name = "\"ShortContent\"")
    private String shortContent; //JSON data (localizable)

    @Column(name = "\"FullContent\"")
    private String fullContent; //JSON data (localizable)

    @Column(name = "\"CreateTime\"")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime = new Date();

    @Column(name = "\"UpdateTime\"")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    @Column(name = "\"DeepLink\"")
    private String deepLink;

    @Column(name = "\"RefNotiId\"")
    private String refNotiId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
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

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public int getClientType() {
        return clientType;
    }

    public void setClientType(int clientType) {
        this.clientType = clientType;
    }

    public String getObjType() {
        return objType;
    }

    public void setObjType(String objType) {
        this.objType = objType;
    }

    public Long getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(Long notificationType) {
        this.notificationType = notificationType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortContent() {
        return shortContent;
    }

    public void setShortContent(String shortContent) {
        this.shortContent = shortContent;
    }

    public String getFullContent() {
        return fullContent;
    }

    public void setFullContent(String fullContent) {
        this.fullContent = fullContent;
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

    public String getDeepLink() {
        return deepLink;
    }

    public void setDeepLink(String deepLink) {
        this.deepLink = deepLink;
    }

    public String getRefNotiId() {
        return refNotiId;
    }

    public void setRefNotiId(String refNotiId) {
        this.refNotiId = refNotiId;
    }

    public NotificationLog() {
    }
}
