package com.hengtianyi.dims;

import com.hengtianyi.common.core.area.AreaUtil;
import com.hengtianyi.common.core.util.CollectionUtil;
import com.hengtianyi.common.core.util.JsonUtil;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AreaUtil测试类
 * @author BBF
 */
public class AreaUtilTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(AreaUtilTest.class);

  /**
   * 地区编码合并
   */
  @Test
  public void getAllCode() {
    String[] arr = {"370201", "370202", "370203"};
    LOGGER.info("getAllCode - {}", AreaUtil.getAllCode(arr));
  }

  /**
   * 获取地区
   */
  @Test
  public void getArea() {
    Map<String, String> val = AreaUtil.getArea();
    LOGGER.info("getArea size={}", val.size());
    Assert.assertTrue(!CollectionUtil.isEmpty(val));
  }

  /**
   * 根据地区编码获取地区名称
   */
  @Test
  public void getAreaNameByCode() {
    String val = AreaUtil.getAreaNameByCode("110115");
    Assert.assertEquals("大兴区", val);
  }

  /**
   * 根据地区编码获取省市地信息
   */
  @Test
  public void getAreaByCode() {
    String[] val = AreaUtil.getAreaByCode("370201");
    LOGGER.info("getAreaByCode json={}", JsonUtil.toJson(val));
    Assert.assertEquals("青岛市", val[1]);
  }

  /**
   * 获取下级地区编码
   */
  @Test
  public void getAreaChildByCode() {
    List<String> val = AreaUtil.getAreaChildByCode("370000");
    LOGGER.info("getAreaChildByCode json={}", JsonUtil.toJson(val));
    Assert.assertEquals(17, val.size());
  }

  /**
   * 获取下级地区编码
   */
  @Test
  public void getAreaChildByCode1() {
    List<String> val = AreaUtil.getAreaChildByCode("370200");
    LOGGER.info("getAreaChildByCode json={}", JsonUtil.toJson(val));
    Assert.assertEquals(12, val.size());
  }

  /**
   * 获取下级地区编码
   */
  @Test
  public void getAreaChildByCode2() {
    // 最末级，无集合
    List<String> val = AreaUtil.getAreaChildByCode("370201");
    Assert.assertTrue(CollectionUtil.isEmpty(val));
  }

  /**
   * 获取下级地区编码
   */
  @Test
  public void getAreaChildByCode3() {
    // 在指定的集合中，找到code的下级
    List<String> val = AreaUtil.getAreaChildByCode(",,370200,,101010,,370202,,370201,,110117,,", "370200");
    LOGGER.info("getAreaChildByCode json={}", JsonUtil.toJson(val));
    Assert.assertEquals(2, val.size());
  }
}