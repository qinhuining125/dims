package com.hengtianyi.dims.service.api;

import com.hengtianyi.common.core.base.CommonEntityDto;
import com.hengtianyi.common.core.base.CommonPageDto;
import com.hengtianyi.common.core.base.CommonStringDto;
import com.hengtianyi.common.core.base.service.AbstractGenericService;
import com.hengtianyi.dims.service.dto.QueryDto;
import com.hengtianyi.dims.service.dto.InspectionPublicityDto;
import com.hengtianyi.dims.service.entity.ImageRevealEntity;
import com.hengtianyi.dims.service.entity.InspectionPublicityEntity;
import com.hengtianyi.dims.service.entity.TaskImageEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * InspectionPublicity接口类
 *
 * @author LY
 */
public interface InspectionPublicityService extends AbstractGenericService<InspectionPublicityEntity, String> {

  /**
   * 保存
   *
   * @param dto 任务信息
   * @return i
   */
  Integer saveData(InspectionPublicityDto dto, HttpServletRequest request);

  /**
   * 自定义分页
   *
   * @param dto dto
   * @return
   */
  CommonEntityDto<InspectionPublicityEntity> pagelist(QueryDto dto,String IpPort);

  /**
   * 原分页
   *
   * @param dto
   * @return
   */
  CommonPageDto listData(QueryDto dto);

  /**
   * echart数据
   *
   * @param startTime 开始时间
   * @param endTime   结束时间
   * @param areaCode  乡镇编号
   * @return json
   */
  String echartsData(String startTime, String endTime, String areaCode);

  /**
   * 查看任务数量
   *
   * @param dto dto
   * @return
   */
  Integer countTask(QueryDto dto);
  /**
   * 删除图片
   *
   * @param idsDto idsDto
   * @return
   */
  String deleteInfo(CommonStringDto idsDto);
  /**
   * 获取图片
   *
   * @param id id
   * @return
   */
  List<TaskImageEntity> getImages(String id);
}
