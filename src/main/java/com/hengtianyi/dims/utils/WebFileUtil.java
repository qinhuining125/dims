package com.hengtianyi.dims.utils;

import com.hengtianyi.common.core.base.CommonFileDto;
import com.hengtianyi.common.core.feature.BusinessException;
import com.hengtianyi.common.core.util.FileUtil;
import com.hengtianyi.common.core.util.HttpUtil;
import com.hengtianyi.common.core.util.StringUtil;
import com.hengtianyi.common.core.util.sequence.SystemClock;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * Web文件上传下载工具类
 * <p style="color:red">必须要form上配置属性enctype="multipart/form-data"</p>
 * <p>必须要在Spring MVC中配置multipartResolver。</p>
 * <p>允许上传的文件类型，需要在resources/conf/global.properties文件中，
 * 设置upload.imageAllow和upload.fileAllow两个配置节</p>
 *
 * @author BBF
 */
public final class WebFileUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(WebFileUtil.class);

  private static final String[] IMAGE_EXT_NAMES = {"gif", "jpg", "jpeg", "png"};
  private static final String[] ALLOW_EXT_NAMES = {
      "gif", "jpg", "jpeg", "png",
      "doc", "docx", "xls", "xlsx",
      "ppt", "pptx", "txt", "rtf",
      "pdf", "zip", "rar", "wps",
      "dps", "et"
  };
  /**
   * 文件存储目录
   */
  private final File fileParentDir;
  /**
   * 文件存储目录的二级目录，根据年月分组
   */
  private final String fileParentDir2;
  /**
   * uploadParam : 上传附加的多值表单字段
   */
  private Map<String, String[]> parameterMap = null;
  /**
   * uploadParam : 上传附加的单值表单字段
   */
  private Map<String, String> parameterSingleMap = null;
  /**
   * 成功列表
   */
  private List<CommonFileDto> successList = null;
  /**
   * 失败列表
   */
  private List<CommonFileDto> failList = null;

  /**
   * 构造函数
   *
   * @param request  HttpServletRequest对象
   * @param filePath 文件存储目录
   */
  public WebFileUtil(HttpServletRequest request, final String filePath) {
    this(request, new File(filePath));
  }

  /**
   * 构造函数
   *
   * @param request  HttpServletRequest对象
   * @param filePath 文件存储目录
   */
  public WebFileUtil(HttpServletRequest request, final File filePath) {
    if (!filePath.exists()) {
      if (!filePath.mkdirs()) {
        throw new BusinessException("文件存储目录出错");
      }
    }
    String yearMonth = DateFormatUtils.format(SystemClock.nowDate(), "yyyyMM");
    File dir = new File(filePath, yearMonth);
    if (!FileUtil.checkFolder(dir)) {
      throw new BusinessException("无法创建上传目录");
    }
    this.parameterMap = new HashMap<>(0);
    this.parameterSingleMap = new HashMap<>(0);
    this.successList = new ArrayList<>(0);
    this.failList = new ArrayList<>(0);
    this.fileParentDir = dir;
    this.fileParentDir2 = yearMonth;
    this.uploadFile(request);
  }

  /**
   * 判断是否图片
   *
   * @param fileName 文件名
   * @return true - 图片
   */
  public static Boolean isImage(String fileName) {
    String ext = FileUtil.getExtension(fileName).toLowerCase();
    return ArrayUtils.contains(IMAGE_EXT_NAMES, ext);
  }

  /**
   * 下载文件，推到response输出流
   * <p style="color:red">需要在调用前，将originalFileName转换为</p>
   *
   * @param response         HttpServletResponse
   * @param file             文件在服务器的路径
   * @param originalFileName 文件的原始名
   */
  public static void download(HttpServletRequest request, HttpServletResponse response,
      File file,
      String originalFileName) {
    if (!file.exists() || !file.isFile()) {
      return;
    }
    try (InputStream is = new FileInputStream(file)) {
      download(request, response, is, originalFileName);
    } catch (IOException ex) {
      LOGGER.error("[WebFileUtil.download]", ex);
    }
  }

  /**
   * 下载文件，推到response输出流
   * <p style="color:red">需要在调用前，将originalFileName转换为</p>
   *
   * @param response         HttpServletResponse
   * @param fileStream       文件流，不关闭
   * @param originalFileName 文件的原始名
   */
  public static void download(HttpServletRequest request, HttpServletResponse response,
      InputStream fileStream,
      String originalFileName) {
    try (InputStream bis = new BufferedInputStream(fileStream)) {
      // 清空response
      response.reset();
      // 设置response的Header
      HttpUtil.setResponseNoCache(response);
      response.addHeader(HttpHeaders.CONTENT_DISPOSITION,
          StringUtil.convertToDownload(originalFileName, request));
      response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
      response.setContentLength(fileStream.available());
      try (OutputStream out = new BufferedOutputStream(response.getOutputStream())) {
        // 循环取出流中的数据
        byte[] b = new byte[4096];
        int len;
        while ((len = bis.read(b)) > 0) {
          out.write(b, 0, len);
        }
        out.flush();
      }
    } catch (IOException ex) {
      LOGGER.error("[WebFileUtil.download]", ex);
    }
  }

  /**
   * 文件上传
   * <p style="color:red">必须要form上配置属性enctype="multipart/form-data"</p>
   * <p>文件是否成功上传，需要通过成功列表获取。</p>
   * 注：一些参数的获取。
   * <ul><li>使用<span style="color:red">getSuccessList()</span>获取成功列表</li>
   * <li>使用<span style="color:red">getFailList()</span>获取失败列表</li>
   * <li>使用<span style="color:red">getUploadParams()</span>获取上传附加的表单字段</li></ul>
   *
   * @param httpServletRequest request对象
   */
  private void uploadFile(HttpServletRequest httpServletRequest) {
    try {
      //转换request
      MultipartHttpServletRequest request = (MultipartHttpServletRequest) httpServletRequest;
      //从request中获取附加参数
      this.getParameterMapFromRequest(request);
      // 获取文件名集合
      Iterator<String> inputNames = request.getFileNames();
      // 上传控件的name属性，有可能相同name，对应多个file
      while (inputNames.hasNext()) {
        String inputName = inputNames.next();
        List<MultipartFile> fileList = request.getFiles(inputName);
        for (MultipartFile file : fileList) {
          CommonFileDto fileDto = this.saveFile(this.fileParentDir, file);
          if (fileDto.getSuccess()) {
            successList.add(fileDto);
          } else {
            failList.add(fileDto);
          }
        }
      }
    } catch (Exception e) {
      LOGGER.error("[WebFileUtil.uploadFile]上传文件出错：{}", e.getMessage(), e);
    }
  }

  /**
   * 保存文件
   *
   * @param dir  保存路径
   * @param file MultipartFile对象
   */
  private CommonFileDto saveFile(File dir, MultipartFile file) {
    String originalFileName = file.getOriginalFilename();
    CommonFileDto dto = new CommonFileDto(originalFileName);
    if (!ArrayUtils.contains(ALLOW_EXT_NAMES, dto.getFileExtName())) {
      dto.setError("该文件是不允许上传的类型");
      return dto;
    }
    try (InputStream is = file.getInputStream()) {
      File saveFile = new File(dir, dto.getFileNewName());
      FileUtil.write(saveFile, is);
      if (!saveFile.exists()) {
        dto.setError("保存文件失败");
        return dto;
      }
      String fileMd5 = FileUtil.getFileMD5(saveFile);
      String fileName = saveFile.getName();
      // 获取相对于上传目录的路径
      String filePath = File.separatorChar + fileParentDir2 + File.separatorChar + fileName;
      long fileSize = FileUtil.sizeOf(saveFile);
      dto.setFileMd5(fileMd5);
      dto.setFileSize(fileSize);
      dto.setStorageType("本地存储");
      dto.setStoragePath(fileName);
      dto.setVirtualPath(filePath);
      dto.setSuccess(true);
      return dto;
    } catch (Exception e) {
      LOGGER.error("[WebFileUtil.saveFile]", e);
      dto.setError("保存文件失败");
      return dto;
    }
  }

  /**
   * 从request中获取附加参数
   *
   * @param request MultipartHttpServletRequest对象
   */
  private void getParameterMapFromRequest(MultipartHttpServletRequest request) {
    this.parameterMap = request.getParameterMap();
    //将多值表单Map集合转换为单值集合
    this.parameterSingleMap = new HashMap<>(0);
    Iterator<Map.Entry<String, String[]>> it = this.parameterMap.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<String, String[]> entry = it.next();
      String[] value = entry.getValue();
      if (ArrayUtils.isEmpty(value)) {
        continue;
      }
      String key = entry.getKey();
      this.parameterSingleMap.put(key, value[0]);
    }
  }

  /**
   * 获取上传附加的多值表单字段
   *
   * @return 上传附加的表单字段
   */
  public Map<String, String[]> getParameterMap() {
    return this.parameterMap;
  }

  /**
   * 获取上传附加的单值表单字段
   * <p>如果有相同name，取第一个值</p>
   *
   * @return 上传附加的表单字段
   */
  public Map<String, String> getParameterSingleMap() {
    return this.parameterSingleMap;
  }

  /**
   * 获取上传成功的文件DTO集合
   *
   * @return 上传成功的文件DTO集合
   */
  public List<CommonFileDto> getSuccessList() {
    return this.successList;
  }

  /**
   * 获取上传失败的文件DTO集合
   *
   * @return 上传失败的文件DTO集合
   */
  public List<CommonFileDto> getFailList() {
    return this.failList;
  }

}