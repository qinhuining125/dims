package com.hengtianyi.dims.service.api;

import com.hengtianyi.common.core.base.CommonEntityDto;
import com.hengtianyi.common.core.base.CommonPageDto;
import com.hengtianyi.common.core.base.service.AbstractGenericService;
import com.hengtianyi.dims.service.dto.QueryDto;
import com.hengtianyi.dims.service.dto.RevealInfoDto;
import com.hengtianyi.dims.service.entity.RevealInfoEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * RevealInfo接口类
 *
 * @author
 */
public interface RevealInfoService extends AbstractGenericService<RevealInfoEntity, String> {

  /**
   * 保存
   *
   * @param dto 任务信息
   * @return i
   */
  Integer saveData(RevealInfoDto dto, HttpServletRequest request);

  /**
   * 自定义分页
   *
   * @param dto dto
   * @return
   */
  CommonEntityDto<RevealInfoEntity> pagelist(QueryDto dto);

  /**
   * 原分页
   *
   * @param dto
   * @return
   */
  CommonPageDto listData(QueryDto dto);

  /**
   * echart数据
   *
   * @param startTime 开始时间
   * @param endTime   结束时间
   * @param areaCode  乡镇编号
   * @return json
   */
  String echartsData(String startTime, String endTime, String areaCode);

  /**
   * 查看任务数量
   *
   * @param dto dto
   * @return
   */
  Integer countReveal(QueryDto dto);

  String getDataList (QueryDto dto);
}
