<#include "/common/head.ftl"/>
<link rel="stylesheet" href="${global.staticPath!}static/plugins/select2/select2.min.css">
<link rel="shortcut icon" href="/static/favicon.ico">
<link href="css/bootstrap.min.css" rel="stylesheet">
<script src="https://ajax.aspnetcdn.com/ajax/jquery/jquery-1.9.1.min.js"></script>
<#--<script src="js/bootstrap.min.js"></script>-->
<!-- 全局js -->

<style>
  .fa-times {
    font-size: 15px;
  }
  .panel {
    height: 285px;
    margin-bottom: 0;
    margin-top: 10px;
  }
  .panel-heading {
    text-align: right;
  }
  /*加号*/
  .icon-add-50 {
    position: relative;
    box-sizing: border-box;
    width: 50px;
    height: 50px;
    border: 2px dashed #9a9ba3;
    border-radius: 50%;
  }
  .icon-add-50:before {
    content: '';
    position: absolute;
    width: 30px;
    height: 2px;
    left: 50%;
    top: 50%;
    margin-left: -15px;
    margin-top: -1px;
    background-color: #aaabb2;
  }
  .icon-add-50:after {
    content: '';
    position: absolute;
    width: 2px;
    height: 30px;
    left: 50%;
    top: 50%;
    margin-left: -1px;
    margin-top: -15px;
    background-color: #aaabb2;
  }

  .updatepanel {
    border: 1px solid #ccc;
    text-align: center;
  }
  .updatepanel .addbox {
    vertical-align: middle;
    height: 285px;
    line-height: 285px;
  }

  #image {
    overflow: hidden;
  }
  .panel-body {
    /*padding-top: 0px;
    padding-bottom: 0px;*/
  }
  label {
    width: 100%;
  }
</style>
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content row">
  <div class="col-xs-12">
    <div class="form-middle">
      <form id="form-taskInfo" action="${mapping!}/saveData.json?${global.addSign!}=${(entity.id)!}"
            class="form-horizontal" method="post" enctype="multipart/form-data">
        <input type="hidden" name="id" value="${(entity.id)!}"/>
        <input type="hidden" name="receiveId" id="receiveId" value=""/>
        <input type="hidden" name="receiveRoleId" id="receiveRoleId" value=""/>
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
            <select id="valliage" multiple class="form-control" onchange="choseArea(this);">
            </select>
            <span class="help-block m-b-none"></span>
          </div>
        </div>
        <div class="col-xs-12">
          <div class="form-group">
            <label class="col-xs-3 control-label"><span class="text-danger">*</span>内容：</label>
            <div class="col-xs-9">
              <textarea cols="12" rows="6" name="content" class="form-control" placeholder="请输入内容">${(entity.content)!}</textarea>
              <span class="help-block m-b-none"></span>
            </div>
          </div>
        </div>
        <div class="col-xs-12">
          <div class="form-group">
            <label class="col-xs-3 control-label"><span class="text-danger">*</span>图片：</label>
            <div class="col-xs-9">
                <select class="form-control" id="township" onchange="searchImage(this);">
                  <option value="">全部</option>
                  <#list areaList as obj>
                    <option value="${obj.userId},${obj.areaCode}">${obj.areaName}</option>
                  </#list>
                </select>
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
  <option value="{{userId}}">{{areaName}}</option>
  {{/each}}
</script>
<script>
  var formObj;
  $(document).ready(function () {
    $('#valliage').select2({
      placeholder:"--请选择--",
      multiple : true
    });
    formObj = $("#form-taskInfo");
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
    const township = $(obj).val();
    if (township == '') {
      return false;
    }
    const array = township.split(",");
    $("#receiveId").val(array[0]);
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
        $("#valliage").html(template(data.result));
      }
    });
  }
  function searchImage(obj) {
    const township = $(obj).val();
    if (township == '') {
      return false;
    }
    const array = township.split(",");
    $("#receiveId").val(array[0]);
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
        $("#valliage").html(template(data.result));
      }
    });
  }
  function choseArea(obj) {
    const userId = $(obj).val();
    if (userId == '') {
      const township = $("#township").val();
      $("#receiveId").val(township.split(",")[0]);
      $("#receiveRoleId").val(1003);
    } else {
      $("#receiveId").val(userId);
      $("#receiveRoleId").val(1002);
    }
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
