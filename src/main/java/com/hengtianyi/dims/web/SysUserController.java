package com.hengtianyi.dims.web;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.hengtianyi.common.core.annotation.Permissions;
import com.hengtianyi.common.core.base.BaseEntity;
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
import com.hengtianyi.dims.service.api.RelUserAreaService;
import com.hengtianyi.dims.service.api.SysUserService;
import com.hengtianyi.dims.service.api.TownshipService;
import com.hengtianyi.dims.service.dto.UserDto;
import com.hengtianyi.dims.service.entity.RelUserAreaEntity;
import com.hengtianyi.dims.service.entity.SysUserEntity;
import com.hengtianyi.dims.service.entity.TownshipEntity;
import com.hengtianyi.dims.utils.WebUtil;

import java.util.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * SysUser - Controller
 *
 * @author BBF
 */
@Controller
@RequestMapping(value = "/a/sysUser")
public class SysUserController extends AbstractBaseController<SysUserEntity, String> {

  private static final Logger LOGGER = LoggerFactory.getLogger(SysUserController.class);

  @Resource
  private SysUserService sysUserService;
  @Resource
  private RelUserAreaService relUserAreaService;
  @Resource
  private TownshipService townshipService;
  /**
   * 在配置文件中定义的默认密码
   */
  @Value("${default.password}")
  private String defaultPassword;

  @Override
  public SysUserService getService() {
    return sysUserService;
  }

  @Override
  public Logger getLogger() {
    return LOGGER;
  }

  /**
   * 模块首页[视图]
   *
   * @return 视图
   */
  @GetMapping(value = "/index.html", produces = BaseConstant.HTML)
  public String index(Model model) {
    List<TownshipEntity> list = townshipService.areaList();
    model.addAttribute("areaList",list);
    return "web/sysUser/sysUser_index";
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
    model.addAttribute("entity", this.getDataByIdCommon(id));
    boolean isNew = false;
    if (StringUtil.isBlank(id)) {
      // 新增，显示密码输入框，并填充默认密码
      isNew = true;
      model.addAttribute("pwd", defaultPassword);
    }
    model.addAttribute("isNew", isNew);
    return "web/sysUser/sysUser_edit";
  }

  /**
   * 修改账号[视图] 面向所有登录用户
   *
   * @param request HttpServletRequest
   * @param model   Model
   * @return 视图
   */
  @GetMapping(value = "/edit_info.html", produces = BaseConstant.HTML)
  public String editInfo(HttpServletRequest request,
      Model model) {
    SysUserEntity user = WebUtil.getUser(request);
    model.addAttribute("special", BaseConstant.ASC);
    model.addAttribute("account", user.getUserAccount());
    model.addAttribute("url", "saveDataAccount.json");
    model.addAttribute("isAdmin", false);
    return "web/sysUser/sysUser_edit_info";
  }

  /**
   * 管理员修改账号[视图]
   *
   * @param model Model
   * @param uid   用户ID
   * @return 视图
   */
  @GetMapping(value = "/edit_info_admin.html", produces = BaseConstant.HTML)
  public String editInfoAdmin(Model model, @RequestParam String uid) {
    SysUserEntity user = this.getDataByIdCommon(uid);
    model.addAttribute("special", BaseConstant.ASC);
    model.addAttribute("account", user.getUserAccount());
    model.addAttribute("url", "saveDataAccountAdmin.json?uid=" + user.getId());
    model.addAttribute("isAdmin", true);
    return "web/sysUser/sysUser_edit_info";
  }

  /**
   * 用户修改自己的密码[视图] 面向所有登录用户
   *
   * @return 视图
   */
  @GetMapping(value = "/edit_pwd.html", produces = BaseConstant.HTML)
  public String editPwd() {
    return "web/sysUser/sysUser_edit_pwd";
  }

