package com.hengtianyi.dims.service.api.impl;

import com.hengtianyi.common.core.base.UserDTO;
import com.hengtianyi.common.core.base.service.AbstractGenericServiceImpl;
import com.hengtianyi.common.core.constant.BaseConstant;
import com.hengtianyi.common.core.feature.ServiceResult;
import com.hengtianyi.common.core.util.CollectionUtil;
import com.hengtianyi.common.core.util.JsonUtil;
import com.hengtianyi.common.core.util.StringUtil;
import com.hengtianyi.common.core.util.cache.CacheUtil;
import com.hengtianyi.common.core.util.security.PasswordHash;
import com.hengtianyi.common.core.util.sequence.SystemClock;
import com.hengtianyi.dims.cache.CacheLogin;
import com.hengtianyi.dims.config.CustomProperties;
import com.hengtianyi.dims.constant.FrameConstant;
import com.hengtianyi.dims.constant.RoleEnum;
import com.hengtianyi.dims.exception.ErrorEnum;
import com.hengtianyi.dims.exception.WebException;
import com.hengtianyi.dims.service.api.SysUserService;
import com.hengtianyi.dims.service.dao.SysUserDao;
import com.hengtianyi.dims.service.dto.AppUserDto;
import com.hengtianyi.dims.service.entity.SysUserEntity;
import com.hengtianyi.dims.service.entity.SysUserSecurityEntity;
import com.hengtianyi.dims.utils.WebUtil;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * SysUser接口的实现类
 *
 * @author BBF
 */
