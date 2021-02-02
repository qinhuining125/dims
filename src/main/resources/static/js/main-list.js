/**
 * 通用的列表函数
 * @param listConf
 * @param bindRowEvent 列表ajax加载，绑定删除和编辑之外的其他按钮
 */
var CommonListFun = function (listConf, bindRowEvent) {
  var $ = jQuery;
  var layerIndex = -1; //记录Layer的ID
  var listTableObj = $("#table-" + listConf.moduleName);
  //判断窗口是否应该显示最大化，不全等于100%，才显示最大化按钮
  var canMaxMin = listConf.area[0] !== "100%" || listConf.area[1] !== "100%";
  var defaultsLayer = {
    "type": 1,
    "btn": ["确定"], //按钮
    "area": listConf.area,
    "maxmin": listConf.maxmin && canMaxMin,
    "end": function () {
      layerIndex = -1;
      //初始化调用layer源的相关信息
      if ("object" === typeof(top.layerCallerWD)) {
        top.layerCallerWD.clear();
      }
    }
  };

  // BootGrid的配置json
  var bootGridSetting = {
    "post": listConf.otherParams,
    "url": listConf.url.list,
    "formatters": listConf.formatters
  };
  if(!!listConf.singlePage){
    // 控制不显示foot的分页条和head的分页选项
    bootGridSetting["navigation"] = 1;
    bootGridSetting["rowCount"] = -1;
  }

  /**
   * 绑定bootGrid刷新
   */
  var bindBootGridRefresh = function () {
    listTableObj.bootgrid("reloadCurr");
  };

  /**
   * 绑定工具条按钮
   */
  var bindToolBarEvent = function () {
    //添加
    $("#btn-add-" + listConf.moduleName).on("click", function () {
      openLayer({
        "type": 2,
        "title": "新增",
        "content": listConf.url.edit(""),
        "yes": function (index, layero) {
          // 调用子窗口的添加方法
          layero.find("iframe")[0].contentWindow.subSave();
        }
      });
    });
    //删除
    $("#btn-del-" + listConf.moduleName).on("click", function () {
      delData(listTableObj.bootgrid("getSelectedRows"));
    });
    //绑定搜索
    $("#btn-search-" + listConf.moduleName).on("click", function () {
      var data = $(this).parent().parent().serializeJson();
      listTableObj.bootgrid("search", data);
      //阻止form提交
      return false;
    });
  };

  /**
   * 绑定按钮事件
   * @param tableId grid表格的Id
   */
  var bindBootGridEvent = function (tableId) {
    //绑定删除
    $(tableId + " .ops-del").on("click", function () {
      delData([$(this).parent().attr("data-id")]);
    });
    //绑定修改
    $(tableId + " .ops-edit").on("click", function () {
      openLayer({
        "type": 2,
        "title": "修改",
        "content": listConf.url.edit($(this).parent().attr("data-id")),
        "yes": function (index, layero) {
          // 调用子窗口的添加方法
          layero.find("iframe")[0].contentWindow.subSave();
        }
      });
    });
  };

  /**
   * 删除数据
   * @param ids id集合
   */
  var delData = function (ids) {
    CommonUtils.AjaxUtil.CommonDelData(listConf.url.delete, {
      "ids": ids
    }, {
      "successCallback": function (result) {
        bindBootGridRefresh();
      }
    });
    if ($.isFunction(top.clearIframeFocus)) {
      window.setTimeout(function () {
        top.clearIframeFocus();
      }, 400);
    }
  };

  // 绑定列表的通用事件
  CommonUtils.AjaxUtil.CommonBootGrid("#table-" + listConf.moduleName, bootGridSetting, function (obj) {
    bindBootGridEvent("#table-" + listConf.moduleName);
    if (bindRowEvent && $.isFunction(bindRowEvent)) {
      // 指定外部绑定的行按钮事件
      bindRowEvent();
    }
    obj = $(obj);
    var method = obj.bootgrid("getLastMethod");
    if ("search" === method) {
      var total = obj.bootgrid("getTotalRowCount") >> 0;
      if (0 < total) {
        top.CommonUtils.ToastrUtil.success("\u5171\u627E\u5230" + total + "\u6761\u8BB0\u5F55\u3002", "\u67E5\u8BE2\u5B8C\u6BD5");
      } else {
        top.CommonUtils.ToastrUtil.warning("\u6682\u65F6\u6CA1\u6709\u6570\u636E\u3002");
      }
    }
  }, function () {
    bindToolBarEvent();
  });

  /**
   * 打开详情窗口
   * @param conf layer的配置参数
   */
  var openLayer = function (conf) {
    // 把当前窗口的window和document写入到top.layerCallerWD
    if ("object" === typeof(top.layerCallerWD)) {
      top.layerCallerWD.set(window, document);
    }
    var _conf = $.extend({}, defaultsLayer, conf);
    layerIndex = top.layer.open(_conf);
  };

  // 绑定刷新事件
  window["refreshTable"] = bindBootGridRefresh;
  // 绑定弹窗方法
  window["openLayer"] = openLayer;
  // 绑定弹窗的index
  window["closeSelf"] = function () {
    top.layer.close(layerIndex);
  };
};