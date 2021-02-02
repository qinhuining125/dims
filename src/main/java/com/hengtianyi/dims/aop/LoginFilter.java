package com.hengtianyi.dims.aop;

import com.hengtianyi.common.core.constant.BaseConstant;
import com.hengtianyi.common.core.util.HttpUtil;
import com.hengtianyi.common.core.util.StringUtil;
import com.hengtianyi.dims.utils.WebUtil;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录状态过滤器
 *
 * @author BBF
 */
public class LoginFilter implements Filter {

  private static final String FORCED_OFF = "{success:false,message:%s}";
  private static final String LOGIN_HTML = "%s/login.html";

  @Override
  public void init(FilterConfig filterConfig) {
    // 过滤器初始化
  }

  @Override
  public void destroy() {
    // 过滤器销毁
  }

  @Override
  public void doFilter(ServletRequest servletRequest,
      ServletResponse servletResponse,
      FilterChain chain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;
    boolean isAjax = HttpUtil.isAjax(request);
    //验证用户是否登录
    if (!WebUtil.isLogin(request)) {
      //ajax显示未登录的json，非ajax跳转到登录页
      HttpUtil.responseHandle(response, isAjax,
          isAjax ? BaseConstant.NOT_LOGIN : String.format(LOGIN_HTML, request.getContextPath()));
      return;
    }
    //验证用户是否被强制下线（从Session中获取kickOut数据）
    String kickOut = (String) WebUtil.getObjectFromSession(request, BaseConstant.SESSION_KICK_OUT);
    if (StringUtil.isNoneBlank(kickOut)) {
      //强制下线，清除Session数据
      WebUtil.removeUser(request);
      //ajax显示未登录的json，非ajax跳转到登录页
      HttpUtil.responseHandle(response, isAjax,
          isAjax ? String.format(FORCED_OFF, kickOut)
              : String.format(LOGIN_HTML, request.getContextPath()));
    }
    chain.doFilter(servletRequest, servletResponse);
  }

}