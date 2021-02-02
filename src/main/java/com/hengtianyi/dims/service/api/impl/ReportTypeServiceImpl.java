package com.hengtianyi.dims.service.api.impl;

import com.hengtianyi.common.core.base.service.AbstractGenericServiceImpl;
import com.hengtianyi.common.core.util.StringUtil;
import com.hengtianyi.dims.service.api.ReportTypeService;
import com.hengtianyi.dims.service.dao.ReportTypeDao;
import com.hengtianyi.dims.service.dto.KeyValueDto;
import com.hengtianyi.dims.service.entity.ClueReportEntity;
import com.hengtianyi.dims.service.entity.ReportTypeEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * ReportType接口的实现类
 *
 * @author LY
 */
@Service
public class ReportTypeServiceImpl extends AbstractGenericServiceImpl<ReportTypeEntity, String>
    implements ReportTypeService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ReportTypeServiceImpl.class);

  @Resource
  private ReportTypeDao reportTypeDao;

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
    PROPERTY_COLUMN = new HashMap<>(5);
    // 格式：实体字段名 - 数据库字段名
    PROPERTY_COLUMN.put("id", "ID");
    PROPERTY_COLUMN.put("roleId", "ROLE_ID");
    PROPERTY_COLUMN.put("content", "CONTENT");
    PROPERTY_COLUMN.put("sortNo", "SORT_NO");
    PROPERTY_COLUMN.put("createTime", "CREATE_TIME");
  }

  /**
   * 注入Mybatis的Dao
   *
   * @return reportTypeDao
   */
  @Override
  public ReportTypeDao getDao() {
    return reportTypeDao;
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
  public Integer nextId() {
    return reportTypeDao.maxId() + 1;
  }

  @Override
  public List<KeyValueDto> contents(Integer roleId, String reportIds) {
    List<KeyValueDto> dtoList = new ArrayList<>();
    KeyValueDto keyValueDto = null;
    for (String key : reportIds.split(StringUtil.COMMA)) {
      keyValueDto = new KeyValueDto();
      keyValueDto.setKey(key);
      keyValueDto.setValue(
          reportTypeDao.contentByRoleSortNo(roleId, Integer.parseInt(key)));
      dtoList.add(keyValueDto);
    }
    return dtoList;
  }
}
