package com.hengtianyi.dims.api;

import com.hengtianyi.common.core.base.CommonEntityDto;
import com.hengtianyi.common.core.constant.BaseConstant;
import com.hengtianyi.common.core.feature.ServiceResult;
import com.hengtianyi.common.core.util.TimeUtil;
import com.hengtianyi.common.core.util.sequence.IdGenUtil;
import com.hengtianyi.common.core.util.sequence.SystemClock;
import com.hengtianyi.dims.constant.FrameConstant;
import com.hengtianyi.dims.service.api.ClueFlowService;
import com.hengtianyi.dims.service.api.ClueReportService;
import com.hengtianyi.dims.service.api.RelUserAreaService;
import com.hengtianyi.dims.service.api.ReportService;
import com.hengtianyi.dims.service.api.SysUserService;
import com.hengtianyi.dims.service.api.VillageService;
import com.hengtianyi.dims.service.dto.QueryDto;
import com.hengtianyi.dims.service.entity.ClueFlowEntity;
import com.hengtianyi.dims.service.entity.ClueReportEntity;
import com.hengtianyi.dims.service.entity.ReportEntity;
import com.hengtianyi.dims.service.entity.SysUserEntity;
import com.hengtianyi.dims.service.entity.VillageEntity;
import com.hengtianyi.dims.utils.WebUtil;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.math.NumberUtils;
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
@RequestMapping(value = "/api/clueReport")
public class ClueReportApiController {

  private static final Logger LOGGER = LoggerFactory.getLogger(ClueReportApiController.class);
  @Resource
  private ClueReportService clueReportService;
  @Resource
  private SysUserService sysUserService;
  @Resource
  private ClueFlowService clueFlowService;
  @Resource
  private RelUserAreaService relUserAreaService;
  @Resource
  private VillageService villageService;
  @Resource
  private ReportService reportService;
  private static final String DEFAULT_FORMAT = "yyyyMMdd";

  /**
   * @param entity 实体
   * @return json
   */
  @PostMapping(value = "/saveData.json", produces = BaseConstant.JSON)
  public String saveData(@RequestBody ClueReportEntity entity, HttpServletRequest request) {
    ServiceResult<Object> result = new ServiceResult<>();
    try {
      String userId = WebUtil.getUserIdByToken(request);
      SysUserEntity userEntity = sysUserService.searchDataById(userId);
      Integer roleId = userEntity.getRoleId();
      if (roleId > 1002) {
        //只有网格员和联络员可以上报
        result.setError("无上报权限");
      } else {
        String areaCode = userEntity.getAreaCode().substring(0, 12);
        VillageEntity villageEntity = villageService.searchDataById(areaCode);
        //添加报表编号
        String date = TimeUtil.format(new Date(), DEFAULT_FORMAT);
        String serialNo = reportService.maxSerialNo(areaCode);
        int nowNo = NumberUtils.toInt(serialNo, 0) + 1;
        serialNo = int2Str(nowNo, 3);
        String reportNo =
            villageEntity.getAreaName() + villageEntity.getSortNo() + "-" + date + "-" + serialNo;
        ReportEntity reportEntity = new ReportEntity(areaCode, serialNo, reportNo);
        reportService.insertData(reportEntity);

        String clueId = IdGenUtil.uuid32();
        entity.setId(clueId);
        entity.setReportRoleId(roleId);
        entity.setCreateTime(SystemClock.nowDate());
        entity.setUserId(WebUtil.getUserIdByToken(request));
        entity.setClueNo(reportNo);
        int ct = clueReportService.insertData(entity);

        //上级管理员
        SysUserEntity adminUser = sysUserService
            .superiorUser(userEntity.getAreaCode().substring(0, 9), 1003);
        //插入一条流程
        ClueFlowEntity flowEntity = new ClueFlowEntity();
        flowEntity.setId(IdGenUtil.uuid32());
        flowEntity.setClueId(clueId);
        flowEntity.setState(0);
        flowEntity.setReceiveId(adminUser.getId());
        flowEntity.setCreateTime(SystemClock.nowDate());
        int cf = clueFlowService.insertData(flowEntity);

        result.setSuccess(ct > 0 && cf > 0);
        result.setResult(ct > 0 && cf > 0);
      }
    } catch (Exception e) {
      LOGGER.error("[saveData]上报出错,{}", e.getMessage(), e);
      result.setError("网格员上报出错");
    }
    return result.toJson();
  }

  /**
   * 数字位数补齐
   *
   * @param num    数字
   * @param length 长度
   * @return 00x
   */
  private String int2Str(int num, int length) {
    String numStr = String.valueOf(num);
    String prefix = "";
    for (int i = 1; i <= length - numStr.length(); i++) {
      prefix += "0";
    }
    numStr = prefix + numStr;
    return numStr;
  }

