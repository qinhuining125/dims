package com.hengtianyi.dims.service.api.impl;

import com.hengtianyi.common.core.base.service.AbstractGenericServiceImpl;
import com.hengtianyi.dims.service.api.RevealFlowService;
import com.hengtianyi.dims.service.dao.RevealFlowDao;
import com.hengtianyi.dims.service.entity.RevealFlowEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RevealFlow接口的实现类
 *
 * @author
 */
@Service
public class RevealFlowServiceImpl extends AbstractGenericServiceImpl<RevealFlowEntity, String>
    implements RevealFlowService {

  private static final Logger LOGGER = LoggerFactory.getLogger(RevealFlowServiceImpl.class);

  @Resource
  private RevealFlowDao revealFlowDao;

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
    PROPERTY_COLUMN.put("revealId", "REVEAL_ID");
    PROPERTY_COLUMN.put("receiveId", "RECEIVE_ID");
    PROPERTY_COLUMN.put("receiveRoleId", "RECEIVE_ROLE_ID");
    PROPERTY_COLUMN.put("remark", "REMARK");
    PROPERTY_COLUMN.put("state", "STATE");
    PROPERTY_COLUMN.put("createTime", "CREATE_TIME");
  }

  /**
   * 注入Mybatis的Dao
   *
   * @return revealFlowDao
   */
  @Override
  public RevealFlowDao getDao() {
    return revealFlowDao;
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
   * @param revealId 举报表单Id
   * @return list
   */
  @Override
  public List<RevealFlowEntity> getAllFlows(String revealId) {
    return revealFlowDao.getAllFlows(revealId);
  }

  /**
   * 根据条件获得指定流程
   *
   * @param RevealFlowEntity 举报流程实体类
   * @return list
   */
  @Override
  public List<RevealFlowEntity> getFlowsByConditons(RevealFlowEntity entity) {
    return revealFlowDao.searchAllData(entity);
  }

}
