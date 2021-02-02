package com.hengtianyi.dims.service.api.impl;

import com.hengtianyi.common.core.base.service.AbstractGenericServiceImpl;
import com.hengtianyi.common.core.constant.BaseConstant;
import com.hengtianyi.common.core.util.CollectionUtil;
import com.hengtianyi.dims.service.api.SysAuthService;
import com.hengtianyi.dims.service.dao.RelRoleAuthDao;
import com.hengtianyi.dims.service.dao.SysAuthDao;
import com.hengtianyi.dims.service.entity.SysAuthEntity;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * SysAuth接口的实现类
 *
 * @author LY
 */
@Service
public class SysAuthServiceImpl extends AbstractGenericServiceImpl<SysAuthEntity, String>
    implements SysAuthService {

  private static final Logger LOGGER = LoggerFactory.getLogger(SysAuthServiceImpl.class);

  @Resource
  private SysAuthDao sysAuthDao;
  @Resource
  private RelRoleAuthDao relRoleAuthDao;
  /**
   * 实体与数据表字段的映射
   * <p>格式：实体字段 - 数据库字段</p>
   */
  private static final Map<String, String> PROPERTY_COLUMN;

  /**
   * 默认排序的sql，order by后面的部分，例如: id asc,name desc
   */
  private static final String DEFAULT_ORDER_SQL = "id asc";

  static {
    PROPERTY_COLUMN = new HashMap<>(4);
    // 格式：实体字段名 - 数据库字段名
    PROPERTY_COLUMN.put("id", "ID");
    PROPERTY_COLUMN.put("authName", "AUTH_NAME");
    PROPERTY_COLUMN.put("authCode", "AUTH_CODE");
    PROPERTY_COLUMN.put("enable", "ENABLE");
  }

  /**
   * 注入Mybatis的Dao
   *
   * @return sysAuthDao
   */
  @Override
  public SysAuthDao getDao() {
    return sysAuthDao;
  }

  /**
   * 注入实体与数据库字段的映射
   *
   * @return key：实体属性名，value：数据库字段名
   */
  @Override
  public Map<String, String> getPropertyColumn() {
    return PROPERTY_COLUMN;
  }

  /**
   * 注入排序集合
   *
   * @return 排序集合
   */
  @Override
  public String getDefaultSort() {
    return DEFAULT_ORDER_SQL;
  }

  @Override
  public Integer saveAuthByRoleId(Integer roleId, List<String> authList) {
    int ct = BaseConstant.NUM_0;
    try {
      // 先删除角色权限关系，然后重新添加
      relRoleAuthDao.deleteRoleAuthByRoleId(roleId);
      if (!CollectionUtil.isEmpty(authList)) {
        ct = relRoleAuthDao.addRoleAuthByRoleId(roleId, authList);
      }
    } catch (Exception ex) {
      ct = BaseConstant.NUM_0;
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      LOGGER.error("[SysAuthServiceImpl.saveAuthByRoleId]回滚，原因：{}", ex.getMessage(), ex);
    }
    return ct;
  }

  /**
   * 根据用户ID获取所有可用的权限
   *
   * @param roleId 用户角色ID
   * @return 可用的权限集合
   */
  @Override
  public Set<String> getAuthByRoleId(Integer roleId) {
    // 通过角色权限关系表获取对应的权限（包含禁用的权限）
    List<String> authRoleList = relRoleAuthDao.getRoleAuthByRoleId(roleId);
    if (CollectionUtil.isEmpty(authRoleList)) {
      return Collections.emptySet();
    }
    // 查询启用的权限代码（启用的权限）
    List<String> authCodeList = sysAuthDao.getAuthCodeByAuthId(authRoleList);
    if (CollectionUtil.isEmpty(authCodeList)) {
      return Collections.emptySet();
    }
    return new HashSet<>(authCodeList);
  }
}
