/**
 * Openlayers工具类
 * @author bijingjia
 */
;
(function (mapu, ol, $) {
    'use strict';

    var mapObject;
    var a;
    var mapParam = {
        center: [111, 36],
        zoom: 4,
        projection: 'EPSG:4326',
        extent: [73.45, 18.16, 134.976, 53.532]
    };

    var baseLayerIds = [];
    var arcgisServerActiveLayers = [];
    var geoServerActiveLayers = [];
    var markerLayers = [];
    var mapZoom;

    var beltMarkerLayer;
    var beltMarkersource = new ol.source.Vector();
    var beltFeatures;

    var selectSource = new ol.source.Vector();
    var selectLayer = new ol.layer.Vector({
        name: "选择器图层",
        id: "selectLayer",
        source: selectSource,
        style: new ol.style.Style({
            fill: new ol.style.Fill({               //填充样式
                color: 'rgba(255, 255, 255, 0.2)'
            }),
            stroke: new ol.style.Stroke({           //线样式
                color: '#ffcc33',
                width: 2
            }),
            image: new ol.style.Circle({            //点样式
                radius: 7,
                fill: new ol.style.Fill({
                    color: '#ffcc33'
                })
            })
        })
    });

    //动态标注点
    var markerSize = 30;
    var markerCanvas = document.createElement('canvas');
    markerCanvas.width = markerSize;
    markerCanvas.height = markerSize;
    var markerContext = markerCanvas.getContext("2d");
    var markerRadius = markerSize / 10;
    //创建构造函数Circle
    function CircleMarker(x, y, radius) {
        this.xx = x;
        this.yy = y;
        this.radius = radius;
    }

    CircleMarker.prototype.radiu = function () {
        markerRadius += 0.5; //每一帧半径增加0.5
        if (this.radius >= markerSize / 2) {
            markerRadius = markerSize / 10;
        }
    };
    // 绘制圆形的方法
    CircleMarker.prototype.paint = function () {
        markerContext.beginPath();
        markerContext.arc(this.xx, this.yy, markerSize / 5, 0, Math.PI * 2);
        markerContext.fillStyle = "rgba(250,50,50,1)";//填充颜色,默认是黑色
        markerContext.fill();//画实心圆
        markerContext.closePath();
        markerContext.lineWidth = 2; //线条宽度
        markerContext.strokeStyle = 'rgba(250,50,50,1)'; //颜色
        markerContext.stroke();
        this.paintkong();
        this.paintkong(0);
    };
    CircleMarker.prototype.paintkong = function (num) {
        var rkong = this.radius + num;
        if (rkong < markerSize / 2) {
            markerContext.beginPath();
            markerContext.arc(this.xx, this.yy, rkong, 0, Math.PI * 2);
            markerContext.closePath();
            markerContext.lineWidth = 1; //线条宽度
            markerContext.strokeStyle = 'rgba(250,50,50,1)'; //颜色
            markerContext.stroke();
        }

    };
    function createCircles() {
        var newMarker = new CircleMarker(markerSize / 2, markerSize / 2,
            markerRadius);//调用构造函数
        newMarker.paint();
        newMarker.radiu();
    }

    // 创建临时canvas
    var markerBackCanvas = document.createElement('canvas'),
        markerBackCtx = markerBackCanvas.getContext('2d');
    markerBackCanvas.width = markerSize;
    markerBackCanvas.height = markerSize;
    //设置主canvas的绘制透明度
    markerContext.globalAlpha = 0.7;
    //显示即将绘制的图像，忽略临时canvas中已存在的图像
    markerBackCtx.globalCompositeOperation = 'copy';

    var measureSource = new ol.source.Vector();
    var measureLayer = new ol.layer.Vector({
        name: "测量图层",
        id: "measureLayer",
        source: measureSource,
        style: new ol.style.Style({
            fill: new ol.style.Fill({               //填充样式
                color: 'rgba(255, 255, 255, 0.2)'
            }),
            stroke: new ol.style.Stroke({           //线样式
                color: '#ffcc33',
                width: 2
            }),
            image: new ol.style.Circle({
                radius: 5,
                stroke: new ol.style.Stroke({
                    color: 'rgba(0, 0, 0, 0.7)'
                }),
                fill: new ol.style.Fill({
                    color: 'rgba(255, 255, 255, 0.2)'
                })
            })
        })
    });

    var markerSource = new ol.source.Vector();
    var markerLayer = new ol.layer.Vector({
        name: "标注图层",
        id: "markerLayer",
        source: markerSource,
        style: new ol.style.Style({
            image: new ol.style.Icon({            //点样式
                img: markerCanvas,
                imgSize: [markerSize, markerSize],
                rotation: 1.2
            })
        })
    });

    //i查询工具
    var identifyEvent;
    var identifyLayerIds = [];
    var identifyResults = [];
    var infoWindow;
    var infoContentFun;
    var selectSingleClick = new ol.interaction.Select();

    //测量工具
    var sketch;
    var helpTooltipElement;
    var helpTooltip;
    var measureTooltipElement;
    var measureTooltip;
    var continuePolygonMsg = '单击继续绘制多边形';
    var continueLineMsg = '单击继续绘制线';
    var measureType;

    //拉框放大缩小
    var dragZoomIn;
    var dragZoomOut;

    //图形绘制
    var drawSource = new ol.source.Vector();
    var drawLayer = new ol.layer.Vector({
        name: "测量图层",
        id: "drawLayer",
        source: drawSource,
        style: new ol.style.Style({
            fill: new ol.style.Fill({               //填充样式
                color: 'rgba(255, 255, 255, 0.2)'
            }),
            stroke: new ol.style.Stroke({           //线样式
                color: '#ffcc33',
                width: 2
            }),
            image: new ol.style.Circle({
                radius: 5,
                stroke: new ol.style.Stroke({
                    color: 'rgba(0, 0, 0, 0.7)'
                }),
                fill: new ol.style.Fill({
                    color: 'rgba(255, 255, 255, 0.2)'
                })
            })
        })
    });

    var pointerMoveHandler = function (evt) {
        if (evt.dragging) {
            return;
        }
        /** @type {string} */
        var helpMsg = '单击开始绘制';

        if (sketch) {
            var geom = (sketch.getGeometry());
            if (geom instanceof ol.geom.Polygon) {
                helpMsg = continuePolygonMsg;
            } else if (geom instanceof ol.geom.LineString) {
                helpMsg = continueLineMsg;
            }
        }
        if (measureType === "Point") {
            helpTooltipElement.innerHTML = "点击添加标注，当前经度：" + evt.coordinate[0].toFixed(
                3) + ";当前纬度：" + evt.coordinate[1].toFixed(3);
        } else {
            helpTooltipElement.innerHTML = helpMsg;
        }

        helpTooltip.setPosition(evt.coordinate);

        helpTooltipElement.classList.remove('hidden');
    };

    var formatLength = function (line) {
        var length = ol.sphere.getLength(line, {
            projection: mapParam.projection
        });
        var output;
        if (length > 100) {
            output = (Math.round(length / 1000 * 100) / 100) +
                ' ' + 'km';
        } else {
            output = (Math.round(length * 100) / 100) +
                ' ' + 'm';
        }
        return output;
    };

    var formatArea = function (polygon) {
        var area = ol.sphere.getArea(polygon, {
            projection: mapParam.projection
        });
        var output;
        if (area > 10000) {
            output = (Math.round(area / 1000000 * 100) / 100) +
                ' ' + 'km<sup>2</sup>';
        } else {
            output = (Math.round(area * 100) / 100) +
                ' ' + 'm<sup>2</sup>';
        }
        return output;
    };

    function createHelpTooltip() {
        if (helpTooltipElement) {
            helpTooltipElement.parentNode.removeChild(helpTooltipElement);
        }
        helpTooltipElement = document.createElement('div');
        helpTooltipElement.className = 'tooltip hidden';
        helpTooltip = new ol.Overlay({
            element: helpTooltipElement,
            offset: [15, 0],
            positioning: 'center-left'
        });
        mapObject.addOverlay(helpTooltip);
    }

    function createMeasureTooltip() {
        if (measureTooltipElement) {
            measureTooltipElement.parentNode.removeChild(measureTooltipElement);
        }
        measureTooltipElement = document.createElement('div');
        measureTooltipElement.className = 'tooltip tooltip-measure';
        measureTooltip = new ol.Overlay({
            element: measureTooltipElement,
            offset: [0, -15],
            positioning: 'bottom-center'
        });
        mapObject.addOverlay(measureTooltip);
    }

    function createMiddleTooltip(output, coordinate) {
        var middleTooltipElement = document.createElement('div');
        middleTooltipElement.className = 'tooltip tooltip-middle';
        middleTooltipElement.innerHTML = output;
        var middleTooltip = new ol.Overlay({
            element: middleTooltipElement,
            offset: [0, 0],
            positioning: 'top-center'
        });
        mapObject.addOverlay(middleTooltip);
        middleTooltip.setPosition(coordinate);
    }

    //创建天地图图层
    function createTDTLayerXYZ(type, name, id, visibility) {
        return new ol.layer.Tile({
            name: name,
            source: new ol.source.XYZ({
                crossOrigin: 'anonymous',
                url: "http://t" + Math.round(Math.random() * 7)
                    + ".tianditu.com/DataServer?T=" + type
                    + "&x={x}&y={y}&l={z}&tk=6f455fa7d3033cc6a2fbc8de0feea343"
            }),
            id: id,
            visible: visibility
        });
    }

    //创建高德图层
    function createGDLayerXYZ(type, id, visibility) {
        switch (type) {
            case "GDV":
                return new ol.layer.Tile({
                    source: new ol.source.XYZ({
                        crossOrigin: 'anonymous',
                        url: 'http://wprd0{1-4}.is.autonavi.com/appmaptile?lang=zh_cn&size=1&style=7&x={x}&y={y}&z={z}'
                    }),
                    name: "高德矢量图",
                    id: id,
                    visible: visibility
                });
                break;
            case "GDI":
                return new ol.layer.Tile({
                    source: new ol.source.XYZ({
                        crossOrigin: 'anonymous',
                        url: 'http://wprd0{1-4}.is.autonavi.com/appmaptile?style=6&x={x}&y={y}&z={z}'
                    }),
                    name: "高德影像图",
                    id: id,
                    visible: visibility
                });
                break;
            case "GDL":
                return new ol.layer.Tile({
                    source: new ol.source.XYZ({
                        crossOrigin: 'anonymous',
                        url: 'http://wprd0{1-4}.is.autonavi.com/appmaptile?style=8&x={x}&y={y}&z={z}'
                    }),
                    name: "高德影像图标注",
                    id: id,
                    visible: visibility
                });
                break;
            default:
                return new ol.layer.Tile({
                    source: new ol.source.XYZ({
                        crossOrigin: 'anonymous',
                        url: 'http://wprd0{1-4}.is.autonavi.com/appmaptile?lang=zh_cn&size=1&style=7&x={x}&y={y}&z={z}'
                    }),
                    name: "高德矢量图",
                    id: id,
                    visible: visibility
                });
                break;
        }
    }

    //创建谷歌图层
    function createGoogleLayerXYZ(type, id, visibility) {
        switch (type) {
            case "GGV":
                return new ol.layer.Tile({
                    source: new ol.source.XYZ({
                        crossOrigin: 'anonymous',
                        url: 'http://www.google.cn/maps/vt/pb=!1m4!1m3!1i{z}!2i{x}!3i{y}!2m3!1e0!2sm!3i380072576!3m8!2szh-CN!3scn!5e1105!12m4!1e68!2m2!1sset!2sRoadmap!4e0!5m1!1e0'
                    }),
                    name: "谷歌矢量图",
                    id: id,
                    visible: visibility
                });
                break;
            case "GGI":
                return new ol.layer.Tile({
                    source: new ol.source.XYZ({
                        crossOrigin: 'anonymous',
                        url: 'http://www.google.cn/maps/vt?lyrs=s@189&gl=cn&x={x}&y={y}&z={z}'
                    }),
                    name: "谷歌影像图",
                    id: id,
                    visible: visibility
                });
                break;
            case "GGL":
                return new ol.layer.Tile({
                    source: new ol.source.XYZ({
                        crossOrigin: 'anonymous',
                        url: 'http://www.google.cn/maps/vt?lyrs=h@189&gl=cn&x={x}&y={y}&z={z}'
                    }),
                    name: "谷歌影像图标注",
                    id: id,
                    visible: visibility
                });
                break;
            case "GGT":
                return new ol.layer.Tile({
                    source: new ol.source.XYZ({
                        crossOrigin: 'anonymous',
                        url: 'http://www.google.cn/maps/vt?lyrs=t@189&gl=cn&x={x}&y={y}&z={z}'
                    }),
                    name: "谷歌地形图",
                    id: id,
                    visible: visibility
                });
                break;
        }
    }

    //创建百度图层
    function createBDLayer(type, name, id, visibility) {
        // 自定义分辨率和瓦片坐标系
        var resolutions = [];
        var maxZoom = 18;
        // 计算百度使用的分辨率
        for (var i = 0; i <= maxZoom + 1; i++) {
            resolutions[i] = Math.pow(2, maxZoom - i);
        }
        var tilegrid = new ol.tilegrid.TileGrid({
            origin: [0, 28000], // 设置原点坐标,修正百度偏移
            resolutions: resolutions // 设置分辨率
        });
        // 创建百度行政区划
        var baiduSource = new ol.source.TileImage({
            crossOrigin: 'anonymous',
            projection: mapParam.projection,
            tileGrid: tilegrid,
            tileUrlFunction: function (tileCoord, pixelRatio, proj) {
                var z = tileCoord[0];
                var x = tileCoord[1];
                var y = tileCoord[2];
                // 百度瓦片服务url将负数使用M前缀来标识
                if (x < 0) {
                    x = 'M' + (-x);
                }
                if (y < 0) {
                    y = 'M' + (-y);
                }
                var url = "";
                switch (type) {
                    case "BDV":
                        url = 'http://online' + parseInt(Math.random() * 10)
                            + '.map.bdimg.com/onlinelabel/?qt=tile&x=' +
                            x + '&y=' + y + '&z=' + z
                            + '&styles=pl&udt=20170620&scaler=1&p=1';
                        break;
                    case "BDI":
                        url = 'http://shangetu' + parseInt(Math.random() * 10)
                            + '.map.bdimg.com/it/u=x=' + x +
                            ';y=' + y + ';z=' + z + ';v=009;type=sate&fm=46&udt=20170606';
                        break;
                    case "BDL":
                        url = 'http://online' + parseInt(Math.random() * 10)
                            + '.map.bdimg.com/onlinelabel/?qt=tile&x=' +
                            x + '&y=' + y + '&z=' + z
                            + '&styles=sl&udt=20170620&scaler=1&p=1';
                        break;
                }
                return url;
            }
        });
        return new ol.layer.Tile({
            source: baiduSource,
            name: name,
            id: id,
            visible: visibility
        });

    }

    //创建ArcGISServer图层
    function createArcGISServerLayer(name, id, url, visibility) {
        return new ol.layer.Tile({
            source: new ol.source.TileArcGISRest({
                crossOrigin: 'anonymous',
                projection: mapParam.projection,
                url: url
            }),
            name: name,
            id: id,
            visible: visibility
        });
    }

    function createGeoServerLayer1(name, id, url, serverName, visibility) {
        // return new ol.layer.Tile({
        //     source: new ol.source.TileImage({
        //         url: 'http://192.168.1.31:8080/geoserver/wangguoqing/wms?service=WMS&version=1.1.0&request=GetMap&layers=wangguoqing:mosaicWuxue&styles=&bbox=115.35919189453101,29.815620766601747,115.83984800788537,30.231781089557405&width=768&height=664&srs=EPSG:4326&format=application/openlayers'
        //     }),
        //     name: name,
        //     id: id,
        //     visible: visibility
        // });
        return new ol.layer.Image({
            source: new ol.source.ImageWMS({
                url: url,
                params: {'LAYERS': 'wangguoqing:mosaicWuxue', 'TILED': true},
                ratio: 1,
                serverType: 'geoserver'
            }),
            name: name,
            id: id,
            visible: visibility
        })
    }
    //创建GeoServer图层
    function createGeoServerLayer(name, id, url, serverName, visibility) {
        return new ol.layer.Tile({
            source: new ol.source.TileWMS({
                crossOrigin: 'anonymous',
                extent: mapParam.extent,
                params: {
                    'LAYERS': serverName,
                    'VERSION': '1.1.0',
                    'BBOX': mapParam.extent,
                    'CRS': mapParam.projection,
                    'WIDTH': 768,
                    'HEIGHT': 416
                },
                projection: mapParam.projection,
                url: url
            }),
            name: name,
            id: id,
            visible: visibility
        });
    }

    //i查询实现函数
    function doIdentify(evt) {
        if (infoWindow) {
            infoWindow.setPosition(undefined);
        }
        if (evt.selected) {
            if (evt.selected.length > 0) {
                if (infoWindow) {
                    var layer = selectSingleClick.getLayer(evt.selected[0]);
                    var infoPosition;
                    if (evt.selected[0].getGeometry().flatCoordinates.length
                        === 2) {
                        infoPosition = evt.selected[0].getGeometry().flatCoordinates;
                    } else {
                        infoPosition = evt.coordinate;

                    }
                    if (infoContentFun) {
                        infoContentFun(layer.get("id"), layer.get("name"),
                            evt.selected[0], infoPosition);
                    } else {
                        var popTitle = document.getElementById(
                            mapParam.target + '-popup-title');
                        popTitle.innerHTML = layer.get("name");
                        var popContent = document.getElementById(
                            mapParam.target + '-popup-content');
                        popContent.innerHTML = createInfoWindowContent(
                            evt.selected[0]);
                        infoWindow.setPosition(infoPosition);
                    }

                }
            }
        } else {
            identifyLayerIds = [];
            identifyResults = [];
            selectSource.clear();
            mapObject.removeLayer(selectLayer);
            mapObject.addLayer(selectLayer);
            identifyArcGISServerLayer(evt, 0.001);
            identifyGeoServerLayer(evt);

            var identifyWaiting = setInterval(function () {
                var idCount = identifyLayerIds.length;
                var resultCount = identifyResults.length;
                if (idCount === resultCount) {
                    clearInterval(identifyWaiting);
                    var results = [];
                    for (var i = 0; i < identifyResults.length; i++) {
                        if (identifyResults[i].features.length > 0) {
                            results.push(identifyResults[i]);
                        }
                    }
                    if (results.length > 0) {
                        var layerCollection = mapObject.getLayers();
                        var layerIds = [];
                        layerCollection.forEach(function (layer) {
                            layerIds.push(layer.get("id"));
                        });
                        results.sort(function (a, b) {
                            // order是规则  results是需要排序的数组
                            var order = layerIds;
                            return order.indexOf(b.id) - order.indexOf(a.id);
                        });
                        selectSource.addFeature(results[0].features[0]);
                        if (infoWindow) {
                            if (infoContentFun) {
                                infoContentFun(results[0].id, results[0].name,
                                    results[0].features[0]);
                            } else {
                                var popTitle = document.getElementById(
                                    mapParam.target + '-popup-title');
                                popTitle.innerHTML = results[0].name;
                                var popContent = document.getElementById(
                                    mapParam.target + '-popup-content');
                                popContent.innerHTML = createInfoWindowContent(
                                    results[0].features[0]);
                            }
                            if (results[0].features[0].getGeometry().flatCoordinates.length
                                === 2) {
                                infoWindow.setPosition(
                                    results[0].features[0].getGeometry().flatCoordinates);
                            } else {
                                infoWindow.setPosition(evt.coordinate);
                            }
                        }
                    }
                }
            }, 30);
        }
    }

    //ArcGIS Server服务i查询实现方法
    function identifyArcGISServerLayer(evt, tolerance) {
        //url = 'http://192.168.1.31:6080/arcgis/rest/services/fangjia/MapServer/0/query';
        //tolerance = 0.001;
        for (var i = 0; i < arcgisServerActiveLayers.length; i++) {
            if (checkLayerVisible(arcgisServerActiveLayers[i].id)) {
                identifyLayerIds.push(arcgisServerActiveLayers[i].id);
                var url = arcgisServerActiveLayers[i].mapServerUrl + "/"
                    + arcgisServerActiveLayers[i].mapServerLayerId + "/query";
                var strEnvelope = '{"xmin" : ' + (evt.coordinate[0]
                    - tolerance).toString()
                    + ', "ymin" : ' + (evt.coordinate[1] - tolerance).toString()
                    + ', "xmax" : ' + (evt.coordinate[0] + tolerance).toString()
                    + ', "ymax" : ' + (evt.coordinate[1] + tolerance).toString()
                    + ', "spatialReference" : {"wkid" : ' + mapParam.projection.substr(
                        4)
                    + '}}';
                var data = {};
                data.where = "";
                data.text = "";
                data.objectIds = "";
                data.time = "";
                data.geometry = strEnvelope;
                data.geometryType = "esriGeometryEnvelope";
                data.inSR = "";
                data.spatialRel = "esriSpatialRelIntersects";
                data.relationParam = "";
                data.outFields = "*";
                data.returnGeometry = true;
                data.maxAllowableOffset = "";
                data.geometryPrecision = "";
                data.outSR = "";
                data.returnIdsOnly = false;
                data.returnCountOnly = false;
                data.orderByFields = "";
                data.groupByFieldsForStatistics = "";
                data.outStatistics = "";
                data.returnZ = "";
                data.returnM = "";
                data.gdbVersion = "";
                data.returnDistinctValues = "";
                data.f = "pjson";

                arcAjax(arcgisServerActiveLayers[i].id,
                    arcgisServerActiveLayers[i].name, url, data)

            }
        }

    }

    function arcAjax(id, name, url, data) {
        $.ajax({
            type: 'post',
            url: url,
            async: false,
            dataType: 'jsonp',
            contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
            data: data,
            success: function (resData) {
                var geojsonFormat = new ol.format.EsriJSON();
                var features = geojsonFormat.readFeatures(JSON.stringify(resData));
                identifyResults.push({
                    id: id,
                    name: name,
                    features: features
                });
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log("连接数据的时候出现了：" + textStatus);
            }

        });
    }

    //GeoServer服务i查询实现方法
    function identifyGeoServerLayer(evt) {
        var view = mapObject.getView();
        var viewResolution = view.getResolution();
        for (var i = 0; i < geoServerActiveLayers.length; i++) {
            if (checkLayerVisible(geoServerActiveLayers[i].id)) {
                identifyLayerIds.push(geoServerActiveLayers[i].id);
                var source = new ol.source.TileWMS({
                    extent: [73.45, 18.16, 134.976, 53.532],
                    params: {
                        'LAYERS': geoServerActiveLayers[i].serverLayerName,
                        'VERSION': '1.1.0',
                        'BBOX': [75.0, 18.0, 134.425796508789, 50.2599487304688],
                        'CRS': mapParam.projection,
                        'WIDTH': 768,
                        'HEIGHT': 416
                    },
                    projection: mapParam.projection,
                    url: geoServerActiveLayers[i].geoServerUrl
                });
                var url = source.getGetFeatureInfoUrl(
                    evt.coordinate, viewResolution, view.getProjection(),
                    {'INFO_FORMAT': 'application/json', 'FEATURE_COUNT': 50});
                geoAjax(geoServerActiveLayers[i].id, geoServerActiveLayers[i].name,
                    url);
            }
        }
    }

    function geoAjax(id, name, url) {
        if (url) {
            $.ajax({
                type: 'GET',
                url: url,
                dataType: 'json',
                success: function (data) {
                    var geojsonFormat = new ol.format.GeoJSON(
                        {defaultDataProjection: mapParam.projection});
                    var features = geojsonFormat.readFeatures(data);
                    identifyResults.push({
                        id: id,
                        name: name,
                        features: features
                    });
                },
                error: function (err) {
                    console.log(err);
                }
            });
        }
    }

    //i查询弹框
    function checkLayerVisible(id) {
        var layerCollection = mapObject.getLayers();
        var result = false;
        layerCollection.forEach(function (layer) {
            if (layer.get("id") === id) {
                result = layer.get("visible");

            }
        });
        return result;
    }

    function initInfoWindow() {
        var pop_container = document.createElement("div");
        pop_container.className = "ol-popup";
        pop_container.innerHTML = '<div id="' + mapParam.target
            + '-popup-title"  style="font:bold 20px sans-serif;align:left;position: relative;top: 5px;left: 12px;color: rgb(76,181,192);">'
            + '洪阳湖港区---春锦'
            + '</div>'
            + '<a class="ol-popup-closer" onclick="MapUtils.OpenlayersUtil.closeInfoWindow();" style="color:#8e908c;border: 1px;padding: 2px;border-radius: 2px"></a>'
            + '<div id="' + mapParam.target
            + '-popup-content" style="padding: 10px;max-height: 200px;overflow: auto;color: white;line-height: 2"></div>';
        document.body.appendChild(pop_container);
        infoWindow = new ol.Overlay({
            element: pop_container,
            autoPan: true,
            autoPanAnimation: {
                duration: 250
            }
        });
        mapObject.addOverlay(infoWindow);
    }

    function createInfoWindowContent(feature) {
        var html = '<table style = "width:250px">';
        var keys = feature.getKeys();
        for (var i = 0; i < keys.length; i++) {
            if (keys[i] == '皮带定点监控' || keys[i] == '皮带周边监控' || keys[i] == '泊位周边监控') {
                html += '<tr><td>' + keys[i] + ':</td><td><a style="border-bottom: 1px solid rgb(76,181,192);color:rgb(76,181,192)"; href="http://www.baidu.com" target="_blank">' + feature.get(keys[i])
                    + '<a></td>'
            }
            else if (keys[i] == 'ELON' || keys[i] == 'ELAT' || keys[i] == 'geometry') {
            }
            else {
                html += '<tr><td>' + keys[i] + ':</td><td>' + feature.get(keys[i])
                    + '</td>'
            }
        }
        html += '</table>';
        return html;
    }

    function getDialog() {
        infoWindow.setPosition(undefined);
    }

    //图层控制
    function loadLayersControl(target, layer) {
        var ulId = target + "-ul";
        var treeContent;
        if (document.getElementById(ulId)) {
            treeContent = document.getElementById(ulId); //图层目录容器
        } else {
            var ulContent = document.getElementById(target);
            var ulDom = document.createElement("ul");
            ulDom.id = ulId;
            ulContent.appendChild(ulDom);
            treeContent = ulDom;
        }
        //新增li元素，用来承载图层项
        var elementLi = document.createElement('li');
        treeContent.appendChild(elementLi); // 添加子节点
        elementLi.style.listStyle = "none";
        //创建复选框元素
        var elementInput = document.createElement('input');
        elementInput.type = "checkbox";
        elementInput.name = "layers";
        elementLi.appendChild(elementInput);
        //创建label元素
        var elementLable = document.createElement('label');
        elementLable.className = "layer";
        //设置图层名称
        setInnerText(elementLable, layer.get('name'));
        elementLi.appendChild(elementLable);

        //设置图层默认显示状态
        if (layer.getVisible()) {
            elementInput.checked = true;
        }
        addChangeEvent(elementInput, layer); //为checkbox添加变更事件
    }

    function addChangeEvent(element, layer) {
        element.onclick = function () {
            var legend = document.getElementById("legend_" + layer.get("id"));
            if (element.checked) {
                layer.setVisible(true); //显示图层
                legend.style.display = "block";
            } else {
                layer.setVisible(false); //不显示图层
                legend.style.display = "none";
            }
        };
    }

    function setInnerText(element, text) {
        if (typeof element.textContent === "string") {
            element.textContent = text;
        } else {
            element.innerText = text;
        }
    }

    //图例
    function getArcGISServerLegend(serverUrl, layerId, legendName, target, id,
        visible) {
        $.ajax({
            type: 'GET',
            url: serverUrl + "/legend",
            data: {
                f: "pjson"
            },
            dataType: 'json',
            success: function (data) {
                var legendData = data.layers[layerId];
                var legendContainer = document.createElement("div");
                legendContainer.id = "legend_" + id;
                var html = '<table><tr style="font-weight: bold"><td colspan="2">'
                    + legendName + '</td></tr>';
                for (var i = 0; i < legendData.legend.length; i++) {
                    var imgUrl = serverUrl + "/" + layerId + "/images/"
                        + legendData.legend[i].url;
                    html += '<tr><td><img src="' + imgUrl + '"/></td><td>'
                        + legendData.legend[i].label
                        + '</td></tr>'
                }
                html += '</table>';
                legendContainer.innerHTML = html;
                if (!visible) {
                    legendContainer.style.display = "none";
                }
                var legendContent = document.getElementById(target);
                legendContent.appendChild(legendContainer);
            },
            error: function (err) {
                console.log(err);
            }
        });
    }

    function getGeoServerLegend(serverUrl, layerName, legendName, target, id,
        visible) {
        var workspace = layerName.substr(0, layerName.indexOf(":"));
        var layername = layerName.substr(layerName.indexOf(":") + 1);
        $.ajax({
            type: 'GET',
            url: "/geoServer/getLegend.json",
            data: {
                workSpace: workspace,
                layerName: layername
            },
            dataType: 'json',
            success: function (data) {
                var legendContainer = document.createElement("div");
                legendContainer.id = "legend_" + id;
                var html = '<table><tr style="font-weight: bold"><td colspan="2">'
                    + legendName + '</td></thead>';
                if (data.rules) {
                    for (var i = 0; i < data.rules.length; i++) {
                        var imgurl = serverUrl
                            + "geoserver/wms?REQUEST=GetLegendGraphic&VERSION=1.0.0&FORMAT=image/png&WIDTH=20&HEIGHT=20&LAYER="
                            + layerName + "&RULE="
                            + data.rules[i].rule;
                        html += '<tr><td><img src="' + imgurl + '"/></td><td>'
                            + data.rules[i].title
                            + '</td></tr>'
                    }
                } else {
                    var imgurl = serverUrl
                        + "geoserver/wms?REQUEST=GetLegendGraphic&VERSION=1.0.0&FORMAT=image/png&WIDTH=20&HEIGHT=20&LAYER="
                        + layerName;
                    html += '<tr><td><img src="' + imgurl + '"/></td></tr>'
                }

                html += '</table>';
                legendContainer.innerHTML = html;
                if (!visible) {
                    legendContainer.style.display = "none";
                }
                var legendContent = document.getElementById(target);
                legendContent.appendChild(legendContainer);
            },
            error: function (err) {
                console.log(err);
            }
        });
    }

    function getMarkerLegend(icon, legendName, target, id, visible) {
        var legendContainer = document.createElement("div");
        legendContainer.id = "legend_" + id;
        var html = '<table>' + '<tr style="font-weight: bold">' + '<td><img style="height: 20px;width: 20px;" src="' + icon + '"/></td>' + '<td colspan="2">' + legendName + '</td></tr></table>';
        legendContainer.innerHTML = html;
        if (!visible) {
            legendContainer.style.display = "none";
        }
        var legendContent = document.getElementById(target);
        legendContent.appendChild(legendContainer);
    }

    //创建文字标注
    var createTextStyle = function (feature, label) {
        return new ol.style.Text({
            text: feature.get(label),
            fill: new ol.style.Fill({color: 'rgba(0, 0, 0, 0.8)'}),
            stroke: new ol.style.Stroke({color: 'white', width: 2}),
            offsetX: 0,
            offsetY: -20
        });
    };

    mapu.OpenlayersUtil = {


        /**
         * 初始化地图
         * @param map 地图参数
         * var map = {
        target: "map",（必填）
        center: [120.5, 36],
        zoom: 10,
        projection: 'EPSG:4326',
        extent: [73.45, 18.16, 134.976, 53.532]
      };
         *@param infoContent i查询结果处理函数
         * @returns {*}
         */
        "initMap": function (map, infoContent) {
            mapParam.target = map.target;
            if (map.projection) {
                if (mapParam.projection !== map.projection) {
                    if (map.center) {
                        mapParam.center = ol.proj.transform(map.center, mapParam.projection,
                            map.projection);
                    } else {
                        mapParam.center = ol.proj.transform(mapParam.center,
                            mapParam.projection,
                            map.projection);
                    }
                    if (map.extent) {
                        mapParam.extent = ol.proj.transform(map.extent, mapParam.projection,
                            map.projection);
                    } else {
                        mapParam.extent = ol.proj.transform(mapParam.extent,
                            mapParam.projection,
                            map.projection);
                    }
                    mapParam.projection = map.projection;
                } else {
                    if (map.center) {
                        mapParam.center = map.center;
                    }
                    if (map.extent) {
                        mapParam.extent = map.extent;
                    }
                }
            } else {
                if (map.center) {
                    mapParam.center = map.center;
                }
                if (map.extent) {
                    mapParam.extent = map.extent;
                }
            }
            if (map.zoom) {
                mapParam.zoom = map.zoom;
            }

            mapObject = new ol.Map({
                layers: [
                    selectLayer,
                    measureLayer
                ],
                view: new ol.View({
                    center: mapParam.center,
                    zoom: mapParam.zoom,
                    projection: mapParam.projection,
                    maxZoom: 28,
                    minZoom: 3
                }),
                target: map.target
            });

            initInfoWindow();
            infoContentFun = infoContent;
            identifyEvent = mapObject.on('singleclick', doIdentify);

            mapObject.addInteraction(selectSingleClick);
            selectSingleClick.on('select', doIdentify);


            dragZoomIn = new ol.interaction.DragZoom({
                condition: ol.events.condition.always,
                out: false
            });
            mapObject.addInteraction(dragZoomIn);
            dragZoomIn.setActive(false);

            dragZoomOut = new ol.interaction.DragZoom({
                condition: ol.events.condition.always,
                out: true
            });
            mapObject.addInteraction(dragZoomOut);
            dragZoomOut.setActive(false);
            return mapObject;
        },
        /**
         * 初始化底图及底图控件
         * @param layers 底图配置（必填）
         * @param domId 放置dom
         * @param orient 悬浮位置
         */
        "initBaseLayer": function (layers, domId, orient) {
            var dom;
            var innerHtml;
            if (!orient) {
                orient = 'right';
            }
            if (domId) {
                dom = window.document.getElementById(domId);
                innerHtml = '<ul style="float: ' + orient + ';width: '
                    + (parseInt(dom.style.width.substr(0, dom.style.width.length - 2))
                        + 4)
                    * layers.length + 'px;height: ' + (parseInt(
                        dom.style.height.substr(0, dom.style.height.length - 2))
                        + 4) + 'px; padding:0;">';
            }
            var display = 'block';
            for (var i = 0; i < layers.length; i++) {
                var layerId = mapu.generateUUID();
                switch (layers[i].type) {
                    case "TDTV":
                        mapObject.addLayer(createTDTLayerXYZ("vec_w", "天地图矢量图", layerId,
                            layers[i].visibility));
                        mapObject.addLayer(createTDTLayerXYZ("cva_w", "天地图矢量标注", layerId,
                            layers[i].visibility));
                        break;
                    case "TDTI":
                        mapObject.addLayer(createTDTLayerXYZ("img_w", "天地图影像图", layerId,
                            layers[i].visibility));
                        mapObject.addLayer(createTDTLayerXYZ("cva_w", "天地图影像图标注", layerId,
                            layers[i].visibility));
                        break;
                    case "GDV":
                        mapObject.addLayer(
                            createGDLayerXYZ("GDV", layerId, layers[i].visibility));
                        break;
                    case "GDI":
                        mapObject.addLayer(
                            createGDLayerXYZ("GDI", layerId, layers[i].visibility));
                        mapObject.addLayer(
                            createGDLayerXYZ("GDL", layerId, layers[i].visibility));
                        break;
                    case "GGV":
                        mapObject.addLayer(
                            createGoogleLayerXYZ("GGV", layerId, layers[i].visibility));
                        break;
                    case "GGI":
                        mapObject.addLayer(
                            createGoogleLayerXYZ("GGI", layerId, layers[i].visibility));
                        mapObject.addLayer(
                            createGoogleLayerXYZ("GGL", layerId, layers[i].visibility));
                        break;
                    case "GGT":
                        mapObject.addLayer(
                            createGoogleLayerXYZ("GGT", layerId, layers[i].visibility));
                        break;
                    case "BDV":
                        mapObject.addLayer(
                            createBDLayer("BDV", "百度矢量图", layerId, layers[i].visibility));
                        break;
                    case "BDI":
                        mapObject.addLayer(
                            createBDLayer("BDI", "百度影像图", layerId, layers[i].visibility));
                        mapObject.addLayer(
                            createBDLayer("BDL", "百度影像图标注", layerId, layers[i].visibility));
                        break;
                    case "ArcGISServer":
                        mapObject.addLayer(
                            createArcGISServerLayer(layers[i].layerName, layerId,
                                layers[i].url, layers[i].visibility));
                        break;
                    case "GeoServer":
                        mapObject.addLayer(
                            createGeoServerLayer1(layers[i].layerName, layerId,
                                layers[i].url, layers[i].serverLayerName,
                                layers[i].visibility));
                        break;
                    default:
                        break;
                }
                if (i > 0) display = 'none';
                if (domId) {
                    innerHtml += '<li style="display:' + display + '; cursor:pointer;padding:0;margin:0;list-style: none;float: '
                        + orient
                        + ';" onclick="MapUtils.OpenlayersUtil.changeBaseLayerVisibility(\''
                        + layerId + '\')"><img src="'
                        + layers[i].icon + '" style="height: ' + dom.style.height
                        + ';width:'
                        + dom.style.width
                        + ';border:solid 2px #ffffff;" '
                        + 'onmouseover="this.style.border=\'solid 2px #58ACFA\'; MapUtils.getNextSiblingsNode(this).style.color=\'#ffffff\'; MapUtils.getNextSiblingsNode(this).style.background=\'#58ACFA\';" '
                        + 'onmouseout="this.style.border=\'solid 2px #ffffff\'; MapUtils.getNextSiblingsNode(this).style.color=\'transparent\'; MapUtils.getNextSiblingsNode(this).style.background=\'transparent\';">'
                        + '<span style="position:absolute; ' + orient + ': '
                        + (parseInt(dom.style.width.substr(0, dom.style.width.length - 2))
                            + 4) * i
                        + 'px; bottom:0;width:' + dom.style.width
                        + ';display: inline-block;height: 20px;line-height: 20px;padding: 0px 4px;color: transparent;background: transparent;">'
                        + layers[i].text + '</span></li>';
                }
                baseLayerIds.push(layerId);
            }
            if (domId) {
                innerHtml += '<ul>';
                dom.innerHTML = innerHtml;
            }
        },

        /**
         * 叠加ArcGIS发布地图服务
         * @param url 地图服务地址（必填）
         * @param name 图层名称（必填）
         * @param id  图层id
         */
        "addArcGISLayer": function (url, name, id) {
            mapObject.addLayer(new ol.layer.Tile({
                source: new ol.source.TileArcGISRest({
                    projection: mapParam.projection,
                    url: url
                }),
                name: name,
                id: id !== undefined ? id : mapu.generateUUID()
            }));
        },

        /**
         * 叠加GeoServer发布地图服务
         * @param serverUrl GeoServer地址（必填）
         * @param layerName 图层服务名称（必填）
         * @param name  图层名称（必填）
         * @param id  图层id
         */
        "addGeoServerLayer": function (serverUrl, layerName, name, id) {
            mapObject.addLayer(new ol.layer.Tile({
                source: new ol.source.TileWMS({
                    extent: mapParam.extent,
                    params: {
                        'LAYERS': layerName,
                        'VERSION': '1.1.0',
                        'BBOX': mapParam.extent,
                        'CRS': mapParam.projection,
                        'WIDTH': 768,
                        'HEIGHT': 416
                    },
                    projection: mapParam.projection,
                    url: serverUrl
                }),
                name: name,
                id: id !== undefined ? id : mapu.generateUUID()
            }));
        },

        /**
         * 底图切换执行函数
         * @param layerId 底图ID（必填）
         */
        "changeBaseLayerVisibility": function (layerId) {
            var layerCollection = mapObject.getLayers();
            layerCollection.forEach(function (layer) {
                if (layer.get("id")) {
                    for (var i = 0; i < baseLayerIds.length; i++) {
                        if (layer.get("id") === baseLayerIds[i]) {
                            if (layer.get("id") === layerId) {
                                layer.setVisible(true);
                                return;
                            } else {
                                layer.setVisible(false);
                                return;
                            }
                        }
                    }
                }
            })
        },

        /**
         * 添加ArcGISServer发布服务为活动图层
         * @param wmsUrl WMS服务地址
         * @param mapServerUrl MapServer服务地址
         * @param wmsLayerName WMS服务图层名称
         * @param mapServerLayerId MapServer图层ID
         * @param layerName 图层名称
         * @Param visibility 图层是否显示
         * @Param target 图层控制器
         * @param legendTarget 图例位置
         * @param layerId 图层ID
         * @returns {*} 图层ID
         */
        "addArcGISServerActiveLayer": function (wmsUrl, mapServerUrl, wmsLayerName,
            mapServerLayerId, layerName, visibility, target, legendTarget,
            layerId) {
            if (!layerId) {
                layerId = mapu.generateUUID();
            }
            var layer = new ol.layer.Tile({
                source: new ol.source.TileWMS({
                    crossOrigin: 'anonymous',
                    params: {'LAYERS': wmsLayerName},
                    projection: mapParam.projection,
                    url: wmsUrl
                }),
                visible: visibility,
                name: layerName,
                id: layerId
            });
            mapObject.addLayer(layer);
            arcgisServerActiveLayers.push({
                id: layerId,
                name: layerName,
                mapServerUrl: mapServerUrl,
                mapServerLayerId: mapServerLayerId,
                visible: visibility,
                target: target
            });
            if (target) {
                loadLayersControl(target, layer);
            }
            if (legendTarget) {
                getArcGISServerLegend(mapServerUrl, mapServerLayerId, layerName,
                    legendTarget,
                    layerId, visibility);
            }
            return layerId;
        },

        /**
         * 添加GeoServer发布服务为活动图层
         * @param geoServerUrl Geoserver服务地址
         * @param serverLayerName GeoServer图层名称
         * @param layerName 图层名称
         * @param visibility 图层是否显示
         * @Param target 图层控制器
         * @param legendTarget 图例位置
         * @param layerId 图层id
         * @returns {*} 图层Id
         */
        "addGeoServerActiveLayer": function (geoServerUrl, serverLayerName,
            layerName, visibility, target, legendTarget, layerId) {
            if (!layerId) {
                layerId = mapu.generateUUID();
            }
            var layer = new ol.layer.Tile({
                source: new ol.source.TileWMS({
                    crossOrigin: 'anonymous',
                    extent: mapParam.extent,
                    params: {
                        'LAYERS': serverLayerName,
                        'VERSION': '1.1.0',
                        'BBOX': mapParam.extent,
                        'CRS': mapParam.projection,
                        'WIDTH': 768,
                        'HEIGHT': 416
                    },
                    projection: mapParam.projection,
                    url: geoServerUrl
                }),
                visible: visibility,
                name: layerName,
                id: layerId
            });
            mapObject.addLayer(layer);
            geoServerActiveLayers.push({
                id: layerId,
                name: layerName,
                geoServerUrl: geoServerUrl,
                serverLayerName: serverLayerName,
                visible: visibility,
                target: target
            });
            if (target) {
                loadLayersControl(target, layer);
            }
            if (legendTarget) {
                var serverUrl = geoServerUrl.substr(0,
                    geoServerUrl.indexOf("geoserver"));
                getGeoServerLegend(serverUrl, serverLayerName, layerName, legendTarget,
                    layerId, visibility);
            }
            return layerId;
        },
        "changeBeltMarkerSize": function (iconArray) {
            beltFeatures = beltMarkersource.getFeatures();
            var beltFeaturesOrder = [];
            for(var i = 0;i < iconArray.length;i++){
                for(var j = 0;j < beltFeatures.length;j++){
                    if(iconArray[i][1].item == beltFeatures[j].values_.beltId){
                        beltFeaturesOrder.push(beltFeatures[j]);
                        break;
                    }
                }
            }
            beltFeatures = beltFeaturesOrder;
            //beltMarkersource.getFeatures()
        },
        "beltMarkerChangeByMove": function (iconArray, callFunction) {
            mapObject.on('moveend', function (e) {
                if (!!beltMarkersource && !!beltMarkerLayer && iconArray.length > 0 && !!iconArray) {
                    //for (var i = 0; i < beltFeatures.length; i++) {
                    //beltFeatures[i].setStyle(beltFeatures[i].getStyle().getImage().setScale(mapObject.getView().getZoom() * 0.03));
                    //}
                    beltMarkersource.clear();
                    mapObject.removeLayer(beltMarkerLayer);
                    mapObject.addLayer(beltMarkerLayer);
                    for (var i = 0; i < beltFeatures.length; i++) {
                        beltFeatures[i].setStyle(new ol.style.Style({
                            image: new ol.style.Icon({
                                anchor: [0.5, 0.9],
                                src: iconArray[i][0].image,
                                scale: mapObject.getView().getZoom() * 0.03
                            })
                        }));
                    }
                    beltMarkersource.addFeatures(beltFeatures);
                }
                //callFunction(mapObject,beltMarkerLayer,beltMarkersource);
            })
        },
        //移除皮带秤图标图层
        "removeBeltMarkerLayer": function () {
            beltMarkersource.clear();
            mapObject.removeLayer(beltMarkerLayer);
            $('#legendControl').html('');
        },
        //添加皮带秤图标图层
        "addBeltMarkerLayer": function (layerId, layerName, visibility) {
            beltMarkerLayer = new ol.layer.Vector({
                id: layerId,
                name: layerName,
                source: beltMarkersource,
                visible: visibility,
            });
            mapObject.addLayer(beltMarkerLayer);
        },
        "addWuXueMarker": function (data, lon, lat, icon, visibility, layerName, legendTarget, layerId) {
            var beltfeature = new ol.Feature({
                geometry: new ol.geom.Point([data[lon], data[lat]])
            });
            beltfeature.setStyle(new ol.style.Style({
                image: new ol.style.Icon({
                    anchor: [0.5, 0.9],
                    src: icon,
                    scale: mapObject.getView().getZoom() * 0.03
                })
            }));
            beltfeature.setProperties(data);
            beltMarkersource.addFeature(beltfeature);
            if (legendTarget) {
                getMarkerLegend(icon, layerName, legendTarget, layerId, visibility);
            }
        },
        "addMarkerLayer": function (data, lon, lat, icon, visibility, layerName, legendTarget) {
            var layerId = mapu.generateUUID();
            var source = new ol.source.Vector();
            for (var i = 0; i < data.length; i++) {
                var newfeature = new ol.Feature({
                    geometry: new ol.geom.Point([data[i][lon], data[i][lat]])
                });
                newfeature.setProperties(data[i]);
                source.addFeature(newfeature);
            }
            var layer = new ol.layer.Vector({
                id: layerId,
                name: layerName,
                source: source,
                visible: visibility,
                style: function (feature) {
                    return new ol.style.Style({
                        image: new ol.style.Icon({
                            anchor: [0.5, 0.9],
                            src: icon,
                            scale: mapObject.getView().getZoom() * 0.03
                        })
                        // , text: createTextStyle(feature, label)
                    });
                }
            });
            mapObject.addLayer(layer);
            // markerLayers.push({
            //   name: layerName,
            //   id: layerId,
            //   visible: visibility,
            //   target: target
            // });
            // if (target) {
            //   loadLayersControl(target, layer);
            // }
            if (legendTarget) {
                getMarkerLegend(icon, layerName, legendTarget, layerId, visibility);
            }
            return layerId;
        },

        /**
         * 关闭infoWindow
         */
        "closeInfoWindow": function () {
            infoWindow.setPosition(undefined);
        },

        /**
         * 设置infoWindow标题
         * @param title 标题
         */
        "setInfoTitle": function (title) {
            var popTitle = document.getElementById(
                mapParam.target + '-popup-title');
            popTitle.innerHTML = title;
        },

        /**
         * 设置infoWindow内容
         * @param content 内容
         */
        "setInfoContent": function (content) {
            var popContent = document.getElementById(
                mapParam.target + '-popup-content');
            popContent.innerHTML = content;
        },

        /**
         * 显示infoWindow
         * @param position
         */
        "setInfoPosition": function (position) {
            infoWindow.setPosition(position);
        },

        /**
         * 初始化ArcGIS Server服务搜索
         * @param layerId
         * @param fieldName
         * @param boxTarget
         * @param resultTarget
         */
        "initArcGISServerSearchBox": function (layerId, fieldName, boxTarget,
            resultTarget) {
            for (var i = 0; i < arcgisServerActiveLayers.length; i++) {
                if (layerId === arcgisServerActiveLayers[i].id) {
                    var url = arcgisServerActiveLayers[i].mapServerUrl + "/"
                        + arcgisServerActiveLayers[i].mapServerLayerId + "/query";
                    var boxContent = document.getElementById(boxTarget);
                    var box = document.createElement("div");
                    box.innerHTML = '<input type="text" id="text_' + boxTarget
                        + '" style="width: 80%"/><input type="button" id="btn_search_'
                        + boxTarget + '" value="搜索" style="width: 15%"/>';
                    boxContent.appendChild(box);
                    var button = document.getElementById('btn_search_' + boxTarget);
                    button.onclick = function () {
                        var data = {};
                        data.where = fieldName + " like '%" + document.getElementById(
                            "text_" + boxTarget).value + "%'";
                        data.outFields = "*";
                        data.returnGeometry = true;
                        data.f = "pjson";
                        $.ajax({
                            type: 'post',
                            url: url,
                            async: false,
                            dataType: 'jsonp',
                            contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
                            data: data,
                            success: function (resData) {
                                var geojsonFormat = new ol.format.EsriJSON();
                                var features = geojsonFormat.readFeatures(
                                    JSON.stringify(resData));
                                var geoJSONFormat = new ol.format.GeoJSON();
                                selectSource.clear();
                                mapObject.removeLayer(selectLayer);
                                mapObject.addLayer(selectLayer);
                                selectSource.addFeatures(features);
                                mapObject.getView().fit(selectSource.getExtent());
                                if (typeof resultTarget === "string") {
                                    var html = "<ul style='list-style: none;cursor: pointer;'>";
                                    for (var i = 0; i < features.length; i++) {
                                        var feature = geoJSONFormat.writeFeature(features[i]);
                                        html += '<li onclick=\'MapUtils.OpenlayersUtil.moveToFeature('
                                            + feature + ');\'>' + features[i].get(fieldName)
                                            + '</li>';
                                    }
                                    html += "</ul>";
                                    var resultDom = document.getElementById(resultTarget);
                                    resultDom.innerHTML = html;
                                }
                                else if (typeof resultTarget === "function") {
                                    resultTarget(features);
                                }

                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                console.log("连接数据的时候出现了：" + textStatus);
                            }

                        });
                    }
                }
            }

        },

        /**
         * 初始化GeoServer服务搜索
         * @param layerId
         * @param fieldName
         * @param boxTarget
         * @param resultTarget
         */
        "initGeoServerSearchBox": function (layerId, fieldName, boxTarget,
            resultTarget) {
            for (var i = 0; i < geoServerActiveLayers.length; i++) {
                if (layerId === geoServerActiveLayers[i].id) {
                    var layerName = geoServerActiveLayers[i].serverLayerName;
                    var workspace = layerName.substr(0, layerName.indexOf(":"));
                    var serverUrl = geoServerActiveLayers[i].geoServerUrl.substr(0,
                        geoServerActiveLayers[i].geoServerUrl.indexOf("geoserver"));
                    var boxContent = document.getElementById(boxTarget);
                    var box = document.createElement("div");
                    box.innerHTML = '<input type="text" id="text_' + boxTarget
                        + '" style="width: 80%"/><input type="button" id="btn_search_'
                        + boxTarget + '" value="搜索" style="width: 15%"/>';
                    boxContent.appendChild(box);
                    var button = document.getElementById('btn_search_' + boxTarget);
                    button.onclick = function () {
                        var filter = ol.format.filter.like(fieldName,
                            "%" + document.getElementById(
                            "text_" + boxTarget).value + "%");
                        var featureRequest = new ol.format.WFS().writeGetFeature({
                            srsName: mapParam.projection,//坐标系统
                            featureNS: workspace,//命名空间 URI
                            featurePrefix: workspace,//工作区名称
                            featureTypes: [layerName],//查询图层，可以同一个工作区下多个图层，逗号隔开
                            filter: filter
                        });
                        var param = new XMLSerializer().serializeToString(featureRequest);
                        var wfsParams = {
                            service: 'WFS',
                            version: '1.0.0',
                            request: 'GetFeature',
                            typeName: layerName,  //图层名称
                            filter: param,
                            outputFormat: 'application/json'
                        };
                        $.ajax({
                            type: 'POST',
                            url: serverUrl + "geoserver/wfs",
                            data: $.param(wfsParams),
                            dataType: 'json',
                            success: function (data) {
                                var geoJSONFormat = new ol.format.GeoJSON();
                                var features = geoJSONFormat.readFeatures(data);
                                selectSource.clear();
                                mapObject.removeLayer(selectLayer);
                                mapObject.addLayer(selectLayer);
                                selectSource.addFeatures(features);
                                mapObject.getView().fit(selectSource.getExtent());
                                if (typeof resultTarget === "string") {
                                    var html = "<ul style='list-style: none;cursor: pointer;'>";
                                    for (var i = 0; i < features.length; i++) {
                                        var feature = geoJSONFormat.writeFeature(features[i]);
                                        html += '<li onclick=\'MapUtils.OpenlayersUtil.moveToFeature('
                                            + feature + ');\'>' + features[i].get(fieldName)
                                            + '</li>';
                                    }
                                    html += "</ul>";
                                    var resultDom = document.getElementById(resultTarget);
                                    resultDom.innerHTML = html;
                                } else if (typeof resultTarget === "function") {
                                    resultTarget(features);
                                }
                            },
                            error: function (err) {
                                console.log(err);
                            }
                        });
                    }
                }
            }

        },

        /**
         * 移动到图形位置
         * @param featureJson
         */
        "moveToFeature": function (featureJson) {
            var geoJSONFormat = new ol.format.GeoJSON();
            var feature = geoJSONFormat.readFeature(featureJson);
            selectSource.clear();
            mapObject.removeLayer(selectLayer);
            mapObject.addLayer(selectLayer);
            selectSource.addFeature(feature);
            mapObject.getView().fit(feature.getGeometry().getExtent());
        },

        /**
         * 初始化测量工具
         * @param type
         */
        "initMeasureTools": function (type) {
            mapObject.removeLayer(measureLayer);
            mapObject.addLayer(measureLayer);
            measureType = type;
            if (type === "Clear") {
                measureSource.clear();
                var overlays = mapObject.getOverlays();
                overlays.forEach(function (overlay) {
                    if (infoWindow !== overlay) {
                        mapObject.removeOverlay(overlay);
                    }
                });
                overlays.forEach(function (overlay) {
                    if (infoWindow !== overlay) {
                        mapObject.removeOverlay(overlay);
                    }
                });
                overlays.forEach(function (overlay) {
                    if (infoWindow !== overlay) {
                        mapObject.removeOverlay(overlay);
                    }
                });
                return;
            }

            if (identifyEvent) {
                ol.Observable.unByKey(identifyEvent);
            }
            mapObject.removeInteraction(selectSingleClick);

            var pointerMove = mapObject.on('pointermove', pointerMoveHandler);
            var pointerOut = mapObject.getViewport().addEventListener('mouseout',
                function () {
                    helpTooltipElement.classList.add('hidden');
                });
            var draw = new ol.interaction.Draw({
                source: measureSource,
                type: type,
                style: new ol.style.Style({
                    fill: new ol.style.Fill({
                        color: 'rgba(255, 255, 255, 0.2)'
                    }),
                    stroke: new ol.style.Stroke({
                        color: 'rgba(0, 0, 0, 0.5)',
                        lineDash: [10, 10],
                        width: 2
                    }),
                    image: new ol.style.Circle({
                        radius: 5,
                        stroke: new ol.style.Stroke({
                            color: 'rgba(0, 0, 0, 0.7)'
                        }),
                        fill: new ol.style.Fill({
                            color: 'rgba(255, 255, 255, 0.2)'
                        })
                    })
                })
            });
            mapObject.addInteraction(draw);

            createMeasureTooltip();
            createHelpTooltip();

            var listener;
            var singleClick;
            var starter = draw.on('drawstart',
                function (evt) {
                    // set sketch
                    sketch = evt.feature;
                    var tooltipCoord = evt.coordinate;
                    listener = sketch.getGeometry().on('change', function (evt) {
                        var geom = evt.target;
                        var output;
                        if (geom instanceof ol.geom.Polygon) {
                            output = formatArea(geom);
                            tooltipCoord = geom.getInteriorPoint().getCoordinates();
                        } else if (geom instanceof ol.geom.LineString) {
                            output = formatLength(geom);
                            tooltipCoord = geom.getLastCoordinate();

                        }
                        measureTooltipElement.innerHTML = output;
                        measureTooltip.setPosition(tooltipCoord);
                    });

                    var drawgeom = evt.feature.getGeometry();
                    singleClick = mapObject.on('singleclick', function (evt) {
                        if (drawgeom instanceof ol.geom.LineString) {
                            if (drawgeom.flatCoordinates.length > 4) {
                                var point1 = drawgeom.flatCoordinates.slice(
                                    drawgeom.flatCoordinates.length - 6,
                                    drawgeom.flatCoordinates.length - 4);
                                var point2 = drawgeom.flatCoordinates.slice(
                                    drawgeom.flatCoordinates.length - 4,
                                    drawgeom.flatCoordinates.length - 2);
                                var lineString = new ol.geom.LineString([point1, point2]);
                                var output = formatLength(lineString);
                                createMiddleTooltip(output, evt.coordinate);
                            }
                        }
                    });

                }, this);

            draw.on('drawend',
                function (evt) {
                    var drawgeom = evt.feature.getGeometry();
                    if (drawgeom instanceof ol.geom.LineString) {
                        if (drawgeom.flatCoordinates.length > 4) {
                            var point1 = drawgeom.flatCoordinates.slice(
                                drawgeom.flatCoordinates.length - 4,
                                drawgeom.flatCoordinates.length - 2);
                            var point2 = drawgeom.flatCoordinates.slice(
                                drawgeom.flatCoordinates.length - 2,
                                drawgeom.flatCoordinates.length);
                            var lineString = new ol.geom.LineString([point1, point2]);
                            var output = formatLength(lineString);
                            createMiddleTooltip(output, point2);
                        }
                    } else if (drawgeom instanceof ol.geom.Point) {
                        measureTooltipElement.innerHTML = "经度："
                            + drawgeom.flatCoordinates[0].toFixed(3) + ";纬度："
                            + drawgeom.flatCoordinates[1].toFixed(3);
                        measureTooltip.setPosition(drawgeom.flatCoordinates);
                    }
                    measureTooltipElement.className = 'tooltip tooltip-static';
                    measureTooltip.setOffset([0, -7]);
                    // unset sketch
                    sketch = null;
                    // unset tooltip so that a new one can be created
                    measureTooltipElement = null;
                    createMeasureTooltip();
                    ol.Observable.unByKey(listener);
                    ol.Observable.unByKey(starter);
                    ol.Observable.unByKey(pointerMove);
                    ol.Observable.unByKey(pointerOut);
                    ol.Observable.unByKey(singleClick);

                    helpTooltipElement.classList.add('hidden');
                    setTimeout(function () {
                        identifyEvent = mapObject.on('singleclick', doIdentify);
                        mapObject.addInteraction(selectSingleClick);
                        mapObject.removeInteraction(draw);
                    }, 300);
                }, this);
        },

        /**
         * 导出地图为PDF并下载
         * @param btn
         * @param format
         * @param resolution
         */
        "mapExportPDF": function (btn, format, resolution) {
            btn.disabled = true;
            document.body.style.cursor = 'progress';
            var dims = {
                a0: [1189, 841],
                a1: [841, 594],
                a2: [594, 420],
                a3: [420, 297],
                a4: [297, 210],
                a5: [210, 148]
            };

            var dim = dims[format];//A4
            var width = Math.round(dim[0] * resolution / 25.4);
            var height = Math.round(dim[1] * resolution / 25.4);
            var size = /** @type {module:ol/size~Size} */ (mapObject.getSize());
            var extent = mapObject.getView().calculateExtent(size);

            mapObject.once('rendercomplete', function (event) {
                var canvas = event.context.canvas;
                var data = canvas.toDataURL('image/jpeg');
                var pdf = new jsPDF('landscape', undefined, "a4");
                pdf.addImage(data, 'JPEG', 0, 0, dim[0], dim[1]);
                pdf.save('map.pdf');
                // Reset original map size
                mapObject.setSize(size);
                mapObject.getView().fit(extent, {size: size});
                btn.disabled = false;
                document.body.style.cursor = 'auto';
            });

            // Set print size
            var printSize = [width, height];
            mapObject.setSize(printSize);
            mapObject.getView().fit(extent, {size: printSize});
        },

        /**
         * 导出地图为PNG并下载
         */
        "mapExportPNG": function () {
            mapObject.once('rendercomplete', function (event) {
                var canvas = event.context.canvas;
                if (navigator.msSaveBlob) {
                    navigator.msSaveBlob(canvas.msToBlob(), 'map.png');
                } else {
                    canvas.toBlob(function (blob) {
                        saveAs(blob, 'map.png');
                    });
                }
            });
            mapObject.renderSync();
        },

        /**
         * 全图
         */
        "fullExtent": function () {
            var view = mapObject.getView();
            view.animate({zoom: mapParam.zoom}, {center: mapParam.center});
        },

        /**
         * 定位
         * @param lon 经度
         * @param lat 纬度
         */
        "fitToPoint": function (lon, lat) {

            var view = mapObject.getView();
            view.animate({zoom: mapParam.zoom}, {center: [lon, lat]});
            /*var div = document.createElement("div");
             div.style.backgroundColor = "transparent";
             div.appendChild(markerCanvas);
             var marker = new ol.Overlay({
             element: div,
             autoPan: true,
             autoPanAnimation: {
             duration: 250
             }
             });
             mapObject.addOverlay(marker);
             marker.setPosition([lon, lat])*/

            createCircles();
            setInterval("MapUtils.OpenlayersUtil.markerRender()", 100);
            mapObject.removeLayer(markerLayer);
            mapObject.addLayer(markerLayer);
            var marker = new ol.Feature({
                geometry: new ol.geom.Point([lon, lat])
            });
            marker.setProperties({"经度": lon});
            marker.setProperties({"纬度": lat});
            markerSource.addFeature(marker);

        },

        /**
         * 拉框放大
         */
        "dragZoomIn": function () {
            dragZoomOut.setActive(false);
            dragZoomIn.G = false;
            dragZoomIn.setActive(true);
            document.getElementById(mapParam.target).style.cursor = "crosshair";
        },

        /**
         * 拉框缩小
         */
        "dragZoomOut": function () {
            dragZoomIn.setActive(false);
            dragZoomOut.G = true;
            dragZoomOut.setActive(true);
            document.getElementById(mapParam.target).style.cursor = "crosshair";
        },

        /**
         * 平移
         */
        "setPan": function () {
            dragZoomIn.setActive(false);
            dragZoomOut.setActive(false);
            document.getElementById(mapParam.target).style.cursor = "move";
        },

        /**
         * Canvas动态标注渲染
         */
        "markerRender": function () {
            //先将主canvas的图像缓存到临时canvas中
            markerBackCtx.drawImage(markerCanvas, 0, 0, markerSize, markerSize);
            //清除主canvas上的图像
            markerContext.clearRect(0, 0, markerSize, markerSize);
            //在主canvas上画新圆
            createCircles();
            //新圆画完后，再把临时canvas的图像绘制回主canvas中
            markerContext.drawImage(markerBackCanvas, 0, 0, markerSize, markerSize);
        },

        /**
         * 创建绘图工具（未完成）
         * @param typeValue 类型
         */
        "initDrawTool": function (typeValue) {
            if (typeValue !== 'None') {

                mapObject.removeLayer(drawLayer);
                mapObject.addLayer(drawLayer);

                drawSource.clear();

                if (identifyEvent) {
                    ol.Observable.unByKey(identifyEvent);
                }
                mapObject.removeInteraction(selectSingleClick);

                var geometryFunction, maxPoints;
                if (typeValue === 'Square') {                 //正方形
                    typeValue = 'Circle';
                    geometryFunction = ol.interaction.Draw.createRegularPolygon(4);
                } else if (typeValue === 'Box') {              //长方形
                    typeValue = 'Circle';
                    geometryFunction = ol.interaction.Draw.createBox();
                }
                //实例化图形绘制控件对象并添加到地图容器中
                var draw = new ol.interaction.Draw({
                    source: drawSource,
                    type: typeValue,
                    geometryFunction: geometryFunction
                });
                mapObject.addInteraction(draw);
                draw.on('drawend', function (evt) {
                    setTimeout(function () {
                        identifyEvent = mapObject.on('singleclick', doIdentify);
                        mapObject.addInteraction(selectSingleClick);
                        mapObject.removeInteraction(draw);
                    }, 300);
                }, this);
            }
        },

        /**
         * 根据图层id删除图层
         * @param layerId 图层id
         */
        "removeLayerById": function (layerId) {
            var layerCollection = mapObject.getLayers();
            layerCollection.forEach(function (layer) {
                if (layer) {
                    if (layer.get("id")) {
                        if (layerId === layer.get("id")) {
                            mapObject.removeLayer(layer);

                        }
                    }
                }

            });
        },

        /**
         * 根据图层id关键字删除图层
         * @param key 图层id关键字
         */
        "removeLayersByKey": function (key) {
            var layerCollection = mapObject.getLayers();
            layerCollection.forEach(function (layer) {
                if (layer) {
                    if (layer.get("id")) {
                        if (layer.get("id").indexOf(key) !== -1) {
                            mapObject.removeLayer(layer);
                        }
                    }
                }

            });
        }

    };

})(MapUtils, ol, jQuery);