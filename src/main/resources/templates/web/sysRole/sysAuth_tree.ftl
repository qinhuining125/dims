<#-- 在角色管理页面使用，绑定角色和权限的关系 -->
<#include "/common/head.ftl"/>
<style>
  span.indent {
    margin-left: 10px;
    margin-right: 10px;
  }

  span.menu {
    margin-left: 10px;
  }

  .list-group-item.module {
    background-color: #343a40 !important;
    color: #fff !important;
  }

  .list-group-item.auth {
    cursor: pointer !important;
    padding: 10px !important;
  }

  .list-group-item.active {
    background-color: #379bde !important;
    color: #fff !important;
  }

  .list-group-item.active .fa-square-o, .list-group-item .fa-check-square-o {
    display: none;
  }

  .list-group-item.active .fa-check-square-o {
    display: initial;
  }
</style>
</head>
<body class="gray-bg mini-navbar">
<div class="wrapper ">
  <div class="col-xs-12">
    <ul id="list-auth" class="elements-list list-group">
      <li class="list-group-item module"><i class="fa fa-desktop"></i>
        <span class="menu">权限一览</span></li>
        <#list authAllList as obj>
          <li id="${obj.id}" class="list-group-item auth" onclick="getAuth(this)">
            <span class="indent"></span><span class="indent"></span>
            <i class="fa fa-square-o"></i><i class="fa fa-check-square-o"></i><span
                class="menu">${obj.authName?html}</span>
          </li>
        </#list>
    </ul>
  </div>
</div>
<#include "/common/scriptfile.ftl"/>
<script>
  var className = "active";
  var authListJson = ${userAuthList};
  $(document).ready(function () {
    for (var i = 0, l = authListJson.length; i < l; i++) {
      $("#" + authListJson[i]).addClass(className);
    }
  });

  <#-- 选中权限 -->

  function getAuth(obj) {
    obj = $(obj);
    if (obj.hasClass(className)) {
      obj.removeClass(className)
    } else {
      obj.addClass(className)
    }
  }

  <#-- 必须有的方法，在这里提交 -->

  function subSave() {
    // 内部窗口获取layer的索引
    const layerIndex = parent.layer.getFrameIndex(window.name);
    const ids = [];
    $("#list-auth").children(".active").each(function (i, n) {
      ids.push($(n).attr("id"));
    });
    CommonUtils.AjaxUtil.CommonAjaxData("a/sysRole/saveAuthtree.json", {
      "rid": "${rid!}",
      "ids": ids.join(",")
    }, {
      "loadingType": 2,
      "successCallback": function (data) {
        window.setTimeout(function () {
          top.layer.close(layerIndex);
        }, 50);
      }
    });
  }
</script>
</body>
</html>
