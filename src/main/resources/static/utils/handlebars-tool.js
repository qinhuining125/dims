//自定义handlebars方法

/**
 * 格式化时间戳
 * @param timestamp 时间戳
 * @param format 格式化类型
 */
Handlebars.registerHelper({
  'formatDate': function (timestamp, format) {//格式化时间
    if (arguments.length > 2) {
      format = arguments[1];
    }
    if (timestamp) {
      return new Date(timestamp).Format(format);
    } else {
      return '';
    }
  }
});

/**
 * 格式化金额
 * @param s 金额
 * @param n 保留位数
 * @param defaultVal 默认值（当s为未定义返回的值，不填写则返回""空字符串）
 */
Handlebars.registerHelper({
  'formatMoney': function (s, n, defaultVal) {
    return CommonUtils.StringUtil.formatMoney(s, n, defaultVal);
  }
});

/**
 * 数字金额大写转换(可以处理整数,小数,负数)
 * @param n 待转换数值
 * @returns {string}
 */
Handlebars.registerHelper({
  'smalltoBIG': function (n) {
    return CommonUtils.StringUtil.smalltoBIG(n);
  }
});

/**
 * 等于判断
 * @param v1 参数1
 * @param v2 参数2
 */
Handlebars.registerHelper('ifEqual', function (v1, v2, opts) {
  if (v1 === v2) {
    return opts.fn(this);
  } else {
    return opts.inverse(this);
  }
});

/**
 * 不等于判断
 * @param v1 参数1
 * @param v2 参数2
 */
Handlebars.registerHelper('ifNotEqual', function (v1, v2, opts) {
  if (v1 != v2) {
    return opts.fn(this);
  } else {
    return opts.inverse(this);
  }
});

/**
 * 注册索引+1的helper（作为序号展示使用）
 * @param index handlebars自带index
 */
var handleHelper = Handlebars.registerHelper("addOne", function (index) {
  //返回+1之后的结果
  return index + 1;
});

/**
 * 判断是否等于其中之一
 * @param v1 参数1
 * @param v2 参数2
 * @param v3 参数3
 */
Handlebars.registerHelper('ifEqualOneOf', function (v1, v2, v3, opts) {
  if (v1 === v2 || v1 === v3) {
    return opts.fn(this);
  } else {
    return opts.inverse(this);
  }
});

/**
 * 判断是否包含某个值
 * @param v1 参数1
 * @param v2 参数2
 */
Handlebars.registerHelper('ifContain', function (v1, v2, opts) {
  if (v1.indexOf(v2) !== -1) {
    return opts.fn(this);
  } else {
    return opts.inverse(this);
  }
});

/**
 * 生成UUID
 */
Handlebars.registerHelper('uuid', function () {
  var S4 = function () {
    return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
  };
  return (S4() + S4() + "-" + S4() + "-" + S4() + "-" + S4() + "-" + S4() + S4()
      + S4());
});

/**
 * 大小比较
 */
Handlebars.registerHelper("compare", function (v1, v2, options) {
  if (v1 >= v2) {
    return options.fn(this);    //满足条件执行，返回 this数据
  } else {
    return options.inverse(this);//不满足条件执行，不返回 this数据
  }
});

