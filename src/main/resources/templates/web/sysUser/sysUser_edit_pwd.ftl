<#include "/common/head.ftl"/>
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content  row">
  <div class="col-xs-12">
    <div class="form-middle">
      <form id="form-sysUser"
            action="a/sysUser/saveDataPassword.json?t=2"
            class="form-horizontal" method="post">
        <div class="col-xs-12">
          <div class="form-group">
            <label class="col-xs-3 control-label"><span class="text-danger">*</span>原始密码：</label>
            <div class="col-xs-9">
              <input type="password" id="a1" name="a1" class="form-control"
                     placeholder="请输入原始密码"/>
            </div>
          </div>
          <div class="form-group">
            <label class="col-xs-3 control-label"><span class="text-danger">*</span>新的密码：</label>
            <div class="col-xs-9">
              <input type="password" id="p1" name="a2" class="form-control"
                     placeholder="请输入新的密码"/>
            </div>
          </div>
          <div class="form-group">
            <label class="col-xs-3 control-label"><span
                  class="text-danger">*</span>确认密码：</label>
            <div class="col-xs-9">
              <input type="password" id="p2" name="a3" class="form-control"
                     placeholder="请再次输入新的密码"/>
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
  jQuery.validator.addMethod("checkPwd", function (value, element) {
    return this.optional(element) || ($("#p1").val() === value);
  }, "请确认密码");
  jQuery.validator.addMethod("checkRepeatPwd", function (value, element) {
    return this.optional(element) || ($("#a1").val() != value);
  }, "请确认密码");

  $(document).ready(function () {
    formObj = $("#form-sysUser");
    <#-- 这是是作为一个表单验证的示例，针对具体的逻辑进行验证 -->
    formObj.validate({
      "rules": {
        "a21": {
          "required": true
        },
        "a2": {
          "required": true,
          "checkRepeatPwd": true,
          "minlength": 2,
          "maxlength": 20
        },
        "a3": {
          "required": true,
          "checkPwd": true
        }
      },
      "messages": {
        "a1": {
          "required": "请输入原始密码"
        },
        "a2": {
          "required": "请输入新的密码",
          "checkRepeatPwd": "新密码不能与原密码相同",
          "minlength": "长度不能小于2",
          "maxlength": "长度不能大于20"
        },
        "a3": {
          "required": "请再次输入新密码",
          "checkPwd": "输入两次新的密码不一致"
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
