package com.hengtianyi.dims.cache;

import com.hengtianyi.common.core.constant.BaseConstant;
import com.hengtianyi.common.core.util.cache.CacheUtil;

/**
 * 用户登录的缓存代理类
 *
 * @author BBF
 */
public final class CacheLogin {

  private static final String CACHE_ONE = "cache_module_%s";
  /**
   * 系统配置过期时间，单位秒
   */
  private static final int TIME_OUT = 900;

  private CacheLogin() {
  }

  /**
   * 获取登录账号的锁定时间，秒
   *
   * @param account 登录账号
   * @return 锁定时间，秒
   */
  public static long getExpire(String account) {
    try {
      return CacheUtil.getExpire(String.format(CACHE_ONE, account));
    } catch (Exception ex) {
      // 缓存类，有异常就直接丢弃
    }
    return BaseConstant.NUM_0;
  }

  /**
   * 缓存登录的错误次数
   *
   * @param account 登录账号
   * @param times   错误次数
   */
  public static void setErrorTimes(String account, long times) {
    try {
      CacheUtil.put(String.format(CACHE_ONE, account), times, TIME_OUT);
    } catch (Exception ex) {
      // 缓存类，有异常就直接丢弃
    }
  }

  /**
   * 获取登录账号的锁定时间，秒
   *
   * @param account 登录账号
   * @return 锁定时间，秒
   */
  public static long getErrorTimes(String account) {
    try {
      return (Long) CacheUtil.get(String.format(CACHE_ONE, account));
    } catch (Exception ex) {
      // 缓存类，有异常就直接丢弃
    }
    return BaseConstant.NUM_0;
  }

  /**
   * 登录成功，删除缓存
   *
   * @param account 登录账号
   */
  public static void loginSuccess(String account) {
    try {
      CacheUtil.remove(String.format(CACHE_ONE, account));
    } catch (Exception ex) {
      // 缓存类，有异常就直接丢弃
    }
  }
}
