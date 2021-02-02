package com.hengtianyi.dims.service.api;

import com.hengtianyi.common.core.base.service.AbstractGenericService;
import com.hengtianyi.dims.service.entity.RevealFlowEntity;

import java.util.List;

/**
 * RevealFlow接口类
 *
 * @author
 */
public interface RevealFlowService extends AbstractGenericService<RevealFlowEntity, String> {

  /**
   * 所有流程
   *
   * @param revealId 举报表单Id
   * @return list
   */
  List<RevealFlowEntity> getAllFlows(String revealId);

  List<RevealFlowEntity> getFlowsByConditons(RevealFlowEntity entity);
}
