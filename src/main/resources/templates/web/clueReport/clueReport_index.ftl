<#include "/common/head.ftl"/>
<#include "/common/cssfile_list.ftl"/>
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content row">
  <div class="col-sm-12">
    <div class="ibox">
      <div class="ibox-title">
        <h5>查询</h5>
        <div class="ibox-tools"><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></div>
      </div>
      <div class="ibox-content">
        <form class="form-horizontal" id="frm-search-clueReport">
          <div class="row">
            <div class="col-xs-6 col-sm-4 col-lg-3">
              <div class="form-group">
                <label class="col-sm-4 control-label">时间段：</label>
                <div class="col-sm-7">
                  <div class="input-group">
                    <input type="text" class="form-control" id="timeRange"
                           readonly="readonly" placeholder="日期"/>
                  </div>
                </div>
                <input type="hidden" id="startTime" name="startTime">
                <input type="hidden" id="endTime" name="endTime">
                <input type="hidden" id="areaCode" name="areaCode">
              </div>
            </div>
            <div class="col-xs-6 col-sm-4 col-lg-3">
              <div class="form-group">
                <label class="col-sm-5 control-label">上报角色类型：</label>
                <div class="col-sm-7">
                  <select name="reportRoleId" class="form-control">
                    <option value="">全部</option>
                    <option value="1001">村（社）网格员</option>
                    <option value="1002">廉润寿川网格联络员</option>
                  </select>
                </div>
              </div>
            </div>
            <div class="col-xs-6 col-sm-4 col-lg-3">
              <div class="form-group">
                <label class="col-sm-5 control-label">状态</label>
                <div class="col-sm-7">
                  <select name="state" class="form-control">
                    <option value="">全部</option>
                    <option value="0">未处理</option>
                    <option value="1">已受理</option>
                    <option value="2">已办结</option>
                    <option value="3">已知晓</option>
                    <option value="4">已转办</option>
                  </select>
                </div>
              </div>
            </div>
            <div class="col-xs-6 col-sm-4 col-lg-3">
              <div class="form-group">
                <label class="col-sm-5 control-label">报送类型状态</label>
                <div class="col-sm-7">
                  <select name="reportTypeState" class="form-control">
                    <option value="">全部</option>
                    <option value="有效">有效</option>
                    <option value="无效">无效</option>
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
                  <select class="form-control" id="township" onchange="searchVillage(this);">
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
                <label class="col-sm-5 control-label">村:</label>
                <div class="col-sm-7">
                  <select id="valliage" class="form-control" onchange="choseArea(this);">
                    <option value="">全部</option>
                  </select>
                </div>
              </div>
            </div>
            <div class="col-xs-6 col-sm-4 col-lg-3">
              <div class="form-group">
                <label class="col-sm-5 control-label">网格员报送类型</label>
                <div class="col-sm-7">
                  <select name="reportIds1" id="reportIds1" class="form-control">
                    <option value="">全部</option>
                    <#list reportTypeList1 as obj1>
                      <option value="${(obj1.sortNo)!}">${(obj1.content)!}</option>
                    </#list>
                  </select>
                </div>
              </div>
            </div>
            <div class="col-xs-6 col-sm-4 col-lg-3">
              <div class="form-group">
                <label class="col-sm-5 control-label">联络员报送类型</label>
                <div class="col-sm-7">
                  <select name="reportIds2" id="reportIds2" class="form-control">
                    <option value="">全部</option>
                    <#list reportTypeList2 as obj2>
                      <option value="${(obj2.sortNo)!}">${(obj2.content)!}</option>
                    </#list>
                  </select>
                </div>
              </div>
            </div>
          </div>



          <div class="row">
            <div class="col-xs-6 col-sm-4 col-lg-3">
              <div class="form-group">
                <label class="col-sm-4 control-label">当前当前审批人角色类型：</label>
                <div class="col-sm-7">
                  <select name="receivedRoleId" class="form-control">
                    <option value="0">全部</option>
                    <option value="1003">乡镇纪委管理员</option>
                    <option value="1004">县信访室</option>
                    <option value="1012">村干部/社区书记</option>
                    <option value="3010">乡镇农经站/城区财务</option>
                    <option value="3020">林业站</option>
                    <option value="3030">环卫站</option>
                    <option value="3040">安监站/安全站</option>
                    <option value="3050">民政办</option>
                    <option value="3060">国土所</option>
                    <option value="3070">派出所/城区片警</option>
                    <option value="3080">综治信访</option>
                    <option value="3090">党建</option>
                    <option value="3999">其他</option>
                  </select>
                </div>
              </div>
            </div>
            <div class="col-xs-6 col-sm-4 col-lg-3">
              <div class="form-group">
                <label class="col-sm-4 control-label">当前审批人用户名：</label>
                <div class="col-sm-7">
                  <input type="text" name="receivedUserName" placeholder="搜索用户名" class="form-control"/>
                </div>
              </div>
            </div>
          </div>

          <div class="text-center">
            <button id="btn-search-clueReport" class="btn btn-primary"><i class="fa fa-search"></i> 查询
            </button>
            <button type="reset" class="btn"><i class="fa fa-minus"></i> 重置</button>
          </div>
        </form>
      </div>
    </div>
    <div class="table-responsive ibox-content">
      <div class="clearfix" style="margin-bottom:10px;">
        <div class="pull-left">
