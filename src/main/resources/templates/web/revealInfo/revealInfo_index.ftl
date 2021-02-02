<#include "/common/head.ftl"/>
<#include "/common/cssfile_list.ftl"/>
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
        <form class="form-horizontal" id="frm-search-revealInfo">
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
                    <option value="1">已分配</option>
                    <option value="2">已知晓</option>
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
                  <select id="village" class="form-control" onchange="choseArea(this);">
                    <option value="">全部</option>
                  </select>
                </div>
              </div>
            </div>
          </div>
          <div class="text-center">
            <button id="btn-search-revealInfo" class="btn btn-primary"><i class="fa fa-search"></i> 查询
            </button>
            <button type="reset" class="btn"><i class="fa fa-minus"></i> 重置</button>
          </div>
        </form>
      </div>
    </div>
    <div class="table-responsive ibox-content">
      <div class="clearfix" style="margin-bottom:10px;">
        <div class="pull-left">
          <!--
          <button type="button" class="btn btn-primary" id="btn-add-revealInfo"><i
                class="fa fa-plus"></i> 新增
          </button>
          <button type="button" class="btn btn-danger" id="btn-del-revealInfo"><i
                class="fa fa-remove"></i> 删除
          </button>
          -->
        </div>
        <div class="grid-o pull-right" id="bootgrid-toolbar"></div>
      </div>
      <table id="table-revealInfo"
             class="table table-condensed table-hover table-striped table-bordered no-margins">
        <thead>
        <tr>
          <th data-column-id="ID" data-width="60px" data-formatter="no" data-sortable="false"
              data-identifier="true">序号
          </th>
          <th data-column-id="patrolName" data-order="desc" data-visible="true" data-sortable="true">
            巡察轮次
          </th>
          <th data-column-id="userName" data-order="desc" data-visible="true" data-sortable="true">
            举报人
          </th>
          <th data-column-id="roleId" data-order="desc" data-formatter="fun_revealRoleId"
              data-visible="true" data-sortable="true">
            举报人角色
          </th>
          <th data-column-id="content" data-order="desc" data-visible="true" data-sortable="true">
            举报内容
          </th>
          <th data-column-id="createTime" data-order="desc" data-visible="true"
              data-sortable="true" data-formatter="fun_date">举报时间
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
<script id="template-revealInfo" type="text/x-handlebars-template">
  <td data-id="{{id}}">
    <button class="btn btn-primary btn-xs o-ch ops-view"><i class="fa fa-eye"></i> 查看</button>
    <!--添加角色判定，
    1.对于分配任务，只有管理员和巡察办主任角色有这个按钮权限
    2.对于已知晓，只要是0和1状态都可以点击。对于管理员和巡察办角色。如果为2，则不存在。
    3.对于巡察组而言，只有已知晓按钮，给到他的肯定是已经1的状态，则只有是1的时候，出现已知晓。
    -->
    <#if roleIds == 1007 || roleIds == 1008> <!--添加角色判定，只有管理员和巡察办主任角色有这个按钮权限-->
      {{#ifEqual state 0}}
      <button class="btn btn-success btn-xs mg-5 o-c" onclick="assignRevealTask(this)"><i class="fa fa-user"></i>&nbsp;分配任务</button>
      <button class="btn btn-success btn-xs mg-5 o-c" onclick="knowTask(this)"><i class="fa fa-user"></i>&nbsp;已知晓</button>
      {{/ifEqual}}
      {{#ifEqual state 1}}
      <button class="btn btn-success btn-xs mg-5 o-c" onclick="knowTask(this)"><i class="fa fa-user"></i>&nbsp;已知晓</button>
      {{/ifEqual}}
      {{#fun_equals_one rowLocked}}
      <button class="btn btn-xs o-d" disabled="disabled"><i class="fa fa-close"></i> 删除</button>
      {{else}}
      <button class="btn btn-danger btn-xs o-d ops-del"><i class="fa fa-close"></i> 删除</button>
      {{/fun_equals_one}}
    </#if>
    <#if roleIds == 1009 || roleIds == 1010 || roleIds == 1011>
      {{#ifEqual state 1}}
      <button class="btn btn-success btn-xs mg-5 o-c" onclick="knowTask(this)"><i class="fa fa-user"></i>&nbsp;已知晓</button>
      {{/ifEqual}}
    </#if>
  </td>
</script>
<script id="template-villages" type="text/x-handlebars-template">
  <option value="">--请选择--</option>
  {{#each this}}
  <option value="{{areaCode}}">{{areaName}}</option>
  {{/each}}
</script>

<script id="template-view-revealInfo" type="text/x-handlebars-template">
  <div class="form-horizontal" style="margin: 20px;">
    <div class="form-group">
      <label class="col-xs-4 control-label">举报人：</label>
      <div class="col-xs-8">
        <div class="form-control-static">{{userName}}</div>
      </div>
    </div>
    <div class="form-group">
      <label class="col-xs-4 control-label">举报人角色：</label>
      <div class="col-xs-8">
        <div class="form-control-static">
          {{#ifEqual roleId 1001}}村（社）网格员{{/ifEqual}}
          {{#ifEqual roleId 1002}}纪检监察网格联络员{{/ifEqual}}
          {{#ifEqual roleId 1003}}乡镇纪委管理员{{/ifEqual}}
          {{#ifEqual roleId 1004}}县纪委联系室/巡察办{{/ifEqual}}
          {{#ifEqual roleId 1005}}分管领导{{/ifEqual}}
          {{#ifEqual roleId 1006}}县级领导班子{{/ifEqual}}
          {{#ifEqual roleId 1007}}超级管理员{{/ifEqual}}
          {{#ifEqual roleId 1008}}巡察办主任{{/ifEqual}}
          {{#ifEqual roleId 1009}}巡察一组{{/ifEqual}}
          {{#ifEqual roleId 1010}}巡察二组{{/ifEqual}}
          {{#ifEqual roleId 1011}}巡察三组{{/ifEqual}}
          {{#ifEqual roleId 2000}}群众{{/ifEqual}}
        </div>
      </div>
    </div>

    <div class="form-group">
      <label class="col-xs-4 control-label">乡镇：</label>
      <div class="col-xs-8">
        <div class="form-control-static">{{townName}}</div>
      </div>
    </div>

    <div class="form-group">
      <label class="col-xs-4 control-label">村：</label>
      <div class="col-xs-8">
        <div class="form-control-static">{{villageName}}</div>
      </div>
    </div>

    <div class="form-group">
      <label class="col-xs-4 control-label">举报类型：</label>
      <div class="col-xs-8">
        <div class="form-control-static" data-formatter="fun_type">
          {{#ifEqual type 0}}匿名{{/ifEqual}}
          {{#ifEqual type 1}}实名{{/ifEqual}}
        </div>
      </div>
    </div>
    <div class="form-group">
      <label class="col-xs-4 control-label">举报对象：</label>
      <div class="col-xs-8">
        <div class="form-control-static">{{revealTarget}}</div>
      </div>
    </div>
    <div class="form-group">
      <label class="col-xs-4 control-label">举报内容：</label>
      <div class="col-xs-8">
        <div class="form-control-static">{{content}}</div>
      </div>
    </div>
    <div class="form-group">
      <label class="col-xs-4 control-label">举报时间：</label>
      <div class="col-xs-8">
        <div class="form-control-static">{{formatDate createTime "yyyy-MM-dd h
          h:mm:ss"}}</div>
      </div>
    </div>
    <div class="form-group">
      <label class="col-xs-4 control-label">图片内容：</label>
      <div class="col-xs-8">
        <div class="form-control-static">
          <!--图片样式是否要调整-->
          {{#each imgApps}}
          <img src="${global.preUrl!}{{imageUrl}}" width="200" height="200"/>
          </tr>
          {{/each}}
        </div>
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
            {{#ifEqual state 1}}已分配{{/ifEqual}}
            {{#ifEqual state 2}}已知晓{{/ifEqual}}
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

  function assignRevealTask(obj) {
    obj = $(obj);
    var revealId = obj.parent().attr("data-id");
    openLayer({
      "type": 2,
      "title": "分配举报内容",
      "area": ["400px", "500px"],
      "maxmin": false,
      "content": "${mapping!}/tree.html?uid=" + revealId,
      "yes": function (index, layero) {
        // 调用子窗口的添加方法
        layero.find("iframe")[0].contentWindow.subSave();
      }
    });
  }

  function knowTask(obj) {
    obj = $(obj);
    var revealId = obj.parent().attr("data-id");
    //直接用ajax处理流程返回就可以了。
    openLayer({
      "type": 2,
      "title": "分配举报内容",
      "area": ["400px", "500px"],
      "maxmin": false,
      "content": "${mapping!}/knowTask.html?uid=" + revealId,
      "yes": function (index, layero) {
        // 调用子窗口的添加方法
        layero.find("iframe")[0].contentWindow.subSave();
      }
    });
  }


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
        $("#village").html(template(data.result));
      }
    });
  }

  function choseArea(obj) {
    var code = $(obj).val();
    if (code == '') {
      code = $("#township").val();
    }
    $("#areaCode").val(code);
    $("#villageCode").val(code);
  }

  $(document).ready(function () {
    var HBR_TD = Handlebars.compile($("#template-revealInfo").html());
    var pageParams = {
      <#-- 模块id，最重要的属性 -->
      "moduleName": "revealInfo",
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
            return "已分配";
          } else if (state == 2) {
            return "已知晓";
          }
        },

        "fun_type": function (rolumn, row) {
          var type = row.type;
          if (type == 0) {
            return "匿名";
          } else if (type == 1) {
            return "实名";
          }
        },
        "fun_revealRoleId": function (rolumn, row) {
          var roleId = row.roleId;
          if (roleId == "1001") {
            return "网格员";
          } else if (roleId == "1002") {
            return "联络员";
          } else if (roleId == "1003") {
            return "乡镇纪委管理员";
          } else if (roleId == "1004") {
            return "县纪委联系室/巡察办";
          } else if (roleId == "1005") {
            return "分管领导";
          } else if (roleId == "1006") {
            return "县级领导班子";
          } else if (roleId == "1007") {
            return "超级管理员"
          } else if (roleId == "1008") {
            return "巡察办主任";
          } else if (roleId == "1009") {
            return "巡察一组";
          } else if (roleId == "1010") {
            return "巡察二组";
          } else if (roleId == "1011") {
            return "巡察三组";
          } else if (roleId == "2000") {
            return "群众";
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
    var HBR_VIEW = Handlebars.compile($("#template-view-revealInfo").html());
    <#-- 列表ajax加载，绑定删除和编辑之外的其他按钮 -->

    function bindRowEvent() {
      <#-- 绑定查看，本例是用的本页渲染，也可以使用单独的页面 -->
      <#-- CommonGetData定义在main-list.js -->
      $("#table-revealInfo .ops-view").on("click", function () {
        var id = $(this).parent().attr("data-id");
        CommonGetData(pageParams.url.view, {
          "id": id
        }, function (result) {
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
