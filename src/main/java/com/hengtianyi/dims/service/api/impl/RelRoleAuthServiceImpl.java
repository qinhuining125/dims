package com.hengtianyi.dims.service.api.impl;

import com.hengtianyi.common.core.base.service.AbstractGenericServiceImpl;
import com.hengtianyi.dims.service.api.RelRoleAuthService;
import com.hengtianyi.dims.service.dao.RelRoleAuthDao;
import com.hengtianyi.dims.service.entity.RelRoleAuthEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/** 
 * RelRoleAuth接口的实现类
 * @author LY
 */
@Service
public class RelRoleAuthServiceImpl extends AbstractGenericServiceImpl<RelRoleAuthEntity, String>
    implements RelRoleAuthService {
  private static final Logger LOGGER = LoggerFactory.getLogger(RelRoleAuthServiceImpl.class);

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
  private static final String DEFAULT_ORDER_SQL = "roleId asc";
  
  static {
    PROPERTY_COLUMN = new HashMap<>(2);
    // 格式：实体字段名 - 数据库字段名
     PROPERTY_COLUMN.put("roleId", "ROLE_ID");
     PROPERTY_COLUMN.put("authId", "AUTH_ID");
  }

  /**
   * 注入Mybatis的Dao
   * @return relRoleAuthDao
   */
  @Override
  public RelRoleAuthDao getDao() {
    return relRoleAuthDao;
  }

  /**
   * 注入实体与数据库字段的映射
   * @return key：实体属性名，value：数据库字段名
   */
  @Override
  public Map<String, String> getPropertyColumn() {
    return PROPERTY_COLUMN;
  }

  /**
   * 注入排序集合
   * @return 排序集合
   */
  @Override
  public String getDefaultSort() {
    return DEFAULT_ORDER_SQL;
  }

  @Override
  public List<String> getRoleAuthByRoleId(Integer roleId) {
    return relRoleAuthDao.getRoleAuthByRoleId(roleId);
  }
}
