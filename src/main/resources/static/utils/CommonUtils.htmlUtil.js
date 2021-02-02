/**
 * CommonUtils.HtmlUtil，HTML工具类
 * @author BBF
 */
;
(function (comm) {
  'use strict';
  /**
   * 块结构元素列表
   */
  var BLOCK = {
    "address": 1,
    "blockquote": 1,
    "center": 1,
    "dir": 1,
    "div": 1,
    "dl": 1,
    "fieldset": 1,
    "form": 1,
    "h1": 1,
    "h2": 1,
    "h3": 1,
    "h4": 1,
    "h5": 1,
    "h6": 1,
    "hr": 1,
    "isindex": 1,
    "menu": 1,
    "noframes": 1,
    "ol": 1,
    "p": 1,
    "pre": 1,
    "table": 1,
    "ul": 1
  };
  comm.HtmlUtil = {
    "fillChar": "\u200B",
    "isIE": false,
    /**
     * 是否初始化
     */
    "isInit": false,
    /**
     * 初始化元素列表
     */
    "init": function () {
      if (!this.isInit) {
        this.isInit = true;
        for (var k in BLOCK) {
          BLOCK[k.toUpperCase()] = BLOCK[k];
        }
        //需要过滤的字符
        var agent = navigator.userAgent.toLowerCase();
        var _ie = /(msie\s|trident.*rv:)([\w.]+)/.test(agent);
        if (_ie) {
          this.isIE = true;
          var v1 = agent.match(/(?:msie\s([\w.]+))/);
          var v2 = agent.match(/(?:trident.*rv:([\w.]+))/);
          var version = 0;
          if (v1 && v2 && v1[1] && v2[1]) {
            version = Math.max(v1[1] * 1, v2[1] * 1);
          } else if (v1 && v1[1]) {
            version = v1[1] * 1;
          } else if (v2 && v2[1]) {
            version = v2[1] * 1;
          } else {
            version = 0;
          }
          if ("6" === version) {
            this.fillChar = "\ufeff";
          }
        }
      }
    },
    /**
     * 是否以指定字符串开始
     * @param {String} str 字符串
     * @param {String} prefix 指定字符串
     */
    "startsWith": function (str, prefix) {
      if ("function" === typeof (String.prototype.startsWith)) {
        return str.startsWith(prefix);
      } else {
        return prefix === str.slice(0, prefix.length);
      }
    },
    /**
     * 获取编辑器中的纯文本内容,没有段落格式
     * @method getContentTxt
     * @return { String } 编辑器不带段落格式的纯文本内容字符串
     * @example
     * var html = "<p><strong>1</strong></p><p>2<img src='1.jpg'/></p>";
     * CommonUtils.HtmlUtil.getPlainTxt(html); //输出:"12"
     */
    "getContentTxt": function (html) {
      this.init();
      var reg = new RegExp(this.fillChar, "g");
      var divObj = document.createElement("div");
      divObj.innerHTML = html;
      //取出来的空格会有c2a0会变成乱码，处理这种情况\u00a0
      html = divObj[this.ie ? "innerText" : "textContent"].replace(reg, "").replace(/\u00a0/g, " ");
      divObj.innerHTML = "";
      divObj = null;
      return html;
    },
    /**
     * 得到编辑器的纯文本内容，但会保留段落格式
     * @param {String} html 需要处理的html字符串
     * @return {String} 编辑器带段落格式的纯文本内容字符串
     * @example
     * var html = "<p><strong>1</strong></p><p>2<img src='1.jpg'/></p>";
     * CommonUtils.HtmlUtil.getPlainTxt(html); //输出:"1\n<img src='1.jpg'/>\n"
     */
    "getPlainTxt": function (html) {
      this.init();
      var reg = new RegExp(this.fillChar, "g");
      var block = BLOCK;
      html = html.replace(/[\n\r]/g, "");//ie需要先去掉\n
      html = html.replace(/<(p|div)[^>]*>(<br\/?>|&nbsp;)<\/\1>/gi, "\n")
          .replace(/<br\/?>/gi, "\n").replace(/<[^>/]+>/g, "").replace(/(\n)?<\/([^>]+)>/g,
              function (a, b, c) {
                return block[c] ? "\n" : b ? b : "";
              });
      //取出来的空格会有c2a0会变成乱码，处理这种情况\u00a0
      return html.replace(reg, "").replace(/\u00a0/g, " ").replace(/&nbsp;/g, " ");
    },
    /**
     * 得到编辑器的纯文本内容，但会保留段落格式和table格式
     * @param {String} html 需要处理的html字符串
     * @return {String} 编辑器带段落格式的纯文本内容字符串
     * @example
     * var html = "<p><strong>1</strong></p><p>2<img src='1.jpg'/></p>";
     * CommonUtils.HtmlUtil.getPlainTxt(html); //输出:"1\n<img src='1.jpg'/>\n"
     */
    "getPlainTxt2": function (html) {
      this.init();
      var reg = new RegExp(this.fillChar, "g"), block = BLOCK;
      var that = this;
      html = html.replace(/[\n\r]/g, "");//ie需要先去掉\n
      html = html.replace(/<(p|div)[^>]*>(<br\/?>|&nbsp;)<\/\1>/gi, "\n")
          .replace(/<br\/?>/gi, "\n").replace(/<[^>/]+>/g, function (w) {
            if (that.startsWith(w, "<table")) {
              return "<table border=\"1\">";
            } else if (that.startsWith(w, "<tr")) {
              return "<tr>";
            } else if (that.startsWith(w, "<td")) {
              return "<td>";
            } else {
              return "";
            }
          }).replace(/(\n)?<\/([^>]+)>/g, function (a, b, c) {
            var _c = c.toLowerCase();
            if ("table" === _c) {
              return "</table>";
            } else if ("tr" === _c) {
              return "</tr>";
            } else if ("td" === _c) {
              return "</td>";
            } else {
              return block[c] ? "\n" : b ? b : "";
            }
          });
      //取出来的空格会有c2a0会变成乱码，处理这种情况\u00a0
      return html.replace(reg, "").replace(/\u00a0/g, " ").replace(/&nbsp;/g, " ");
    }
  };
})(CommonUtils);
