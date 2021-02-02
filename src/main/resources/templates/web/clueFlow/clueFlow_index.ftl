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
        <form class="form-horizontal" id="frm-search-clueFlow">
          <div class="row">
             <div class="col-xs-6 col-sm-4 col-lg-3">
              <div class="form-group">
                <label class="col-sm-4 control-label">上报线索id：</label>
                <div class="col-sm-7">
                  <input type="text" name="clueId" placeholder="搜索上报线索id" class="form-control" />
                </div>
              </div>
            </div>
             <div class="col-xs-6 col-sm-4 col-lg-3">
              <div class="form-group">
                <label class="col-sm-4 control-label">状态1,受理，2办结：</label>
                <div class="col-sm-7">
                  <input type="text" name="state" placeholder="搜索状态1,受理，2办结" class="form-control" />
                </div>
              </div>
            </div>
             <div class="col-xs-6 col-sm-4 col-lg-3">
              <div class="form-group">
                <label class="col-sm-4 control-label">受理人id：</label>
                <div class="col-sm-7">
                  <input type="text" name="receiveId" placeholder="搜索受理人id" class="form-control" />
                </div>
              </div>
            </div>
             <div class="col-xs-6 col-sm-4 col-lg-3">
              <div class="form-group">
                <label class="col-sm-4 control-label">备注：</label>
                <div class="col-sm-7">
                  <input type="text" name="remark" placeholder="搜索备注" class="form-control" />
                </div>
              </div>
            </div>
             <div class="col-xs-6 col-sm-4 col-lg-3">
              <div class="form-group">
                <label class="col-sm-4 control-label">时间：</label>
                <div class="col-sm-7">
                  <input type="text" name="createTime" placeholder="搜索时间" class="form-control" />
                </div>
              </div>
            </div>
          </div>
          <div class="text-center">
            <button id="btn-search-clueFlow" class="btn btn-primary"><i class="fa fa-search"></i> 查询</button>
            <button type="reset" class="btn"><i class="fa fa-minus"></i> 重置</button>
          </div>
        </form>
      </div>
    </div>
    <div class="table-responsive ibox-content">
      <div class="clearfix" style="margin-bottom:10px;">
        <div class="pull-left">
          <button type="button" class="btn btn-primary" id="btn-add-clueFlow"><i class="fa fa-plus"></i> 新增</button>
          <button type="button" class="btn btn-danger" id="btn-del-clueFlow"><i class="fa fa-remove"></i> 删除</button>
        </div>
        <div class="grid-o pull-right" id="bootgrid-toolbar"></div>
      </div>
      <table id="table-clueFlow" class="table table-condensed table-hover table-striped table-bordered no-margins">
        <thead>
        <tr>
          <th data-column-id="ID" data-width="60px" data-formatter="no" data-sortable="false" data-identifier="true">序号</th>
           <th data-column-id="clueId" data-order="desc" data-visible="true" data-sortable="true">上报线索id</th>
           <th data-column-id="state" data-order="desc" data-visible="true" data-sortable="true">状态1,受理，2办结</th>
           <th data-column-id="receiveId" data-order="desc" data-visible="true" data-sortable="true">受理人id</th>
           <th data-column-id="remark" data-order="desc" data-visible="true" data-sortable="true">备注</th>
           <th data-column-id="createTime" data-order="desc" data-visible="true" data-sortable="true">时间</th>
          <th data-column-id="link" data-width="180px" data-formatter="commands" data-custom="true" data-sortable="false">操作</th>
        </tr>
        </thead>
      </table>
    </div>
  </div>
</div>
<#include "/common/scriptfile.ftl"/>
<#include "/common/scriptfile_list.ftl"/>
<script id="template-clueFlow" type="text/x-handlebars-template">
  <td data-id="{{id}}">
    <button class="btn btn-primary btn-xs o-ch ops-view"><i class="fa fa-eye"></i> 查看</button>
    <button class="btn btn-warning btn-xs o-c ops-edit"><i class="fa fa-edit"></i> 修改</button>
    {{#fun_equals_one rowLocked}}
    <button class="btn btn-xs o-d" disabled="disabled"><i class="fa fa-close"></i> 删除</button>
    {{else}}
    <button class="btn btn-danger btn-xs o-d ops-del"><i class="fa fa-close"></i> 删除</button>
    {{/fun_equals_one}}
  </td>
</script>
<script id="template-view-clueFlow" type="text/x-handlebars-template">
  <div class="form-horizontal" style="margin: 20px;">
     <div class="form-group">
      <label class="col-xs-4 control-label">上报线索id：</label>
      <div class="col-xs-8">
        <div class="form-control-static">{{clueId}}</div>
      </div>
    </div>
     <div class="form-group">
      <label class="col-xs-4 control-label">状态1,受理，2办结：</label>
      <div class="col-xs-8">
        <div class="form-control-static">{{state}}</div>
      </div>
    </div>
     <div class="form-group">
      <label class="col-xs-4 control-label">受理人id：</label>
      <div class="col-xs-8">
        <div class="form-control-static">{{receiveId}}</div>
      </div>
    </div>
     <div class="form-group">
      <label class="col-xs-4 control-label">备注：</label>
      <div class="col-xs-8">
        <div class="form-control-static">{{remark}}</div>
      </div>
    </div>
     <div class="form-group">
      <label class="col-xs-4 control-label">时间：</label>
      <div class="col-xs-8">
        <div class="form-control-static">{{createTime}}</div>
      </div>
    </div>
  </div>
</script>
<script>
  $(document).ready(function () {
    var HBR_TD = Handlebars.compile($("#template-clueFlow").html());
    var pageParams = {
      <#-- 模块id，最重要的属性 -->
      "moduleName": "clueFlow",
      <#-- 弹窗区域，都是100%，是不显示最大化按钮的 -->
      "area": ["600px", "600px"],
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
      "formatters":{
        "no": function (column, row) {
          return row.idx;
        },
        "commands": function (column, row) {
          return HBR_TD(row);
        }
      },
      <#-- 列表自定义提交数据的附加参数 -->
      "otherParams": {}
    };
    <#-- view的本页渲染的模板 -->
    var HBR_VIEW = Handlebars.compile($("#template-view-clueFlow").html());
    <#-- 列表ajax加载，绑定删除和编辑之外的其他按钮 -->
    function bindRowEvent() {
      <#-- 绑定查看，本例是用的本页渲染，也可以使用单独的页面 -->
      <#-- CommonGetData定义在main-list.js -->
      $("#table-clueFlow .ops-view").on("click", function () {
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
