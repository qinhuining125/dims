package com.hengtianyi.dims.api;

import com.hengtianyi.common.core.base.UserDTO;
import com.hengtianyi.common.core.constant.BaseConstant;
import com.hengtianyi.dims.service.api.SysUserService;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录api
 *
 * @author LY
 */
@RestController
@RequestMapping(value = "/api")
public class LoginApiController {

  private static final Logger LOGGER = LoggerFactory.getLogger(LoginApiController.class);
  @Resource
  private SysUserService sysUserService;

  /**
   * 登录
   *
   * @param dto 账号  密码
   * @return json+token
   */
  @PostMapping(value = "/login.json", produces = BaseConstant.JSON)
  public String login(@RequestBody UserDTO dto) {
    return sysUserService.appLogin(dto);
  }
}
