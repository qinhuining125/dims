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
<#--              <form action=""  enctype="multipart/form-data">-->
              <input type="file" name="" id="choose-file" multiple="multiple">
              <ul class="file-list ">
              </ul>
              <!--</p>-->
              <button style="cursor: pointer;margin-left: 150px;" href="javascript:;" id="upload">上传</button>
<#--              </form>-->
            </div>
          </div>
          <script src="js/jquery.min.js"></script>
          <script src="layer/layer.js"></script>
          <script>

            $(function () {
              ////////////////////////////////////////////////图片上传//////////////////////////////////////////////
              //声明变量
              var $button = $('#upload'),
                      //选择文件按钮
                      $file = $("#choose-file"),
                      //回显的列表
                      $list = $('.file-list'),
                      //选择要上传的所有文件
                      fileList = [];
              //当前选择上传的文件
              var curFile;
              // 选择按钮change事件，实例化fileReader,调它的readAsDataURL并把原生File对象传给它，
              // 监听它的onload事件，load完读取的结果就在它的result属性里了。它是一个base64格式的，可直接赋值给一个img的src.
              $file.on('change', function (e) {
                //上传过图片后再次上传时限值数量
                var numold = $('li').length;
                if(numold >= 6){
                  layer.alert('最多上传6张图片');
                  return;
                }
                //限制单次批量上传的数量
                var num = e.target.files.length;
                var numall = numold + num;
                if(num >6 ){
                  layer.alert('最多上传6张图片');
                  return;
                }else if(numall > 6){
                  layer.alert('最多上传6张图片');
                  return;
                }
                //原生的文件对象，相当于$file.get(0).files;//files[0]为第一张图片的信息;
                curFile = this.files;
                //curFile = $file.get(0).files;
                //console.log(curFile);
                //将FileList对象变成数组
                fileList = fileList.concat(Array.from(curFile));
                console.log(fileList);
                for (var i = 0, len = curFile.length; i < len; i++) {
                  reviewFile(curFile[i])
                }
                console.log(curFile);
                $('.file-list').fadeIn(2500);
              })
              function reviewFile(file) {
                console.log(file)
                //实例化fileReader,
                var fd = new FileReader();
                //获取当前选择文件的类型
                var fileType = file.type;
                //调它的readAsDataURL并把原生File对象传给它，
                fd.readAsDataURL(file);//base64
                //监听它的onload事件，load完读取的结果就在它的result属性里了
                console.log(file)
                fd.onload = function () {
                  if (/^image\/[jpeg|png|jpg|gif]/.test(fileType)) {
                    console.log(this.result)
                    $list.append('<li style="border:solid red px; margin:5px 5px;" class="file-item"><img src="' + this.result + '" alt="" height="70"><span class="file-del">删除</span></li>').children(':last').hide().fadeIn(2500);
                  } else {
                    $list.append('<li class="file-item"><span class="file-name">' + file.name + '</span><span class="file-del">删除</span></li>')
                  }

                }
              }
              //点击删除按钮事件：
              $(".file-list").on('click', '.file-del', function () {
                let $parent = $(this).parent();
                console.log($parent);
                let index = $parent.index();
                fileList.splice(index, 1);
                $parent.fadeOut(850, function () {
                  $parent.remove()
                });
                //$parent.remove()
              });
              //点击上传按钮事件：
              $button.on('click', function () {
                var name = $('#name').val();
                if(fileList.length > 6){
                  layer.alert('最多允许上传6张图片');
                  return;
                } else {
                  var formData = new FormData();
                  for (var i = 0, len = fileList.length; i < len; i++) {
                    var photos=fileList[i]
                    console.log(fileList[i]);
                    $.ajax({
                      <#--url:  ${global.preUrl!}'http://192.168.32.240:81/web/DemoImageController/save_image',-->
                      url:  ${global.preUrl!}'web/DemoImageController/save_image',
                      type: 'post',
                      data: photos,
                      dataType: 'json',
                      processData: false,
                      contentType: "multipart/form-data",
                      success: function (data) {
                        if (data.status == '1') {
                          layer.msg(data.content, {icon: 6});
                        } else if (data.status == '2') {
                          layer.msg(data.content, {icon: 1});
                        }
                      }
                    })
                    formData.append('upfile[]', fileList[i]);
                  }
                  console.log(formData);

                  // $.ajax({
                  //     url: './product_add.php',
                  //     type: 'post',
                  //     data: formData,
                  //     dataType: 'json',
                  //     processData: false,
                  //     contentType: false,
                  //     success: function (data) {
                  //         if (data.status == '1') {
                  //             layer.msg(data.content, {icon: 6});
                  //         } else if (data.status == '2') {
                  //             layer.msg(data.content, {icon: 1});
                  //         }
                  //     }
                  // })
                }
              })
            })
          </script>
        </div>
      </form>
      <form id="form-taskInfo" action="/web/DemoImageController/save_image" method="POST" enctype="multipart/form-data">
        <input type="file"  name="photos" value="photos">
        <input type="submit">
      </form>
      <form action="/web/UploadController/upload" method="post" enctype="multipart/form-data">
        选择图片:<input type="file" name="file" accept="image/*" /> <br>
        <input type="submit" value="立刻上传">
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
