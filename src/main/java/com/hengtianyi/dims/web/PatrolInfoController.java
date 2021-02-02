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
import com.hengtianyi.dims.constant.FrameConstant;
import com.hengtianyi.dims.exception.ErrorEnum;
import com.hengtianyi.dims.exception.WebException;
import com.hengtianyi.dims.service.api.PatrolInfoService;
import com.hengtianyi.dims.service.api.SysUserService;
import com.hengtianyi.dims.service.entity.PatrolInfoEntity;
import com.hengtianyi.dims.service.entity.SysUserEntity;
import com.hengtianyi.dims.utils.BarcodeUtils;
import com.hengtianyi.dims.utils.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * PatrolInfoController - Controller
 * 巡察宣传-后台管理
 * @author jyy
 */
@Controller
@RequestMapping(value = PatrolInfoController.MAPPING)
public class PatrolInfoController extends AbstractBaseController<PatrolInfoEntity, String> {

  private static final Logger LOGGER = LoggerFactory.getLogger(PatrolInfoController.class);
  public static final String MAPPING = "a/patrolInfo";
//  public static final String EWM_SHOW_COTENT = "http://192.168.32.4:8030/reporting/report.html";
//  public static final String EWM_SHOW_COTENT = "http://192.168.32.240:81/reporting/report.html";
  public static final String EWM_SHOW_COTENT = FrameConstant.PREFIX_URL + "reporting/report.html";

//  public static final String PREFIX_URL = "http://localhost:81/";



  @Resource
  private PatrolInfoService patrolInfoService;
  @Resource
  private SysUserService sysUserService;

  @Override
  public PatrolInfoService getService() {
    return patrolInfoService;
  }

  @Override
  public Logger getLogger() {
    return LOGGER;
  }

  @RequestMapping(value = "/qrimage")
  public ResponseEntity<byte[]> getQRImage(@RequestParam String words) {
    byte[] qrcode = null;
    try {
      qrcode = BarcodeUtils.drawLogoQRCode(EWM_SHOW_COTENT, words);
    } catch (Exception e) {
      System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
      e.printStackTrace();
    }
    // Set headers
    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.IMAGE_PNG);
    return new ResponseEntity<byte[]>(qrcode, headers, HttpStatus.CREATED);
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
    return "web/patrolInfo/patrolInfo_index";
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
    PatrolInfoEntity entity = null;
    if (StringUtil.isNoneBlank(id)) {
      entity = this.getDataByIdCommon(id);
      if (entity == null) {
        throw new WebException(ErrorEnum.NO_DATA);
      }
    }
    model.addAttribute("mapping", MAPPING);
    model.addAttribute("entity", entity);//添加完毕以后，就不能再进行编辑操作了。
    /*添加一个userid，和用户关联。这里的逻辑先移掉。
    String userId = WebUtil.getUserId(request);*/
    return "web/patrolInfo/patrolInfo_edit";
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
  public String saveData(HttpServletRequest request, @ModelAttribute PatrolInfoEntity entity,
                         @RequestParam(value = BaseConstant.PAGE_ADD_SIGN, required = false) String n) {
    boolean isNew = StringUtil.isBlank(n);
    if (isNew) {
      String id = entity.getId();
      if (StringUtil.isBlank(id)) {
        entity.setId(IdGenUtil.uuid32());
        entity.setUserId(WebUtil.getUserId());
      }
      entity.setCreateTime(SystemClock.nowDate());
      String filePath = getService().filePath(request, entity.getPatrolName(), entity.getId());
      entity.setImageUrl(filePath);//filePath的返回这里，还缺少图片的具体名字
      return this.insertDataCommon(entity);
    } else {
      return this.updateDataCommon(entity);
    }
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
                            @ModelAttribute PatrolInfoEntity dto, HttpServletRequest request) {

    int rowsCount = this.getService().searchDataCount(dto);
    return this.getDataListCommon(pageDto, dto);
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
    //删除地址栏上的图片
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
    ServiceResult<PatrolInfoEntity> result = new ServiceResult<>();
    PatrolInfoEntity one = this.getDataByIdCommon(id);
    if (null != one) {
      this.clearEntity(one);
      SysUserEntity userEntity = sysUserService.searchDataById(one.getUserId());
      result.setSuccess(true);
      result.setResult(one);
    } else {
      result.setError(BaseConstant.ERROR_READ);
    }
    return JsonUtil.toJson(result);
  }

  @ResponseBody
  @RequestMapping(value = "/checkPatrolName")
  public boolean checkPatrolName(@RequestParam String patrolName) {
    return patrolInfoService.checkPatrolName(patrolName);
  }

  @ResponseBody
  @RequestMapping(value = "/checkPatrolTime")
  public boolean checkPatrolTime(@RequestParam String startTime, @RequestParam String endTime) {
    return patrolInfoService.checkPatrolTime(startTime, endTime);
  }


  /**
   * 二维码展示[视图]
   * @param ewmurl    图片的url
   * @return 视图
   */
  @GetMapping(value = "/showEwm.html", produces = BaseConstant.HTML)
  public String showEwm(Model model,@RequestParam String url) {
    model.addAttribute("url", url);
    return "web/patrolInfo/patrolInfo_showEwm";
  }
}