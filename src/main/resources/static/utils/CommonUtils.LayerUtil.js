/**
 * CommonUtils.LayerUtil，Layer封装工具类
 * @author BBF
 * */
;
(function (comm, $) {
  'use strict';
  var defaults = {
    "type": 1,
    "title": "\u6E29\u99A8\u63D0\u793A", //温馨提示
    "area": ["600px"], //如果指定宽高：[宽,高]
    "skin": "layui-layer-rim", //加上边框
    "fix": true,//固定位置
    "shadeClose": false, //点阴影关闭
    "scrollbar": false, //关闭浏览器滚动
    "shade": 0.8,
    "maxmin": false, //对type:1和type:2有效。默认不显示最大小化按钮
    "closeBtn": 1, //0 - 不显示，默认 1 - 标准小叉号，2 - 圆形叉号
    //关闭窗口回调
    "end": $.noop
  };
  var msgString = {
    "templete": "<div class=\"modal-body\" style=\"padding:20px\"><div class=\"{{class}}\" style=\"margin:0 0 5px 0\"><span style=\"vertical-align:sub\"><i class=\"{{icon}}\"></i></span><span style=\"margin-left:10px\">{{msg}}</span></div></div>",
    "runico": "<i class=\"fa fa-cog fa-spin fa-2x\"></i>",
    "info": {
      "class": "alert alert-info",
      "icon": "fa fa-question-circle fa-2x",
      "msg": "&nbsp;"
    },
    "success": {
      "class": "alert alert-success",
      "icon": "fa fa-check-circle fa-2x",
      "msg": "&nbsp;"
    },
    "warning": {
      "class": "alert alert-warning",
      "icon": "fa fa-exclamation-circle fa-2x",
      "msg": "&nbsp;"
    },
    "danger": {
      "class": "alert alert-danger",
      "icon": "fa fa-times-circle fa-2x",
      "msg": "&nbsp;"
    },
    "loading": {
      "class": "alert alert-info",
      "icon": "fa fa-cog fa-spin fa-2x",
      "msg": "&nbsp;" //为保证有好的体验，建议msg + "<i class=\"fa fa-cog fa-spin fa-2x\"></i>"
    }
  };
  //获取配置
  var getLayerConf = function (layerConf, defaultConf) {
    if (defaultConf) {
      return $.extend({}, defaultConf, layerConf);
    }
    return $.extend({}, defaults, layerConf);
  };

  //获取信息类型
  var getMsgType = function (type) {
    var msgType = msgString[type];
    if (!msgType) {
      msgType = msgString.info;
    }
    return msgType;
  };
  //编译模板
  var compileContent = function (conf) {
    return msgString.templete.compile(conf);
  };
  //简单的信息显示，target默认是top
  var showInfo = function (title, content, type, btns, callback, target) {
    if (!target) {
      target = top;
    }
    content = compileContent(getLayerConf({
      "msg": content
    }, getMsgType(type)));
    var conf = {
      "title": title,
      "content": content,
      "scrollbar": false,
      "btn": btns
    };
    if (!btns) {
      delete conf["btn"];
    }
    if ($.isFunction(callback)) {
      conf["yes"] = function () {
        callback();
      };
    }
    return target.layer.open(getLayerConf(conf));
  };
  comm.LayerUtil = {
    "getLayerConf": getLayerConf, //获取Layer通用配置
    "getMsgType": getMsgType, //获取模板类型
    "compileContent": compileContent, //获取模板编译结果
    "close": function (layerIdx, target) {
      if (!target) {
        target = top;
      }
      target.layer.close(layerIdx);
    },
    "title": function (title, layerIdx, target) {
      if (!target) {
        target = top;
      }
      target.layer.title(title, layerIdx);
    },
    "showLoading": function (type, ct, target) {
      //1-读取，2-提交
      var content = ct;
      if (!ct) {
        content = "\u6B63\u5728\u63D0\u4EA4\u6570\u636E\uFF0C\u8BF7\u8010\u5FC3\u7B49\u5019\u2026\u2026";
        if (1 === type) {
          content = "\u6B63\u5728\u8BFB\u53D6\u6570\u636E\uFF0C\u8BF7\u8010\u5FC3\u7B49\u5019\u2026\u2026";
        }
      }
      return showInfo(defaults.title, content + msgString.runico, "loading", false, null, target);
    },
    "showSuccess": function (title, content, callback, target) {
      return showInfo(title, content, "success", false, callback, target);
    },
    "showError": function (title, content, callback, target) {
      return showInfo(title, content, "danger", ["\u786E\u5B9A"], callback, target);
    },
    "showWarning": function (title, content, callback, target) {
      return showInfo(title, content, "warning", ["\u786E\u5B9A"], callback, target);
    },
    "showInfo": function (title, content, callback, target) {
      return showInfo(title, content, "info", ["\u786E\u5B9A"], callback, target);
    },
    /**
     * 显示确认对话框
     * @param title 标题
     * @param content 内容
     * @param funcyes 点确定的回调，会传递一个callback参数，用于关闭本对话框，例如
     * CommonUtils.LayerUtil.showConfirm("删除提醒", "是否从生产序列中删除此表？", function (fun) {
     *   $(obj).parent().parent().remove();
     *   fun && fun();
     * });
     * @param target 默认是top
     */
    "showConfirm": function (title, content, funcyes, target) {
      if (!target) {
        target = top;
      }
      content = compileContent(getLayerConf({
        "msg": content
      }, getMsgType("warning")));
      var layerConf = {
        "title": title,
        "content": content,
        "btn": ["\u786E\u5B9A", "\u53D6\u6D88"],
        "yes": function () {
          if ("function" === typeof (funcyes)) {
            funcyes(function () {
              target.layer.close(confirmIdx);
            });
          }
        }
      };
      // 点击确定按钮后，回调函数通过confirmIdx关闭
      var confirmIdx = target.layer.open(getLayerConf(layerConf));
    }
  };
})(CommonUtils, jQuery);