package com.hengtianyi.dims.service.api.impl;

import com.hengtianyi.common.core.base.service.AbstractGenericServiceImpl;
import com.hengtianyi.dims.service.api.ImageClueReportService;
import com.hengtianyi.dims.service.dao.ImageClueReportDao;
import com.hengtianyi.dims.service.entity.ImageClueReportEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ImageClueReport接口的实现类
 *
 * @author
 */
@Service
public class ImageClueReportServiceImpl extends AbstractGenericServiceImpl<ImageClueReportEntity, String>
    implements ImageClueReportService {


  private static final Logger LOGGER = LoggerFactory.getLogger(ImageClueReportServiceImpl.class);

  @Resource
  private ImageClueReportDao imageClueReportDao;

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
    PROPERTY_COLUMN = new HashMap<>(7);
    // 格式：实体字段名 - 数据库字段名
    PROPERTY_COLUMN.put("id", "ID");
    PROPERTY_COLUMN.put("imageUrl", "IMAGE_URL");
    PROPERTY_COLUMN.put("clueId", "CLUE_ID");
    PROPERTY_COLUMN.put("createTime", "CREATE_TIME");
  }

  /**
   * 注入Mybatis的Dao
   *
   * @return taskFlowDao
   */
  @Override
  public ImageClueReportDao getDao() {
    return imageClueReportDao;
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
   * 所有流程
   *
   * @param clueId 举报表单Id
   * @return list
   */
  @Override
  public List<ImageClueReportEntity> getAllImages(String clueId) {
    return imageClueReportDao.getAllImages(clueId);
  }

}
