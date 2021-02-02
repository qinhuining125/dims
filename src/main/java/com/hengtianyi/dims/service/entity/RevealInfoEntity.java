package com.hengtianyi.dims.service.entity;

import com.hengtianyi.common.core.base.BaseEntity;

import java.util.Date;
import java.util.List;

/**
 * RevealInfo实体类
 * <p>Table: REVEAL_INFO</p>
 */

public class RevealInfoEntity extends BaseEntity {


    private static final long serialVersionUID = 7250599166074906650L;

    private String id;

    /**
     * 举报人Id
     */
    private String userId;
    /**
     * 举报人用户名
     */
    private String userName;

    /**
     * 举报人联系方式
     */
    private String contact;

    /**
     * 关联巡察内容的id（巡察轮次，巡察主题，巡察内容）
     */
    private String inspectionId;

    /**
     * 举报方式：匿名还是实名 0:匿名，1实名
     */
    private Short type;

    /**
     * 举报人角色id
     */
    private Integer roleId;

    /**
     * 举报对象
     */
    private String revealTarget;

    /**
     * 举报内容
     */
    private String content;

    /**
     * 举报时间-系统自动生成
     */
    private Date createTime;

    /**
     * 状态，1已受理，2已办结
     */
    private Short state;
    /**
     * 受理人id
     */
    private String acceptUserId;
    /**
     * 受理角色id
     */
    private Integer acceptRoleId;

    /**
     * flows
     */
    private List<RevealFlowEntity> flows;

    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 地区编号
     */
    private String areaCode;


    /**
     * 乡镇
     */
    private String townCode;

    /**
     * 村
     */
    private String villageCode;

    /**
     * imagesCode
     */
    private List<ImageRevealEntity> imgApps;

    //巡察轮次名称
    private String patrolName;

    //镇名称
    private String townName;
    //村名称
    private String villageName;

    private List<String> imgApp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getInspectionId() {
        return inspectionId;
    }

    public void setInspectionId(String inspectionId) {
        this.inspectionId = inspectionId;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRevealTarget() {
        return revealTarget;
    }

    public void setRevealTarget(String revealTarget) {
        this.revealTarget = revealTarget;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Short getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = state;
    }

    public String getAcceptUserId() {
        return acceptUserId;
    }

    public void setAcceptUserId(String acceptUserId) {
        this.acceptUserId = acceptUserId;
    }

    public Integer getAcceptRoleId() {
        return acceptRoleId;
    }

    public void setAcceptRoleId(Integer acceptRoleId) {
        this.acceptRoleId = acceptRoleId;
    }

    public List<RevealFlowEntity> getFlows() {
        return flows;
    }

    public void setFlows(List<RevealFlowEntity> flows) {
        this.flows = flows;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public List<ImageRevealEntity> getImgApps() {
        return imgApps;
    }

    public void setImgApps(List<ImageRevealEntity> imgApps) {
        this.imgApps = imgApps;
    }

    public List<String> getImgApp() {
        return imgApp;
    }

    public void setImgApp(List<String> imgApp) {
        this.imgApp = imgApp;
    }

    public String getTownCode() {
        return townCode;
    }

    public void setTownCode(String townCode) {
        this.townCode = townCode;
    }

    public String getVillageCode() {
        return villageCode;
    }

    public void setVillageCode(String villageCode) {
        this.villageCode = villageCode;
    }

    public RevealInfoEntity() {
    }

    public String getPatrolName() {
        return patrolName;
    }

    public void setPatrolName(String patrolName) {
        this.patrolName = patrolName;
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    @Override
    public String toString() {
        return "RevealInfoEntity";
    }
}