  /**
   * 管理员修改密码[视图]
   *
   * @param model Model
   * @param uid   用户ID
   * @return 视图
   */
  @GetMapping(value = "/edit_pwd_admin.html", produces = BaseConstant.HTML)
  public String editPwdAdmin(Model model, @RequestParam String uid) {
    model.addAttribute("uid", uid);
    return "web/sysUser/sysUser_edit_pwd_admin";
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
      @ModelAttribute SysUserEntity dto) {
    return this.getDataListCommon(pageDto, dto);
  }

  /**
   * 通过AJAX获取单条信息[JSON]
   *
   * @param id 主键ID
   * @return JSON
   */
  @ResponseBody
  @RequestMapping(value = "/getDataInfo.json", produces = BaseConstant.JSON,
      method = {RequestMethod.GET, RequestMethod.POST})
  public String getDataInfo(@RequestParam String id) {
    return this.getDataInfoCommon(id);
  }

  /**
   * 通过AJAX删除[JSON]
   *
   * @param idsDto 主键集合DTO
   * @return JSON
   */
  @WebLog(value = "删除用户", type = LogEnum.ADMIN_USER)
  @Permissions("sys:user:manage")
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
  @WebLog(value = "保存用户", type = LogEnum.ADMIN_USER)
  @Permissions("sys:user:manage")
  @ResponseBody
  @PostMapping(value = "/saveData.json", produces = BaseConstant.JSON)
  public String saveData(@ModelAttribute SysUserEntity entity,
      @RequestParam(value = BaseConstant.PAGE_ADD_SIGN, required = false) String n) {
    boolean isNew = StringUtil.isBlank(n);
    long time = SystemClock.now();
    Integer enabled = entity.getEnabled();
    if (enabled == null) {
      // 未勾选，设置为0
      entity.setEnabled(FrameConstant.DISABLED);
    }
    if (isNew) {
      String id = entity.getId();
      if (StringUtil.isBlank(id)) {
        entity.setId(IdGenUtil.uuid32());
      }
      if (StringUtil.isBlank(entity.getPassword())) {
        entity.setPassword(defaultPassword);
      }
      entity.setCreateTime(time);
      return this.insertDataCommon(entity);
    } else {
      return this.updateDataCommon(entity);
    }
  }

  /**
   * 通过AJAX保存修改账号[JSON]
   *
   * @param request HttpServletRequest
   * @param account 新账号
   * @return JSON
   */
  @WebLog(value = "保存登录账号", type = LogEnum.SAVE_ACCOUNT)
  @ResponseBody
  @PostMapping(value = "/saveDataAccount.json", produces = BaseConstant.JSON)
  public String saveDataAccount(HttpServletRequest request,
      @RequestParam(value = "account") String account) {
    ServiceResult<Integer> result = new ServiceResult<>();
    result.setError("保存出错");
    try {
      SysUserEntity user = WebUtil.getUser(request);
      user.setUserAccount(account);
      if (this.saveAccount(user, user.getId())) {
        WebUtil.setUser(request, user);
        result.setResult(1);
        result.setSuccess(true);
      }
    } catch (Exception ex) {
      LOGGER.error("[SysUserController.saveDataAccount]修改账号出错，{}", ex.getMessage(), ex);
    }
    return result.toJson();
  }

  /**
   * 通过AJAX保存修改账号，管理员[JSON]
   *
   * @param request HttpServletRequest
   * @param account 新账号
   * @param uid     用户ID
   * @return JSON
   */
  @WebLog(value = "保存登录账号", type = LogEnum.SAVE_ACCOUNT)
  @ResponseBody
  @PostMapping(value = "/saveDataAccountAdmin.json", produces = BaseConstant.JSON)
  public String saveDataAccountAdmin(HttpServletRequest request,
      @RequestParam(value = "account") String account,
      @RequestParam(value = "uid") String uid) {
    ServiceResult<Integer> result = new ServiceResult<>();
    result.setError("保存出错");
    try {
      SysUserEntity user = this.getDataByIdCommon(uid);
      user.setUserAccount(account);
      if (this.saveAccount(user, WebUtil.getUserId(request))) {
        result.setResult(1);
        result.setSuccess(true);
      }
    } catch (Exception ex) {
      LOGGER.error("[SysUserController.saveDataAccountAdmin]修改账号出错，{}", ex.getMessage(), ex);
    }
    return result.toJson();
  }

