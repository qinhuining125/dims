package com.hengtianyi.dims.exception;

/**
 * 异常类
 *
 * @author BBF
 */
public enum ErrorEnum {

  /**
   * 系统错误
   */
  SYS("1000", "系统错误"),

  /**
   * 验证码错误
   */
  VERIFICATION_CODE("1101", "验证码错误"),

  /**
   * 密码是空的
   */
  NO_PASSWORD("1102", "密码错误"),

  /**
   * 用户未注册
   */
  NO_USER("1103", "用户未注册"),

  /**
   * 密码错误
   */
  PASSWORD_INVALID("1104", "密码校验错误"),

  /**
   * 登录账户被禁用
   */
  USER_DISABLED("1105", "登录账户被禁用"),

  /**
   * 密码不能为空
   */
  PASSWORD_REQUIRED("1106", "密码不能为空"),

  /**
   * 授权出错
   */
  AUTHORIZE_PARAMETER("1200", "授权出错了，参数不正确"),

  /**
   * 权限已锁定
   */
  AUTHORIZE_LOCKED("1201", "权限已锁定，禁止删除"),

  /**
   * 数据库错误
   */
  DATABASE("3000", "数据库错误"),

  /**
   * 更新密码出错
   */
  PASSWORD_UPDATE("3001", "更新密码出错"),

  /**
   * 未登录或登录超时
   */
  NOT_LOGIN("4001", "未登录或登录超时"),

  /**
   * 查询不到数据
   */
  NO_DATA("5000", "查询不到数据"),

  /**
   * 未知错误
   */
  UNDEFINE("-1", "未知错误");

  private final String code;
  private final String message;

  ErrorEnum(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }
}
