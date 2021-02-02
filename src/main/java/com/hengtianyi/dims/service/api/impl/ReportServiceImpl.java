package com.hengtianyi.dims.service.api.impl;

import com.hengtianyi.common.core.base.service.AbstractGenericServiceImpl;
import com.hengtianyi.dims.service.api.ReportService;
import com.hengtianyi.dims.service.dao.ReportDao;
import com.hengtianyi.dims.service.entity.ReportEntity;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Report接口的实现类
 *
 * @author LY
 */
@Service
public class ReportServiceImpl extends AbstractGenericServiceImpl<ReportEntity, String>
    implements ReportService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ReportServiceImpl.class);

  @Resource
  private ReportDao reportDao;

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
    PROPERTY_COLUMN.put("dateTime", "DATE_TIME");
    PROPERTY_COLUMN.put("serialNo", "SERIAL_NO");
    PROPERTY_COLUMN.put("reportNo", "REPORT_NO");
  }

  /**
   * 注入Mybatis的Dao
   *
   * @return reportDao
   */
  @Override
  public ReportDao getDao() {
    return reportDao;
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
   * 查询最大序列号
   *
   * @param areaCode 村编号
   * @return 最大序列号
   */
  @Override
  public String maxSerialNo(String areaCode) {
    return reportDao.maxSerialNo(areaCode);
  }
}
