<style>
  #alarm-area {
    min-width: 260px;
    display: inline-block;
    text-align: right;
    margin-left: 10%;
    margin-top: 15px;
    cursor: pointer;
  }

  .navbar {
    min-width: 1260px;
  }

  .alert-text {
    color: #F56C6C;
    display: inline-block;
    width: 100px;
    margin: 5px 3px;
    height: 20px;
    line-height: 20px;

  }
</style>
<div class="row border-bottom">
  <nav class="navbar navbar-static-top" role="navigation" style="margin-bottom: 0">
    <div class="navbar-header">
      <a class="navbar-minimalize minimalize-styl-2 btn btn-primary"><i class="fa fa-bars"></i></a>

    </div>
    <div id="alarm-area" onclick="toNewTab()"></div>
    <ul class="nav navbar-top-links navbar-right">
      <li style="display:none">
        <a data-href="a/version/timeline.html?flag=0" class="opennewtab roll-nav count-info"><i
              class="fa fa fa-bell"></i><span class="label label-primary" id="markcount"></span></a>
      </li>
      <li class="dropdown" style="border-left: solid 1px #ddd">
        <a class="roll-navd ropdown-toggle count-info" data-toggle="dropdown">
          <i class="fa fa fa-user"></i>${userName!?html}<span class="caret"></span></a>
        <ul class="dropdown-menu dropdown-menu-right">
            <#if global.hasPermissions("sys:user:manage")>
              <li onclick="changeAccount()"><a>
                  <div><i class="fa fa-user fa-fw"></i>&nbsp;修改账号</div>
                </a></li>
            </#if>
          <li class="divider"></li>
          <li onclick="changePassword()"><a id="btn_changepwd">
              <div><i class="fa fa-key fa-fw"></i>&nbsp;修改密码</div>
            </a></li>
            <#--<li class="divider"></li>-->
            <#--<li><a href="/logout.html"><div><i class="fa fa-sign-out fa-fw"></i>&nbsp;退出系统</div></a></li>-->
        </ul>
      </li>
    </ul>
  </nav>
</div>
<script src="${global.staticPath!}static/plugins/jquery/jquery.min.js"></script>
<script>
  $(document).ready(function () {
    //每10秒执行一次
    <#if global.hasAlarm()>
    setInterval(getState, 10000);
    </#if>
  })

  function getState() {
    var shtml = "";
    $.ajax({
      "type": "get",
      "url": "a/realmis/allstate.json",
      "dataType": "json",
      "async": false,
      "success": function (data) {
        data = data.result;
        for (var i = 0; i < data.length; i++) {
          if (data[i].tzzt != '正常') {
            shtml += "<span class='alert-text'>" + data[i].beltName + "网络故障</span>";
          }
        }
        if (shtml != '') {
          shtml = "<img src='${global.staticPath!}static/img/alarm.gif' >" + shtml;
        }
      }
    });
    $("#alarm-area").html(shtml);
  }

  function toNewTab() {
    top.openNewTab("a/realmis/index.html", "异常记录", 50);
  }
</script>


