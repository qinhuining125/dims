package com.hengtianyi.dims.service.api.impl;

import com.hengtianyi.common.core.base.service.AbstractGenericServiceImpl;
import com.hengtianyi.dims.service.api.VillageService;
import com.hengtianyi.dims.service.dao.VillageDao;
import com.hengtianyi.dims.service.entity.VillageEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Village接口的实现类
 *
 * @author LY
 */
@Service
public class VillageServiceImpl extends AbstractGenericServiceImpl<VillageEntity, String>
    implements VillageService {

  private static final Logger LOGGER = LoggerFactory.getLogger(VillageServiceImpl.class);

  @Resource
  private VillageDao villageDao;

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
    PROPERTY_COLUMN = new HashMap<>(3);
    // 格式：实体字段名 - 数据库字段名
    PROPERTY_COLUMN.put("areaCode", "AREA_CODE");
    PROPERTY_COLUMN.put("sortNo", "SORT_NO");
    PROPERTY_COLUMN.put("areaName", "AREA_NAME");
  }

  /**
   * 注入Mybatis的Dao
   *
   * @return villageDao
   */
  @Override
  public VillageDao getDao() {
    return villageDao;
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
   * 乡镇下的村
   *
   * @param townCode 乡镇编号
   * @return 村list
   */
  @Override
  public List<VillageEntity> townChilds(String townCode) {
    return villageDao.townChilds(townCode);
  }

  @Override
  public List<VillageEntity> townChilds2(String townCode) {
    return villageDao.townChilds2(townCode);
  }

  @Override
  public List<VillageEntity> areaList2(String areaCode) {
    return villageDao.areaList2(areaCode);
  }
}
