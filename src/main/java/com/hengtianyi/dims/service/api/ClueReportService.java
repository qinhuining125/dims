package com.hengtianyi.dims.service.api;

import com.hengtianyi.common.core.base.CommonEntityDto;
import com.hengtianyi.common.core.base.CommonPageDto;
import com.hengtianyi.common.core.base.service.AbstractGenericService;
import com.hengtianyi.dims.service.dto.QueryDto;
import com.hengtianyi.dims.service.entity.ClueReportEntity;

/**
 * ClueReport接口类
 *
 * @author LY
 */
public interface ClueReportService extends AbstractGenericService<ClueReportEntity, String> {

  /**
   * 自定义分页
   *
   * @param dto dto
   * @return
   */
  CommonEntityDto<ClueReportEntity> pagelist(QueryDto dto);

  /**
   * 原分页
   *
   * @param dto
   * @return
   */
  CommonPageDto listData(QueryDto dto);

  /**
   * 查看报告数量
   *
   * @param dto dto
   * @return
   */
  Integer countReport(QueryDto dto);

  /**
   * echart数据
   *
   * @param startTime 开始时间
   * @param endTime   结束时间
   * @param areaCode  乡镇编号
   * @return json
   */
  String echartsData(String startTime, String endTime, String areaCode);
}
