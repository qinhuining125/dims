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
        <form class="form-horizontal" id="frm-search-sysUser">
          <div class="row">
            <div class="col-xs-6 col-sm-4 col-lg-3">
              <div class="form-group">
                <label class="col-sm-4 control-label">用户名：</label>
                <div class="col-sm-7">
                  <input type="text" name="userName" placeholder="搜索用户名" class="form-control"/>
                </div>
              </div>
            </div>
            <div class="col-xs-6 col-sm-4 col-lg-3">
              <div class="form-group">
                <label class="col-sm-4 control-label">状态：</label>
                <div class="col-sm-7">
                  <select name="enabled" class="form-control">
                    <option value="">全部</option>
                    <option value="1">启用</option>
                    <option value="0">禁用</option>
                  </select>
                </div>
              </div>
            </div>
            <div class="col-xs-6 col-sm-4 col-lg-3">
              <div class="form-group">
                <label class="col-sm-4 control-label">角色：</label>
                <div class="col-sm-7">
                  <select name="roleId" class="form-control">
                    <option value="">全部</option>
                    <option value="1001">村（社）网格员</option>
                    <option value="1002">纪检监察网格联络员</option>
                    <option value="1003">乡镇纪委管理员</option>
                    <option value="1004">县纪委联系室/巡察办</option>
                    <option value="1005">分管领导</option>
                    <option value="1006">县级领导班子</option>
                    <option value="1007">超级管理员</option>
                    <option value="1008">巡察办主任</option>
                    <option value="1009">巡察一组</option>
                    <option value="1010">巡察二组</option>
                    <option value="1011">巡察三组</option>
                  </select>
                </div>
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col-xs-6 col-sm-4 col-lg-3">
              <div class="form-group">
                <label class="col-sm-4 control-label">乡镇:</label>
                <div class="col-sm-7">
                  <select class="form-control" id="township" name="townCode" onchange="searchVillage(this);">
                    <option value="">全部</option>
                    <#list areaList as obj>
                      <option value="${(obj.areaCode!)}">${(obj.areaName)!}</option>
                    </#list>
                  </select>
                </div>
              </div>
            </div>
            <div class="col-xs-6 col-sm-4 col-lg-3">
              <div class="form-group">
                <label class="col-sm-4 control-label">村:</label>
                <div class="col-sm-7">
                  <select id="valliage" class="form-control" name="areaCode" onchange="choseArea(this);">
                    <option value="">全部</option>
                  </select>
                </div>
              </div>
            </div>

          </div>
          <div class="text-center">
            <button id="btn-search-sysUser" class="btn btn-primary"><i class="fa fa-search"></i>&nbsp;查询
            </button>
            <button type="reset" class="btn"><i class="fa fa-remove"></i>&nbsp;重置</button>
          </div>
        </form>
      </div>
    </div>
    <div class="table-responsive ibox-content">
      <div class="clearfix" style="margin-bottom:10px;">
        <div class="pull-left">
            <button type="button" class="btn btn-primary" id="btn-add-sysUser"><i
                  class="fa fa-plus"></i>&nbsp;新增
            </button>
          <button type="button" class="btn btn-primary" onclick="importExcel()"><i
                class="fa fa-file-excel-o"></i>&nbsp;导入
          </button>
          <button type="button" class="btn btn-danger" id="btn-del-sysUser"><i
                class="fa fa-remove"></i>&nbsp;删除
          </button>
            <#if global.hasPermissions("sys:user:manage")>
              <button type="button" class="btn btn-warning" onclick="exportData(this)"><i
                    class="fa fa-file-excel-o mr-2"></i>&nbsp;导出
              </button>
            </#if>
        </div>
        <div class="grid-o pull-right" id="bootgrid-toolbar"></div>
      </div>
      <table id="table-sysUser"
             class="table table-condensed table-hover table-striped table-bordered no-margins">
        <thead>
        <tr>
          <th data-column-id="id" data-width="60px" data-formatter="no" data-sortable="false"
              data-identifier="true">序号
          </th>
          <th data-column-id="userName" data-order="desc" data-formatter="fun_name_sex"
              data-visible="true" data-sortable="true">用户名
          </th>
          <th data-column-id="userAccount" data-order="desc"
              data-visible="true" data-sortable="true">登录账号
          </th>
          <th data-column-id="enabled" data-order="desc" data-width="80px"
              data-formatter="fun_enabled" data-visible="true" data-sortable="true">
            状态
          </th>
          <th data-column-id="sex" data-order="desc" data-width="80px"
              data-visible="true" data-sortable="true">
            性别
          </th>
          <th data-column-id="areaCode" data-order="desc" data-visible="true" data-sortable="true">
            地区编号
          </th>
          <th data-column-id="reportUserAreaName" data-order="desc" data-visible="true" data-sortable="true"
              data-formatter="fun_township_userName">
            乡镇
          </th>
          <th data-column-id="areaName" data-order="desc" data-visible="true" data-sortable="true">
            地区名称
          </th>
          <th data-column-id="enabled" data-order="desc" data-formatter="fun_name_role"
              data-visible="true" data-sortable="true">角色
          </th>
          <th data-column-id="link" data-formatter="commands" data-custom="true"
              data-sortable="false" data-width="300px">操作
          </th>
        </tr>
        </thead>
      </table>
    </div>
  </div>
