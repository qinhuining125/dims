package com.hengtianyi.dims.exception;

import com.hengtianyi.common.core.feature.BusinessException;

/**
 * 简易WEB异常类
 *
 * @author BBF
 */
public class WebException extends BusinessException {

  private static final long serialVersionUID = -7953221920049947034L;
  private final ErrorEnum err;

  public WebException(String message) {
    super(message);
    this.err = ErrorEnum.SYS;
    super.setCode(this.err.getCode());
  }

  public WebException(ErrorEnum error) {
    super(error.getMessage());
    this.err = error;
    super.setCode(this.err.getCode());
  }

}
