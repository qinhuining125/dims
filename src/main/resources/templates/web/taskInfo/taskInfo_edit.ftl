<#include "/common/head.ftl"/>
<link rel="stylesheet" href="${global.staticPath!}static/plugins/select2/select2.min.css">
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content row">
  <div class="col-xs-12">
    <div class="form-middle">
      <form id="form-taskInfo" action="${mapping!}/saveData.json?${global.addSign!}=${(entity.id)!}"
            class="form-horizontal" method="post">
        <input type="hidden" name="id" value="${(entity.id)!}"/>
        <input type="hidden" name="receiveId" id="receiveId" value=""/>
        <input type="hidden" name="receiveRoleId" id="receiveRoleId" value=""/>
        <div class="form-group">
          <label class="col-xs-3 control-label"><span class="text-danger">*</span>乡镇:</label>
          <div class="col-xs-3">
            <select class="form-control" id="township" onchange="searchVillage(this);"  data-bv-choice-message="必选项">
              <option value="">全部</option>
                <#list areaList as obj>
                  <option value="${obj.userId},${obj.areaCode}">${obj.areaName}</option>
                </#list>
            </select>
            <span class="help-block m-b-none"></span>
          </div>
          <label class="col-xs-3 control-label">村:</label>
          <div class="col-xs-3">
            <select id="valliage" multiple class="form-control" data-bv-choice-message="必选" onchange="choseArea(this);">
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
              <input type="text" name="images" id="images" value="" >
            </div>
          </div>
        </div>
      </form>
      <div class="col-xs-12">
        <div class="form-group">
          <#--          占位-->
          <label class="col-xs-3 control-label"><span class="text-danger"></span></label>
          <div class="col-xs-9">
            <form id= "uploadForm">
              <input type="file" name="file1" id="file1" onchange="setImagePreviews();" multiple="multiple"  />
              <div id="localImag">
                <img id="preview" width=-1 height=-1 style="diplay:none" />
              </div>
              <input type="button" value="上传" onclick="doUpload()" />
            </form>
<#--            <script type="text/javascript" src="lib/jquery/1.9.1/jquery.min.js"></script>-->
<#--            <script type="text/javascript" src="lib/layer/2.4/layer.js"></script>-->
<#--            <script type="text/javascript" src="js/common.js"></script>-->
            <script type="text/javascript">
              function setImagePreviews(avalue) {
                var docObj = document.getElementById("file1");
                var dd = document.getElementById("localImag");
                dd.innerHTML = "";
                var fileList = docObj.files;
                fileimage=docObj.files;
                console.log(fileimage)
                for (var i = 0; i < fileList.length; i++) {
                  dd.innerHTML += "<div style='float:left' > <img id='img" + i + "'  /> </div>";
                  var imgObjPreview = document.getElementById("img"+i);
                  if (docObj.files && docObj.files[i]) {
                    //火狐下，直接设img属性
                    imgObjPreview.style.display = 'block';
                    imgObjPreview.style.width = '150px';
                    imgObjPreview.style.height = '180px';
                    //imgObjPreview.src = docObj.files[0].getAsDataURL();
                    //火狐7以上版本不能用上面的getAsDataURL()方式获取，需要一下方式
                    imgObjPreview.src = window.URL.createObjectURL(docObj.files[i]);
                  }
                  else {
                    //IE下，使用滤镜
                    docObj.select();
                    var imgSrc = document.selection.createRange().text;
                    alert(imgSrc)
                    var localImagId = document.getElementById("img" + i);
                    //必须设置初始大小
                    localImagId.style.width = "150px";
                    localImagId.style.height = "180px";
                    //图片异常的捕捉，防止用户修改后缀来伪造图片
                    try {
                      localImagId.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)";
                      localImagId.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = imgSrc;
                    }
                    catch (e) {
                      alert("您上传的图片格式不正确，请重新选择!");
                      return false;
                    }
                    imgObjPreview.style.display = 'none';
                    document.selection.empty();
                  }
                }
                return true;
              }
              //获取token
              // var token = parent.window.document.getElementById("token").value;
              function doUpload() {
                var formData = new FormData();
                // formData.append("token", token);
                // console.log(token)
                var filelength = $('#file1')[0].files.length;
                if (filelength==0){
                  alert("没有可上传图片")
                }
                if (filelength>0){
                  var i=0
                  while (i<filelength){
                    formData.append("file1", $('#file1')[0].files[i]);
                    i++
                  }
                  $.ajax({
                    url: '/web/DemoImageController/uploadMult',
                    type: 'post',
                    data: formData,
                    cache: false,
                    processData: false,
                    contentType: false,
                    async: false
                  }).done(function(res) {
                    console.log(res[0])
                    if (res[0]=="false"){
                      alert("服务器异常，图片上传失败")
                    }else {
                      $('#images').val(res)

                    }
                    console.log(res)
                  }).fail(function(res) {

                  });
                }

              }
            </script>
          </div>
        </div>

      </div>
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
        "township": {
          "required": true
        },
        "valliage": {
          "required": true
        },
        "content": {
          "required": true
        },

      },
      "messages": {
        "receiveId": {
          "required": "请选择",
        },
        "receiveRoleId": {
          "required": "请选择",
        },
        "township": {
          "required": "请选择",
        },
        "valliage": {
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
    console.log(township)
    console.log(array)
    $("#receiveId").val(array[0]);
    $("#receiveRoleId").val(1003);
    console.log(township)
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
        if (data.result==null){
          alert(请选择村)
        }
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
