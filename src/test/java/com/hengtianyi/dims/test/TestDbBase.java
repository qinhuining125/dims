package com.hengtianyi.dims.test;

import com.hengtianyi.dims.Application;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 单元测试的基类
 * @author bbf
 */
@RunWith(SpringJUnit4ClassRunner.class)
//指定SpringBoot工程的Application启动类
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public abstract class TestDbBase {
  protected static final Logger LOGGER = LoggerFactory.getLogger(TestDbBase.class);

  @BeforeClass
  public static void init() {
    LOGGER.info("[TestDbBase.init]开始初始化");
  }

}
