package com.hengtianyi.dims.service.dao;

import com.hengtianyi.common.core.base.service.AbstractGenericDao;
import com.hengtianyi.dims.service.entity.ClueFlowEntity;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * ClueFlow数据库读写DAO
 *
 * @author LY
 */
@Mapper
public interface ClueFlowDao extends AbstractGenericDao<ClueFlowEntity, String> {

  /**
   * 子流程
   *
   * @param clueId 线索id
   * @return list
   */
  List<ClueFlowEntity> getAllFlows(@Param("clueId") String clueId);

  /**
   * @param clueId 线索id
   * @param state  状态
   * @return 接受人员
   */
  String getReceiveId(@Param("clueId") String clueId, @Param("state") Integer state);
}
