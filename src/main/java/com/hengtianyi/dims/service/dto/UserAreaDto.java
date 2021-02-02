package com.hengtianyi.dims.service.dto;

import com.hengtianyi.common.core.base.BaseBean;
import java.util.List;

/**
 * @author Administrator
 */
public class UserAreaDto extends BaseBean {

  private static final long serialVersionUID = 1085963252938092279L;
  /**
   * 主键ID
   */
  private String userId;
  /**
   * 用户昵称
   */
  private String userName;
  /**
   * 区域编码
   */
  private String areaCode;

  /**
   * 区域名称
   */
  private String areaName;

  /**
   * 角色Id
   */
  private Integer roleId;

  /**
   * 子用户
   */
  private List<UserAreaDto> childs;

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getAreaCode() {
    return areaCode;
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

  public Integer getRoleId() {
    return roleId;
  }

  public void setRoleId(Integer roleId) {
    this.roleId = roleId;
  }

  public List<UserAreaDto> getChilds() {
    return childs;
  }

  public void setChilds(List<UserAreaDto> childs) {
    this.childs = childs;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }
}
