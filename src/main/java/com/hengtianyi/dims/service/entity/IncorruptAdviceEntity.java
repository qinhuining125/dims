package com.hengtianyi.dims.service.entity;

import com.hengtianyi.common.core.base.BaseEntity;
import java.util.Date;

/**
 * IncorruptAdvice实体类
 * <p>Table: incorrupt_advice</p>
 *
 * @author LY
 */
public class IncorruptAdviceEntity extends BaseEntity {

  private static final long serialVersionUID = -2842431694883319857L;
  private String id;

  /**
   * 用户Id
   */
  private String userId;

  /**
   * 建议内容
   */
  private String content;

  /**
   * 时间
   */
  private Date createTime;
  /**
   * 用户名
   */
  private String userName;
  /**
   * 角色id
   */
  private Integer roleId;
  /**
   * 角色名
   */
  private String roleName;

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  /**
   * 获取userId属性(用户Id)
   *
   * @return 用户Id
   */
  public String getUserId() {
    return this.userId;
  }

  /**
   * 设置userId属性
   *
   * @param userId 用户Id
   */
  public void setUserId(String userId) {
    this.userId = userId;
  }

  /**
   * 获取content属性(建议内容)
   *
   * @return 建议内容
   */
  public String getContent() {
    return this.content;
  }

  /**
   * 设置content属性
   *
   * @param content 建议内容
   */
  public void setContent(String content) {
    this.content = content;
  }

  /**
   * 获取createTime属性(时间)
   *
   * @return 时间
   */
  public Date getCreateTime() {
    return this.createTime;
  }

  /**
   * 设置createTime属性
   *
   * @param createTime 时间
   */
  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
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

  @Override
  public String toString() {
    return "IncorruptAdviceEntity";
  }
}
