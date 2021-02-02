/**
 * 通用地图工具类
 * @author bijingjia
 */
if (!window.MapUtils) {
    (function () {
        "use strict";
        window.MapUtils = {
            "showMessage": function (msg) {
                alert(msg);
            },
            /**
             * 生成唯一值函数
             * @returns {string}
             */
            "generateUUID": function () {
                var d = new Date().getTime();
                var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
                    var r = (d + Math.random() * 16) % 16 | 0;
                    d = Math.floor(d / 16);
                    return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
                });
                return uuid;
            },
            /**
             * 获取下一个（next）同胞节点
             * @param ele 指定节点
             * @returns {*} 下一个同胞节点
             */
            "getNextSiblingsNode": function (ele) {
                var parent = ele.parentNode;//获取元素父元素
                var children = parent.childNodes;//获取兄弟元素
                var i = 0;
                for (i; i < children.length; i++) {
                    if (children[i].nodeType === 1 && children[i] === ele) {//元素节点nodeType值为1，剔除文本节点
                        if (children[i + 1].nodeType === 1) {//防止li之间没有换行，直接选择下一个i+1
                            return children[i + 1];
                        }
                        if (children[i + 2].nodeType === 1) {//跳过文本节点，所以i+2
                            return children[i + 2];
                        }
                    }
                }
            },
            /**
             * 获取上一个（prev）同胞节点
             * @param ele 指定节点
             * @returns {*} 上一个同胞节点
             */
            "getPrevSiblingsNode": function (ele) {
                var parent = ele.parentNode;//获取元素父元素
                var children = parent.childNodes;//获取兄弟元素
                var i = 0;
                for (i; i < children.length; i++) {
                    if (children[i].nodeType === 1 && children[i] === ele) {//元素节点nodeType值为1，剔除文本节点
                        if (children[i - 1].nodeType === 1) {//防止li之间没有换行，直接选择下一个i+1
                            return children[i - 1];
                        }
                        if (children[i - 2].nodeType === 1) {//跳过文本节点，所以i+2
                            return children[i - 2];
                        }
                    }
                }
            }
        };
    })(window);
}