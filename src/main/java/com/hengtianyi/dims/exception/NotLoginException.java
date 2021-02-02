package com.hengtianyi.dims.exception;

/**
 * 未登录异常类
 *
 * @author BBF
 */
public class NotLoginException extends WebException {

  private static final long serialVersionUID = -5783577397905236215L;

  public NotLoginException() {
    super(ErrorEnum.NOT_LOGIN);
  }

}
