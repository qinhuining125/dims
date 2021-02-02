package com.hengtianyi.dims.api;

import com.hengtianyi.common.core.base.CommonEntityDto;
import com.hengtianyi.common.core.constant.BaseConstant;
import com.hengtianyi.common.core.feature.ServiceResult;
import com.hengtianyi.common.core.util.CollectionUtil;
import com.hengtianyi.common.core.util.StringUtil;
import com.hengtianyi.common.core.util.sequence.IdGenUtil;
import com.hengtianyi.common.core.util.sequence.SystemClock;
import com.hengtianyi.dims.constant.FrameConstant;
import com.hengtianyi.dims.constant.RoleEnum;
import com.hengtianyi.dims.service.api.ImageRevealService;
import com.hengtianyi.dims.service.api.RevealFlowService;
import com.hengtianyi.dims.service.api.RevealInfoService;
import com.hengtianyi.dims.service.api.SysUserService;
import com.hengtianyi.dims.service.dto.KeyValueDto;
import com.hengtianyi.dims.service.dto.QueryDto;
import com.hengtianyi.dims.service.dto.RevealInfoDto;
import com.hengtianyi.dims.service.entity.ImageRevealEntity;
import com.hengtianyi.dims.service.entity.RevealFlowEntity;
import com.hengtianyi.dims.service.entity.RevealInfoEntity;
import com.hengtianyi.dims.service.entity.SysUserEntity;
import com.hengtianyi.dims.utils.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 百姓举报
 *
 * @author
 */
@RestController
@RequestMapping(value = "/api/revealInfo")
public class RevealInfoApiController {

  private static final Logger LOGGER = LoggerFactory.getLogger(RevealInfoApiController.class);


  @Resource
  private SysUserService sysUserService;
  @Resource
  private RevealInfoService revealInfoService;
  @Resource
  private RevealFlowService revealFlowService;
  @Resource
  private ImageRevealService imageRevealService;

  /**
   * 提交百姓举报页面
   * @param dto
   * @return json
   */
  @PostMapping(value = "/saveData.json", produces = BaseConstant.JSON)
  public String saveData(@RequestBody RevealInfoDto dto, HttpServletRequest request) {
    ServiceResult<Object> result = new ServiceResult<>();
    try {
      String userId = "";
      Integer roleId = 0;
      if(WebUtil.getUserIdByToken(request)==null || WebUtil.getUserIdByToken(request).trim().equals("")){
        userId = "MASSES_USERID" + SystemClock.now();//对于群众需要随机设置一个id
        roleId = 2000;//群众roleId
      }else{
        userId = WebUtil.getUserIdByToken(request);//对于网格员，联络员可以从token中获取userId
        SysUserEntity userEntity = sysUserService.searchDataById(userId);
        String areaCode=userEntity.getAreaCode();
        String township=areaCode.substring(0,9);
        String area=areaCode.substring(0,12);
        dto.setTownCode(township);
        dto.setVillageCode(area);
        roleId = userEntity.getRoleId();
      }
      int ct = 0;
      int cf = 0;

      if (roleId > 1002 && roleId!=2000) {//只有网格员和联络员可以举报
        result.setSuccess(false);
        result.setMessage("无举报权限");
      } else {
        String revealId = IdGenUtil.uuid32();
        RevealInfoEntity entity = new RevealInfoEntity();
        entity.setId(revealId);
        entity.setUserId(userId);
        entity.setRoleId(roleId);//如果有值就设置为用户roleId，如果没有就设置为群众的roleId
        entity.setContact(dto.getContact());
        entity.setContent(dto.getContent());
        entity.setUserName(dto.getUserName());
        entity.setRevealTarget(dto.getRevealTarget());
        entity.setType(dto.getType());
        entity.setCreateTime(SystemClock.nowDate());
        entity.setState(Short.valueOf("0"));//0未处理，1已分配，2已知晓
        entity.setInspectionId(dto.getInspectionId());//后期需替换成App或者H5扫描后的传过来的id
        entity.setTownCode(dto.getTownCode());
        entity.setVillageCode(dto.getVillageCode());
        ct = revealInfoService.insertData(entity);

        //上传图片 这里需要对多图片进行处理
        List<String> ss=dto.getImageArray();
        if(ss.size()>0){
          for(int i=0;i < ss.size() ;i++){
            ImageRevealEntity imageRevealEntity = new ImageRevealEntity();
            String imageId = IdGenUtil.uuid32();
            imageRevealEntity.setId(imageId);
            imageRevealEntity.setRevealId(revealId);
            imageRevealEntity.setImageUrl(ss.get(i));
            imageRevealEntity.setCreateTime(SystemClock.nowDate());
            imageRevealService.insertData(imageRevealEntity);
          }
        }
        //流程
        RevealFlowEntity flowEntity = new RevealFlowEntity();
        flowEntity.setId(IdGenUtil.uuid32());
        flowEntity.setRevealId(revealId);
        List<SysUserEntity> user = sysUserService.getUserIdByRoleId(RoleEnum.XCBADMIN.getRoleId());//巡察办主任
        flowEntity.setReceiveId(user.get(0).getId());//巡察办主任用户Id
        flowEntity.setReceiveRoleId(user.get(0).getRoleId());//巡察办主任角色
        flowEntity.setState(0);//0未接受，1已受理，2已知晓
        flowEntity.setCreateTime(SystemClock.nowDate());
        cf = revealFlowService.insertData(flowEntity);
        result.setSuccess(ct > 0 && cf > 0);
        result.setResult(ct > 0 && cf > 0);
        result.setMessage("举报成功");
      }
    } catch (Exception e) {
      LOGGER.error("[saveData]举报出错,{}", e.getMessage(), e);
      result.setSuccess(false);
      result.setMessage("举报出错");
    }
    return result.toJson();
  }

