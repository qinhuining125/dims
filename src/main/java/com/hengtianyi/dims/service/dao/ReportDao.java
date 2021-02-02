package com.hengtianyi.dims.service.dao;

import com.hengtianyi.common.core.base.service.AbstractGenericDao;
import com.hengtianyi.dims.service.entity.ReportEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Report数据库读写DAO
 *
 * @author LY
 */
@Mapper
public interface ReportDao extends AbstractGenericDao<ReportEntity, String> {

  /**
   * 查询最大序列号
   *
   * @param areaCode 村编号
   * @return 最大序列号
   */
  String maxSerialNo(@Param("areaCode") String areaCode);

}
