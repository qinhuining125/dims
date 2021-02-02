package com.hengtianyi.dims.web;

import com.hengtianyi.common.core.base.CommonPageDto;
import com.hengtianyi.common.core.base.CommonStringDto;
import com.hengtianyi.common.core.base.service.AbstractBaseController;
import com.hengtianyi.common.core.constant.BaseConstant;
import com.hengtianyi.common.core.feature.ServiceResult;
import com.hengtianyi.common.core.util.StringUtil;
import com.hengtianyi.common.core.util.sequence.IdGenUtil;
import com.hengtianyi.dims.exception.ErrorEnum;
import com.hengtianyi.dims.exception.WebException;
import com.hengtianyi.dims.service.api.RelUserAreaService;
import com.hengtianyi.dims.service.entity.RelUserAreaEntity;
import com.hengtianyi.dims.utils.WebUtil;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * RelUserArea - Controller
 *
 * @author LY
 */
@Controller
@RequestMapping(value = RelUserAreaController.MAPPING)
public class RelUserAreaController extends AbstractBaseController<RelUserAreaEntity, String> {

  private static final Logger LOGGER = LoggerFactory.getLogger(RelUserAreaController.class);
  public static final String MAPPING = "a/relUserArea";

  @Resource
  private RelUserAreaService relUserAreaService;

  @Override
  public RelUserAreaService getService() {
    return relUserAreaService;
  }

  @Override
  public Logger getLogger() {
    return LOGGER;
  }

  /**
   * 模块首页[视图]
   *
   * @param model Model
   * @return 视图
   */
  @GetMapping(value = "/index.html", produces = BaseConstant.HTML)
  public String index(Model model) {
    model.addAttribute("mapping", MAPPING);
    return "web/relUserArea/relUserArea_index";
  }

  /**
   * 模块编辑页[视图]
   *
   * @param model Model
   * @param id    主键ID
   * @return 视图
   */
  @GetMapping(value = "/edit.html", produces = BaseConstant.HTML)
  public String edit(Model model, @RequestParam(value = "id", required = false) String id) {
    RelUserAreaEntity entity = null;
    if (StringUtil.isNoneBlank(id)) {
      entity = this.getDataByIdCommon(id);
      if (entity == null) {
        throw new WebException(ErrorEnum.NO_DATA);
      }
    }
    model.addAttribute("mapping", MAPPING);
    model.addAttribute("entity", entity);
    return "web/relUserArea/relUserArea_edit";
  }

  /**
   * 通过AJAX获取列表信息[JSON]
   *
   * @param pageDto 通用DTO
   * @param dto     查询DTO
   * @return JSON
   */
  @ResponseBody
  @PostMapping(value = "/getDataList.json", produces = BaseConstant.JSON)
  public String getDataList(@ModelAttribute CommonPageDto pageDto,
      @ModelAttribute RelUserAreaEntity dto) {
    return this.getDataListCommon(pageDto, dto);
  }

  /**
   * 通过AJAX获取单条信息[JSON]
   *
   * @param id 主键ID
   * @return JSON
   */
  @ResponseBody
  @PostMapping(value = "/getDataInfo.json", produces = BaseConstant.JSON)
  public String getDataInfo(@RequestParam String id) {
    return this.getDataInfoCommon(id);
  }

  /**
   * 通过AJAX删除[JSON]
   *
   * @param idsDto 主键集合DTO
   * @return JSON
   */
  @ResponseBody
  @PostMapping(value = "/deleteData.json", produces = BaseConstant.JSON)
  public String deleteData(@ModelAttribute CommonStringDto idsDto) {
    return deleteDataCommon(idsDto);
  }

  /**
   * 通过AJAX保存[JSON]
   *
   * @param entity 对象实体
   * @param n      这个可以是任意非空字符，用来确定是否添加
   * @return JSON
   */
  @ResponseBody
  @PostMapping(value = "/saveData.json", produces = BaseConstant.JSON)
  public String saveData(@ModelAttribute RelUserAreaEntity entity,
      @RequestParam(value = BaseConstant.PAGE_ADD_SIGN, required = false) String n) {
    boolean isNew = StringUtil.isBlank(n);
    if (isNew) {
      String id = entity.getUserId();
      if (StringUtil.isBlank(id)) {
        entity.setUserId(IdGenUtil.uuid32());
      }
      return this.insertDataCommon(entity);
    } else {
      return this.updateDataCommon(entity);
    }
  }

  /**
   * 角色列表
   *
   * @return
   */
  @GetMapping(value = "/list.json", produces = BaseConstant.JSON)
  public String list(HttpServletRequest request) {
    ServiceResult<Object> result = new ServiceResult<>();
    try {
      //当前用户
      String userId = WebUtil.getUserId(request);
      result.setResult(relUserAreaService.getUserArealist(userId));
    } catch (Exception e) {
      LOGGER.error("[查询下级用户出错],{}", e.getMessage(), e);
      result.setError("查询下级用户出错");
    }
    return result.toJson();
  }

  @Override
  public void clearEntity(RelUserAreaEntity entity) {
    if (null != entity) {
      entity.clean();
    }
  }
}
