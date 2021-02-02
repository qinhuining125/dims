package com.hengtianyi.dims.config;

import com.hengtianyi.common.core.constant.BaseConstant;
import com.hengtianyi.common.core.util.StringUtil;
import com.hengtianyi.dims.aop.LogInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.*;

/**
 * 自定义拦截器
 *
 * @author BBF
 */
@Configuration
public class MyWebMvcConfig implements WebMvcConfigurer{

  @Autowired
  private CustomProperties customProperties;

  /**
   * 拦截器配置
   *
   * @param registry InterceptorRegistry
   */
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new LogInterceptor()).addPathPatterns("/**")
        .excludePathPatterns("/static/**","/town","/village1","/reporting");
  }



  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
//    registry.addResourceHandler("/upload/**").addResourceLocations("file:D:/upload/");
//    registry.addResourceHandler("").addResourceLocations("file:" + customProperties.getUploadPath() + "/");
    registry.addResourceHandler("/upload/**").addResourceLocations("file:" + customProperties.getUploadPath() + "/");

  }

  /**
   * 跨域CORS配置
   *
   * @param registry CorsRegistry
   */
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/**")
        .allowedHeaders(StringUtil.ASTERISK)
        .allowedOrigins(StringUtil.ASTERISK)
        .allowedMethods("OPTIONS", "GET", "POST")
        .exposedHeaders(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
            HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,
            HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
            HttpHeaders.ACCESS_CONTROL_MAX_AGE,
            "X-Frame-Options", "token")
        .allowCredentials(false)
        .maxAge(BaseConstant.SS_IN_HOUR);
  }
}