  /**
   * 通过AJAX保存修改密码[JSON]
   *
   * @param request     HttpServletRequest
   * @param oldPassword 旧密码
   * @param newPassword 新密码
   * @return JSON
   */
  @WebLog(value = "保存登录密码", type = LogEnum.SAVE_PASSWORD, desensitise = true)
  @ResponseBody
  @PostMapping(value = "/saveDataPassword.json", produces = BaseConstant.JSON)
  public String saveDataPassword(HttpServletRequest request,
      @RequestParam(value = "a1") String oldPassword,
      @RequestParam(value = "a2") String newPassword) {
    ServiceResult<Integer> result = new ServiceResult<>();
    String uid = WebUtil.getUserId(request);
    try {
      // 比较旧密码
      if (!sysUserService.validateByUid(uid, oldPassword)) {
        throw new WebException(ErrorEnum.PASSWORD_INVALID);
      }
      //修改密码
      sysUserService.changePassword(uid, newPassword);
      result.setResult(BaseConstant.NUM_1);
      result.setSuccess(true);
    } catch (WebException ex) {
      result.setError(ex.getMessage());
      result.setResult(BaseConstant.NUM_0);
      LOGGER.error("[SysUserController.saveDataPassword]修改密码出错，{}", ex.getMessage(), ex);
    } catch (Exception ex) {
      result.setError("修改密码出错");
      result.setResult(BaseConstant.NUM_0);
      LOGGER.error("[SysUserController.saveDataPassword]修改密码出错，{}", ex.getMessage(), ex);
    }
    return result.toJson();
  }

  /**
   * 通过AJAX保存修改密码，管理员[JSON]
   *
   * @param uid      用户ID
   * @param password 新密码
   * @return JSON
   */
  @WebLog(value = "管理员修改密码", type = LogEnum.ADMIN_PASSWORD, desensitise = true)
  @ResponseBody
  @PostMapping(value = "/saveDataPasswordAdmin.json", produces = BaseConstant.JSON)
  public String saveDataPasswordAdmin(@RequestParam String uid,
      @RequestParam String password) {
    ServiceResult<Integer> result = new ServiceResult<>();
    try {
      //修改密码
      sysUserService.changePassword(uid, password);
      result.setResult(BaseConstant.NUM_1);
      result.setSuccess(true);
    } catch (WebException ex) {
      result.setError(ex.getMessage());
      result.setResult(BaseConstant.NUM_0);
      LOGGER.error("[SysUserController.saveDataPasswordAdmin]修改密码出错，{}", ex.getMessage(), ex);
    } catch (Exception ex) {
      result.setError("修改密码出错");
      result.setResult(BaseConstant.NUM_0);
      LOGGER.error("[SysUserController.saveDataPasswordAdmin]修改密码出错，{}", ex.getMessage(), ex);
    }
    return result.toJson();
  }

  /**
   * 通过AJAX检查登录名是否重复[JSON]
   *
   * @param request HttpServletRequest
   * @param id      主键
   * @param name    登录名
   * @return true-无重名，false-有重名
   */
  @ResponseBody
  @PostMapping(value = "/check.json", produces = BaseConstant.JSON)
  public Boolean checkRepeat(HttpServletRequest request,
      @RequestParam(required = false) String id,
      @RequestParam String name) {
    try {
      if (StringUtil.equals(id, BaseConstant.ASC)) {
        // 用户修改自己账号
        if (WebUtil.getUser().getUserAccount().equals(name)) {
          return false;
        }
        id = WebUtil.getUserId(request);
      }
      return sysUserService.checkRepeat(id, name);
    } catch (Exception ex) {
      LOGGER
          .error("[SysUserController.check]{}, id = {}, name = {}", ex.getMessage(), id, name, ex);
    }
    return false;
  }


