package com.hengtianyi.dims.service.dto;

import com.hengtianyi.common.core.base.BaseBean;

import java.util.Date;
import java.util.List;

/**
 * @author LY
 */
public class QueryDto extends BaseBean {

  private static final long serialVersionUID = -7878629197396102963L;

  /**
   * 开始
   */
  private int first;
  /**
   * 结束
   */
  private int end;

  /**
   * 查询人
   */
  private String userId;

  /**
   * 当前角色id
   */
  private Integer roleId;

  /**
   * 1巡察办、2联系室
   */
  private Integer authId;
  /**
   * 状态，1已受理，2已办结
   */
  private Integer state;

  /**
   * 当前页码，后端返回数据时，根据实际数据量，对页码进行调整
   */
  private int currentPage = 0;

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
   * 上报类型角色Id， 网格员
   */
  private Integer reportRoleId;

  /**
   * 联络员
   */
  private Integer reportRoleId2;
  /**
   * 上报类型ids
   */
  private String reportIds;

  /**
   * 报送类型状态，全部、有效、无效
   */
  private String reportTypeState;

  /**
   * 上报类型内容
   */
  private String  reportContents;

  private List<String> wgyContentsList;

  private List<String> llyContentsList;



  private Integer receivedRoleId;

  private String receivedUserId;

  private String receivedUserName;

  private String receivedUserAccount;

  private String receivedAreaCode;

  private Date receivedTime;


  /**
   * 上报类型内容。联络员
   */
  private String  reportContents2;

  public String getReportIds() {
    return reportIds;
  }

  public void setReportIds(String reportIds) {
    this.reportIds = reportIds;
  }

  public String getReportContents() {
    return reportContents;
  }

  public void setReportContents(String reportContents) {
    this.reportContents = reportContents;
  }

  public Integer getReportRoleId2() {
    return reportRoleId2;
  }

  public void setReportRoleId2(Integer reportRoleId2) {
    this.reportRoleId2 = reportRoleId2;
  }

  public String getReportContents2() {
    return reportContents2;
  }

  public void setReportContents2(String reportContents2) {
    this.reportContents2 = reportContents2;
  }

  public String getReportTypeState() {
    return reportTypeState;
  }

  public void setReportTypeState(String reportTypeState) {
    this.reportTypeState = reportTypeState;
  }

  public List<String> getWgyContentsList() {
    return wgyContentsList;
  }

  public void setWgyContentsList(List<String> wgyContentsList) {
    this.wgyContentsList = wgyContentsList;
  }

  public List<String> getLlyContentsList() {
    return llyContentsList;
  }

  public void setLlyContentsList(List<String> llyContentsList) {
    this.llyContentsList = llyContentsList;
  }

  public int getFirst() {
    return first;
  }

  public void setFirst(int first) {
    this.first = first;
  }

  public int getEnd() {
    return end;
  }

  public void setEnd(int end) {
    this.end = end;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public Integer getRoleId() {
    return roleId;
  }

  public void setRoleId(Integer roleId) {
    this.roleId = roleId;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public int getCurrentPage() {
    return currentPage;
  }

  public void setCurrentPage(int currentPage) {
    this.currentPage = currentPage;
  }

  public Integer getAuthId() {
    return authId;
  }

  public void setAuthId(Integer authId) {
    this.authId = authId;
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

  public Integer getReportRoleId() {
    return reportRoleId;
  }

  public void setReportRoleId(Integer reportRoleId) {
    this.reportRoleId = reportRoleId;
  }

  public Integer getReceivedRoleId() {
    return receivedRoleId;
  }

  public void setReceivedRoleId(Integer receivedRoleId) {
    this.receivedRoleId = receivedRoleId;
  }

  public String getReceivedUserId() {
    return receivedUserId;
  }

  public void setReceivedUserId(String receivedUserId) {
    this.receivedUserId = receivedUserId;
  }

  public String getReceivedUserName() {
    return receivedUserName;
  }

  public void setReceivedUserName(String receivedUserName) {
    this.receivedUserName = receivedUserName;
  }

  public String getReceivedUserAccount() {
    return receivedUserAccount;
  }

  public void setReceivedUserAccount(String receivedUserAccount) {
    this.receivedUserAccount = receivedUserAccount;
  }

  public String getReceivedAreaCode() {
    return receivedAreaCode;
  }

  public void setReceivedAreaCode(String receivedAreaCode) {
    this.receivedAreaCode = receivedAreaCode;
  }

  public Date getReceivedTime() {
    return receivedTime;
  }

  public void setReceivedTime(Date receivedTime) {
    this.receivedTime = receivedTime;
  }
}
