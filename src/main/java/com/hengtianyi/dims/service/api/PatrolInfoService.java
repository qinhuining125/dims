package com.hengtianyi.dims.service.api;

import com.hengtianyi.common.core.base.service.AbstractGenericService;
import com.hengtianyi.dims.service.entity.PatrolInfoEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * PatrolInfo接口类
 *
 * @author jyy
 */
public interface PatrolInfoService extends AbstractGenericService<PatrolInfoEntity, String> {

  /**
   * 保存
   *
   * @param dto 巡察信息
   * @return i
   */
  Integer saveData(PatrolInfoEntity dto, HttpServletRequest request);

    /**
     * 获取文件保存路径，做回显显示
     *
     * @param words 二维码下面的文字
     * @param id 此二维码在数据库中对应的id
     * @return i
     */
  String filePath(HttpServletRequest request,String words,String id);

  Boolean checkPatrolName(String patrolName );

  Boolean checkPatrolTime(String startTime,String endTime);

  PatrolInfoEntity isHaveProcess();

  List<PatrolInfoEntity> getImageUrl();

 }
