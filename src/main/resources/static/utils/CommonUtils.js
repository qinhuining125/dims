/**
 * 通用的工具类
 * @author BBF
 */
if (!window.CommonUtils) {
  (function () {
    "use strict";
    if (!window.location.origin) {
      /**
       * 协议名、主机名和端口号
       * @type {string}
       */
      window.location.origin = window.location.protocol + "//" + window.location.hostname
          + (window.location.port ? ":" + window.location.port : "");
    }

    /**
     * Base64基础方法实现，使用参见CommonUtils.StringUtil.base64Encode
     */
    if (!window.btoa) {
      var a = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
      window.btoa = function (c) {
        var d = "";
        var m, k, h = "";
        var l, j, g, f = "";
        var e = 0;
        do {
          m = c.charCodeAt(e++);
          k = c.charCodeAt(e++);
          h = c.charCodeAt(e++);
          l = m >> 2;
          j = ((m & 3) << 4) | (k >> 4);
          g = ((k & 15) << 2) | (h >> 6);
          f = h & 63;
          if (isNaN(k)) {
            g = f = 64;
          } else {
            if (isNaN(h)) {
              f = 64;
            }
          }
          d = d + a.charAt(l) + a.charAt(j) + a.charAt(g) + a.charAt(f);
          m = k = h = "";
          l = j = g = f = "";
        } while (e < c.length);
        return d;
      };
      window.atob = function (c) {
        var d = "";
        var m, k, h = "";
        var l, j, g, f = "";
        var e = 0;
        do {
          l = a.indexOf(c.charAt(e++));
          if (l < 0) {
            continue;
          }
          j = a.indexOf(c.charAt(e++));
          if (j < 0) {
            continue;
          }
          g = a.indexOf(c.charAt(e++));
          if (g < 0) {
            continue;
          }
          f = a.indexOf(c.charAt(e++));
          if (f < 0) {
            continue;
          }
          m = (l << 2) | (j >> 4);
          k = ((j & 15) << 4) | (g >> 2);
          h = ((g & 3) << 6) | f;
          d += String.fromCharCode(m);
          if (g !== 64) {
            d += String.fromCharCode(k);
          }
          if (f !== 64) {
            d += String.fromCharCode(h);
          }
          m = k = h = "";
          l = j = g = f = ""
        } while (e < c.length);
        return d;
      }
    }

    if (!window.requestAnimationFrame) {
      window.requestAnimationFrame = (window.webkitRequestAnimationFrame ||
          window.mozRequestAnimationFrame ||
          window.oRequestAnimationFrame ||
          window.msRequestAnimationFrame ||
          function (callback) {
            return window.setTimeout(callback, 1000 / 60);
          });
    }

    if (!window.cancelAnimationFrame) {
      window.cancelAnimationFrame = (window.cancelRequestAnimationFrame ||
          window.webkitCancelAnimationFrame ||
          window.webkitCancelRequestAnimationFrame ||
          window.mozCancelAnimationFrame || window.mozCancelRequestAnimationFrame ||
          window.msCancelAnimationFrame || window.msCancelRequestAnimationFrame ||
          window.oCancelAnimationFrame || window.oCancelRequestAnimationFrame ||
          window.clearTimeout);
    }

    window.CommonUtils = {
      /**
       * 在指定时间区域内，多次触发事件，只执行最后一次
       * @param fn 实际执行的函数
       * @param delay 延迟时间，单位毫秒
       * @return {Function}
       * 调用方法：CommonUtils.debounce(function(){}, 500);
       */
      "debounce": function (fn, delay) {
        var timer = null;
        // 返回一个函数，这个函数会在一个时间区间结束后的 delay 毫秒时执行 fn 函数
        return function () {
          var ctx = this, args = arguments;
          // 当持续触发事件时，若发现事件触发的定时器已设置时，则清除之前的定时器
          if (timer) {
            clearTimeout(timer);
            timer = null;
          }
          // 重新设置事件触发的定时器
          timer = setTimeout(function () {
            // 保存函数调用时的上下文和参数，传递给 fn
            fn.apply(ctx, args);
            // 当事件真正执行后，清空定时器
            timer = null;
          }, delay);
        }
      },
      /**
       * 频度控制，两次执行间隔固定时间区间
       * @param fn 实际执行的函数
       * @param hold 间隔时间，单位毫秒
       * @return {Function}
       */
      "throttle": function (fn, hold) {
        // 记录上次触发事件
        var last, timer;
        return function () {
          var ctx = this, args = arguments;
          var now = +Date.now();
          if (last && now < last + hold) {
            // 如果距离上次执行 fn 函数的时间小于 hold，那么就放弃
            // 执行 fn，并重新计时
            clearTimeout(timer);
            // 保证在当前时间区间结束后，再执行一次 fn
            timer = setTimeout(function () {
              last = now;
              fn.apply(ctx, args)
            }, hold);
          } else {
            // 在时间区间的最开始和到达指定间隔的时候执行一次 fn
            last = now;
            fn.apply(ctx, args);
          }
        }
      },
      "color": {
        "parse": function (color, toNumber) {
          if (true === toNumber) {
            if ("number" === typeof color) {
              return (color | 0);
            }
            if ("string" === typeof color && "#" === color[0]) {
              //如果传入的是#fff000,那么得到的是fff000，将#剪切掉
              color = color.slice(1);
            }
            return window.parseInt(color, 16);
          } else {
            if ("number" === typeof color) {
              //(color | 0).toString(16) -> 64
              //"00000" + (color | 0).toString(16) ->"0000064";
              //"#"+("00000" + (color | 0).toString(16)).substr(-6); ->"#000064";
              color = "#" + ("00000" + (color | 0).toString(16)).substr(-6); //pad
            }
            return color;
          }
        },
        "toRGB": function (color, alpha) {
          //如果是字符串格式，转换为数字
          if ("string" === typeof color && "#" === color[0]) {
            //parseInt(("#ffffff").slice(1),16) 为 16777215
            color = window.parseInt(color.slice(1), 16);
          }
          alpha = undefined === alpha ? 1 : alpha;
          //将color转换成r,g,b值，>>右移  <<左移
          //例如：16777215 >> 16 变成 255， 255 & 0xff为255
          var r = color >> 16 & 0xff;
          var g = color >> 8 & 0xff;
          var b = color & 0xff;
          a = alpha < 0 ? 0 : (alpha > 1 ? 1 : alpha);
          if (1 === a) {
            return "rgb(" + r + "," + g + "," + b + ")";
          } else {
            return "rgb(" + r + "," + g + "," + b + "," + a + ")";
          }
        }
      },
      /**
       * 捕获鼠标移动事件的坐标
       * @param element 触发事件的元素
       * @return {{x: number, y: number}}
       */
      "captureMouse": function (element) {
        var mouse = {x: 0, y: 0};
        element.addEventListener("mousemove", function (event) {
          var x, y;
          if (event.pageX || event.pageY) {
            x = event.pageX;
            y = event.pageY;
          } else {
            x = event.clientX + document.body.scrollLeft + document.documentElement.scrollLeft;
            y = event.clientY + document.body.scrollTop + document.documentElement.scrollTop;
          }
          x -= element.offsetLeft;
          y -= element.offsetTop;
          mouse.x = x;
          mouse.y = y;
        }, false);
        return mouse;
      },
      /**
       * 捕获触摸事件的坐标
       * @param element 触发事件的元素
       * @return {{x: null, y: null, isPressed: boolean, event: null}}
       */
      "captureTouch": function (element) {
        var touch = {x: null, y: null, isPressed: false, event: null},
            body_scrollLeft = document.body.scrollLeft,
            element_scrollLeft = document.documentElement.scrollLeft,
            body_scrollTop = document.body.scrollTop,
            element_scrollTop = document.documentElement.scrollTop,
            offsetLeft = element.offsetLeft,
            offsetTop = element.offsetTop;
        element.addEventListener("touchstart", function (event) {
          touch.isPressed = true;
          touch.event = event;
        }, false);
        element.addEventListener("touchend", function (event) {
          touch.isPressed = false;
          touch.x = null;
          touch.y = null;
          touch.event = event;
        }, false);
        element.addEventListener("touchmove", function (event) {
          var x, y, touch_event = event.touches[0]; //first touch
          if (touch_event.pageX || touch_event.pageY) {
            x = touch_event.pageX;
            y = touch_event.pageY;
          } else {
            x = touch_event.clientX + body_scrollLeft + element_scrollLeft;
            y = touch_event.clientY + body_scrollTop + element_scrollTop;
          }
          x -= offsetLeft;
          y -= offsetTop;
          touch.x = x;
          touch.y = y;
          touch.event = event;
        }, false);
        return touch;
      }
    };
  })(window);

  //简单的模板引擎
  if (!String.prototype.compile) {
    //针对于某个键的helper
    var handler = {};
  }

  if ("function" !== typeof String.prototype.compile) {
    /**
     * 简单的模板引擎实现
     * @param jsonDate json对象
     * @param prefixes 递归用，正常调用时，不需要使用这个参数
     * @return {String}
     */
    String.prototype.compile = function (jsonDate, prefixes) {
      var result = this;
      for (var key in jsonDate) {
        if (!jsonDate.hasOwnProperty(key)) {
          continue;
        }
        var value = jsonDate[key];
        if (value != null && "function" !== typeof value) {
          if ("object" === typeof value) {
            var keys = prefixes;
            if (!prefixes) {
              keys = [];
            }
            keys.push(key);
            result = result.compile(value, keys) + "";
          } else {
            if (handler && handler[key] && "function" === typeof handler[key]) {
              value = handler[key](value);
            }
            key = prefixes ? prefixes.join(".") + "." + key : key;
            var pattern = new RegExp("\\{\\{" + key + "\\}\\}", "gm");
            result = result.replace(pattern, (value.replace) ? value.replace(/\$/gi, "&#36;")
                : value);
          }
        }
      }
      return result;
    };
  }

  if ("function" !== typeof String.prototype.compileEmpty) {
    /**
     * 空模板引擎处理，清除占位标签
     * @return {string}
     */
    String.prototype.compileEmpty = function () {
      var pattern = new RegExp("\\{\\{.+?\\}\\}", "gm");
      return this.replace(pattern, "");
    };
  }

  if ("function" !== typeof Array.prototype.indexOf) {
    Array.prototype.indexOf = function (searchElement, fromIndex) {
      var index = -1;
      fromIndex = fromIndex >> 0;
      for (var i = 0, len = this.length; i < len; i++) {
        if (i >= fromIndex && this[i] === searchElement) {
          index = i;
          break;
        }
      }
      return index;
    };
  }

  if (typeof Array.prototype.map !== "function") {
    Array.prototype.map = function (fn, context) {
      var arr = [];
      if (typeof fn === "function") {
        for (var k = 0, length = this.length; k < length; k++) {
          arr.push(fn.call(context, this[k], k, this));
        }
      }
      return arr;
    };
  }

  if ("function" !== typeof Array.prototype.reduce) {
    Array.prototype.reduce = function (callback, initialValue) {
      var previous = initialValue, k = 0, len = this.length;
      if (typeof initialValue === "undefined") {
        previous = this[0];
        k = 1;
      }
      if (typeof callback === "function") {
        for (k; k < len; k++) {
          this.hasOwnProperty(k) && (previous = callback(previous, this[k], k, this));
        }
      }
      return previous;
    };
  }

  if ("function" !== typeof Array.prototype.remove) {
    /**
     * 删除指定元素
     * @param item 要删除的元素
     */
    Array.prototype.remove = function (item) {
      var index = this.indexOf(item);
      if (index > -1) {
        this.splice(index, 1);
      }
    };
  }

  if ("function" !== typeof Array.prototype.insert) {
    /**
     * 插入元素到指定位置
     * @param index 插入的下标
     * @param item 要插入的元素
     */
    Array.prototype.insert = function (index, item) {
      this.splice(index, 0, item);
    };
  }

  if ("function" !== typeof Date.prototype.Format) {
    /**
     * 将 Date 转化为指定格式的String
     * 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符
     * 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
     * @param fmt 日期格式字符串
     * @return {string}
     * (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
     * (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
     */
    Date.prototype.Format = function (fmt) {
      var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
      };
      if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
      for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
          fmt = fmt.replace(RegExp.$1, (1 === RegExp.$1.length) ? (o[k]) : (("00" + o[k])
              .substr(("" + o[k]).length)));
      return fmt;
    }
  }
}

