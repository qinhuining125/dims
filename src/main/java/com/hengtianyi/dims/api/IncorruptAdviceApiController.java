package com.hengtianyi.dims.api;

import com.hengtianyi.common.core.base.CommonEntityDto;
import com.hengtianyi.common.core.constant.BaseConstant;
import com.hengtianyi.common.core.feature.ServiceResult;
import com.hengtianyi.common.core.util.sequence.IdGenUtil;
import com.hengtianyi.common.core.util.sequence.SystemClock;
import com.hengtianyi.dims.constant.FrameConstant;
import com.hengtianyi.dims.constant.RoleEnum;
import com.hengtianyi.dims.service.api.IncorruptAdviceService;
import com.hengtianyi.dims.service.api.SysUserService;
import com.hengtianyi.dims.service.dto.QueryDto;
import com.hengtianyi.dims.service.entity.IncorruptAdviceEntity;
import com.hengtianyi.dims.service.entity.SysUserEntity;
import com.hengtianyi.dims.utils.WebUtil;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 廉政建议
 *
 * @author LY
 */
@RestController
@RequestMapping(value = "/api/advice")
public class IncorruptAdviceApiController {

  private static final Logger LOGGER = LoggerFactory.getLogger(IncorruptAdviceApiController.class);

  @Resource
  private IncorruptAdviceService incorruptAdviceService;
  @Resource
  private SysUserService sysUserService;

  /**
   * 分页查询
   *
   * @param dto 分页
   * @return list
   */
  @PostMapping(value = "/pagelist.json", produces = BaseConstant.JSON)
  public String pagelist(@RequestBody CommonEntityDto<IncorruptAdviceEntity> dto) {
    ServiceResult<Object> result = new ServiceResult<>();
    try {
      QueryDto queryDto = new QueryDto();
      queryDto.setFirst((dto.getCurrentPage() - 1) * FrameConstant.PAGE_SIZE);
      queryDto.setEnd(dto.getCurrentPage() * FrameConstant.PAGE_SIZE);
      CommonEntityDto<IncorruptAdviceEntity> cpDto = incorruptAdviceService.pagelist(queryDto);
      result.setResult(cpDto);
      result.setSuccess(true);
    } catch (Exception ex) {
      result.setError("error");
      LOGGER.error("[pagelist]{}, pageDto = {}, dto = {}", ex.getMessage(), ex);
    }
    return result.toJson();
  }

  /**
   * @param entity
   * @return
   */
  @PostMapping(value = "/saveData.json", produces = BaseConstant.JSON)
  public String saveData(@RequestBody IncorruptAdviceEntity entity, HttpServletRequest request) {
    ServiceResult<Object> result = new ServiceResult<>();
    try {
      entity.setId(IdGenUtil.uuid32());
      entity.setCreateTime(SystemClock.nowDate());
      entity.setUserId(WebUtil.getUserIdByToken(request));
      int ct = incorruptAdviceService.insertData(entity);
      result.setSuccess(ct > 0);
      result.setResult(ct > 0);
    } catch (Exception e) {
      LOGGER.error("[saveData]意见上报出错,{}", e.getMessage(), e);
      result.setError("网格员上报出错");
    }
    return result.toJson();
  }

  /**
   * 上报详情
   *
   * @param id id
   * @return json
   */
  @GetMapping(value = "/detail.json", produces = BaseConstant.JSON)
  public String detail(@RequestParam String id) {
    ServiceResult<Object> result = new ServiceResult<>();
    try {
      IncorruptAdviceEntity entity = incorruptAdviceService.searchDataById(id);
      SysUserEntity userEntity = sysUserService.searchDataById(entity.getUserId());
      entity.setUserName(userEntity.getUserName());
      entity.setRoleId(userEntity.getRoleId());
      entity.setRoleName(RoleEnum.getNameByRoleId(userEntity.getRoleId()));
      result.setResult(entity);
      result.setSuccess(true);
    } catch (Exception e) {
      LOGGER.error("[detail]出错,{}", e.getMessage(), e);
      result.setError("详情页出错");
    }
    return result.toJson();
  }
}
