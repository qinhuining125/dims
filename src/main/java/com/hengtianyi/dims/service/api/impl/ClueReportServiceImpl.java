package com.hengtianyi.dims.service.api.impl;

import com.hengtianyi.common.core.base.CommonEntityDto;
import com.hengtianyi.common.core.base.CommonPageDto;
import com.hengtianyi.common.core.base.service.AbstractGenericServiceImpl;
import com.hengtianyi.common.core.feature.ServiceResult;
import com.hengtianyi.common.core.util.StringUtil;
import com.hengtianyi.dims.constant.FrameConstant;
import com.hengtianyi.dims.service.api.ClueReportService;
import com.hengtianyi.dims.service.api.SysUserService;
import com.hengtianyi.dims.service.dao.*;
import com.hengtianyi.dims.service.dto.KeyValueDto;
import com.hengtianyi.dims.service.dto.QueryDto;
import com.hengtianyi.dims.service.entity.ClueReportEntity;
import com.hengtianyi.dims.service.entity.SysUserEntity;
import com.hengtianyi.dims.service.entity.TownshipEntity;
import com.hengtianyi.dims.service.entity.VillageEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * ClueReport接口的实现类
 *
 * @author LY
 */
@Service
public class ClueReportServiceImpl extends AbstractGenericServiceImpl<ClueReportEntity, String>
    implements ClueReportService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ClueReportServiceImpl.class);

  @Resource
  private ClueReportDao clueReportDao;
  @Resource
  private ClueFlowDao clueFlowDao;
  @Resource
  private VillageDao villageDao;
  @Resource
  private ReportTypeDao reportTypeDao;

  @Resource
  private TownshipDao townshipDao;

  @Resource
  private SysUserService sysUserService;
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
    PROPERTY_COLUMN.put("userId", "USER_ID");
    PROPERTY_COLUMN.put("reportIds", "REPORT_IDS");
    PROPERTY_COLUMN.put("clueDescribe", "CLUE_DESCRIBE");
    PROPERTY_COLUMN.put("createTime", "CREATE_TIME");
    PROPERTY_COLUMN.put("reportRoleId", "REPORT_ROLE_ID");
  }

  /**
   * 注入Mybatis的Dao
   *
   * @return clueReportDao
   */
  @Override
  public ClueReportDao getDao() {
    return clueReportDao;
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
   * 自定义分页
   *
   * @param dto dto
   * @return
   */
  @Override
  public CommonEntityDto<ClueReportEntity> pagelist(QueryDto dto) {
    Integer count = clueReportDao.pagecount(dto);
    List<ClueReportEntity> list = clueReportDao.pagelist(dto);
    for (ClueReportEntity clueReportEntity : list) {
      clueReportEntity.setReceiveId(clueFlowDao.getReceiveId(clueReportEntity.getId(), 1));
      clueReportEntity.setFlows(clueFlowDao.getAllFlows(clueReportEntity.getId()));
      SysUserEntity sysuser = sysUserService.searchDataById(clueReportEntity.getUserId());
      TownshipEntity town = townshipDao.selectByAreaCode(sysuser.getAreaCode().substring(0,9));
      clueReportEntity.setReportUserAreaName(town.getAreaName());
      List<KeyValueDto> dtoList = new ArrayList<>();
      KeyValueDto keyValueDto = null;
      for (String key : clueReportEntity.getReportIds().split(StringUtil.COMMA)) {
        keyValueDto = new KeyValueDto();
        keyValueDto.setKey(key);
        keyValueDto.setValue(
                reportTypeDao.contentByRoleSortNo(clueReportEntity.getReportRoleId(), Integer.parseInt(key)));
        dtoList.add(keyValueDto);
      }
      clueReportEntity.setDtoList(dtoList);
    }
    CommonEntityDto<ClueReportEntity> result = new CommonEntityDto<>(list);
    result.setCurrentPage(dto.getCurrentPage());
    result.setSize(FrameConstant.PAGE_SIZE);
    result.setTotal(count);
    return result;
  }

  /**
   * 原分页
   *
   * @param dto
   * @return
   */
  @Override
  public CommonPageDto listData(QueryDto dto) {
    try {
      int rowsCount = clueReportDao.pagecount(dto);
      List<ClueReportEntity> listData = clueReportDao.pagelist(dto);
      for (ClueReportEntity entity : listData) {
        SysUserEntity sysuser = sysUserService.searchDataById(entity.getUserId());
        TownshipEntity town = townshipDao.selectByAreaCode(sysuser.getAreaCode().substring(0,9));
        entity.setReportUserAreaName(town.getAreaName());
        List<KeyValueDto> dtoList = new ArrayList<>();
        KeyValueDto keyValueDto = null;
        for (String key : entity.getReportIds().split(StringUtil.COMMA)) {
          keyValueDto = new KeyValueDto();
          keyValueDto.setKey(key);
          keyValueDto.setValue(
              reportTypeDao.contentByRoleSortNo(entity.getReportRoleId(), Integer.parseInt(key)));
          dtoList.add(keyValueDto);
        }
        entity.setDtoList(dtoList);
      }
      CommonPageDto cpDto = new CommonPageDto(listData);
      cpDto.setCurrent(dto.getCurrentPage());
      cpDto.setRowCount(FrameConstant.PAGE_SIZE);
      cpDto.setTotal(rowsCount);
      return cpDto;
    } catch (Exception ex) {
      LOGGER.error("[listData]{}, dto = {}", ex.getMessage(), dto.toJson(), ex);
    }
    return new CommonPageDto();
  }

  /**
   * 查看报告数量
   *
   * @param dto dto
   * @return
   */
  @Override
  public Integer countReport(QueryDto dto) {
    return clueReportDao.pagecount(dto);
  }

  /**
   * 查询所有的上报
   *
   * @param startTime 开始时间
   * @param endTime   结束时间
   * @param areaCode  乡镇编号
   * @return list
   */
  @Override
  public String echartsData(String startTime, String endTime, String areaCode) {
    ServiceResult<Object> result = new ServiceResult();
    try {
      List<ClueReportEntity> reportList = clueReportDao
          .getEchartsData(startTime, endTime, areaCode);
      Map<String, Object> map = count(reportList);
      Integer[] wgys = new Integer[11];
      Integer[] llys = new Integer[11];
      String[] types = new String[11];
      for (int i = 1; i < 12; i++) {
        types[i - 1] = "类型" + i;
        wgys[i - 1] = sumNum(reportList, 1001, i);
        llys[i - 1] = sumNum(reportList, 1002, i);
      }
      map.put("types", types);
      map.put("wgys", wgys);
      map.put("llys", llys);
      map.put("wgynrs", reportTypeDao.contentByRole(1001));
      map.put("llynrs", reportTypeDao.contentByRole(1002));
      List<VillageEntity> villageList = villageDao.areaList(areaCode);
      String[] villageNames = new String[villageList.size()];
      String[] unAccepts = new String[villageList.size()];
      String[] knowTasks = new String[villageList.size()];
      String[] turnToOtherTasks = new String[villageList.size()];
      String[] accepts = new String[villageList.size()];
      String[] completes = new String[villageList.size()];
      for (int i = 0; i < villageList.size(); i++) {
        VillageEntity village = villageList.get(i);
        reportList = clueReportDao.getEchartsData(startTime, endTime, village.getAreaCode());
        Map<String, Object> dataMap = count(reportList);
        villageNames[i] = village.getAreaName();
        unAccepts[i] = dataMap.get("unAccept").toString();
        knowTasks[i] = dataMap.get("knowTask").toString();
        turnToOtherTasks[i] = dataMap.get("turnToOtherTask").toString();
        accepts[i] = dataMap.get("accept").toString();
        completes[i] = dataMap.get("complete").toString();
      }
      map.put("villageNames", villageNames);
      map.put("unAccepts", unAccepts);
      map.put("knowTasks", knowTasks);
      map.put("turnToOtherTasks", turnToOtherTasks);
      map.put("accepts", accepts);
      map.put("completes", completes);
      result.setSuccess(true);
      result.setResult(map);
    } catch (Exception e) {
      LOGGER.error("[echartsData]出错,{}", e.getMessage(), e);
      result.setError("false");
    }
    return result.toJson();
  }

  private Map<String, Object> count(List<ClueReportEntity> reportList) {
    Map<String, Object> map = new HashMap<>();
    Integer unAccept = 0;
    Integer knowTask = 0;
    Integer turnToOtherTask = 0;
    Integer accept = 0;
    Integer complete = 0;

    Integer total = reportList.size();
    for (ClueReportEntity reportEntity : reportList) {
      if (reportEntity.getState() == 0) {
        unAccept += 1;
      }else if (reportEntity.getState() == 3) {
        knowTask += 1;
      } else if (reportEntity.getState() == 4) {
        turnToOtherTask += 1;
      } else if (reportEntity.getState() == 1) {
        accept += 1;
      } else if (reportEntity.getState() == 2) {
        complete += 1;
      }
    }
    map.put("total", total);
    map.put("unAccept", unAccept);
    map.put("knowTask", knowTask);
    map.put("turnToOtherTask", turnToOtherTask);
    map.put("accept", accept);
    map.put("complete", complete);
    return map;
  }

  private int sumNum(List<ClueReportEntity> reportList, Integer roleId, Integer sortNo) {
    int total = 0;
    for (ClueReportEntity entity : reportList) {
      if (entity.getReportRoleId().equals(roleId)
          && entity.getReportIds().indexOf(sortNo.toString()) > -1) {
        total += 1;
      }
    }
    return total;
  }
}
