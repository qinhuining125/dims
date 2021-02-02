/**
 * 简单的哈希表
 * @author BBF
 */
;

function HashMap() {
  /** 存放键的数组(遍历用到) */
  this.keys = [];
  /** 存放数据 */
  this.data = {};

  if ("function" !== typeof Array.prototype.indexOf) {
    Array.prototype.indexOf = function (val) {
      for (var i = 0; i < this.length; i++) {
        if (this[i] === val)
          return i;
      }
      return -1;
    };
  }

  if ("function" !== typeof Array.prototype.remove) {
    Array.prototype.remove = function (val) {
      var index = this.indexOf(val);
      if (index > -1) {
        this.splice(index, 1);
      }
    };
  }

  /**
   * 放入一个键值对
   * @param {String} key
   * @param {Object} value
   */
  this.put = function (key, value) {
    if (null == this.data[key]) {
      this.keys.push(key);
    }
    this.data[key] = value;
  };

  /**
   * 获取某键对应的值
   * @param {String} key
   * @return {Object} value
   */
  this.get = function (key) {
    return this.data[key];
  };

  /**
   * 删除一个键值对
   * @param {String} key
   */
  this.remove = function (key) {
    this.keys.remove(key);
    this.data[key] = null;
  };

  /**
   * 遍历Map,执行处理函数
   * @param fn 回调函数 function(key,value,index){..}
   */
  this.each = function (fn) {
    if ("function" === typeof (fn)) {
      return;
    }
    var len = this.keys.length;
    for (var i = 0; i < len; i++) {
      var k = this.keys[i];
      fn(k, this.data[k], i);
    }
  };

  /**
   * 获取键值数组(类似Java的entrySet())
   * @return Array{key,value}的数组
   */
  this.entrys = function () {
    var arr = [], len = this.keys.length;
    for (var i = 0; i < len; i++) {
      arr.push({
        "key": this.keys[i],
        "value": this.data[i]
      });
    }
    return arr;
  };

  /**
   * 判断Map是否为空
   */
  this.isEmpty = function () {
    return 0 === this.size();
  };

  /**
   * 获取键值对数量
   */
  this.size = function () {
    return this.keys.length;
  };

  /**
   * 转换为JSON
   */
  this.toJson = function () {
    var json = {}, len = this.keys.length;
    for (var i = 0; i < len; i++) {
      var k = this.keys[i];
      json[k] = this.data[k];
    }
    return json;
  };

  /**
   * 重写toString
   */
  this.toString = function () {
    var dt = [], len = this.keys.length;
    dt.push("{");
    for (var i = 0; i < len; i++) {
      var k = this.keys[i], v = this.data[k];
      var isString = ("string" === typeof (v));
      if (0 < i) {
        dt.push(", ");
      }
      dt.push("\"");
      dt.push(k);
      dt.push("\":");
      if (isString) {
        dt.push("\"");
      }
      dt.push(v);
      if (isString) {
        dt.push("\"");
      }
    }
    dt.push("}");
    dt = dt.join("");
    return dt;
  };
}

// function testMap() {
//   var m = new HashMap();
//   m.put('key1', 'Comtop');
//   m.put('key2', '南方电网');
//   m.put('key3', '景新花园');
//   m.put('key1', '康拓普');
//   m.remove("key2");
//   m.put('fun', function () {
//     alert(1)
//   });
//   return m;
// }

// var m = testMap();
// console.log(m);
// m.get("fun")();
