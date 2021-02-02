package com.hengtianyi.dims.disruptor;

import com.hengtianyi.dims.feature.SpringContextHolder;
import com.hengtianyi.dims.service.api.SysLogService;
import com.lmax.disruptor.WorkHandler;

/**
 * 日志发送事件处理器
 *
 * @author BBF
 */
public class LogEventHandler implements WorkHandler<LogEvent> {

  private final SysLogService sysLogService;

  public LogEventHandler() {
    sysLogService = SpringContextHolder.getBean(SysLogService.class);
  }

  @Override
  public void onEvent(LogEvent event) {
    if (event.getValue() != null) {
      sysLogService.asyncSaveLogData(event.getValue());
    }
  }
}