@Service
public class SysUserServiceImpl extends AbstractGenericServiceImpl<SysUserEntity, String>
    implements SysUserService {

  private static final Logger LOGGER = LoggerFactory.getLogger(SysUserServiceImpl.class);

  private static final String CACHE_USER_NAME = "CACHE_USER_NAME_%s";
  private static final String CACHE_USER_LAST_LOGIN = "CACHE_USER_LAST_LOGIN_%s";

  private static final String ACCOUNT_LOCKED = "您的账户已被锁定，请%d秒后重试";
  private static final String ACCOUNT_LOCKED_TIP = "登录名或密码错误，连错%d次后将被锁定";
  /**
   * 实体与数据表字段的映射
   * <p>格式：实体字段 - 数据库字段</p>
   */
  private static final Map<String, String> PROPERTY_COLUMN;
  /**
   * 默认排序的sql，order by后面的部分，例如: id asc,name desc
   */
  private static final String DEFAULT_ORDER_SQL = "create_time desc";

  static {
    PROPERTY_COLUMN = new HashMap<>(14);
    PROPERTY_COLUMN.put("id", "ID");
    PROPERTY_COLUMN.put("userAccount", "USER_ACCOUNT");
    PROPERTY_COLUMN.put("userName", "USER_NAME");
    PROPERTY_COLUMN.put("enabled", "ENABLED");
    PROPERTY_COLUMN.put("sex", "SEX");
    PROPERTY_COLUMN.put("phone", "PHONE");
    PROPERTY_COLUMN.put("createTime", "CREATE_TIME");
    PROPERTY_COLUMN.put("areaCode", "AREA_CODE");
    PROPERTY_COLUMN.put("roleId", "ROLE_ID");
    PROPERTY_COLUMN.put("areaName", "AREA_NAME");
    PROPERTY_COLUMN.put("idCard", "ID_CARD");
    PROPERTY_COLUMN.put("remark", "REMARK");
  }

  @Resource
  private SysUserDao sysUserDao;
  @Resource
  private CustomProperties customProperties;

  /**
   * {@inheritDoc}
   */
  @Override
  public SysUserDao getDao() {
    return sysUserDao;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, String> getPropertyColumn() {
    return PROPERTY_COLUMN;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDefaultSort() {
    return DEFAULT_ORDER_SQL;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean checkRepeat(String id, String account) {
    String idInDb = sysUserDao.checkRepeat(account);
    return StringUtil.isBlank(idInDb) || StringUtil.equals(id, idInDb);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SysUserEntity validate(String account, String password) {
    // 最大允许错误次数
    int maxTimes = customProperties.getPasswordErrorTimes();
    int showTips = maxTimes / 2 + 1;
    //从缓存中获取错误次数
    long errorTimes = CacheLogin.getErrorTimes(account);
    if (maxTimes <= errorTimes) {
      throw new WebException(String.format(ACCOUNT_LOCKED, CacheLogin.getExpire(account)));
    }
    SysUserEntity entity = sysUserDao.selectByUserAccount(account);
    if (entity == null) {
      LOGGER.warn("[SysUserServiceImpl.validate]用户不存在, account = {}, password = {}",
          account, password);
      throw new WebException(ErrorEnum.NO_USER);
    }
    String uid = entity.getId();
    SysUserSecurityEntity dto = sysUserDao.getUserSecurity(uid);
    if (!PasswordHash.validate(dto, password)) {
      errorTimes++;
      CacheLogin.setErrorTimes(account, errorTimes);
      if (maxTimes <= errorTimes) {
        throw new WebException(String.format(ACCOUNT_LOCKED, CacheLogin.getExpire(account)));
      }
      if (errorTimes > showTips) {
        throw new WebException(String.format(ACCOUNT_LOCKED_TIP, maxTimes - errorTimes));
      }
      throw new WebException(ErrorEnum.PASSWORD_INVALID);
    }
    // 判断用户是否启用
    if (entity.getEnabled() != FrameConstant.ENABLED) {
      throw new WebException(ErrorEnum.USER_DISABLED);
    }
    //网格员或联络员无登录权限
    if (entity.getRoleId() <= 1002) {
      throw new WebException("网格员或联络员暂不开放登录");
    }
    // 验证成功，写入登录时间
    long now = SystemClock.now();
    sysUserDao.updateUserLoginTime(uid, now);
    // 更新缓存
    CacheUtil.put(String.format(CACHE_USER_LAST_LOGIN, uid), now);
    //登录成功，删除错误次数缓存记录
    CacheLogin.loginSuccess(account);
    return entity;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Boolean validateByUid(String uid, String password) {
    SysUserSecurityEntity dto = sysUserDao.getUserSecurity(uid);
    return PasswordHash.validate(dto, password);
  }

  @Override
  public String getNameById(String uid) {
    String key = String.format(CACHE_USER_NAME, uid);
    String name = CacheUtil.get(key, String.class);
    // 防止缓存穿透，只有当null的时候，才去读数据库
    if (name == null) {
      try {
        SysUserEntity userEntity = sysUserDao.selectByPrimaryKey(uid);
        name = userEntity.getUserName();
      } catch (Exception ex) {
        name = StringUtil.EMPTY;
      } finally {
        // 数据缓存30秒
        CacheUtil.put(key, name, 300);
      }
    }
    return name;
  }

  @Override
  public List<SysUserEntity> getUserIdByRoleId(Integer roleId){
    return sysUserDao.getUserIdByRoleId(roleId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void changePassword(String uid, String password) {
    try {
      SysUserEntity entity = sysUserDao.selectByPrimaryKey(uid);
      entity.setPassword(password);
      sysUserDao.updateByPrimaryKey(entity);
      sysUserDao.updateUserSecurity(this.getSecurityEntity(uid, password));
    } catch (Exception ex) {
      LOGGER.error("[SysUserServiceImpl.changePassword]{}, userId = {}, password = {}",
          ex.getMessage(), uid, password);
      throw new WebException(ErrorEnum.PASSWORD_UPDATE);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer insertData(SysUserEntity userEntity) {
    int ct;
    try {
      ct = sysUserDao
          .insertUserSecurity(this.getSecurityEntity(userEntity.getId(), userEntity.getPassword()));
      if (ct > 0) {
        ct = sysUserDao.insertSelective(userEntity);
      }
    } catch (Exception ex) {
      ct = BaseConstant.NUM_0;
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      LOGGER.error("[SysUserServiceImpl.insertData]回滚，原因：{}，entity = {}",
          ex.getMessage(), JsonUtil.toJson(userEntity), ex);
    }
    return ct;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer updateData(SysUserEntity model) {
    // 让缓存失效
    String uid = model.getId();
    if (StringUtil.isNotBlank(uid)) {
      CacheUtil.remove(String.format(CACHE_USER_NAME, uid));
    }
    return this.getDao().updateByPrimaryKeySelective(model);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer deleteData(String id) {
    return this.deleteDataByIdList(CollectionUtil.singletonList(id));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer deleteData(final List<String> idsList) {
    return this.deleteDataByIdList(idsList);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer deleteData(final SysUserEntity model) {
    List<SysUserEntity> entityList = sysUserDao.searchAllData(model);
    List<String> idsList;
    if (CollectionUtil.isNotEmpty(entityList)) {
      int len = entityList.size();
      idsList = new ArrayList<>(len);
      for (int i = 0; i < len; i++) {
        idsList.add(entityList.get(i).getId());
      }
    } else {
      idsList = Collections.emptyList();
    }
    return this.deleteDataByIdList(idsList);
  }

  /**
   * 获取用户密文实体
   *
   * @param uid      用户ID
   * @param password 密码明文
   * @return 用户密文实体
   * @throws InvalidKeySpecException  加密密钥异常
   * @throws NoSuchAlgorithmException 加密算法异常
   */
  private SysUserSecurityEntity getSecurityEntity(String uid, String password)
      throws InvalidKeySpecException, NoSuchAlgorithmException {
    SysUserSecurityEntity entity = new SysUserSecurityEntity(PasswordHash.pbkdf2(password));
    entity.setId(uid);
    entity.setLastEdit(SystemClock.now());
    return entity;
  }

  /**
   * 批量删除用户 删除对应的用户信息表、用户加密信息表和用户角色关系表
   *
   * @param idsList 用户ID集合
   * @return 操作行数
   */
  private int deleteDataByIdList(final List<String> idsList) {
    int ct = BaseConstant.NUM_0;
    if (CollectionUtil.isNotEmpty(idsList)) {
      try {
        // 删除用户角色关系
        ct = sysUserDao.deleteByIds(idsList);
        if (ct > 0) {
          sysUserDao.deleteUserSecurity(idsList);
        }
      } catch (Exception ex) {
        ct = BaseConstant.NUM_0;
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        LOGGER.error("[SysUserServiceImpl.deleteDataByIdList]回滚，原因：{}，list = {}",
            ex.getMessage(), JsonUtil.toJson(idsList), ex);
      }
    }
    return ct;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SysUserEntity selectByUserAccount(String account) {
    return sysUserDao.selectByUserAccount(account);
  }

  /**
   * APP登录
   *
   * @param userDTO 账号 密码
   * @return json
   */
  @Override
  public String appLogin(UserDTO userDTO) {
    ServiceResult<Object> result = new ServiceResult<>();
    SysUserEntity entity = sysUserDao.selectByUserAccount(userDTO.getAccount());
    if (entity == null) {
      result.setError("用户不存在");
      return JsonUtil.toJson(result);
    }
    SysUserSecurityEntity dto = sysUserDao.getUserSecurity(entity.getId());
    if (!PasswordHash.validate(dto, userDTO.getPassword())) {
      result.setError("密码错误");
      return JsonUtil.toJson(result);
    }
    AppUserDto appUserDto = new AppUserDto();
    appUserDto.setId(entity.getId());
    appUserDto.setAccount(userDTO.getAccount());
    appUserDto.setUserName(entity.getUserName());
    appUserDto.setRoleId(entity.getRoleId());
    appUserDto.setRoleName(RoleEnum.getNameByRoleId(entity.getRoleId()));
    appUserDto.setAuthId(entity.getAuthId());
    // 生成token
    String token = WebUtil.createToken(entity.getId());
    appUserDto.setToken(token);
    result.setResult(appUserDto);
    result.setSuccess(true);
    return JsonUtil.toJson(result);
  }

  /**
   * 上级管理员
   *
   * @param areaCode 区域
   * @param roleId   角色
   * @return 上级
   */
  @Override
  public SysUserEntity superiorUser(String areaCode, Integer roleId) {
    return sysUserDao.superiorUser(areaCode, roleId);
  }

  /**
   * 非网格员用户
   *
   * @return list
   */
  @Override
  public List<SysUserEntity> nonGrid() {
    return sysUserDao.nonGrid();
  }

}

