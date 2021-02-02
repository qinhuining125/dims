package com.hengtianyi.dims.api;

import com.hengtianyi.common.core.base.CommonEntityDto;
import com.hengtianyi.common.core.constant.BaseConstant;
import com.hengtianyi.common.core.feature.ServiceResult;
import com.hengtianyi.common.core.util.CollectionUtil;
import com.hengtianyi.common.core.util.JsonUtil;
import com.hengtianyi.common.core.util.sequence.IdGenUtil;
import com.hengtianyi.common.core.util.sequence.SystemClock;
import com.hengtianyi.dims.constant.FrameConstant;
import com.hengtianyi.dims.service.api.SysUserService;
import com.hengtianyi.dims.service.api.TaskFlowService;
import com.hengtianyi.dims.service.api.TaskInfoService;
import com.hengtianyi.dims.service.dao.TaskImageDao;
import com.hengtianyi.dims.service.dto.QueryDto;
import com.hengtianyi.dims.service.dto.TaskInfoDto;
import com.hengtianyi.dims.service.entity.SysUserEntity;
import com.hengtianyi.dims.service.entity.TaskFlowEntity;
import com.hengtianyi.dims.service.entity.TaskImageEntity;
import com.hengtianyi.dims.service.entity.TaskInfoEntity;
import com.hengtianyi.dims.utils.WebUtil;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 线索上报
 *
 * @author LY
 */
@RestController
@RequestMapping(value = "/api/taskInfo")
public class TaskInfoApiController {

  private static final Logger LOGGER = LoggerFactory.getLogger(TaskInfoApiController.class);
  @Resource
  private TaskImageDao taskImageDao;
  @Resource
  private SysUserService sysUserService;
  @Resource
  private TaskInfoService taskInfoService;
  @Resource
  private TaskFlowService taskFlowService;

  /**
   * 任务指派
   *
   * @param dto
   * @return json
   */
  @PostMapping(value = "/saveData.json", produces = BaseConstant.JSON)
  public String saveData(@RequestBody TaskInfoDto dto, HttpServletRequest request) {
    ServiceResult<Object> result = new ServiceResult<>();
    try {
      String userId = WebUtil.getUserIdByToken(request);
      SysUserEntity userEntity = sysUserService.searchDataById(userId);
      if (userEntity.getRoleId() < 1003) {
        result.setError("无指派权限");
      } else {
        int ct = 0;
        int im = 0;
        int tk = 0;
        for (int i = 0; i < dto.getReceiveId().size(); i++) {
          String taskId = IdGenUtil.uuid32();
          TaskInfoEntity taskInfoEntity = new TaskInfoEntity();
          taskInfoEntity.setId(taskId);
          taskInfoEntity.setContent(dto.getContent());
          taskInfoEntity.setUserId(userId);
          taskInfoEntity.setRoleId(userEntity.getRoleId());
          taskInfoEntity.setCreateTime(SystemClock.nowDate());
          ct = taskInfoService.insertData(taskInfoEntity);
          String dtoImages=dto.getImages();
          if (dtoImages!=null){
            String[] strArray = null;
            strArray = dtoImages.split(",");
            for (int j =0;j<strArray.length;j++){
              TaskImageEntity taskImageEntity = new TaskImageEntity();
              taskImageEntity.setId(IdGenUtil.uuid32());
              taskImageEntity.setTaskId(taskId);
              taskImageEntity.setImageURL(strArray[j].trim());
              taskImageEntity.setCreateTime(SystemClock.nowDate());
              im=taskImageDao.insert(taskImageEntity);
            }
          }

          TaskFlowEntity flowEntity = new TaskFlowEntity();
          flowEntity.setId(IdGenUtil.uuid32());
          flowEntity.setTaskId(taskId);
          flowEntity.setReceiveId(dto.getReceiveId().get(i));
          flowEntity.setReceiveRoleId(dto.getReceiveRoleId().get(i));
          flowEntity.setState(0);
          flowEntity.setCreateTime(SystemClock.nowDate());
          tk = taskFlowService.insertData(flowEntity);
        }

        result.setSuccess(tk > 0 && ct > 0 );
        result.setResult(tk > 0 && ct > 0 );
      }
    } catch (Exception e) {
      LOGGER.error("[saveData]上报出错,{}", e.getMessage(), e);
      result.setError("任务指派出错");
    }
    return result.toJson();
  }

