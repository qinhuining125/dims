<#-- 在用户管理页面使用，绑定用户和角色的关系 -->
<#include "/common/head.ftl"/>
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content row">

  <div class="col-xs-12">
    <div class="form-horizontal" style="margin: 20px;">
      <div class="form-group">
        <label class="col-xs-3 control-label">用户：</label>
        <div class="col-xs-3">
          <div class="form-control-static">${(entity.userName)!}</div>
        </div>
        <label class="col-xs-3 control-label">编号：</label>
        <div class="col-xs-3">
          <div class="form-control-static">${(entity.clueNo)!}</div>
        </div>
      </div>
      <div class="form-group">
        <label class="col-xs-3 control-label">时间：</label>
        <div class="col-xs-3">
          <div class="form-control-static">${(entity.createTime?string('yyyy-MM-dd'))!}</div>
        </div>
        <label class="col-xs-3 control-label">乡镇：</label>
        <div class="col-xs-3">
          <div class="form-control-static">${(entity.reportUserAreaName)!}</div>
        </div>
      </div>
      <div class="form-group">
        <label class="col-xs-3 control-label">上报类型：</label>
        <div class="col-xs-9">
          <#list entity.dtoList as obj>
            <div class="form-control-static">${(obj.key)!}、${(obj.value)!}</div>
          </#list>
        </div>
      </div>
      <div class="form-group">
        <label class="col-xs-3 control-label">线索描述：</label>
        <div class="col-xs-3">
          <div class="form-control-static">${(entity.clueDescribe)!}</div>
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
        <#list entity.flows as obj>
          <tr>
            <td>
              <div class="form-control-static">${(obj.receiveName)!}</div>
            </td>
            <td>
              <div class="form-control-static">
                <#if obj.state == 0>未处理</#if>
                <#if obj.state == 1>已受理</#if>
                <#if obj.state == 2>已办结</#if>
              </div>
            </td>
            <td>
              <div class="form-control-static">${(obj.remark)!}</div>
            </td>
            <td>
              <div class="form-control-static">${(obj.createTime?string('yyyy-MM-dd'))!}</div>
            </td>
          </tr>
        </#list>
        </tbody>
      </table>
    </div>
  </div>


  <div class="col-xs-18">
    <div class="form-horizontal">
      <form id="form-editTask" action="${mapping!}/editTask.json?" class="form-horizontal" method="post">
        <input type="hidden" name="uid" value="${uid}"/>
        <div class="col-xs-18">
          <div class="form-group">
            <label class="col-xs-3 control-label"><span class="text-danger">*</span>办结描述：</label>
            <div class="col-xs-15">
              <textarea name="remark" rows="3" cols="80"  placeholder="请输入新的办结描述（不超过1000字）"> </textarea>
            </div>
          </div>
        </div>
      </form>
    </div>
  </div>

</div>
<#include "/common/scriptfile.ftl"/>
<script>
  var formObj;
  $(document).ready(function () {
    formObj = $("#form-editTask");
  });

  function subSave() {
    // 内部窗口获取layer的索引
    var layerIndex = parent.layer.getFrameIndex(window.name);
    CommonUtils.AjaxUtil.CommonAjaxData(formObj.attr("action"), formObj.serializeJson(), {
      "loadingType": 2,
      "successCallback": function (data) {
        console.log(data);
        top.layerCallerWD.get().window.refreshTable();
        window.setTimeout(function () {
          top.layer.close(layerIndex);
        }, 50);
      }
    });
  }
</script>
</body>
</html>
