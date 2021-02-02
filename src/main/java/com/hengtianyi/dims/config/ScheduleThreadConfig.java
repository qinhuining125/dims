package com.hengtianyi.dims.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * 定时任务线程池
 *
 * @author LY
 */
@Configuration
public class ScheduleThreadConfig implements SchedulingConfigurer {

  @Override
  public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
    //设定一个长度10的定时任务线程池
    ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    scheduler.initialize();
    scheduler.setPoolSize(20);
    //设置线程名开头
    scheduler.setThreadNamePrefix("task-");
    scheduler.setAwaitTerminationSeconds(60);
    scheduler.setWaitForTasksToCompleteOnShutdown(true);
    taskRegistrar.setScheduler(scheduler);
  }
}
