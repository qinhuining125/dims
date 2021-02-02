package com.hengtianyi.dims.service.dao;

import com.hengtianyi.common.core.base.service.AbstractGenericDao;
import com.hengtianyi.dims.service.entity.RelUserAreaEntity;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * RelUserArea数据库读写DAO
 *
 * @author LY
 */
@Mapper
public interface RelUserAreaDao extends AbstractGenericDao<RelUserAreaEntity, String> {

  /**
   * 根据用户查询对应的地区
   *
   * @param userId 用户id
   * @return list
   */
  List<RelUserAreaEntity> getUserArealist(@Param("userId") String userId);

  /**
   * 删除用户对应的地区
   *
   * @param userId uid
   * @return
   */
  int deleteUserArea(@Param("userId") String userId);

  /**
   * 是否是其联系室管理的用户
   *
   * @param reportUserId 上报用户id
   * @param userId   当前用户id
   * @return true是，false否
   */
  String contactAdmin(@Param("reportUserId") String reportUserId, @Param("userId") String userId);
}
