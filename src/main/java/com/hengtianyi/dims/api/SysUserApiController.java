package com.hengtianyi.dims.api;

import com.hengtianyi.common.core.constant.BaseConstant;
import com.hengtianyi.common.core.feature.ServiceResult;
import com.hengtianyi.dims.service.api.RelUserAreaService;
import com.hengtianyi.dims.service.api.SysUserService;
import com.hengtianyi.dims.service.dto.UserPasDto;
import com.hengtianyi.dims.utils.WebUtil;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户接口
 *
 * @author LY
 */
@RestController
@RequestMapping(value = "/api/user")
public class SysUserApiController {

  private static final Logger LOGGER = LoggerFactory.getLogger(IncorruptAdviceApiController.class);

  @Resource
  private RelUserAreaService relUserAreaService;
  @Resource
  private SysUserService sysUserService;

  /**
   * 角色列表
   *
   * @return
   */
  @GetMapping(value = "/list.json", produces = BaseConstant.JSON)
  public String list(HttpServletRequest request) {
    ServiceResult<Object> result = new ServiceResult<>();
    try {
      //当前用户
      String userId = WebUtil.getUserIdByToken(request);
      result.setResult(relUserAreaService.childUsers(userId));
      result.setSuccess(true);
    } catch (Exception e) {
      LOGGER.error("[查询下级用户出错],{}", e.getMessage(), e);
      result.setError("查询下级用户出错");
    }
    return result.toJson();
  }

  /**
   * 非网格员用户列表
   *
   * @param request
   * @return json
   */
  @PostMapping(value = "/nonGrid.json", produces = BaseConstant.JSON)
  public String nonGrid(HttpServletRequest request) {
    ServiceResult<Object> result = new ServiceResult<>();
    try {
      //当前用户
      result.setResult(sysUserService.nonGrid());
      result.setSuccess(true);
    } catch (Exception e) {
      LOGGER.error("[查询非网格员用户出错],{}", e.getMessage(), e);
      result.setError("查询非网格员用户出错");
    }
    return result.toJson();
  }

  /**
   * 修改密码
   *
   * @param request
   * @return
   */
  @PostMapping(value = "/changpas.json", produces = BaseConstant.JSON)
  public String changpas(HttpServletRequest request, @RequestBody UserPasDto dto) {
    ServiceResult<Boolean> result = new ServiceResult<>();
    try {
      String uid = WebUtil.getUserIdByToken(request);
      // 比较旧密码
      if (sysUserService.validateByUid(uid, dto.getOldPassword())) {
        //修改密码
        sysUserService.changePassword(uid, dto.getNewPassword());
        result.setResult(Boolean.TRUE);
        result.setSuccess(Boolean.TRUE);
      } else {
        result.setError("原始密码错误");
        result.setResult(Boolean.FALSE);
      }
    } catch (Exception ex) {
      result.setError("修改密码出错");
      result.setResult(Boolean.FALSE);
      LOGGER.error("[SysUserController.saveDataPassword]修改密码出错，{}", ex.getMessage(), ex);
    }
    return result.toJson();
  }
}