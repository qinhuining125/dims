<#include "/common/head.ftl"/>
</head>
<body class="fixed-sidebar full-height-layout gray-bg pace-done" style="overflow: hidden">
<div class="pace  pace-inactive">
  <div class="pace-progress" data-progress-text="100%" data-progress="99" style="width: 100%;">
    <div class="pace-progress-inner"></div>
  </div>
  <div class="pace-activity"></div>
</div>
<div id="wrapper">
    <#--左侧导航开始-->
    <#include "/index/leftnav.ftl"/>
    <#--左侧导航结束-->
    <#--右侧部分开始-->
  <div id="page-wrapper" class="gray-bg dashbard-1">
      <#--右侧顶部导航开始-->
      <#include "/index/topnav.ftl"/>
      <#--右侧顶部导航结束-->
      <#--右侧顶部工具条开始-->
      <#include "/index/toptools.ftl"/>
      <#--右侧顶部工具条结束-->
    <div class="row J_mainContent" id="content-main">
      <iframe class="J_iframe" name="iframe-1" width="100%" height="100%" data-id="welcome.html"
              src="welcome.html" frameborder="0" seamless></iframe>
    </div>
      <#--底部-->
      <#include "/index/footer.ftl"/>
  </div>
    <#--右侧部分结束-->
</div>
<button id="COMMONTOPBUTTON" style="display:none;width:1px;height:1px"></button>
<#include "/common/scriptfile.ftl"/>
<script>
  $(document).ready(function () {
    if (window.top !== window.self) {
      window.top.location = window.location;
    }
  });
  <#--
   在标签内的触发新tab事件
   top.openNewTab("http://www.baidu.com", "更新说明", 20);
   -->
  <#-- 清除iframe的焦点 -->
  var clearIframeFocus = function () {
    $("#COMMONTOPBUTTON", top.document).show().trigger("focus").hide();
  };
  <#--
    layer调用方的window和document
    top.layerCallerWD.set(window, document)
    top.layerCallerWD.get().window....
  -->
  var layerCallerWD = (function () {
    var wd = {
      "window": false,
      "document": false,
      "others": false
    };
    return {
      "set": function (w, d, others) {
        wd = {
          "window": w,
          "document": d,
          "others": others
        };
      },
      "get": function () {
        return wd;
      },
      "clear": function () {
        wd = {
          "window": false,
          "document": false,
          "others": false
        };
      }
    }
  })();

  function openLayer(conf) {
    // 把当前窗口的window和document写入到top.layerCallerWD
    var _conf = $.extend({}, defaultsLayer, conf);
    layerIndex = top.layer.open(_conf);
  }

  <#-- 修改账号 -->

  function changeAccount() {
    layer.open({
      "type": 2,
      "title": "修改登录账号",
      "area": ["500px", "250px"],
      "btn": ["确定"], //按钮
      "maxmin": false,
      "content": "a/sysUser/edit_info.html",
      "yes": function (index, layero) {
        // 调用子窗口的添加方法
        layero.find("iframe")[0].contentWindow.subSave();
      }
    });
  }

  <#-- 修改密码 -->

  function changePassword() {
    layer.open({
      "type": 2,
      "title": "修改登录密码",
      "area": ["550px", "350px"],
      "btn": ["确定"], //按钮
      "maxmin": false,
      "content": "a/sysUser/edit_pwd.html",
      "yes": function (index, layero) {
        // 调用子窗口的添加方法
        layero.find("iframe")[0].contentWindow.subSave();
      }
    });
  }
</script>
<#include "/common/console.ftl"/>
</body>
</html>