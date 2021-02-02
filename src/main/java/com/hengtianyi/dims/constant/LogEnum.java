package com.hengtianyi.dims.constant;

/**
 * 日志类型枚举
 *
 * @author BBF
 */
public enum LogEnum {

  /**
   * 保存
   */
  SAVE("保存"),

  /**
   * 删除
   */
  DELETE("删除"),

  /**
   * 登录
   */
  LOGIN("登录"),
  /**
   * 修改
   */
  UPDATE("修改"),

  /**
   * 修改密码
   */
  SAVE_PASSWORD("修改密码"),

  /**
   * 修改账号
   */
  SAVE_ACCOUNT("修改账号"),

  /**
   * 未授权
   */
  UNAUTHORIZED("未授权"),

  /**
   * 清空日志
   */
  REMOVE_LOG("清空日志"),

  /**
   * 角色权限
   */
  ADMIN_AUTH("角色权限"),

  /**
   * 管理员用户管理
   */
  ADMIN_USER("用户管理"),

  /**
   * 管理员修改密码
   */
  ADMIN_PASSWORD("管理员修改密码"),

  /**
   * 通用
   */
  COMMON("其他");

  private final String message;

  LogEnum(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
