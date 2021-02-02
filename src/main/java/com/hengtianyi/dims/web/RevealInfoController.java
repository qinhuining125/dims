package com.hengtianyi.dims.web;

import com.hengtianyi.common.core.base.CommonPageDto;
import com.hengtianyi.common.core.base.CommonStringDto;
import com.hengtianyi.common.core.base.service.AbstractBaseController;
import com.hengtianyi.common.core.constant.BaseConstant;
import com.hengtianyi.common.core.feature.ServiceResult;
import com.hengtianyi.common.core.util.CollectionUtil;
import com.hengtianyi.common.core.util.JsonUtil;
import com.hengtianyi.common.core.util.StringUtil;
import com.hengtianyi.common.core.util.sequence.IdGenUtil;
import com.hengtianyi.common.core.util.sequence.SystemClock;
import com.hengtianyi.dims.constant.FrameConstant;
import com.hengtianyi.dims.constant.RevealTypeEnum;
import com.hengtianyi.dims.constant.RoleEnum;
import com.hengtianyi.dims.exception.ErrorEnum;
import com.hengtianyi.dims.exception.WebException;
import com.hengtianyi.dims.service.api.*;
import com.hengtianyi.dims.service.dto.KeyValueDto;
import com.hengtianyi.dims.service.dto.QueryDto;
import com.hengtianyi.dims.service.dto.RevealInfoDto;
import com.hengtianyi.dims.service.entity.RevealFlowEntity;
import com.hengtianyi.dims.service.entity.RevealInfoEntity;
import com.hengtianyi.dims.service.entity.SysUserEntity;
import com.hengtianyi.dims.utils.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * RevealInfo - Controller
 *
 * @author
 */
@Controller
@RequestMapping(value = RevealInfoController.MAPPING)
public class RevealInfoController extends AbstractBaseController<RevealInfoEntity, String> {

  private static final Logger LOGGER = LoggerFactory.getLogger(RevealInfoController.class);
  public static final String MAPPING = "a/revealInfo";

  @Resource
  private RevealInfoService revealInfoService;
  @Resource
  private SysUserService sysUserService;
  @Resource
  private RevealFlowService revealFlowService;
  @Resource
  private TownshipService townshipService;
  @Resource
  private RelUserAreaService relUserAreaService;

  @Resource
  private ImageRevealService imageRevealService;

  @Override
  public RevealInfoService getService() {
    return revealInfoService;
  }

  @Override
  public Logger getLogger() {
    return LOGGER;
  }

  /**
   * 模块首页[视图]
   *
   * @param model Model
   * @return 视图
   */
  @GetMapping(value = "/index.html", produces = BaseConstant.HTML)
  public String index(Model model,HttpServletRequest request) {
    model.addAttribute("mapping", MAPPING);
    model.addAttribute("areaList", townshipService.areaList());
    //将当前用户的角色赋值过去
    SysUserEntity userEntity = WebUtil.getUser(request);
    Integer roleId = userEntity.getRoleId();
    model.addAttribute("roleIds", roleId);
    return "web/revealInfo/revealInfo_index";
  }


  @GetMapping(value = "/tree.html", produces = BaseConstant.HTML)
  public String tree(Model model, @RequestParam String uid) {
    if (StringUtil.isBlank(uid)) {
      throw new WebException(ErrorEnum.AUTHORIZE_PARAMETER);
    }
    RoleEnum[] roleEnums = RoleEnum.values();
    List<KeyValueDto> roleList = new ArrayList<>();
    for (RoleEnum roleEnum : roleEnums) {
      if(roleEnum.getRoleId()> 1008 && roleEnum.getRoleId()< 2000){//巡察一组，巡察二组，巡察三组
        roleList.add(new KeyValueDto(roleEnum.getRoleId().toString(), roleEnum.getName()));
      }
    }
    // 全部的角色数据
    model.addAttribute("roleList", roleList);
    // 指定用户拥有的角色
    model.addAttribute("uid", uid);
    model.addAttribute("mapping", MAPPING);
    return "web/revealInfo/assignRevealTask_tree";
  }

