<#include "/common/head.ftl"/>
<#include "/common/cssfile_list.ftl"/>
</head>
<body class="gray-bg">
<div style="width: 100%;height: 620px;margin: 0 auto" id="container"></div>
<#include "/common/scriptfile.ftl"/>
<#include "/common/scriptfile_list.ftl"/>
<script type="text/javascript"
        src="https://cdn.jsdelivr.net/npm/echarts/dist/echarts.min.js"></script>
<script type="text/javascript"
        src="https://api.map.baidu.com/api?v=2.0&ak=GpAdlGKiARBu82Ne7Lj3sjv9"></script>
<script type="text/javascript"
        src="https://cdn.jsdelivr.net/npm/echarts/dist/extension/bmap.min.js"></script>
<script>
  const dom = document.getElementById("container");
  const myChart = echarts.init(dom);
  const geoCoordMap = {
    '解愁乡': [113.155, 38.0588],
    '上湖乡': [113.062, 37.6894],
    '景尚乡': [113.196, 37.782],
    '羊头崖乡': [113.204, 37.6612],
    '宗艾镇': [113.096, 37.9367],
    '马首乡': [113.124, 37.8122],
    '平舒乡': [112.993, 38.0027],
    '南燕竹镇': [113.052, 37.926],
    '温家庄乡': [113.26, 38.0211],
    '朝阳镇': [113.092, 37.8777],
    '西洛镇': [112.957, 37.827],
    '松塔镇': [113.259, 37.7208],
    '尹灵芝镇': [113.272, 37.8904],
    '平头镇': [112.829, 38.0043],

  };
  const colors = [
    ["#1DE9B6", "#F46E36", "#04B9FF", "#5DBD32", "#FFC809", "#FB95D5", "#BDA29A", "#6E7074",
      "#546570", "#C4CCD3"],
    ["#37A2DA", "#67E0E3", "#32C5E9", "#9FE6B8", "#FFDB5C", "#FF9F7F", "#FB7293", "#E062AE",
      "#E690D1", "#E7BCF3", "#9D96F5", "#8378EA", "#8378EA"],
    ["#DD6B66", "#759AA0", "#E69D87", "#8DC1A9", "#EA7E53", "#EEDD78", "#73A373", "#73B9BC",
      "#7289AB", "#91CA8C", "#F49F42"],
  ];
  const colorIndex = 0;
  $(function () {
    const types = ["问题上报", "任务指派", "廉政建议"];
    const mapData = [
      [],
      [],
      []
    ];

    /*柱子Y名称*/
    const categoryData = [];
    const barData = [];
    const adviceMap = JSON.parse('${adviceMap}');
    const reportMap = JSON.parse('${reportMap}');
    const taskMap = JSON.parse('${taskMap}');
    for (const key in geoCoordMap) {
      categoryData.push(key);
      mapData[0].push({
        "type": '问题上报',
        "name": key,
        "value": reportMap[key]
      });
      mapData[1].push({
        "type": '任务指派',
        "name": key,
        "value": taskMap[key]
      });
      mapData[2].push({
        "type": '廉政建议',
        "name": key,
        "value": adviceMap[key]
      });
    }
    for (var i = 0; i < mapData.length; i++) {
      barData.push([]);
      for (var j = 0; j < mapData[i].length; j++) {
        barData[i].push(mapData[i][j].value)
      }
    }
    const convertData = function (data) {
      const res = [];
      for (var i = 0; i < data.length; i++) {
        const geoCoord = geoCoordMap[data[i].name];
        if (geoCoord) {
          res.push({
            name: data[i].name,
            value: geoCoord.concat(data[i].value)
          });
        }
      }
      return res;
    };
    $.getJSON("${global.staticPath!}static/plugins/echarts/shouyang.json", function (geoJson) {
      echarts.registerMap('SYX', geoJson);
      const optionXyMap01 = {
        timeline: {
          data: types,
          axisType: 'category',
          autoPlay: true,
          playInterval: 3000,
          left: '10%',
          right: '10%',
          bottom: '2%',
          width: '80%',
          label: {
            normal: {
              textStyle: {
                color: '#ddd'
              }
            },
            emphasis: {
              textStyle: {
                color: '#fff'
              }
            }
          },
          symbolSize: 10,
          lineStyle: {
            color: '#555'
          },
          checkpointStyle: {
            borderColor: '#777',
            borderWidth: 2
          },
          controlStyle: {
            showNextBtn: true,
            showPrevBtn: true,
            normal: {
              color: '#666',
              borderColor: '#666'
            },
            emphasis: {
              color: '#aaa',
              borderColor: '#aaa'
            }
          },

        },
        baseOption: {
          animation: true,
          animationDuration: 1000,
          animationEasing: 'cubicInOut',
          animationDurationUpdate: 1000,
          animationEasingUpdate: 'cubicInOut',
          grid: {
            right: '10%',
            top: '18%',
            bottom: '10%',
            width: '25%'
          },
          tooltip: {
            trigger: 'axis', // hover触发器
            axisPointer: { // 坐标轴指示器，坐标轴触发有效
              type: 'shadow', // 默认为直线，可选为：'line' | 'shadow'
              shadowStyle: {
                color: 'rgba(150,150,150,0.1)' //hover颜色
              }
            }
          },
          geo: {
            show: true,
            map: 'SYX',
            roam: true,
            zoom: 1,
            center: [113.177708, 37.891136],
            label: {
              emphasis: {
                show: false
              }
            },
            left: '20%',
            top: '0',
            itemStyle: {
              normal: {
                borderColor: 'rgba(147, 235, 248, 1)',
                borderWidth: 1,
                areaColor: {
                  type: 'radial',
                  x: 0.5,
                  y: 0.5,
                  r: 0.8,
                  colorStops: [{
                    offset: 0,
                    color: 'rgba(147, 235, 248, 0)' // 0% 处的颜色
                  }, {
                    offset: 1,
                    color: 'rgba(147, 235, 248, .2)' // 100% 处的颜色
                  }],
                  globalCoord: false // 缺省为 false
                },
                shadowColor: 'rgba(128, 217, 248, 1)',
                shadowOffsetX: -2,
                shadowOffsetY: 2,
                shadowBlur: 10
              },
              emphasis: {
                areaColor: '#389BB7',
                borderWidth: 0
              }
            }
          },
        },
        options: []

      };
      for (var n = 0; n < types.length; n++) {
        optionXyMap01.options.push({
          backgroundColor: '#051b4a',
          title: [{},
            {
              id: 'statistic',
              text: types[n] + "数据统计情况",
              left: '60%',
              top: '8%',
              textStyle: {
                color: '#fff',
                fontSize: 24
              }
            }
          ],
          xAxis: {
            type: 'value',
            scale: true,
            position: 'top',
            min: 0,
            boundaryGap: false,
            splitLine: {
              show: false
            },
            axisLine: {
              show: false
            },
            axisTick: {
              show: false
            },
            axisLabel: {
              margin: 2,
              textStyle: {
                color: '#aaa'
              }
            },
          },
          yAxis: {
            type: 'category',
            nameGap: 16,
            axisLine: {
              show: true,
              lineStyle: {
                color: '#ddd'
              }
            },
            axisTick: {
              show: false,
              lineStyle: {
                color: '#ddd'
              }
            },
            axisLabel: {
              interval: 0,
              textStyle: {
                color: '#ddd'
              }
            },
            data: categoryData
          },
          series: [
            {
              name: 'light',
              type: 'scatter',
              coordinateSystem: 'geo',
              data: convertData(mapData[n]),
              symbol: 'pin', //气泡
              symbolSize: function (val) {
                return 40;
              },
              label: {
                normal: {
                  formatter: '{@[2]}',
                  color: '#fff',
                  fontSize: 9,
                  show: true
                },
                emphasis: {
                  show: true
                }
              },
              itemStyle: {
                normal: {
                  color: '#F62157'
                }
              }
            },
            {
              type: 'map',
              map: 'SYX',
              geoIndex: 0,
              aspectScale: 0.75, //长宽比
              showLegendSymbol: false, // 存在legend时显示
              label: {
                normal: {
                  show: false
                },
                emphasis: {
                  show: false,
                  textStyle: {
                    color: '#fff'
                  }
                }
              },
              roam: true,
              itemStyle: {
                normal: {
                  areaColor: '#031525',
                  borderColor: '#FFFFFF',
                },
                emphasis: {
                  areaColor: '#2B91B7'
                }
              },
              animation: false,
              data: mapData
            },
            //地图点的动画效果
            {
              type: 'effectScatter',
              coordinateSystem: 'geo',
              data: convertData(mapData[n].sort(function (a, b) {
                return b.value - a.value;
              }).slice(0, 20)),
              symbolSize: function (val) {
                return 10;
              },
              showEffectOn: 'render',
              rippleEffect: {
                brushType: 'stroke'
              },
              hoverAnimation: true,
              label: {
                normal: {
                  formatter: '{b}',
                  position: 'right',
                  show: true
                }
              },
              itemStyle: {
                normal: {
                  color: colors[colorIndex][n],
                  shadowBlur: 10,
                  shadowColor: colors[colorIndex][n]
                }
              },
              zlevel: 1
            },
            {
              zlevel: 1.5,
              type: 'bar',
              symbol: 'none',
              itemStyle: {
                normal: {
                  color: colors[colorIndex][n]
                }
              },
              data: barData[n]
            }
          ]
        })
      }
      myChart.setOption(optionXyMap01);
    });
  });

  function randomNum(minNum, maxNum) {
    switch (arguments.length) {
      case 1:
        return parseInt(Math.random() * minNum + 1, 10);
        break;
      case 2:
        return parseInt(Math.random() * (maxNum - minNum + 1) + minNum, 10);
        break;
      default:
        return 0;
        break;
    }
  }
</script>
</body>
</html>