package com.hengtianyi.dims.web;
import com.hengtianyi.common.core.base.CommonPageDto;
import com.hengtianyi.common.core.base.CommonStringDto;
import com.hengtianyi.common.core.base.service.AbstractBaseController;
import com.hengtianyi.common.core.constant.BaseConstant;
import com.hengtianyi.common.core.feature.ServiceResult;
import com.hengtianyi.common.core.util.JsonUtil;
import com.hengtianyi.common.core.util.StringUtil;
import com.hengtianyi.dims.constant.FrameConstant;
import com.hengtianyi.dims.exception.ErrorEnum;
import com.hengtianyi.dims.exception.WebException;
import com.hengtianyi.dims.service.api.RelUserAreaService;
import com.hengtianyi.dims.service.api.SysUserService;
import com.hengtianyi.dims.service.api.TaskFlowService;
import com.hengtianyi.dims.service.api.InspectionPublicityService;
import com.hengtianyi.dims.service.api.TownshipService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

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
 * InspectionPublicity - Controller
 *
 * @author LY
 */
@RestController
@RequestMapping(value = "/web")
public class uploadImgListController {
    private static final Logger LOGGER = LoggerFactory.getLogger(InspectionPublicityController.class);
    public static final String MAPPING = "a/inspectionPublicity";


    @Resource
    private TownshipService townshipService;
    @RequestMapping("/hello1")
    public String helloSpringBoot() {
        return "Hello SpriddddddddddddngBoot Project.";
    }
    /**
     * 模块首页[视图]
     *
     * @param model Model
     * @return 视图
     */
    @GetMapping(value = "/demo", produces = BaseConstant.HTML)
    public String index(Model model) {
        model.addAttribute("mapping", MAPPING);
        model.addAttribute("areaList", townshipService.areaList());
        return "web/demo/demo";
    }
}
