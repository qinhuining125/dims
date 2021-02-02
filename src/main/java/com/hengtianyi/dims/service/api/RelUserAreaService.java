package com.hengtianyi.dims.service.api;

import com.hengtianyi.common.core.base.service.AbstractGenericService;
import com.hengtianyi.dims.service.dto.UserAreaDto;
import com.hengtianyi.dims.service.entity.RelUserAreaEntity;
import java.util.List;

/**
 * RelUserArea接口类
 *
 * @author LY
 */
public interface RelUserAreaService extends AbstractGenericService<RelUserAreaEntity, String> {

  /**
   * 根据用户查询对应的地区
   *
   * @param userId 用户id
   * @return list
   */
  List<RelUserAreaEntity> getUserArealist(String userId);

  /**
   * 删除用户对应的地区
   *
   * @param userId uid
   * @return
   */
  int deleteUserArea(String userId);

  /**
   * 是否是其联系室管理的用户
   *
   * @param reportUserId 上报用户id
   * @param userId       当前用户id
   * @return true是，false否
   */
  Boolean contactAdmin(String reportUserId, String userId);

  /**
   * 所有子用户
   *
   * @param userId uid
   * @return list
   */
  List<UserAreaDto> childUsers(String userId);
}