  /**
   * 指派
   * @param RevealInfoEntity
   * @return
   */
  @PostMapping(value = "/savetree.json", produces = BaseConstant.JSON)
  public String assign(@RequestBody RevealInfoEntity entity) {
    String uid=entity.getId();
    Integer roleId=entity.getRoleId();
    ServiceResult<Object> result = new ServiceResult<>();
    try {
      int ct = -1;
      if (StringUtil.isNotBlank(uid) && roleId > 0) {
        List<SysUserEntity>  users = sysUserService.getUserIdByRoleId(roleId);
        if(!users.isEmpty()){
          for(SysUserEntity user : users){
            RevealFlowEntity flowEntity=new RevealFlowEntity();
            flowEntity.setId(IdGenUtil.uuid32());
            flowEntity.setRevealId(uid);
            flowEntity.setReceiveRoleId(roleId);
            flowEntity.setState(1);//0未分配，1已分配，2已知晓
            flowEntity.setCreateTime(SystemClock.nowDate());
            flowEntity.setReceiveId(user.getId());
            revealFlowService.insertData(flowEntity);
          }
        }
        RevealInfoEntity revealInfoEntity = revealInfoService.searchDataById(uid) ;
        revealInfoEntity.setState(Short.valueOf("1"));//0未分配，1已分配，2已知晓
        ct = revealInfoService.updateData(revealInfoEntity);
      }
      result.setSuccess(ct > -1);
      result.setResult(ct > -1);
    } catch (Exception e) {
      LOGGER.error("[assign]指派出错,{}", e.getMessage(), e);
      result.setMessage("任务指派出错");
      result.setSuccess(false);
    }
    return result.toJson();
  }


  /**
   * 点击已知晓
   * @param uid 需要拿到该条举报的id
   * @param request
   * @return
   */
  @GetMapping(value = "/knowTask.json", produces = BaseConstant.JSON)
  public String knowTask(@RequestParam String uid, HttpServletRequest request) {

    ServiceResult<Object> result = new ServiceResult<>();
    try {
      RevealInfoEntity revealInfoEntity = revealInfoService.searchDataById(uid);
      String userId = WebUtil.getUserIdByToken(request);//当前登录系统的用户
      SysUserEntity userEntity = sysUserService.searchDataById(userId);
      int ct =0 ;
      boolean flag=true;
      //点击了已知晓，就会出现这样的逻辑在flow表中
      if (StringUtil.isNotBlank(uid)) {
        RevealFlowEntity flowEntity = new RevealFlowEntity();
        flowEntity.setId(IdGenUtil.uuid32());
        flowEntity.setRevealId(uid);
        flowEntity.setReceiveRoleId(userEntity.getRoleId());
        flowEntity.setState(2);//2已知晓
        flowEntity.setCreateTime(SystemClock.nowDate());
        flowEntity.setReceiveId(userEntity.getId());
        ct=revealFlowService.insertData(flowEntity);
      }
      Integer roleId = userEntity.getRoleId();
      List<SysUserEntity>  users = sysUserService.getUserIdByRoleId(roleId);
      if(!users.isEmpty()){
        for(SysUserEntity user : users){
          RevealFlowEntity flowEntity = new RevealFlowEntity();
          flowEntity.setRevealId(uid);//流程ID
          flowEntity.setReceiveRoleId(roleId);//角色是和他一样的
          flowEntity.setState(2);//状态也要一样的
          flowEntity.setReceiveId(user.getId());//分配给到
          List<RevealFlowEntity> list=revealFlowService.getFlowsByConditons(flowEntity);
          if(CollectionUtil.isEmpty(list)){//表示还有其他同一组的成员没有点击已知晓，所以总的目录不能更改
            flag=false;
            break;
          }
        }
      }
      if(flag){
        revealInfoEntity.setState(Short.valueOf("2"));//状态改为 已知晓
        ct = revealInfoService.updateData(revealInfoEntity);
      }
      result.setSuccess(ct > 0);
      result.setResult(ct > 0);
    } catch (Exception e) {
      LOGGER.error("[accept]已知晓出错");
      result.setError("百姓举报任务accept出错");
      result.setSuccess(false);
    }
    return result.toJson();
  }

