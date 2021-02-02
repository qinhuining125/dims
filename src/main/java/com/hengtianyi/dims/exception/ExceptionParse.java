package com.hengtianyi.dims.exception;

import com.hengtianyi.common.core.constant.BaseConstant;
import com.hengtianyi.common.core.util.JsonUtil;
import com.hengtianyi.common.core.util.StringUtil;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.ClassUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 异常处理器
 *
 * @author BBF
 */
public final class ExceptionParse {

  private static final String EXCEPTION = "exception";

  private ExceptionParse() {
  }

  /**
   * 获取错误信息
   *
   * @param request HttpServletRequest
   * @return 错误信息数组，[状态码，显示信息，隐藏信息]
   */
  public static String[] getExceptionMessage(HttpServletRequest request) {
    HttpStatus status = getHttpStatus(request);
    Throwable ex = getThrowable(request);
    String[] result = {String.valueOf(status.value()), StringUtil.EMPTY,
        null == ex ? StringUtil.EMPTY : ex.getClass().getName()};
    switch (status) {
      case BAD_REQUEST:
        if (ex instanceof TypeMismatchException) {
          TypeMismatchException exx = (TypeMismatchException) ex;
          result[1] = "发送的请求参数类型出错了。（错误码：400）";
          result[2] = String
              .format("[org.springframework.beans.TypeMismatchException] 输入参数：[%s]，接受参数：[%s]",
                  ClassUtils.getDescriptiveType(exx.getValue()),
                  ClassUtils.getQualifiedName(exx.getRequiredType()));
        } else if (ex instanceof HttpMessageNotReadableException) {
          result[1] = "发送的请求信息不可读。（错误码：400）";
          result[2] = "[org.springframework.http.converter.HttpMessageNotReadableException]";
        } else if (ex instanceof MissingServletRequestParameterException) {
          MissingServletRequestParameterException exx = (MissingServletRequestParameterException) ex;
          result[1] = "发送的请求缺少参数。（错误码：400）";
          result[2] = String.format(
              "[org.springframework.web.bind.MissingServletRequestParameterException] 缺失参数：[%s，类型%s]",
              exx.getParameterName(), exx.getParameterType());
        } else if (ex instanceof MethodArgumentNotValidException) {
          MethodArgumentNotValidException exx = (MethodArgumentNotValidException) ex;
          FieldError f = exx.getBindingResult().getFieldError();
          result[1] = String.format("%s.（错误码：400）", f.getDefaultMessage());
          result[2] = "[org.springframework.web.bind.MethodArgumentNotValidException]";
        } else {
          result[1] = "您发送的请求出错了。（错误码：400）";
        }
        break;
      case FORBIDDEN:
        result[1] = "资源不可用，服务器拒绝了您的请求。（错误码：403）";
        break;
      case NOT_FOUND:
        result[1] = "您访问的地址不存在，请确认输入的URL地址。（错误码：404）";
        break;
      case METHOD_NOT_ALLOWED:
        if (ex instanceof HttpRequestMethodNotSupportedException) {
          HttpRequestMethodNotSupportedException exx = (HttpRequestMethodNotSupportedException) ex;
          result[1] = "服务器不允许本次请求。（错误码：405）";
          result[2] = String
              .format("[org.springframework.web.HttpRequestMethodNotSupportedException] 方法名：[%s]",
                  exx.getMethod());
        } else {
          result[1] = "您不能直接访问此页面。（错误码：405）";
        }
        break;
      case NOT_ACCEPTABLE:
        if (ex instanceof HttpMediaTypeNotAcceptableException) {
          HttpMediaTypeNotAcceptableException exx = (HttpMediaTypeNotAcceptableException) ex;
          result[1] = "服务器不接受的媒体类型。（错误码：406）";
          result[2] = String
              .format("[org.springframework.web.HttpMediaTypeNotAcceptableException] 支持类型：%s",
                  JsonUtil.toJson(exx.getSupportedMediaTypes()));
        } else {
          result[1] = "服务器不接受的媒体类型。（错误码：406）";
        }
        break;
      case UNSUPPORTED_MEDIA_TYPE:
        if (ex instanceof HttpMediaTypeNotSupportedException) {
          HttpMediaTypeNotSupportedException exx = (HttpMediaTypeNotSupportedException) ex;
          result[1] = "服务器不支持请求的媒体类型。（错误码：415）";
          result[2] = String
              .format("[org.springframework.web.HttpMediaTypeNotSupportedException] 不支持的媒体类型：%s",
                  exx.getContentType());
        } else {
          result[1] = "服务器不支持请求的媒体类型。（错误码：415）";
        }
        break;
      default:
        result[0] = "500";
        if (ex instanceof WebException) {
          result[1] = ex.getMessage();
        } else {
          result[1] = "服务器内部错误。（错误码：500）";
        }
        break;
    }
    return result;
  }

  /**
   * 在request中获取异常类
   *
   * @param request HttpServletRequest
   * @return Throwable
   */
  private static Throwable getThrowable(final HttpServletRequest request) {
    RequestAttributes requestAttributes = new ServletRequestAttributes(request);
    Throwable exception = getAttribute(requestAttributes, EXCEPTION);
    if (null == exception) {
      exception = getAttribute(requestAttributes,
          "org.springframework.boot.autoconfigure.web.DefaultErrorAttributes.ERROR");
    }
    if (null == exception) {
      exception = getAttribute(requestAttributes, BaseConstant.SERVLET_EXCEPTION);
    }
    return exception;
  }

  /**
   * 从RequestAttributes中获取指定属性
   *
   * @param requestAttributes RequestAttributes
   * @param name              属性名
   * @param <T>               对象类型
   * @return 属性值
   */
  @SuppressWarnings("unchecked")
  private static <T> T getAttribute(final RequestAttributes requestAttributes, String name) {
    try {
      return (T) requestAttributes.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
    } catch (Exception ex) {
      return null;
    }
  }

  /**
   * 获取Request状态码
   *
   * @param request HttpServletRequest
   * @return HttpStatus
   */
  private static HttpStatus getHttpStatus(ServletRequest request) {
    Integer statusCode = (Integer) request.getAttribute(BaseConstant.SERVLET_STATUS);
    if (statusCode == null) {
      return HttpStatus.INTERNAL_SERVER_ERROR;
    }
    try {
      return HttpStatus.valueOf(statusCode);
    } catch (Exception ex) {
      return HttpStatus.INTERNAL_SERVER_ERROR;
    }
  }
}
