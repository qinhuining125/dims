/**
 * CommonUtils.BootGridUtil，BttoGrid工具类
 * @author BBF
 * */
;
(function (comm, $) {
  'use strict';
  var defaults = {
    "ajax": true,
    "url": "",
    "post": null,
    "formatters": null,
    "navigation": 3, // 0无head和foot，1-只有head，2-只有foot，3-all
    "rowCount": [10, 20, 30, 40, 50], //分页设置为-1则不显示下拉分页选项
    "sorting": true, //是否开启排序
    "selection": true,
    "multiSelect": true,
    "rowSelect": false,
    "keepSelection": false,
    "paginationMsg": {
      "first": "\u9996\u9875",
      "prev": "\u4E0A\u4E00\u9875",
      "next": "\u4E0B\u4E00\u9875",
      "last": "\u672B\u9875"
    },
    "labels": {
      "all": "\u5168\u90E8",
      "infos": "<i class=\"fa fa-file-o\" style=\"margin-right:0.25rem\"></i>当前<span style=\"margin:0 0.25rem;color:blue\">{{ctx.start}}</span>至<span style=\"margin:0 0.25rem;color:blue\">{{ctx.end}}</span>条，共<span style=\"margin:0 0.25rem;color:blue\">{{ctx.total}}</span>条",
      "loading": "\u52A0\u8F7D\u4E2D...",
      "noResults": "\u4E0D\u597D\u610F\u601D\uFF0C\u6682\u65F6\u6CA1\u6709\u6570\u636E\u3002",
      "refresh": "\u5237\u65B0",
      "search": "\u641C\u7D22"
    },
    "statusMapping": { //行级状态码 - 类名
      "0": "success",
      "1": "info",
      "2": "warning",
      "3": "danger"
    },
    "css": {
      // 适配bootstrap4
      "paginationButton": "page-link"
    },
    "templates": { //自定义单元格的样式
      "footer": "<div id=\"{{ctx.id}}\" class=\"{{css.footer}}\" style=\"padding:0;margin: 15px 0 0 0\">" +
      "<div class=\"row\"><div class=\"col-sm-12 col-md-4 col-xl-4 col-lg-4 infoBar\" style=\"text-align:left\">" +
      "<p class=\"{{css.infos}}\"></p></div><div class=\"col-sm-12 col-md-8 col-xl-8 col-lg-8\" style=\"text-align:right\">" +
      "<p class=\"{{css.pagination}}\"></p></div></div></div>",
      // 适配bootstrap4
      "paginationItem": "<li class=\"{{ctx.css}} page-item\"><a data-page=\"{{ctx.page}}\" " +
      "class=\"{{css.paginationButton}}\">{{ctx.text}}</a></li>"
    },
    "responseHandler": function (response) {
      //当ajax出错的时候，进行特殊处理
      if (response && false === response.success) {
        var msg = response.message, code = response.code >> 0;
        response = "ajaxErr";
        var idx = top.CommonUtils.LayerUtil.showError("\u64CD\u4F5C\u5931\u8D25", msg, function () {
          if (401 === code) {
            top.location.reload();
          } else {
            top.CommonUtils.LayerUtil.close(idx);
          }
        });
      }
      return response;
    },
    //搜索：$("#id").bootgrid("search",{"a":1,"b":2});
    //post数据：searchPhrase[a]=1&searchPhrase[b]=2
    //启用高级搜索的模式
    //post数据：a=1&b=2
    "searchPhraseAdModel": 1
  };

  //修复bootGrid的按钮位置
  var fixedBootGrid = function () {
    //隐藏原来的工具条
    $(".bootgrid-header").hide();
    window.setTimeout(function () {
      $("#bootgrid-toolbar").append($(".actions.btn-group").children());
      $("#bootgrid-toolbar>.btn-default:first").attr("id", "bootgrid-btn-refresh").removeClass(
          "btn-default").addClass("btn-primary mr-2").children(".icon:first").html(
          "&nbsp;&#21047;&#26032;");
      $("#bootgrid-toolbar>.btn-group>.btn-default").removeClass("btn-default").addClass(
          "btn-success mr-1");
    }, 100);
  };

  //增强型的bootGrid入口函数
  $.fn.bootgridEx = function (option) {
    var opt = $.extend({}, defaults, option);
    return $(this).bootgrid(opt);
  };

  /**
   * 绑定表格
   * @param tableId 表格ID
   * @param bootGridSetting BootGrid的配置json
   * @param loadedCallback 加载数据完成后的回调函数，入参：当前上下文
   */
  comm.BootGridUtil = function (tableId, bootGridSetting, loadedCallback) {
    //初始化表格
    $(tableId).on("initialized.rs.jquery.bootgrid", function () {
      fixedBootGrid();
    }).bootgridEx(bootGridSetting).on("loaded.rs.jquery.bootgrid", function () {
      //加载完毕后绑定事件，这里要注意，bindEventFun必须使用on绑定，不要使用delegate
      if ("function" === typeof (loadedCallback)) {
        loadedCallback(this);
      }
    });
  };
})(CommonUtils, jQuery);
