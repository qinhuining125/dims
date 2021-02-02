package com.hengtianyi.dims.service.dao;

import com.hengtianyi.common.core.base.service.AbstractGenericDao;
import com.hengtianyi.dims.service.entity.SysAuthEntity;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * SysAuth数据库读写DAO
 *
 * @author LY
 */
@Mapper
public interface SysAuthDao extends AbstractGenericDao<SysAuthEntity, String> {

  /**
   * 根据权限ID集合获取启用的权限代码集合
   *
   * @param authList 权限ID集合
   * @return 获取启用的权限代码集合
   */
  List<String> getAuthCodeByAuthId(@Param("list") List<String> authList);
}
