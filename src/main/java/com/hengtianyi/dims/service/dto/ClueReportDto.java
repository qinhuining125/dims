package com.hengtianyi.dims.service.dto;

import com.hengtianyi.common.core.base.BaseBean;
import com.hengtianyi.dims.service.entity.ClueFlowEntity;

import java.util.Date;
import java.util.List;

/**
 * @author
 */
public class ClueReportDto extends BaseBean {

  private static final long serialVersionUID = 5017373809545239291L;



  //图片接收
  private List<String> imageArray;


  //语音接收
  private List<String> audioArray;


  //视频接收
  private List<String> videoArray;

  /**
   * 上报类型ids
   */
  private String reportIds;

  /**
   * 上报类型内容
   */
  private String reportContents;

  /**
   * 上报类型状态，全部、有效、无效
   */
  private String reportTypeState;

  /**
   * 上报类型内容
   */
  private List<KeyValueDto> dtoList;
  /**
   * 线索描述
   */
  private String clueDescribe;

  /**
   * 上报给村干部
   */
  private String toVillageMgr;

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

  //新指派（转办）给的人的角色
  private int roleId;

  //处理人的角色
  private int receivedRoleId;

  /**
   * 网格员上报类型ids
   */
  private String reportIds1;
  /**
   * 联络员上报类型ids
   */
  private String reportIds2;

  public String getReportIds1() {
    return reportIds1;
  }

  public void setReportIds1(String reportIds1) {
    this.reportIds1 = reportIds1;
  }

  public String getReportIds2() {
    return reportIds2;
  }

  public void setReportIds2(String reportIds2) {
    this.reportIds2 = reportIds2;
  }


  public String getReportContents() {
    return reportContents;
  }

  public void setReportContents(String reportContents) {
    this.reportContents = reportContents;
  }

  public String getReportTypeState() {
    return reportTypeState;
  }

  public void setReportTypeState(String reportTypeState) {
    this.reportTypeState = reportTypeState;
  }

  public String getReportUserAreaName() {
    return reportUserAreaName;
  }

  public void setReportUserAreaName(String reportUserAreaName) {
    this.reportUserAreaName = reportUserAreaName;
  }

  public int getRoleId() {
    return roleId;
  }

  public void setRoleId(int roleId) {
    this.roleId = roleId;
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

  public String getToVillageMgr() {
    return toVillageMgr;
  }

  public void setToVillageMgr(String toVillageMgr) {
    this.toVillageMgr = toVillageMgr;
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

  public List<String> getImageArray() {
    return imageArray;
  }

  public void setImageArray(List<String> imageArray) {
    this.imageArray = imageArray;
  }

  public List<String> getAudioArray() {
    return audioArray;
  }

  public void setAudioArray(List<String> audioArray) {
    this.audioArray = audioArray;
  }

  public List<String> getVideoArray() {
    return videoArray;
  }

  public void setVideoArray(List<String> videoArray) {
    this.videoArray = videoArray;
  }


  public int getReceivedRoleId() {
    return receivedRoleId;
  }

  public void setReceivedRoleId(int receivedRoleId) {
    this.receivedRoleId = receivedRoleId;
  }
}
