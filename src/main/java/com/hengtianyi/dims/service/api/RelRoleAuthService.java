package com.hengtianyi.dims.service.api;

import com.hengtianyi.common.core.base.service.AbstractGenericService;
import com.hengtianyi.dims.service.entity.RelRoleAuthEntity;
import java.util.List;

/**
 * RelRoleAuth接口类
 *
 * @author LY
 */
public interface RelRoleAuthService extends AbstractGenericService<RelRoleAuthEntity, String> {

  /**
   * 根据角色ID查询对应的权限ID集合
   *
   * @param roleId 角色ID
   * @return 权限ID集合
   */
  List<String> getRoleAuthByRoleId(Integer roleId);
}
