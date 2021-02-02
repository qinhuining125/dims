package com.hengtianyi.dims.service.dao;

import com.hengtianyi.common.core.base.CommonStringDto;
import com.hengtianyi.common.core.base.service.AbstractGenericDao;
import com.hengtianyi.dims.service.dto.QueryDto;
import com.hengtianyi.dims.service.entity.TaskImageEntity;
import com.hengtianyi.dims.service.entity.TaskInfoEntity;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * TaskInfo数据库读写DAO
 *
 * @author LY
 */
@Mapper
public interface TaskInfoDao extends AbstractGenericDao<TaskInfoEntity, String> {

  /**
   * 自定义分页
   *
   * @param dto dto
   * @return list
   */
  List<TaskInfoEntity> pagelist(@Param("dto") QueryDto dto);

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
  List<TaskInfoEntity> getEchartsData(@Param("startTime") String startTime,
      @Param("endTime") String endTime,
      @Param("areaCode") String areaCode);


}
