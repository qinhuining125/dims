package com.hengtianyi.dims.config;

import freemarker.template.TemplateModelException;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

/**
 * Freemarker增加全局变量
 *
 * @author BBF
 */
@Component
public class FreemarkerConfig {

  @Resource
  private FreeMarkerConfigurer freeMarkerConfigurer;

  @Resource
  private CustomProperties customProperties;

  @PostConstruct
  public void setSharedVariable() throws TemplateModelException {
    // 增加共享变量
    freeMarkerConfigurer.getConfiguration().setSharedVariable("global", customProperties);
  }
}
