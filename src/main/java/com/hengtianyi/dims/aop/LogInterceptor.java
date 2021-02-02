package com.hengtianyi.dims.aop;

import com.hengtianyi.common.core.util.sequence.SystemClock;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 页面执行时间检查器
 * <p>注意，HandlerInterceptorAdapter是HandlerInterceptor的抽象类，使用Adapter类可以只Override需要的方法</p>
 *
 * @author BBF
 */
public class LogInterceptor extends HandlerInterceptorAdapter {

  private static final Logger LOGGER = LoggerFactory.getLogger(LogInterceptor.class);
  private static final ThreadLocal<Long> START_TIME_THREAD_LOCAL = new NamedThreadLocal<>(
      "LogInterceptor StartTime");

  @Override
  public boolean preHandle(HttpServletRequest request,
      HttpServletResponse response,
      Object handler) {
    if (LOGGER.isInfoEnabled()) {
      // 线程绑定变量（该数据只有当前请求的线程可见）
      START_TIME_THREAD_LOCAL.set(SystemClock.now());
    }
    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request,
      HttpServletResponse response,
      Object handler, Exception ex) {
    if (LOGGER.isInfoEnabled()) {
      // 得到线程绑定的局部变量（开始时间）
      long beginTime = START_TIME_THREAD_LOCAL.get();
      START_TIME_THREAD_LOCAL.remove();
      long endTime = SystemClock.now();
      long memMax = Runtime.getRuntime().maxMemory();
      long memTotal = Runtime.getRuntime().totalMemory();
      long memFree = Runtime.getRuntime().freeMemory();
//      LOGGER.info("拦截器===》》 URI: {}，耗时：{} 毫秒，最大内存: {}，已分配内存: {}，已分配内存中的剩余空间: {}，最大可用内存: {}",
//          request.getRequestURI(), endTime - beginTime,
//          FileUtil.calcFileSize(memMax), FileUtil.calcFileSize(memTotal),
//          FileUtil.calcFileSize(memFree), FileUtil.calcFileSize(memMax - memTotal + memFree));
    }
  }
}