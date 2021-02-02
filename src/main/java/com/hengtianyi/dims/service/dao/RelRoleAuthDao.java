package com.hengtianyi.dims.service.dao;

import com.hengtianyi.common.core.base.service.AbstractGenericDao;
import com.hengtianyi.dims.service.entity.RelRoleAuthEntity;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * RelRoleAuth数据库读写DAO
 *
 * @author LY
 */
@Mapper
public interface RelRoleAuthDao extends AbstractGenericDao<RelRoleAuthEntity, String> {

  /**
   * 根据角色ID查询对应的权限ID集合
   * <p>rel_sys_role_auth</p>
   *
   * @param roleId 角色ID
   * @return 权限ID集合
   */
  List<String> getRoleAuthByRoleId(@Param("roleId") Integer roleId);

  /**
   * 根据角色ID删除对应的权限角色关系
   * <p>rel_sys_role_auth</p>
   *
   * @param roleId 角色ID
   * @return 权限ID集合
   */
  Integer deleteRoleAuthByRoleId(@Param("roleId") Integer roleId);

  /**
   * 给指定角色ID权限集合
   * <p>rel_sys_role_auth</p>
   *
   * @param roleId   角色ID
   * @param authList 权限ID集合
   * @return 操作行数
   */
  Integer addRoleAuthByRoleId(@Param("roleId") Integer roleId,
      @Param("list") final List<String> authList);
}
