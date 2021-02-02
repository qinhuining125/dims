package com.hengtianyi.dims.service.dto;

import com.hengtianyi.common.core.base.BaseBean;

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
   * 上报类型角色Id
   */
  private Integer reportRoleId;

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
}