  /**
   * 详情 ok
   * 根据表单id 获取 表单详情 + 表单对应的所有流程
   * 包含 界面填报的内容 + 流程
   * @param revealId
   * @return
   */
  @GetMapping(value = "/detail.json", produces = BaseConstant.JSON)
  public String detail(@RequestParam String revealId) {
    ServiceResult<Object> result = new ServiceResult<>();
    try {
      RevealInfoEntity entity = revealInfoService.searchDataById(revealId);//获取表单的内容
      entity.setFlows(revealFlowService.getAllFlows(revealId));//获取所有流程
      entity.setImgApps(imageRevealService.getAllImages(revealId));
      result.setResult(entity);
      result.setSuccess(true);
    } catch (Exception e) {
      LOGGER.error("[detail]出错,{}", e.getMessage(), e);
      result.setSuccess(false);
      result.setMessage("详情页出错");
    }
    return result.toJson();
  }


  /**
   * 分页列表 ok
   *
   * @param dto
   * @return
   */
  @PostMapping(value = "/pagelist.json", produces = BaseConstant.JSON)
  public String list(@RequestBody CommonEntityDto<RevealInfoEntity> dto, HttpServletRequest request) {
    ServiceResult<Object> result = new ServiceResult<>();
    try {
      String userId = WebUtil.getUserIdByToken(request);
      SysUserEntity userEntity = sysUserService.searchDataById(userId);
      Integer roleId = userEntity.getRoleId();
      QueryDto queryDto = new QueryDto();
      queryDto.setFirst((dto.getCurrentPage() - 1) * FrameConstant.PAGE_SIZE);
      queryDto.setEnd(dto.getCurrentPage() * FrameConstant.PAGE_SIZE);
      queryDto.setRoleId(roleId);
      queryDto.setUserId(userId);
      if (dto.getQuery() != null) {
        if (dto.getQuery().getState() != null) {
          queryDto.setState(dto.getQuery().getState().intValue());
        }
        queryDto.setStartTime(dto.getQuery().getStartTime());
        queryDto.setEndTime(dto.getQuery().getEndTime());
        queryDto.setAreaCode(dto.getQuery().getAreaCode());
      }
      CommonEntityDto<RevealInfoEntity> cpDto = revealInfoService.pagelist(queryDto);//查询出了所有状态为1已分配的单子
      result.setResult(cpDto);
      result.setSuccess(true);
    } catch (Exception e) {
      LOGGER.error("[list]出错,{}", e.getMessage(), e);
      result.setError("待处理列表出错");
    }
    return result.toJson();
  }

  /**
   * 列表查询
   * 根据状态获取百姓举报列表（分角色，分状态）
   * e.g.
   * 1.巡察办主任：需要看到3个列表
   * 1.1 全部列表（包含所有状态，0未处理，1已分配，2已知晓）
   * 1.2 未处理列表 （包含状态：0未处理）（这个是包含所有人的）
   * 1.3 已分配列表 （包含状态：1已分配）（这个是包含所有人的）
   * 1.4 已知晓列表 （包含状态：2已知晓）（这个是包含所有人的）
   * 2.巡察一组/二组/三组
   * 2.1 全部列表（包含所有状态，1已分配（分配到自己组的），2已知晓）
   * 2.2 已分配列表 （包含状态：1已分配）（自己账号权限下的）
   * 2.2 已知晓列表 （包含状态：2已知晓）（自己账号权限下的）
   * @param  request
   * @return
   */
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
      queryDto.setRoleId(roleId);//设置当前登录到系统中的用户角色
      queryDto.setState(0);
      map.put("pending", revealInfoService.countReveal(queryDto) > 0);//未处理的单子
      queryDto.setState(1);
      map.put("assign", revealInfoService.countReveal(queryDto) > 0);//已分配的单子
      queryDto.setState(2);
      map.put("accept", revealInfoService.countReveal(queryDto) > 0);//已知晓的单子
      result.setResult(map);
      result.setSuccess(true);
    } catch (Exception e) {
      LOGGER.error("[detail]出错,{}", e.getMessage(), e);
      result.setError("详情页出错");
    }
    return result.toJson();
  }

  //巡察组账号列表
  @GetMapping(value = "/xclist.json", produces = BaseConstant.JSON)
  public String xclist(HttpServletRequest request) {
    ServiceResult<Object> result = new ServiceResult<>();
    try {
      RoleEnum[] roleEnums = RoleEnum.values();
      List<KeyValueDto> roleList = new ArrayList<>();
      for (RoleEnum roleEnum : roleEnums) {
        if(roleEnum.getRoleId()> 1008 && roleEnum.getRoleId()< 2000){//巡察一组，巡察二组，巡察三组
          roleList.add(new KeyValueDto(roleEnum.getRoleId().toString(), roleEnum.getName()));
        }
      }
      // 全部的角色数据
      result.setResult(roleList);
      result.setSuccess(true);
    } catch (Exception e) {
      LOGGER.error("[查询巡察账号出错],{}", e.getMessage(), e);
      result.setError("查询巡察账号出错");
      result.setSuccess(true);
    }
    return result.toJson();
  }

}


