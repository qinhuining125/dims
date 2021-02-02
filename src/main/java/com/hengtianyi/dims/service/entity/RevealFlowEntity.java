package com.hengtianyi.dims.service.entity;

import com.hengtianyi.common.core.base.BaseEntity;

import java.util.Date;

/**
 * RevealFlow实体类
 * <p>Table: REVEAL_FLOW</p>
 */
public class RevealFlowEntity extends BaseEntity {

    private static final long serialVersionUID = -4317556951811414739L;

    /**
     * id
     */
    private String id;

    /**
     * 关联的举报表单业务表id
     */
    private String revealId;

    /**
     * 接受人员Id
     */
    private String receiveId;

    /**
     * 接收角色id
     */
    private Integer receiveRoleId;

    /**
     * 接受人员
     */
    private String receiveName;

    /**
     * 办结内容
     */
    private String remark;

    /**
     * 状态 0未受理，1已受理，2已知晓
     */
    private Integer state;

    /**
     * 时间
     */
    private Date createTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRevealId() {
        return revealId;
    }

    public void setRevealId(String revealId) {
        this.revealId = revealId;
    }

    public String getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(String receiveId) {
        this.receiveId = receiveId;
    }

    public Integer getReceiveRoleId() {
        return receiveRoleId;
    }

    public void setReceiveRoleId(Integer receiveRoleId) {
        this.receiveRoleId = receiveRoleId;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "RevealFlowEntity";
    }
}
