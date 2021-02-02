package com.hengtianyi.dims.service.api;

import com.hengtianyi.common.core.base.service.AbstractGenericService;
import com.hengtianyi.dims.service.dto.KeyValueDto;
import com.hengtianyi.dims.service.entity.ClueReportEntity;
import com.hengtianyi.dims.service.entity.ReportEntity;
import com.hengtianyi.dims.service.entity.ReportTypeEntity;
import java.util.List;

/**
 * ReportType接口类
 *
 * @author LY
 */
public interface ReportTypeService extends AbstractGenericService<ReportTypeEntity, String> {

  /**
   * 下一个Id
   *
   * @return int
   */
  Integer nextId();

  /**
   * 根据上报类型，查对应的内容
   *
   * @param roleId    角色Id
   * @param reportIds ids
   * @return list
   */
  List<KeyValueDto> contents(Integer roleId, String reportIds);
}