<#--            <button type="button" class="btn btn-primary" id="btn-add-clueReport"><i-->
<#--                  class="fa fa-plus"></i> 新增-->
<#--            </button>-->
            <#--<button type="button" class="btn btn-danger" id="btn-del-clueReport"><i
                  class="fa fa-remove"></i> 删除
            </button>-->
        </div>
        <div class="grid-o pull-right" id="bootgrid-toolbar"></div>
      </div>
      <table id="table-clueReport"
             class="table table-condensed table-hover table-striped table-bordered no-margins">
        <thead>
        <tr>
          <th data-column-id="ID" data-width="60px" data-formatter="no" data-sortable="false"
              data-identifier="true">序号
          </th>
          <th data-column-id="clueNo" data-order="desc" data-visible="true" data-sortable="true">
            编号
          </th>
          <th data-column-id="reportUserAreaName" data-order="desc" data-visible="true" data-sortable="true" data-formatter="fun_township_userName">
            乡镇
          </th>
          <th data-column-id="userName" data-order="desc" data-visible="true" data-sortable="true">
            用户
          </th>
          <th data-column-id="dtoList" data-order="desc" data-visible="true" data-sortable="true"
              data-formatter="fun_type_content">
            上报类型
          </th>
          <th data-column-id="createTime" data-order="desc" data-visible="true"
              data-sortable="true" data-formatter="fun_date">时间
          </th>
          <th data-column-id="state" data-order="desc" data-visible="true"
              data-formatter="fun_state" data-sortable="true">
            状态
          </th>
          <th data-column-id="receivedUserAccount"  data-visible="true" data-sortable="true">
            当前审批人账号
          </th>
          <th data-column-id="receivedUserName"  data-visible="true" data-sortable="true">
            当前审批人用户名
          </th>
          <th data-column-id="enabled" data-order="desc" data-formatter="fun_name_role"
              data-visible="true" data-sortable="true">当前审批人角色
          </th>

          <th data-column-id="link" data-width="180px" data-formatter="commands" data-custom="true"
              data-sortable="false">操作
          </th>
        </tr>
        </thead>
      </table>
    </div>
  </div>
