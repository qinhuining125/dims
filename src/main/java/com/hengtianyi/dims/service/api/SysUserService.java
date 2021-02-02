package com.hengtianyi.dims.service.api;

import com.hengtianyi.common.core.base.UserDTO;
import com.hengtianyi.common.core.base.service.AbstractGenericService;
import com.hengtianyi.dims.service.entity.SysUserEntity;
import java.util.List;

/**
 * SysUser接口类
 *
 * @author BBF
 */
public interface SysUserService extends AbstractGenericService<SysUserEntity, String> {

  /**
   * 验证登录账户是否已经存在
   *
   * @param id      用户ID
   * @param account 登录账户
   * @return true-无重名，false-有重名
   */
  boolean checkRepeat(String id, String account);

  /**
   * 验证登录凭证
   * <p>根据登录账号从数据库获取登录信息，得到密码的密文和盐、密钥等数据，
   * 将传入的密码，通过盐和密钥进行加密计算，比较两组密文，一致表示验证通过</p>
   * <p style="color:red">验证通过返回用户实体，否则抛出WebException异常</p>
   *
   * @param account  用户登录账号
   * @param password 密码
   * @return 验证通过返回用户实体，否则抛出WebException异常
   */
  SysUserEntity validate(String account, String password);

  /**
   * 验证登录凭证
   * <p>根据登录账号从数据库获取登录信息，得到密码的密文和盐、密钥等数据，
   * 将传入的密码，通过盐和密钥进行加密计算，比较两组密文，一致表示验证通过</p>
   * <p style="color:red">验证通过返回用户实体，否则抛出WebException异常</p>
   *
   * @param uid      用户ID
   * @param password 密码
   * @return true - 验证通过
   */
  Boolean validateByUid(String uid, String password);

  /**
   * 根据用户ID获取用户名
   *
   * @param uid 用户ID
   * @return 用户名
   */
  String getNameById(String uid);


  /**
   * 根据角色ID查询对应的用户ID集合
   * @param roleId 角色ID
   * @return 用户ID集合
   */
  List<SysUserEntity> getUserIdByRoleId(Integer roleId);


  /**
   * 修改密码
   *
   * @param uid      用户ID
   * @param password 新密码
   */
  void changePassword(String uid, String password);

  /**
   * 根据登录账号获取用户实体
   *
   * @param account 用户登录账号
   * @return 验证通过返回用户实体
   */
  SysUserEntity selectByUserAccount(String account);

  /**
   * APP登录
   *
   * @param dto 账号 密码
   * @return json
   */
  String appLogin(UserDTO dto);

  /**
   * 上级管理员
   *
   * @param areaCode 区域
   * @param roleId   角色
   * @return 上级
   */
  SysUserEntity superiorUser(String areaCode, Integer roleId);

  /**
   * 非网格员用户
   *
   * @return list
   */
  List<SysUserEntity> nonGrid();
}

