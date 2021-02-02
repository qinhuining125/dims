package com.hengtianyi.dims.service.dto;

import com.hengtianyi.common.core.base.BaseBean;

/**
 * 用户密码
 *
 * @author LY
 */
public class UserPasDto extends BaseBean {

  private static final long serialVersionUID = -7997527862305717738L;

  /**
   * 旧密码
   */
  private String oldPassword;
  /**
   * 新密码
   */
  private String newPassword;

  public String getOldPassword() {
    return oldPassword;
  }

  public void setOldPassword(String oldPassword) {
    this.oldPassword = oldPassword;
  }

  public String getNewPassword() {
    return newPassword;
  }

  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }
}
