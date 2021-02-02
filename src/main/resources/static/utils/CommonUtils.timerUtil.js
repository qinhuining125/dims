/**
 * CommonUtils.TimerUtil，通用的时间工具类
 * @author BBF
 */
;
(function (comm) {
  'use strict';
  var tipBlockTime = 700; //阻塞时间，单位毫秒
  var getTimeInterval = function (startTime) {
    //如果AJAX返回的时间小于阻塞时间，则将结果暂时隐藏。此处返回0，表示禁止阻塞
    var diff = new Date().valueOf() - startTime.valueOf();
    return diff > tipBlockTime ? 0 : tipBlockTime - diff;
  };

  var timeString = {
    "units": {
      "second": "秒",
      "seconds": "秒",
      "minute": "分钟",
      "minutes": "分钟",
      "hour": "小时",
      "hours": "小时",
      "day": "天",
      "days": "天",
      "month": "个月",
      "months": "个月",
      "year": "年",
      "years": "年"
    },
    "prefixes": {
      "jsut": "刚刚",
      "lt": "",
      "about": "",
      "over": "",
      "almost": ""
    },
    "suffix": "之前"
  };
  comm.TimerUtil = {
    "timer": new Date(), //通用计时器对象暂存
    "clearTimeout": function () {
      window.clearTimeout(timer);
    },
    "clearInterval": function () {
      window.clearInterval(timer);
    },
    "tipBlockTime": tipBlockTime,
    "getTimeInterval": getTimeInterval,
    "timeAgo": {
      "lang": timeString,
      //入口函数
      //CommonUtils.TimerUtil.timeAgo.timeAgoInWords(new Date());
      //CommonUtils.TimerUtil.timeAgo.timeAgoInWords(1472610307935);
      //CommonUtils.TimerUtil.timeAgo.timeAgoInWords("2016-8-29")
      "timeAgoInWords": function (dateObj) {
        var date, dt = typeof(dateObj);
        if ("number" === dt) {
          date = new Date(dateObj);
        } else if ("string" === dt) {
          date = this.parse(dateObj);
        } else {
          date = dateObj;
        }
        var timeInWord = this.distanceOfTimeInWords(date);
        if ("" === timeInWord) {
          return this.lang.prefixes.jsut;
        }
        return timeInWord + this.lang.suffix;
      },
      "parse": function (dataString) {
        var timeStr = dataString.replace(/(^\s*)|(\s*$)/g, '');
        timeStr = timeStr.replace(/\.\d\d\d+/, "");
        timeStr = timeStr.replace(/-/, "/").replace(/-/, "/");
        timeStr = timeStr.replace(/T/, " ").replace(/Z/, " UTC");
        timeStr = timeStr.replace(/([+\-]\d\d):?(\d\d)/, " $1$2");
        return new Date(timeStr);
      },
      "getTimeDistanceInMinutes": function (absoluteTime) {
        var timeDistance = new Date().getTime() - absoluteTime.getTime();
        return Math.round((Math.abs(timeDistance) / 1000) / 60);
      },
      "distanceOfTimeInWords": function (absolutTime) {
        var dim = this.getTimeDistanceInMinutes(absolutTime);
        if (0 === dim) {
          return "";
        } else if (1 === dim) {
          return "1 " + this.lang.units.minute;
        } else if (dim >= 2 && dim <= 44) {
          return "" + dim + " " + this.lang.units.minutes;
        } else if (dim >= 45 && dim <= 89) {
          return "" + this.lang.prefixes.about + " 1 " + this.lang.units.hour;
        } else if (dim >= 90 && dim <= 1439) {
          return "" + this.lang.prefixes.about + " " + (Math.round(dim / 60)) + " "
              + this.lang.units.hours;
        } else if (dim >= 1440 && dim <= 2519) {
          return "1 " + this.lang.units.day;
        } else if (dim >= 2520 && dim <= 43199) {
          return "" + (Math.round(dim / 1440)) + " " + this.lang.units.days;
        } else if (dim >= 43200 && dim <= 86399) {
          return "" + this.lang.prefixes.about + " 1 " + this.lang.units.month;
        } else if (dim >= 86400 && dim <= 525599) {
          return "" + (Math.round(dim / 43200)) + " " + this.lang.units.months;
        } else if (dim >= 525600 && dim <= 655199) {
          return "" + this.lang.prefixes.about + " 1 " + this.lang.units.year;
        } else if (dim >= 655200 && dim <= 914399) {
          return "" + this.lang.prefixes.over + " 1 " + this.lang.units.year;
        } else if (dim >= 914400 && dim <= 1051199) {
          return "" + this.lang.prefixes.almost + " 2 " + this.lang.units.years;
        } else {
          return "" + this.lang.prefixes.about + " " + (Math.round(dim / 525600)) + " "
              + this.lang.units.years;
        }
      }
    },
    /**
     * 获取指定日期所在周的指定星期值的日期
     * 注意：本方法中根据国际通用算法，周日作为一周的起点。
     *
     * @param date 指定日期
     * @param dayOfWeek 指定星期值(0~6：周日~周六)
     * @return 指定星期值的日期
     */
    "getWeekday": function (date, dayOfWeek) {
      // 获得当前日期是一个星期的第几天
      var d = date.getDay();
      if (d != dayOfWeek) {
        date.setDate(date.getDate() + dayOfWeek - d);
      }
      return date;
    },
    /**
     * 获取指定日期所在周的指定星期值的日期
     * 本方法中周一作为一周的起点
     *
     * @param date 指定日期
     * @param dayOfWeek 指定星期值(0~6：周日~周六)
     * @return 指定星期值的日期
     */
    "getWeekdayForChina": function (date, dayOfWeek) {
      var d = date.getDay();
      if (d == 0) {
        if (d == dayOfWeek) {
          return date;
        } else {
          date.setDate(date.getDate() - 7);
          date = this.getWeekday(date, dayOfWeek);
          return date;
        }
      }
      date = this.getWeekday(date, dayOfWeek);
      if (date.getDay() == 0) {
        // 判断是否是周日，如果是就加7，因为按照中国算法，这应该算是上周日了。
        date.setDate(date.getDate() + 7);
        return date;
      }
      date = this.getWeekday(date, dayOfWeek);
      return date;
    }
  };
})(CommonUtils);
