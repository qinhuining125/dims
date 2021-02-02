package com.hengtianyi.dims.service.api;

import com.hengtianyi.common.core.base.service.AbstractGenericService;
import com.hengtianyi.dims.service.entity.ClueFlowEntity;
import java.util.List;

/**
 * ClueFlow接口类
 *
 * @author LY
 */
public interface ClueFlowService extends AbstractGenericService<ClueFlowEntity, String> {

  /**
   * 子流程
   *
   * @param clueId 线索id
   * @return list
   */
  List<ClueFlowEntity> getAllFlows(String clueId);

  /**
   * 接受人员id
   *
   * @param clueId 线索id
   * @param state  0,接收，1受理
   * @return 接受人员
   */
  String getReceiveId(String clueId, Integer state);
}
