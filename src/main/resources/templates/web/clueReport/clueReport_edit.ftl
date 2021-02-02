<#include "/common/head.ftl"/>
<style>
  .print-show {
    display: none;
  }

  @media print {
    .print-hide {
      display: none;
    }

    .print-show {
      display: block;
    }
  }
</style>
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content animated fadeInRight row">
  <div class="form-middle" id="printArea">
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
      <div class="text-center print-hide">
        <button type="button" id="btn-dy" class="btn btn-info"><i class="fa fa-print"></i>&nbsp;打印
        </button>
      </div>
    </div>
  </div>
</div>
<#include "/common/scriptfile.ftl"/>
<script>
  $(document).ready(function () {
    //打印
    $("#btn-dy").on('click', function () {
      window.print();
    });
  });
</script>
</body>
</html>