  @Override
  public void clearEntity(SysUserEntity entity) {
    if (null != entity) {
      entity.clean();
      entity.setCreateTime(null);
      int enabled = entity.getEnabled();
      if (enabled != FrameConstant.ENABLED) {
        entity.setRowStatus(BaseEntity.ST_DANGER);
      }
    }
  }

  /**
   * 保存用户账号
   *
   * @param user  用户实体
   * @param opUid 操作人ID
   * @return true - 成功
   */
  private boolean saveAccount(SysUserEntity user, String opUid) {
    long time = SystemClock.now();
    // 修改账号
    SysUserEntity entity = new SysUserEntity();
    entity.setUserAccount(user.getUserAccount());
    entity.setId(user.getId());
    return 0 < sysUserService.updateData(entity);
  }

  @GetMapping(value = "/bindArea.html", produces = BaseConstant.HTML)
  public String bindArea(Model model, @RequestParam String uid) {
    model.addAttribute("uid", uid);
    List<RelUserAreaEntity> areaEntityList = relUserAreaService.getUserArealist(uid);
    List<String> userAreaList = new ArrayList<>();
    for (RelUserAreaEntity areaEntity : areaEntityList) {
      userAreaList.add(areaEntity.getAreaCode());
    }
    model.addAttribute("userAreaList", JsonUtil.toJson(userAreaList));
    model.addAttribute("areaAllList", townshipService.areaList());
    return "web/sysUser/sysUser_area";
  }

  @ResponseBody
  @PostMapping(value = "/savearea.json", produces = BaseConstant.JSON)
  public String saveTree(@RequestParam String uid,
      @RequestParam(defaultValue = StringUtil.EMPTY) String ids) {
    if (StringUtil.isBlank(uid)) {
      throw new WebException(ErrorEnum.AUTHORIZE_PARAMETER);
    }
    List<String> areaList = null;
    if (StringUtil.isNotBlank(ids)) {
      areaList = Arrays.asList(StringUtil.split(ids, StringUtil.C_COMMA));
    }
    int ct = -1;
    // 给指定用户ID绑定角色集合
    if (StringUtil.isNotBlank(uid)) {
      RelUserAreaEntity areaEntity = null;
      relUserAreaService.deleteUserArea(uid);
      for (String area : areaList) {
        areaEntity = new RelUserAreaEntity();
        String[] array = area.split("-");
        areaEntity.setUserId(uid);
        areaEntity.setAreaCode(array[0]);
        areaEntity.setAreaName(array[1]);
        ct = relUserAreaService.insertData(areaEntity);
      }
    }
    ServiceResult<Boolean> result = new ServiceResult<>();
    result.setSuccess(true);
    result.setResult(ct > -1);
    return result.toJson();
  }

  @GetMapping(value = "/excelImport.html", produces = BaseConstant.HTML)
  public String excelImport(Model model) {
    return "web/sysUser/excelImport";
  }

  @ResponseBody
  @PostMapping(value = "/importUser.json", produces = BaseConstant.JSON)
  public String importUser(@RequestParam("file") MultipartFile multipartFile) {
    ServiceResult<Boolean> result = new ServiceResult<>();
    try {
      ImportParams params = new ImportParams();
      params.setSheetNum(1);
      List<UserDto> list = ExcelImportUtil
          .importExcel(multipartFile.getInputStream(), UserDto.class, params);
      for (UserDto user : list) {
        SysUserEntity entity = new SysUserEntity();
        entity.setId(IdGenUtil.uuid32());
        entity.setUserAccount(user.getAccount());
        entity.setPassword(user.getPassword());
        entity.setUserName(user.getUserName());
        entity.setEnabled(1);
        entity.setSex(user.getSex());
        entity.setAreaCode(user.getAreaCode());
        entity.setAreaName(user.getAreaName());
        entity.setRoleId(1001);
        entity.setIdCard(user.getIdCard());
        entity.setCreateTime(SystemClock.now());
        sysUserService.insertData(entity);
      }
      result.setSuccess(true);
      result.setMessage("导入用户成功");
    } catch (Exception e) {
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      LOGGER.error("导入用户失败");
      result.setError("导入用户失败");
    }
    return result.toJson();
  }

}
