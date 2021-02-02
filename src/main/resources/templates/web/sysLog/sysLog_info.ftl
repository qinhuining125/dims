<#include "/common/head.ftl"/>
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content  row">
  <div class="col-xs-12">
    <div class="row">
      <div class="col-sm-6">
          <#if logEntity.status == 1 >
            <div class="alert alert-success alert-dismissible">
              <h5><i class="icon fa fa-check mr-2"></i>执行成功</h5>${(logEntity.uri)!}
            </div>
          <#else>
            <div class="alert alert-danger alert-dismissible">
              <h5><i class="icon fa fa-warning mr-2"></i>执行失败</h5>${(logEntity.uri)!}
            </div>
          </#if>
        <div class="ibox">
          <div class="ibox-title">
            <h5>执行信息</h5>
          </div>
          <div class="ibox-content">
            <table class="table table-bordered">
              <tr>
                <td class="font-bold" style="width:100px">执行时间</td>
                <td>${(logEntity.startTime)?number_to_datetime}</td>
              </tr>
              <tr>
                <td class="font-bold">执行耗时</td>
                <td>${(logEntity.runTime)!0}毫秒</td>
              </tr>
              <tr>
                <td class="font-bold">请求方法</td>
                <td>${(logEntity.method)!}</td>
              </tr>
              <tr>
                <td class="font-bold">请求类型</td>
                <td>${(logEntity.type)!}</td>
              </tr>
              <tr>
                <td class="font-bold">操作描述</td>
                <td>${(logEntity.description)!}</td>
              </tr>
            </table>
          </div>
        </div>
      </div>

      <div class="col-sm-6">
        <div class="ibox">
          <div class="ibox-title"><h5>用户信息</h5></div>
          <div class="ibox-content">
            <table class="table table-bordered">
              <tr>
                <td class="font-bold" style="width:100px">登录IP</td>
                <td>${(logEntity.ip)!}</td>
              </tr>
              <tr>
                <td class="font-bold">设备UA</td>
                <td>${(logEntity.userAgent)!}</td>
              </tr>
                <#if userEntity??>
                  <tr>
                    <td class="font-bold">用户ID</td>
                    <td>${(userEntity.id)!}</td>
                  </tr>
                  <tr>
                    <td class="font-bold">登录账号</td>
                    <td>${(userEntity.userAccount)!}</td>
                  </tr>
                  <tr>
                    <td class="font-bold">用户名</td>
                    <td>${(userEntity.userName)!}</td>
                  </tr>
                </#if>
            </table>
          </div>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-sm-6">
        <div class="ibox">
          <div class="ibox-title"><h5>输入参数</h5></div>
          <div class="ibox-content" style="overflow-x:auto">
            <p>${param!}</p>
          </div>
        </div>
      </div>

      <div class="col-sm-6">
        <div class="ibox">
          <div class="ibox-title"><h5>返回结果</h5></div>
          <div class="ibox-content" style="overflow-x:auto">
            <p>${result!}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<#include "/common/scriptfile.ftl"/>
</body>
</html>
