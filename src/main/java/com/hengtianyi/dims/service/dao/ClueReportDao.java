package com.hengtianyi.dims.service.dao;

import com.hengtianyi.common.core.base.service.AbstractGenericDao;
import com.hengtianyi.dims.service.dto.QueryDto;
import com.hengtianyi.dims.service.entity.ClueReportEntity;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * ClueReport数据库读写DAO
 *
 * @author LY
 */
@Mapper
public interface ClueReportDao extends AbstractGenericDao<ClueReportEntity, String> {

  /**
   * 自定义分页
   *
   * @param dto dto
   * @return list
   */
  List<ClueReportEntity> pagelist(@Param("dto") QueryDto dto);

  /**
   * 自定义分页数量
   *
   * @param dto dto
   * @return count
   */
  Integer pagecount(@Param("dto") QueryDto dto);

  /**
   * 查询所有的上报
   *
   * @param startTime 开始时间
   * @param endTime   结束时间
   * @param areaCode  乡镇编号
   * @return list
   */
  List<ClueReportEntity> getEchartsData(@Param("startTime") String startTime,
      @Param("endTime") String endTime,
      @Param("areaCode") String areaCode);
}
