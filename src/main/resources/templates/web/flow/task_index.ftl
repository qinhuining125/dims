<#include "/common/head.ftl"/>
<#include "/common/cssfile_list.ftl"/>
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content row">
  <div class="col-sm-12">
    <div class="ibox">
      <div class="ibox-title">
        <h5>流程图</h5>
        <div class="ibox-tools"><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></div>
      </div>
      <div class="ibox-content" style="width:100%;display: inline-block">
        <img src="${global.staticPath!}static/img/flow/task_flow.png" style="max-width: 100%">
        <img src="${global.staticPath!}static/img/flow/task2_flow.png" style="max-width: 100%;">
      </div>
    </div>
  </div>
</div>
<#include "/common/scriptfile.ftl"/>
<#include "/common/scriptfile_list.ftl"/>

</body>
</html>
