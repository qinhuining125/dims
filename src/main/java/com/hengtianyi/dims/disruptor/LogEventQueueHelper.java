package com.hengtianyi.dims.disruptor;

import com.hengtianyi.common.core.disruptor.BaseQueueHelper;
import com.hengtianyi.dims.service.entity.SysLogEntity;
import com.lmax.disruptor.EventFactory;

/**
 * 日志队列处理器
 *
 * @author BBF
 */
public class LogEventQueueHelper extends BaseQueueHelper<SysLogEntity, LogEvent> {

  private static final int QUEUE_SIZE = 1024;

  public LogEventQueueHelper() {
    super();
  }

  public LogEventQueueHelper(int threadNum) {
    super(threadNum);
  }

  /**
   * 获取单例对象
   *
   * @return 队列处理器
   */
  public static LogEventQueueHelper getInstance() {
    return Singleton.INSTANCE.getInstance();
  }

  @Override
  public String getNamingPattern() {
    return "LogData-%d";
  }

  @Override
  public int getQueueSize() {
    return QUEUE_SIZE;
  }

  @Override
  public EventFactory<LogEvent> getEventFactory() {
    return new EventFactory<LogEvent>() {
      @Override
      public LogEvent newInstance() {
        return new LogEvent();
      }
    };
  }

  @Override
  public LogEventHandler[] getHandler() {
    int size = getThreadNum();
    LogEventHandler[] handlers = new LogEventHandler[size];
    for (int i = 0; i < size; i++) {
      handlers[i] = new LogEventHandler();
    }
    return handlers;
  }

  /**
   * 获取单例，枚举方式
   */
  private enum Singleton {
    /**
     * 枚举单例
     */
    INSTANCE;
    private LogEventQueueHelper single;

    Singleton() {
      single = new LogEventQueueHelper(5);
    }

    private LogEventQueueHelper getInstance() {
      return single;
    }
  }
}