  /**
   * 受理
   *
   * @return
   */
  @PostMapping(value = "/accept.json", produces = BaseConstant.JSON)
  public String accept(@RequestBody TaskFlowEntity flowEntity, HttpServletRequest request) {
    ServiceResult<Object> result = new ServiceResult<>();
    try {
      String userId = WebUtil.getUserIdByToken(request);
      TaskInfoEntity taskInfoEntity = taskInfoService.searchDataById(flowEntity.getTaskId());
      List<TaskFlowEntity> taskFlowList = taskFlowService
              .searchAllData(flowEntity, Collections.singletonMap("createTime", "desc"));
      if (CollectionUtil.isNotEmpty(taskFlowList)) {
        TaskFlowEntity taskFlowEntity = taskFlowList.get(0);
        if (userId.equals(taskFlowEntity.getReceiveId())) {
          //修改任务状态
          taskInfoEntity.setState(Short.valueOf("1"));
          taskInfoService.updateData(taskInfoEntity);
          //插入一条流程
          taskFlowEntity.setId(IdGenUtil.uuid32());
          taskFlowEntity.setState(1);
          taskFlowEntity.setRemark(flowEntity.getRemark());
          taskFlowEntity.setCreateTime(SystemClock.nowDate());
          int ct = taskFlowService.insertData(taskFlowEntity);
          result.setSuccess(ct > 0);
          result.setResult(ct > 0);
        } else {
          result.setError("无受理权限");
        }
      } else {
        result.setError("不存在的任务流程");
      }
    } catch (Exception e) {
      LOGGER.error("[accept]受理出错,{}", e.getMessage(), e);
      result.setError("任务受理出错");
    }
    return result.toJson();
  }

  /**
   * 指派
   *
   * @param flowEntity
   * @param request
   * @return
   */
  @PostMapping(value = "/assign.json", produces = BaseConstant.JSON)
  public String assign(@RequestBody TaskFlowEntity flowEntity, HttpServletRequest request) {
    ServiceResult<Object> result = new ServiceResult<>();
    try {
      TaskInfoEntity taskInfoEntity = taskInfoService.searchDataById(flowEntity.getTaskId());
      taskInfoEntity.setState(Short.valueOf("3"));
      taskInfoService.updateData(taskInfoEntity);

      flowEntity.setId(IdGenUtil.uuid32());
      flowEntity.setState(3);
      flowEntity.setCreateTime(SystemClock.nowDate());
      int ct = taskFlowService.insertData(flowEntity);
      result.setSuccess(ct > 0);
      result.setResult(ct > 0);
    } catch (Exception e) {
      LOGGER.error("[assign]指派出错,{}", e.getMessage(), e);
      result.setError("任务指派出错");
    }
    return result.toJson();
  }

  /**
   * 办结
   *
   * @param flowEntity
   * @param request
   * @return
   */
  @PostMapping(value = "/close.json", produces = BaseConstant.JSON)
  public String close(@RequestBody TaskFlowEntity flowEntity, HttpServletRequest request) {
    ServiceResult<Object> result = new ServiceResult<>();
    try {
      TaskInfoEntity taskInfoEntity = taskInfoService.searchDataById(flowEntity.getTaskId());
      taskInfoEntity.setState(Short.valueOf("2"));
      taskInfoService.updateData(taskInfoEntity);

      String userId = WebUtil.getUserIdByToken(request);
      SysUserEntity userEntity = sysUserService.searchDataById(userId);
      flowEntity.setId(IdGenUtil.uuid32());
      flowEntity.setReceiveId(userId);
      flowEntity.setReceiveRoleId(userEntity.getRoleId());
      flowEntity.setState(2);
      flowEntity.setCreateTime(SystemClock.nowDate());
      int ct = taskFlowService.insertData(flowEntity);
      result.setSuccess(ct > 0);
      result.setResult(ct > 0);
    } catch (Exception e) {
      LOGGER.error("[close]办结出错,{},{}", e.getMessage(), JsonUtil.toJson(flowEntity), e);
      result.setError("任务办结出错");
    }
    return result.toJson();
  }

