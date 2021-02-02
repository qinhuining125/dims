package com.hengtianyi.dims.service.dao;

import com.hengtianyi.common.core.base.service.AbstractGenericDao;
import com.hengtianyi.dims.service.entity.SysLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * SysLog数据库读写DAO
 *
 * @author wangfanjun
 */
@Mapper
public interface SysLogDao extends AbstractGenericDao<SysLogEntity, Long> {

  /**
   * 获取日志记录的参数
   *
   * @param logId 日志ID
   * @return 日志记录的参数
   */
  String getParameter(@Param("logId") Long logId);

  /**
   * 获取日志记录的返回
   *
   * @param logId 日志ID
   * @return 日志记录的返回
   */
  String getResult(@Param("logId") Long logId);

  /**
   * 获取日志记录的参数
   *
   * @param logId     日志ID
   * @param parameter 入参
   * @param result    返回
   * @return 日志记录的参数
   */
  Integer insertParameterAndResult(@Param("logId") Long logId,
      @Param("parameter") String parameter,
      @Param("result") String result);

  /**
   * 清空日志表
   */
  void truncateLog();

  /**
   * 清空日志参数表
   */
  void truncateLogText();

  /**
   * 获取最早的日志时间戳
   *
   * @return 日志时间戳
   */
  Long getFirstTime();
}
