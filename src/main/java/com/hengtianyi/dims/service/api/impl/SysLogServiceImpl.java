package com.hengtianyi.dims.service.api.impl;

import com.alibaba.fastjson.JSONObject;
import com.hengtianyi.common.core.base.service.AbstractGenericDegradeServiceImpl;
import com.hengtianyi.common.core.util.JsonUtil;
import com.hengtianyi.common.core.util.StringUtil;
import com.hengtianyi.common.core.util.cache.CacheAdvUtil;
import com.hengtianyi.common.core.util.sequence.IdGenUtil;
import com.hengtianyi.dims.constant.FrameConstant;
import com.hengtianyi.dims.service.api.SysLogService;
import com.hengtianyi.dims.service.dao.SysLogDao;
import com.hengtianyi.dims.service.entity.SysLogEntity;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * SysLog接口的实现类
 *
 * @author wangfanjun
 */
@Service
public class SysLogServiceImpl extends AbstractGenericDegradeServiceImpl<SysLogEntity, Long>
    implements SysLogService {

  private static final Logger LOGGER = LoggerFactory.getLogger(SysLogServiceImpl.class);
  private static final String CACHE_NAME = "SysLogService.cache";
  /**
   * 每条日志对应2条长文本记录（入参和返回）
   */
  private static final int PARAMETER_RESULT_COUNT = 2;
  /**
   * 默认排序的sql，order by后面的部分，例如: id asc,name desc
   */
  private static final String DEFAULT_ORDER_SQL = "id desc";
  /**
   * 默认的日志查询起始日期，2018-1-1
   */
  private static final long DEFAULT_FIRST_TIME = 1514736000000L;
  @Resource
  private SysLogDao sysLogDao;

  /**
   * 根据id获取数据
   *
   * @param id 主键ID
   * @return 实体对象
   */
  @Override
  public SysLogEntity searchDataById(final Long id) {
    return sysLogDao.selectByPrimaryKey(id);
  }

  /**
   * 通过条件，查询数据集合，返回分页数据
   *
   * @param model 包含查询条件的对象实体
   * @return 符合条件的数据总数量
   */
  @Override
  public Integer searchDataCount(final SysLogEntity model) {
    return sysLogDao.searchDataCount(model);
  }

  /**
   * 通过条件，查询数据集合，返回分页数据
   *
   * @param model 包含查询条件的对象实体
   * @param sorts 排序字段集合，固定为null
   * @return 符合条件的数据分页集合
   */
  @Override
  public List<SysLogEntity> searchData(final SysLogEntity model, final Map<String, String> sorts) {
    model.setOrderSql(DEFAULT_ORDER_SQL);
    return sysLogDao.searchData(model);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void asyncSaveLogData(SysLogEntity entity) {
    try {
      entity.setId(IdGenUtil.getId());
      String result = entity.getResult();
      if (entity.getServiceResult()) {
        // 如果是@ResponseBody，需要拆解ServiceResult，从返回的结果中获取success
        entity.setStatus(isSuccess(result) ? FrameConstant.SUCCESS : FrameConstant.FAILURE);
      } else {
        // 非@ResponseBody，如果return的视图名是errors，也标记为失败
        if (StringUtil.equals(FrameConstant.VIEW_ERROR, result)) {
          entity.setStatus(FrameConstant.FAILURE);
        }
      }
      int ct = sysLogDao.insertParameterAndResult(entity.getId(), entity.getParameter(), result);
      if (ct == PARAMETER_RESULT_COUNT) {
        sysLogDao.insert(entity);
      }
    } catch (Exception ex) {
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      LOGGER.error("[SysLogServiceImpl.asyncSaveLogData]回滚，原因：{}，entity = {}",
          ex.getMessage(), JsonUtil.toJson(entity), ex);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean removeAll() {
    try {
      sysLogDao.truncateLog();
      sysLogDao.truncateLogText();
      return true;
    } catch (Exception ex) {
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      LOGGER.error("[SysLogServiceImpl.removeAll]回滚，原因：{}", ex.getMessage(), ex);
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getParameter(Long logId) {
    return sysLogDao.getParameter(logId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getResult(Long logId) {
    return sysLogDao.getResult(logId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Long getFirstTime() {
    // 从缓存获取，如果失败则读数据库
    Long firstTime = CacheAdvUtil.get(CACHE_NAME, Long.class);
    if (firstTime == null) {
      firstTime = sysLogDao.getFirstTime();
      if (firstTime != null) {
        // 存入缓存
        CacheAdvUtil.put(CACHE_NAME, firstTime);
        return firstTime;
      }
    }
    return DEFAULT_FIRST_TIME;
  }

  /**
   * 判断结果是否成功
   *
   * @param result json字符串
   * @return true - 成功
   */
  private boolean isSuccess(final String result) {
    try {
      // 判断success，重新设置成功标志位
      JSONObject json = JSONObject.parseObject(result);
      return json.getBoolean("success");
    } catch (Exception e) {
      // 转换失败，设置为false
    }
    return false;
  }
}
