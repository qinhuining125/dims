<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>百姓举报</title>
    <meta name="viewport" content="initial-scale=1, width=device-width, maximum-scale=1, user-scalable=no">
    <script src="/static/html/js/jquery-3.5.1.min.js" type="application/javascript"></script>
    <script src="/static/html/layui/layui.all.js"type="application/javascript"></script>
    <script src="/static/html/js/picker.min.js"type="application/javascript"></script>
    <script src="/static/html/js/town-ship.js"type="application/javascript"></script>
    <link rel="stylesheet" href="/static/html/layui/css/layui.css" media="all" type="text/css">
    <!--    <link rel="stylesheet" href="css/layui.mobile.css">-->
    <#include "/common/head.ftl"/>
</head>

<body>
    <style>
        body {
            font-family: PingFangSC-Regular, "Helvetica Neue", Helvetica, Arial, sans-serif;
            font-size: 14px;
            text-align: center;
        }

        .form-diy {
            font-size: 14px;
        }

        .form-group-diy {
            border-bottom: 1px solid #e4e6e6;
            padding: 5px 10px !important;
        }

        .form-input-style {
            border: none;
            /*border-bottom: 1px solid #e4e6e6;*/
            -webkit-box-shadow: none;
            border-radius: 0;
            line-height: 36px;
        }

        .left {
            text-align: left !important;

        }

        .layui-form-item .layui-input-inline {
            margin: 0;
        }

        .layui-form-item {
            margin-bottom: 0px;
        }

        .uploadFile {
            width: 90px;
            height: 90px;
            border-radius: 1px;
            border: dashed 1px #d8d8d8;
        }


        .alert {
            font-size: 10px;
            font-weight: normal;
            font-stretch: normal;
            line-height: 14px;
            color: #5e5f5f;
            /*margin: 10px 24px;*/
            text-align: left;
        }

        .header {
            width: 100%;
            height: 44px;
            background-color: #156dd8;
            box-shadow: 0px 5px 8px 0px rgba(0, 0, 0, 0.03);
            text-align: center;
        }

        .header-title {
            font-size: 18px;
            font-weight: normal;
            font-stretch: normal;
            letter-spacing: 0px;
            color: #ffffff;
            line-height: 44px;
        }

        .layui-form-checked[lay-skin=primary] i {
            border-color: #156dd8 !important;
            background-color: #156dd8;
            color: #fff;
        }

        .layui-form-label {
            text-align: left;
        }

        .picker .picker-panel .picker-choose .confirm {
            color: #156dd8;
        }

        .submit-btn {
            width: 268px;
            height: 48px;
            background-color: #156dd8;
            border-radius: 6px;
            opacity: 0.95;
            border: none;
            font-size: 18px;
            font-weight: normal;
            font-stretch: normal;
            letter-spacing: 0px;
            color: #ffffff;
            margin-top: 20px;
        }

        .pic-container {
            width: 90px;
            height: 90px;
            margin: 10px;
            display: inline-block;
        }

        .layui-upload-img {
            width: 90px;
            height: 90px;

        }

        .icon-close {
            width: 20px;
            height: 20px;
            position: absolute;
            right: -5px;
            top: -3px;
            color: #b6c2d9;
            /*background: #b6c2d9;*/
        }
        label{
            margin-bottom: 0;
        }
    </style>
    <header class="header">
        <div class="header-title">百姓举报</div>
    </header>
    <div class="alert">请您如实详细填写您的举报信息</div>
<#--    <input type="text" name="type" id="isanonymous" value="1" />-->
    <div class=" layui-form-item form-group-diy" style="margin-top: -20px;">
        <label class="layui-form-label" style="width: 25%">是否匿名</label>
        <div class="layui-input-block" style="margin-left: 0;text-align: left">
<#--            <input type="checkbox" name="state[anonymous]" lay-skin="primary" id="isanonymous" onclick="anonymousis()" title="匿名举报" >-->
            <input type="checkbox" name="state[anonymous]" lay-skin="primary" id="isanonymous" title="匿名举报" style="margin-top: 12px">
<#--            <button type="button" onclick="anonymousis()" class="layui-btn">匿名举报</button>-->
        </div>
    </div>
    <div class="layui-form-item form-group-diy">
        <label class="layui-form-label" style="width: 25%"><span class="text-danger">*</span>乡镇村:</label>
        <div class="col-xs-3" style="padding-top: 0px;padding-top: 7px">
            <select class="" id="township" onchange="searchVillage(this);">
                <option value="">--请选择--</option>
                <#list areaList2 as obj>
                    <option value="${obj.areaCode}">${obj.areaName}</option>
                </#list>
            </select>
            <span class="help-block m-b-none"></span>
        </div>
