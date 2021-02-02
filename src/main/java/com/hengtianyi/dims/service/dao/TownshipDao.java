package com.hengtianyi.dims.service.dao;

import com.hengtianyi.common.core.base.service.AbstractGenericDao;
import com.hengtianyi.dims.service.entity.TownshipEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Township数据库读写DAO
 *
 * @author LY
 */
@Mapper
public interface TownshipDao extends AbstractGenericDao<TownshipEntity, String> {


    /**
     * 根据主键查找
     */
    TownshipEntity selectByAreaCode(@Param("areaCode") String areaCode);
}
