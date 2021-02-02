package com.hengtianyi.dims.aop;

import com.hengtianyi.common.core.constant.BaseConstant;
import com.hengtianyi.common.core.util.CollectionUtil;
import com.hengtianyi.common.core.util.HttpUtil;
import com.hengtianyi.dims.utils.WebUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT过滤器
 *
 * @author BBF
 */
public class JwtFilter implements Filter {

  /**
   * 过滤器白名单，不进行token验证的url
   */
  private static final List<String> WHITE_LIST;
  /**
   * 通用token
   */
  public static final String COMMON_TOKEN = "XG5m-edfPW41LQZ0sj8WXPbgGaNq9hh3MMzkGIETVws";

  static {
    WHITE_LIST = new ArrayList<>(1);
    WHITE_LIST.add("/api/login.json");
  }

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
    if (!CollectionUtil.contains(WHITE_LIST, request.getRequestURI())) {
      // 不在白名单，进行JWT鉴权
      String token = WebUtil.getHeaderToken(request);
      if (!COMMON_TOKEN.equals(token) && !WebUtil.validToken(token)) {
        // 验证失败
        HttpUtil.responseHandle(response, true, BaseConstant.NOT_LOGIN);
        return;
      }
    }
    chain.doFilter(servletRequest, servletResponse);
  }

}