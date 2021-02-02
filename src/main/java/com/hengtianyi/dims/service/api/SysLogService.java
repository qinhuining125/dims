package com.hengtianyi.dims.service.api;

import com.hengtianyi.common.core.base.service.AbstractGenericService;
import com.hengtianyi.dims.service.entity.SysLogEntity;

/**
 * SysLog接口类
 *
 * @author wangfanjun
 */
public interface SysLogService extends AbstractGenericService<SysLogEntity, Long> {

  /**
   * 保存日志，使用异步保存
   *
   * @param logEntity 日志实体
   */
  void asyncSaveLogData(SysLogEntity logEntity);

  /**
   * 清空日志表和日志参数表
   *
   * @return 是否成功，true - 成功
   */
  boolean removeAll();

  /**
   * 获取日志记录的参数
   *
   * @param logId 日志ID
   * @return 日志记录的参数
   */
  String getParameter(Long logId);

  /**
   * 获取日志记录的返回
   *
   * @param logId 日志ID
   * @return 日志记录的返回
   */
  String getResult(Long logId);

  /**
   * 获取最早的日志时间戳
   *
   * @return 日志时间戳
   */
  Long getFirstTime();
}

