/**
 * CommonUtils.AjaxUtil，通用list操作ajax类
 * @author BBF
 * */
;
(function (comm, $) {
  'use strict';
  var defaults = {
    "loadingType": 2, //1 - 读取，2 - 提交，默认2
    "msgTitle": "\u6E29\u99A8\u63D0\u793A", // 信息标题
    "lazyTime": 100, // successCallback 延迟执行的时间，毫秒
    "autoCloseModal": true, //自动关闭提示
    "loadingTitle": "\u8F7D\u5165\u4E2D...", // loading的标题，<载入中...>
    "msg": "\u64CD\u4F5C\u6210\u529F\uFF01", // 信息正文，<操作成功！>
    "wrong": "\u64CD\u4F5C\u5931\u8D25\uFF01", // 返回成功但无结果的提示，<\u64CD\u4F5C\u5931\u8D25！>
    "error": "\u64CD\u4F5C\u9519\u8BEF\uFF01", // 返回错误的提示，<操作错误！>
    "preAjaxCallback": function () {
    }, //Ajax调用前的回调函数
    "successCallback": function (result) {
    }, //Ajax成功的回调函数
    "completeCallback": function () {
    }
  };
  //删除前的提示信息
  var delTemplate = "\u662F\u5426\u5220\u9664\u5F53\u524D\u6240\u9009\u7684<span style=\"color:red;margin:0 5px\">{{len}}</span>\u6761\u6570\u636E\uFF1F";
  var Layer = comm.LayerUtil;
  //关闭layer
  var closeLayer = function (idx, target) {
    window.setTimeout(function () {
      Layer.close(idx, target);
    }, 100);
  };
  //是否函数
  var isFunction = function (func) {
    return "function" === typeof (func);
  };

  //获取AJAX的参数配置
  //读取时，不显示成功信息，直接回调
  var getAjaxConf = function (loadingIdx, startTime, options, target) {
    return {
      "type": options.type || "POST",
      "cache": false,
      "dataType": "json",
      "success": function (data) {
        if (!!data && data.success) {
          var result = data.result;
          if (1 === options.loadingType) {
            //读取，不显示成功信息，直接回调
            window.setTimeout(function () {
              closeLayer(loadingIdx, target);
              if (isFunction(options.successCallback)) {
                options.successCallback(result);
              }
            }, comm.TimerUtil.getTimeInterval(startTime));
          } else {
            //提交
            window.setTimeout(function () {
              CommonUtils.ToastrUtil.success(options.msg, options.msgTitle, false, target);
              //var msgIdx = Layer.showSuccess(options.msgTitle, options.msg, target);
              closeLayer(loadingIdx, target);
              window.setTimeout(function () {
                if (isFunction(options.successCallback)) {
                  options.successCallback(result);
                }
                //if (options.autoCloseModal) {
                //  Layer.close(msgIdx, target);
                //}
              }, options.lazyTime);
            }, comm.TimerUtil.getTimeInterval(startTime));
          }
        } else {
          window.setTimeout(function () {
            Layer.showError("\u64CD\u4F5C\u5931\u8D25&nbsp;", options.error + data.message, null, target);
            closeLayer(loadingIdx, target);
          }, comm.TimerUtil.getTimeInterval(startTime));
        }
      },
      "error": function (req, textStatus, errorThrown) {
        window.setTimeout(function () {
          var msg = textStatus + "。" + errorThrown;
          if (req && !!req.responseJSON && !!req.responseJSON.message) {
            msg = req.responseJSON.message;
          }
          Layer.showError("\u64CD\u4F5C\u5931\u8D25&nbsp;&nbsp;", msg, null, target);
          closeLayer(loadingIdx, target);
        }, comm.TimerUtil.getTimeInterval(startTime));
      },
      "complete": function () {
        if (isFunction(options.completeCallback)) {
          options.completeCallback();
        }
      }
    };
  };

  //AJAX提交数据
  var ajaxPostData = function (url, param, conf, target) {
    var options = $.extend({}, defaults, conf);
    window.setTimeout(function () {
      if (isFunction(options.preAjaxCallback)) {
        options.preAjaxCallback();
      }
    }, 100);
    var loadingIdx = Layer.showLoading(options.loadingType, false, target);
    var settings = getAjaxConf(loadingIdx, +new Date(), options, target);
    settings.url = url;
    settings.data = param;
    $.ajax(settings);
  };

  /**
   * Get请求，获取数据
   * @param url 请求地址
   * @param param 请求参数
   * @param successFun 成功后的回调
   * @param target 窗口的target，默认是top
   */
  window["CommonGetData"] = function (url, param, successFun, target) {
    comm.AjaxUtil.CommonAjaxData(url, param, {
      "loadingType": 1,
      "successCallback": function (result) {
        //Ajax成功的回调函数
        successFun(result);
      }
    }, target);
  };

  comm.AjaxUtil = {
    /**
     * 序列化表单数据，返回JSON
     * @param formObj 表单对象
     * @return JSON字符串
     */
    "serializeFrom": function (formObj) {
      return $(formObj).serializeJson();
    },
    /**
     * 绑定表格
     * @param tableId 表格ID
     * @param bootGridSetting BootGrid的配置json
     * @param loadedCallback 绑定事件的回调函数
     * @param initCallback 初始化的回调函数
     */
    "CommonBootGrid": function (tableId, bootGridSetting, loadedCallback, initCallback) {
      comm.BootGridUtil(tableId, bootGridSetting, loadedCallback);
      if (isFunction(initCallback)) {
        initCallback();
      }
    },
    /*
     * 通用ajax提交数据
     * @param url ajax的路径
     * @param param ajax提交的参数
     * @param conf  配置参数，见jQuery Ajax
     * @param target 窗口的target，默认是top
     */
    "CommonAjaxData": function (url, param, conf, target) {
      ajaxPostData(url, param, conf, target);
    },
    /*
     * 通用删除数据
     * @param url ajax的路径
     * @param param ajax提交的参数，必须包含参数ids
     * @param conf  配置参数，见jQuery Ajax
     * @param target 窗口的target，默认是top
     */
    "CommonDelData": function (url, param, conf, target) {
      //删除前的检验
      var msg = "", len = 0;
      if (!param.ids) {
        //数据格式错误
        msg = "\u6570\u636E\u683C\u5F0F\u9519\u8BEF";
      } else {
        if (0 === param.ids.length) {
          //请先选择数据后，再操作！
          msg = "\u8BF7\u5148\u9009\u62E9\u6570\u636E\u540E\uFF0C\u518D\u64CD\u4F5C\uFF01";
        } else {
          //处理ids
          var ids = param.ids;
          len = ids.length;
          param.ids = [];
          for (var i = 0, l = ids.length; i < l; i++) {
            param["ids[" + i + "]"] = ids[i];
          }
        }
      }
      if (0 < msg.length) {
        Layer.showError(defaults.title, msg, null, target);
      } else {
        var content = delTemplate.compile({
          "len": len
        });
        Layer.showConfirm("\u5220\u9664\u786E\u8BA4", content, function (callback) {
          if (!conf) {
            conf = {};
          }
          var preAjaxCallback = conf.preAjaxCallback;
          if (isFunction(preAjaxCallback)) {
            if (isFunction(callback)) {
              conf.preAjaxCallback = function () {
                preAjaxCallback();
                callback();
              };
            }
          } else {
            if (isFunction(callback)) {
              conf.preAjaxCallback = function () {
                callback();
              };
            }
          }
          ajaxPostData(url, param, conf, target);
        }, target);
      }
    },
    /*
     * 通用form的ajaxSubmit
     * @param formObj form的jQuery对象
     * @param conf  配置参数，见jQuery Ajax，不需要设置url和data
     * @param target 窗口的target，默认是top
     */
    "CommonAjaxFormData": function (formObj, conf, target) {
      var options = $.extend({}, defaults, conf);
      window.setTimeout(function () {
        if (isFunction(options.preAjaxCallback)) {
          options.preAjaxCallback();
        }
      }, 100);
      var loadingIdx = Layer.showLoading(options.loadingType, false, target);
      formObj.ajaxSubmit(getAjaxConf(loadingIdx, +new Date(), options, target));
    },
    /*
     * 多值删除数据
     * @param url ajax的路径
     * @param param ajax提交的参数，必须包含参数ids
     * @param conf  配置参数，见jQuery Ajax
     * @param arrStr 待处理的字段名称数组
     * @param target 窗口的target，默认是top
     */
    "CommonDelMultiData": function (url, param, conf, arrNames, target) {
      //删除前的检验
      var msg = "", len = 0, flag = false;
      for (var ii = 0, ll = arrNames.length; ii < ll; ii++) {
        var colName = arrNames[ii];
        if (param[colName] && 0 !== param[colName].length) {
          flag = true;
          //处理ids
          var ids = param[colName];
          len += ids.length;
          param[colName] = [];
          for (var i = 0, l = ids.length; i < l; i++) {
            param[colName + "[" + i + "]"] = ids[i];
          }
        }
      }
      if (!flag) {
        //请先选择数据后，再操作！
        msg = "\u8BF7\u5148\u9009\u62E9\u6570\u636E\u540E\uFF0C\u518D\u64CD\u4F5C\uFF01";
      }
      if (0 < msg.length) {
        Layer.showError(defaults.title, msg, null, target);
      } else {
        var content = delTemplate.compile({
          "len": len
        });
        Layer.showConfirm("\u5220\u9664\u786E\u8BA4", content, function (callback) {
          if (!conf) {
            conf = {};
          }
          var preAjaxCallback = conf.preAjaxCallback;
          if (isFunction(preAjaxCallback)) {
            if (isFunction(callback)) {
              conf.preAjaxCallback = function () {
                preAjaxCallback();
                callback();
              };
            }
          } else {
            if (isFunction(callback)) {
              conf.preAjaxCallback = function () {
                callback();
              };
            }
          }
          ajaxPostData(url, param, conf, target);
        }, target);
      }
    }
  };
})(CommonUtils, jQuery);
