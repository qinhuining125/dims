package com.hengtianyi.dims.api;

import com.hengtianyi.common.core.base.CommonEntityDto;
import com.hengtianyi.common.core.constant.BaseConstant;
import com.hengtianyi.common.core.feature.ServiceResult;
import com.hengtianyi.common.core.util.StringUtil;
import com.hengtianyi.common.core.util.TimeUtil;
import com.hengtianyi.common.core.util.sequence.IdGenUtil;
import com.hengtianyi.common.core.util.sequence.SystemClock;
import com.hengtianyi.dims.constant.FrameConstant;
import com.hengtianyi.dims.constant.RoleEnum;
import com.hengtianyi.dims.service.api.*;
import com.hengtianyi.dims.service.dao.ImageClueReportDao;
import com.hengtianyi.dims.service.dao.ReportTypeDao;
import com.hengtianyi.dims.service.dto.ClueReportDto;
import com.hengtianyi.dims.service.dto.KeyValueDto;
import com.hengtianyi.dims.service.dto.QueryDto;
import com.hengtianyi.dims.service.entity.*;
import com.hengtianyi.dims.utils.WebUtil;

import java.util.*;
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
  private ImageClueReportDao imageClueReportDao;


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

  @Resource
  private ReportTypeDao reportTypeDao;

  /**
   * @param dto 实体
   * @return json
   */
  @PostMapping(value = "/saveData.json", produces = BaseConstant.JSON)
  public String saveData(@RequestBody ClueReportDto dto, HttpServletRequest request) {
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
//        ReportEntity reportEntity = new ReportEntity();
//        reportEntity.setAreaCode(areaCode);
//        reportEntity.setSerialNo(serialNo);
//        reportEntity.setReportNo(reportNo);
        reportService.insertData(reportEntity);

        StringBuffer sb = new StringBuffer();
        String[] ids = dto.getReportIds().split(StringUtil.COMMA);
        for (int i=0; i<ids.length; i++) {
          sb.append("[").append(ids[i]).append("-").append(reportTypeDao.contentByRoleSortNo(userEntity.getRoleId(), Integer.parseInt(ids[i]))).append("]").append(",");
        }
        String reportContentsResult = sb.toString().substring(0,sb.toString().length()-1);
        ClueReportEntity entity = new ClueReportEntity();
        entity.setReportContents(reportContentsResult);

        String clueId = IdGenUtil.uuid32();
        entity.setReportIds(dto.getReportIds());
        entity.setClueDescribe(dto.getClueDescribe());
        entity.setId(clueId);
        entity.setReportRoleId(roleId);
        entity.setToVillageMgr(dto.getToVillageMgr());
        entity.setCreateTime(SystemClock.nowDate());
        entity.setUserId(WebUtil.getUserIdByToken(request));
        entity.setClueNo(reportNo);

        int ct = clueReportService.insertData(entity);

        //上级管理员
        SysUserEntity adminUser;

        //判断是否为空
        boolean flag = entity.getToVillageMgr()!= null && entity.getToVillageMgr().equals("") ;
        if(entity.getToVillageMgr().equals("上报给村干部/社区书记")){//上报给村干部/社区书记
          adminUser = sysUserService
                  .superiorUser(userEntity.getAreaCode().substring(0, 12),1012);
        } else if(entity.getToVillageMgr().equals("上报给县信访室")){
          adminUser = sysUserService
                  .superiorUser(userEntity.getAreaCode().substring(0, 6),1004);
        } else {//上报给乡镇纪委管理员
          adminUser = sysUserService
                  .superiorUser(userEntity.getAreaCode().substring(0, 9),1003);
        }

        //上传图片 这里需要对多图片进行处理
        List<String> ss=dto.getImageArray();
        if(ss.size()>0){
          for(int i=0;i < ss.size() ;i++){
            ImageClueReportEntity imageClueReportEntity = new ImageClueReportEntity();
            String imageId = IdGenUtil.uuid32();
            imageClueReportEntity.setId(imageId);
            imageClueReportEntity.setType(ImageClueReportEntity.TYPE_IMAGE);
            imageClueReportEntity.setClueId(clueId);
            imageClueReportEntity.setImageUrl(ss.get(i));
            imageClueReportEntity.setCreateTime(SystemClock.nowDate());
            imageClueReportDao.insert(imageClueReportEntity);
          }
        }
        List<String> ssAudio=dto.getAudioArray();
        if(ssAudio.size()>0){
          for(int i=0;i < ssAudio.size() ;i++){
            ImageClueReportEntity imageClueReportEntity = new ImageClueReportEntity();
            String audioId = IdGenUtil.uuid32();
            imageClueReportEntity.setId(audioId);
            imageClueReportEntity.setType(ImageClueReportEntity.TYPE_AUDIO);
            imageClueReportEntity.setClueId(clueId);
            imageClueReportEntity.setImageUrl(ssAudio.get(i));
            imageClueReportEntity.setCreateTime(SystemClock.nowDate());
            imageClueReportDao.insert(imageClueReportEntity);
          }
        }

        List<String> ssVideo=dto.getVideoArray();
        if(ssVideo.size()>0){
          for(int i=0;i < ssVideo.size() ;i++){
            ImageClueReportEntity imageClueReportEntity = new ImageClueReportEntity();
            String videoId = IdGenUtil.uuid32();
            imageClueReportEntity.setId(videoId);
            imageClueReportEntity.setType(ImageClueReportEntity.TYPE_VIDEO);
            imageClueReportEntity.setClueId(clueId);
            imageClueReportEntity.setImageUrl(ssVideo.get(i));
            imageClueReportEntity.setCreateTime(SystemClock.nowDate());
            imageClueReportDao.insert(imageClueReportEntity);
          }
        }

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
      if (roleId < 1005 || roleId==1012 || (roleId>=3000 && roleId<=3999)) {
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
      entity.setImg(clueReportService.getAttachmentsByIdType(clueId, ImageClueReportEntity.TYPE_IMAGE));
      entity.setAudio(clueReportService.getAttachmentsByIdType(clueId, ImageClueReportEntity.TYPE_AUDIO));
      entity.setVideo(clueReportService.getAttachmentsByIdType(clueId, ImageClueReportEntity.TYPE_VIDEO));
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
  public String turnToOtherTask(@RequestBody ClueReportEntity entity, HttpServletRequest request) {
//    return business(flowEntity, request, 4);

    ServiceResult<Object> result = new ServiceResult<>();
    try {
      ClueReportEntity clueReportEntity = clueReportService.searchDataById(entity.getId());
      SysUserEntity u = sysUserService.searchDataById(clueReportEntity.getUserId());

      int roleId = entity.getRoleId();//获取前台界面选择的转办到的人的角色；
      SysUserEntity adminUser;//分配给的人员
      if (roleId == 1012) {
        //村干部
        adminUser = sysUserService
                .superiorUser(u.getAreaCode().substring(0, 12), 1012);
      } else {
        //乡镇站所或者乡镇纪委管理员
        adminUser = sysUserService
                .superiorUser(u.getAreaCode().substring(0, 9), roleId);
      }
      //更改线索状态
      clueReportEntity.setState(Short.parseShort("0"));//转办的，都设定为0，未处理，因为转办过去之后，需要新的用户重新受理并办结
      int cr = clueReportService.updateData(clueReportEntity);

      //插入一条流程
      ClueFlowEntity flowEntity = new ClueFlowEntity();
      flowEntity.setId(IdGenUtil.uuid32());
      flowEntity.setClueId(entity.getId());
      flowEntity.setState(0);//转办的，都设定为0，未处理，因为转办过去之后，需要新的用户重新受理并办结
      flowEntity.setReceiveId(adminUser.getId());
      flowEntity.setCreateTime(SystemClock.nowDate());
      int ct = clueFlowService.insertData(flowEntity);
      result.setSuccess(cr > 0 && ct > 0);
      result.setResult(cr > 0 && ct > 0);

    } catch (Exception e) {
      result.setError("操作失败");
      LOGGER.error("[accept]失败,{}", e.getMessage(), e);
    }
    return result.toJson();

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


  /**
   * 判断用户是否是城区，滨河城区管委会
   * @param userId
   * @return
   */
  @GetMapping(value = "/checkUserIsChengQu.json", produces = BaseConstant.JSON)
  public String checkUserIsChengQu(@RequestParam("userId") String userId) {
    ServiceResult<Object> result = new ServiceResult<>();
    SysUserEntity userEntity = sysUserService.searchDataById(userId);
    boolean flag = userEntity.getAreaCode() != null && userEntity.getAreaCode().substring(0, 9).equals("140725701");
    result.setResult(flag);
    return result.toJson();
  }


  //问题上报可以转办的人员角色
  @GetMapping(value = "/reportZBRoleList.json", produces = BaseConstant.JSON)
  public String reportZBRoleList(@RequestParam("type") Integer type) {
    ServiceResult<Object> result = new ServiceResult<>();
    try {
      RoleEnum[] roleEnums = RoleEnum.values();
      List<KeyValueDto> roleList = new ArrayList<>();

      if(type==0){//当前登录系统的用户是1003，乡镇纪委管理员，乡镇纪委管理员管理员进行转办的话，只能选择到村干部和站所
        for (RoleEnum roleEnum : roleEnums) {
          if(roleEnum.getRoleId()==1012 || (roleEnum.getRoleId()>=3000 && roleEnum.getRoleId()<=3999)
          ){//乡镇纪委管理员，村干部，以及所有的站所
            roleList.add(new KeyValueDto(roleEnum.getRoleId().toString(), roleEnum.getName()));
          }
        }
      }else if(type==1){//当前登录系统的用户是1012或者1004县纪委联系室，村干部，村干部进行转办的话，只能选择到乡镇纪委管理员和站所
        for (RoleEnum roleEnum : roleEnums) {
          if(roleEnum.getRoleId()==1003 ||  (roleEnum.getRoleId()>=3000 && roleEnum.getRoleId()<=3999)
          ){//乡镇纪委管理员，村干部，以及所有的站所
            roleList.add(new KeyValueDto(roleEnum.getRoleId().toString(), roleEnum.getName()));
          }
        }
      }

      // 全部的角色数据
      result.setResult(roleList);
      result.setSuccess(true);
    } catch (Exception e) {
      LOGGER.error("[查询问题上报转办角色出错],{}", e.getMessage(), e);
      result.setError("查询问题上报转办角色出错");
      result.setSuccess(true);
    }
    return result.toJson();
  }


}
