package com.hengtianyi.dims.service.dao;

import com.hengtianyi.common.core.base.service.AbstractGenericDao;
import com.hengtianyi.dims.service.entity.ImageClueReportEntity;
import com.hengtianyi.dims.service.entity.ImageRevealEntity;
import com.hengtianyi.dims.service.entity.TaskImageEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ImageClueReport数据库读写DAO
 *
 * @author LY
 */
@Mapper
public interface ImageClueReportDao extends AbstractGenericDao<ImageClueReportEntity, String> {

    void deleteByClueIds(@Param("idsList") List<String> idsList);

    /**
     * 所有流程
     *
     * @param clueId
     * @return list
     */
    List<ImageClueReportEntity> getAllImages(@Param("clueId") String clueId);

//    /**
//     * 自定义分页
//     *
//     * @param dto dto
//     * @return list
//     */
//    List<TaskInfoEntity> pagelist(@Param("dto") QueryDto dto);
//
//    /**
//     * 自定义分页数量
//     *
//     * @param dto dto
//     * @return count
//     */
//    Integer pagecount(@Param("dto") QueryDto dto);
//    /**
//     * 图标所用数据
//     *
//     * @param startTime 开始时间
//     * @param endTime   结束时间
//     * @param areaCode  乡镇
//     * @return list
//     */
//    List<TaskInfoEntity> getEchartsData(@Param("startTime") String startTime,
//                                        @Param("endTime") String endTime,
//                                        @Param("areaCode") String areaCode);
}
