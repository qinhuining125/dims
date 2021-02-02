package com.hengtianyi.dims.web;

import com.hengtianyi.common.core.base.service.AbstractBaseController;
import com.hengtianyi.common.core.constant.BaseConstant;
import com.hengtianyi.common.core.feature.ServiceResult;
import com.hengtianyi.common.core.util.sequence.IdGenUtil;
import com.hengtianyi.common.core.util.sequence.SystemClock;
import com.hengtianyi.dims.constant.RoleEnum;
import com.hengtianyi.dims.service.api.*;
import com.hengtianyi.dims.service.dto.RevealInfoDto;
import com.hengtianyi.dims.service.entity.*;
import com.hengtianyi.dims.utils.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * RelUserArea - Controller
 *
 * @author LY
 */
@Controller
@RequestMapping(value = ReportingController.MAPPING)
public class ReportingController extends AbstractBaseController<RelUserAreaEntity, String> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ReportingController.class);
  public static final String MAPPING = "reporting";

  @Resource
  private RelUserAreaService relUserAreaService;
  @Resource
  private RevealInfoService revealInfoService;
  @Resource
  private RevealFlowService revealFlowService;
  @Resource
  private ImageRevealService imageRevealService;
  @Resource
  private SysUserService sysUserService;
  @Resource
  private PatrolInfoService  patrolInfoService;

  @Resource
  private VillageService villageService;

  @Override
  public RelUserAreaService getService() {
    return relUserAreaService;
  }

  @Override
  public Logger getLogger() {
    return LOGGER;
  }

  /**
   * 百姓举报h5[视图]
   *
   * @param model Model
   * @return 视图
   */
  @GetMapping(value = "/report.html", produces = BaseConstant.HTML)
  public String report(Model model,@RequestParam("id") String id, HttpServletRequest request) {
//      String userId="f833f1ab30c347abb0936b6aaa734bd0";
      PatrolInfoEntity patrolInfoEntity=patrolInfoService.searchDataById(id);
//      model.addAttribute("areaList", relUserAreaService.getUserArealist(userId));
      model.addAttribute("areaList2", villageService.areaList2(""));

      model.addAttribute("patrolName",patrolInfoEntity.getPatrolName());
      model.addAttribute("inspectionId",id);
      return "web/revealInfo/report";
  }
  /**
   * 镇列表
   *
   * @return
   */
  @ResponseBody
  @GetMapping(value = "/list.json", produces = BaseConstant.JSON)
  public String list(HttpServletRequest request) {
    ServiceResult<Object> result = new ServiceResult<>();
    try {
      //当前用户
//      String userId = WebUtil.getUserId(request);
      String userId="f833f1ab30c347abb0936b6aaa734bd0";
      result.setResult(relUserAreaService.getUserArealist(userId));
    } catch (Exception e) {
      LOGGER.error("[查询下级用户出错],{}", e.getMessage(), e);
      result.setError("查询下级用户出错");
    }
    return result.toJson();
  }

  //百姓举报提交接口，去除session的验证
  /**
   * 提交百姓举报页面
   * @param dto
   * @return json
   */
  @ResponseBody
  @PostMapping(value = "/saveData.json", produces = BaseConstant.JSON)
  public String saveData(@RequestBody RevealInfoDto dto, HttpServletRequest request) {
    ServiceResult<Object> result = new ServiceResult<>();
    try {
      String userId = "";
      Integer roleId = 0;
      String token = request.getHeader("token");
      if(token==null){
        userId = "MASSES_USERID" + SystemClock.now();//对于群众需要随机设置一个id
        roleId = 2000;//群众roleId
      }else{
        userId = WebUtil.getUserIdByToken(request);//对于网格员，联络员可以从token中获取userId
        SysUserEntity userEntity = sysUserService.searchDataById(userId);
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
   * 二维码详情
   *
   * @param id id
   * @return json
   */
  @GetMapping(value = "/detail.json", produces = BaseConstant.JSON)
  public String detail(@RequestParam String id) {
    ServiceResult<Object> result = new ServiceResult<>();
    try {
      PatrolInfoEntity entity = patrolInfoService.searchDataById(id);
      Date date1=entity.getEndTime();
      Date date2 = new Date();
      DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      String n1 = format.format(date1);
      String n2 = format.format(date2);
      if(n1.compareTo(n2)>=0){
        result.setMessage("正确查询到二维码的详情信息");
        result.setResult(entity);
        result.setSuccess(true);
      }else{
        result.setMessage("该二维码已经失效，无法进行举报操作");
        result.setSuccess(false);
      }
    } catch (Exception e) {
      LOGGER.error("[二维码查询detail]出错,{}", e.getMessage(), e);
      result.setMessage("查询二维码详情出错");
      result.setSuccess(false);
    }
    return result.toJson();
  }
}
