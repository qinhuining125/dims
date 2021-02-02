<#include "/common/head.ftl"/>
<#include "/common/cssfile_list.ftl"/>
<style>
  .table-responsive .bootgrid-table td, .table-responsive .bootgrid-table th>.column-header-anchor>.text{
    overflow: hidden !important;
    text-overflow: ellipsis !important;
    white-space: nowrap !important;
    -moz-text-overflow: ellipsis !important;
    　　　　-o-text-overflow: ellipsis !important;
    　　　　-webkit-text-overflow: ellipsis !important;
  }
  .table-responsive .bootgrid-table{
    table-layout:fixed!important;
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
        <form class="form-horizontal" id="frm-search-taskInfo">
          <div class="row">
            <div class="col-xs-6 col-sm-4 col-lg-3">
              <div class="form-group">
                <label class="col-sm-4 control-label">时间段：</label>
                <div class="col-sm-7">
                  <div class="input-group">
                    <input type="text" class="form-control" id="timeRange"
                           readonly="readonly" placeholder="日期"/>
                  </div>
                </div>
                <input type="hidden" id="startTime" name="startTime">
                <input type="hidden" id="endTime" name="endTime">
                <input type="hidden" id="areaCode" name="areaCode">
              </div>
            </div>
            <div class="col-xs-6 col-sm-4 col-lg-3">
              <div class="form-group">
                <label class="col-sm-4 control-label">状态</label>
                <div class="col-sm-7">
                  <select name="state" class="form-control">
                    <option value="">全部</option>
                    <option value="0">未处理</option>
                    <option value="1">已受理</option>
                    <option value="2">已办结</option>
                  </select>
                </div>
              </div>
            </div>
            <div class="col-xs-6 col-sm-4 col-lg-3">
              <div class="form-group">
                <label class="col-sm-4 control-label">乡镇:</label>
                <div class="col-sm-7">
                  <select class="form-control" id="township" onchange="searchVillage(this);">
                    <option value="">全部</option>
                    <#list areaList as obj>
                      <option value="${(obj.areaCode!)}">${(obj.areaName)!}</option>
                    </#list>
                  </select>
                </div>
              </div>
            </div>
            <div class="col-xs-6 col-sm-4 col-lg-3">
              <div class="form-group">
                <label class="col-sm-4 control-label">村:</label>
                <div class="col-sm-7">
                  <select id="valliage" class="form-control" onchange="choseArea(this);">
                    <option value="">全部</option>
                  </select>
                </div>
              </div>
            </div>
          </div>
          <div class="text-center">
            <button id="btn-search-taskInfo" class="btn btn-primary"><i class="fa fa-search"></i> 查询
            </button>
            <button type="reset" class="btn"><i class="fa fa-minus"></i> 重置</button>
          </div>
        </form>
      </div>
    </div>
    <div class="table-responsive ibox-content">
      <div class="clearfix" style="margin-bottom:10px;">
        <div class="pull-left">
          <#if (roleId==1008)> 
          <button type="button" class="btn btn-primary" id="btn-add-taskInfo"><i
                    class="fa fa-plus"></i> 新增
          </button>
          <button type="button" class="btn btn-danger" id="btn-del-taskInfo"><i
                    class="fa fa-remove"></i> 删除
          </button>
          </#if>

        </div>

        <div class="grid-o pull-right" id="bootgrid-toolbar"></div>
      </div>
      <table id="table-taskInfo" style="table-layout:fixed; "
             class="table table-condensed table-hover table-striped table-bordered no-margins">
        <thead>
        <tr>
          <th data-column-id="id" data-width="60px" data-formatter="no" data-sortable="false"
              data-identifier="true">序号
          </th>
          <th data-column-id="userName" data-order="desc" data-visible="true" data-sortable="true">
            用户
          </th>
          <th width="30px" data-column-id="content"  data-order="desc" data-visible="true" data-sortable="true">
            内容
          </th>
          <th data-column-id="createTime" data-order="desc" data-visible="true"
              data-sortable="true" data-formatter="fun_date">任务时间
          </th>
          <th data-column-id="state" data-order="desc" data-formatter="fun_state"
              data-visible="true" data-sortable="true">
            状态
          </th>
          <th data-column-id="link" data-width="180px" data-formatter="commands" data-custom="true"
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
<script src="${global.staticPath!}/static/utils/handlebars-tool.js"></script>
<script id="template-taskInfo" type="text/x-handlebars-template">
  <td data-id="{{id}}">
    <button class="btn btn-primary btn-xs o-ch ops-view"><i class="fa fa-eye"></i> 查看</button>
    <#--<button class="btn btn-warning btn-xs o-c ops-edit"><i class="fa fa-edit"></i> 修改</button>-->
    {{#fun_equals_one rowLocked}}
    <button class="btn btn-xs o-d" disabled="disabled"><i class="fa fa-close"></i> 删除</button>
    {{else}}
    <button class="btn btn-danger btn-xs o-d ops-del"><i class="fa fa-close"></i> 删除</button>
    {{/fun_equals_one}}
  </td>
</script>
<script id="template-villages" type="text/x-handlebars-template">
  <option value="">--请选择--</option>
  {{#each this}}
  <option value="{{areaCode}}">{{areaName}}</option>
  {{/each}}
</script>

<script id="template-view-taskInfo" type="text/x-handlebars-template">
  <div class="form-horizontal" style="margin: 20px;">
    <div class="form-group">
      <label class="col-xs-4 control-label">用户：</label>
      <div class="col-xs-8">
        <div class="form-control-static">{{userName}}</div>
      </div>
    </div>
    <div class="form-group">
      <label class="col-xs-4 control-label">内容：</label>
      <div class="col-xs-8">
        <div class="form-control-static">{{content}}</div>
      </div>
    </div>
    <div class="form-group">
      <label class="col-xs-4 control-label">任务时间：</label>
      <div class="col-xs-8">
        <div class="form-control-static">{{formatDate createTime "yyyy-MM-dd hh:mm:ss"}}</div>
      </div>
    </div>
    <div class="form-group">
      <label class="col-xs-4 control-label">二维码：</label>
      <div id="localImag">
        <img src="{{qR_Code}}" width="200" height="200">
      </div>
    </div>
    <div class="form-group">
      <label class="col-xs-4 control-label">图片：</label>
      <div id="localImag">
        {{#each img}}
        <img src="${global.preUrl!}{{imageURL}}" width="200" height="200" />
        {{/each}}
        <img id="preview" width=-1 height=-1 style="diplay:none" />
      </div>
    </div>
    <table class="table table-condensed table-hover table-striped table-bordered no-margins">
      <thead>
      <tr>
        <td>接受人员</td>
        <td>状态</td>
        <td>反馈意见</td>
        <td>时间</td>
      </tr>
      </thead>
      <tbody>
      {{#each flows}}
      <tr>
        <td>
          <div class="form-control-static">{{receiveName}}</div>
        </td>
        <td>
          <div class="form-control-static">
            {{#ifEqual state 0}}未处理{{/ifEqual}}
            {{#ifEqual state 1}}已受理{{/ifEqual}}
            {{#ifEqual state 2}}已办结{{/ifEqual}}
            {{#ifEqual state 3}}已指派{{/ifEqual}}
          </div>
        </td>
        <td>
          <div class="form-control-static">{{remark}}</div>
        </td>
        <td>
          <div class="form-control-static">{{formatDate createTime "yyyy-MM-dd hh:mm:ss"}}</div>
        </td>
      </tr>
      {{/each}}
      </tbody>
    </table>
  </div>
</script>
<script>
  laydate.render({
    "elem": "#timeRange",
    "range": "至",
    "format": "yyyy-MM-dd",
    "trigger": "click",
    "done": function (value, startDate, endDate) {
      if (value) {
        const array = value.split("至");
        $("#startTime").val(array[0]);
        $("#endTime").val(array[1]);
      } else {
        $("#startTime").val("");
        $("#endTime").val("");
      }
    }
  });

  function searchVillage(obj) {
    const township = $(obj).val();
    $("#receiveId").val(township);
    if (township == '') {
      return false;
    }
    $.ajax({
      "type": "GET",
      "cache": false,
      "url": "/a/village/list.json",
      "data": {
        "areaCode": township
      },
      "dataType": "json",
      "success": function (data) {
        const template = Handlebars.compile($("#template-villages").html());
        $("#valliage").html(template(data.result));
      }
    });
  }

  function choseArea(obj) {
    var code = $(obj).val();
    if (code == '') {
      code = $("#township").val();
    }
    $("#areaCode").val(code);
  }

  $(document).ready(function () {
    var HBR_TD = Handlebars.compile($("#template-taskInfo").html());
    var pageParams = {
      <#-- 模块id，最重要的属性 -->
      "moduleName": "taskInfo",
      <#-- 弹窗区域，都是100%，是不显示最大化按钮的 -->
      "area": ["60%", "500px"],
      "url": {
        "list": "${mapping!}/getDataList.json",
        "delete": "${mapping!}/deleteData.json",
        <#-- 会自动把id参数写入 -->
        "view": "${mapping!}/getDataInfo.json",
        "edit": function (id) {
          return "${mapping!}/edit.html?id=" + id;
        }
      },
      <#-- 这里列表的行渲染方法，配置data-formatter="..." -->
      "formatters": {
        "no": function (column, row) {
          return row.idx;
        },
        "commands": function (column, row) {
          return HBR_TD(row);
        },
        "fun_state": function (rolumn, row) {
          var state = row.state;
          if (state == 0) {
            return "未处理";
          } else if (state == 1 || state == 3) {
            return "已受理";
          } else if (state == 2) {
            return "已办结";
          }
        },
        "fun_date": function (rolumn, row) {
          return new Date(row.createTime).Format("yyyy-MM-dd hh:mm:ss");
        }
      },
      <#-- 列表自定义提交数据的附加参数 -->
      "otherParams": {}
    };
    <#-- view的本页渲染的模板 -->
    var HBR_VIEW = Handlebars.compile($("#template-view-taskInfo").html());
    <#-- 列表ajax加载，绑定删除和编辑之外的其他按钮 -->

    function bindRowEvent() {
      <#-- 绑定查看，本例是用的本页渲染，也可以使用单独的页面 -->
      <#-- CommonGetData定义在main-list.js -->
      $("#table-taskInfo .ops-view").on("click", function () {
        var id = $(this).parent().attr("data-id");
        CommonGetData(pageParams.url.view, {
          "id": id
        }, function (result) {
          console.log(result)
          openLayer({
            "title": "查看",
            "btn": ["关闭"],
            "content": HBR_VIEW(result)
          });
          if ($.isFunction(top.clearIframeFocus)) {
            top.clearIframeFocus();
          }
        });
      });
    }

    <#-- 通用功能的入口 -->
    CommonListFun(pageParams, bindRowEvent);
  });
</script>
</body>
</html>
