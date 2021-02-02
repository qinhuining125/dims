package com.hengtianyi.dims.service.api;

import com.hengtianyi.common.core.base.service.AbstractGenericService;
import com.hengtianyi.dims.service.entity.TaskFlowEntity;
import java.util.List;

/**
 * TaskFlow接口类
 *
 * @author LY
 */
public interface TaskFlowService extends AbstractGenericService<TaskFlowEntity, String> {

  /**
   * 所有流程
   *
   * @param taskId 任务Id
   * @return list
   */
  List<TaskFlowEntity> getAllFlows(String taskId);
}
