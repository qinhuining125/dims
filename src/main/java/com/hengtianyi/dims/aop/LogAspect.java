package com.hengtianyi.dims.aop;

import com.hengtianyi.common.core.util.JsonUtil;
import com.hengtianyi.common.core.util.StringUtil;
import com.hengtianyi.common.core.util.sequence.SystemClock;
import com.hengtianyi.dims.constant.FrameConstant;
import com.hengtianyi.dims.constant.LogEnum;
import com.hengtianyi.dims.disruptor.LogUtil;
import com.hengtianyi.dims.service.api.SysUserService;
import com.hengtianyi.dims.service.entity.SysLogEntity;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 系统日志切面类
 *
 * @author BBF
 */
@Aspect
@Component
public class LogAspect {

  /**
   * 数据脱敏字符串
   */
  private static final String DESENSITISE_STR = "******";

  @Resource
  private SysUserService sysUserService;

  /**
   * 从登录请求中查找用户账号（第一个字符串类型的参数）
   *
   * @param args 入参
   * @return 用户账号
   */
  private static String getUserAccount(Object[] args) {
    for (int i = 0, l = args.length; i < l; i++) {
      Object one = args[i];
      if (one instanceof String) {
        return (String) one;
      }
    }
    return null;
  }

  /**
   * 将method的参数转换为json字符串
   *
   * @param args pjp.getArgs()
   * @return json字符串
   */
  public static String convertParameterToJson(Object[] args) {
    int len = args.length;
    List<Object> argList = new ArrayList<>(len);
    for (int i = 0; i < len; i++) {
      Object one = args[i];
      if (one == null ||
          one instanceof ServletRequest ||
          one instanceof ServletResponse ||
          one instanceof Model ||
          one instanceof HttpSession ||
          one instanceof InputStream ||
          one instanceof OutputStream) {
        continue;
      }
      argList.add(args[i]);
    }
    len = argList.size();
    if (len == 0) {
      return StringUtil.EMPTY;
    } else if (len == 1) {
      return getJson(argList.get(0));
    } else {
      return JsonUtil.toJson(argList);
    }
  }

  /**
   * 根据对象类型进行字符串转换
   *
   * @param obj 对象
   * @return 字符串
   */
  private static String getJson(Object obj) {
    if (obj == null) {
      return StringUtil.EMPTY;
    }
    if (obj instanceof String) {
      return (String) obj;
    }
    return JsonUtil.toJson(obj);
  }

  /**
   * Controller层切点 注解拦截
   */
  @Pointcut("@annotation(com.hengtianyi.dims.aop.WebLog)")
  public void controllerAspect() {
    // 切入点方法，不需要内容
  }

  /**
   * 环绕通知，决定真实的方法是否执行，而且必须有返回值。 同时在所拦截方法的前后执行一段逻辑。
   *
   * @param pjp 连接点
   * @return 执行方法的返回值
   * @throws Throwable 抛出异常
   */
  @Around("controllerAspect()")
  public Object aroundLog(ProceedingJoinPoint pjp) throws Throwable {
    Method method = ((MethodSignature) pjp.getSignature()).getMethod();
    //获取注解中的信息
    WebLog annotation = method.getAnnotation(WebLog.class);
    boolean hasResponseBody = null != method.getAnnotation(ResponseBody.class);
    //获取业务类型
    LogEnum type = annotation.type();
    SysLogEntity logEntity = new SysLogEntity(type.getMessage(), annotation.value());
    logEntity.setServiceResult(hasResponseBody);
    logEntity.setStartTime(SystemClock.now());
    // 获取参数
    Object[] args = pjp.getArgs();
    if (annotation.desensitise()) {
      logEntity.setParameter(DESENSITISE_STR);
    } else {
      // 不做数据脱敏时，才记录输入参数
      logEntity.setParameter(convertParameterToJson(args));
    }
    try {
      Object result = pjp.proceed();
      if (type == LogEnum.LOGIN) {
        // 单独处理登录，因为此时session中还没有用户信息
        String userAccount = getUserAccount(args);
        if (StringUtil.isNotBlank(userAccount)) {
          try {
            logEntity.setUserId(sysUserService.selectByUserAccount(userAccount).getId());
          } catch (Exception ex) {
            //得不到用户，就放弃
          }
        }
        if (result instanceof String && StringUtil.equals(FrameConstant.REDIRECT_INDEX,
            (String) result)) {
          logEntity.setResult("登录成功");
          logEntity.setStatus(FrameConstant.SUCCESS);
        } else {
          logEntity.setResult("登录失败");
          logEntity.setStatus(FrameConstant.FAILURE);
          // 当登录失败，记录下错误的密码备查
          logEntity.setParameter(convertParameterToJson(args));
        }
      } else {
        logEntity.setResult(getJson(result));
        logEntity.setStatus(FrameConstant.SUCCESS);
      }
      return result;
    } catch (Throwable th) {
      logEntity.setResult(th.getMessage());
      logEntity.setStatus(FrameConstant.FAILURE);
      // 继续抛出异常
      throw th;
    } finally {
      logEntity.setRunTime(SystemClock.now() - logEntity.getStartTime());
      LogUtil.saveLog(logEntity);
    }
  }
}