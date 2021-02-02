package com.hengtianyi.dims.api;

import com.hengtianyi.common.core.constant.BaseConstant;
import com.hengtianyi.common.core.feature.ServiceResult;
import com.hengtianyi.dims.service.api.ReportTypeService;
import com.hengtianyi.dims.service.entity.ReportTypeEntity;
import java.util.Collections;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 上报类型api
 *
 * @author LY
 */
@RestController
@RequestMapping(value = "/api/reportType")
public class ReportTypeApiController {

  @Resource
  private ReportTypeService reportTypeService;

  /**
   * 上报类型内容查询
   *
   * @param roldId 角色
   * @return list
   */
  @GetMapping(value = "/list.json", produces = BaseConstant.JSON)
  public String list(@RequestParam("roldId") Integer roldId) {
    ServiceResult<Object> result = new ServiceResult<>();
    result.setSuccess(true);
    ReportTypeEntity entity = new ReportTypeEntity();
    entity.setRoleId(roldId);
    List<ReportTypeEntity> list = reportTypeService
        .searchAllData(entity, Collections.singletonMap("sort_no", "asc"));
    result.setResult(list);
    return result.toJson();
  }
}
