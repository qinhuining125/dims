<#-- 在用户管理页面使用，绑定用户和角色的关系 -->
<#include "/common/head.ftl"/>
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content row">
  <div class="col-xs-12">
    <div class="form-middle">
      <form id="form-sysRole" action="${mapping!}/savetree.json?" class="form-horizontal"
            method="post">
        <div class="col-xs-12">
          <div class="form-group">
            <input type="hidden" value="${(uid)!}" name="uid"/>
            <label class="col-xs-3 control-label">角色：</label>
            <div class="col-xs-9">
              <select name="roleId" class="form-control">
                  <option value="">--请选择--</option>
                  <#list roleList as role>
                    <option value="${(role.key)!}">${(role.value)!}</option>
                  </#list>
              </select>
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
    formObj = $("#form-sysRole");
  });

  function subSave() {
    // 内部窗口获取layer的索引
    var layerIndex = parent.layer.getFrameIndex(window.name);
    CommonUtils.AjaxUtil.CommonAjaxData(formObj.attr("action"), formObj.serializeJson(), {
      "loadingType": 2,
      "successCallback": function (data) {
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
