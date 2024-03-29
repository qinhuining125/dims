package com.hengtianyi.dims.service.api;

import com.hengtianyi.common.core.base.CommonEntityDto;
import com.hengtianyi.common.core.base.CommonPageDto;
import com.hengtianyi.common.core.base.service.AbstractGenericService;
import com.hengtianyi.dims.service.dto.QueryDto;
import com.hengtianyi.dims.service.entity.ClueReportEntity;
import com.hengtianyi.dims.service.entity.ImageClueReportEntity;
import com.hengtianyi.dims.service.entity.TaskImageEntity;

import java.util.List;

/**
 * ClueReport接口类
 *
 * @author LY
 */
public interface ClueReportService extends AbstractGenericService<ClueReportEntity, String> {

    /**
     * 自定义分页
     *
     * @param dto dto
     * @return
     */
    CommonEntityDto<ClueReportEntity> pagelist(QueryDto dto);


    List<ImageClueReportEntity> getImages(String id);


    List<ImageClueReportEntity> getAttachmentsByIdType(String id, int type);

    /**
     * 原分页
     *
     * @param dto
     * @return
     */
    CommonPageDto listData(QueryDto dto);

    /**
     * 查看报告数量
     *
     * @param dto dto
     * @return
     */
    Integer countReport(QueryDto dto);

    /**
     * echart数据
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param areaCode  乡镇编号
     * @return json
     */
    String echartsData(String startTime, String endTime, String areaCode, String reportRoleId, String reportState, String reportIds1,String reportIds2, Integer receivedRoleId);
    /**
     * echart 0数据
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param areaCode  乡镇编号
     * @return json
     */
    String echartsReport0Data(String startTime, String endTime, String areaCode, String reportRoleId, String reportState, String reportIds, Integer receivedRoleId);

}
