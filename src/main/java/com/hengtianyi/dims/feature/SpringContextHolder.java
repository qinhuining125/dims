package com.hengtianyi.dims.feature;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 以静态变量保存Spring ApplicationContext, 可在任何代码任何地方任何时候取出ApplicationContext
 * <p>目的：普通类调用Spring bean对象</p>
 * <p>当启动web服务容器的时候，就将ApplicationContext注入到spring工具类的一个静态属性中，
 * 这样普通类就可以通过工具类获取ApplicationContext，从而通过getBean(...)获取bean对象</p>
 *
 * @author BBF
 */
@Component
public final class SpringContextHolder implements ApplicationContextAware, DisposableBean {

  private static ApplicationContext context = null;

  /**
   * 取得存储在静态变量中的ApplicationContext
   *
   * @return Spring，ApplicationContext对象
   */
  public static synchronized ApplicationContext getApplicationContext() {
    assertContextInjected();
    return context;
  }

  /**
   * 实现ApplicationContextAware接口, 注入Context到静态变量中
   */
  @Override
  public synchronized void setApplicationContext(final ApplicationContext applicationContext) {
    if (SpringContextHolder.context == null) {
      SpringContextHolder.context = applicationContext;
    }
  }

  /**
   * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型
   *
   * @param <T>    泛型
   * @param beanId Bean的beanId
   * @return Bean实例
   */
  @SuppressWarnings("unchecked")
  public static synchronized <T> T getBean(final String beanId) {
    assertContextInjected();
    return (T) context.getBean(beanId);
  }

  /**
   * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型
   *
   * @param <T>   泛型
   * @param clazz Bean的class
   * @return Bean实例
   */
  public static synchronized <T> T getBean(final Class<T> clazz) {
    assertContextInjected();
    return context.getBean(clazz);
  }

  /**
   * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型
   *
   * @param <T>    泛型
   * @param beanId Bean的beanId
   * @param clazz  Bean的class
   * @return Bean实例
   */
  public static synchronized <T> T getBean(final String beanId, final Class<T> clazz) {
    assertContextInjected();
    return context.getBean(beanId, clazz);
  }

  /**
   * 清除SpringContextHolder中的ApplicationContext为Null
   */
  public static void clearHolder() {
    context = null;
  }

  /**
   * 检查ApplicationContext不为空
   */
  private static void assertContextInjected() {
    if (context == null) {
      throw new IllegalStateException(
          "ApplicationContext属性未注入, 请在spring-servlet.xml中定义SpringContextHolder。");
    }
  }

  /**
   * 实现DisposableBean接口, 在Context关闭时清理静态变量
   */
  @Override
  public void destroy() {
    SpringContextHolder.clearHolder();
  }
}