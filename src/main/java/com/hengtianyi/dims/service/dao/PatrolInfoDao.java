package com.hengtianyi.dims.service.dao;

import com.hengtianyi.common.core.base.service.AbstractGenericDao;
import com.hengtianyi.dims.service.entity.PatrolInfoEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * PatrolInfo数据库读写DAO
 *
 * @author jyy
 */
@Mapper
public interface PatrolInfoDao extends AbstractGenericDao<PatrolInfoEntity, String> {

}
