package com.hengtianyi.dims.api;

import com.hengtianyi.common.core.constant.BaseConstant;
import com.hengtianyi.common.core.feature.ServiceResult;
import com.hengtianyi.dims.constant.RoleEnum;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色api
 *
 * @author LY
 */
@RestController
@RequestMapping(value = "/api/role")
public class RoleApiController {

  /**
   * 角色列表
   *
   * @return
   */
  @GetMapping(value = "/list.json", produces = BaseConstant.JSON)
  public String list() {
    ServiceResult<Object> result = new ServiceResult<>();
    result.setSuccess(true);
    List<Map<String, String>> mapList = new ArrayList<>();
    Map<String, String> map = null;
    RoleEnum[] enums = RoleEnum.values();
    for (RoleEnum roleEnum : enums) {
      map = new HashMap<>();
      map.put("roleId", roleEnum.getRoleId().toString());
      map.put("name", roleEnum.getName());
      mapList.add(map);
    }
    result.setResult(mapList);
    return result.toJson();
  }
}
