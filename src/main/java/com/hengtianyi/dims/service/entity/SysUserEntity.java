package com.hengtianyi.dims.service.entity;

import com.hengtianyi.dims.service.dto.KeyValueDto;
import com.hengtianyi.dims.service.dto.SysUserDTO;
import org.springframework.security.core.Transient;

import java.util.List;
import java.util.Map;

/**
 * SysUser实体类
 * <p>Table: sys_user</p>
 *
 * @author LY
 */
public class SysUserEntity extends SysUserDTO {

  private static final long serialVersionUID = -289298095500738231L;
  /**
   * 主键ID
   */
  private String id;

  /**
   * 登录账号
   */
  private String userAccount;

  /**
   * 用户昵称
   */
  private String userName;

  /**
   * 状态
   */
  private Integer enabled;

  /**
   * 性别
   */
  private String sex;

  /**
   * 电话
   */
  private String phone;

  private String townCode;

  /**
   * 区域编码
   */
  private String areaCode;
  /**
   * 角色Id
   */
  private Integer roleId;
  /**
   * 区域名称
   */
  private String areaName;

  /**
   * 身份证号
   */
  private String idCard;

  /**
   * 备注
   */
  private String remark;

  /**
   * 1巡察办、2联系室
   */
  private Integer authId;
  /**
   * 创建时间
   */
  private Long createTime;

  public String getTownCode() {
    return townCode;
  }

  public void setTownCode(String townCode) {
    this.townCode = townCode;
  }


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUserAccount() {
    return userAccount;
  }

  public void setUserAccount(String userAccount) {
    this.userAccount = userAccount;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public Integer getEnabled() {
    return enabled;
  }

  public void setEnabled(Integer enabled) {
    this.enabled = enabled;
  }

  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getAreaCode() {
    return areaCode;
  }

  public void setAreaCode(String areaCode) {
    this.areaCode = areaCode;
  }

  public Integer getRoleId() {
    return roleId;
  }

  public void setRoleId(Integer roleId) {
    this.roleId = roleId;
  }

  public String getAreaName() {
    return areaName;
  }

  public void setAreaName(String areaName) {
    this.areaName = areaName;
  }

  public String getIdCard() {
    return idCard;
  }

  public void setIdCard(String idCard) {
    this.idCard = idCard;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public Long getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Long createTime) {
    this.createTime = createTime;
  }

  public Integer getAuthId() {
    return authId;
  }

  public void setAuthId(Integer authId) {
    this.authId = authId;
  }

  @Override
  public String toString() {
    return "SysUserEntity";
  }
}
