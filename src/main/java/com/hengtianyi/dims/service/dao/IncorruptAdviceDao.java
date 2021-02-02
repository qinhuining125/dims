package com.hengtianyi.dims.service.dao;

import com.hengtianyi.common.core.base.service.AbstractGenericDao;
import com.hengtianyi.dims.service.dto.QueryDto;
import com.hengtianyi.dims.service.entity.IncorruptAdviceEntity;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * IncorruptAdvice数据库读写DAO
 *
 * @author LY
 */
@Mapper
public interface IncorruptAdviceDao extends AbstractGenericDao<IncorruptAdviceEntity, String> {

  /**
   * 自定义分页
   *
   * @param dto dto
   * @return list
   */
  List<IncorruptAdviceEntity> pagelist(@Param("dto") QueryDto dto);

  /**
   * 图标所用数据
   *
   * @param startTime 开始时间
   * @param endTime   结束时间
   * @param areaCode  乡镇
   * @return list
   */
  List<IncorruptAdviceEntity> getEchartsData(@Param("startTime") String startTime,
      @Param("endTime") String endTime,
      @Param("areaCode") String areaCode);

  /**
   * 自定义分页数量
   *
   * @param dto dto
   * @return count
   */
  Integer pagecount(@Param("dto") QueryDto dto);
}
