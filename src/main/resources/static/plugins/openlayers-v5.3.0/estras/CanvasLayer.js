/**
 * Created by Administrator on 2019/2/22.
 */
(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined'
        ? module.exports = factory() : typeof define === 'function' && define.amd
        ? define(factory) : (global.CanvasLayer = factory());
}(this, (function () {
    'use strict';

    /**
     * @author https://github.com/chengquan223
     * @Date 2017-02-27
     * */
    var CanvasLayer = function (options) {
        this.options = options || {};
        this.paneName = this.options.paneName || 'labelPane';
        this.zIndex = this.options.zIndex || 0;
        this._map = options.map;
        this._lastDrawTime = null;
        this.show();
    };

    CanvasLayer.prototype.initialize = function () {
        var canvas = this.canvas = document.createElement('canvas');
        var ctx = this.ctx = this.canvas.getContext('2d');
        canvas.style.cssText = 'position:absolute;' + 'left:0;' + 'top:0;'
            + 'z-index:' + this.zIndex + ';';
        this.adjustSize();
        this._map.getViewport().appendChild(canvas);
        var that = this;
        this._map.on("pointerdrag", function (evt) {
            that.canvas.style.cssText = 'position:absolute;' + 'left:' + evt.pixel[0]
                + 'px;' + 'top:' + evt.pixel[1] + 'px;' + 'z-index:' + that.zIndex + ';';
        });
        this._map.on("moveend", function () {
            that.canvas.style.cssText = 'position:absolute;' + 'left:0;' + 'top:0;'
                + 'z-index:' + that.zIndex + ';';
        });
        return this.canvas;
    };

    CanvasLayer.prototype.adjustSize = function () {
        var size = this._map.getSize();
        var canvas = this.canvas;
        canvas.width = size[0];
        canvas.height = size[1];
        canvas.style.width = canvas.width + 'px';
        canvas.style.height = canvas.height + 'px';
    };

    CanvasLayer.prototype.getContainer = function () {
        return this.canvas;
    };

    CanvasLayer.prototype.show = function () {
        this.initialize();
        this.canvas.style.display = 'block';
    };

    CanvasLayer.prototype.hide = function () {
        this.canvas.style.display = 'none';
    };

    CanvasLayer.prototype.setZIndex = function (zIndex) {
        this.canvas.style.zIndex = zIndex;
    };

    CanvasLayer.prototype.getZIndex = function () {
        return this.zIndex;
    };

    return CanvasLayer;

})));
