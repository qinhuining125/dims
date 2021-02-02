package com.hengtianyi.dims.service.entity;

import com.hengtianyi.common.core.base.BaseEntity;

/** 
 * Township实体类
 * <p>Table: TOWNSHIP</p>
 * @author LY
 */
public class TownshipEntity extends BaseEntity {

  private static final long serialVersionUID = -1515463748301572715L;
  private String areaCode;

  private String areaName;


  public String getAreaCode(){
    return this.areaCode;
  }

  public void setAreaCode(String areaCode){
    this.areaCode = areaCode;
  }

  public String getAreaName(){
    return this.areaName;
  }

  public void setAreaName(String areaName){
    this.areaName = areaName;
  }

  @Override
  public String toString() {
    return "TownshipEntity";
  }
}
