package com.hengtianyi.dims.web;

import com.hengtianyi.common.core.base.CommonPageDto;
import com.hengtianyi.common.core.base.CommonStringDto;
import com.hengtianyi.common.core.base.service.AbstractBaseController;
import com.hengtianyi.common.core.constant.BaseConstant;
import com.hengtianyi.common.core.feature.ServiceResult;
import com.hengtianyi.common.core.util.JsonUtil;
import com.hengtianyi.common.core.util.StringUtil;
import com.hengtianyi.common.core.util.sequence.IdGenUtil;
import com.hengtianyi.common.core.util.sequence.SystemClock;
import com.hengtianyi.dims.aop.WebLog;
import com.hengtianyi.dims.constant.FrameConstant;
import com.hengtianyi.dims.constant.LogEnum;
import com.hengtianyi.dims.exception.ErrorEnum;
import com.hengtianyi.dims.exception.WebException;
import com.hengtianyi.dims.service.api.ClueFlowService;
import com.hengtianyi.dims.service.api.ClueReportService;
import com.hengtianyi.dims.service.api.ReportTypeService;
import com.hengtianyi.dims.service.api.SysUserService;
import com.hengtianyi.dims.service.api.TownshipService;
import com.hengtianyi.dims.service.dto.QueryDto;
import com.hengtianyi.dims.service.entity.*;
import com.hengtianyi.dims.utils.WebUtil;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * ClueReport - Controller
 *
 * @author LY
 */
