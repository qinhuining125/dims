package com.hengtianyi.dims.aop;

import com.hengtianyi.common.core.util.StringUtil;
import com.hengtianyi.dims.constant.LogEnum;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，用于切面捕捉日志输出
 *
 * @author wangfanjun
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface WebLog {

  /**
   * 描述业务操作
   *
   * @return 业务操作的描述
   */
  String value() default StringUtil.EMPTY;

  /**
   * 操作类型
   *
   * @return 操作类型
   */
  LogEnum type() default LogEnum.COMMON;

  /**
   * 是否做数据脱敏（即不记录输入参数），默认false
   *
   * @return true表示不记录输入参数
   */
  boolean desensitise() default false;
}