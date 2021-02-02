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
<#--<body class="gray-bg">
      <form action="/web/DemoImageController/filesUpload" method="POST" name="xiangmu" id="xiangmu"
            enctype="multipart/form-data">
        <table>
          <tr>
            <td>图片上传：</td>
            <td><input type=file name="myfiles" id="doc"
                       onchange="setImagePreviews();" multiple="multiple">
            </td>
            <div id="localImag">
              <img id="preview" width=-1 height=-1 style="diplay:none" />
            </div>
          </tr>
          <tr>
            <td><input type="submit" value="提交" />
            </td>
          </tr>
        </table>
      </form>
      <script type="text/javascript">
        //下面用于多图片上传预览功能
        function setImagePreviews(avalue) {
          var docObj = document.getElementById("doc");
          var dd = document.getElementById("localImag");
          dd.innerHTML = "";
          var fileList = docObj.files;
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
      </script>
</body>-->
<body>

<form id= "uploadForm">
  <p >指定文件名： <input type="text" name="filename" value= ""/></p >
  <p >上传文件： <input type="file" name="file1" id="file1"  multiple="multiple"  /></p>
  <#--  <p >上传文件： <input type="file" name="file2" id="file2" /></p>-->
  <#--  <p >上传文件： <input type="file" name="file3" id="file3" /></p>-->
  <input type="button" value="上传" onclick="doUpload()" />
</form>
<#--<script type="text/javascript" src="lib/jquery/1.9.1/jquery.min.js"></script>-->
<#--<script type="text/javascript" src="lib/layer/2.4/layer.js"></script>-->
<#--<script type="text/javascript" src="js/common.js"></script>-->
<script type="text/javascript">
  //获取token
  var token = parent.window.document.getElementById("token").value;

  function doUpload() {
    var formData = new FormData();
    formData.append("token", token);
    console.log(token)

    var filelength = $('#file1')[0].files.length;

    console.log(filelength);
    var i=0
while (i<filelength){
  formData.append("file1", $('#file1')[0].files[i]);
      i++
}
console.log(formData)
    // for(int i=0 i<6; i++){
    // formData.append("file1", $('#file1')[0].files[i]);
    // }

    $.ajax({
      url: '/web/DemoImageController/uploadMult',
      type: 'post',
      data: formData,
      cache: false,
      processData: false,
      contentType: false,
      async: false
    }).done(function(res) {

    }).fail(function(res) {

    });
  }


</script>

</body>
</html>
