<#include "/common/head.ftl"/>
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content animated fadeInRight row">
  <div class="col-xs-12">
    <div class="form-middle">
      <form id="form-excel" enctype="multipart/form-data"
            action="/a/sysUser/importUser.json" class="form-horizontal" method="post">
        <div class="col-xs-12">
          <div class="form-group">
            <label class="col-xs-3 control-label"><span class="text-danger">*</span>文件：</label>
            <div class="col-xs-9">
              <input type="file" name="file"
                     accept="application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                     class="form-control"/>
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
    formObj = $("#form-excel");
    <#-- 这是是作为一个表单验证的示例，针对具体的逻辑进行验证 -->
    formObj.validate({});
  });

  /**
   * 添加和修改
   */
  <#-- 必须有的方法，在这里提交 -->
  function subSave() {
    if (formObj.valid()) {
      // 内部窗口获取layer的索引
      var layerIndex = parent.layer.getFrameIndex(window.name);
      var frmObj = $("#form-excel");
      CommonUtils.AjaxUtil.CommonAjaxFormData(frmObj, {
        "loadingType": 2,
        "successCallback": function (data) {
          <#-- 成功后刷新列表页的Gird，这个refreshTable方法定义在main-list.js -->
          top.layerCallerWD.get().window.refreshTable();
          window.setTimeout(function () {
            top.layer.close(layerIndex);
          }, 50);
        }
      });
    }
  }
</script>
</body>
</html>
