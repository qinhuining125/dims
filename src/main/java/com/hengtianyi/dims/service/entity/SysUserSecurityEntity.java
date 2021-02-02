package com.hengtianyi.dims.service.entity;

import com.hengtianyi.common.core.util.security.PasswordHashEntity;

/**
 * 用户密文实体
 * <p>Table: sys_user_security</p>
 *
 * @author BBF
 */
public class SysUserSecurityEntity extends PasswordHashEntity {

  private static final long serialVersionUID = -4705286811047532903L;

  /**
   * 用户ID
   */
  private String id;

  /**
   * 最后一次登录时间
   */
  private Long lastLogin;

  /**
   * 最后一次修改时间
   */
  private Long lastEdit;

  public SysUserSecurityEntity() {
    super();
  }

  public SysUserSecurityEntity(PasswordHashEntity entity) {
    super();
    this.setCipherText(entity.getCipherText());
    this.setDkLen(entity.getDkLen());
    this.setIterations(entity.getIterations());
    this.setSalt(entity.getSalt());
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Long getLastLogin() {
    return lastLogin;
  }

  public void setLastLogin(Long lastLogin) {
    this.lastLogin = lastLogin;
  }

  public Long getLastEdit() {
    return lastEdit;
  }

  public void setLastEdit(Long lastEdit) {
    this.lastEdit = lastEdit;
  }

  @Override
  public String toString() {
    return "SysUserSecurityEntity{" +
        "id='" + id + '\'' +
        ", lastLogin=" + lastLogin +
        ", lastEdit=" + lastEdit +
        '}';
  }
}
