package com.hengtianyi.dims.service.api;

import com.hengtianyi.common.core.base.service.AbstractGenericService;
import com.hengtianyi.dims.service.entity.VillageEntity;
import java.util.List;

/**
 * Village接口类
 *
 * @author LY
 */
public interface VillageService extends AbstractGenericService<VillageEntity, String> {

  /**
   * 乡镇下的村
   *
   * @param townCode 乡镇编号
   * @return 村list
   */
  List<VillageEntity> townChilds(String townCode);

  /**
   * 乡镇下的村
   *
   * @param townCode 乡镇编号
   * @return 村list
   */
  List<VillageEntity> townChilds2(String townCode);

  List<VillageEntity> areaList2(String areaCode);

}
