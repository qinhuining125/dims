package com.hengtianyi.dims.service.dao;

import com.hengtianyi.common.core.base.service.AbstractGenericDao;
import com.hengtianyi.dims.service.entity.VillageEntity;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Village数据库读写DAO
 *
 * @author LY
 */
@Mapper
public interface VillageDao extends AbstractGenericDao<VillageEntity, String> {

  /**
   * 乡镇下的村
   *
   * @param townCode 乡镇编号
   * @return 村list
   */
  List<VillageEntity> townChilds(@Param("townCode") String townCode);

  /**
   * 地区
   *
   * @param areaCode 地区编号
   * @return list
   */
  List<VillageEntity> areaList(@Param("areaCode") String areaCode);



  /**
   * 乡镇下的村2-for 百姓举报中的业务逻辑
   *
   * @param townCode 乡镇编号
   * @return 村list
   */
  List<VillageEntity> townChilds2(@Param("townCode") String townCode);



  /**
   * 地区2
   *
   * @return list
   */
  List<VillageEntity> areaList2(@Param("areaCode") String areaCode);


}
