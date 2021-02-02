package com.hengtianyi.dims.web;

import com.hengtianyi.common.core.constant.BaseConstant;
import com.hengtianyi.common.core.util.JsonUtil;
import com.hengtianyi.common.core.util.StringUtil;
import com.hengtianyi.dims.aop.WebLog;
import com.hengtianyi.dims.constant.FrameConstant;
import com.hengtianyi.dims.constant.LogEnum;
import com.hengtianyi.dims.exception.ErrorEnum;
import com.hengtianyi.dims.exception.WebException;
import com.hengtianyi.dims.service.api.ClueReportService;
import com.hengtianyi.dims.service.api.IncorruptAdviceService;
import com.hengtianyi.dims.service.api.SysAuthService;
import com.hengtianyi.dims.service.api.SysUserService;
import com.hengtianyi.dims.service.api.TaskInfoService;
import com.hengtianyi.dims.service.api.TownshipService;
import com.hengtianyi.dims.service.dto.QueryDto;
import com.hengtianyi.dims.service.entity.SysUserEntity;
import com.hengtianyi.dims.service.entity.TownshipEntity;
import com.hengtianyi.dims.utils.WebUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 首页Controller
 *
 * @author BBF
 */
@Controller
@RequestMapping(value = "/")
public class IndexController {

  private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);
  @Resource
  private SysUserService sysUserService;
  @Resource
  private SysAuthService sysAuthService;
  @Resource
  private TownshipService townshipService;
  @Resource
  private IncorruptAdviceService incorruptAdviceService;
  @Resource
  private ClueReportService clueReportService;
  @Resource
  private TaskInfoService taskInfoService;

  /**
   * 登录页面
   *
   * @param request HttpServletRequest
   * @param model   Model
   * @return view
   */
  @GetMapping(value = "/login.html", produces = BaseConstant.HTML)
  public String loginHtml(HttpServletRequest request, Model model) {
    if (WebUtil.isLogin(request)) {
      return "redirect:/index.html";
    }
    //从Session中获取Kickout数据（强制下线）
    String kickout = (String) WebUtil.getObjectFromSession(request, BaseConstant.SESSION_KICK_OUT);
    WebUtil.removeObjectFromSession(request, BaseConstant.SESSION_KICK_OUT);
    request.getSession().invalidate();
    model.addAttribute("msg", kickout);
    model.addAttribute("captchaShow", false);
    return "login";
  }

  /**
   * 验证用户登录
   *
   * @param request  HttpServletRequest
   * @param model    Model
   * @param account  登录账户
   * @param password 登录密码
   * @param code     验证码
   * @return 视图名
   */
  @WebLog(value = "登录验证", type = LogEnum.LOGIN, desensitise = true)
  @PostMapping(value = "/login.html", produces = BaseConstant.HTML)
  public String loginHandler(HttpServletRequest request,
      Model model,
      @RequestParam(value = "uid") String account,
      @RequestParam(value = "pwd") String password,
      @RequestParam(value = "code", required = false) String code) {
    String errorMsg;
    try {
      if (StringUtil.isBlank(password)) {
        throw new WebException(ErrorEnum.NO_PASSWORD);
      }
      SysUserEntity sysUserEntity = sysUserService.validate(account, password);
      // 获取用户权限
      Set<String> stringSet = sysAuthService.getAuthByRoleId(sysUserEntity.getRoleId());
      sysUserEntity.setAuthList(stringSet);
      // 将用户信息写入session
      WebUtil.setUser(request, sysUserEntity);
      WebUtil.setUserName(request, sysUserEntity.getUserName());
      return FrameConstant.REDIRECT_INDEX;
    } catch (Exception ex) {
      if (ex instanceof WebException) {
        errorMsg = ex.getMessage();
      } else {
        errorMsg = "登录失败";
      }
      LOGGER.error("[IndexController.login]{}，account = {}, password = {}",
          ex.getMessage(), account, password);
    }
    model.addAttribute("msg", errorMsg);
    model.addAttribute("captchaShow", false);
    return FrameConstant.VIEW_LOGIN;
  }

  /**
   * 基础页面
   *
   * @param request HttpServletRequest
   * @param model   Model
   * @return view
   */
  @GetMapping(value = {"/", "/index.html"}, produces = BaseConstant.HTML)
  public String index(HttpServletRequest request, Model model) {
    model.addAttribute("userName", WebUtil.getUserName(request));
    model.addAttribute("roleId", WebUtil.getUser().getRoleId());
    return "index";
  }

  /**
   * 基础页面
   *
   * @return view
   */
  @GetMapping(value = "welcome.html", produces = BaseConstant.HTML)
  public String welcome(Model model) {
    List<TownshipEntity> townList = townshipService.areaList();
    QueryDto dto = new QueryDto();
    Map<String, Integer> adviceMap = new HashMap<>(townList.size());
    Map<String, Integer> reportMap = new HashMap<>(townList.size());
    Map<String, Integer> taskMap = new HashMap<>(townList.size());
    for (TownshipEntity entity : townList) {
      dto.setAreaCode(entity.getAreaCode());
      adviceMap.put(entity.getAreaName(), incorruptAdviceService.countAdvice(dto));
      reportMap.put(entity.getAreaName(), clueReportService.countReport(dto));
      taskMap.put(entity.getAreaName(), taskInfoService.countTask(dto));
    }
    model.addAttribute("adviceMap", JsonUtil.toJson(adviceMap));
    model.addAttribute("reportMap", JsonUtil.toJson(reportMap));
    model.addAttribute("taskMap", JsonUtil.toJson(taskMap));
    return "welcome/index2";
  }

  /**
   * 登出
   *
   * @param request HttpServletRequest
   * @return 重定向至登录页面
   */
  @GetMapping(value = "/logout.html")
  public String logout(HttpServletRequest request) {
    WebUtil.removeUser(request);
    request.getSession().invalidate();
    return "redirect:/login.html";
  }

}