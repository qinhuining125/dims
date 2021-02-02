package com.hengtianyi.dims.web;

import com.hengtianyi.common.core.base.CommonPageDto;
import com.hengtianyi.common.core.base.CommonStringDto;
import com.hengtianyi.common.core.base.service.AbstractBaseController;
import com.hengtianyi.common.core.constant.BaseConstant;
import com.hengtianyi.common.core.feature.ServiceResult;
import com.hengtianyi.common.core.util.JsonUtil;
import com.hengtianyi.common.core.util.StringUtil;
import com.hengtianyi.dims.constant.FrameConstant;
import com.hengtianyi.dims.exception.ErrorEnum;
import com.hengtianyi.dims.exception.WebException;
import com.hengtianyi.dims.service.api.*;
import com.hengtianyi.dims.service.dto.QueryDto;
import com.hengtianyi.dims.service.dto.InspectionPublicityDto;
import com.hengtianyi.dims.service.entity.PatrolInfoEntity;
import com.hengtianyi.dims.service.entity.SysUserEntity;
import com.hengtianyi.dims.service.entity.InspectionPublicityEntity;
import com.hengtianyi.dims.utils.WebUtil;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.util.List;

/**
 * InspectionPublicity - Controller
 *
 * @author LY
 */
@Controller
@RequestMapping(value = InspectionPublicityController.MAPPING)
public class InspectionPublicityController extends AbstractBaseController<InspectionPublicityEntity, String> {

  private static final Logger LOGGER = LoggerFactory.getLogger(InspectionPublicityController.class);
  public static final String MAPPING = "a/inspectionPublicity";

  @Resource
  private PatrolInfoService patrolInfoService;
  @Resource
  private InspectionPublicityService inspectionPublicityService;
  @Resource
  private SysUserService sysUserService;
  @Resource
  private TaskFlowService taskFlowService;
  @Resource
  private TownshipService townshipService;
  @Resource
  private RelUserAreaService relUserAreaService;

  @Override
  public InspectionPublicityService getService() {
    return inspectionPublicityService;
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
  public String index(Model model) {
    model.addAttribute("roleId", WebUtil.getUser().getRoleId());
    model.addAttribute("mapping", MAPPING);
    model.addAttribute("areaList", townshipService.areaList());
    return "web/inspectionPublicity/inspectionPublicity_index";
  }
  @RequestMapping("/hello")
  public String helloSpringBoot() {
    return "Hello SpringBoot Proaaaaaaaaaaaaaaaaaject.";
  }
  @GetMapping(value = "/editdemo.html", produces = BaseConstant.HTML)
  public String editdemo(Model model, @RequestParam(value = "id", required = false) String id,
                     HttpServletRequest request) {

    return "web/inspectionPublicity/inspectionPublicity_editdemo1";
  }
  @GetMapping(value = "/editdemo1.html", produces = BaseConstant.HTML)
  public String editdemo2(Model model, @RequestParam(value = "id", required = false) String id,
                         HttpServletRequest request) {
    return "web/inspectionPublicity/inspectionPublicity_demo2";
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
    InspectionPublicityEntity entity = null;
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
    return "web/inspectionPublicity/inspectionPublicity_edit";
  }


  /**
   * 通过AJAX获取二维码[JSON]
   *
   * @param id 主键ID
   * @return JSON
   */

  @ResponseBody
  @GetMapping(value = "/getQRCodelist.json", produces = BaseConstant.JSON)
  public String getQRCodelist() {
//    return JsonUtil.toJson("result");
    List<PatrolInfoEntity> list=patrolInfoService.getImageUrl();
//    System.out.println(json);
    return JsonUtil.toJson(list);
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
                            @ModelAttribute InspectionPublicityEntity dto, HttpServletRequest request) {
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
    }
    queryDto.setCurrentPage(pageDto.getCurrent());
    queryDto.setFirst((pageDto.getCurrent() - 1) * FrameConstant.PAGE_SIZE);
    queryDto.setEnd(pageDto.getCurrent() * FrameConstant.PAGE_SIZE);
    return inspectionPublicityService.listData(queryDto).toJsonHtml();
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
    ServiceResult<InspectionPublicityEntity> result = new ServiceResult<>();
    InspectionPublicityEntity one = this.getDataByIdCommon(id);
    if (null != one) {
      this.clearEntity(one);
      SysUserEntity userEntity = sysUserService.searchDataById(one.getUserId());
      if (userEntity != null) {
        one.setUserName(userEntity.getUserName());
      }
      one.setFlows(taskFlowService.getAllFlows(one.getId()));
      one.setImg(inspectionPublicityService.getImages(id));
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
    //    return deleteDataCommon(idsDto);
    return inspectionPublicityService.deleteInfo(idsDto);
  }

  /**
   * 通过AJAX保存[JSON]
   *
   * @return JSON
   */
  @ResponseBody
  @PostMapping(value = "/saveData.json", produces = BaseConstant.JSON)
  public String saveData(@ModelAttribute InspectionPublicityDto dto, HttpServletRequest request) {
    ServiceResult<Integer> result = new ServiceResult<>();
    System.out.println(dto);
    try {
      int ct = inspectionPublicityService.saveData(dto, request);
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
  public void clearEntity(InspectionPublicityEntity entity) {
    if (null != entity) {
      entity.clean();

    }
  }
}
