package com.hengtianyi.dims.service.dto;

import com.hengtianyi.common.core.base.BaseBean;

/**
 * app接口验证实体
 *
 * @author LY
 */
public class AppUserDto extends BaseBean {

  private static final long serialVersionUID = -117237146431434827L;
  /**
   * ID
   */
  private String id;

  /**
   * 用户登录账号
   */
  private String account;

  /**
   * 用户名
   */
  private String userName;

  /**
   * token
   */
  private String token;

  /**
   * 区域编码
   */
  private Long areaCode;
  /**
   * 角色Id
   */
  private Integer roleId;

  /**
   * 角色名称
   */
  private String roleName;
  /**
   * 1巡察办、2联系室
   */
  private Integer authId;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Long getAreaCode() {
    return areaCode;
  }

  public void setAreaCode(Long areaCode) {
    this.areaCode = areaCode;
  }

  public Integer getRoleId() {
    return roleId;
  }

  public void setRoleId(Integer roleId) {
    this.roleId = roleId;
  }

  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  public Integer getAuthId() {
    return authId;
  }

  public void setAuthId(Integer authId) {
    this.authId = authId;
  }
}
