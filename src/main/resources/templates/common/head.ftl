<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="utf-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <meta name="renderer" content="webkit"/>
  <title>纪检监察管理系统</title>
  <!--[if lt IE 9]>
  <meta http-equiv="refresh" content="0;/errors/404"/>
  <![endif]-->
  <base href="${springMacroRequestContext.contextPath}/"/>
  <link rel="shortcut icon" href="/static/favicon.ico">
  <link rel="stylesheet"
        href="${global.staticPath!}static/plugins/bootstrap/css/bootstrap.min.css"/>
  <link rel="stylesheet"
        href="${global.staticPath!}static/plugins/font-awesome/css/font-awesome.min.css"/>
  <link rel="stylesheet" href="${global.staticPath!}static/plugins/toastr/toastr.min.css"/>
  <link rel="stylesheet" href="${global.staticPath!}static/plugins/metisMenu/metisMenu.min.css"/>
  <link rel="stylesheet" href="${global.staticPath!}static/plugins/animate/animate.min.css"/>
  <link rel="stylesheet"
        href="${global.staticPath!}static/css/style.min.css?v=${global.jsVersion!}"/>
    <#if global.hasSepcialDomain()>
  <script>document.domain = "${global.domain!}";</script>
</#if>