</div>
<#include "/common/scriptfile.ftl"/>
<#include "/common/scriptfile_list.ftl"/>
<script src="${global.staticPath!}/static/utils/handlebars-tool.js"></script>
<script id="template-sysUser" type="text/x-handlebars-template">
  <td data-id="{{id}}">
    <button class="btn btn-primary btn-xs mg-5 o-ch ops-view"><i class="fa fa-eye"></i>&nbsp;查看
    </button>
    <button class="btn btn-warning btn-xs mg-5 o-c ops-edit"><i class="fa fa-edit"></i>&nbsp;修改
    </button>
    <button class="btn btn-danger btn-xs mg-5 o-d ops-del"><i class="fa fa-close"></i>&nbsp;删除
    </button>
    <button class="btn btn-success btn-xs mg-5 o-c" onclick="bindRole(this)"><i
          class="fa fa-user"></i>&nbsp;角色
    </button>
    <button class="btn btn-warning btn-xs mg-5 o-c" onclick="bindArea(this)"><i
          class="fa fa-user"></i>&nbsp;权限
    </button>
    <button class="btn btn-danger btn-xs mg-5 o-c" onclick="bindPwd(this)"><i
          class="fa fa-key"></i>&nbsp;密码
    </button>
  </td>
</script>

<script id="template-villages" type="text/x-handlebars-template">
  <option value="">--请选择--</option>
  {{#each this}}
  <option value="{{areaCode}}">{{areaName}}</option>
  {{/each}}
</script>

<script id="template-view-sysUser" type="text/x-handlebars-template">
  <div class="form-horizontal" style="margin: 20px;">
    <div class="form-group">
      <label class="col-xs-4 control-label">登录账号：</label>
      <div class="col-xs-8">
        <div class="form-control-static">{{userAccount}}</div>
      </div>
    </div>
    <div class="form-group">
      <label class="col-xs-4 control-label">用户名：</label>
      <div class="col-xs-8">
        <div class="form-control-static">{{userName}}</div>
      </div>
    </div>
    <div class="form-group">
      <label class="col-xs-4 control-label">是否启用：</label>
      <div class="col-xs-8">
        <div class="form-control-static">{{#fun_equals_one enabled}}
          <span class="label label-success">已启用</span>
          {{else}}
          <span class="label label-danger">已禁用</span>
          {{/fun_equals_one}}
        </div>
      </div>
    </div>
    <div class="form-group">
      <label class="col-xs-4 control-label">性别：</label>
      <div class="col-xs-8">
        <div class="form-control-static">{{sex}}
        </div>
      </div>
    </div>
    <div class="form-group">
      <label class="col-xs-4 control-label">地区编号：</label>
      <div class="col-xs-8">
        <div class="form-control-static">{{areaCode}}</div>
      </div>
    </div>
    <div class="form-group">
      <label class="col-xs-4 control-label">乡镇：</label>
      <div class="col-xs-8">
        <div class="form-control-static">{{reportUserAreaName}}</div>
      </div>
    </div>

    <div class="form-group">
      <label class="col-xs-4 control-label">地区名称:</label>
      <div class="col-xs-8">
        <div class="form-control-static">{{areaName}}</div>
      </div>
    </div>
    <div class="form-group">
      <label class="col-xs-4 control-label">角色：</label>
      <div class="col-xs-8">
        <div class="form-control-static">
          {{#ifEqual roleId 1001}}村（社）网格员{{/ifEqual}}
          {{#ifEqual roleId 1002}}纪检监察网格联络员{{/ifEqual}}
          {{#ifEqual roleId 1003}}乡镇纪委管理员{{/ifEqual}}
          {{#ifEqual roleId 1004}}县纪委联系室/巡察办{{/ifEqual}}
          {{#ifEqual roleId 1005}}分管领导{{/ifEqual}}
          {{#ifEqual roleId 1006}}县级领导班子{{/ifEqual}}
          {{#ifEqual roleId 1007}}超级管理员{{/ifEqual}}
          {{#ifEqual roleId 1008}}巡察办主任{{/ifEqual}}
          {{#ifEqual roleId 1009}}巡察一组{{/ifEqual}}
          {{#ifEqual roleId 1010}}巡察二组{{/ifEqual}}
          {{#ifEqual roleId 1011}}巡察三组{{/ifEqual}}
          {{#ifEqual roleId 2000}}群众{{/ifEqual}}
        </div>
      </div>
    </div>
    <div class="form-group">
      <label class="col-xs-4 control-label">备注：</label>
      <div class="col-xs-8">
        <div class="form-control-static">{{remark}}</div>
      </div>
    </div>
  </div>
</script>
<script>
  $(document).ready(function () {
    var HBR_TD = Handlebars.compile($("#template-sysUser").html());
    var pageParams = {
      <#-- 模块id，最重要的属性 -->
      "moduleName": "sysUser",
      <#-- 弹窗区域，都是100%，是不显示最大化按钮的 -->
      "area": ["600px", "600px"],
      "url": {
        "list": "a/sysUser/getDataList.json",
        "delete": "a/sysUser/deleteData.json",
        <#-- 会自动把id参数写入 -->
        "view": "a/sysUser/getDataInfo.json",
        "edit": function (id) {
          return "a/sysUser/edit.html?id=" + id;
        }
      },
      <#-- 这里列表的行渲染方法，配置data-formatter="..." -->
      "formatters": {
        "no": function (column, row) {
          return row.idx;
        },
        "fun_enabled": function (column, row) {
          return (1 === row.enabled) ? "<span class=\"label label-primary\">已启用</span>" :
              "<span class=\"label label-danger\">已禁用</span>";
        },
        "fun_name_role": function (column, row) {
          var roleId = row.roleId;
          if (roleId === 1001) {
            return "村（社）网格员";
          } else if (roleId === 1002) {
            return "纪检监察网格联络员";
          } else if (roleId === 1003) {
            return "乡镇纪委管理员";
          } else if (roleId === 1004) {
            return "县纪委联系室";
          } else if (roleId === 1005) {
            return "分管领导";
          } else if (roleId === 1006) {
            return "县级领导班子";
          } else if (roleId === 1007) {
            return "超级管理员";
          } else if (roleId === 1008) {
            return "巡察办主任";
          } else if (roleId === 1009) {
            return "巡察一组";
          } else if (roleId === 1010) {
            return "巡察二组";
          } else if (roleId === 1011) {
            return "巡察三组";
          }
        },

        "fun_type_content": function (rolumn, row) {
          // const clist = row.map ;
          const clist = townCodeNameMap;
          var content = "";
          for (var i = 0; i < clist.length; i++) {
            content += "<p>" + clist[i].key + "、" + clist[i].value + "</p>";
          }
          return content;
        },
        "fun_township_userName": function (rolumn, row) {
          var areaCodeVar = "".concat(row.areaCode);
          if(areaCodeVar==="undefined"){
           return "";
          }else{
            var areaCodeStr = areaCodeVar.substring(0,9);
            if(areaCodeStr === "140725200"){
              return  "平舒乡";
            }else if(areaCodeStr === "140725201"){
              return  "解愁乡";
            }else if(areaCodeStr === "140725202"){
              return  "温家庄乡";
            }else if(areaCodeStr === "140725203"){
              return  "景尚乡";
            }else if(areaCodeStr === "140725204"){
              return  "上湖乡";
            }else if(areaCodeStr === "140725100"){
              return  "朝阳镇";
            }else if(areaCodeStr === "140725101"){
              return  "南燕竹镇";
            }else if(areaCodeStr === "140725102"){
              return  "宗艾镇";
            }else if(areaCodeStr === "140725103"){
              return  "平头镇";
            }else if(areaCodeStr === "140725104"){
              return  "松塔镇";
            }else if(areaCodeStr === "140725105"){
              return  "西洛镇";
            }else if(areaCodeStr === "140725106"){
              return  "尹灵芝镇";
            }else if(areaCodeStr === "140725205"){
              return  "羊头崖乡";
            }else if(areaCodeStr === "140725206"){
              return  "马首乡";
            }else if(areaCodeStr === "140725700"){
              return  "丹凤城区管委会";
            }else if(areaCodeStr === "140725701"){
              return  "滨河城区管委会";
            }else{
              return "";
            }
          }


        },
        "commands": function (column, row) {
          return HBR_TD(row);
        }
      },
      <#-- 列表自定义提交数据的附加参数 -->
      "otherParams": {}
    };
    <#-- view的本页渲染的模板 -->
    var HBR_VIEW = Handlebars.compile($("#template-view-sysUser").html());
    <#-- 列表ajax加载，绑定删除和编辑之外的其他按钮 -->

    function bindRowEvent() {
      <#-- 绑定查看，本例是用的本页渲染，也可以使用单独的页面 -->
      <#-- CommonGetData定义在main-list.js -->
      $("#table-sysUser .ops-view").on("click", function () {
        var id = $(this).parent().attr("data-id");
        CommonGetData(pageParams.url.view, {
          "id": id
        }, function (result) {
          openLayer({
            "title": "查看",
            "content": HBR_VIEW(result)
          });
          if ($.isFunction(top.clearIframeFocus)) {
            top.clearIframeFocus();
          }
        });
      });
    }

    <#-- 通用功能的入口 -->
    CommonListFun(pageParams, bindRowEvent);
  });


  function searchVillage(obj) {
    var township = $(obj).val();
    $("#areaCode").val(township);
    if (township == '') {
      return false;
    }
    $.ajax({
      "type": "GET",
      "cache": false,
      "url": "/a/village/list.json",
      "data": {
        "areaCode": township
      },
      "dataType": "json",
      "success": function (data) {
        const template = Handlebars.compile($("#template-villages").html());
        $("#valliage").html(template(data.result));
      }
    });
  }

  function choseArea(obj) {
    var code = $(obj).val();
    if (code == '') {
      code = $("#township").val();
    }
    $("#areaCode").val(code);
  }

  function bindRole(obj) {
    obj = $(obj);
    var userId = obj.parent().attr("data-id");
    openLayer({
      "type": 2,
      "title": "绑定角色",
      "area": ["400px", "500px"],
      "maxmin": false,
      "content": "a/sysRole/tree.html?uid=" + userId,
      "yes": function (index, layero) {
        // 调用子窗口的添加方法
        layero.find("iframe")[0].contentWindow.subSave();
      }
    });
  }

  function bindArea(obj) {
    obj = $(obj);
    var userId = obj.parent().attr("data-id");
    openLayer({
      "type": 2,
      "title": "地区权限配置",
      "area": ["550px", "400px"],
      "maxmin": false,
      "content": "a/sysUser/bindArea.html?uid=" + userId,
      "yes": function (index, layero) {
        // 调用子窗口的添加方法
        layero.find("iframe")[0].contentWindow.subSave();
      }
    });
  }

  function bindPwd(obj) {
    obj = $(obj);
    var userId = obj.parent().attr("data-id");
    openLayer({
      "type": 2,
      "title": "修改密码",
      "area": ["550px", "250px"],
      "maxmin": false,
      "content": "a/sysUser/edit_pwd_admin.html?uid=" + userId,
      "yes": function (index, layero) {
        // 调用子窗口的添加方法
        layero.find("iframe")[0].contentWindow.subSave();
      }
    });
  }

  function importExcel() {
    openLayer({
      "type": 2,
      "title": "导入用户",
      "area": ["600px", "400px"],
      "content": "a/sysUser/excelImport.html",
      "yes": function (index, layero) {
        // 调用子窗口的添加方法
        layero.find("iframe")[0].contentWindow.subSave();
      }
    });
  }
</script>
</body>
</html>
