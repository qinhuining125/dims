package com.hengtianyi.dims.service.dao;

import com.hengtianyi.common.core.base.service.AbstractGenericDao;
import com.hengtianyi.dims.service.entity.RevealFlowEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/** 
 * RevealFlow数据库读写DAO
 * @author
 */
@Mapper
public interface RevealFlowDao extends AbstractGenericDao<RevealFlowEntity, String> {

  /**
   * 所有流程
   *
   * @param revealId
   * @return list
   */
  List<RevealFlowEntity> getAllFlows(@Param("revealId") String revealId);

}
