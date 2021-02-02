<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="utf-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <meta name="renderer" content="webkit"/>
  <title>纪检监察管理系统</title>
  <base href="${springMacroRequestContext.contextPath}/"/>
  <link rel="shortcut icon" href="${global.jsprefix!}/static/favicon.ico"/>
  <link rel="stylesheet"
        href="${global.staticPath!}static/plugins/bootstrap/css/bootstrap.min.css"/>
  <link rel="stylesheet"
        href="${global.staticPath!}static/plugins/font-awesome/css/font-awesome.min.css"/>
  <link rel="stylesheet" href="${global.staticPath!}static/plugins/animate/animate.min.css"/>
  <link rel="stylesheet"
        href="${global.staticPath!}static/css/style.min.css?v=${global.jsVersion!}"/>
</head>
<body class="gray-bg">
<div class="middle-box text-center animated fadeInDown">
  <h1><img src="${global.staticPath!}static/img/404-500error.png"/></h1>
  <h3 class="font-bold">系统温馨提示：</h3>
  <div class="error-desc">
    <div class="mg-t-10">${message!}</div>
    <input type="button" onclick="returnIndex()" class="btn btn-primary m-t" value="返回首页">
  </div>
</div>
<script src="${global.staticPath!}static/plugins/jquery/jquery.min.js"></script>
<script src="${global.staticPath!}static/plugins/bootstrap/js/bootstrap.min.js"></script>
<script src="${global.staticPath!}static/js/gohome.js?v=${global.jsVersion!}"></script>
<script src="${global.staticPath!}static/js/contabs.js?v=${global.jsVersion!}"></script>
<#if showStackTrace == 1>
<#--显示错误堆栈，?trace=trace -->
  <div>
    <div>${describe!}</div>
    <div>${exception!}</div>
    <pre>
    ${trace!}
  </pre>
  </div>
<#else>
  <div style="display:none">
    <div>${describe!}</div>
    <div>${exception!}</div>
  </div>
</#if>
<script type="application/javascript">
  function returnIndex() {
    window.location.replace("index.html");
  }
</script>
</body>
</html>