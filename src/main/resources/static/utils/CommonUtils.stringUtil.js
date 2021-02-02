/**
 * CommonUtils.StringUtil，字符串工具类
 * @author BBF
 */
;
(function (comm) {
  "use strict";
  comm.StringUtil = {
    /**
     * 两端去空格
     * @param {String} str 字符串
     */
    "trim": function (str) {
      if ("function" === typeof String.prototype.trim) {
        return str.trim();
      } else {
        return str.replace(/(^\s*)|(\s*$)/g, "");
      }
    },
    /**
     * 左边去空格
     * @param {String} str 字符串
     */
    "trimLeft": function (str) {
      if ("function" === typeof String.prototype.trimLeft) {
        return str.trimLeft();
      } else {
        return str.replace(/(^\s*)/g, "");
      }
    },
    /**
     * 右边去空格
     * @param {String} str 字符串
     */
    "trimRight": function (str) {
      if ("function" === typeof String.prototype.trimRight) {
        return str.trimRight();
      } else {
        return str.replace(/(\s*$)/g, "");
      }
    },
    /**
     * 是否包含指定字符串
     * @param {String} str 字符串
     * @param {String} parts 指定字符串
     */
    "includes": function (str, parts) {
      if ("function" === typeof String.prototype.includes) {
        return str.includes(parts);
      } else {
        return -1 !== str.indexOf(parts);
      }
    },
    /**
     * 是否以指定字符串开始
     * @param {String} str 字符串
     * @param {String} prefix 指定字符串
     */
    "startsWith": function (str, prefix) {
      if ("function" === typeof String.prototype.startsWith) {
        return str.startsWith(prefix);
      } else {
        return prefix === str.slice(0, prefix.length);
      }
    },
    /**
     * 是否以指定字符串结束
     * @param {String} str 字符串
     * @param {String} suffix 指定字符串
     */
    "endsWith": function (str, suffix) {
      if ("function" === typeof String.prototype.endsWith) {
        return str.endsWith(suffix);
      } else {
        return -1 !== str.indexOf(suffix, str.length - suffix.length);
      }
    },
    /**
     * 计算字节长度，汉字长度等于2
     * @param {String} str 字符串
     */
    "byteLength": function (str) {
      return str.replace(/[^\x00-\xff]/g, "..").length;
    },
    /**
     * 全角字符转换成半角
     * @param {String} str 字符串
     * @Example CommonUtils.StringUtil.dbc2sbc("ＡＢＣ　１２３，我们都是好朋友");
     */
    "dbc2sbc": function (str) {
      //全角空格为12288，半角空格为32
      //其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
      return str.replace(/[\uff01-\uff5e]/g, function (a) {
        return String.fromCharCode(a.charCodeAt(0) - 65248);
      }).replace(/\u3000/g, " ");
    },
    /**
     * 字符串转换成字符数组
     * @param {String} str 字符串
     */
    "toArray": function (str) {
      return str.split("");
    },
    /**
     * 字符串倒序
     * @param {String} str 字符串
     */
    "reverse": function (str) {
      return this.toArray(str).reverse().join("");
    },
    /**
     * 是否undefined或null
     * @param {String} str 字符串
     */
    "isNull": function (str) {
      return typeof str === "undefined" || str === null;
    },
    /**
     * 转换为空串
     * @param {String} str 字符串
     */
    "killNull": function (str) {
      if (this.isNull(str)) {
        return "";
      }
      return this.trim(str);
    },
    /**
     * 是否空白
     * @param {String} str 字符串
     */
    "isBlank": function (str) {
      return "undefined" === typeof (str) || null === str || "" === this.trim(str);
    },
    /**
     * 是否不为空白
     * @param {String} str 字符串
     */
    "isNotBlank": function (str) {
      return !this.isBlank(str);
    },
    /**
     * 是否数字
     * @param {String} str 字符串
     */
    "isNumber": function (str) {
      if (this.isBlank(str)) {
        return false;
      }
      return (/^\d*$/g).test(str);
    },
    /**
     * 合并多个空白为一个空白
     * @param {String} str 字符串
     */
    "resetBlank": function (str) {
      return str.replace(/s+/g, " ")
    },
    /**
     * 左截取指定长度的字符串
     * @param {String} str 字符串
     * @param {Number} len 长度
     */
    "left": function (str, len) {
      if (len < this.byteLength(str)) {
        var strArr = this.toArray(str), newArr = [], newLen = 0, charLen;
        do {
          var oneChar = strArr.shift();
          charLen = this.byteLength(oneChar);
          newLen += charLen;
          if (len >= newLen) {
            newArr.push(oneChar);
          } else {
            break;
          }
        } while (true);
        strArr = null;
        return newArr.join("");
      } else {
        return str;
      }
    },
    /**
     * 右截取指定长度的字符串
     * @param {String} str 字符串
     * @param {Number} len 长度
     */
    "right": function (str, len) {
      return this.reverse(this.left(this.reverse(str), len));
    },
    /**
     * 获取对象的类型名
     * @param {Object} object
     * @example CommonUtils.StringUtil.getClass(5); // => "Number"
     * @example CommonUtils.StringUtil.getClass({}); // => "Object"
     * @example CommonUtils.StringUtil.getClass(/foo/); // => "RegExp"
     * @example CommonUtils.StringUtil.getClass(""); // => "String"
     * @example CommonUtils.StringUtil.getClass(true); // => "Boolean"
     * @example CommonUtils.StringUtil.getClass([]); // => "Array"
     * @example CommonUtils.StringUtil.getClass(undefined); // => "Window"
     * @example CommonUtils.StringUtil.getClass(Element); // => "Constructor"
     */
    "getClass": function (object) {
      return Object.prototype.toString.call(object).match(/^\[object\s(.*)]$/)[1];
    },
    /**
     * 产生任意长度随机字母数字组合
     * @param {Number} len 密码长度
     * @param {Number} randlen 附加随机长度
     */
    "randomWord": function (len, randlen) {
      var str = [], range = len;
      var arr = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz",
          arrlen = arr.length;
      //随机产生
      if (randlen > 0) {
        range = Math.round(Math.random() * randlen) + len;
      }
      arrlen--;
      for (var i = 0; i < range; i++) {
        var pos = Math.round(Math.random() * arrlen);
        str.push(arr[pos]);
      }
      str = str.join("");
      return str;
    },
    /**
     * Base64编码
     * @param str 待编码字符串
     * @return {string}
     */
    "base64Encode": function (str) {
      return window.btoa(unescape(encodeURIComponent(str)));
    },
    /**
     * Base64解码
     * @param code Base64编码字符串
     * @return {string}
     */
    "base64Decode": function (code) {
      return decodeURIComponent(escape(window.atob(code)));
    },
    /**
     * Base64字符串转成ArrayBuffer数组
     * @param {String} base64
     */
    "base64ToArrayBuffer": function (base64) {
      var binary_string = window.atob(base64);
      var len = binary_string.length;
      var bytes = new Uint8Array(len);
      for (var i = 0; i < len; i++) {
        bytes[i] = binary_string.charCodeAt(i);
      }
      return bytes.buffer;
    },
    /**
     * ArrayBuffer数组转成Base64字符串
     * @param {ArrayBuffer} buffer
     */
    "arrayBufferToBase64": function (buffer) {
      var bytes = new Uint8Array(buffer);
      var binary = "";
      for (var i = 0, l = bytes.byteLength; i < l; i++) {
        binary += String.fromCharCode(bytes[i]);
      }
      return window.btoa(binary);
    },
    /**
     * 返回文档碎片
     * @param {String} html
     * @param {String} tags 承载html的标签
     * @return DocumentFragment
     */
    "getDocumentFragment": function (html, tags) {
      tags = tags || "div";
      var tmpDiv = document.createElement(tags);
      tmpDiv.innerHTML = html;
      var df = document.createDocumentFragment();
      for (var i = 0, l = tmpDiv.childNodes.length; i < l; i++) {
        var one = tmpDiv.firstChild;
        if (1 === one.nodeType) {
          //节点对象
          df.appendChild(one);
        } else {
          tmpDiv.removeChild(one);
        }
      }
      tmpDiv = null;
      return df;
    },
    /**
     * Unicode（UTF-16）转 UTF-8
     * @param str Unicode字符串
     * @return {string}
     */
    "unicodeToUtf8": function (str) {
      var out = "";
      for (var i = 0, l = str.length; i < l; i++) {
        var c = str.charCodeAt(i);
        if (c >= 0x0001 && c <= 0x007F) {
          out += str.charAt(i);
        } else if (c > 0x07FF) {
          out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));
          out += String.fromCharCode(0x80 | ((c >> 6) & 0x3F));
          out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
        } else {
          out += String.fromCharCode(0xC0 | ((c >> 6) & 0x1F));
          out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
        }
      }
      return out;
    },
    /**
     * 驼峰命名，将word-word转换为wordWord
     * @param {String} str 字符串
     */
    "camelCase": function (str) {
      return str.replace(/-(\w)/g, function (math, p1) {
        return p1.toUpperCase();
      });
    },
    /**
     * 将wordWord转换为word-word
     * @param {String} str 字符串
     * @param {String} sep 分隔符，默认 -
     */
    "unCamelCase": function (str, sep) {
      sep = sep || "-";
      return str.replace(/([a-z])([A-Z])/g, function (match, p1, p2) {
        return p1 + sep + p2.toLowerCase();
      });
    }
  };
})(CommonUtils);
