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
        <form class="form-horizontal" id="frm-search-patrolInfo">
          <!--查询栏目需要查询的东西-->
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
                <input type="hidden" id="startTime1" name="startTime1">
                <input type="hidden" id="endTime1" name="endTime1" >
              </div>
            </div>
            <div class="col-xs-6 col-sm-4 col-lg-3">
              <div class="form-group">
                <label class="col-sm-4 control-label">巡察名称</label>
                <div class="col-sm-7">
                  <div class="input-group">
                    <input type="text" class="form-control" id="patrolName"
                           name="patrolName"  placeholder="巡察名称"/>
                  </div>
                </div>
              </div>
            </div>
            <div class="col-xs-6 col-sm-4 col-lg-3">
              <div class="form-group">
                <label class="col-sm-4 control-label">巡察单位</label>
                <div class="col-sm-7">
                  <div class="input-group">
                    <input type="text" class="form-control" id="patrolUnit"
                           name="patrolUnit"  placeholder="巡察单位"/>
                  </div>
                </div>
              </div>
            </div>
           </div>
          <div class="text-center">
            <button id="btn-search-patrolInfo" class="btn btn-primary"><i class="fa fa-search"></i> 查询
            </button>
            <button type="reset" class="btn"><i class="fa fa-minus"></i> 重置</button>
          </div>
        </form>
      </div>
    </div>
 <!--新增、删除、刷新、页码-->
    <div class="table-responsive ibox-content">
      <!--新增、删除、刷新、页码-->
      <div class="clearfix" style="margin-bottom:10px;">
        <div class="pull-left">
          <#if (roleId==1008)> 
            <button type="button" class="btn btn-primary" id="btn-add-patrolInfo"><i
                      class="fa fa-plus"></i> 新增
            </button>
            <button type="button" class="btn btn-danger" id="btn-del-patrolInfo"><i
                      class="fa fa-remove"></i> 删除
            </button>
          </#if>
        </div>
        <div class="grid-o pull-right" id="bootgrid-toolbar"></div>
      </div>
      <!--table的列表出现-->
      <table id="table-patrolInfo"
             class="table table-condensed table-hover table-striped table-bordered no-margins">
        <thead>
        <tr>
          <th data-column-id="id" data-width="60px" data-formatter="no" data-sortable="false"
              data-identifier="true">序号
          </th>
          <th data-column-id="patrolName" data-order="desc" data-visible="true" data-sortable="true">
            巡察名称
          </th>
          <th data-column-id="patrolUnit" data-order="desc" data-visible="true" data-sortable="true">
            巡察单位
          </th>
          <th data-column-id="createTime" data-order="desc" data-visible="true"
              data-sortable="true" data-formatter="fun_date">巡察时间
          </th>
          <th data-column-id="imageUrl" data-order="desc" data-visible="true"
              data-sortable="true" data-formatter="fun_url">二维码链接
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
<!--巡察列表，操作列表，查看和删除-->
<script id="template-patrolInfo" type="text/x-handlebars-template">
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

<!--点击查看按钮，进行的操作-->
<script id="template-view-patrolInfo" type="text/x-handlebars-template">
  <div class="form-horizontal" style="margin: 20px;">
    <div class="form-group">
      <label class="col-xs-4 control-label">巡察名称：</label>
      <div class="col-xs-8">
        <div class="form-control-static">{{patrolName}}</div>
      </div>
    </div>
    <div class="form-group">
      <label class="col-xs-4 control-label">巡察单位：</label>
      <div class="col-xs-8">
        <div class="form-control-static">{{patrolUnit}}</div>
      </div>
    </div>
    <div class="form-group">
      <label class="col-xs-4 control-label">开始时间：</label>
      <div class="col-xs-8">
        <div class="form-control-static">{{formatDate startTime "yyyy-MM-dd"}}</div>
      </div>
    </div>
    <div class="form-group">
      <label class="col-xs-4 control-label">结束时间：</label>
      <div class="col-xs-8">
        <div class="form-control-static">{{formatDate endTime "yyyy-MM-dd"}}</div>
      </div>
    </div>
    <div class="form-group">
      <label class="col-xs-4 control-label">二维码图：</label>
      <div class="col-xs-8">
        <div class="form-control-static"><img src="${global.preUrl2!}{{imageUrl}}"></div>
      </div>
    </div>
  </div>
</script>
<script>
  function assignRevealTask(obj){
    openLayer({
      "type": 2,
      "title": "二维码",
      "area": ["360px", "400px"],
      "maxmin": false,
      "content": "a/patrolInfo/showEwm.html?url="+obj,
      "btn": ["关闭"]
    });
  }
  //时间插件
  laydate.render({
    "elem": "#timeRange",
    "range": "至",
    "format": "yyyy-MM-dd",
    "trigger": "click",
    "done": function (value, startDate, endDate) {
      if (value) {
        const array = value.split("至");
        $("#startTime1").val(array[0]);
        $("#endTime1").val(array[1]);
      } else {
        $("#startTime1").val("");
        $("#endTime1").val("");
      }
    }
  });

  $(document).ready(function () {

    var HBR_TD = Handlebars.compile($("#template-patrolInfo").html());
    var pageParams = {
      <#-- 模块id，最重要的属性 -->
      "moduleName": "patrolInfo",
      <#-- 弹窗区域，都是100%，是不显示最大化按钮的 -->
      "area": ["60%", "600px"],
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
        "fun_date": function (rolumn, row) {
          return new Date(row.startTime).Format("yyyy-MM-dd")+' 至 '+new Date(row.endTime).Format("yyyy-MM-dd");
        },
        "fun_url":function(rolumn,row){ //暂时先做成新的弹窗
          var url="\""+row.imageUrl+"\"";
          return "<a  onclick='assignRevealTask("+url+");'  href='javascript:void(0);' >" + row.imageUrl + "<a/>";
        }
      },
      <#-- 列表自定义提交数据的附加参数 -->
      "otherParams": {}
    };


    <#-- view的本页渲染的模板 -->
    var HBR_VIEW = Handlebars.compile($("#template-view-patrolInfo").html());
    <#-- 列表ajax加载，绑定删除和编辑之外的其他按钮 -->

    function bindRowEvent() {
      <#-- 绑定查看，本例是用的本页渲染，也可以使用单独的页面 -->
      <#-- CommonGetData定义在main-list.js -->
      $("#table-patrolInfo .ops-view").on("click", function () {
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
