<#include "/common/head.ftl"/>
<link rel="stylesheet"
      href="${global.staticPath!}static/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.min.css"/>
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content  row">
  <div class="col-xs-12">
    <div class="form-middle">
      <form id="form-sysAuth" action="a/sysAuth/saveData.json?${global.addSign!}=${(entity.id)!}"
            class="form-horizontal" method="post">
        <input type="hidden" name="id" value="${(entity.id)!}"/>
        <input type="hidden" name="modId" value="${mid!}"/>
        <div class="col-xs-12">
          <div class="form-group">
            <label class="col-xs-3 control-label"><span class="text-danger">*</span>权限名称：</label>
            <div class="col-xs-9">
              <input type="text" name="authName" class="form-control" placeholder="请输入权限名称"
                     value="${(entity.authName)!?html}"/><span class="help-block m-b-none">
              </span>
            </div>
          </div>
          <div class="form-group">
            <label class="col-xs-3 control-label"><span class="text-danger">*</span>权限代码：</label>
            <div class="col-xs-9">
              <input type="text" id="authCode" name="authCode" class="form-control"
                     placeholder="请输入权限代码"
                     value="${(entity.authCode)!?html}"/><span class="help-block m-b-none">
              </span>
            </div>
          </div>
          <div class="form-group">
            <label class="col-xs-3 control-label">权限状态：</label>
            <div class="col-xs-9">
              <div class="abc-checkbox abc-checkbox-danger checkbox-inline" data-toggle="tooltip"
                   data-placement="bottom" title="启用，表示授权后可以访问">
                <input type="checkbox" id="menu_enable" name="enabled" value="1">
                <label for="menu_enable"> 启用权限</label></div>
              <div class="abc-checkbox abc-checkbox-warning checkbox-inline" data-toggle="tooltip"
                   data-placement="bottom" title="锁定，表示该权限无法删除">
                <input type="checkbox" id="menu_lock" name="locked" value="1">
                <label for="menu_lock"> 删除锁定</label></div>
            </div>
          </div>
          <div class="form-group">
            <label class="col-xs-3 control-label">功能备注：</label>
            <div class="col-xs-9">
            <textarea name="remark" class="form-control"
                      style="resize:none">${(entity.remark)!?html}</textarea>
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
  var menuEnable = "${(entity.enabled)!"0"}", menuLock = "${(entity.locked)!"0"}",
      menuId = "${(entity.id)!}";

  $(document).ready(function () {
    formObj = $("#form-sysAuth");
    <#-- 配置初始化的数据 -->
    if (menuEnable === "1" || menuId === "") {
      $("#menu_enable").prop("checked", true);
    }
    if (menuLock === "1") {
      $("#menu_lock").prop("checked", true);
    }
    <#-- 这是是作为一个表单验证的示例，针对具体的逻辑进行验证 -->
    formObj.validate({
      "rules": {
        "authName": {
          "required": true,
          "minlength": 2,
          "maxlength": 20
        },
        "authCode": {
          "required": true,
          "minlength": 2,
          "maxlength": 30,
          "remote": {
            "type": "POST",
            "url": "a/sysAuth/check.json?id=${(entity.id)!}",
            "data": {
              "name": function () {
                return $("#authCode").val();
              }
            }
          }
        }
      },
      "messages": {
        "authName": {
          "required": "请输入权限名称",
          "minlength": "长度不能小于2",
          "maxlength": "长度不能大于20"
        },
        "authCode": {
          "required": "请输入授权码",
          "minlength": "长度不能小于2",
          "maxlength": "长度不能大于30",
          "remote": "授权码已存在"
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