<#--        <label class="layui-form-label" >村:</label>-->
        <div class="col-xs-3" style="padding-left: 60px;padding-top: 7px">
            <select id="valliage"  class="" onchange="choseArea(this);">
            </select>
            <span class="help-block m-b-none"></span>
        </div>
    </div>
    <form class="layui-form " action="" id="demo">
        <div class="layui-form-item form-group-diy" >
            <label class="layui-form-label" style="width: 25%"><span class="text-danger">*</span>巡察轮次</label>
            <div class="layui-input-block">
                <input class="form-input-style" type="text" disabled="disabled" value="${patrolName}"/>
                <input type="hidden" name="inspectionId" id="inspectionId" value="${inspectionId}"/>
            </div>
        </div>
        <input type="hidden" name="townCode" id="townCode" value=""/>
        <input type="hidden" name="type" id="type" value="1" />
        <input type="hidden" name="villageCode" id="villageCode" value=""/>
        <div class="layui-form-item form-group-diy" id="username" style="display:block">
            <label class="layui-form-label">姓名</label>
            <div class="layui-input-block">
                <input class="form-input-style" type="text" name="userName"
                        placeholder="请输入举报人" autocomplete="off" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item form-group-diy" id="mobilePhone">
            <label class="layui-form-label" style="width: 25%">联系方式</label>
            <div class="layui-input-block">
                <input class="form-input-style" type="tel" name="contact" value=""
                        placeholder="请输入手机号" id="contact" phone autocomplete="off"
                    class="layui-input">