  /**
   * 详情
   *
   * @param taskId 任务Id
   * @return
   */
  @GetMapping(value = "/detail.json", produces = BaseConstant.JSON)
  public String detail(@RequestParam String taskId) {
    ServiceResult<Object> result = new ServiceResult<>();
    try {
      TaskInfoEntity entity = taskInfoService.searchDataById(taskId);
      entity.setFlows(taskFlowService.getAllFlows(taskId));
      entity.setImg(taskInfoService.getImages(taskId));
      result.setResult(entity);
      result.setSuccess(true);
    } catch (Exception e) {
      LOGGER.error("[detail]出错,{}", e.getMessage(), e);
      result.setError("详情页出错");
    }
    return result.toJson();
  }

  /**
   * 分页列表
   *
   * @param dto
   * @return
   */
  @PostMapping(value = "/pagelist.json", produces = BaseConstant.JSON)
  public String list(@RequestBody CommonEntityDto<TaskInfoEntity> dto, HttpServletRequest request) {
    ServiceResult<Object> result = new ServiceResult<>();
    try {
      String userId = WebUtil.getUserIdByToken(request);
      SysUserEntity userEntity = sysUserService.searchDataById(userId);
      Integer roleId = userEntity.getRoleId();
      QueryDto queryDto = new QueryDto();
      queryDto.setFirst((dto.getCurrentPage() - 1) * FrameConstant.PAGE_SIZE);
      queryDto.setEnd(dto.getCurrentPage() * FrameConstant.PAGE_SIZE);
      queryDto.setRoleId(roleId);
      if (roleId < 1005) {
        queryDto.setUserId(userId);
      }
      if (dto.getQuery() != null) {
        if (dto.getQuery().getState() != null) {
          queryDto.setState(dto.getQuery().getState().intValue());
        }
        queryDto.setStartTime(dto.getQuery().getStartTime());
        queryDto.setEndTime(dto.getQuery().getEndTime());
        queryDto.setAreaCode(dto.getQuery().getAreaCode());
      }
      String IpPort=request.getLocalAddr()+":"+request.getLocalPort();
      System.out.println(IpPort);
      CommonEntityDto<TaskInfoEntity> cpDto = taskInfoService.pagelist(queryDto,IpPort);
      result.setResult(cpDto);
      result.setSuccess(true);
    } catch (Exception e) {
      LOGGER.error("[list]出错,{}", e.getMessage(), e);
      result.setError("待审批列表出错");
    }
    return result.toJson();
  }

  @GetMapping(value = "/state.json", produces = BaseConstant.JSON)
  public String state(HttpServletRequest request) {
    ServiceResult<Object> result = new ServiceResult<>();
    try {
      //当前用户
      String userId = WebUtil.getUserIdByToken(request);
      Map<String, Object> map = new HashMap<>(2);
      SysUserEntity userEntity = sysUserService.searchDataById(userId);
      Integer roleId = userEntity.getRoleId();
      QueryDto queryDto = new QueryDto();
      queryDto.setUserId(userId);
      queryDto.setRoleId(roleId);
      queryDto.setState(0);
      map.put("accept", taskInfoService.countTask(queryDto) > 0);
      queryDto.setState(1);
      map.put("close", taskInfoService.countTask(queryDto) > 0);
      result.setResult(map);
      result.setSuccess(true);
    } catch (Exception e) {
      LOGGER.error("[detail]出错,{}", e.getMessage(), e);
      result.setError("详情页出错");
    }
    return result.toJson();
  }
}
