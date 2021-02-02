package com.hengtianyi.dims.service.api.impl;

import com.hengtianyi.common.core.base.service.AbstractGenericServiceImpl;
import com.hengtianyi.dims.service.api.TownshipService;
import com.hengtianyi.dims.service.dao.TownshipDao;
import com.hengtianyi.dims.service.entity.TownshipEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Township接口的实现类
 *
 * @author LY
 */
@Service
public class TownshipServiceImpl extends AbstractGenericServiceImpl<TownshipEntity, String>
    implements TownshipService {

  private static final Logger LOGGER = LoggerFactory.getLogger(TownshipServiceImpl.class);

  @Resource
  private TownshipDao townshipDao;

  /**
   * 实体与数据表字段的映射
   * <p>格式：实体字段 - 数据库字段</p>
   */
  private static final Map<String, String> PROPERTY_COLUMN;

  /**
   * 默认排序的sql，order by后面的部分，例如: id asc,name desc
   */
  private static final String DEFAULT_ORDER_SQL = "areaCode asc";

  static {
    PROPERTY_COLUMN = new HashMap<>(2);
    // 格式：实体字段名 - 数据库字段名
    PROPERTY_COLUMN.put("areaCode", "AREA_CODE");
    PROPERTY_COLUMN.put("areaName", "AREA_NAME");
  }

  /**
   * 注入Mybatis的Dao
   *
   * @return townshipDao
   */
  @Override
  public TownshipDao getDao() {
    return townshipDao;
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

  /**
   * 所有权限地区
   *
   * @return list
   */
  @Override
  public List<TownshipEntity> areaList() {
    return townshipDao.searchAllData(new TownshipEntity());
  }
}
