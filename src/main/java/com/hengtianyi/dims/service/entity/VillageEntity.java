package com.hengtianyi.dims.service.entity;

import com.hengtianyi.common.core.base.BaseEntity;

/**
 * Village实体类
 * <p>Table: VILLAGE</p>
 *
 * @author LY
 */
public class VillageEntity extends BaseEntity {


  private static final long serialVersionUID = 7309225082382078095L;

  private String userId;

  private String areaCode;

  private Integer sortNo;

  private String areaName;

  public String getUserId() {
    return userId;
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

  public Integer getSortNo() {
    return this.sortNo;
  }

  public void setSortNo(Integer sortNo) {
    this.sortNo = sortNo;
  }

  public String getAreaName() {
    return this.areaName;
  }

  public void setAreaName(String areaName) {
    this.areaName = areaName;
  }

  @Override
  public String toString() {
    return "VillageEntity";
  }
}
