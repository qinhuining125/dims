package com.hengtianyi.dims.constant;

import org.springframework.beans.factory.annotation.Value;

/**
 * 框架的常量类
 *
 * @author BBF
 */
public final class FrameConstant {

  /**
   * 错误信息的常量
   */
  public static final String MESSAGE = "message";
  public static final String DESCRIBE = "describe";
  public static final String EXCEPTION = "exception";
  public static final String SHOW_STACK_TRACE = "showStackTrace";
  public static final String TRACE = "trace";

//  public static final String PREFIX_URL = "http://localhost:81/";


  public static final String PREFIX_URL = "http://183.201.252.83:49012/";

    public static final String PREFIX_URL2 = "http://183.201.252.83:49012";

//  public static final String PREFIX_URL = "http://127.0.0.1:81/";

//  public static final String PREFIX_URL2 = "http://127.0.0.1:81";


//  public static final String PREFIX_URL = "http://192.168.1.112:81/";
//
//  public static final String PREFIX_URL2 = "http://192.168.1.112:81";



//  public static final String PREFIX_URL = "http://192.168.32.240:81/";

//  public static final String PREFIX_URL2 = "http://192.168.32.240:81";



  public static final String PIC_STATIC= "upload";

  /**
   * 视图，login
   */
  public static final String VIEW_LOGIN = "login";
  /**
   * 视图，errors
   */
  public static final String VIEW_ERROR = "errors";
  /**
   * 跳转到Index
   */
  public static final String REDIRECT_INDEX = "redirect:/index.html";
  /**
   * 启用
   */
  public static final int ENABLED = 1;
  /**
   * 禁用
   */
  public static final int DISABLED = 0;
  /**
   * 启用和禁用
   */
  public static final int ALL = 999;
  /**
   * 成功
   */
  public static final int SUCCESS = 1;
  /**
   * 失败
   */
  public static final int FAILURE = 0;

  /**
   * 每页数量
   */
  public static final int PAGE_SIZE = 10;

  private FrameConstant() {
    super();
  }

}
