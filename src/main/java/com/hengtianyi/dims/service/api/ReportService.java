package com.hengtianyi.dims.service.api;

import com.hengtianyi.common.core.base.service.AbstractGenericService;
import com.hengtianyi.dims.service.entity.ReportEntity;

/**
 * Report接口类
 *
 * @author LY
 */
public interface ReportService extends AbstractGenericService<ReportEntity, String> {

  /**
   * 查询最大序列号
   *
   * @param areaCode 村编号
   * @return 最大序列号
   */
  String maxSerialNo(String areaCode);
}
