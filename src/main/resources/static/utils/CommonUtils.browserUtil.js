/**
 * CommonUtils.BrowserUtil，浏览器工具类
 * @author BBF
 */
;
(function (comm) {
  'use strict';
  var agent = navigator.userAgent.toLowerCase();
  var _ie = /(msie\s|trident.*rv:)([\w.]+)/.test(agent);
  var _opera = !!window.opera && window.opera.version;
  var _webkit = agent.indexOf(' applewebkit/') > -1;
  var _gecko = navigator.product === 'Gecko' && !_webkit && !_opera && !_ie;

  function getVersion() {
    var version = 0;
    if (_ie) {
      var v1 = agent.match(/(?:msie\s([\w.]+))/);
      var v2 = agent.match(/(?:trident.*rv:([\w.]+))/);
      if (v1 && v2 && v1[1] && v2[1]) {
        version = Math.max(v1[1] * 1, v2[1] * 1);
      } else if (v1 && v1[1]) {
        version = v1[1] * 1;
      } else if (v2 && v2[1]) {
        version = v2[1] * 1;
      } else {
        version = 0;
      }
    }
    if (_gecko) {
      var geckoRelease = agent.match(/rv:([\d.]+)/);
      if (geckoRelease) {
        geckoRelease = geckoRelease[1].split('.');
        version = geckoRelease[0] * 10000 + (geckoRelease[1] || 0) * 100 + (geckoRelease[2] || 0)
            * 1;
      }
    }
    //检测当前浏览器是否为Chrome, 如果是，则返回Chrome的大版本号
    if (/chrome\/(\d+\.\d)/i.test(agent)) {
      version = +RegExp['\x241'];
    }
    //检测当前浏览器是否为Safari, 如果是，则返回Safari的大版本号
    if (/(\d+\.\d)?(?:\.\d)?\s+safari\/?(\d+\.\d+)?/i.test(agent) && !/chrome/i.test(agent)) {
      version = +(RegExp['\x241'] || RegExp['\x242']);
    }
    // Opera 9.50+
    if (_opera) {
      version = parseFloat(window.opera.version());
    }
    // WebKit 522+ (Safari 3+)
    if (_webkit) {
      version = parseFloat(agent.match(/ applewebkit\/(\d+)/)[1]);
    }
    return version;
  }

  comm.BrowserUtil = {
    /**
     * @property {String} agent
     */
    "agent": agent,

    /**
     * @property {boolean} ie 检测当前浏览器是否为IE
     */
    "ie": _ie,

    /**
     * @property {boolean} quirks 检测当前浏览器是否处于“怪异模式”下
     */
    "quirks": "BackCompat" === document.compatMode,

    /**
     * @property {boolean} opera 检测当前浏览器是否为Opera
     */
    "opera": _opera,

    /**
     * @property {boolean} webkit 检测当前浏览器是否是webkit内核的浏览器
     */
    "webkit": _webkit,

    /**
     * @property {boolean} gecko 检测当前浏览器内核是否是gecko内核
     */
    "gecko": _gecko,

    /**
     * @property {boolean} mac 检测当前浏览器是否是运行在mac平台下
     */
    "mac": agent.indexOf('macintosh') > -1,

    /**
     * @property {Number} 浏览器版本
     */
    "version": getVersion()
  };
})(CommonUtils);
