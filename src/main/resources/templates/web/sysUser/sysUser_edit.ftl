<#include "/common/head.ftl"/>
<link rel="stylesheet"
      href="${global.staticPath!}static/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.min.css"/>
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content  row">
  <div class="col-xs-12">
    <div class="form-middle">
      <form id="form-sysUser" action="a/sysUser/saveData.json?${global.addSign!}=${(entity.id)!}"
            class="form-horizontal" method="post">
        <input type="hidden" name="id" value="${(entity.id)!}"/>
        <div class="col-md-12">
            <#if isNew>
              <div class="form-group">
                <label class="col-xs-3 control-label"><span
                      class="text-danger">*</span>登录账号：</label>
                <div class="col-xs-9">
                  <input type="text" id="account" name="userAccount" class="form-control"
                         placeholder="请输入登录账号"/>
                </div>
              </div>
              <div class="form-group">
                <label class="col-xs-3 control-label"><span
                      class="text-danger">*</span>初始密码：</label>
                <div class="col-xs-9">
                  <input type="text" name="password" class="form-control" placeholder="请输入初始密码"
                         value="${pwd!}"/>
                </div>
              </div>
            </#if>
          <div class="form-group">
            <label class="col-xs-3 control-label"><span class="text-danger">*</span>用户名：</label>
            <div class="col-xs-9">
              <input type="text" name="userName" class="form-control" placeholder="请输入用户名"
                     value="${(entity.userName)!?html}"/>
            </div>
          </div>
          <div class="form-group">
            <label class="col-xs-3 control-label">用户状态：</label>
            <div class="col-xs-9">
              <div class="abc-checkbox abc-checkbox-danger checkbox-inline" data-toggle="tooltip"
                   data-placement="bottom" title="启用，表示用户可用">
                <input type="checkbox" id="user_enable" name="enabled" value="1">
                <label for="user_enable"> 启用用户</label></div>
            </div>
          </div>
          <div class="form-group">
            <label class="col-xs-3 control-label">性别：</label>
            <div class="col-xs-9">
              <select name="sex" id="sex" class="form-control">
                <option value="男">男</option>
                <option value="女">女</option>
              </select>
            </div>
          </div>
          <div class="form-group">
            <label class="col-xs-3 control-label">地区名称：</label>
            <div class="col-xs-9">
              <input type="text" name="areaName" class="form-control" placeholder="请输入地区名称"
                     value="${(entity.areaName)!?html}"/>
            </div>
          </div>
          <div class="form-group">
            <label class="col-xs-3 control-label">地区编号：</label>
            <div class="col-xs-9">
              <input type="text" name="areaCode" class="form-control" placeholder="请输入地区编号"
                     value="${(entity.areaCode)!?html}"/>
            </div>
          </div>
          <div class="form-group">
            <label class="col-xs-3 control-label">备注：</label>
            <div class="col-xs-9">
              <textarea name="remark" class="form-control" style="resize:none">${(entity.remark)!?html}</textarea>
            </div>
          </div>
        </div>
      </form>
    </div>
  </div>
</div>
<#include "/common/scriptfile.ftl"/>
<script>
  var userEnable = "${(entity.enabled)!"0"}", userId = "${(entity.id)!}";
  var userSex = "${(entity.sex)!1}";
  var formObj;
  $(document).ready(function () {
    formObj = $("#form-sysUser");
    <#-- 配置初始化的数据 -->
    if (userEnable === "1" || userId === "") {
      $("#user_enable").prop("checked", true);
    }
    $("#sex").val(userSex);
    $("#companyId").val("${companyId!}");
    <#-- 这是是作为一个表单验证的示例，针对具体的逻辑进行验证 -->
    formObj.validate({
      "rules": {
        <#if isNew>
        "userAccount": {
          "required": true,
          "minlength": 2,
          "maxlength": 20,
          "remote": {
            "type": "POST",
            "url": "a/sysUser/check.json?id=${(entity.id)!}",
            "data": {
              "name": function () {
                return $("#account").val();
              }
            }
          }
        },
        "password": {
          "required": true,
          "minlength": 2,
          "maxlength": 20
        },
        </#if>
        "userName": {
          "required": true,
          "minlength": 2,
          "maxlength": 20
        },
        "mail": {
          "email": true
        }
      },
      "messages": {
        <#if isNew>
        "userAccount": {
          "required": "请输入登录账号",
          "minlength": "长度不能小于2",
          "maxlength": "长度不能大于20",
          "remote": "登录账号已存在"
        },
        "password": {
          "required": "请输入初始密码",
          "minlength": "长度不能小于2",
          "maxlength": "长度不能大于20"
        },
        </#if>
        "userName": {
          "required": "请输入名称",
          "minlength": "长度不能小于2",
          "maxlength": "长度不能大于20"
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
