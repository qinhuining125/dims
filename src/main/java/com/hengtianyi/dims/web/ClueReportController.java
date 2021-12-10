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
import com.hengtianyi.dims.constant.RoleEnum;
import com.hengtianyi.dims.exception.ErrorEnum;
import com.hengtianyi.dims.exception.WebException;
import com.hengtianyi.dims.service.api.ClueFlowService;
import com.hengtianyi.dims.service.api.ClueReportService;
import com.hengtianyi.dims.service.api.ReportTypeService;
import com.hengtianyi.dims.service.api.SysUserService;
import com.hengtianyi.dims.service.api.TownshipService;
import com.hengtianyi.dims.service.dao.ReportTypeDao;
import com.hengtianyi.dims.service.dao.SysUserDao;
import com.hengtianyi.dims.service.dto.KeyValueDto;
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

import java.util.ArrayList;
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

  @Resource
  private ReportTypeDao reportTypeDao;

  @Resource
  private SysUserDao sysUserDao;

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
    Integer roleId1 = 1001;//网格员
    Integer roleId2 = 1002;//联络员
    model.addAttribute("reportTypeList1", reportTypeService.getListAll(roleId1));
    model.addAttribute("reportTypeList2", reportTypeService.getListAll(roleId2));
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
      if (dto.getReportIds1()!=null&&dto.getReportIds1().length()>0){//最多只能选择一个
        dto.setReportIds(dto.getReportIds1());
        queryDto.setReportRoleId(1001);
        StringBuffer sb = new StringBuffer();
        sb.append("[").append(dto.getReportIds1()).append("-").append(reportTypeDao.contentByRoleSortNo(1001, Integer.parseInt(dto.getReportIds1()))).append("]");
        queryDto.setReportContents(sb.toString());
       }
      if (dto.getReportIds2()!=null&&dto.getReportIds2().length()>0){//最多只能选择一个
        dto.setReportIds(dto.getReportIds2());
        queryDto.setReportRoleId2(1002);
        StringBuffer sb = new StringBuffer();
        sb.append("[").append(dto.getReportIds2()).append("-").append(reportTypeDao.contentByRoleSortNo(1002, Integer.parseInt(dto.getReportIds2()))).append("]");
        queryDto.setReportContents2(sb.toString());
      }

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
      queryDto.setReceivedRoleId(null==dto.getReceivedRoleId() ? 0 : dto.getReceivedRoleId() );
      queryDto.setReceivedUserName(null==dto.getReceivedUserName() ? "" : dto.getReceivedUserName());

      List<String> wgyContentsList = new ArrayList<>();
      List<String> llyContentsList = new ArrayList<>();

       if(dto.getReportTypeState()!=null && !dto.getReportTypeState().equals("") && !dto.getReportTypeState().equals("全部")){
         List<ReportTypeEntity> listByStateWgy = reportTypeDao.getListByStateWgy(dto.getReportTypeState());
         List<ReportTypeEntity> listByStateLly= reportTypeDao.getListByStateLly(dto.getReportTypeState());
         if(!listByStateWgy.isEmpty()){
           for(ReportTypeEntity e : listByStateWgy){
             wgyContentsList.add(e.getContent());
           }
         }
         if(!listByStateLly.isEmpty()){
           for(ReportTypeEntity e : listByStateLly){
             llyContentsList.add(e.getContent());
           }
         }
       }
      queryDto.setWgyContentsList(wgyContentsList);
      queryDto.setLlyContentsList(llyContentsList);
      queryDto.setReportTypeState(dto.getReportTypeState());