</div>
<#include "/common/scriptfile.ftl"/>
<#include "/common/scriptfile_list.ftl"/>
<script src="${global.staticPath!}static/plugins/laydate/laydate.js"></script>
<script src="${global.staticPath!}/static/utils/handlebars-tool.js"></script>
<script id="template-clueReport" type="text/x-handlebars-template">
  <td data-id="{{id}}">
    <button class="btn btn-primary btn-xs o-ch ops-view"><i class="fa fa-eye"></i> 查看</button>

    <#if roleId == 1003 || roleId == 1004 || roleId == 1005 || roleId == 1012 >
      {{#ifEqual state 2}}
<#--      <button class="btn btn-warning btn-xs o-c ops-edit"><i class="fa fa-edit"></i> 修改</button>-->
      <button class="btn btn-success btn-xs mg-5 o-c" onclick="editTask(this)"><i class="fa fa-user"></i>修改</button>
      {{/ifEqual}}
    </#if>

    <#if roleId == 1003 || roleId == 1004 || roleId == 1005 || roleId == 1012>
    {{#ifEqual state 0}}
    <button class="btn btn-success btn-xs mg-5 o-c" onclick="knowTask(this)"><i class="fa fa-user"></i>&nbsp;已知晓</button>
    <button class="btn btn-success btn-xs mg-5 o-c" onclick="turnToOtherTask(this)"><i class="fa fa-user"></i>&nbsp;已转办</button>
    {{/ifEqual}}
    </#if>

    <#if roleId == 1007 >
      {{#ifNotEqual state 0}}
      <button class="btn btn-success btn-xs mg-5 o-c" onclick="turnToUntreatedTask(this)"><i class="fa fa-user"></i>&nbsp;重新设置为未处理</button>
      {{/ifNotEqual}}
    </#if>

    {{#fun_equals_one rowLocked}}
    <button class="btn btn-xs o-d" disabled="disabled"><i class="fa fa-close"></i> 删除</button>
    {{else}}
    <button class="btn btn-danger btn-xs o-d ops-del"><i class="fa fa-close"></i> 删除</button>
    {{/fun_equals_one}}
    <button class="btn btn-warning btn-xs o-d ops-print"><i class="fa fa-print"></i> 打印</button>
  </td>
</script>

<script id="template-villages" type="text/x-handlebars-template">
  <option value="">--请选择--</option>
  {{#each this}}
  <option value="{{areaCode}}">{{areaName}}</option>
  {{/each}}
</script>

<script id="template-view-clueReport" type="text/x-handlebars-template">
  <div class="form-horizontal" style="margin: 20px;">
    <div class="form-group">
      <label class="col-xs-4 control-label">乡镇：</label>
      <div class="col-xs-8">
        <div class="form-control-static">{{reportUserAreaName}}</div>
      </div>
    </div>

    <div class="form-group">
      <label class="col-xs-4 control-label">用户：</label>
      <div class="col-xs-8">
        <div class="form-control-static">{{userName}}</div>
      </div>
    </div>

    <div class="form-group">
      <label class="col-xs-4 control-label">上报类型：</label>
      <div class="col-xs-8">
        {{#each dtoList}}
        <div class="form-control-static">
          {{key}}、{{value}}
        </div>
        {{/each}}
      </div>
    </div>
    <div class="form-group">
      <label class="col-xs-4 control-label">线索描述：</label>
      <div class="col-xs-8">
        <div class="form-control-static">{{clueDescribe}}</div>
      </div>
    </div>
    <div class="form-group">
      <label class="col-xs-4 control-label">时间：</label>
      <div class="col-xs-8">
        <div class="form-control-static">{{formatDate createTime "yyyy-MM-dd hh:mm:ss"}}</div>
      </div>
    </div>


    <div class="form-group">
      <label class="col-xs-4 control-label">图片：</label>
      <div id="localImag">
        {{#each img}}
        <img src="${global.preUrl!}{{imageUrl}}" width="200" height="200" />
        {{/each}}
      </div>
    </div>

    <div class="form-group">
      <label class="col-xs-4 control-label">语音：</label>
      <div id="localAudio">
          <table class="table table-condensed table-hover table-striped table-bordered no-margins">
        {{#each audio}}
          <tr>
<#--        <a href="${global.preUrl!}{{imageUrl}}">点击右键在新窗口播放语音{{imageUrl}}</a>-->
          <audio width="70%" height="70%" controls="controls" src="${global.preUrl!}{{imageUrl}}"/>
          </tr>
        {{/each}}
          </table>
      </div>
    </div>

    <div class="form-group">
      <label class="col-xs-4 control-label">视频：</label>
      <div id="localVideo">
          {{#each video}}
          <tr>
<#--            <video width="70%" height="70%" controls="controls" src="http://183.201.252.83:49012/upload/1624943914308video2.mp4"/>-->
          <video width="70%" height="70%" controls="controls" src="${global.preUrl!}{{imageUrl}}"/>
          </tr>
          {{/each}}
      </div>
    </div>


    <table class="table table-condensed table-hover table-striped table-bordered no-margins">
      <thead>
      <tr>
        <td>接受人员</td>
        <td>状态</td>
        <td>反馈意见</td>
        <td>时间</td>
      </tr>
      </thead>
      <tbody>
      {{#each flows}}
      <tr>
        <td>
          <div class="form-control-static">{{receiveName}}</div>
        </td>
        <td>
          <div class="form-control-static">
            {{#ifEqual state 0}}未处理{{/ifEqual}}
            {{#ifEqual state 1}}已受理{{/ifEqual}}
            {{#ifEqual state 2}}已办结{{/ifEqual}}
            {{#ifEqual state 3}}已知晓{{/ifEqual}}
            {{#ifEqual state 4}}已转办{{/ifEqual}}
          </div>
        </td>
        <td>
          <div class="form-control-static">{{remark}}</div>
        </td>
        <td>
          <div class="form-control-static">{{formatDate createTime "yyyy-MM-dd hh:mm:ss"}}</div>
        </td>
      </tr>
      {{/each}}
      </tbody>
    </table>
  </div>
</script>
<script>
  laydate.render({
    "elem": "#timeRange",
    "range": "至",
    "format": "yyyy-MM-dd",
    "trigger": "click",
    "done": function (value, startDate, endDate) {
      if (value) {
        const array = value.split("至");
        $("#startTime").val(array[0]);
        $("#endTime").val(array[1]);
      } else {
        $("#startTime").val("");
        $("#endTime").val("");
      }
    }
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

  function knowTask(obj) {
    obj = $(obj);
    var clueId = obj.parent().attr("data-id");
    //直接用ajax处理流程返回就可以了。
    openLayer({
      "type": 2,
      "title": "已知晓",
      "area": ["400px", "200px"],
      "maxmin": false,
      "content": "${mapping!}/knowTask.html?uid=" + clueId,
      "yes": function (index, layero) {
        // 调用子窗口的添加方法
        layero.find("iframe")[0].contentWindow.subSave();
      }
    });
  }


  function editTask(obj) {
    obj = $(obj);
    var clueId = obj.parent().attr("data-id");
    //直接用ajax处理流程返回就可以了。
    openLayer({
      "type": 2,
      "title": "修改办结描述",
      "area": ["800px", "600px"],
      "maxmin": false,
      "content": "${mapping!}/editTask.html?uid=" + clueId,
      "yes": function (index, layero) {
        // 调用子窗口的添加方法
        layero.find("iframe")[0].contentWindow.subSave();
      }
    });
  }


  function turnToOtherTask(obj) {
    obj = $(obj);
    var clueId = obj.parent().attr("data-id");
    //直接用ajax处理流程返回就可以了。
    openLayer({
      "type": 2,
      "title": "已转办",
      "area": ["600px", "400px"],
      "maxmin": false,
      "content": "${mapping!}/turnToOtherTask.html?uid=" + clueId,
      "yes": function (index, layero) {
        // 调用子窗口的添加方法
        layero.find("iframe")[0].contentWindow.subSave();
      }
    });
  }

  function turnToUntreatedTask(obj) {
    obj = $(obj);
    var clueId = obj.parent().attr("data-id");
    //直接用ajax处理流程返回就可以了。
    openLayer({
      "type": 2,
      "title": "重新设置为未处理",
      "area": ["400px", "200px"],
      "maxmin": false,
      "content": "${mapping!}/turnToUntreatedTask.html?uid=" + clueId,
      "yes": function (index, layero) {
        // 调用子窗口的添加方法
        layero.find("iframe")[0].contentWindow.subSave();
      }
    });
  }


  $(document).ready(function () {
    var HBR_TD = Handlebars.compile($("#template-clueReport").html());
    var pageParams = {
      <#-- 模块id，最重要的属性 -->
      "moduleName": "clueReport",
      <#-- 弹窗区域，都是100%，是不显示最大化按钮的 -->
      "area": ["600px", "600px"],
      "url": {
        "list": "${mapping!}/getDataList.json",
        "delete": "${mapping!}/deleteData.json",
        <#-- 会自动把id参数写入 -->
        "view": "${mapping!}/getDataInfo.json",
        "edit": function (id) {
          return "${mapping!}/edit.html?id=" + id;
        }
      },
      <#-- 这里列表的行渲染方法，配置data-formatter="..." -->
      "formatters": {
        "no": function (column, row) {
          return row.idx;
        },
        "commands": function (column, row) {
          return HBR_TD(row);
        },
        "fun_state": function (rolumn, row) {
          var state = row.state;
          if (state == 0) {
            return "未处理";
          } else if (state == 1) {
            return "已受理";
          } else if (state == 2) {
            return "已办结";
          }else if (state == 3) {
            return "已知晓";
          } else if (state == 4) {
            return "已转办";
          }
        },
        "fun_date": function (rolumn, row) {
          return new Date(row.createTime).Format("yyyy-MM-dd hh:mm:ss");
        },
        "fun_type_content": function (rolumn, row) {
          const clist = row.dtoList;
          var content = "";
          for (var i = 0; i < clist.length; i++) {
            content += "<p>" + clist[i].key + "、" + clist[i].value + "</p>";
          }
          return content;
        },

        "fun_name_role": function (column, row) {
          var roleId = row.receivedRoleId;
          if (roleId === 1001) {
            return "村（社）网格员";
          } else if (roleId === 1002) {
            return "廉润寿川网格联络员";
          } else if (roleId === 1003) {
            return "乡镇纪委管理员";
          } else if (roleId === 1004) {
            return "县信访室";
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
          } else if (roleId === 1012) {
            return "村干部/社区书记";
          } else if (roleId === 3010) {
            return "乡镇农经站/城区财务";
          } else if (roleId === 3020) {
            return "林业站";
          } else if (roleId === 3030) {
            return "环卫站";
          } else if (roleId === 3040) {
            return "安监站/安全站";
          } else if (roleId === 3050) {
            return "民政办";
          } else if (roleId === 3060) {
            return "国土所";
          } else if (roleId === 3070) {
            return "派出所/城区片警";
          } else if (roleId === 3080) {
            return "综治信访";
          } else if (roleId === 3090) {
            return "党建";
          } else if (roleId === 3999) {
            return "其他";
          }
        },

        "fun_township_userName": function (rolumn, row) {
          var content = row.reportUserAreaName;
          return content;
        }
      },
      <#-- 列表自定义提交数据的附加参数 -->
      "otherParams": {}
    };
    <#-- view的本页渲染的模板 -->
    var HBR_VIEW = Handlebars.compile($("#template-view-clueReport").html());
    <#-- 列表ajax加载，绑定删除和编辑之外的其他按钮 -->

    function bindRowEvent() {
      <#-- 绑定查看，本例是用的本页渲染，也可以使用单独的页面 -->
      <#-- CommonGetData定义在main-list.js -->
      $("#table-clueReport .ops-view").on("click", function () {
        var id = $(this).parent().attr("data-id");
        CommonGetData(pageParams.url.view, {
          "id": id
        }, function (result) {
          openLayer({
            "title": "查看",
            "btn": ["关闭"],
            "content": HBR_VIEW(result)
          });
          if ($.isFunction(top.clearIframeFocus)) {
            top.clearIframeFocus();
          }
        });
      });
      $("#table-clueReport .ops-print").on("click", function () {
        var id = $(this).parent().attr("data-id");
        openLayer({
          "type": 2,
          "title": "打印页面",
          "area": ["80%", "500px"],
          "content": "${mapping!}/edit.html?id=" + id
        });
      });
    }
      <#-- 通用功能的入口 -->
    CommonListFun(pageParams, bindRowEvent);
  });
</script>

</body>
</html>
