package com.hengtianyi.dims.test;

import com.hengtianyi.dims.Application;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * 单元测试的基类
 * @author bbf
 */
@RunWith(SpringJUnit4ClassRunner.class)
// 用来表示测试环境使用的ApplicationContext将是WebApplicationContext类型的
@WebAppConfiguration
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public abstract class TestWebBase {
  protected static final Logger LOGGER = LoggerFactory.getLogger(TestWebBase.class);

  @BeforeClass
  public static void init() {
    LOGGER.info("[TestWebBase.init]开始初始化");
  }

}
