package com.hengtianyi.dims.service.api.impl;

import com.hengtianyi.common.core.base.service.AbstractGenericServiceImpl;
import com.hengtianyi.common.core.util.sequence.IdGenUtil;
import com.hengtianyi.common.core.util.sequence.SystemClock;
import com.hengtianyi.dims.config.CustomProperties;
import com.hengtianyi.dims.constant.FrameConstant;
import com.hengtianyi.dims.service.api.PatrolInfoService;
import com.hengtianyi.dims.service.dao.PatrolInfoDao;
import com.hengtianyi.dims.service.entity.PatrolInfoEntity;
import com.hengtianyi.dims.service.entity.SysUserEntity;
import com.hengtianyi.dims.utils.BarcodeUtils;
import com.hengtianyi.dims.utils.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * PatrolInfo接口的实现类
 *
 * @author jyy
 */
@Service
public class PatrolInfoServiceImpl extends AbstractGenericServiceImpl<PatrolInfoEntity, String>
        implements PatrolInfoService {

  private static final Logger LOGGER = LoggerFactory.getLogger(PatrolInfoServiceImpl.class);

  @Resource
  private PatrolInfoDao patrolInfoDao;

  @Resource
  private CustomProperties customProperties;

  /**
   * 实体与数据表字段的映射
   * <p>格式：实体字段 - 数据库字段</p>
   */
  private static final Map<String, String> PROPERTY_COLUMN;

  /**
   * 默认排序的sql，order by后面的部分，例如: id asc,name desc
   */
  private static final String DEFAULT_ORDER_SQL = "id asc";

  /**
   * 二维码存储路径
   */
//  private static final String EWM_IMAGE_URL = "/src/main/resources/upload/ewm/";



//  private static final String EWM_IMAGE_URL = "/src/main/resources/upload/ewm/";
  /**
   * 二维码显示路径
   */
//  private static final String EWM_IMAGE_URL_SHOW = "upload/ewm/";
  private static final String EWM_IMAGE_URL_SHOW = "upload/";
//  private static final String EWM_IMAGE_URL_SHOW = "upload/ewm/";
  /**
   * 二维码扫描出来的路径地址http://localhost:81/reporting/report.html
   */
//  private static final String EWM_SHOW_COTENT="http://192.168.32.4:8030/reporting/report.html";

//  private static final String EWM_SHOW_COTENT="http://192.168.32.240:81/reporting/report.html";


  private static final String EWM_SHOW_COTENT= FrameConstant.PREFIX_URL + "reporting/report.html";

  static {
    PROPERTY_COLUMN = new HashMap<>(5);
    // 格式：实体字段名 - 数据库字段名
    PROPERTY_COLUMN.put("id", "ID");
    PROPERTY_COLUMN.put("userId", "USER_ID");
    PROPERTY_COLUMN.put("patrolName", "PATROL_NAME");
    PROPERTY_COLUMN.put("startTime", "START_TIME");
    PROPERTY_COLUMN.put("endTime", "END_TIME");
    PROPERTY_COLUMN.put("patrolUnit", "PATROL_UNIT");
    PROPERTY_COLUMN.put("imageUrl", "IMAGE_URL");
    PROPERTY_COLUMN.put("createTime", "CREATE_TIME");
  }

  /**
   * 注入Mybatis的Dao
   *
   * @return taskInfoDao
   */
  @Override
  public PatrolInfoDao getDao() {
    return patrolInfoDao;
  }

  /**
   * 注入实体与数据库字段的映射
   *
   * @return key：实体属性名，value：数据库字段名
   */
  @Override
  public Map<String, String> getPropertyColumn() {
    return PROPERTY_COLUMN;
  }

  /**
   * 注入排序集合
   *
   * @return 排序集合
   */
  @Override
  public String getDefaultSort() {
    return DEFAULT_ORDER_SQL;
  }

  /**
   * 保存
   *
   * @param dto 巡察信息
   * @return i
   */
  @Override
  public Integer saveData(PatrolInfoEntity dto, HttpServletRequest request) {
    try {
      int ct = 0;
      SysUserEntity userEntity = WebUtil.getUser(request);
      dto.setId(IdGenUtil.uuid32());
      dto.setUserId(userEntity.getId());
      dto.setCreateTime(SystemClock.nowDate());
      ct = patrolInfoDao.insert(dto);
      return ct;
    } catch (Exception e) {
      LOGGER.error("[任务指派出错],{}", e.getMessage(), e);
    }
    return -1;
  }

  @Override
  public String filePath(HttpServletRequest request,String words,String id) {
    String path=null;
    String urlPath=null;
    try {
      String info = EWM_SHOW_COTENT+"?id="+id;
//      File directory = new File(new File("").getCanonicalPath()); //获取项目路径
      //文件上传保存路径
      String FileName=String.valueOf(System.currentTimeMillis()) + ".png";
      String EWM_IMAGE_URL = customProperties.getUploadPath();
      path=EWM_IMAGE_URL + "/" + FileName;
      File qrCodeFile = new File(path); //为图片文件夹下的图片存放文件夹目录
      if (!qrCodeFile.exists()){
        qrCodeFile.mkdirs();
      }
      Boolean flag= BarcodeUtils.drawLogoQRCodeToFile(qrCodeFile,info,words);
      if(flag){
        urlPath="/"+ EWM_IMAGE_URL_SHOW+FileName;
      }
    } catch (Exception e) {
      System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
      e.printStackTrace();
    }
     return urlPath;
  }

  @Override
  public Boolean checkPatrolName(String patrolName) {
    PatrolInfoEntity dto=new PatrolInfoEntity();
    dto.setPatrolName(patrolName);
    List<PatrolInfoEntity> list=patrolInfoDao.searchAllData(dto);
    if(null==list||list.size()==0){
      return true;
    }
    return false;
  }

  @Override
  public Boolean checkPatrolTime(String startTime, String endTime) {
    try{
      PatrolInfoEntity dto=new PatrolInfoEntity();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      dto.setStartTime1(startTime);
      dto.setEndTime1(endTime);
      List<PatrolInfoEntity> plist=patrolInfoDao.searchAllData(dto);
      if(null==plist||plist.size()==0){
        return true;
      }
    }catch(Exception  e){
      LOGGER.error("[时间段验证出错],{}", e.getMessage(), e);
    }
    return false;
  }

  //api接口调用，用于网格员直接进行调用
  @Override
  public PatrolInfoEntity isHaveProcess() {
    PatrolInfoEntity ct=new PatrolInfoEntity();
    try{
      Date dd=new Date();
      SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd");
      String time=sim.format(dd);
      PatrolInfoEntity dto=new PatrolInfoEntity();
      dto.setStartTime1(time);
      dto.setEndTime1(time);
      List<PatrolInfoEntity> plist=patrolInfoDao.searchAllData(dto);
      if(null==plist||plist.size()==0){
        return ct;
      }else{
        return plist.get(0);
      }
    }catch(Exception  e){
      LOGGER.error("[判定该时间段是否有巡察出错],{}", e.getMessage(), e);
    }
    return ct;
  }

  @Override
  public List<PatrolInfoEntity> getImageUrl() {
    //判定二维码的失效时间，只要二维码的结束时间小于当前的时间段，则认为这个二维码是无效的。取反面，就是二维码的结束时间大于当前时间段。
    List<PatrolInfoEntity> plist=new ArrayList<PatrolInfoEntity>();
    try{
      Date dd=new Date();
      SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd");
      String time=sim.format(dd);
      PatrolInfoEntity dto=new PatrolInfoEntity();
      dto.setStartTime1(time);
      plist=patrolInfoDao.searchAllData(dto);
    }catch(Exception  e){
      LOGGER.error("[判定二维码的列表失效有问题],{}", e.getMessage(), e);
    }
    return plist;
  }

}
