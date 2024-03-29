package com.hengtianyi.dims.web;

import com.hengtianyi.common.core.constant.BaseConstant;
import com.hengtianyi.common.core.util.TimeUtil;
import com.hengtianyi.dims.service.api.*;
import com.hengtianyi.dims.service.entity.ReportTypeEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 流程管理
 *
 * @author LY
 */
@Controller
public class FlowController {

    @Resource
    private TownshipService townshipService;
    @Resource
    private ClueReportService clueReportService;
    @Resource
    private IncorruptAdviceService incorruptAdviceService;
    @Resource
    private TaskInfoService taskInfoService;
    @Resource
    private RevealInfoService revealInfoService;
    @Resource
    private ReportTypeService reportTypeService;

    /**
     * @param model
     * @return
     */
    @GetMapping(value = "a/flow/clue.html", produces = BaseConstant.HTML)
    public String clue(Model model) {
        return "web/flow/clue_index";
    }

    @GetMapping(value = "a/flow/advice.html", produces = BaseConstant.HTML)
    public String advice(Model model) {
        return "web/flow/advice_index";
    }

    @GetMapping(value = "a/flow/task.html", produces = BaseConstant.HTML)
    public String task(Model model) {
        return "web/flow/task_index";
    }

    @GetMapping(value = "a/analysis/clue.html", produces = BaseConstant.HTML)
    public String analysisClue(Model model) {

        Integer roleId1 = 1001;//        网格员
        Integer roleId2 = 1002;//联络员
        model.addAttribute("areaList", townshipService.areaList());
        model.addAttribute("reportTypeList1", reportTypeService.getListAll(roleId1));
        List<ReportTypeEntity> reportTypeList2 = reportTypeService.getListAll(roleId2);
        for (int i=0;i<reportTypeList2.size();i++){
            if (reportTypeList2.get(i).getContent().indexOf("每周零报告")>-1){
                reportTypeList2.remove(i);
            }
        }
        model.addAttribute("reportTypeList2", reportTypeList2);
        return "web/analysis/clue_index";
    }

    @ResponseBody
    @PostMapping(value = "a/analysis/clueData.json", produces = BaseConstant.JSON)
    public String clueData(@RequestParam(required = false) Long startTime,
                           @RequestParam(required = false) Long endTime,
                           @RequestParam(required = false) String areaCode,
                           @RequestParam(required = false) String reportRoleId,
                           @RequestParam(required = false) String reportState,
                           @RequestParam(required = false) String reportIds1,
                           @RequestParam(required = false) String reportIds2,
                           @RequestParam(required = false) Integer receivedRoleId) {
        String start = startTime != null ? TimeUtil.format(startTime, BaseConstant.DATE_FORMAT2) : "";
        String end = endTime != null ? TimeUtil.format(endTime, BaseConstant.DATE_FORMAT2) : "";
        Integer receivedRoleId2 = null==receivedRoleId ? 0 : receivedRoleId;
        return clueReportService.echartsData(start, end, areaCode, reportRoleId, reportState, reportIds1, reportIds2,receivedRoleId2);
    }

    @GetMapping(value = "a/analysis/report0.html", produces = BaseConstant.HTML)
    public String analysisReport0(Model model) {
        model.addAttribute("areaList", townshipService.areaList());
        return "web/analysis/report0_index";
    }

    @ResponseBody
    @PostMapping(value = "a/analysis/report0Data.json", produces = BaseConstant.JSON)
    public String report0Data(@RequestParam(required = false) Long startTime,
                              @RequestParam(required = false) Long endTime,
                              @RequestParam(required = false) String areaCode,
                              @RequestParam(required = false) String reportRoleId,
                              @RequestParam(required = false) String reportState,
                              @RequestParam(required = false) String reportIds,
                              @RequestParam(required = false) Integer receivedRoleId) {
        String start = startTime != null ? TimeUtil.format(startTime, BaseConstant.DATE_FORMAT2) : "";
        String end = endTime != null ? TimeUtil.format(endTime, BaseConstant.DATE_FORMAT2) : "";
        Integer receivedRoleId2 = null==receivedRoleId ? 0 : receivedRoleId;
        return clueReportService.echartsReport0Data(start, end, areaCode, reportRoleId, reportState, reportIds,receivedRoleId2);
    }

    @GetMapping(value = "a/analysis/advice.html", produces = BaseConstant.HTML)
    public String analysisAdvice(Model model) {
        model.addAttribute("areaList", townshipService.areaList());
        return "web/analysis/advice_index";
    }

    @ResponseBody
    @PostMapping(value = "a/analysis/adviceData.json", produces = BaseConstant.JSON)
    public String adviceData(@RequestParam(required = false) Long startTime,
                             @RequestParam(required = false) Long endTime,
                             @RequestParam(required = false) String areaCode) {
        String start = startTime != null ? TimeUtil.format(startTime, BaseConstant.DATE_FORMAT2) : "";
        String end = endTime != null ? TimeUtil.format(endTime, BaseConstant.DATE_FORMAT2) : "";
        return incorruptAdviceService.echartsData(start, end, areaCode);
    }

    //添加举报统计
    @GetMapping(value = "a/analysis/report.html", produces = BaseConstant.HTML)
    public String analysisReport(Model model) {//取出镇和村列表
        model.addAttribute("areaList", townshipService.areaList());
        return "web/analysis/report_index";
    }

    @ResponseBody
    @PostMapping(value = "a/analysis/reportData.json", produces = BaseConstant.JSON)
    public String reportData(@RequestParam(required = false) Long startTime,
                             @RequestParam(required = false) Long endTime,
                             @RequestParam(required = false) String areaCode) {
        String start = startTime != null ? TimeUtil.format(startTime, BaseConstant.DATE_FORMAT2) : "";
        String end = endTime != null ? TimeUtil.format(endTime, BaseConstant.DATE_FORMAT2) : "";
        return revealInfoService.echartsData(start, end, areaCode);
    }

    @GetMapping(value = "a/analysis/task.html", produces = BaseConstant.HTML)
    public String analysisTask(Model model) {
        model.addAttribute("areaList", townshipService.areaList());
        return "web/analysis/task_index";
    }

    @ResponseBody
    @PostMapping(value = "a/analysis/taskData.json", produces = BaseConstant.JSON)
    public String taskData(@RequestParam(required = false) Long startTime,
                           @RequestParam(required = false) Long endTime,
                           @RequestParam(required = false) String areaCode) {
        String start = startTime != null ? TimeUtil.format(startTime, BaseConstant.DATE_FORMAT2) : "";
        String end = endTime != null ? TimeUtil.format(endTime, BaseConstant.DATE_FORMAT2) : "";
        return taskInfoService.echartsData(start, end, areaCode);
    }

}
