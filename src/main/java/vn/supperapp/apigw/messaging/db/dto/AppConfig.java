package vn.supperapp.apigw.messaging.db.dto;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "APP_CONFIG")
public class AppConfig implements Serializable {
    public static final String TABLE_NAME = "APP_CONFIG";
    public static final String SEQ_NAME = "APP_CONFIG_SEQ";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_config_generator")
    @SequenceGenerator(name = "app_config_generator", allocationSize = 1, sequenceName = "APP_CONFIG_SEQ")
    @Column(name = "ID", nullable = false)
    private Long id;
    @Column(name = "LANGUAGE")
    private String language;
    @Column(name = "FEATURE_CODE")
    private String featureCode;
    @Column(name = "OBJ_TYPE")
    private String objType;
    @Column(name = "OBJ_ID")
    private Long objId;
    @Column(name = "OBJ_VALUE")
    private String objValue;
    @Column(name = "CONFIG_KEY")
    private String configKey;
    @Column(name = "CONFIG_VALUE")
    private String configValue;
    @Column(name = "CONFIG_NAME")
    private String configName;
    @Column(name = "STATUS")
    private Integer status;
    @Column(name = "CREATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime = new Date();
    @Column(name = "UPDATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
    @Column(name = "OBJ_DATA1")
    private String objData1;
    @Column(name = "OBJ_DATA2")
    private String objData2;
    @Column(name = "OBJ_DATA3")
    private String objData3;
    @Column(name = "OBJ_DATA4")
    private String objData4;
    @Column(name = "OBJ_DATA5")
    private String objData5;
    @Column(name = "DESCRIPTION")
    private Integer description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getFeatureCode() {
        return featureCode;
    }

    public void setFeatureCode(String featureCode) {
        this.featureCode = featureCode;
    }

    public String getObjType() {
        return objType;
    }

    public void setObjType(String objType) {
        this.objType = objType;
    }

    public Long getObjId() {
        return objId;
    }

    public void setObjId(Long objId) {
        this.objId = objId;
    }

    public String getObjValue() {
        return objValue;
    }

    public void setObjValue(String objValue) {
        this.objValue = objValue;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getObjData1() {
        return objData1;
    }

    public void setObjData1(String objData1) {
        this.objData1 = objData1;
    }

    public String getObjData2() {
        return objData2;
    }

    public void setObjData2(String objData2) {
        this.objData2 = objData2;
    }

    public String getObjData3() {
        return objData3;
    }

    public void setObjData3(String objData3) {
        this.objData3 = objData3;
    }

    public String getObjData4() {
        return objData4;
    }

    public void setObjData4(String objData4) {
        this.objData4 = objData4;
    }

    public String getObjData5() {
        return objData5;
    }

    public void setObjData5(String objData5) {
        this.objData5 = objData5;
    }

    public Integer getDescription() {
        return description;
    }

    public void setDescription(Integer description) {
        this.description = description;
    }
}
