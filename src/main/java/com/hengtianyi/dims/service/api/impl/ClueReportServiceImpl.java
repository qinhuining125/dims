package com.hengtianyi.dims.service.api.impl;

import com.hengtianyi.common.core.base.CommonEntityDto;
import com.hengtianyi.common.core.base.CommonPageDto;
import com.hengtianyi.common.core.base.service.AbstractGenericServiceImpl;
import com.hengtianyi.common.core.feature.ServiceResult;
import com.hengtianyi.common.core.util.StringUtil;
import com.hengtianyi.dims.constant.FrameConstant;
import com.hengtianyi.dims.service.api.ClueReportService;
import com.hengtianyi.dims.service.api.SysUserService;
import com.hengtianyi.dims.service.dao.*;
import com.hengtianyi.dims.service.dto.KeyValueDto;
import com.hengtianyi.dims.service.dto.QueryDto;
import com.hengtianyi.dims.service.entity.*;

import java.util.*;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * ClueReport接口的实现类
 *
 * @author LY
 */
@Service
public class ClueReportServiceImpl extends AbstractGenericServiceImpl<ClueReportEntity, String>
        implements ClueReportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClueReportServiceImpl.class);

    @Resource
    private ClueReportDao clueReportDao;
    @Resource
    private ClueFlowDao clueFlowDao;
    @Resource
    private VillageDao villageDao;
    @Resource
    private ReportTypeDao reportTypeDao;

    @Resource
    private TownshipDao townshipDao;

    @Resource
    private SysUserService sysUserService;
    /**
     * 实体与数据表字段的映射
     * <p>格式：实体字段 - 数据库字段</p>
     */
    private static final Map<String, String> PROPERTY_COLUMN;

    /**
     * 默认排序的sql，order by后面的部分，例如: id asc,name desc
     */
    private static final String DEFAULT_ORDER_SQL = "id asc";

    static {
        PROPERTY_COLUMN = new HashMap<>(5);
        // 格式：实体字段名 - 数据库字段名
        PROPERTY_COLUMN.put("id", "ID");
        PROPERTY_COLUMN.put("userId", "USER_ID");
        PROPERTY_COLUMN.put("reportIds", "REPORT_IDS");
        PROPERTY_COLUMN.put("clueDescribe", "CLUE_DESCRIBE");
        PROPERTY_COLUMN.put("createTime", "CREATE_TIME");
        PROPERTY_COLUMN.put("reportRoleId", "REPORT_ROLE_ID");
    }

    /**
     * 注入Mybatis的Dao
     *
     * @return clueReportDao
     */
    @Override
    public ClueReportDao getDao() {
        return clueReportDao;
    }

    /**
     * 注入实体与数据库字段的映射
     *
     * @return key：实体属性名，value：数据库字段名
     */
    @Override
    public Map<String, String> getPropertyColumn() {
        return PROPERTY_COLUMN;
    }

    /**
     * 注入排序集合
     *
     * @return 排序集合
     */
    @Override
    public String getDefaultSort() {
        return DEFAULT_ORDER_SQL;
    }

    /**
     * 自定义分页
     *
     * @param dto dto
     * @return
     */
    @Override
    public CommonEntityDto<ClueReportEntity> pagelist(QueryDto dto) {
        Integer count = clueReportDao.pagecount(dto);
        List<ClueReportEntity> list = clueReportDao.pagelist(dto);
        for (ClueReportEntity clueReportEntity : list) {
            clueReportEntity.setReceiveId(clueFlowDao.getReceiveId(clueReportEntity.getId(), 1));
            clueReportEntity.setFlows(clueFlowDao.getAllFlows(clueReportEntity.getId()));
            SysUserEntity sysuser = sysUserService.searchDataById(clueReportEntity.getUserId());
            TownshipEntity town = townshipDao.selectByAreaCode(sysuser.getAreaCode().substring(0, 9));
            clueReportEntity.setReportUserAreaName(town.getAreaName());
            List<KeyValueDto> dtoList = new ArrayList<>();
            KeyValueDto keyValueDto = null;
            for (String key : clueReportEntity.getReportIds().split(StringUtil.COMMA)) {
                keyValueDto = new KeyValueDto();
                keyValueDto.setKey(key);
                keyValueDto.setValue(
                        reportTypeDao.contentByRoleSortNo(clueReportEntity.getReportRoleId(), Integer.parseInt(key)));
                dtoList.add(keyValueDto);
            }
            clueReportEntity.setDtoList(dtoList);
        }
        CommonEntityDto<ClueReportEntity> result = new CommonEntityDto<>(list);
        result.setCurrentPage(dto.getCurrentPage());
        result.setSize(FrameConstant.PAGE_SIZE);
        result.setTotal(count);
        return result;
    }

    /**
     * 原分页
     *
     * @param dto
     * @return
     */
    @Override
    public CommonPageDto listData(QueryDto dto) {
        try {
            int rowsCount = clueReportDao.pagecount(dto);
            List<ClueReportEntity> listData = clueReportDao.pagelist(dto);
            for (ClueReportEntity entity : listData) {
                SysUserEntity sysuser = sysUserService.searchDataById(entity.getUserId());
                TownshipEntity town = townshipDao.selectByAreaCode(sysuser.getAreaCode().substring(0, 9));
                entity.setReportUserAreaName(town.getAreaName());
                List<KeyValueDto> dtoList = new ArrayList<>();
                KeyValueDto keyValueDto = null;
                String[] reportIds=entity.getReportIds().split(StringUtil.COMMA);
                for (String key : reportIds) {
                    keyValueDto = new KeyValueDto();
                    keyValueDto.setKey(key);
                    keyValueDto.setValue(
                            reportTypeDao.contentByRoleSortNo(entity.getReportRoleId(), Integer.parseInt(key)));
                    dtoList.add(keyValueDto);
                }
                entity.setDtoList(dtoList);
            }
            CommonPageDto cpDto = new CommonPageDto(listData);
            cpDto.setCurrent(dto.getCurrentPage());
            cpDto.setRowCount(FrameConstant.PAGE_SIZE);
            cpDto.setTotal(rowsCount);
            return cpDto;
        } catch (Exception ex) {
            LOGGER.error("[listData]{}, dto = {}", ex.getMessage(), dto.toJson(), ex);
        }
        return new CommonPageDto();
    }

    /**
     * 查看报告数量
     *
     * @param dto dto
     * @return
     */
    @Override
    public Integer countReport(QueryDto dto) {
        return clueReportDao.pagecount(dto);
    }

    /**
     * 查询所有的上报
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param areaCode  乡镇编号
     * @return list
     */
    @Override
    public String echartsData(String startTime, String endTime, String areaCode, String reportRoleId, String reportState, String reportIds1, String reportIds2) {
        ServiceResult<Object> result = new ServiceResult();
        try {
            String reportIds="";
            String reportContents="";
            String reportContents2="";
            List<String> wgyContentsList = new ArrayList<>();
            List<String> llyContentsList = new ArrayList<>();
            if(reportState!=null && !reportState.equals("") && !reportState.equals("全部")){
                List<ReportTypeEntity> listByStateWgy = reportTypeDao.getListByStateWgy(reportState);
                List<ReportTypeEntity> listByStateLly= reportTypeDao.getListByStateLly(reportState);
                if(!listByStateWgy.isEmpty()){
                    for(ReportTypeEntity e : listByStateWgy){
                        wgyContentsList.add(e.getContent());
                    }
                }
                if(!listByStateLly.isEmpty()){
                    for(ReportTypeEntity e : listByStateLly){
                        llyContentsList.add(e.getContent());
                    }
                }
            }
            if (reportIds1.length()>0){
                reportIds=reportIds1;
                reportRoleId="1001";
                StringBuffer sb = new StringBuffer();
                sb.append("[").append(reportIds1).append("-").append(reportTypeDao.contentByRoleSortNo(1001, Integer.parseInt(reportIds1))).append("]");
                reportContents=sb.toString();
            }
            if (reportIds2.length()>0){
                reportIds=reportIds2;
                reportRoleId="1002";
                StringBuffer sb = new StringBuffer();
                sb.append("[").append(reportIds2).append("-").append(reportTypeDao.contentByRoleSortNo(1002, Integer.parseInt(reportIds2))).append("]");
                reportContents2=sb.toString();
            }
            List<ClueReportEntity> reportList = clueReportDao
                    .getEchartsData(startTime, endTime, areaCode, reportRoleId, reportState, reportIds,reportContents,reportContents2,wgyContentsList,llyContentsList);

            Map<String, Object> map = count(reportList);
            List<String> wgynrsList = reportTypeDao.contentByRole(1001);
            List<String> llynrsList = reportTypeDao.contentByRole(1002);
            //删除集合中某一元素值
            Iterator<String> iterator1 = wgynrsList.iterator();
            while (iterator1.hasNext()) {
                String next = iterator1.next();
                if (next.indexOf("每周零报告") > -1) {
                    iterator1.remove();
                }
            }
            //删除集合中某一元素值
            Iterator<String> iterator2 = llynrsList.iterator();
            while (iterator2.hasNext()) {
                String next = iterator2.next();
                if (next.indexOf("每周零报告") > -1) {
                    iterator2.remove();
                }
            }
            Integer roleId1 = 1001;
            Integer roleId2 = 1002;
            List<ReportTypeEntity> reportTypeList1 = reportTypeDao.getListAll(roleId1);
            List<ReportTypeEntity> reportTypeList2 = reportTypeDao.getListAll(roleId2);
            Integer[] wgys = new Integer[reportTypeList1.size()];
            Integer[] llys = new Integer[reportTypeList2.size()];
            String[] types = new String[wgynrsList.size()];
            int a = 0;
            int b = 0;
            for (int i = 0; i < reportTypeList1.size(); i++) {
                if (reportTypeList1.get(i).getContent().indexOf("每周零报告") == -1) {
//                    types[i] = "类型" +( i + 1);
                    wgys[a] = sumNum(reportList, roleId1, i + 1);
                    a++;
                }
            }
            for (int i = 0; i < reportTypeList2.size(); i++) {
                if (reportTypeList2.get(i).getContent().indexOf("每周零报告") == -1) {
//                    types[i] = "类型" +( i + 1);
                    llys[b] = sumNum(reportList, roleId2, i + 1);
                    b++;
                }
            }
            map.put("types", types);
            map.put("wgys", wgys);
            map.put("llys", llys);
            map.put("wgynrs", wgynrsList);
            map.put("llynrs", llynrsList);
//            map.put("wgynrs", reportTypeDao.contentByRole(1001));
//            map.put("llynrs", reportTypeDao.contentByRole(1002));
            List<VillageEntity> villageList = villageDao.areaList(areaCode);
            String[] villageNames = new String[villageList.size()];
            String[] unAccepts = new String[villageList.size()];
            String[] knowTasks = new String[villageList.size()];
            String[] turnToOtherTasks = new String[villageList.size()];
            String[] accepts = new String[villageList.size()];
            String[] completes = new String[villageList.size()];
            String[] sums = new String[villageList.size()];
            for (int i = 0; i < villageList.size(); i++) {
                VillageEntity village = villageList.get(i);
                reportList = clueReportDao.getEchartsData(startTime, endTime, village.getAreaCode(), reportRoleId, reportState, reportIds,reportContents,reportContents2,wgyContentsList,llyContentsList);
                Map<String, Object> dataMap = count(reportList);
                villageNames[i] = village.getAreaName();
                unAccepts[i] = dataMap.get("unAccept").toString();
                knowTasks[i] = dataMap.get("knowTask").toString();
                turnToOtherTasks[i] = dataMap.get("turnToOtherTask").toString();
                accepts[i] = dataMap.get("accept").toString();
                completes[i] = dataMap.get("complete").toString();
                sums[i] = dataMap.get("sum").toString();
            }
            map.put("villageNames", villageNames);
            map.put("unAccepts", unAccepts);
            map.put("knowTasks", knowTasks);
            map.put("turnToOtherTasks", turnToOtherTasks);
            map.put("accepts", accepts);
            map.put("completes", completes);
            map.put("sums", sums);
            result.setSuccess(true);
            result.setResult(map);
        } catch (Exception e) {
            LOGGER.error("[echartsData]出错,{}", e.getMessage(), e);
            result.setError("false");
        }
        return result.toJson();
    }

    private Map<String, Object> count(List<ClueReportEntity> reportList) {
        Map<String, Object> map = new HashMap<>();
        Integer unAccept = 0;
        Integer knowTask = 0;
        Integer turnToOtherTask = 0;
        Integer accept = 0;
        Integer complete = 0;
        Integer sum = 0;

        Integer total = reportList.size();
        for (ClueReportEntity reportEntity : reportList) {
            if (reportEntity.getReportIds().indexOf("11")==-1){
                if (reportEntity.getState() == 0) {
                    unAccept += 1;
                } else if (reportEntity.getState() == 3) {
                    knowTask += 1;
                } else if (reportEntity.getState() == 4) {
                    turnToOtherTask += 1;
                } else if (reportEntity.getState() == 1) {
                    accept += 1;
                } else if (reportEntity.getState() == 2) {
                    complete += 1;
                }
                if (reportEntity.getState() == 0 || reportEntity.getState() == 1 || reportEntity.getState() == 2 || reportEntity.getState() == 3 || reportEntity.getState() == 4) {
                    sum += 1;
                }
            }
        }
        map.put("total", total);
        map.put("unAccept", unAccept);
        map.put("knowTask", knowTask);
        map.put("turnToOtherTask", turnToOtherTask);
        map.put("accept", accept);
        map.put("complete", complete);
        map.put("sum", sum);
        return map;
    }

    @Override
    public String echartsReport0Data(String startTime, String endTime, String areaCode, String reportRoleId, String reportState, String reportIds) {
        ServiceResult<Object> result = new ServiceResult();
        try {
            String report0Wook="11";
            String roleId="1002";
            List<ClueReportEntity> reportList = clueReportDao
                    .getEchartsReport0Data(startTime, endTime, areaCode, roleId,report0Wook);
            Map<String, Object> map = countReport0(reportList);

            List<VillageEntity> villageList = villageDao.areaList(areaCode);
            String[] villageNames = new String[villageList.size()];
            String[] unAccepts = new String[villageList.size()];
            String[] knowTasks = new String[villageList.size()];
            String[] turnToOtherTasks = new String[villageList.size()];
            String[] accepts = new String[villageList.size()];
            String[] completes = new String[villageList.size()];
            String[] sums = new String[villageList.size()];
            for (int i = 0; i < villageList.size(); i++) {
                VillageEntity village = villageList.get(i);
                reportList = clueReportDao.getEchartsReport0Data(startTime, endTime, village.getAreaCode(), roleId,report0Wook);
                Map<String, Object> dataMap = countReport0(reportList);
                villageNames[i] = village.getAreaName();
                unAccepts[i] = dataMap.get("unAccept").toString();
                knowTasks[i] = dataMap.get("knowTask").toString();
                turnToOtherTasks[i] = dataMap.get("turnToOtherTask").toString();
                accepts[i] = dataMap.get("accept").toString();
                completes[i] = dataMap.get("complete").toString();
                sums[i] = dataMap.get("sum").toString();
            }
            map.put("villageNames", villageNames);
            map.put("unAccepts", unAccepts);
            map.put("knowTasks", knowTasks);
            map.put("turnToOtherTasks", turnToOtherTasks);
            map.put("accepts", accepts);
            map.put("completes", completes);
            map.put("sums", sums);
            result.setSuccess(true);
            result.setResult(map);
        } catch (Exception e) {
            LOGGER.error("[echartsData]出错,{}", e.getMessage(), e);
            result.setError("false");
        }
        return result.toJson();
    }

    private Map<String, Object> countReport0(List<ClueReportEntity> reportList) {
        Map<String, Object> map = new HashMap<>();
        Integer unAccept = 0;
        Integer knowTask = 0;
        Integer turnToOtherTask = 0;
        Integer accept = 0;
        Integer complete = 0;
        Integer sum = 0;

        Integer total = reportList.size();
        for (ClueReportEntity reportEntity : reportList) {
            if (reportEntity.getState() == 0) {
                unAccept += 1;
            } else if (reportEntity.getState() == 3) {
                knowTask += 1;
            } else if (reportEntity.getState() == 4) {
                turnToOtherTask += 1;
            } else if (reportEntity.getState() == 1) {
                accept += 1;
            } else if (reportEntity.getState() == 2) {
                complete += 1;
            }
            if (reportEntity.getState() == 0 || reportEntity.getState() == 1 || reportEntity.getState() == 2 || reportEntity.getState() == 3 || reportEntity.getState() == 4) {
                sum += 1;
            }
        }
        map.put("total", total);
        map.put("unAccept", unAccept);
        map.put("knowTask", knowTask);
        map.put("turnToOtherTask", turnToOtherTask);
        map.put("accept", accept);
        map.put("complete", complete);
        map.put("sum", sum);
        return map;
    }


    private int sumNum(List<ClueReportEntity> reportList, Integer roleId, Integer sortNo) {
        int total = 0;
        for (ClueReportEntity entity : reportList) {
            if (entity.getReportRoleId().equals(roleId)
                    && entity.getReportIds().indexOf(sortNo.toString()) > -1) {
                total += 1;
            }
        }
        return total;
    }
}
