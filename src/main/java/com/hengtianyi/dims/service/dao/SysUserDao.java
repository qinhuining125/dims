package com.hengtianyi.dims.service.dao;

import com.hengtianyi.common.core.base.service.AbstractGenericDao;
import com.hengtianyi.dims.service.dto.UserAreaDto;
import com.hengtianyi.dims.service.entity.SysUserEntity;
import com.hengtianyi.dims.service.entity.SysUserSecurityEntity;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * SysUser数据库读写DAO
 *
 * @author BBF
 */
@Mapper
public interface SysUserDao extends AbstractGenericDao<SysUserEntity, String> {



  /**
   * 根据角色ID查询对应的用户ID集合
   * @param roleId 角色ID
   * @return 用户ID集合
   */
  List<SysUserEntity> getUserIdByRoleId(@Param("roleId") Integer roleId);


  /**
   * 根据登录账号（唯一）查询用户信息
   *
   * @param userAccount 登录账号
   * @return 用户实体
   */
  SysUserEntity selectByUserAccount(@Param("userAccount") String userAccount);

  /**
   * 验证登录账号是否已经存在
   *
   * @param userAccount 登录账号
   * @return 用户ID
   */
  String checkRepeat(@Param("userAccount") String userAccount);

  /**
   * 根据用户ID获取用户加密信息
   *
   * @param id 用户ID
   * @return 用户加密信息
   */
  SysUserSecurityEntity getUserSecurity(@Param("id") String id);

  /**
   * 新增用户加密信息
   *
   * @param entity 用户密文实体
   * @return 操作行数
   */
  Integer insertUserSecurity(SysUserSecurityEntity entity);

  /**
   * 更新用户加密信息
   *
   * @param entity 用户密文实体
   * @return 操作行数
   */
  Integer updateUserSecurity(SysUserSecurityEntity entity);

  /**
   * 根据用户ID集合删除登录信息
   *
   * @param userList 用户ID集合
   * @return 操作行数
   */
  Integer deleteUserSecurity(@Param("list") List<String> userList);

  /**
   * 根据用户ID修改登录时间
   *
   * @param id   用户ID
   * @param time 登录时间
   * @return 操作行数
   */
  Integer updateUserLoginTime(@Param("id") String id, @Param("time") Long time);

  /**
   * 上级用户
   *
   * @param areaCode 区域
   * @param roleId   角色
   * @return 上级
   */
  SysUserEntity superiorUser(@Param("areaCode") String areaCode, @Param("roleId") Integer roleId);

  /**
   * 所有子用户
   *
   * @param areaCode 区域编号
   * @return list
   */
  List<UserAreaDto> childTaskUser(@Param("areaCode") String areaCode);

  /**
   * 非网格员用户
   *
   * @return list
   */
  List<SysUserEntity> nonGrid();
}
