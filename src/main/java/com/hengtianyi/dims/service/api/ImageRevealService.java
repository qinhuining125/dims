package com.hengtianyi.dims.service.api;

import com.hengtianyi.common.core.base.service.AbstractGenericService;
import com.hengtianyi.dims.service.entity.ImageRevealEntity;
import com.hengtianyi.dims.service.entity.TaskFlowEntity;

import java.util.List;

/**
 * ImageReveal接口类
 *
 * @author
 */
public interface ImageRevealService extends AbstractGenericService<ImageRevealEntity, String> {

  /**
   *
   *
   * @param revealId 举报表单Id
   * @return list
   */
  List<ImageRevealEntity> getAllImages(String revealId);
}
