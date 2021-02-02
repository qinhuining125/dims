package com.hengtianyi.dims.service.api;

import com.hengtianyi.common.core.base.service.AbstractGenericService;
import com.hengtianyi.dims.service.entity.TownshipEntity;
import java.util.List;

/**
 * Township接口类
 *
 * @author LY
 */
public interface TownshipService extends AbstractGenericService<TownshipEntity, String> {
  /**
   * 所有权限地区
   *
   * @return list
   */
  List<TownshipEntity> areaList();
}
