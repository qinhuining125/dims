package com.hengtianyi.dims.web;

import com.hengtianyi.common.core.constant.BaseConstant;
import com.hengtianyi.common.core.feature.ServiceResult;
import com.hengtianyi.common.core.util.JsonUtil;
import com.hengtianyi.common.core.util.StringUtil;
import com.hengtianyi.dims.constant.RoleEnum;
import com.hengtianyi.dims.exception.ErrorEnum;
import com.hengtianyi.dims.exception.WebException;
import com.hengtianyi.dims.service.api.RelRoleAuthService;
import com.hengtianyi.dims.service.api.SysAuthService;
import com.hengtianyi.dims.service.api.SysUserService;
import com.hengtianyi.dims.service.dto.KeyValueDto;
import com.hengtianyi.dims.service.entity.SysAuthEntity;
import com.hengtianyi.dims.service.entity.SysUserEntity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author LY
 */
@Controller
@RequestMapping(value = SysRoleController.MAPPING)
public class SysRoleController {

  private static final Logger LOGGER = LoggerFactory.getLogger(SysRoleController.class);
  public static final String MAPPING = "a/sysRole";
  @Resource
  private SysUserService sysUserService;
  @Resource
  private RelRoleAuthService relRoleAuthService;
  @Resource
  private SysAuthService sysAuthService;

  /**
   * 管理员修改密码[视图]
   *
   * @param model Model
   * @return 视图
   */
  @GetMapping(value = "/index.html", produces = BaseConstant.HTML)
  public String index(Model model) {
    RoleEnum[] roleEnums = RoleEnum.values();
    List<KeyValueDto> dtoList = new ArrayList<>();
    for (RoleEnum roleEnum : roleEnums) {
      dtoList.add(new KeyValueDto(roleEnum.getRoleId().toString(), roleEnum.getName()));
    }
    model.addAttribute("roleList", dtoList);
    return "web/sysRole/role_index";
  }

  @GetMapping(value = "/tree.html", produces = BaseConstant.HTML)
  public String tree(Model model, @RequestParam String uid) {
    if (StringUtil.isBlank(uid)) {
      throw new WebException(ErrorEnum.AUTHORIZE_PARAMETER);
    }
    RoleEnum[] roleEnums = RoleEnum.values();
    List<KeyValueDto> roleList = new ArrayList<>();
    for (RoleEnum roleEnum : roleEnums) {
      roleList.add(new KeyValueDto(roleEnum.getRoleId().toString(), roleEnum.getName()));
    }
    // 全部的角色数据
    model.addAttribute("roleList", roleList);
    // 指定用户拥有的角色
    model.addAttribute("uid", uid);
    model.addAttribute("mapping", MAPPING);
    return "web/sysRole/sysRole_tree";
  }

  @ResponseBody
  @PostMapping(value = "/savetree.json", produces = BaseConstant.JSON)
  public String saveTree(@RequestParam String uid, @RequestParam Integer roleId) {
    if (StringUtil.isBlank(uid)) {
      throw new WebException(ErrorEnum.AUTHORIZE_PARAMETER);
    }
    int ct = -1;
    // 给指定用户ID绑定角色集合
    if (StringUtil.isNotBlank(uid) && roleId > 0) {
      SysUserEntity userEntity = sysUserService.searchDataById(uid);
      userEntity.setRoleId(roleId);
      ct = sysUserService.updateData(userEntity);
    }
    ServiceResult<Boolean> result = new ServiceResult<>();
    result.setSuccess(ct > -1);
    result.setResult(ct > -1);
    return result.toJson();
  }

  @GetMapping(value = "/authtree.html", produces = BaseConstant.HTML)
  public String authtree(Model model, @RequestParam Integer rid) {
    List<String> authList = relRoleAuthService.getRoleAuthByRoleId(rid);
    model.addAttribute("userAuthList", JsonUtil.toJson(authList));
    model.addAttribute("authAllList", sysAuthService.searchAllData(new SysAuthEntity(), null));
    model.addAttribute("rid", rid);
    return "web/sysRole/sysAuth_tree";
  }

  @ResponseBody
  @PostMapping(value = "/saveAuthtree.json", produces = BaseConstant.JSON)
  public String saveAuthtree(@RequestParam Integer rid, @RequestParam String ids) {
    if (rid == null) {
      throw new WebException(ErrorEnum.AUTHORIZE_PARAMETER);
    }
    List<String> authList = null;
    if (StringUtil.isNotBlank(ids)) {
      authList = Arrays.asList(StringUtil.split(ids, StringUtil.C_COMMA));
    }
    // 给指定角色ID绑定权限集合
    int ct = sysAuthService.saveAuthByRoleId(rid, authList);
    ServiceResult<Boolean> result = new ServiceResult<>();
    result.setSuccess(true);
    result.setResult(ct > -1);
    return result.toJson();
  }
}
