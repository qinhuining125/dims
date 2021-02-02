package com.hengtianyi.dims.web;

import com.hengtianyi.common.core.base.CommonPageDto;
import com.hengtianyi.common.core.base.CommonStringDto;
import com.hengtianyi.common.core.base.service.AbstractBaseController;
import com.hengtianyi.common.core.constant.BaseConstant;
import com.hengtianyi.common.core.util.StringUtil;
import com.hengtianyi.common.core.util.sequence.IdGenUtil;
import com.hengtianyi.dims.exception.ErrorEnum;
import com.hengtianyi.dims.exception.WebException;
import com.hengtianyi.dims.service.api.TaskFlowService;
import com.hengtianyi.dims.service.entity.TaskFlowEntity;
import javax.annotation.Resource;
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
 * TaskFlow - Controller
 * @author LY
 */
@Controller
@RequestMapping(value = TaskFlowController.MAPPING)
public class TaskFlowController extends AbstractBaseController<TaskFlowEntity, String> {
  private static final Logger LOGGER = LoggerFactory.getLogger(TaskFlowController.class);
  public static final String MAPPING = "a/taskFlow";

  @Resource
  private TaskFlowService taskFlowService;

  @Override
  public TaskFlowService getService() {
    return taskFlowService;
  }

  @Override
  public Logger getLogger() {
    return LOGGER;
  }

  /**
   * 模块首页[视图]
   * @param model Model
   * @return 视图
   */
  @GetMapping(value = "/index.html", produces = BaseConstant.HTML)
  public String index(Model model) {
    model.addAttribute("mapping", MAPPING);
    return "web/taskFlow/taskFlow_index";
  }

  /**
   * 模块编辑页[视图]
   * @param model Model
   * @param id 主键ID
   * @return 视图
   */
  @GetMapping(value = "/edit.html", produces = BaseConstant.HTML)
  public String edit(Model model, @RequestParam(value = "id", required = false) String id) {
    TaskFlowEntity entity = null;
    if (StringUtil.isNoneBlank(id)) {
      entity = this.getDataByIdCommon(id);
      if (entity == null) {
        throw new WebException(ErrorEnum.NO_DATA);
      }
    }
    model.addAttribute("mapping", MAPPING);
    model.addAttribute("entity", entity);
    return "web/taskFlow/taskFlow_edit";
  }

  /** 
   * 通过AJAX获取列表信息[JSON]
   * @param pageDto 通用DTO
   * @param dto 查询DTO
   * @return JSON
   */
  @ResponseBody
  @PostMapping(value = "/getDataList.json", produces = BaseConstant.JSON)
  public String getDataList(@ModelAttribute CommonPageDto pageDto,
                            @ModelAttribute TaskFlowEntity dto) {
    return this.getDataListCommon(pageDto, dto);
  }

  /**
   * 通过AJAX获取单条信息[JSON]
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
   * @param entity 对象实体
   * @param n 这个可以是任意非空字符，用来确定是否添加
   * @return JSON
   */
  @ResponseBody
  @PostMapping(value = "/saveData.json", produces = BaseConstant.JSON)
  public String saveData(@ModelAttribute TaskFlowEntity entity,
                         @RequestParam(value = BaseConstant.PAGE_ADD_SIGN, required = false) String n) {
    boolean isNew = StringUtil.isBlank(n);
    if (isNew) {
      // 如果没有在表单指定主键，这里需要生成一个
      String id = entity.getId();
      if (StringUtil.isBlank(id)) {
        entity.setId(IdGenUtil.uuid32());
      }
      return this.insertDataCommon(entity);
    } else {
      return this.updateDataCommon(entity);
    }
  }

  @Override
  public void clearEntity(TaskFlowEntity entity) {
    if (null != entity) {
      entity.clean();

    }
  }
}
