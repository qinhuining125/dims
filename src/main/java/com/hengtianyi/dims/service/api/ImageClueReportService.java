package com.hengtianyi.dims.service.api;

import com.hengtianyi.common.core.base.service.AbstractGenericService;
import com.hengtianyi.dims.service.entity.ImageClueReportEntity;
import com.hengtianyi.dims.service.entity.ImageRevealEntity;

import java.util.List;

/**
 * ImageClueReport接口类
 *
 * @author
 */
public interface ImageClueReportService extends AbstractGenericService<ImageClueReportEntity, String> {

  /**
   *
   *
   * @param revealId 举报表单Id
   * @return list
   */
  List<ImageClueReportEntity> getAllImages(String revealId);
}
