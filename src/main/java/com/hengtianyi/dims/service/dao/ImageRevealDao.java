package com.hengtianyi.dims.service.dao;

import com.hengtianyi.common.core.base.service.AbstractGenericDao;
import com.hengtianyi.dims.service.entity.ImageRevealEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/** 
 * ImageReveal数据库读写DAO
 * @author
 */
@Mapper
public interface ImageRevealDao extends AbstractGenericDao<ImageRevealEntity, String> {

  /**
   * 所有流程
   *
   * @param revealId 任务Id
   * @return list
   */
  List<ImageRevealEntity> getAllImages(@Param("revealId") String revealId);

}
