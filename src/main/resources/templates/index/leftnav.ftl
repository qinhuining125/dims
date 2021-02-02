<nav class="navbar-default navbar-static-side" role="navigation">
  <div class="nav-close">
    <i class="fa fa-times-circle"></i>
  </div>
  <div class="sidebar-collapse">
    <ul class="nav" id="side-menu">
      <li>
        <div class="dropdown profile-element" style="color:#fff;border-bottom:1px solid #3b4a5a">
          <div class="nav-top-h1"
               style="height:auto;padding:20px;border-bottom: 1px solid #283142;line-height:12px">
              <#--<img src="${global.staticPath!}static/img/logos/logo.png"
                   style="border-radius:50%;height:125px;margin-bottom:5px">-->
            <br>
            <a href="#">纪检监察管理系统</a>
          </div>
        </div>
        <div class="logo-element">
        </div>
      </li>
        <#if global.hasPermissions("sys:manage:index")>
          <li>
            <a href="#"><i class="fa fa-columns"></i><span class="nav-label">系统管理</span>
              <span class="fa arrow"></span></a>
            <ul class="nav nav-second-level">
              <li><a class="J_menuItem" href="/a/sysUser/index.html">用户管理</a></li>
              <li><a class="J_menuItem" href="/a/sysRole/index.html">用户角色</a></li>
              <li><a class="J_menuItem" href="/a/sysLog/index.html">系统日志</a></li>
              <li><a class="J_menuItem" href="/a/reportType/index.html">报送类型</a></li>
            </ul>
          </li>
        </#if>
      <li>
        <a href="#"><i class="fa fa-edit"></i><span class="nav-label">纪检监察管理</span>
          <span class="fa arrow"></span></a>
        <ul class="nav nav-second-level">
          <li><a class="J_menuItem" href="/a/incorruptAdvice/index.html">廉政建议</a></li>
          <li><a class="J_menuItem" href="/a/clueReport/index.html">线索上报</a></li>
          <li><a class="J_menuItem" href="/a/taskInfo/index.html">任务指派</a></li>
        </ul>
      </li>
      <#if (roleId==1007 || roleId==1008 || roleId==1009 || roleId==1010 || roleId==1011 )>
        <li>
          <a href="#"><i class="fa fa-edit"></i><span class="nav-label">巡察工作管理</span>
            <span class="fa arrow"></span></a>
          <ul class="nav nav-second-level">
            <li><a class="J_menuItem" href="/a/inspectionPublicity/index.html">巡察宣传</a></li>
            <li><a class="J_menuItem" href="/a/revealInfo/index.html">百姓举报</a></li>
            <li><a class="J_menuItem" href="/a/patrolInfo/index.html">后台管理</a></li>
          </ul>
        </li>
     </#if>
      <li>
        <a href="#"><i class="fa fa-magic"></i><span class="nav-label">流程管理</span>
          <span class="fa arrow"></span></a>
        <ul class="nav nav-second-level">
          <li><a class="J_menuItem" href="/a/flow/clue.html">上报流程</a></li>
          <li><a class="J_menuItem" href="/a/flow/advice.html">建议流程</a></li>
          <li><a class="J_menuItem" href="/a/flow/task.html">任务流程</a></li>
        </ul>
      </li>
      <li>
        <a href="#"><i class="fa fa-bar-chart-o"></i><span class="nav-label">统计分析</span>
          <span class="fa arrow"></span></a>
        <ul class="nav nav-second-level">
          <li><a class="J_menuItem" href="/a/analysis/clue.html">上报统计</a></li>
          <li><a class="J_menuItem" href="/a/analysis/advice.html">建议统计</a></li>
          <li><a class="J_menuItem" href="/a/analysis/task.html">任务统计</a></li>
          <li><a class="J_menuItem" href="/a/analysis/report.html">举报统计</a></li>
        </ul>
      </li>
    </ul>
  </div>
</nav>
<#-- 这个是必须要留存的，用于JS触发Tab页 -->
<a class="J_menuItem" href="#" id="top-hidden-link" style="display: none"></a>