/**
 * 表单序列化成JSON对象，多选值保存是数组
 * @author BBF
 */
(function ($) {
  $.fn.serializeJson = function () {
    var serializeObj = {};
    var array = this.serializeArray();
    $(array).each(function () {
      if (serializeObj[this.name]) {
        if ($.isArray(serializeObj[this.name])) {
          serializeObj[this.name].push(this.value);
        } else {
          serializeObj[this.name] = [serializeObj[this.name], this.value];
        }
      } else {
        serializeObj[this.name] = this.value;
      }
    });
    return serializeObj;
  };
})(jQuery);

/**
 * 提示类，需要引入toastr.js
 * @author BBF
 * */
if ("object" === typeof (toastr)) {
  (function (comm, toastr) {
    "use strict";
    toastr.options = {
      "closeButton": true,
      "debug": false,
      "progressBar": true,
      "positionClass": "toast-top-center",
      "onclick": null,
      "showDuration": "200",
      "hideDuration": "1000",
      "timeOut": "2000",
      "extendedTimeOut": "1000",
      "showEasing": "swing",
      "hideEasing": "linear",
      "showMethod": "fadeIn", //show,fadeIn,slideDown
      "hideMethod": "slideUp" //hide,fadeOut,slideUp
    };
    comm.ToastrUtil = {
      "success": function (msg, title) {
        toastr.success(msg, title || "\u6210\u529F");
      },
      "warning": function (msg, title) {
        toastr.warning(msg, title || "\u8B66\u544A");
      },
      "error": function (msg, title) {
        toastr.error(msg, title || "\u9519\u8BEF");
      },
      "info": function (msg, title) {
        toastr.info(msg, title || "\u63D0\u793A");
      }
    };
  })(CommonUtils, toastr);
}

/**
 * Handlebar的自定义函数
 */
if ("object" === typeof Handlebars) {
  /**
   * 处理条件分支，判断是否等于1
   */
  Handlebars.registerHelper("fun_equals_one", function (condition, options) {
    if (condition === 1) {
      return options.fn(this);
    } else {
      return options.inverse(this);
    }
  });
  Handlebars.registerHelper("fun_timestamp", function (timestamp) {
    try {
      return new Date(timestamp).Format("yyyy-MM-dd hh:mm:ss");
    } catch (e) {
      return "";
    }
  });
}

if (!colorReverse && "function" !== typeof colorReverse) {
  /**
   * 颜色反色，例如#f00fff -- #0ff000
   */
  function colorReverse(colorHex) {
    var reg = /#([a-fA-F0-9]{2})([a-fA-F0-9]{2})([a-fA-F0-9]{2})/;
    var m = reg.exec(colorHex);
    var hex = "#";
    for (var i = 0; i < 3; i++) {
      var s = (255 - parseInt(m[i + 1], 16)).toString(16);
      if (s.length === 1) {
        hex += "0";
      }
      hex += s;
    }
    return hex;
  }
}