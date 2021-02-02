package com.hengtianyi.dims.cache;

import com.hengtianyi.common.core.util.StringUtil;
import com.hengtianyi.common.core.util.cache.CacheAdvUtil;

/**
 * 模块权限的缓存代理类
 *
 * @author BBF
 */
public final class CacheModuleAuth {

  private static final String CACHE_MODULE_ALL = "cache_module_all";
  private static final String CACHE_MODULE_TREE = "cache_module_tree";
  private static final String CACHE_MODULE_ONE = "cache_module_%s";

  private CacheModuleAuth() {
  }

  /**
   * 缓存全部模块信息的JSON字符串
   *
   * @param cacheData 要缓存的JSON
   */
  public static void putModuleAll(String cacheData) {
    try {
      CacheAdvUtil.put(CACHE_MODULE_ALL, cacheData);
    } catch (Exception ex) {
      // 缓存类，有异常就直接丢弃
    }
  }

  /**
   * 获取缓存的全部模块信息的JSON字符串
   *
   * @return 全部的模块信息的JSON字符串
   */
  public static String getModuleAll() {
    try {
      return CacheAdvUtil.get(CACHE_MODULE_ALL, String.class);
    } catch (Exception ex) {
      // 缓存类，有异常就直接丢弃
    }
    return StringUtil.EMPTY;
  }

  /**
   * 移除缓存
   */
  public static void removeModuleAll() {
    try {
      CacheAdvUtil.remove(CACHE_MODULE_ALL);
    } catch (Exception ex) {
      // 缓存类，有异常就直接丢弃
    }
  }

  /**
   * 缓存单条模块信息的JSON字符串
   *
   * @param mid       模块ID
   * @param cacheData 要缓存的JSON
   */
  public static void putModuleOne(String mid, String cacheData) {
    try {
      CacheAdvUtil.put(String.format(CACHE_MODULE_ONE, mid), cacheData);
    } catch (Exception ex) {
      // 缓存类，有异常就直接丢弃
    }
  }

  /**
   * 获取缓存的单条模块信息的JSON字符串
   *
   * @param mid 模块ID
   * @return 全部的模块信息的JSON字符串
   */
  public static String getModuleOne(String mid) {
    try {
      return CacheAdvUtil.get(String.format(CACHE_MODULE_ONE, mid), String.class);
    } catch (Exception ex) {
      // 缓存类，有异常就直接丢弃
    }
    return StringUtil.EMPTY;
  }

  /**
   * 移除缓存
   *
   * @param mid 模块ID
   */
  public static void removeModuleOne(String mid) {
    try {
      CacheAdvUtil.remove(String.format(CACHE_MODULE_ONE, mid));
    } catch (Exception ex) {
      // 缓存类，有异常就直接丢弃
    }
  }


  /**
   * 移除缓存
   */
  public static void removeModuleTree() {
    try {
      CacheAdvUtil.remove(CACHE_MODULE_TREE);
    } catch (Exception ex) {
      // 缓存类，有异常就直接丢弃
    }
  }

}
