package com.hengtianyi.dims.service.entity;

import com.hengtianyi.common.core.base.BaseEntity;

import java.util.Date;

/**
 * PatrolInfoEntity巡察实体类
 * <p>Table: PATROL_INFO</p>
 *
 * @author jyy
 */
public class PatrolInfoEntity extends BaseEntity {

  private static final long serialVersionUID = -7528738187238993843L;
  private String id;

  private String userId;
  /**
   * 巡察名称
   */
  private String patrolName;
  /**
   * 巡察单位
   */
  private String patrolUnit;
  /**
   * 创建时间
   */

  private Date createTime;

  /**
   * 开始时间
   */
  private Date startTime;

  /**
   * 开始时间
   */
  private Date endTime;
  /**
   * 二维码存储路径
   */
  private String imageUrl;


  /**
   * 开始时间
   */
  private String startTime1;
  /**
   * 结束时间
   */
  private String endTime1;


  public PatrolInfoEntity() {
  }


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

  public String getPatrolName() {
    return patrolName;
  }

  public void setPatrolName(String patrolName) {
    this.patrolName = patrolName;
  }


  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  public String getPatrolUnit() {
    return patrolUnit;
  }

  public void setPatrolUnit(String patrolUnit) {
    this.patrolUnit = patrolUnit;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public String getStartTime1() {
    return startTime1;
  }

  public void setStartTime1(String startTime1) {
    this.startTime1 = startTime1;
  }

  public String getEndTime1() {
    return endTime1;
  }

  public void setEndTime1(String endTime1) {
    this.endTime1 = endTime1;
  }

  @Override
  public String toString() {
    return "PatrolInfoEntity";
  }

  //添加一个构造函数
}
