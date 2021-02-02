package com.hengtianyi.dims.service.api.impl;

import com.hengtianyi.common.core.base.CommonEntityDto;
import com.hengtianyi.common.core.base.CommonPageDto;
import com.hengtianyi.common.core.base.CommonStringDto;
import com.hengtianyi.common.core.base.service.AbstractGenericServiceImpl;
import com.hengtianyi.common.core.feature.ServiceResult;
import com.hengtianyi.common.core.util.CollectionUtil;
import com.hengtianyi.common.core.util.JsonUtil;
import com.hengtianyi.common.core.util.sequence.IdGenUtil;
import com.hengtianyi.common.core.util.sequence.SystemClock;
import com.hengtianyi.dims.constant.FrameConstant;
import com.hengtianyi.dims.service.api.InspectionPublicityService;
import com.hengtianyi.dims.service.api.TownshipService;
import com.hengtianyi.dims.service.dao.TaskFlowDao;
import com.hengtianyi.dims.service.dao.InspectionPublicityDao;
import com.hengtianyi.dims.service.dao.TaskImageDao;
import com.hengtianyi.dims.service.dto.QueryDto;
import com.hengtianyi.dims.service.dto.InspectionPublicityDto;
import com.hengtianyi.dims.service.entity.*;
import com.hengtianyi.dims.utils.WebUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * InspectionPublicity接口的实现类
 *
 * @author LY
 */
