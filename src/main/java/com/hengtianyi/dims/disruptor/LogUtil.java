package com.hengtianyi.dims.disruptor;

import com.hengtianyi.dims.service.entity.SysLogEntity;

/**
 * 保存日志工具类
 *
 * @author BBF
 */
public final class LogUtil {

  private static final LogEventQueueHelper LOG_QUEUE;

  static {
    LOG_QUEUE = LogEventQueueHelper.getInstance();
    LOG_QUEUE.start();
  }

  /**
   * 保存日志
   *
   * @param logEntity 日志实体
   */
  public static void saveLog(final SysLogEntity logEntity) {
    LOG_QUEUE.publishEvent(logEntity);
  }

}
