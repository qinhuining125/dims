package com.hengtianyi.dims.service.dto;

import com.hengtianyi.common.core.base.UserDTO;

/**
 * 用户登录后存入session的实体
 *
 * @author BBF
 */
public class SysUserDTO extends UserDTO {

  private static final long serialVersionUID = 6416730096295541733L;
  /**
   * 登录时间
   */
  private Long loginTime;
  private String sessionId;
  private String ip;
  private String userAgent;

  public Long getLoginTime() {
    return loginTime;
  }

  public void setLoginTime(Long loginTime) {
    this.loginTime = loginTime;
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public String getUserAgent() {
    return userAgent;
  }

  public void setUserAgent(String userAgent) {
    this.userAgent = userAgent;
  }

  @Override
  public String toString() {
    return "SysUserDTO{" +
        "loginTime=" + loginTime +
        ", sessionId='" + sessionId + '\'' +
        ", ip='" + ip + '\'' +
        ", userAgent='" + userAgent + '\'' +
        '}';
  }
}