@Service
public class InspectionPublicityServiceImpl extends AbstractGenericServiceImpl<InspectionPublicityEntity, String>
        implements InspectionPublicityService {

  private static final Logger LOGGER = LoggerFactory.getLogger(InspectionPublicityServiceImpl.class);
  @Resource
  private TaskImageDao taskImageDao;
  @Resource
  private InspectionPublicityDao inspectionPublicityDao;
  @Resource
  private TaskFlowDao taskFlowDao;
  @Resource
  private TownshipService townshipService;
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
    PROPERTY_COLUMN.put("roleId", "ROLE_ID");
    PROPERTY_COLUMN.put("content", "CONTENT");
    PROPERTY_COLUMN.put("createTime", "CREATE_TIME");
  }

  /**
   * 注入Mybatis的Dao
   *
   * @return inspectionPublicityDao
   */
  @Override
  public InspectionPublicityDao getDao() {
    return inspectionPublicityDao;
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
   * 保存
   *
   * @param dto 任务信息
   * @return i
   */
  @Override
  public Integer saveData(InspectionPublicityDto dto, HttpServletRequest request) {
    try {
      int ct = 0;
      SysUserEntity userEntity = WebUtil.getUser(request);
      for (int i = 0; i < dto.getReceiveId().size(); i++) {
        String taskId = IdGenUtil.uuid32();
        InspectionPublicityEntity inspectionPublicityEntity = new InspectionPublicityEntity();
        inspectionPublicityEntity.setId(taskId);
        inspectionPublicityEntity.setContent(dto.getContent());
        inspectionPublicityEntity.setQR_Code(dto.getQrcode());
        inspectionPublicityEntity.setQR_CodeId(dto.getQrcodeId());
        inspectionPublicityEntity.setUserId(userEntity.getId());
        inspectionPublicityEntity.setRoleId(userEntity.getRoleId());
        inspectionPublicityEntity.setCreateTime(SystemClock.nowDate());
        inspectionPublicityEntity.setState(Short.valueOf("0"));
        inspectionPublicityDao.insert(inspectionPublicityEntity);
        String dtoImages=dto.getImages();
//        String images = dtoImages.substring(1, dtoImages.length()-1);
        String[] strArray = null;
        strArray = dtoImages.split(",");
        for (int j =0;j<strArray.length;j++){
          TaskImageEntity taskImageEntity = new TaskImageEntity();
          taskImageEntity.setId(IdGenUtil.uuid32());
          taskImageEntity.setTaskId(taskId);
          taskImageEntity.setImageURL(strArray[j]);
          taskImageEntity.setCreateTime(SystemClock.nowDate());
          taskImageDao.insert(taskImageEntity);
        }
        TaskFlowEntity flowEntity = new TaskFlowEntity();
        flowEntity.setId(IdGenUtil.uuid32());
        flowEntity.setTaskId(taskId);
        flowEntity.setReceiveId(dto.getReceiveId().get(i));
        flowEntity.setReceiveRoleId(dto.getReceiveRoleId().get(0));
        flowEntity.setState(0);
        flowEntity.setCreateTime(SystemClock.nowDate());
        ct = taskFlowDao.insert(flowEntity);
      }
      return ct;
    } catch (Exception e) {
      LOGGER.error("[任务指派出错],{}", e.getMessage(), e);
    }
    return -1;
  }

  /**
   * 自定义分页
   *
   * @param dto dto
   * @return
   */
  @Override
  public CommonEntityDto<InspectionPublicityEntity> pagelist(QueryDto dto,String IpPort) {
    Integer count = inspectionPublicityDao.pagecount(dto);
    List<InspectionPublicityEntity> list = inspectionPublicityDao.pagelist(dto);
    for (InspectionPublicityEntity inspectionPublicityEntity : list) {
      List<TaskImageEntity>imagesDao=  this.getImages(inspectionPublicityEntity.getId());
      String[] imagesArr = new String[imagesDao.size()];
      for (int i=0;i<imagesDao.size();i++){
//        imagesArr[i]="http://"+IpPort+"/"+imagesDao.get(i).getImageURL();
        imagesArr[i]=FrameConstant.PREFIX_URL+imagesDao.get(i).getImageURL();
      }
      inspectionPublicityEntity.setImgApp(imagesArr);
//      inspectionPublicityEntity.setQR_Code();
      List<TaskFlowEntity> flowEntityList = taskFlowDao.getAllFlows(inspectionPublicityEntity.getId());
      for (TaskFlowEntity flowEntity : flowEntityList) {
        //取出受理人id
        if (flowEntity.getState() == 1) {
          inspectionPublicityEntity.setAcceptUserId(flowEntity.getReceiveId());
          inspectionPublicityEntity.setAcceptRoleId(flowEntity.getReceiveRoleId());
          break;
        }
      }
      inspectionPublicityEntity.setFlows(flowEntityList);
    }

    CommonEntityDto<InspectionPublicityEntity> result = new CommonEntityDto<>(list);
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
      int rowsCount = inspectionPublicityDao.pagecount(dto);
      List<InspectionPublicityEntity> listData = inspectionPublicityDao.pagelist(dto);
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
   * echart数据
   *
   * @param startTime 开始时间
   * @param endTime   结束时间
   * @param areaCode  乡镇编号
   * @return json
   */
  @Override
  public String echartsData(String startTime, String endTime, String areaCode) {
    ServiceResult<Object> result = new ServiceResult();
    try {
      List<InspectionPublicityEntity> entityList = inspectionPublicityDao.getEchartsData(startTime, endTime, areaCode);
      Map<String, Object> map = count(entityList);
      List<TownshipEntity> townList = townshipService.areaList();
      String[] townNames = new String[townList.size()];
      String[] unAccepts = new String[townList.size()];
      String[] accepts = new String[townList.size()];
      String[] completes = new String[townList.size()];
      for (int i = 0; i < townList.size(); i++) {
        entityList = inspectionPublicityDao
                .getEchartsData(startTime, endTime, townList.get(i).getAreaCode());
        Map<String, Object> dataMap = count(entityList);
        townNames[i] = townList.get(i).getAreaName();
        unAccepts[i] = dataMap.get("unAccept").toString();
        accepts[i] = dataMap.get("accept").toString();
        completes[i] = dataMap.get("complete").toString();
      }
      map.put("townNames", townNames);
      map.put("unAccepts", unAccepts);
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

  private Map<String, Object> count(List<InspectionPublicityEntity> list) {
    Map<String, Object> map = new HashMap<>();
    Integer unAccept = 0;
    Integer accept = 0;
    Integer complete = 0;
    for (InspectionPublicityEntity entity : list) {
      if (entity.getState() == null) {
        continue;
      }
      if (entity.getState() == 0 || entity.getState() == 3) {
        unAccept += 1;
      } else if (entity.getState() == 1) {
        accept += 1;
      } else if (entity.getState() == 2) {
        complete += 1;
      }
    }
    map.put("unAccept", unAccept);
    map.put("accept", accept);
    map.put("complete", complete);
    return map;
  }

  /**
   * 查看任务数量
   *
   * @param dto dto
   * @return
   */
  @Override
  public Integer countTask(QueryDto dto) {
    return inspectionPublicityDao.pagecount(dto);
  }
  /**
   * 删除任务
   *
   * @param idsDto idsDto
   * @return
   */
  @Override
  public String deleteInfo(CommonStringDto idsDto){
    ServiceResult<Integer> result = new ServiceResult();
    List<String> idsList = idsDto.getIds();
    if (CollectionUtil.isEmpty(idsList)) {
      result.setError("没有待删除的数据");
      return JsonUtil.toJson(result);
    } else {
      try {
        inspectionPublicityDao.deleteByIds(idsList);
        taskImageDao.deleteByIds(idsList);
        result.setSuccess(true);
      } catch (Exception var6) {
        result.setError("删除数据出错");
      }
      return JsonUtil.toJson(result);
    }
  }
  /**
   * 获取任务图片
   *
   * @param id id
   * @return
   */
  @Override
  public List<TaskImageEntity> getImages(String id){
    TaskImageEntity taskImageEntity =new TaskImageEntity();
    taskImageEntity.setTaskId(id);
    List<TaskImageEntity>  list= taskImageDao.searchAllData(taskImageEntity);
    return list;
  }
}
