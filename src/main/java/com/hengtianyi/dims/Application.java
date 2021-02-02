package com.hengtianyi.dims;

import com.hengtianyi.common.core.constant.BaseConstant;
import com.hengtianyi.common.core.feature.FastJsonAutoTypeConfig;
import com.hengtianyi.dims.aop.JwtFilter;
import com.hengtianyi.dims.aop.LoginFilter;
import com.hengtianyi.dims.config.CustomProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

/**
 * SpringBoot 入口类
 *
 * @author BBF
 */
@SpringBootApplication
@ImportResource(locations = {"classpath:config/conf.xml"})
@PropertySource(encoding = BaseConstant.UTF8, value = {"classpath:config/custom.properties"})
@EnableConfigurationProperties({CustomProperties.class})
public class Application extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Profile(value = {"war"})
  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return builder.sources(Application.class);
  }

  /**
   * 添加FastJson的autoType白名单,Redis缓存使用自定义的FastJson时使用
   *
   * @return FastJsonAutoTypeConfig
   * @see <a href="https://github.com/alibaba/fastjson/wiki/enable_autotype">添加autoType白名单</a>
   */
  @Bean
  public FastJsonAutoTypeConfig fastJsonAutoTypeConfig() {
    return new FastJsonAutoTypeConfig("com.hengtianyi");
  }

  /**
   * 配置登录过滤器
   *
   * @return FilterRegistrationBean
   */
  @Bean
  public FilterRegistrationBean<LoginFilter> loginFilter() {
    FilterRegistrationBean<LoginFilter> registration = new FilterRegistrationBean<>();
    registration.setFilter(new LoginFilter());
    registration.addUrlPatterns("/", "/index.html", "/welcome.html", "/a/*");
    registration.setName("loginFilter");
    return registration;
  }

  /**
   * 配置JWT过滤器
   *
   * @return FilterRegistrationBean
   */
  @Bean
  public FilterRegistrationBean<JwtFilter> jwtFilter() {
    FilterRegistrationBean<JwtFilter> registration = new FilterRegistrationBean<>();
    registration.setFilter(new JwtFilter());
    registration.addUrlPatterns("/api/*");
    registration.setName("jwtFilter");
    return registration;
  }

  /**
   * 配置CSRF过滤器
   *
   * @return {@link org.springframework.boot.web.servlet.FilterRegistrationBean}
   */
  @Bean
  public FilterRegistrationBean<CsrfFilter> csrfFilter() {
    FilterRegistrationBean<CsrfFilter> filter = new FilterRegistrationBean<>();
    filter.setFilter(new CsrfFilter(new HttpSessionCsrfTokenRepository()));
    filter.addUrlPatterns("/sys/*");
    filter.setName("csrfFilter");
    return filter;
  }
}
