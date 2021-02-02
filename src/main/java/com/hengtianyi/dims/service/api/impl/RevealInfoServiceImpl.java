package com.hengtianyi.dims.service.api.impl;

import com.hengtianyi.common.core.base.CommonEntityDto;
import com.hengtianyi.common.core.base.CommonPageDto;
import com.hengtianyi.common.core.base.service.AbstractGenericServiceImpl;
import com.hengtianyi.common.core.feature.ServiceResult;
import com.hengtianyi.common.core.util.CollectionUtil;
import com.hengtianyi.common.core.util.sequence.IdGenUtil;
import com.hengtianyi.common.core.util.sequence.SystemClock;
import com.hengtianyi.dims.constant.FrameConstant;
import com.hengtianyi.dims.constant.RoleEnum;
import com.hengtianyi.dims.service.api.RevealInfoService;
import com.hengtianyi.dims.service.api.SysUserService;
import com.hengtianyi.dims.service.api.TownshipService;
import com.hengtianyi.dims.service.dao.ImageRevealDao;
import com.hengtianyi.dims.service.dao.RevealFlowDao;
import com.hengtianyi.dims.service.dao.RevealInfoDao;
import com.hengtianyi.dims.service.dao.VillageDao;
import com.hengtianyi.dims.service.dto.QueryDto;
import com.hengtianyi.dims.service.dto.RevealInfoDto;
import com.hengtianyi.dims.service.entity.*;
import com.hengtianyi.dims.utils.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * RevealInfo接口的实现类
 *
 * @author
 */
