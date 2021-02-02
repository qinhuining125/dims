package com.hengtianyi.dims.service.entity;

import com.hengtianyi.common.core.base.BaseEntity;
import com.hengtianyi.dims.service.dto.KeyValueDto;
import java.util.Date;
import java.util.List;

/**
 * ClueReport实体类
 * <p>Table: CLUE_REPORT</p>
 *
 * @author LY
 */
public class ClueReportEntity extends BaseEntity {


  private static final long serialVersionUID = 7914269882113570329L;
  private String id;

  /**
   * 用户Id
   */
  private String userId;
  /**
   * username
   */
  private String userName;

  /**
   * 上报类型ids
   */
  private String reportIds;

  /**
   * 上报类型内容
   */
  private List<KeyValueDto> dtoList;
  /**
   * 线索描述
   */
  private String clueDescribe;

  /**
   * 时间
   */
  private Date createTime;

  /**
   * 状态，1已受理，2已办结
   */
  private Short state;

  /**
   * 上报类型角色Id
   */
  private Integer reportRoleId;

  /**
   * 线索编号 村名+年份+编号从1开始
   */
  private String clueNo;

  /**
   * 接受人员id
   */
  private String receiveId;
  /**
   * 流程
   */
  private List<ClueFlowEntity> flows;

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


  private String reportUserAreaName;

  public String getReportUserAreaName() {
    return reportUserAreaName;
  }

  public void setReportUserAreaName(String reportUserAreaName) {
    this.reportUserAreaName = reportUserAreaName;
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  /**
   * 获取userId属性(用户Id)
   *
   * @return 用户Id
   */
  public String getUserId() {
    return this.userId;
  }

  /**
   * 设置userId属性
   *
   * @param userId 用户Id
   */
  public void setUserId(String userId) {
    this.userId = userId;
  }

  /**
   * 获取reportIds属性(上报类型ids)
   *
   * @return 上报类型ids
   */
  public String getReportIds() {
    return this.reportIds;
  }

  /**
   * 设置reportIds属性
   *
   * @param reportIds 上报类型ids
   */
  public void setReportIds(String reportIds) {
    this.reportIds = reportIds;
  }

  /**
   * 获取clueDescribe属性(线索描述)
   *
   * @return 线索描述
   */
  public String getClueDescribe() {
    return this.clueDescribe;
  }

  /**
   * 设置clueDescribe属性
   *
   * @param clueDescribe 线索描述
   */
  public void setClueDescribe(String clueDescribe) {
    this.clueDescribe = clueDescribe;
  }

  /**
   * 获取createTime属性(时间)
   *
   * @return 时间
   */
  public Date getCreateTime() {
    return this.createTime;
  }

  /**
   * 设置createTime属性
   *
   * @param createTime 时间
   */
  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  /**
   * 获取state属性(状态，1已受理，2已办结)
   *
   * @return 状态，1已受理，2已办结
   */
  public Short getState() {
    return this.state;
  }

  /**
   * 设置state属性
   *
   * @param state 状态，1已受理，2已办结
   */
  public void setState(Short state) {
    this.state = state;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public List<ClueFlowEntity> getFlows() {
    return flows;
  }

  public void setFlows(List<ClueFlowEntity> flows) {
    this.flows = flows;
  }

  public Integer getReportRoleId() {
    return reportRoleId;
  }

  public void setReportRoleId(Integer reportRoleId) {
    this.reportRoleId = reportRoleId;
  }

  public String getClueNo() {
    return clueNo;
  }

  public void setClueNo(String clueNo) {
    this.clueNo = clueNo;
  }

  public String getReceiveId() {
    return receiveId;
  }

  public void setReceiveId(String receiveId) {
    this.receiveId = receiveId;
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

  public List<KeyValueDto> getDtoList() {
    return dtoList;
  }

  public void setDtoList(List<KeyValueDto> dtoList) {
    this.dtoList = dtoList;
  }

  @Override
  public String toString() {
    return "ClueReportEntity";
  }
}