<#--                <input type="tel" name="tel" value="{$data.tel|default=''}" autocomplete="off" maxlength=11-->
<#--                       placeholder="请输入手机号" lay-verify="required|phones" class="layui-input">-->
<#--                <input type="tel" name="tel"  autocomplete="off"-->
<#--                       maxlength=11 placeholder="请输入手机号" lay-verify="required|phones">-->
            </div>
        </div>
        <div class="layui-form-item form-group-diy">
            <label class="layui-form-label" style="width: 25%"><span class="text-danger">*</span>举报对象</label>
            <div class="layui-input-block">
                <input class="form-input-style" type="text" name="revealTarget" lay-verify="required"
                    lay-reqtext="请输入被举报对象" placeholder="请输入被举报对象" autocomplete="off" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item form-group-diy" style="border: none">
            <label for="inputJubao3" class="layui-form-label" style="width: auto;margin-bottom: 5px"><span class="text-danger">*</span>举报详情<span
                    style="font-size: 10px;color: #919293;margin-left: 15px">最多可以输入200字</span></label>
            <textarea name="content" required lay-verify="required" placeholder="请输入" class="layui-textarea" id="inputJubao3"
                style="margin-left: 9px;width: 96%;height: 170px;margin-top: 15px"></textarea>
        </div>
        <div style="text-align: left;margin:10px 21px">图片上传</div>
        <div class="layui-upload" style="text-align: left;padding: 0 20px">
            <div class="layui-row">
                <div class="layui-col-xs9">
                    <input type="file" name="file1" id="file1" onchange="setImagePreviews();" multiple="multiple" />
                </div>
                <div class="layui-col-xs3">
                    <input type="button" style="padding-left: 10px" value="上传" onclick="doUpload()" />
                </div>
            </div>

            <div id="localImag">
                <img id="preview" width=-1 height=-1 style="diplay:none" />
            </div>

            <input type="hidden" name="imageArray" id="imageArray" value=""/>
        </div>
        <button type="submit" class="submit-btn" lay-submit="" lay-filter="demo"
            id="LAY-component-form-getval">立即提交</button>
        <div style="padding-bottom: 20px"></div>
    </form>

    <script src="https://twitter.github.io/typeahead.js/js/handlebars.js"></script>
    <script id="template-villages" type="text/x-handlebars-template">
        <option value="">--请选择--</option>
        {{#each this}}
        <option value="{{areaCode}}">{{areaName}}</option>
        {{/each}}
    </script>
    <script  type="text/javascript">

        // function anonymousis() {
        //     var isanonymous=document.getElementById("username");
        //     console.log(isanonymous)
        //     if (isanonymous== true) {
        //         alert("1")
        //     }else {
        //         alert("2")
        //     }
        // }
        $(function(){
            $("#isanonymous").click(function() {
                if ($(this).is(":checked")== true) {
                    $("#type").val(0);
                    $("#username").css("display","none");
                    $("#mobilePhone").css("display","none");
                    //选中触发事件
                } else {
                    //取消选中触发事件
                    $("#type").val(1);
                    $("#username").css("display","block");
                    $("#mobilePhone").css("display","block");
                }
            });
        })


        // var username=document.getElementById(username);
        // console.log(username)
        function searchVillage(obj) {
            const township = $(obj).val();
            if (township == '') {
                return false;
            }
            console.log(township)
            $("#townCode").val()
            const array = township.split(",");
            $("#receiveId").val(array[0]);
            $("#receiveRoleId").val(1003);

            $("#townCode").val(array[0])
            $.ajax({
                "type": "GET",
                "cache": false,
                "url": "/village1/list2.json",
                "data": {
                    "areaCode": array[0]
                },
                "dataType": "json",
                "success": function (data) {
                    console.log(data);
                    const template = Handlebars.compile($("#template-villages").html());
                    $("#valliage").html(template(data.result));
                }
            });
        }
        function choseArea(obj) {
            const township = $(obj).val();
            $("#villageCode").val(township);
        }
    </script>
    <script>
    // var token
<#--        安卓调用-->
//         sendInfoToJava()
//         function sendInfoToJava(){
//             //调用android程序中的方法，并传递参数
//             // var name = document.getElementById("name_input").value;
//             // window.AndroidWebView.showInfoFromJs(name);
//         }
//         //在android代码中调用此方法
//         function showInfoFromJava(appToken){
//             token=appToken
//             alert(appToken);
//             //yincang biaotidiv
//             $(".header").css("display","none");
//         }
<#--        安卓调用-->
        function setImagePreviews(avalue) {
            var docObj = document.getElementById("file1");
            var dd = document.getElementById("localImag");
            dd.innerHTML = "";
            var fileList = docObj.files;
            fileimage = docObj.files;
            for (var i = 0; i < fileList.length; i++) {
                dd.innerHTML += "<div style='float:left' > <img id='img" + i + "'  /> </div>";
                var imgObjPreview = document.getElementById("img" + i);
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
        var imagearr=[]
        function doUpload() {
            var formData = new FormData();
            var filelength = $('#file1')[0].files.length;
            if (filelength == 0) {
                alert("没有可上传图片")
            }
            if (filelength > 0) {
                var i = 0
                while (i < filelength) {
                    formData.append("file1", $('#file1')[0].files[i]);
                    console.log(formData)
                    console.log($('#file1')[0].files[i])
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
                }).done(function (res) {
                    console.log(res)
                    console.log(formData)
                    if (res[0] == "false") {
                        alert("服务器异常，图片上传失败")
                    } else {
                        imagearr=res
                        alert("图片上传成功")
                        $('#imageArray').val(res)
                    }
                }).fail(function (res) {
                    alert("请求异常，图片上传失败")
                    console.log(res)
                });
            }

        }
        //由于模块都一次性加载，因此不用执行 layui.use() 来加载对应模块，直接使用即可：
        !function () {
            var layer = layui.layer
                , form = layui.form
                , upload = layui.upload;

            //自定义验证规则
            form.verify({
                contact: function(value) {
                    function check(money) {
                        var patrn = /^[1-9]\d*$/;
                        if (value == '') {
                            return true;
                        }
                        if (!patrn.exec(value)) {
                            return false;
                        }
                        return true;
                    }
                }
            });
            form.render();
            //监听提交
            form.on('submit(demo)', function (data) {
                // layer.alert(JSON.stringify(data.field), {
                //     title: '最终的提交信息'
                // })
                console.log(
                    data
                )
                var type=data.field.type;
                var b= parseInt(type);
                data.field.type=b;
                data.field.imageArray=imagearr
                $.ajax({
                    url:'/reporting/saveData.json',
                    type:'POST',
                    dataType:'json',
                    data:JSON.stringify(data.field),
                    contentType:'application/json',
                    success:function(msg){
                        alert(msg.message);
                    },
                    error: function(msg){
                        alert(msg.message);
                    }
                });
                return false;
            });
        }();
    </script>
</body>
</html>