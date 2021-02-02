package com.hengtianyi.dims.service.entity;

import com.hengtianyi.common.core.base.BaseEntity;

/** 
 * RelRoleAuth实体类
 * <p>Table: REL_ROLE_AUTH</p>
 * @author LY
 */
public class RelRoleAuthEntity extends BaseEntity {


  private static final long serialVersionUID = 3249337165647345604L;
  private Integer roleId;

  private String authId;


  public Integer getRoleId(){
    return this.roleId;
  }

  public void setRoleId(Integer roleId){
    this.roleId = roleId;
  }

  public String getAuthId(){
    return this.authId;
  }

  public void setAuthId(String authId){
    this.authId = authId;
  }

  @Override
  public String toString() {
    return "RelRoleAuthEntity";
  }
}
