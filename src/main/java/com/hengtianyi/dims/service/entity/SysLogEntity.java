package com.hengtianyi.dims.service.entity;

import com.hengtianyi.common.core.base.BaseEntity;
import com.hengtianyi.common.core.util.IpUtil;
import com.hengtianyi.common.core.util.UserAgentUtil;
import com.hengtianyi.dims.constant.FrameConstant;
import com.hengtianyi.dims.utils.WebUtil;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 业务日志实体类
 * <p>Table: sys_log</p>
 *
 * @author BBF
 */
public class SysLogEntity extends BaseEntity {

  private static final long serialVersionUID = -4949259512134038724L;
  private Long id;

  /**
   * 成功标志，1成功，0失败
   */
  private Integer status;

  /**
   * 执行时间
   */
  private Long startTime;

  /**
   * 运行耗时毫秒
   */
  private Long runTime;

  /**
   * 业务类型
   */
  private String type;

  /**
   * 业务描述
   */
  private String description;

  /**
   * url
   */
  private String uri;

  /**
   * 方法名
   */
  private String method;

  private String userAgent;

  private String ip;

  private String userId;

  /**
   * 搜索用的结束区间
   */
  private Long endTime;

  /**
   * 入参
   */
  private String parameter;

  /**
   * 返回
   */
  private String result;

  /**
   * 是否使用ServiceResult包装
   */
  private Boolean isServiceResult;

  /**
   * 构造函数
   */
  public SysLogEntity() {
    super();
  }

  /**
   * 构造函数
   *
   * @param type 业务操作类型
   * @param desc 描述
   */
  public SysLogEntity(String type, String desc) {
    HttpServletRequest request = null;
    try {
      ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder
          .getRequestAttributes();
      request = sra.getRequest();
    } catch (Exception ex) {
      // 获取request对象失败，就放弃
    }
    this.type = type;
    this.description = desc;
    // 耗时初始化为：0秒
    this.runTime = 0L;
    // 执行状态初始化为：成功
    this.status = FrameConstant.SUCCESS;
    this.isServiceResult = false;
    if (request != null) {
      this.ip = IpUtil.getIp(request);
      this.method = request.getMethod();
      this.uri = request.getRequestURI();
      this.userAgent = new UserAgentUtil(request).toString();
      try {
        // 有可能从session中取不到用户ID
        this.userId = WebUtil.getUserId(request);
      } catch (Exception ex) {
        // 得不到用户ID，就不设置
      }
    }
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  /**
   * 获取status属性(成功标志，1成功，0失败)
   *
   * @return 成功标志，1成功，0失败
   */
  public Integer getStatus() {
    return this.status;
  }

  /**
   * 设置status属性
   *
   * @param status 成功标志，1成功，0失败
   */
  public void setStatus(Integer status) {
    this.status = status;
  }

  /**
   * 获取startTime属性(执行时间)
   *
   * @return 执行时间
   */
  public Long getStartTime() {
    return this.startTime;
  }

  /**
   * 设置startTime属性
   *
   * @param startTime 执行时间
   */
  public void setStartTime(Long startTime) {
    this.startTime = startTime;
  }

  /**
   * 获取runTime属性(运行耗时秒)
   *
   * @return 运行耗时秒
   */
  public Long getRunTime() {
    return this.runTime;
  }

  /**
   * 设置runTime属性
   *
   * @param runTime 运行耗时秒
   */
  public void setRunTime(Long runTime) {
    this.runTime = runTime;
  }

  /**
   * 获取type属性(业务类型)
   *
   * @return 业务类型
   */
  public String getType() {
    return this.type;
  }

  /**
   * 设置type属性
   *
   * @param type 业务类型
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * 获取description属性(业务描述)
   *
   * @return 业务描述
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * 设置description属性
   *
   * @param description 业务描述
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * 获取uri属性(url)
   *
   * @return url
   */
  public String getUri() {
    return this.uri;
  }

  /**
   * 设置uri属性
   *
   * @param uri url
   */
  public void setUri(String uri) {
    this.uri = uri;
  }

  /**
   * 获取method属性(方法名)
   *
   * @return 方法名
   */
  public String getMethod() {
    return this.method;
  }

  /**
   * 设置method属性
   *
   * @param method 方法名
   */
  public void setMethod(String method) {
    this.method = method;
  }

  public String getUserAgent() {
    return this.userAgent;
  }

  public void setUserAgent(String userAgent) {
    this.userAgent = userAgent;
  }

  public String getIp() {
    return this.ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public String getUserId() {
    return this.userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public Long getEndTime() {
    return endTime;
  }

  public void setEndTime(Long endTime) {
    this.endTime = endTime;
  }

  public String getParameter() {
    return parameter;
  }

  public void setParameter(String parameter) {
    this.parameter = parameter;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public Boolean getServiceResult() {
    return isServiceResult;
  }

  public void setServiceResult(Boolean serviceResult) {
    isServiceResult = serviceResult;
  }

  @Override
  public String toString() {
    return "SysLogEntity{" +
        "id=" + id +
        ", status=" + status +
        ", startTime=" + startTime +
        ", runTime=" + runTime +
        ", type='" + type + '\'' +
        ", description='" + description + '\'' +
        ", uri='" + uri + '\'' +
        ", method='" + method + '\'' +
        ", userAgent='" + userAgent + '\'' +
        ", ip='" + ip + '\'' +
        ", userId='" + userId + '\'' +
        ", endTime=" + endTime +
        ", parameter='" + parameter + '\'' +
        ", result='" + result + '\'' +
        ", isServiceResult=" + isServiceResult +
        '}';
  }
}