@Controller
@RequestMapping(value = ClueReportController.MAPPING)
public class ClueReportController extends AbstractBaseController<ClueReportEntity, String> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ClueReportController.class);
  public static final String MAPPING = "a/clueReport";

  @Resource
  private ClueReportService clueReportService;
  @Resource
  private SysUserService sysUserService;
  @Resource
  private ClueFlowService clueFlowService;
  @Resource
  private TownshipService townshipService;
  @Resource
  private ReportTypeService reportTypeService;

  @Override
  public ClueReportService getService() {
    return clueReportService;
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

    List<TownshipEntity> list = townshipService.areaList();

    model.addAttribute("areaList", list);

    //将当前用户的角色赋值过去
    SysUserEntity userEntity = WebUtil.getUser(request);
    Integer roleId = userEntity.getRoleId();
    model.addAttribute("roleId", roleId);
    return "web/clueReport/clueReport_index";
  }

  /**
   * 模块编辑页[视图]
   *
   * @param model Model
   * @param id    主键ID
   * @return 视图
   */
  @GetMapping(value = "/edit.html", produces = BaseConstant.HTML)
  public String edit(Model model, @RequestParam(value = "id", required = false) String id) {
    List<TownshipEntity> list = townshipService.areaList();
    ClueReportEntity entity = null;
    if (StringUtil.isNoneBlank(id)) {
      entity = this.getDataByIdCommon(id);
      if (entity == null) {
        throw new WebException(ErrorEnum.NO_DATA);
      }
    }
    model.addAttribute("mapping", MAPPING);
    SysUserEntity userEntity = sysUserService.searchDataById(entity.getUserId());
    if (userEntity != null) {
      entity.setUserName(userEntity.getUserName());
      TownshipEntity listOne=null;
      for(TownshipEntity l : list){
        TownshipEntity a = l;
        if(a.getAreaCode().equals(userEntity.getAreaCode().substring(0,9))) {
          listOne=a;
          break;
        }
      }

      entity.setReportUserAreaName(null==listOne ? "" : listOne.getAreaName());

    }
    entity.setFlows(clueFlowService.getAllFlows(entity.getId()));
    entity.setDtoList(reportTypeService.contents(entity.getReportRoleId(), entity.getReportIds()));
    model.addAttribute("entity", entity);
    return "web/clueReport/clueReport_edit";
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
      @ModelAttribute ClueReportEntity dto, HttpServletRequest request) {
    QueryDto queryDto = new QueryDto();
    SysUserEntity userEntity = WebUtil.getUser(request);
    Integer roleId = userEntity.getRoleId();
    queryDto.setRoleId(roleId);
    if (roleId < 1005) {
      queryDto.setUserId(userEntity.getId());
    }
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
      queryDto.setReportRoleId(dto.getReportRoleId());
    }
    queryDto.setCurrentPage(pageDto.getCurrent());
    queryDto.setFirst((pageDto.getCurrent() - 1) * FrameConstant.PAGE_SIZE);
    queryDto.setEnd(pageDto.getCurrent() * FrameConstant.PAGE_SIZE);
    return clueReportService.listData(queryDto).toJsonHtml();
  }

  /**
   * 通过AJAX获取单条信息[JSON]
   *
   * @param id 主键ID
   * @return JSON
   */
  @ResponseBody
  @PostMapping(value = "/getDataInfo.json", produces = BaseConstant.JSON)
  public String getDataInfo(@RequestParam String id, Model model) {
    List<TownshipEntity> list = townshipService.areaList();
    ServiceResult<ClueReportEntity> result = new ServiceResult<>();
    ClueReportEntity one = this.getDataByIdCommon(id);
    if (null != one) {
      this.clearEntity(one);
      SysUserEntity userEntity = sysUserService.searchDataById(one.getUserId());
      if (userEntity != null) {
        one.setUserName(userEntity.getUserName());
        TownshipEntity listOne=null;
        for(TownshipEntity l : list){
          TownshipEntity a = l;
          if(a.getAreaCode().equals(userEntity.getAreaCode().substring(0,9))) {
            listOne=a;
            break;
          }
        }

        one.setReportUserAreaName(null==listOne ? "" : listOne.getAreaName());

      }

      one.setFlows(clueFlowService.getAllFlows(one.getId()));
      one.setDtoList(reportTypeService.contents(one.getReportRoleId(), one.getReportIds()));
      result.setSuccess(true);
      result.setResult(one);
    } else {
      result.setError(BaseConstant.ERROR_READ);
    }


    model.addAttribute("areaList", list);
    return JsonUtil.toJson(result);
  }

  /**
   * 通过AJAX删除[JSON]
   *
   * @param idsDto 主键集合DTO
   * @return JSON
   */
  @WebLog(value = "删除线索上报内容", type = LogEnum.DELETE)
  @ResponseBody
  @PostMapping(value = "/deleteData.json", produces = BaseConstant.JSON)
  public String deleteData(@ModelAttribute CommonStringDto idsDto) {
    return deleteDataCommon(idsDto);
  }

  /**
   * 通过AJAX保存[JSON]
   *
   * @param entity 对象实体
   * @param n      这个可以是任意非空字符，用来确定是否添加
   * @return JSON
   */
  @ResponseBody
  @PostMapping(value = "/saveData.json", produces = BaseConstant.JSON)
  public String saveData(@ModelAttribute ClueReportEntity entity,
      @RequestParam(value = BaseConstant.PAGE_ADD_SIGN, required = false) String n) {
    boolean isNew = StringUtil.isBlank(n);
    if (isNew) {
      // 如果没有在表单指定主键，这里需要生成一个
      String id = entity.getId();
      if (StringUtil.isBlank(id)) {
        entity.setId(IdGenUtil.uuid32());
      }
      return this.insertDataCommon(entity);
    } else {
      return this.updateDataCommon(entity);
    }
  }

  @Override
  public void clearEntity(ClueReportEntity entity) {
    if (null != entity) {
      entity.clean();

    }
  }



  @GetMapping(value = "/knowTask.html", produces = BaseConstant.HTML)
  public String knowTask(Model model, @RequestParam String uid) {
    if (StringUtil.isBlank(uid)) {
      throw new WebException(ErrorEnum.AUTHORIZE_PARAMETER);
    }
    model.addAttribute("uid", uid);
    model.addAttribute("mapping", MAPPING);
    return "web/clueReport/knowTask_tree";
  }


  //已知晓（选择已知晓后，不需要后续操作）
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
      ClueFlowEntity flowEntity = new ClueFlowEntity();
      flowEntity.setId(IdGenUtil.uuid32());
      flowEntity.setClueId(uid);
      flowEntity.setState(3);//3已知晓
      flowEntity.setCreateTime(SystemClock.nowDate());
      flowEntity.setReceiveId(u.getId());
      ct=clueFlowService.insertData(flowEntity);
    }
    if(flag){
      ClueReportEntity clueReportEntity = clueReportService.searchDataById(uid) ;
      clueReportEntity.setState(Short.valueOf("3"));//0未处理，1已受理，2已办结，3已知晓，4已转办
      ct = clueReportService.updateData(clueReportEntity);
    }
    ServiceResult<Boolean> result = new ServiceResult<>();
    //这样写是否有问题
    result.setSuccess(ct > -1);
    result.setResult(ct > -1);
    return result.toJson();
  }

  @GetMapping(value = "/turnToOtherTask.html", produces = BaseConstant.HTML)
  public String turnToOtherTask(Model model, @RequestParam String uid) {
    if (StringUtil.isBlank(uid)) {
      throw new WebException(ErrorEnum.AUTHORIZE_PARAMETER);
    }
    model.addAttribute("uid", uid);
    model.addAttribute("mapping", MAPPING);
    return "web/clueReport/turnToOtherTask_tree";
  }


  //已转办（选择已转办后，不需要后续操作）
  @ResponseBody
  @PostMapping(value = "/turnToOtherTask.json", produces = BaseConstant.JSON)
  public String turnToOtherTask(@RequestParam String uid,HttpServletRequest request) {
    if (StringUtil.isBlank(uid)) {
      throw new WebException(ErrorEnum.AUTHORIZE_PARAMETER);
    }
    SysUserEntity u = WebUtil.getUser();;
    int ct = -1;
    boolean flag=true;
    //单个成员点击，时候会触发这个事件，新加入一条
    if (StringUtil.isNotBlank(uid)) {
      ClueFlowEntity flowEntity = new ClueFlowEntity();
      flowEntity.setId(IdGenUtil.uuid32());
      flowEntity.setClueId(uid);
      flowEntity.setState(4);//4已转办
      flowEntity.setCreateTime(SystemClock.nowDate());
      flowEntity.setReceiveId(u.getId());
      ct=clueFlowService.insertData(flowEntity);
    }
    if(flag){
      ClueReportEntity clueReportEntity = clueReportService.searchDataById(uid) ;
      clueReportEntity.setState(Short.valueOf("4"));//0未处理，1已受理，2已办结，3已知晓，4已转办
      ct = clueReportService.updateData(clueReportEntity);
    }
    ServiceResult<Boolean> result = new ServiceResult<>();
    //这样写是否有问题
    result.setSuccess(ct > -1);
    result.setResult(ct > -1);
    return result.toJson();
  }


  @GetMapping(value = "/turnToUntreatedTask.html", produces = BaseConstant.HTML)
  public String turnToUntreatedTask(Model model, @RequestParam String uid) {
    if (StringUtil.isBlank(uid)) {
      throw new WebException(ErrorEnum.AUTHORIZE_PARAMETER);
    }
    model.addAttribute("uid", uid);
    model.addAttribute("mapping", MAPPING);
    return "web/clueReport/turnToUntreatedTask_tree";
  }


  //已转办（选择已转办后，不需要后续操作）
  @ResponseBody
  @PostMapping(value = "/turnToUntreatedTask.json", produces = BaseConstant.JSON)
  public String turnToUntreatedTask(@RequestParam String uid,HttpServletRequest request) {
    if (StringUtil.isBlank(uid)) {
      throw new WebException(ErrorEnum.AUTHORIZE_PARAMETER);
    }
    SysUserEntity u = WebUtil.getUser();;
    int ct = -1;
    boolean flag=true;
    //单个成员点击，时候会触发这个事件，新加入一条
    if (StringUtil.isNotBlank(uid)) {
      ClueFlowEntity flowEntity = new ClueFlowEntity();
      flowEntity.setId(IdGenUtil.uuid32());
      flowEntity.setClueId(uid);
      flowEntity.setState(0);//0未处理
      flowEntity.setCreateTime(SystemClock.nowDate());
      String receiveId = clueFlowService.getReceiveId(uid,0);
      flowEntity.setReceiveId(receiveId);//需要找到原始的状态为0的那个受理人
      flowEntity.setRemark("状态重置为未处理！");
      ct=clueFlowService.insertData(flowEntity);
    }
    if(flag){
      ClueReportEntity clueReportEntity = clueReportService.searchDataById(uid) ;
      clueReportEntity.setState(Short.valueOf("0"));//0未处理，1已受理，2已办结，3已知晓，4已转办
      ct = clueReportService.updateData(clueReportEntity);
    }
    ServiceResult<Boolean> result = new ServiceResult<>();
    //这样写是否有问题
    result.setSuccess(ct > -1);
    result.setResult(ct > -1);
    return result.toJson();
  }

}
