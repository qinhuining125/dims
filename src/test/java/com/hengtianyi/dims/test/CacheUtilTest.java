package com.hengtianyi.dims.test;

import com.hengtianyi.common.core.base.NameValueDTO;
import com.hengtianyi.common.core.feature.ServiceResult;
import com.hengtianyi.common.core.util.cache.CacheUtil;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通用缓存工具测试类
 * @author BBF
 */
public class CacheUtilTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(CacheUtilTest.class);

  @Test
  public void getName() {
    Assert.assertEquals("commonCache", CacheUtil.getName());
  }

  @Test
  public void put1() {
    String key = "putTest";
    String value = "putTestValue汉字ABC";
    CacheUtil.put(key, value);
    String getValue = CacheUtil.get(key, String.class);
    LOGGER.info("获取{}缓存：{}", CacheUtil.getName(), getValue);
    Assert.assertEquals(getValue, value);
  }

  @Test
  public void get1() {
    String key = "putTest";
    ServiceResult<NameValueDTO> result = new ServiceResult<>();
    result.setResult(new NameValueDTO("a", "b"));
    result.setSuccess(true);
    CacheUtil.put(key, result);
    ServiceResult getValue = CacheUtil.get(key, ServiceResult.class);
    LOGGER.info("获取{}缓存：{}", CacheUtil.getName(), getValue);
  }

  @Test
  public void evict() {
    String key = "putTest";
    String value = "putTestValue汉字ABC";
    CacheUtil.put(key, value);
    LOGGER.info("获取{}缓存1：{}", CacheUtil.getName(), CacheUtil.get(key));
    CacheUtil.remove(key);
    Assert.assertNull(CacheUtil.get(key));
  }

  @Test
  public void clear() {
    String key = "putTest";
    String value = "putTestValue汉字ABC";
    String key2 = "putTest2";
    String value2 = "adfsdfsf";
    CacheUtil.put(key, value);
    CacheUtil.put(key2, value2);
    LOGGER.info("获取{}缓存1：{}", CacheUtil.getName(), CacheUtil.get(key));
    LOGGER.info("获取{}缓存2：{}", CacheUtil.getName(), CacheUtil.get(key2));
    CacheUtil.clear();
    Assert.assertNull(CacheUtil.get(key));
  }
}