package com.hengtianyi.dims.utils;

import com.hengtianyi.common.core.constant.BaseConstant;
import com.hengtianyi.common.core.permissions.UserHelper;
import com.hengtianyi.common.core.util.CollectionUtil;
import com.hengtianyi.common.core.util.IpUtil;
import com.hengtianyi.common.core.util.UserAgentUtil;
import com.hengtianyi.common.core.util.jwt.JwtDTO;
import com.hengtianyi.common.core.util.jwt.JwtUtil;
import com.hengtianyi.common.core.util.sequence.SystemClock;
import com.hengtianyi.dims.constant.FrameConstant;
import com.hengtianyi.dims.exception.ErrorEnum;
import com.hengtianyi.dims.exception.NotLoginException;
import com.hengtianyi.dims.exception.WebException;
import com.hengtianyi.dims.service.entity.SysUserEntity;
import io.jsonwebtoken.Claims;
import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Web工具类
 *
 * @author BBF
 */
public final class WebUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(WebUtil.class);
  /**
   * 43位密钥
   */
  public static final String SECRET_KEY = "DaoKeDaoFeiChangDao0MingKeMingFeiChangMing0";
  /**
   * JWT的header名称
   */
  private static final String JWT_HEADER_NAME = "token";

  private WebUtil() {
  }

  /**
   * 跳转错误页
   *
   * @param model   Model
   * @param message 错误信息
   * @return 错误页面名称
   */
  public static String toErrorPage(Model model, String message) {
    model.addAttribute(FrameConstant.SHOW_STACK_TRACE, 0);
    model.addAttribute(FrameConstant.MESSAGE, message);
    model.addAttribute(FrameConstant.DESCRIBE, message);
    model.addAttribute(FrameConstant.EXCEPTION, message);
    return FrameConstant.VIEW_ERROR;
  }

  /**
   * 从Session中获取对象
   *
   * @param request     HttpServletRequest
   * @param sessionName SessionName
   * @return Session中保存的对象
   */
  public static Object getObjectFromSession(final HttpServletRequest request,
      final String sessionName) {
    try {
      return request.getSession().getAttribute(sessionName);
    } catch (Exception ex) {
      // 从Session获取数据异常，直接返回null
    }
    return null;
  }

  /**
   * 从Session中获取对象
   * <p style="color:red">尽量使用 getObjectFromSession(HttpServletRequest, String)</p>
   *
   * @param sessionName SessionName
   * @return Session中保存的对象
   */
  public static Object getObjectFromSession(final String sessionName) {
    try {
      ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder
          .getRequestAttributes();
      return getObjectFromSession(sra.getRequest(), sessionName);
    } catch (Exception ex) {
      // 从Session获取数据异常，直接返回null
    }
    return null;
  }

  /**
   * 对象写入Session
   *
   * @param request     HttpServletRequest
   * @param sessionName SessionName
   * @param obj         要存储的可序列化对象
   */
  public static void setObjectFromSession(final HttpServletRequest request,
      final String sessionName,
      final Serializable obj) {
    try {
      request.getSession().setAttribute(sessionName, obj);
    } catch (Exception ex) {
      // 写Session异常，不处理
    }
  }

  /**
   * 对象写入Session
   * <p style="color:red">尽量使用 setObjectFromSession(HttpServletRequest, String, Object)</p>
   *
   * @param sessionName SessionName
   * @param obj         要存储的可序列化对象
   */
  public static void setObjectFromSession(final String sessionName, final Serializable obj) {
    try {
      ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder
          .getRequestAttributes();
      setObjectFromSession(sra.getRequest(), sessionName, obj);
    } catch (Exception ex) {
      // 写Session异常，不处理
    }
  }

  /**
   * 从Session中删除对象
   *
   * @param request     HttpServletRequest
   * @param sessionName SessionName
   */
  public static void removeObjectFromSession(final HttpServletRequest request,
      final String sessionName) {
    try {
      request.getSession().removeAttribute(sessionName);
    } catch (Exception ex) {
      // 写Session异常，不处理
    }
  }

  /**
   * 从Session中删除对象
   * <p style="color:red">尽量使用 removeObjectFromSession(HttpServletRequest, String)</p>
   *
   * @param sessionName SessionName
   */
  public static void removeObjectFromSession(final String sessionName) {
    try {
      ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder
          .getRequestAttributes();
      removeObjectFromSession(sra.getRequest(), sessionName);
    } catch (Exception ex) {
      // 写Session异常，不处理
    }
  }

  /**
   * 判断用户是否登录
   *
   * @param request HttpServletRequest
   * @return true - 已登录
   */
  public static boolean isLogin(final HttpServletRequest request) {
    return null != getObjectFromSession(request, BaseConstant.SESSION_USER);
  }

  /**
   * 判断用户是否登录
   * <p style="color:red">尽量使用 isLogin(HttpServletRequest)</p>
   *
   * @return true - 已登录
   */
  public static boolean isLogin() {
    return null == getObjectFromSession(BaseConstant.SESSION_USER);
  }

  /**
   * 从Session中获取系统用户对象
   *
   * @param request HttpServletRequest
   * @return 系统用户对象
   */
  public static SysUserEntity getUser(HttpServletRequest request) {
    Object userObj = getObjectFromSession(request, BaseConstant.SESSION_USER);
    if (null == userObj) {
      throw new NotLoginException();
    }
    return (SysUserEntity) userObj;
  }

  /**
   * 从Session中获取系统用户对象
   * <p style="color:red">尽量使用 getUser(HttpServletRequest)</p>
   *
   * @return 系统用户对象
   */
  public static SysUserEntity getUser() {
    Object userObj = getObjectFromSession(BaseConstant.SESSION_USER);
    if (null == userObj) {
      throw new NotLoginException();
    }
    return (SysUserEntity) userObj;
  }

  /**
   * 用户实体写入Session
   * <p style="color:red">尽量使用 setUser(HttpServletRequest,SysUserEntity)</p>
   *
   * @param user 用户实体
   */
  public static void setUser(SysUserEntity user) {
    setObjectFromSession(BaseConstant.SESSION_USER, user);
  }

  /**
   * 从Session中获取系统用户ID
   *
   * @param request HttpServletRequest
   * @return 系统用户ID
   */
  public static String getUserId(HttpServletRequest request) {
    return getUser(request).getId();
  }

  /**
   * 从Session中获取系统用户ID
   * <p style="color:red">尽量使用 getUser(HttpServletRequest)</p>
   *
   * @return 系统用户ID
   */
  public static String getUserId() {
    return getUser().getId();
  }

  /**
   * 从Session中获取用户名
   *
   * @param request HttpServletRequest
   * @return 用户名
   */
  public static String getUserName(final HttpServletRequest request) {
    Object userName = getObjectFromSession(request, BaseConstant.SESSION_USER_NAME);
    if (null == userName) {
      throw new NotLoginException();
    }
    return (String) userName;
  }

  /**
   * 从Session中获取用户名
   * <p style="color:red">尽量使用 getUserName(HttpServletRequest)</p>
   *
   * @return 用户名
   */
  public static String getUserName() {
    Object userName = getObjectFromSession(BaseConstant.SESSION_USER_NAME);
    if (null == userName) {
      throw new NotLoginException();
    }
    return (String) userName;
  }

  /**
   * 用户名写入Session
   * <p style="color:red">尽量使用 setUserName(HttpServletRequest,String)</p>
   *
   * @param userName 用户名
   */
  public static void setUserName(final String userName) {
    setObjectFromSession(BaseConstant.SESSION_USER_NAME, userName);
  }

  /**
   * 用户名写入Session
   *
   * @param request  HttpServletRequest
   * @param userName 用户名
   */
  public static void setUserName(final HttpServletRequest request,
      final String userName) {
    setObjectFromSession(request, BaseConstant.SESSION_USER_NAME, userName);
  }

  /**
   * 从Session中删除用户名
   *
   * @param request HttpServletRequest
   */
  public static void removeUserName(final HttpServletRequest request) {
    removeObjectFromSession(request, BaseConstant.SESSION_USER_NAME);
  }

  /**
   * 从Session中删除用户名
   * <p style="color:red">尽量使用 removeUserName(HttpServletRequest)</p>
   */
  public static void removeUserName() {
    removeObjectFromSession(BaseConstant.SESSION_USER_NAME);
  }

  public static void setUser(HttpServletRequest request, SysUserEntity user) {
    user.clean();
    user.setCreateTime(null);
    user.setCreateTime(null);
    //存储登录时间
    user.setLoginTime(SystemClock.now());
    //存储SessionId
    user.setSessionId(request.getSession().getId());
    //存储登录IP
    user.setIp(IpUtil.getIp(request));
    //存储userAgent
    user.setUserAgent(new UserAgentUtil(request).toString());
    setObjectFromSession(request, BaseConstant.SESSION_USER, user);
  }

  /**
   * 从Session中删除用户对象
   *
   * @param request HttpServletRequest
   */
  public static void removeUser(HttpServletRequest request) {
    removeObjectFromSession(request, BaseConstant.SESSION_USER);
    removeUserName(request);
  }

  /**
   * 从Session中删除用户对象
   * <p style="color:red">尽量使用 removeUser(HttpServletRequest)</p>
   */
  public static void removeUser() {
    removeObjectFromSession(BaseConstant.SESSION_USER);
  }

  /**
   * 判断是否具有指定权限组的任意一个权限
   *
   * @param request    HttpServletRequest
   * @param permission 权限组
   * @return true - 具有权限
   */
  public static boolean hasPermission(HttpServletRequest request, String... permission) {
    return UserHelper.hasPermission(WebUtil.getUser(request), permission);
  }

  /**
   * 判断是否具有指定权限组的任意一个权限
   *
   * @param permission 权限组
   * @return true - 具有权限
   */
  public static boolean hasPermission(String... permission) {
    //如果权限代码不为空，则需要从session中获取权限
    ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder
        .getRequestAttributes();
    HttpServletRequest request = sra.getRequest();
    return hasPermission(request, permission);
  }

  /**
   * 生成JWT，写入到response的header中
   *
   * @param userId 需要写入JWT的userId
   * @return jwt
   */
  public static String createToken(String userId) {
    JwtDTO dto = new JwtDTO();
    dto.setClaims(CollectionUtil.singletonMap("userId", userId));
    dto.setExpired(BaseConstant.SSS_IN_DAY);
    return JwtUtil.getToken(SECRET_KEY, dto);
  }

  /**
   * 从request的header中获取token
   *
   * @param request HttpServletRequest
   * @return jwt
   */
  public static String getHeaderToken(HttpServletRequest request) {
    return request.getHeader(JWT_HEADER_NAME);
  }

  /**
   * 验证JWT
   *
   * @param token JWT字符串
   * @return true - 验证成功
   */
  public static boolean validToken(String token) {
    return JwtUtil.validToken(SECRET_KEY, token);
  }

  /**
   * 从token中获取系统用户ID
   *
   * @return 系统用户ID
   */
  public static String getUserIdByToken(HttpServletRequest request) {
    try {
      Claims claims = JwtUtil.parseToken(SECRET_KEY, getHeaderToken(request));
      return claims.get("userId").toString();
    } catch (Exception e) {
      LOGGER.error("[token转uid出错],{}", e.getMessage(), e);
      throw new WebException(ErrorEnum.NOT_LOGIN);
    }
  }

}