@Service
public class RevealInfoServiceImpl extends AbstractGenericServiceImpl<RevealInfoEntity, String>
        implements RevealInfoService {


  private static final Logger LOGGER = LoggerFactory.getLogger(RevealInfoServiceImpl.class);


  private static final String TEST_PICTURE = "/static/upload/1.jpg";

  private static final String TEST_PATROL_ID = "955de15531584c1c8db0214520a0b830";
  //固定服务的ip
//  private static final String REQUEST_URL = "http://192.168.32.4:8030/";

//  private static final String REQUEST_URL = "http://183.201.252.83:49012/";

  @Resource
  private RevealInfoDao revealInfoDao;

  @Resource
  private ImageRevealDao imageRevealDao;

  @Resource
  private RevealFlowDao revealFlowDao;
  @Resource
  private TownshipService townshipService;

  @Resource
  private SysUserService sysUserService;

  @Resource
  private VillageDao villageDao;

  /**
   * 实体与数据表字段的映射
   * <p>格式：实体字段 - 数据库字段</p>
   */
  private static final Map<String, String> PROPERTY_COLUMN;

  /**
   * 默认排序的sql，order by后面的部分，例如: id asc,name desc
   */
  private static final String DEFAULT_ORDER_SQL = "id asc";

  static {
    PROPERTY_COLUMN = new HashMap<>(10);
    // 格式：实体字段名 - 数据库字段名
    PROPERTY_COLUMN.put("id", "ID");
    PROPERTY_COLUMN.put("userId", "USER_ID");
    PROPERTY_COLUMN.put("roleId", "ROLE_ID");
    PROPERTY_COLUMN.put("contact", "CONTACT");
    PROPERTY_COLUMN.put("revealTarget", "REVEAL_TARGET");
    PROPERTY_COLUMN.put("content", "CONTENT");
    PROPERTY_COLUMN.put("userName", "USER_NAME");
    PROPERTY_COLUMN.put("type", "TYPE");
    PROPERTY_COLUMN.put("createTime", "CREATE_TIME");
    PROPERTY_COLUMN.put("state", "STATE");
  }

  /**
   * 注入Mybatis的Dao
   *
   * @return revealInfoDao
   */
  @Override
  public RevealInfoDao getDao() {
    return revealInfoDao;
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
   * @param dto 举报表单信息
   * @return i
   */
  @Override
  public Integer saveData(RevealInfoDto dto, HttpServletRequest request) {
    try {
      int ct = 0;
      SysUserEntity userEntity = WebUtil.getUser(request);
      String revealId = IdGenUtil.uuid32();
      RevealInfoEntity revealInfoEntity = new RevealInfoEntity();
      revealInfoEntity.setId(revealId);
      revealInfoEntity.setUserId(userEntity.getId());
      revealInfoEntity.setRoleId(userEntity.getRoleId() == null ? RoleEnum.MASSES.getRoleId() : userEntity.getRoleId());//如果有值就设置为用户roleId，如果没有就设置为群众的roleId
      revealInfoEntity.setContact(dto.getContact());
      revealInfoEntity.setContent(dto.getContent());
      revealInfoEntity.setUserName(dto.getUserName());
      revealInfoEntity.setRevealTarget(dto.getRevealTarget());
      revealInfoEntity.setType(dto.getType());
      revealInfoEntity.setCreateTime(SystemClock.nowDate());
      revealInfoEntity.setState(Short.valueOf("0"));//0未处理，1已分配，2已知晓
      revealInfoEntity.setInspectionId(TEST_PATROL_ID);//后期需替换成App或者H5扫描后的传过来的id
      revealInfoEntity.setTownCode(dto.getTownCode());
      revealInfoEntity.setVillageCode(dto.getVillageCode());
      revealInfoDao.insert(revealInfoEntity);

      //上传图片
      ImageRevealEntity imageRevealEntity = new ImageRevealEntity();
      imageRevealEntity.setId(IdGenUtil.uuid32());
      imageRevealEntity.setRevealId(revealId);
      imageRevealEntity.setImageUrl(TEST_PICTURE);
      imageRevealEntity.setCreateTime(SystemClock.nowDate());
      imageRevealDao.insert(imageRevealEntity);

      //流程
      RevealFlowEntity flowEntity = new RevealFlowEntity();
      flowEntity.setId(IdGenUtil.uuid32());
      flowEntity.setRevealId(revealId);
      List<SysUserEntity> user = sysUserService.getUserIdByRoleId(RoleEnum.XCBADMIN.getRoleId());//巡察办主任
      flowEntity.setReceiveId(user.get(0).getId());//巡察办主任用户Id
      flowEntity.setReceiveRoleId(user.get(0).getRoleId());//巡察办主任角色
      flowEntity.setState(0);//0未接受，1已受理，2已知晓
      flowEntity.setCreateTime(SystemClock.nowDate());
      ct = revealFlowDao.insert(flowEntity);
      return ct;
    } catch (Exception e) {
      LOGGER.error("[举报内容出错],{}", e.getMessage(), e);
    }
    return -1;
  }

  /**
   * 自定义分页
   *
   * @param dto dto
   * @return
   */
  @Override
  public CommonEntityDto<RevealInfoEntity> pagelist(QueryDto dto) {
    Integer count = revealInfoDao.pagecount(dto);
    List<RevealInfoEntity> list = revealInfoDao.pagelist(dto);
    //将flow放置进来
    for (RevealInfoEntity revealInfoEntity : list) {
      List<RevealFlowEntity> revealFlowEntity=revealFlowDao.getAllFlows(revealInfoEntity.getId());
      revealInfoEntity.setFlows(revealFlowEntity);
      List<ImageRevealEntity> getAllImages=imageRevealDao.getAllImages(revealInfoEntity.getId());
      List<String> imgs=new ArrayList<String>();
      for(ImageRevealEntity imageRevealEntity : getAllImages ){
        imgs.add(FrameConstant.PREFIX_URL+imageRevealEntity.getImageUrl());
      }
      revealInfoEntity.setImgApp(imgs);
    }

    List<RevealInfoEntity> listDataNew = new ArrayList<RevealInfoEntity>();
    int roleId=dto.getRoleId();
    if(roleId==1009||roleId==1010||roleId==1011){//如果是巡察组，然后去分表中将各个信息的查询出然后更改下各个状态
      for(int i=0;i<list.size();i++){
        RevealInfoEntity revealInfoEntity=list.get(i);
        RevealFlowEntity flowEntity=new RevealFlowEntity();
        flowEntity.setReceiveRoleId(roleId);
        flowEntity.setRevealId(revealInfoEntity.getId());
        flowEntity.setReceiveId(dto.getUserId());
        flowEntity.setState(2);//查询出是否有2的，如果有2的话，则更改状态，如果没有的话，则保持不变
        List<RevealFlowEntity> fl=revealFlowDao.searchAllData(flowEntity);
        if(CollectionUtil.isNotEmpty(fl)){
          revealInfoEntity.setState(Short.valueOf("2"));
        }
        listDataNew.add(revealInfoEntity);
      }
    }else{
      listDataNew=list;
    }
    CommonEntityDto<RevealInfoEntity> result = new CommonEntityDto<>(listDataNew);
    result.setCurrentPage(dto.getCurrentPage());
    result.setSize(FrameConstant.PAGE_SIZE);
    result.setTotal(count);
    return result;
  }


  /**
   * 原分页
   *
   * @param dto
   * @return
   */
  @Override
  public CommonPageDto listData(QueryDto dto) {
    try {
      int rowsCount = revealInfoDao.pagecount(dto);
      List<RevealInfoEntity> listData = revealInfoDao.pagelist(dto);
      CommonPageDto cpDto = new CommonPageDto(listData);
      cpDto.setCurrent(dto.getCurrentPage());
      cpDto.setRowCount(FrameConstant.PAGE_SIZE);
      cpDto.setTotal(rowsCount);
      return cpDto;
    } catch (Exception ex) {
      LOGGER.error("[listData]{}, dto = {}", ex.getMessage(), dto.toJson(), ex);
    }
    return new CommonPageDto();
  }

  /**
   * echart数据
   *
   * @param startTime 开始时间
   * @param endTime   结束时间
   * @param areaCode  乡镇编号
   * @return json
   */
  @Override
  public String echartsData(String startTime, String endTime, String areaCode) {
    ServiceResult<Object> result = new ServiceResult();
    try {
      List<RevealInfoEntity> adviceEntityList = revealInfoDao
              .getEchartsData(startTime, endTime, areaCode);
      Map<String, Object> map = count(adviceEntityList);
      List<VillageEntity> villageList = villageDao.areaList(areaCode);
      String[] villageNames = new String[villageList.size()];
      String[] wanggeyuans = new String[villageList.size()];
      String[] lianluoyuans = new String[villageList.size()];
      String[] xiangzhens = new String[villageList.size()];
      for (int i = 0; i < villageList.size(); i++) {
        VillageEntity village = villageList.get(i);
        adviceEntityList = revealInfoDao
                .getEchartsData(startTime, endTime, village.getAreaCode());
        Map<String, Object> dataMap = count(adviceEntityList);
        villageNames[i] = village.getAreaName();
        wanggeyuans[i] = dataMap.get("wanggeyuan").toString();
        lianluoyuans[i] = dataMap.get("lianluoyuan").toString();
        xiangzhens[i] = dataMap.get("xiangzhen").toString();
      }
      map.put("villageNames", villageNames);
      map.put("wanggeyuans", wanggeyuans);
      map.put("lianluoyuans", lianluoyuans);
      map.put("xiangzhens", xiangzhens);
      result.setSuccess(true);
      result.setResult(map);
    } catch (Exception e) {
      LOGGER.error("[echartsData]出错,{}", e.getMessage(), e);
      result.setError("false");
    }
    return result.toJson();
  }

  private Map<String, Object> count(List<RevealInfoEntity> list) {
    Map<String, Object> map = new HashMap<>();
    Integer wanggeyuan = 0;
    Integer lianluoyuan = 0;
    Integer xiangzhen = 0;
    for (RevealInfoEntity entity : list) {
      if (entity.getRoleId() == 1001) {
        wanggeyuan += 1;
      } else if (entity.getRoleId() == 1002) {
        lianluoyuan += 1;
      } else if (entity.getRoleId() == 2000) {
        xiangzhen += 1;
      }
    }
    map.put("wanggeyuan", wanggeyuan);
    map.put("lianluoyuan", lianluoyuan);
    map.put("xiangzhen", xiangzhen);
    return map;
  }

  /**
   * 查看数量
   *
   * @param dto dto
   * @return
   */
  @Override
  public Integer countReveal(QueryDto dto) {
    return revealInfoDao.pagecount(dto);
  }

  @Override
  public String getDataList(QueryDto dto) {
    try {
      int rowsCount = revealInfoDao.pagecount(dto);
      List<RevealInfoEntity> listData = revealInfoDao.pagelist(dto);
      //根据角色来判定
      List<RevealInfoEntity> listDataNew = new ArrayList<RevealInfoEntity>();
      int roleId=dto.getRoleId();
      if(roleId==1009||roleId==1010||roleId==1011){//如果是巡察组，然后去分表中将各个信息的查询出然后更改下各个状态
        for(int i=0;i<listData.size();i++){
          RevealInfoEntity revealInfoEntity=listData.get(i);
          RevealFlowEntity flowEntity=new RevealFlowEntity();
          flowEntity.setReceiveRoleId(roleId);
          flowEntity.setRevealId(revealInfoEntity.getId());
          flowEntity.setReceiveId(dto.getUserId());
          flowEntity.setState(2);//查询出是否有2的，如果有2的话，则更改状态，如果没有的话，则保持不变
          List<RevealFlowEntity> fl=revealFlowDao.searchAllData(flowEntity);
          if(CollectionUtil.isNotEmpty(fl)){
            revealInfoEntity.setState(Short.valueOf("2"));
          }
          listDataNew.add(revealInfoEntity);
        }
      }else{
        listDataNew=listData;
      }

      CommonPageDto cpDto = new CommonPageDto(listDataNew);
      cpDto.setCurrent(dto.getCurrentPage());
      cpDto.setRowCount(FrameConstant.PAGE_SIZE);
      cpDto.setTotal(rowsCount);
      return cpDto.toJsonHtml();
    } catch (Exception ex) {
      LOGGER.error("[listData]{}, dto = {}", ex.getMessage(), dto.toJson(), ex);
    }
    return new CommonPageDto().toJsonHtml();
  }

}
