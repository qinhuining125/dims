<#include "/common/head.ftl"/>
<#include "/common/cssfile_list.ftl"/>
<style>
  .column-header-anchor {
    cursor: default !important;
  }
</style>
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content row">
  <div class="col-sm-12">
    <div class="ibox">
      <div class="ibox-title">
        <h5>查询</h5>
        <div class="ibox-tools"><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></div>
      </div>
      <div class="ibox-content">
        <form class="form-horizontal" id="frm-search-sysLog">
          <div class="row">
            <div class="col-xs-6 col-sm-4 col-lg-3">
              <div class="form-group">
                <label class="col-sm-4 control-label">状态：</label>
                <div class="col-sm-8">
                  <select name="status" class="form-control">
                    <option value="">全部</option>
                    <option value="1">成功</option>
                    <option value="0">失败</option>
                  </select>
                </div>
              </div>
            </div>
            <div class="col-xs-6 col-sm-4 col-lg-4">
              <div class="form-group">
                <label class="col-sm-4 control-label">类型：</label>
                <div class="col-sm-8">
                  <select name="type" class="form-control">
                    <option value="">全部</option>
                      <#list logType as type>
                        <option value="${type}">${type}</option>
                      </#list>
                  </select>
                </div>
              </div>
            </div>
            <div class="col-xs-12 col-sm-4 col-lg-5">
              <div class="form-group">
                <label class="col-sm-4 control-label">时间：</label>
                <div class="col-sm-8">
                  <div class="input-group">
                    <input type="text" class="form-control" id="iptStart"
                           readonly="readonly" placeholder="调用时间"/>
                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span></div>
                </div>
              </div>
            </div>
          </div>
          <div class="text-center">
            <button id="btn-search-sysLog" class="btn btn-primary"><i class="fa fa-search"></i>&nbsp;查询
            </button>
            <button type="reset" class="btn"><i class="fa fa-remove"></i>&nbsp;重置</button>
          </div>
          <input type="text" style="display:none" id="startTime" name="startTime"/>
          <input type="text" style="display:none" id="endTime" name="endTime"/>
        </form>
      </div>
    </div>
    <div class="table-responsive ibox-content">
      <div class="clearfix" style="margin-bottom:10px;">
        <div class="pull-left">
          <a class="btn btn-primary" href="a/sysLog/downloadLog.html" target="_blank"><i
                class="fa fa-download mr-2"></i>下载错误日志
          </a>
          <button type="button" class="btn btn-danger" onclick="removeAll()"><i
                class="fa fa-remove mr-2"></i>清空日志
          </button>
        </div>
        <div class="grid-o pull-right" id="bootgrid-toolbar"></div>
      </div>
      <table id="table-sysLog"
             class="table table-condensed table-hover table-striped table-bordered no-margins">
        <thead>
        <tr>
          <th data-column-id="id" data-width="60px" data-formatter="no" data-sortable="false"
              data-identifier="false">序号
          </th>
          <th data-column-id="status" data-formatter="fun_status" data-width="60px"
              data-order="desc" data-visible="true" data-sortable="false">状态
          </th>
          <th data-column-id="type" data-order="desc" data-visible="true" data-sortable="false">类型
          </th>
          <th data-column-id="userId" data-order="desc" data-visible="true"
              data-sortable="false">操作用户
          </th>
          <th data-column-id="uri" data-order="desc" data-visible="true" data-sortable="false">路径
          </th>
          <th data-column-id="startTime" data-width="180px" data-order="desc"
              data-formatter="fun_timer" data-visible="true" data-sortable="false">调用时间
          </th>
          <th data-column-id="runTime" data-formatter="fun_time_run" data-width="80px"
              data-order="desc" data-visible="true" data-sortable="false">耗时
          </th>
          <th data-column-id="method" data-width="80px" data-order="desc" data-visible="true"
              data-sortable="false">方法
          </th>
          <th data-column-id="description" data-order="desc" data-visible="true"
              data-sortable="false">描述
          </th>
          <th data-column-id="link" data-width="70px" data-formatter="commands" data-custom="true"
              data-sortable="false">操作
          </th>
        </tr>
        </thead>
      </table>
    </div>
  </div>