  /**
   * 所有报送内容
   *
   * @param dto 分页
   * @return
   */
  @PostMapping(value = "/pagelist.json", produces = BaseConstant.JSON)
  public String pagelist(@RequestBody CommonEntityDto<ClueReportEntity> dto,
      HttpServletRequest request) {
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
      queryDto.setAuthId(userEntity.getAuthId());
      if (dto.getQuery() != null) {
        if (dto.getQuery().getState() != null) {
          queryDto.setState(dto.getQuery().getState().intValue());
        }
        queryDto.setStartTime(dto.getQuery().getStartTime());
        queryDto.setEndTime(dto.getQuery().getEndTime());
        queryDto.setAreaCode(dto.getQuery().getAreaCode());
      }
      CommonEntityDto<ClueReportEntity> cpDto = clueReportService.pagelist(queryDto);
      result.setResult(cpDto);
      result.setSuccess(true);
    } catch (Exception e) {
      LOGGER.error("[list]出错,{}", e.getMessage(), e);
      result.setError("待审批列表出错");
    }
    return result.toJson();
  }

  /**
   * 上报详情
   *
   * @param clueId 线索id
   * @return json
   */
  @GetMapping(value = "/detail.json", produces = BaseConstant.JSON)
  public String detail(@RequestParam String clueId) {
    ServiceResult<Object> result = new ServiceResult<>();
    try {
      ClueReportEntity entity = clueReportService.searchDataById(clueId);
      entity.setFlows(clueFlowService.getAllFlows(clueId));
      result.setResult(entity);
      result.setSuccess(true);
    } catch (Exception e) {
      LOGGER.error("[detail]出错,{}", e.getMessage(), e);
      result.setError("详情页出错");
    }
    return result.toJson();
  }

  /**
   * 受理
   *
   * @return
   */
  @PostMapping(value = "/accept.json", produces = BaseConstant.JSON)
  public String accept(@RequestBody ClueFlowEntity flowEntity, HttpServletRequest request) {
    return business(flowEntity, request, 1);
  }

  /**
   * 办结
   *
   * @return
   */
  @PostMapping(value = "/close.json", produces = BaseConstant.JSON)
  public String close(@RequestBody ClueFlowEntity flowEntity, HttpServletRequest request) {
    return business(flowEntity, request, 2);
  }

  /**
   * 已知晓
   *
   * @return
   */
  @PostMapping(value = "/knowTask.json", produces = BaseConstant.JSON)
  public String knowTask(@RequestBody ClueFlowEntity flowEntity, HttpServletRequest request) {
    return business(flowEntity, request, 3);
  }


  /**
   * 已转办
   *
   * @returnknowTask
   */
  @PostMapping(value = "/turnToOtherTask.json", produces = BaseConstant.JSON)
  public String turnToOtherTask(@RequestBody ClueFlowEntity flowEntity, HttpServletRequest request) {
    return business(flowEntity, request, 4);
  }

  /**
   * 业务处理
   *
   * @param flowEntity
   * @param request
   * @param state      1受理，2办结，3已知晓，4已转办
   * @return
   */
  private String business(ClueFlowEntity flowEntity, HttpServletRequest request, Integer state) {
    ServiceResult<Object> result = new ServiceResult<>();
    try {
      ClueReportEntity clueReportEntity = clueReportService.searchDataById(flowEntity.getClueId());
      SysUserEntity reportUser = sysUserService.searchDataById(clueReportEntity.getUserId());
      //当前用户
      String userId = WebUtil.getUserIdByToken(request);
      Boolean flag = false;
      if (state == 1 || state == 3 || state == 4) {
        //受理,已知晓，已转办，可以是上级或联系室
        //接收id
        String receiveId = clueFlowService.getReceiveId(flowEntity.getClueId(), 0);
        if (userId.equals(receiveId) || relUserAreaService
            .contactAdmin(clueReportEntity.getUserId(), userId)) {
          flag = true;
        }
      } else if (state == 2) {
        String acceptId = clueFlowService.getReceiveId(flowEntity.getClueId(), 1);
        //办结只能是自己办结
        if (userId.equals(acceptId)) {
          flag = true;
        }
      }
      if (flag) {
        //更改线索状态
        clueReportEntity.setState(state.shortValue());
        int cr = clueReportService.updateData(clueReportEntity);

        //插入一条流程
        flowEntity.setId(IdGenUtil.uuid32());
        flowEntity.setState(state);
        flowEntity.setReceiveId(userId);
        flowEntity.setCreateTime(SystemClock.nowDate());
        int ct = clueFlowService.insertData(flowEntity);
        result.setSuccess(cr > 0 && ct > 0);
        result.setResult(cr > 0 && ct > 0);
      } else {
        result.setError("无操作权限");
      }
    } catch (Exception e) {
      result.setError("操作失败");
      LOGGER.error("[accept]失败,{}", e.getMessage(), e);
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
      queryDto.setAuthId(userEntity.getAuthId());
      queryDto.setState(0);
      map.put("accept", clueReportService.countReport(queryDto) > 0);
      queryDto.setState(1);
      map.put("close", clueReportService.countReport(queryDto) > 0);
      result.setResult(map);
      result.setSuccess(true);
    } catch (Exception e) {
      LOGGER.error("[state]出错,{}", e.getMessage(), e);
      result.setError("详情页出错");
    }
    return result.toJson();
  }
}
