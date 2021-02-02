package com.hengtianyi.dims.config;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 异步配置，@Async必须是public方法
 *
 * @author BBF
 */
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

  private static final Logger LOGGER = LoggerFactory.getLogger(AsyncConfig.class);

  @Override
  public Executor getAsyncExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(10);
    executor.setMaxPoolSize(100);
    executor.setQueueCapacity(100);
    executor.setWaitForTasksToCompleteOnShutdown(true);
    executor.setAwaitTerminationSeconds(600);
    executor.setThreadNamePrefix("AsyncThread-");
    // 如果不初始化，导致找到不到执行器
    executor.initialize();
    return executor;
  }

  @Override
  public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
    return new AsyncUncaughtExceptionHandler() {
      @Override
      public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        LOGGER.error(
            "[AsyncConfig.AsyncUncaughtExceptionHandler]异步调用发生异常：ex = {}, method = {}, params = {}",
            ex.getMessage(), method, params);
      }
    };
  }
}
