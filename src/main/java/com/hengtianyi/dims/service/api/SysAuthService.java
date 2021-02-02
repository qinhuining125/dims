package com.hengtianyi.dims.service.api;

import com.hengtianyi.common.core.base.service.AbstractGenericService;
import com.hengtianyi.dims.service.entity.SysAuthEntity;
import java.util.List;
import java.util.Set;

/**
 * SysAuth接口类
 *
 * @author LY
 */
public interface SysAuthService extends AbstractGenericService<SysAuthEntity, String> {

  /**
   * 给指定角色ID绑定权限集合
   *
   * @param roleId   角色ID
   * @param authList 权限ID集合
   * @return 操作行数
   */
  Integer saveAuthByRoleId(Integer roleId, List<String> authList);

  /**
   * 根据用户ID获取所有可用的权限
   *
   * @param roleId 用户角色ID
   * @return 可用的权限集合
   */
  Set<String> getAuthByRoleId(Integer roleId);
}
