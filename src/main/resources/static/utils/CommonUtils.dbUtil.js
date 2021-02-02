/**
 * CommonUtils.h5DbUtil，通用的HTML5存储工具类
 * @author BBF
 */
;
(function (comm) {
  'use strict';
  comm.h5DbUtil = {
    "localStorage": {
      /**
       * 获取应用存储区中保存的键值对的个数
       */
      "len": function () {
        return localStorage.length;
      },
      /**
       * 获取键值对中指定索引值的key值
       * @param {Number} i 键值对索引
       */
      "key": function (i) {
        return localStorage.key(i);
      },
      /**
       * 通过键(key)检索获取应用存储的值
       * @param {String} key 键名
       */
      "getItem": function (key) {
        if (key) {
          for (var i = 0, l = comm.h5DbUtil.localStorage.len(); i < l; i++) {
            if (key === localStorage.key(i)) {
              return localStorage.getItem(key);
            }
          }
        }
        return null;
      },
      /**
       * 修改或添加键值(key-value)对数据到应用数据存储中
       * 如果设置的键在应用数据存储中已经存在，更新存储的键值。
       * 存储的键和值没有容量限制，但过多的数据量会导致效率降低。
       * 建议单个键值数据不要超过10Kb。
       * @param {String} key 键名
       * @param {String} value 键值
       */
      "setItem": function (key, value) {
        localStorage.setItem(key, value);
      },
      /**
       * 通过key值删除键值对存储的数据
       * @param {String} key 键名
       */
      "delItem": function (key) {
        localStorage.removeItem(key);
      },
      /**
       * 清除应用所有的键值对存储数据
       */
      "clear": function () {
        localStorage.clear();
      }
    },
    //web sql手机端关系型数据库的最佳方案，各种手机都支持。
    "websql": {
      /**
       * 打开数据库
       * @param {String} name 数据库名
       * @param {String} size 数据库大小
       */
      "db": function (name, size) {
        var db_name = name ? name : 'db_bbf_test';
        var db_size = size ? size : 2;
        return openDatabase(db_name, '1.0', 'web_sql_db_bbf', db_size * 1024 * 1024);
      },
      /**
       * 执行sql，select/insert/update/delete
       * @param {Object} db 数据库
       * @param {String} sql sql语句
       * @param {Array} params 参数数组
       * @param {Function} cb_success(res) 成功回调函数
       * @param {Function} cb_error(err) 失败回调函数
       */
      "runsql": function (db, sql, params, cb_success, cb_error) {
        if (db && sql)
          db.transaction(function (tx, result) {
            tx.executeSql(sql, params, function (tx, results) {
              cb_success && cb_success(results);
            }, function (tx, error) {
              cb_error && cb_error(error);
            });
          });
      }
      /**
       * var db = CommonUtils.h5DbUtil.websql.db("db_test", 100);
       * CommonUtils.h5DbUtil.websql.runsql(db, "create table if not exists t1 (id unique, log)");
       * CommonUtils.h5DbUtil.websql.runsql(db, "insert into t1 (id, log) values (?, ?)", [1, "foobar"], function(res){alert("操作成功" + res)});
       * CommonUtils.h5DbUtil.websql.runsql(db, "drop table t1", null, function(res){alert("操作成功" + res)});
       */
    },
    /**
     * plus.storage也是键值对数据存储。它是把OS给原生App使用的键值对存储数据库封装一层给JS使用。
     */
    "storage": {
      /**
       * 获取应用存储区中保存的键值对的个数
       */
      "len": function () {
        return plus.storage.getLength();
      },
      /**
       * 获取键值对中指定索引值的key值
       * @param {Number} i 键值对索引
       */
      "key": function (i) {
        return plus.storage.key(i);
      },
      /**
       * 通过键(key)检索获取应用存储的值
       * @param {String} key 键名
       */
      "getItem": function (key) {
        if (key) {
          for (var i = 0, l = comm.storage.len(); i < l; i++) {
            if (key === plus.storage.key(i)) {
              return plus.storage.getItem(key);
            }
          }
        }
        return null;
      },
      /**
       * 修改或添加键值(key-value)对数据到应用数据存储中
       * 如果设置的键在应用数据存储中已经存在，更新存储的键值。
       * 存储的键和值没有容量限制，但过多的数据量会导致效率降低。
       * 建议单个键值数据不要超过10Kb。
       * @param {String} key 键名
       * @param {String} value 键值
       */
      "setItem": function (key, value) {
        plus.storage.setItem(key, value);
      },
      /**
       * 通过key值删除键值对存储的数据
       * @param {String} key 键名
       */
      "delItem": function (key) {
        plus.storage.removeItem(key);
      },
      /**
       * 清除应用所有的键值对存储数据
       */
      "clear": function () {
        plus.storage.clear();
      }
    }
  };
})(CommonUtils);
