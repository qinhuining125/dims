package com.hengtianyi.dims.service.dao;

import com.hengtianyi.common.core.base.service.AbstractGenericDao;
import com.hengtianyi.dims.service.entity.TaskFlowEntity;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/** 
 * TaskFlow数据库读写DAO
 * @author LY
 */
@Mapper
public interface TaskFlowDao extends AbstractGenericDao<TaskFlowEntity, String> {

  /**
   * 所有流程
   *
   * @param taskId 任务Id
   * @return list
   */
  List<TaskFlowEntity> getAllFlows(@Param("taskId") String taskId);

}
