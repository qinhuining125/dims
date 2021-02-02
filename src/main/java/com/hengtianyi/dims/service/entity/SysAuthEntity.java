package com.hengtianyi.dims.service.entity;

import com.hengtianyi.common.core.base.BaseEntity;

/** 
 * SysAuth实体类
 * <p>Table: SYS_AUTH</p>
 * @author LY
 */
public class SysAuthEntity extends BaseEntity {

  private static final long serialVersionUID = 8582584279948530181L;
  private String id;

  private String authName;

  private String authCode;

  private Integer enable;


  public String getId(){
    return this.id;
  }

  public void setId(String id){
    this.id = id;
  }

  public String getAuthName(){
    return this.authName;
  }

  public void setAuthName(String authName){
    this.authName = authName;
  }

  public String getAuthCode(){
    return this.authCode;
  }

  public void setAuthCode(String authCode){
    this.authCode = authCode;
  }

  public Integer getEnable(){
    return this.enable;
  }

  public void setEnable(Integer enable){
    this.enable = enable;
  }

  @Override
  public String toString() {
    return "SysAuthEntity";
  }
}
