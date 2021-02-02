package com.hengtianyi.dims.service.api.impl;

import com.hengtianyi.common.core.base.service.AbstractGenericServiceImpl;
import com.hengtianyi.common.core.util.StringUtil;
import com.hengtianyi.dims.service.api.RelUserAreaService;
import com.hengtianyi.dims.service.dao.RelUserAreaDao;
import com.hengtianyi.dims.service.dao.SysUserDao;
import com.hengtianyi.dims.service.dto.UserAreaDto;
import com.hengtianyi.dims.service.entity.RelUserAreaEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * RelUserArea接口的实现类
 *
 * @author LY
 */
@Service
public class RelUserAreaServiceImpl extends AbstractGenericServiceImpl<RelUserAreaEntity, String>
    implements RelUserAreaService {

  private static final Logger LOGGER = LoggerFactory.getLogger(RelUserAreaServiceImpl.class);

  @Resource
  private RelUserAreaDao relUserAreaDao;
  @Resource
  private SysUserDao sysUserDao;
  /**
   * 实体与数据表字段的映射
   * <p>格式：实体字段 - 数据库字段</p>
   */
  private static final Map<String, String> PROPERTY_COLUMN;

  /**
   * 默认排序的sql，order by后面的部分，例如: id asc,name desc
   */
  private static final String DEFAULT_ORDER_SQL = "AREA_CODE asc";

  static {
    PROPERTY_COLUMN = new HashMap<>(2);
    // 格式：实体字段名 - 数据库字段名
    PROPERTY_COLUMN.put("userId", "USER_ID");
    PROPERTY_COLUMN.put("areaCode", "AREA_CODE");
  }

  /**
   * 注入Mybatis的Dao
   *
   * @return relUserAreaDao
   */
  @Override
  public RelUserAreaDao getDao() {
    return relUserAreaDao;
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
   * 根据用户查询对应的地区
   *
   * @param userId 用户id
   * @return list
   */
  @Override
  public List<RelUserAreaEntity> getUserArealist(String userId) {
    return relUserAreaDao.getUserArealist(userId);
  }

  /**
   * 删除用户对应的地区
   *
   * @param userId uid
   * @return
   */
  @Override
  public int deleteUserArea(String userId) {
    return relUserAreaDao.deleteUserArea(userId);
  }

  /**
   * 是否是其联系室管理的用户
   *
   * @param reportUserId 上报用户id
   * @param userId       当前用户id
   * @return true是，false否
   */
  @Override
  public Boolean contactAdmin(String reportUserId, String userId) {
    return StringUtil.isNotEmpty(relUserAreaDao.contactAdmin(reportUserId, userId));
  }

  /**
   * 所有子用户
   *
   * @param userId uid
   * @return list
   */
  @Override
  public List<UserAreaDto> childUsers(String userId) {
    List<RelUserAreaEntity> areaList = relUserAreaDao.getUserArealist(userId);
    List<UserAreaDto> userAreaDtoList = new ArrayList<>();
    UserAreaDto userAreaDto = null;
    for (RelUserAreaEntity areaEntity : areaList) {
      userAreaDto = new UserAreaDto();
      userAreaDto.setUserId(areaEntity.getUserId());
      userAreaDto.setAreaCode(areaEntity.getAreaCode());
      userAreaDto.setAreaName(areaEntity.getAreaName());
      userAreaDto.setRoleId(1003);
      userAreaDto.setChilds(sysUserDao.childTaskUser(areaEntity.getAreaCode()));
      userAreaDtoList.add(userAreaDto);
    }
    return userAreaDtoList;
  }
}
