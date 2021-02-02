<#include "/common/head.ftl"/>
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content  row">
  <div class="col-xs-12">
    <div class="form-middle">
      <form id="form-sysUser" class="form-horizontal" method="post"
            action="a/sysUser/${url!}">
        <div class="col-xs-12">
          <div class="form-group">
            <label class="col-xs-3 control-label">原账号：</label>
            <div class="col-xs-9">${account!?html}</div>
          </div>
          <div class="form-group">
            <label class="col-xs-3 control-label"><span class="text-danger">*</span>新账号：</label>
            <div class="col-xs-9">
              <input type="text" id="account" name="account" class="form-control"
                     placeholder="请输入新的登录账号"/>
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
    formObj = $("#form-sysUser");
    <#-- 这是是作为一个表单验证的示例，针对具体的逻辑进行验证 -->
    formObj.validate({
      "rules": {
        "account": {
          "required": true,
          "minlength": 2,
          "maxlength": 20,
          "remote": {
            "type": "POST",
            "url": "a/sysUser/check.json?id=${special!}",
            "data": {
              "name": function () {
                return $("#account").val();
              }
            }
          }
        }
      },
      "messages": {
        "account": {
          "required": "请输入登录账号",
          "minlength": "长度不能小于2",
          "maxlength": "长度不能大于20",
          "remote": "登录账号已存在"
        }
      }
    });
  });

  /**
   * 添加和修改
   */
  <#-- 必须有的方法，在这里提交 -->
  function subSave() {
    if (formObj.valid()) {
      // 内部窗口获取layer的索引
      var layerIndex = parent.layer.getFrameIndex(window.name);
      CommonUtils.AjaxUtil.CommonAjaxData(formObj.attr("action"), formObj.serializeJson(), {
        "loadingType": 2,
        "successCallback": function (data) {
          <#if isAdmin>
          top.layerCallerWD.get().window.refreshTable();
          </#if>
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
