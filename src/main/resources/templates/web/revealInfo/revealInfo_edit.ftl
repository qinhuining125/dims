<#include "/common/head.ftl"/>
<link rel="stylesheet" href="${global.staticPath!}static/plugins/select2/select2.min.css">
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content row">
  <div class="col-xs-12">
    <div class="form-middle">
      <form id="form-revealInfo" action="${mapping!}/saveData.json?${global.addSign!}=${(entity.id)!}"
            class="form-horizontal" method="post">
        <input type="hidden" name="id" value="${(entity.id)!}"/>
        <input type="hidden" name="receiveId" id="receiveId" value=""/>
        <input type="hidden" name="receiveRoleId" id="receiveRoleId" value=""/>
        <input type="hidden" name="townCode" id="townCode" value=""/>
        <input type="hidden" name="areaCode" id="areaCode" value=""/>
        <input type="hidden" name="villageCode" id="villageCode" value=""/>

        <div class="form-group">
          <label class="col-xs-3 control-label"><span class="text-danger">*</span>乡镇:</label>
          <div class="col-xs-3">
            <select class="form-control" id="township" onchange="searchVillage(this);">
              <option value="">全部</option>
              <#list areaList as obj>
                <option value="${obj.userId},${obj.areaCode}">${obj.areaName}</option>
              </#list>
            </select>
            <span class="help-block m-b-none"></span>
          </div>
          <label class="col-xs-3 control-label">村:</label>
          <div class="col-xs-3">
            <select id="village" multiple class="form-control" onchange="choseArea(this);">
            </select>
            <span class="help-block m-b-none"></span>
          </div>

        </div>
        <div class="col-xs-12">
          <div class="form-group">
            <label class="col-xs-3 control-label"><span class="text-danger"></span>姓名：</label>
            <div class="col-xs-9">
              <textarea cols="12" rows="1" name="userName" class="form-control" placeholder="请输入姓名">${(entity.userName)!}</textarea>
              <span class="help-block m-b-none"></span>
            </div>
          </div>
        </div>

        <div class="col-xs-12">
          <div class="form-group">
            <label class="col-xs-3 control-label"><span class="text-danger"></span>联系方式：</label>
            <div class="col-xs-9">
              <textarea cols="12" rows="1" name="contact" class="form-control" placeholder="请输入联系方式">${(entity.contact)!}</textarea>
              <span class="help-block m-b-none"></span>
            </div>
          </div>
        </div>
        <div class="form-group">
          <label class="col-xs-3 control-label"><span class="text-danger">*</span>举报类型：</label>
          <div class="col-xs-9">
            <#assign seletedType="${(entity.type)!}" />
            <select name="type" class="form-control">
<#--              <option value="">--请选择--</option>-->
              <#list revealTypeList as obj>
                <#if seletedType == obj.key>
                  <option value="${(obj.key)!}" selected>${(obj.value)!}</option>
                <#else>
                  <option value="${(obj.key!)}">${(obj.value)!}</option>
                </#if>
              </#list>
            </select>
            <span class="help-block m-b-none"></span>
          </div>
        </div>


        <div class="col-xs-12">
          <div class="form-group">
            <label class="col-xs-3 control-label"><span class="text-danger"></span>举报对象：</label>
            <div class="col-xs-9">
              <textarea cols="12" rows="2" name="revealTarget" class="form-control" placeholder="请输入举报对象">${(entity.revealTarget)!}</textarea>
              <span class="help-block m-b-none"></span>
            </div>
          </div>
        </div>
        <div class="col-xs-12">
          <div class="form-group">
            <label class="col-xs-3 control-label"><span class="text-danger"></span>举报内容：</label>
            <div class="col-xs-9">
              <textarea cols="12" rows="3" name="content" class="form-control" placeholder="请输入举报内容">${(entity.content)!}</textarea>
              <span class="help-block m-b-none"></span>
            </div>
          </div>
        </div>

        <div class="col-xs-12">
          <div class="form-group">
            <label class="col-xs-3 control-label"><span class="text-danger"></span>上传图片：</label>
            <div class="col-xs-9">
              <textarea cols="12" rows="3" name="picture" class="form-control" placeholder="请上传图片"></textarea>
              <span class="help-block m-b-none"></span>
            </div>
          </div>
        </div>

      </form>
    </div>
  </div>
</div>
<#include "/common/scriptfile.ftl"/>
<script src="${global.staticPath!}static/plugins/select2/select2.min.js"></script>
<script id="template-villages" type="text/x-handlebars-template">
  <option value="">--请选择--</option>
  {{#each this}}
  <option value="{{areaCode}}">{{areaName}}</option>
  {{/each}}
</script>
<script>
  var formObj;
  $(document).ready(function () {
    $('#village').select2({
      placeholder:"--请选择--",
      multiple : true
    });
    formObj = $("#form-revealInfo");
    <#-- 这是是作为一个表单验证的示例，针对具体的逻辑进行验证 -->
    formObj.validate({
      "rules": {
        "receiveId": {
          "required": true
        },
        "receiveRoleId": {
          "required": true
        },
        "content": {
          "required": true
        }
      },
      "messages": {
        "receiveId": {
          "required": "请选择",
        },
        "content": {
          "required": "请输入内容"
        }
      }
    });
  });
  function searchVillage(obj) {
    // debugger;
    const township = $(obj).val();
    if (township == '') {
      return false;
    }
    const array = township.split(",");
    $("#receiveId").val(array[0]);
    $("#townCode").val(array[1]);
    $("#receiveRoleId").val(1003);
    $.ajax({
      "type": "GET",
      "cache": false,
      "url": "/a/village/list.json",
      "data": {
        "areaCode": array[1]
      },
      "dataType": "json",
      "success": function (data) {
        const template = Handlebars.compile($("#template-villages").html());
        $("#village").html(template(data.result));
      }
    });
  }
  function choseArea(obj) {
    var code = $(obj).val();
    if (code == '') {
      code = $("#township").val();
    }
    $("#areaCode").val(code);
    $("#villageCode").val($(obj).val());
  }

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
