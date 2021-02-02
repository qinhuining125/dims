package com.hengtianyi.dims.service.api.impl;

import com.hengtianyi.common.core.base.service.AbstractGenericServiceImpl;
import com.hengtianyi.dims.service.api.ClueFlowService;
import com.hengtianyi.dims.service.dao.ClueFlowDao;
import com.hengtianyi.dims.service.entity.ClueFlowEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * ClueFlow接口的实现类
 *
 * @author LY
 */
@Service
public class ClueFlowServiceImpl extends AbstractGenericServiceImpl<ClueFlowEntity, String>
    implements ClueFlowService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ClueFlowServiceImpl.class);

  @Resource
  private ClueFlowDao clueFlowDao;

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
    PROPERTY_COLUMN = new HashMap<>(6);
    // 格式：实体字段名 - 数据库字段名
    PROPERTY_COLUMN.put("id", "ID");
    PROPERTY_COLUMN.put("clueId", "CLUE_ID");
    PROPERTY_COLUMN.put("state", "STATE");
    PROPERTY_COLUMN.put("receiveId", "RECEIVE_ID");
    PROPERTY_COLUMN.put("remark", "REMARK");
    PROPERTY_COLUMN.put("createTime", "CREATE_TIME");
  }

  /**
   * 注入Mybatis的Dao
   *
   * @return clueFlowDao
   */
  @Override
  public ClueFlowDao getDao() {
    return clueFlowDao;
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
   * 子流程
   *
   * @param clueId 线索id
   * @return list
   */
  @Override
  public List<ClueFlowEntity> getAllFlows(String clueId) {
    return clueFlowDao.getAllFlows(clueId);
  }

  /**
   * @param clueId 线索id
   * @param state  0,接收，1受理
   * @return 接受人员
   */
  @Override
  public String getReceiveId(String clueId, Integer state) {
    return clueFlowDao.getReceiveId(clueId, state);
  }
}
