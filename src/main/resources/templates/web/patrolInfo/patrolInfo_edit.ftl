<#include "/common/head.ftl"/>
<html xmlns:th="http://www.thymeleaf.org">
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content row">
  <div class="col-xs-12">
    <div class="form-middle">
      <form id="form-patrolInfo"
            action="${mapping!}/saveData.json?${global.addSign!}=${(entity.id)!}"
            class="form-horizontal" method="post">
        <input type="hidden" name="id" value="${(entity.id)!}"/>
        <div class="col-xs-12">
          <div class="form-group">
            <label class="col-xs-3 control-label"><span class="text-danger">*</span>巡察名称：</label>
            <div class="col-xs-9">
              <input type="string" name="patrolName" id="patrolName" class="form-control" placeholder="请输入巡察名称"
                     value="${(entity.patrolName)!}" onchange="checkPatrolName(this)" />
              <span class="help-block m-b-none"></span>
            </div>
          </div>
        </div>
        <div class="col-xs-12">
          <div class="form-group">
            <label class="col-xs-3 control-label"><span class="text-danger">*</span>巡察单位：</label>
            <div class="col-xs-9">
              <input type="string" name="patrolUnit" id="patrolUnit" class="form-control" placeholder="请输入巡察单位"
                     value="${(entity.patrolName)!}"/>
              <span class="help-block m-b-none"></span>
            </div>
          </div>
        </div>
        <div class="col-xs-12">
          <div class="form-group">
            <label class="col-xs-3 control-label"><span class="text-danger">*</span>巡察时间：</label>
            <div class="col-xs-9">
              <input type="text" class="form-control" id="timeRange"
                     placeholder="巡察时间"/>
              <span class="help-block m-b-none"></span>
              <input type="hidden" id="startTime" name="startTime">
              <input type="hidden" id="endTime" name="endTime">
            </div>
          </div>
        </div>
        <div class="col-xs-12">
          <div class="form-group">
            <label class="col-xs-3 control-label"><span class="text-danger">*</span>二维码：</label>
            <div class="col-xs-9">
              <!--这里的背景图片需要修改一下-->
              <img  id="addImg" alt="qrcode" src="${global.staticPath!}static/img/logos/ewm.jpg" style="width:215px;height: 235px"/>
              <span class="help-block m-b-none"></span>
            </div>
          </div>
        </div>
      </form>
      <div class="col-xs-12">
        <button class="btn btn-success btn-xs"  style="width: 100%;height: 10%;"  onclick="createEwm()">
          <i class="fa fa-key"></i>&nbsp;生成二维码
        </button>
      </div>
    </div>
  </div>
</div>
<#include "/common/scriptfile.ftl"/>
<script src="${global.staticPath!}static/plugins/laydate/laydate.js"></script>
<script>

  function checkPatrolName(obj){
    var f=checkName(obj);
    if(!f){
      alert("巡察名称已经存在，请进行更换！");
      $("#patrolName").val("");
      $("#patrolName").focus();
    }
  }
  //时间插件
  laydate.render({
    "elem": "#timeRange",
    "range": "至",
    "format": "yyyy-MM-dd",
    "trigger": "click",
    "done": function (value, startDate, endDate) {
      if (value) {
        const array = value.split("至");
        $("#startTime").val(new Date(array[0]).Format("yyyy-MM-dd"));
        $("#endTime").val(array[1]);
      } else {
        $("#startTime").val("");
        $("#endTime").val("");
      }
    }
  });

  var formObj;
  var readOnlyFlag;
  $(document).ready(function () {
    formObj = $("#form-patrolInfo");
    <#-- 这是是作为一个表单验证的示例，针对具体的逻辑进行验证 -->
    formObj.validate({
      "rules": {
        "patrolName": {
          "required": true,
          "maxlength": 100
        },
        "patrolUnit": {
          "required": true,
          "maxlength": 100
        }
      },
      "messages": {
        "patrolName": {
          "required": "请输入巡察名称",
          "maxlength": "长度不能大于100"
        },
        "patrolUnit": {
          "required": "请输入巡察单位",
          "maxlength": "长度不能大于100"
        }
      }
    });
  });

  <!--时间取值变化的话，需要做check-->
  function checkTime(){
    var status=false;
    $.ajax({
      type: "POST",
      url: "/a/patrolInfo/checkPatrolTime",
      data: "startTime="+$("#startTime").val()+"&endTime="+$("#endTime").val(),
      async:false,
      success: function(data){
        status=data;
      },
      error:function(args){
        console.log(args);
      }
    });
    return status;
  }

  <!--时间取值变化的话，需要做check-->
  function checkName(obj){
    var s=false;
    $.ajax({
      type: "POST",
      url: "/a/patrolInfo/checkPatrolName",
      data:{"patrolName":obj.value},
      async:false,
      success: function(data){
        s=data;
      },
      error:function(args){
        console.log(args);
      }
    });
    return s;
  }

  <#-- 创建二维码-->
  function createEwm() {
    if (formObj.valid()) {//如果验证不通过，会出提示信息，验证通过的话
      var stime=$("#startTime").val();
      if(typeof stime== "undefined" || stime == null || stime == ""){
        alert("请填入巡察时间");
        return false;
      }
       if(checkTime()){
         var addImg=document.getElementById("addImg");
         addImg.src = "/a/patrolInfo/qrimage?words="+$('#patrolName').val();//这里返回的是图片流
         $("#patrolName").attr("readOnly",true);
         $("#patrolUnit").attr("readOnly",true);
         $("#timeRange").attr("readOnly",true);
         readOnlyFlag=true;
       }else{
         alert("时间与已有的巡察时间段冲突，请重新填写");
         $("#startTime").val("");
         $("#endTime").val("");
         $("#timeRange").attr("readOnly",false);
         $("#timeRange").val("");
         return false;
       }
    }
  }
  /**
   * 添加和修改
   */
  <#-- 必须有的方法，在这里提交 -->
  function subSave() { //提前之前生成了二维码，已经做了所有的验证。这里不再需要验证。
    if(readOnlyFlag){
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
    }else{
      alert("请先填报信息，生成二维码，再进行提交");
    }
  }



</script>
</body>
</html>
