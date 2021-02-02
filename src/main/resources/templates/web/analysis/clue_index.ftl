<#include "/common/head.ftl"/>
<#include "/common/cssfile_list.ftl"/>
<style>
  .content {
    width: 300px;
    white-space: pre-wrap;
  }
</style>
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content row">
  <div class="col-sm-12">
    <div class="ibox">
      <div class="ibox-content">
        <form class="form-horizontal">
          <div class="row">
            <div class="col-xs-6 col-sm-4 col-lg-4">
              <div class="form-group">
                <label class="col-sm-3 control-label"><span class="text-danger">*</span>时间段：</label>
                <div class="col-sm-8">
                  <div class="input-group">
                    <input type="text" class="form-control" id="timeRange" readonly="readonly"
                           placeholder="统计日期"/>
                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span></div>
                </div>
                <input type="hidden" id="startTime" name="startTime">
                <input type="hidden" id="endTime" name="endTime">
              </div>
            </div>
            <div class="col-xs-6 col-sm-4 col-lg-3">
              <div class="form-group">
                <label class="col-sm-4 control-label">乡镇</label>
                <div class="col-sm-7">
                  <select name="areaCode" id="areaCode" class="form-control">
                    <option value="">全部</option>
                      <#list areaList as obj>
                        <option value="${(obj.areaCode)!}">${(obj.areaName)!}</option>
                      </#list>
                  </select>
                </div>
              </div>
            </div>
          </div>
          <div class="text-center">
            <button id="btn-cx" class="btn btn-primary"><i class="fa fa-search"></i>&nbsp;查询
            </button>
          </div>
        </form>
      </div>
    </div>
    <div>
      <div style="width: 45%;height: 500px;float: left" id="pieContainer"></div>
      <div style="width: 45%;height: 500px;float: left" id="barContainer"></div>
      <div style="width: 45%;height: 500px;float: left" id="llyContainer"></div>
      <div style="width: 45%;height: 500px;float: left" id="wgyContainer"></div>
    </div>
  </div>
</div>
<#include "/common/scriptfile.ftl"/>
<#include "/common/scriptfile_list.ftl"/>
<script src="${global.staticPath!}static/plugins/laydate/laydate.js"></script>
<script src="${global.staticPath!}static/plugins/echarts/echarts-all.js"></script>
<script>
  laydate.render({
    "elem": "#timeRange",
    "range": "至",
    "format": "yyyy-MM-dd",
    "trigger": "click",
    "done": function (value, startDate, endDate) {
      if (value) {
        $("#startTime").val(+new Date(startDate.year, startDate.month - 1, startDate.date));
        $("#endTime").val(+new Date(endDate.year, endDate.month - 1, endDate.date + 1));
      } else {
        $("#startTime").val("");
        $("#endTime").val("");
      }
    }
  });
  $(document).ready(function () {
    $("#btn-cx").on("click", function () {
      const data = $(this).parent().parent().serialize();
      $.ajax({
        "url": "a/analysis/clueData.json",
        "type": "POST",
        "data": data,
        "dataType": "json",
        "cache": false,
        "async": false,
        "success": function (data) {
          if (data.success) {
            initPieData(data.result);
            initBarData(data.result);
            initLlyBarData(data.result);
            initWgyBarData(data.result);
          }
        }
      });
      return false;
    });
    $("#btn-cx").click();
  });

  //饼状图
  function initPieData(pieData) {
    const piedom = document.getElementById("pieContainer");
    const pieChart = echarts.init(piedom);
    const pieOption = {
      tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b}: {c} ({d}%)'
      },
      legend: {
        orient: 'horizontal',
        left: 10,
        data: ['未受理', '已受理', '已办结', '已知晓' , '已转办']
      },
      title: [{
        text: '总量',
        top: 'center',
        left: 'center'
      }],
      series: [
        {
          type: 'pie',
          radius: ['50%', '70%'],
          data: [
            {value: pieData.unAccept, name: '未受理'},
            {value: pieData.accept, name: '已受理'},
            {value: pieData.complete, name: '已办结'},
            {value: pieData.knowTask, name: '已知晓'},
            {value: pieData.turnToOtherTask, name: '已转办'}
          ]
        }
      ]
    };
    if (pieOption && typeof pieOption === "object") {
      pieChart.setOption(pieOption, true);
    }
  }

  //柱状图
  function initBarData(barData) {
    const bardom = document.getElementById("barContainer");
    const barChart = echarts.init(bardom);
    const barOption = {
      tooltip: {
        trigger: 'axis',
        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
          type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
        }
      },
      legend: {
        data: ['未受理', '已受理', '已办结', '已知晓' , '已转办']
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: [
        {
          type: 'category',
          data: barData.villageNames,
          axisLabel: {
            rotate: 30
          }
        }
      ],
      yAxis: [
        {
          type: 'value'
        }
      ],
      series: [
        {
          name: '未受理',
          type: 'bar',
          stack: '数量',
          data: barData.unAccepts
        },
        {
          name: '已知晓',
          type: 'bar',
          stack: '数量',
          data: barData.knowTasks
        },
        {
          name: '已转办',
          type: 'bar',
          stack: '数量',
          data: barData.turnToOtherTasks
        },
        {
          name: '已受理',
          type: 'bar',
          stack: '数量',
          data: barData.accepts
        },
        {
          name: '已办结',
          type: 'bar',
          stack: '数量',
          data: barData.completes
        }
      ]
    };
    if (barOption && typeof barOption === "object") {
      barChart.setOption(barOption, true);
    }
  }

  //联络员
  function initLlyBarData(barData) {
    const dom = document.getElementById("llyContainer");
    const myChart = echarts.init(dom);
    const option = {
      title: {
        text: '联络员上报类型统计'
      },
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        },
        formatter: function (params) {
          return "<div class='content'>"
              + params[0].name + " : " + params[0].value + '<br/>'
              + barData.llynrs[params[0].dataIndex] + "</div>";
        }
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: {
        type: 'value',
        boundaryGap: [0, 1]
      },
      yAxis: {
        type: 'category',
        data: ["类型1", "类型2", "类型3", "类型4", "类型5", "类型6", "类型7", "类型8", "类型9", "类型10", "类型11"]
      },
      series: [
        {
          name: '联络员',
          type: 'bar',
          data: barData.llys
        }
      ]
    };
    if (option && typeof option === "object") {
      myChart.setOption(option, true);
    }
  }

  //网格员
  function initWgyBarData(barData) {
    const dom = document.getElementById("wgyContainer");
    const myChart = echarts.init(dom);
    const option = {
      title: {
        text: '网格员上报类型统计'
      },
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        },
        formatter: function (params) {
          return "<div class='content'>"
              + params[0].name + " : " + params[0].value + '<br/>'
              + barData.wgynrs[params[0].dataIndex] + "</div>";
        }
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: {
        type: 'value',
        boundaryGap: [0, 1]
      },
      yAxis: {
        type: 'category',
        data: ["类型1", "类型2", "类型3", "类型4", "类型5", "类型6", "类型7"]
      },
      series: [
        {
          name: '网格员',
          type: 'bar',
          data: barData.wgys
        }
      ]
    };
    if (option && typeof option === "object") {
      myChart.setOption(option, true);
    }
  }
</script>
</body>
</html>
