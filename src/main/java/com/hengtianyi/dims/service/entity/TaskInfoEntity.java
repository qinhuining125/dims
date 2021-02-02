package com.hengtianyi.dims.service.entity;

import com.hengtianyi.common.core.base.BaseEntity;
import java.util.Date;
import java.util.List;

/**
 * TaskInfo实体类
 * <p>Table: TASK_INFO</p>
 *
 * @author LY
 */
public class TaskInfoEntity extends BaseEntity {


  private static final long serialVersionUID = -7528738187238993843L;
  private String id;

  private String userId;
  /**
   * 用户名
   */
  private String userName;
  private List<TaskImageEntity> img;

  private  String[] imgApp;

  public String[] getImgApp() {
    return imgApp;
  }

  public void setImgApp(String[] imgApp) {
    this.imgApp = imgApp;
  }

  public List<TaskImageEntity> getImg() {
    return img;
  }

  public void setImg(List<TaskImageEntity> img) {
    this.img = img;
  }
  /**
   * 角色id
   */
  private Integer roleId;

  /**
   * 内容
   */
  private String content;

  public void setImages(String images) {
    this.images = images;
  }

  public String getImages() {
    return images;
  }

  /**
   * 图片
   */
  private String images;
  /**
   * 任务时间
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
  private List<TaskFlowEntity> flows;

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

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUserId() {
    return this.userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  /**
   * 获取roleId属性(角色id)
   *
   * @return 角色id
   */
  public Integer getRoleId() {
    return this.roleId;
  }

  /**
   * 设置roleId属性
   *
   * @param roleId 角色id
   */
  public void setRoleId(Integer roleId) {
    this.roleId = roleId;
  }

  /**
   * 获取content属性(内容)
   *
   * @return 内容
   */
  public String getContent() {
    return this.content;
  }

  /**
   * 设置content属性
   *
   * @param content 内容
   */
  public void setContent(String content) {
    this.content = content;
  }

  /**
   * 获取createTime属性(任务时间)
   *
   * @return 任务时间
   */
  public Date getCreateTime() {
    return this.createTime;
  }

  /**
   * 设置createTime属性
   *
   * @param createTime 任务时间
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

  public List<TaskFlowEntity> getFlows() {
    return flows;
  }

  public void setFlows(List<TaskFlowEntity> flows) {
    this.flows = flows;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
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

  @Override
  public String toString() {
    return "TaskInfoEntity";
  }
}
