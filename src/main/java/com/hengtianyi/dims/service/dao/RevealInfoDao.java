package com.hengtianyi.dims.service.dao;

import com.hengtianyi.common.core.base.service.AbstractGenericDao;
import com.hengtianyi.dims.service.dto.QueryDto;
import com.hengtianyi.dims.service.entity.RevealInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * RevealInfo数据库读写DAO
 *
 * @author
 */
@Mapper
public interface RevealInfoDao extends AbstractGenericDao<RevealInfoEntity, String> {

  /**
   * 自定义分页
   *
   * @param dto dto
   * @return list
   */
  List<RevealInfoEntity> pagelist(@Param("dto") QueryDto dto);

  /**
   * 自定义分页数量
   *
   * @param dto dto
   * @return count
   */
  Integer pagecount(@Param("dto") QueryDto dto);
  /**
   * 图标所用数据
   *
   * @param startTime 开始时间
   * @param endTime   结束时间
   * @param areaCode  乡镇
   * @return list
   */
  List<RevealInfoEntity> getEchartsData(@Param("startTime") String startTime,
      @Param("endTime") String endTime,
      @Param("areaCode") String areaCode);
}
