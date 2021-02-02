package com.hengtianyi.dims.service.entity;

import com.hengtianyi.common.core.base.BaseEntity;

/**
 * RelUserArea实体类
 * <p>Table: REL_USER_AREA</p>
 *
 * @author LY
 */
public class RelUserAreaEntity extends BaseEntity {


  private static final long serialVersionUID = -2283416381088356054L;

  /**
   * 用户Id
   */
  private String userId;

  /**
   * 地区编号
   */
  private String areaCode;

  /**
   * 地区名
   */
  private String areaName;

  public String getUserId() {
    return this.userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getAreaCode() {
    return this.areaCode;
  }

  public void setAreaCode(String areaCode) {
    this.areaCode = areaCode;
  }

  public String getAreaName() {
    return areaName;
  }

  public void setAreaName(String areaName) {
    this.areaName = areaName;
  }

  @Override
  public String toString() {
    return "RelUserAreaEntity";
  }
}