</div>
<#include "/common/scriptfile.ftl"/>
<#include "/common/scriptfile_list.ftl"/>
<script src="${global.staticPath!}static/plugins/laydate/laydate.js"></script>
<script id="template-sysLog" type="text/x-handlebars-template">
  <td data-id="{{userAgent}}">
    <button class="btn btn-primary btn-xs o-ch" onclick="showInfo(this)"><i class="fa fa-eye"></i>&nbsp;查看
    </button>
  </td>
</script>
<script>
  $(document).ready(function () {
    var HBR_TD = Handlebars.compile($("#template-sysLog").html());
    var pageParams = {
      <#-- 模块id，最重要的属性 -->
      "moduleName": "sysLog",
      <#-- 弹窗区域，都是100%，是不显示最大化按钮的 -->
      "area": ["700px", "550px"],
      "url": {
        "list": "a/sysLog/getDataList.json"
      },
      "formatters": {
        "no": function (column, row) {
          return row.idx;
        },
        "fun_status": function (column, row) {
          return (1 === row.status) ? "<span class=\"label label-primary\">成功</span>" :
              "<span class=\"label label-danger\">失败</span>";
        },
        "commands": function (column, row) {
          return HBR_TD(row);
        },
        "fun_timer": function (column, row) {
          return new Date(row.startTime).Format("yyyy-MM-dd hh:mm:ss");
        },
        "fun_time_run": function (column, row) {
          if (!!row.runTime) {
            return (row.runTime / 1000.0).toFixed(1) + "秒";
          }
          return "";
        }
      },
      <#-- 列表自定义提交数据的附加参数 -->
      "otherParams": {}
    };
    <#-- 列表ajax加载，绑定删除和编辑之外的其他按钮 -->

    function bindRowEvent() {
      <#-- 绑定查看，本例是用的本页渲染，也可以使用单独的页面 -->
    }

    <#-- 通用功能的入口 -->
    CommonListFun(pageParams, bindRowEvent);

    laydate.render({
      "elem": "#iptStart",
      "range": "至",
      "format": "yyyy-MM-dd",
      "min": (new Date(${firstTime!"0"})).Format("yyyy-MM-dd"),
      "max": (new Date()).Format("yyyy-MM-dd"),
      "done": function (value, date, endDate) {
        date && date.year && $("#startTime").val(+new Date(date.year, date.month - 1, date.date));
        endDate && endDate.year && $("#endTime").val(
            +new Date(endDate.year, endDate.month - 1, endDate.date));
      }
    });
  });

  function showInfo(obj) {
    var id = $(obj).parent().attr("data-id");
    openLayer({
      "type": 2,
      "title": "详细日志",
      "area": ["600px", "550px"],
      "maxmin": true,
      "content": "a/sysLog/info.html?id=" + id
    });
  }

  function removeAll() {
    CommonUtils.LayerUtil.showConfirm("温馨提示", "是否清空日志记录？", function (fun) {
      //点yes后，关闭confirm窗口
      fun && fun();
      //打开loading窗口，1-读取，2-提交
      var frmIdx = CommonUtils.LayerUtil.showLoading(2, "正在清空日志，请耐心等候...");
      $.ajax({
        "url": "a/sysLog/removeAll.json",
        "type": "POST",
        "dataType": "json",
        "success": function (data) {
          top.CommonUtils.ToastrUtil.success("清除成功");
          self.window.location.reload();
        },
        "error": function (req, textStatus, errorThrown) {

        },
        "complete": function () {
          //关闭loading窗口
          CommonUtils.LayerUtil.close(frmIdx);
        }
      });
    });

  }
</script>
</body>
</html>
