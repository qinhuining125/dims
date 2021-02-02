package com.hengtianyi.dims.service.dao;

import com.hengtianyi.common.core.base.service.AbstractGenericDao;
import com.hengtianyi.dims.service.entity.ReportTypeEntity;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * ReportType数据库读写DAO
 *
 * @author LY
 */
@Mapper
public interface ReportTypeDao extends AbstractGenericDao<ReportTypeEntity, String> {

  List<ReportTypeEntity> listByRole(@Param("roleId") Integer roleId);

  /**
   * 角色查内容
   *
   * @param roleId 角色
   * @return list
   */
  List<String> contentByRole(@Param("roleId") Integer roleId);

  /**
   * 最大Id
   *
   * @return
   */
  Integer maxId();

  /**
   * 角色,顺序查内容
   *
   * @param roleId 角色id
   * @param sortNo 排序
   * @return content
   */
  String contentByRoleSortNo(@Param("roleId") Integer roleId, @Param("sortNo") Integer sortNo);
}
