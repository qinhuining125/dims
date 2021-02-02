package com.hengtianyi.dims.config;

import com.hengtianyi.common.core.base.BaseBean;
import com.hengtianyi.common.core.constant.BaseConstant;
import com.hengtianyi.common.core.util.StringUtil;
import com.hengtianyi.dims.constant.FrameConstant;
import com.hengtianyi.dims.utils.WebUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 全局配置类
 *
 * @author BBF
 */
@ConfigurationProperties(prefix = "bbf")
public class CustomProperties extends BaseBean {

  private static final long serialVersionUID = -3823804773124959970L;

  private final String preUrl ;

  private final String preUrl2 ;

  /**
   * 在表单上区分add和edit的标记
   */
  private final String addSign;

  /**
   * JS和CSSd版本号，防止缓存
   */
  private final String jsVersion;

  /**
   * 上传文件的路径
   */
  private String uploadPath;

  /**
   * 跨域domain
   */
  private String domain;

  /**
   * 静态资源路径前缀
   */
  private String staticPath;

  /**
   * 密码最大错误次数
   */
  private Integer passwordErrorTimes;

  /**
   * 项目的版本，用于swagger
   */
  private String version;

  /**
   * 是否报警提醒
   */
  private String alarm;
  /**
   * 错误日志保存路径
   */
  private String errorLogFile;

  public CustomProperties() {
    addSign = BaseConstant.PAGE_ADD_SIGN;
    //每次启动的时候，更新js版本号
    jsVersion = StringUtil.getRandomAlphanumeric(10);

    preUrl = FrameConstant.PREFIX_URL;
    preUrl2 = FrameConstant.PREFIX_URL2;

  }

  public String getJsVersion() {
    return jsVersion;
  }

  public String getUploadPath() {
    return uploadPath;
  }

  public void setUploadPath(String uploadPath) {
    this.uploadPath = uploadPath;
  }

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public boolean hasSepcialDomain() {
    return StringUtil.isNoneBlank(domain);
  }

  public String getStaticPath() {
    return staticPath;
  }

  public void setStaticPath(String staticPath) {
    this.staticPath = staticPath;
  }

  public Integer getPasswordErrorTimes() {
    return passwordErrorTimes;
  }

  public void setPasswordErrorTimes(Integer passwordErrorTimes) {
    this.passwordErrorTimes = passwordErrorTimes;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getErrorLogFile() {
    return errorLogFile;
  }

  public void setErrorLogFile(String errorLogFile) {
    this.errorLogFile = errorLogFile;
  }

  /**
   * 判断当前用户是否具备指定权限
   *
   * @param authCode 权限代码
   * @return true - 有权限
   */
  public boolean hasPermissions(String... authCode) {
    return WebUtil.hasPermission(authCode);
  }

  public boolean hasAlarm() {
    return StringUtil.equals(alarm, "true");
  }

  /**
   * 获取在表单上区分add和edit的标记
   *
   * @return 在表单上区分add和edit的标记
   */
  public String getAddSign() {
    return addSign;
  }

  public String isAlarm() {
    return alarm;
  }

  public void setAlarm(String alarm) {
    this.alarm = alarm;
  }

  public String getPreUrl() {
    return preUrl;
  }


  public String getPreUrl2() {
    return preUrl2;
  }
}
