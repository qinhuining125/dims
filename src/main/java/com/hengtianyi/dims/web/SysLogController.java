package com.hengtianyi.dims.web;

import com.hengtianyi.common.core.annotation.Permissions;
import com.hengtianyi.common.core.base.CommonPageDto;
import com.hengtianyi.common.core.base.service.AbstractBaseController;
import com.hengtianyi.common.core.constant.BaseConstant;
import com.hengtianyi.common.core.feature.ServiceResult;
import com.hengtianyi.common.core.util.HttpUtil;
import com.hengtianyi.common.core.util.StringUtil;
import com.hengtianyi.dims.aop.WebLog;
import com.hengtianyi.dims.config.CustomProperties;
import com.hengtianyi.dims.constant.LogEnum;
import com.hengtianyi.dims.service.api.SysLogService;
import com.hengtianyi.dims.service.api.SysUserService;
import com.hengtianyi.dims.service.entity.SysLogEntity;
import com.hengtianyi.dims.service.entity.SysUserEntity;
import com.hengtianyi.dims.utils.WebUtil;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 业务日志管理控制器
 *
 * @author BBF
 */
@Controller
@RequestMapping(value = "/a/sysLog")
public class SysLogController extends AbstractBaseController<SysLogEntity, Long> {

  private static final Logger LOGGER = LoggerFactory.getLogger(SysLogController.class);

  @Resource
  private SysUserService sysUserService;

  @Resource
  private SysLogService sysLogService;

  @Resource
  private CustomProperties customProperties;

  @Override
  public SysLogService getService() {
    return sysLogService;
  }

  @Override
  public Logger getLogger() {
    return LOGGER;
  }

  /**
   * 日志首页[视图]
   *
   * @param model Model
   * @return 视图
   */
  @Permissions("sys:log:index")
  @GetMapping(value = "/index.html", produces = BaseConstant.HTML)
  public String index(Model model) {
    model.addAttribute("firstTime", sysLogService.getFirstTime());
    List<String> logTypeList = new ArrayList<>();
    for (LogEnum log : LogEnum.values()) {
      logTypeList.add(log.getMessage());
    }
    model.addAttribute("logType", logTypeList);
    return "web/sysLog/sysLog_index";
  }

  /**
   * 日志详情[视图]
   *
   * @param model Model
   * @param id    日志ID
   * @return 视图
   */
  @Permissions("sys:log:index")
  @GetMapping(value = "/info.html", produces = BaseConstant.HTML)
  public String info(Model model, @RequestParam Long id) {
    SysLogEntity logEntity = sysLogService.searchDataById(id);
    //获取入参
    String param = sysLogService.getParameter(id);
    //获取返回
    String result = sysLogService.getResult(id);
    //获取用户名
    SysUserEntity userEntity = null;
    String uid = logEntity.getUserId();
    if (StringUtil.isNoneBlank(uid)) {
      userEntity = sysUserService.searchDataById(uid);
    }
    model.addAttribute("logEntity", logEntity);
    model.addAttribute("param", param);
    model.addAttribute("result", result);
    model.addAttribute("userEntity", userEntity);
    return "web/sysLog/sysLog_info";
  }

  /**
   * 通过AJAX获取列表信息[JSON]
   *
   * @param pageDto 通用DTO
   * @param dto     查询DTO
   * @return JSON
   */
  @Permissions("sys:log:index")
  @ResponseBody
  @PostMapping(value = "/getDataList.json", produces = BaseConstant.JSON)
  public String getDataList(@ModelAttribute CommonPageDto pageDto,
      @ModelAttribute SysLogEntity dto) {
    Long endTime = dto.getEndTime();
    if (endTime != null && endTime > 0) {
      dto.setEndTime(endTime + BaseConstant.SSS_IN_DAY);
    }
    return this.getDataListCommon(pageDto, dto);
  }

  /**
   * 通过AJAX获取列表信息[JSON]
   *
   * @return JSON
   */
  @WebLog(value = "清空日志", type = LogEnum.REMOVE_LOG)
  @Permissions("sys:log:remove")
  @ResponseBody
  @PostMapping(value = "/removeAll.json", produces = BaseConstant.JSON)
  public String removeAll() {
    ServiceResult<Boolean> result = new ServiceResult<>();
    if (sysLogService.removeAll()) {
      result.setSuccess(true);
      result.setResult(true);
    } else {
      result.setError("清空日志失败");
    }
    return result.toJson();
  }

  /**
   * 下载错误日志
   *
   * @param response HttpServletResponse
   * @param model    Model
   * @return JSON
   * @throws IOException IO异常
   */
  @WebLog(value = "下载错误日志", type = LogEnum.COMMON)
  @Permissions("sys:log:index")
  @GetMapping(value = "/downloadLog.html", produces = BaseConstant.HTML)
  public String downloadDoc(HttpServletRequest request, HttpServletResponse response, Model model)
      throws IOException {
    File file = new File(customProperties.getErrorLogFile());
    if (file.exists() && file.isFile()) {
      // 清空response
      response.reset();
      // 设置response的Header
      HttpUtil.setResponseNoCache(response);
      response
          .addHeader(HttpHeaders.CONTENT_DISPOSITION,
              StringUtil.convertToDownload("log.zip", request));
      response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
      try (OutputStream out = new BufferedOutputStream(response.getOutputStream());
          ZipOutputStream zip = new ZipOutputStream(out);
          InputStream fos = new FileInputStream(file);
          InputStream bis = new BufferedInputStream(fos)) {
        zip.putNextEntry(new ZipEntry("error.log"));
        // 将模板内容写入到ZIP流
        byte[] b = new byte[4096];
        int len;
        while ((len = bis.read(b)) > 0) {
          zip.write(b, 0, len);
        }
        zip.closeEntry();
        out.flush();
      }
      return StringUtil.EMPTY;
    } else {
      return WebUtil.toErrorPage(model, "获取日志文件失败。");
    }
  }

  /**
   * 前端显示的数据
   *
   * @param entity 实体对象
   */
  @Override
  public void clearEntity(SysLogEntity entity) {
    if (null != entity) {
      // 18位long类型，js无法处理这么大的精度
      // 会自动转换，因此在这里转换为字符串，临时使用字段代替
      entity.setUserAgent(String.valueOf(entity.getId()));
      //获取用户名
      String uid = entity.getUserId();
      entity.setUserId(sysUserService.getNameById(uid));
    }
  }
}