  @ResponseBody
  @PostMapping(value = "/savetree.json", produces = BaseConstant.JSON)
  public String saveTree(@RequestParam String uid, @RequestParam Integer roleId) {
    if (StringUtil.isBlank(uid)) {
      throw new WebException(ErrorEnum.AUTHORIZE_PARAMETER);
    }
    int ct = -1;
    // 给指定角色分配举报任务
    if (StringUtil.isNotBlank(uid) && roleId > 0) {

      List<SysUserEntity>  users = sysUserService.getUserIdByRoleId(roleId);
      if(!users.isEmpty()){
        for(SysUserEntity user : users){
          RevealFlowEntity flowEntity = new RevealFlowEntity();
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
    ServiceResult<Boolean> result = new ServiceResult<>();
    result.setSuccess(ct > -1);
    result.setResult(ct > -1);
    return result.toJson();
  }



  @GetMapping(value = "/knowTask.html", produces = BaseConstant.HTML)
  public String knowTask(Model model, @RequestParam String uid) {
    if (StringUtil.isBlank(uid)) {
      throw new WebException(ErrorEnum.AUTHORIZE_PARAMETER);
    }
    model.addAttribute("uid", uid);
    model.addAttribute("mapping", MAPPING);
    return "web/revealInfo/knowTask_tree";
  }


  //已知晓（相当于受理 接受任务）
  @ResponseBody
  @PostMapping(value = "/knowTask.json", produces = BaseConstant.JSON)
  public String knowTask(@RequestParam String uid,HttpServletRequest request) {
    if (StringUtil.isBlank(uid)) {
      throw new WebException(ErrorEnum.AUTHORIZE_PARAMETER);
    }
    SysUserEntity u = WebUtil.getUser();;
    int ct = -1;
    boolean flag=true;
    //单个成员点击，时候会触发这个事件，新加入一条
    if (StringUtil.isNotBlank(uid)) {
      RevealFlowEntity flowEntity = new RevealFlowEntity();
      flowEntity.setId(IdGenUtil.uuid32());
      flowEntity.setRevealId(uid);
      flowEntity.setReceiveRoleId(u.getRoleId());
      flowEntity.setState(2);//2已知晓
      flowEntity.setCreateTime(SystemClock.nowDate());
      flowEntity.setReceiveId(u.getId());
      ct=revealFlowService.insertData(flowEntity);
    }
    //对于同组里面的其他成员，这里要怎么显示
    SysUserEntity userEntity = WebUtil.getUser(request);
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
      RevealInfoEntity revealInfoEntity = revealInfoService.searchDataById(uid) ;
      revealInfoEntity.setState(Short.valueOf("2"));//0未处理，1已分配，2已知晓
      ct = revealInfoService.updateData(revealInfoEntity);
    }
    ServiceResult<Boolean> result = new ServiceResult<>();
    //这样写是否有问题
    result.setSuccess(ct > -1);
    result.setResult(ct > -1);
    return result.toJson();
  }


  /**
   * 模块编辑页[视图]
   *
   * @param model Model
   * @param id    主键ID
   * @return 视图
   */
  @GetMapping(value = "/edit.html", produces = BaseConstant.HTML)
  public String edit(Model model, @RequestParam(value = "id", required = false) String id,
                     HttpServletRequest request) {
    RevealInfoEntity entity = null;
    if (StringUtil.isNoneBlank(id)) {
      entity = this.getDataByIdCommon(id);
      if (entity == null) {
        throw new WebException(ErrorEnum.NO_DATA);
      }
    }
    model.addAttribute("mapping", MAPPING);
    model.addAttribute("entity", entity);
    String userId = WebUtil.getUserId(request);
    model.addAttribute("areaList", relUserAreaService.getUserArealist(userId));
    RevealTypeEnum[] revealTypeEnums = RevealTypeEnum.values();
    List<KeyValueDto> dtoList = new ArrayList<>();
    for (RevealTypeEnum revealTypeEnum : revealTypeEnums) {
      dtoList.add(new KeyValueDto(revealTypeEnum.getType().toString(), revealTypeEnum.getValue()));
    }
    model.addAttribute("revealTypeList", dtoList);

    return "web/revealInfo/revealInfo_edit";
  }

  /**
   * 通过AJAX获取列表信息[JSON]
   *
   * @param pageDto 通用DTO
   * @param dto     查询DTO
   * @return JSON
   */
  @ResponseBody
  @PostMapping(value = "/getDataList.json", produces = BaseConstant.JSON)
  public String getDataList(@ModelAttribute CommonPageDto pageDto,
                            @ModelAttribute RevealInfoEntity dto, HttpServletRequest request) {
    QueryDto queryDto = new QueryDto();
    SysUserEntity userEntity = WebUtil.getUser(request);
    Integer roleId = userEntity.getRoleId();
    queryDto.setRoleId(roleId);
    queryDto.setUserId(userEntity.getId());
    if (dto != null) {
      if (dto.getState() != null) {
        queryDto.setState(dto.getState().intValue());
      }
      if (StringUtil.isNotEmpty(dto.getStartTime())) {
        queryDto.setStartTime(dto.getStartTime().trim() + " 00:00:00");
      }
      if (StringUtil.isNotEmpty(dto.getEndTime())) {
        queryDto.setEndTime(dto.getEndTime().trim() + " 00:00:00");
      }
      queryDto.setAreaCode(dto.getAreaCode());
    }
    queryDto.setCurrentPage(pageDto.getCurrent());
    queryDto.setFirst((pageDto.getCurrent() - 1) * FrameConstant.PAGE_SIZE);
    queryDto.setEnd(pageDto.getCurrent() * FrameConstant.PAGE_SIZE);
    //这里进行区分
    return revealInfoService.getDataList(queryDto);

  }

  /**
   * 通过AJAX获取单条信息[JSON]
   *
   * @param id 主键ID
   * @return JSON
   */
  @ResponseBody
  @PostMapping(value = "/getDataInfo.json", produces = BaseConstant.JSON)
  public String getDataInfo(@RequestParam String id) {
    ServiceResult<RevealInfoEntity> result = new ServiceResult<>();
    RevealInfoEntity one = this.getDataByIdCommon(id);
    if (null != one) {
      this.clearEntity(one);
      SysUserEntity userEntity = sysUserService.searchDataById(one.getUserId());
      if (userEntity != null) {
        one.setUserName(userEntity.getUserName());
      }
      one.setFlows(revealFlowService.getAllFlows(one.getId()));
      one.setImgApps(imageRevealService.getAllImages(one.getId()));
      result.setSuccess(true);
      result.setResult(one);
    } else {
      result.setError(BaseConstant.ERROR_READ);
    }
    return JsonUtil.toJson(result);
  }

  /**
   * 通过AJAX删除[JSON]
   *
   * @param idsDto 主键集合DTO
   * @return JSON
   */
  @ResponseBody
  @PostMapping(value = "/deleteData.json", produces = BaseConstant.JSON)
  public String deleteData(@ModelAttribute CommonStringDto idsDto) {
    return deleteDataCommon(idsDto);
  }

  /**
   * 通过AJAX保存[JSON]
   *
   * @return JSON
   */
  @ResponseBody
  @PostMapping(value = "/saveData.json", produces = BaseConstant.JSON)
  public String saveData(@ModelAttribute RevealInfoDto dto, HttpServletRequest request) {
    ServiceResult<Integer> result = new ServiceResult<>();
    try {
      int ct = revealInfoService.saveData(dto, request);
      if (ct > BaseConstant.NUM_0) {
        result.setResult(ct);
        result.setSuccess(true);
      } else {
        result.setResult(BaseConstant.NUM_0);
        result.setError(BaseConstant.ERROR_SAVE);
      }
    } catch (Exception ex) {
      String exMsg = ex.getMessage();
      result.setResult(-1);
      result.setError(BaseConstant.ERROR_SAVE);
      LOGGER.error("[insertDataCommon]{}, entity = {}", exMsg, dto.toJson(), ex);
    }
    return JsonUtil.toJson(result);
  }

  @Override
  public void clearEntity(RevealInfoEntity entity) {
    if (null != entity) {
      entity.clean();

    }
  }
}
