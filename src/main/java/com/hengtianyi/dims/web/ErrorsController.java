package com.hengtianyi.dims.web;

import com.hengtianyi.common.core.constant.BaseConstant;
import com.hengtianyi.common.core.util.StringUtil;
import com.hengtianyi.dims.constant.FrameConstant;
import com.hengtianyi.dims.exception.ExceptionParse;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.AbstractTemplateView;

/**
 * 通用的异常处理Controller
 *
 * @author BBF
 */
@Controller
@RequestMapping("/errors")
public class ErrorsController extends BasicErrorController {

  public ErrorsController(ServerProperties serverProperties) {
    super(new DefaultErrorAttributes(), serverProperties.getError());
  }

  @RequestMapping(value = "/401", method = {RequestMethod.POST, RequestMethod.GET},
      produces = BaseConstant.HTML)
  public String unAuthorized(Model model) {
    model.addAttribute(FrameConstant.MESSAGE, "您没有权限访问这个功能。（错误码：401）");
    model.addAttribute(FrameConstant.DESCRIBE, BaseConstant.HTTP401);
    model.addAttribute(FrameConstant.EXCEPTION, StringUtil.EMPTY);
    model.addAttribute(FrameConstant.SHOW_STACK_TRACE, FrameConstant.DISABLED);
    return FrameConstant.VIEW_ERROR;
  }

  /**
   * 覆盖方法，处理@ResponseBody请求
   *
   * @param request HttpServletRequest
   * @return ResponseEntity
   */
  @Override
  public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
    String[] info = ExceptionParse.getExceptionMessage(request);
    boolean showStackTrace = isIncludeStackTrace(request, MediaType.ALL);
    Map<String, Object> body = getErrorAttributes(request, showStackTrace);
    //输出自定义的Json格式
    Map<String, Object> map = new HashMap<>(9);
    map.put("success", false);
    map.put(FrameConstant.MESSAGE, info[1]);
    map.put(FrameConstant.DESCRIBE, info[2]);
    map.put(FrameConstant.EXCEPTION, body.get(FrameConstant.MESSAGE));
    //包含堆栈的时候进行输出
    if (showStackTrace) {
      map.put(FrameConstant.TRACE, body.get(FrameConstant.TRACE));
    }
    //为适用jquery-boot-gird加入下面属性
    map.put("current", 1);
    map.put("rowCount", 10);
    map.put("total", 0);
    map.put("rows", Collections.emptyList());
    //固定返回状态码200
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

  /**
   * 覆盖方法，处理@RequestMapping(produces = "text/html")
   *
   * @param request  HttpServletRequest
   * @param response HttpServletResponse
   * @return ModelAndView
   */
  @Override
  public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
    String[] info = ExceptionParse.getExceptionMessage(request);
    response.setStatus(Integer.parseInt(info[0]));
    // 如果要显示错误堆栈，需要在request的参数中加入trace=true
    boolean showStackTrace = isIncludeStackTrace(request, MediaType.TEXT_HTML);
    Map<String, Object> body = getErrorAttributes(request, showStackTrace);
    ModelAndView model = new ModelAndView(FrameConstant.VIEW_ERROR, body);
    model.addObject(FrameConstant.MESSAGE, info[1]);
    model.addObject(FrameConstant.DESCRIBE, info[2]);
    model.addObject(FrameConstant.EXCEPTION, body.get(FrameConstant.MESSAGE));
    model.addObject(FrameConstant.SHOW_STACK_TRACE,
        showStackTrace ? FrameConstant.ENABLED : FrameConstant.DISABLED);
    if (showStackTrace) {
      model.addObject(FrameConstant.TRACE, body.get(FrameConstant.TRACE));
    }
    // 见 org.springframework.web.servlet.view.AbstractTemplateView 的154行，
    // 当配置了 spring.freemarker.expose-spring-macro-helpers=true ，如果进行错误跳转，
    // 则model中因为有两次 springMacroRequestContext 而抛出异常。
    request.removeAttribute(AbstractTemplateView.SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE);
    //指定自定义的视图
    return model;
  }
}