//      queryDto.setReportIds(dto.getReportIds());

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
      one.setImg(clueReportService.getAttachmentsByIdType(id,ImageClueReportEntity.TYPE_IMAGE));//图片
      one.setAudio(clueReportService.getAttachmentsByIdType(id,ImageClueReportEntity.TYPE_AUDIO));//语音
      one.setVideo(clueReportService.getAttachmentsByIdType(id,ImageClueReportEntity.TYPE_VIDEO));//视频
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

  @GetMapping(value = "/editTask.html", produces = BaseConstant.HTML)
  public String editTask(Model model, @RequestParam String uid) {

   if (StringUtil.isBlank(uid)) {
      throw new WebException(ErrorEnum.AUTHORIZE_PARAMETER);
    }

    List<TownshipEntity> list = townshipService.areaList();
    ClueReportEntity entity = null;
    if (StringUtil.isNoneBlank(uid)) {
      entity = this.getDataByIdCommon(uid);
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

    model.addAttribute("uid", uid);
    model.addAttribute("mapping", MAPPING);
    return "web/clueReport/editTask_tree";
  }


  //对于状态是已办结的，可以允许再修改办结描述
  @ResponseBody
  @PostMapping(value = "/editTask.json", produces = BaseConstant.JSON)
  public String editTask(@RequestParam String uid,@RequestParam String remark) {
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
      flowEntity.setState(2);//2已办结
      flowEntity.setCreateTime(SystemClock.nowDate());
      flowEntity.setReceiveId(u.getId());
      flowEntity.setRemark(remark);
      ct=clueFlowService.insertData(flowEntity);
    }
    if(flag){
      ClueReportEntity clueReportEntity = clueReportService.searchDataById(uid) ;
      clueReportEntity.setState(Short.valueOf("2"));//0未处理，1已受理，2已办结，3已知晓，4已转办
      ct = clueReportService.updateData(clueReportEntity);
    }
    ServiceResult<Boolean> result = new ServiceResult<>();
    //这样写是否有问题
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
    SysUserEntity u = WebUtil.getUser();
    int userRoleId = u.getRoleId();
    RoleEnum[] roleEnums = RoleEnum.values();
    List<KeyValueDto> roleList = new ArrayList<>();
    if(userRoleId==1004){
      for (RoleEnum roleEnum : roleEnums) {
        if(roleEnum.getRoleId()==1003
                || (roleEnum.getRoleId()>=3000 && roleEnum.getRoleId()<=3999)
        ){//县信访室可以转办给 乡镇纪委管理员 或者站所
          roleList.add(new KeyValueDto(roleEnum.getRoleId().toString(), roleEnum.getName()));
        }
      }
    }else{
      for (RoleEnum roleEnum : roleEnums) {
        if(roleEnum.getRoleId()==1012
                || (roleEnum.getRoleId()>=3000 && roleEnum.getRoleId()<=3999)
        ){//村干部，所有站所
          roleList.add(new KeyValueDto(roleEnum.getRoleId().toString(), roleEnum.getName()));
        }
      }
    }

    // 全部的角色数据
    model.addAttribute("roleList", roleList);
    model.addAttribute("uid", uid);
    model.addAttribute("mapping", MAPPING);
    return "web/clueReport/turnToOtherTask_tree";
  }


  //已转办（选择已转办后，不需要后续操作）
  @ResponseBody
  @PostMapping(value = "/turnToOtherTask.json", produces = BaseConstant.JSON)
  public String turnToOtherTask(@RequestParam String uid,@RequestParam Integer roleId) {
    if (StringUtil.isBlank(uid)) {
      throw new WebException(ErrorEnum.AUTHORIZE_PARAMETER);
    }
    SysUserEntity u = WebUtil.getUser();
    int ct = -1;
    boolean flag=true;

    ClueReportEntity clueReportEntity = clueReportService.searchDataById(uid) ;
    //要分配给这个村对应的站所的账号，不能直接根据角色分配
    if (StringUtil.isNotBlank(uid) && roleId > 0) {
      String userId = clueReportEntity.getUserId();
      SysUserEntity uu = sysUserDao.getUserById(userId);
      SysUserEntity user = sysUserService.superiorUser(uu.getAreaCode().substring(0, 12),roleId);
      //单个成员点击，时候会触发这个事件，新加入一条
      if (StringUtil.isNotBlank(uid)) {
        ClueFlowEntity flowEntity = new ClueFlowEntity();
        flowEntity.setId(IdGenUtil.uuid32());
        flowEntity.setClueId(uid);
        flowEntity.setState(0);//4已转办
        flowEntity.setCreateTime(SystemClock.nowDate());
        flowEntity.setReceiveId(user.getId());
        ct = clueFlowService.insertData(flowEntity);
      }
    }
    if(flag){
      clueReportEntity.setState(Short.valueOf("0"));//0未处理，1已受理，2已办结，3已知晓，4已转办  (转办过去的单子设置为未处理)
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
