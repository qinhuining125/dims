package com.hengtianyi.dims.api;

import com.hengtianyi.common.core.constant.BaseConstant;
import com.hengtianyi.common.core.feature.ServiceResult;
import com.hengtianyi.dims.service.api.PatrolInfoService;
import com.hengtianyi.dims.service.entity.PatrolInfoEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 巡察信息api
 *
 * @author LY
 */
@RestController
@RequestMapping(value = "/api/patrolInfo")
public class PatrolInfoApiController {

  private static final Logger LOGGER = LoggerFactory.getLogger(PatrolInfoApiController.class);

  @Resource
  private PatrolInfoService patrolInfoService;


  /**
   * 二维码详情
   *
   * @param id id
   * @return json
   */
  @GetMapping(value = "/detail.json", produces = BaseConstant.JSON)
  public String detail(@RequestParam String id) {
    ServiceResult<Object> result = new ServiceResult<>();
    try {
      PatrolInfoEntity entity = patrolInfoService.searchDataById(id);
      Date date1=entity.getEndTime();
      Date date2 = new Date();
      DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      String n1 = format.format(date1);
      String n2 = format.format(date2);
      if(n1.compareTo(n2)>=0){
        result.setMessage("正确查询到二维码的详情信息");
        result.setResult(entity);
        result.setSuccess(true);
      }else{
        result.setMessage("该二维码已经失效，无法进行举报操作");
        result.setSuccess(false);
      }
    } catch (Exception e) {
      LOGGER.error("[二维码查询detail]出错,{}", e.getMessage(), e);
      result.setMessage("查询二维码详情出错");
      result.setSuccess(false);
    }
    return result.toJson();
  }

  /**
   * 判定当前时间，是否有在进行的巡察轮次
   *
   * @return json
   */
  @GetMapping(value = "/isHaveProcess.json", produces = BaseConstant.JSON)
  public String isHaveProcess(HttpServletRequest request) {

    ServiceResult<Object> result = new ServiceResult<>();
    PatrolInfoEntity entity=patrolInfoService.isHaveProcess();
      if (entity.getId()==null) {//表示没有这个时间段的
        result.setMessage("该时间段没有正在进行的巡察");
        result.setSuccess(Boolean.FALSE);
      } else {//表示有正在进行的信息
        //将正在进行查询的结果拿过来。
        result.setMessage("查询到当前时间段巡察信息");
        result.setResult(entity);
        result.setSuccess(Boolean.TRUE);
      }
    return result.toJson();
  }

}
