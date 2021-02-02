<#include "/common/head.ftl"/>
<#include "/common/cssfile_list.ftl"/>
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content  row">
  <div class="col-sm-12">
    <div class="ibox">
      <div class="ibox-title">
        <h5>查询</h5>
        <div class="ibox-tools"><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></div>
      </div>
      <div class="ibox-content">

      </div>
    </div>
    <div class="table-responsive ibox-content">
      <div class="clearfix" style="margin-bottom:10px;">
        <div class="grid-o pull-right" id="bootgrid-toolbar"></div>
      </div>
      <table id="table-sysRole"
             class="table table-condensed table-hover table-striped table-bordered no-margins">
        <thead>
        <tr>
          <th>序号</th>
          <th>角色</th>
          <th>编号</th>
          <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <#list roleList as obj>
          <tr data-id="${(obj.key)!}">
            <td>${(obj_index+1)!}</td>
            <td>${(obj.value)!}</td>
            <td>${(obj.key)!}</td>
            <td>
              <button class="btn btn-success btn-xs" onclick="bindAuth(this)">
                <i class="fa fa-key"></i>&nbsp;授权
              </button>
            </td>
          </tr>
        </#list>
        </tbody>
      </table>
    </div>
  </div>
</div>
<#include "/common/scriptfile.ftl"/>
<#include "/common/scriptfile_list.ftl"/>

<script>
  function bindAuth(obj) {
    // 把当前窗口的window和document写入到top.layerCallerWD
    if ("object" === typeof (top.layerCallerWD)) {
      top.layerCallerWD.set(window, document);
    }
    const roleId = $(obj).parent().parent().attr("data-id");
    top.layer.open({
      "type": 2,
      "title": "角色授权",
      "area": ["400px", "400px"],
      "btn": "保存",
      "content": "a/sysRole/authtree.html?rid=" + parseInt(roleId),
      "yes": function (index, layero) {
        // 调用子窗口的添加方法
        layero.find("iframe")[0].contentWindow.subSave();
      }
    });
    return false;
  }

  $(document).ready(function () {
  });

</script>
</body>
</html>
