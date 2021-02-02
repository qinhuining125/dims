# jquery.bootgrid.js修改

## 2016年7月25日
1. 行状态。``row.status`` 改为 ``row.rowstatus``
2. 控制行变色
   如果想让数据整行变色，设置``rowstatus =[0,1,2,3,4]``
   -  0  - success
   -  1  - info
   -  2  - warning
   -  3  - danger


## 2016年7月26日
1. ``templates.search`` 加入``display:none``，不显示搜索框
2. 修改``renderRows``方法，增加行号。``formatter``中，使用``row.idx``，根据页码返回相应行号
3. 修改``renderRows``，增加自定义td的html。
   必须注意几点：
   -  表头设置``formatter``属性：``data-formatter="link"``
   -  表头设置自定义代码属性 ``data-custom="true"``
   -  bootgrid的设置里，**必须写入**对应的``formatter``。否则将输出空的td


# 2016年7月28日
原``reload()``刷新后回到第一页
1. 增加reloadEx方法，刷新指定页
````javascript  
  $("#id").bootgrid("reloadEx", 2);

  //获取当前页方法
  $('#id').bootgrid('getCurrentPage')
````
2. 增加reloadCurr方法，刷新当前页
````javascript  
  $("#id").bootgrid("reloadCurr");
````
3. 加入分页的配置项 paginationMsg
````javascript  
  paginationMsg = {
    "first" : "首页",
    "prev" : "上一页",
    "next" : "下一页",
    "last" : "末页"
  }
````

## 2016年7月29日
1. 将分页条加入到表格容器内，``wrapper.after`` 改为 ``wrapper.append``
2. 加入新属性，高级搜索模式 ``searchPhraseAdModel = 1``
   **搜索分为两种模式**
````javascript  
   // 普通的文本，searchPhrase = xxx
   $("#id").bootgrid("search"); 
  
   // JSON，searchPhrase[a]=1&searchPhrase[b]=2
   $("#id").bootgrid("search", {"a":1,"b":2});
````
   **启用高级搜索的模式**（``searchPhraseAdModel = 1``）
   post数据：``a=1&b=2``
3. 加入记录最后一次事件的方法：getLastMethod，获取一次，立即清除
   $("#id").bootgrid("getLastMethod");

## 2016年9月9日
1. 首次加载不向后端传递排序字段，使用后端自己的排序规则。（``isFirst``）

## 2016年12月6日
1. 刷新按钮增加频度控制
2. 增加搜索句柄，``searchPhraseHandler``，修复搜索表单如果变更内容，点击刷新依然按照之前搜索的bug。

## 2016年12月7日
1. 获取当前请求的数据
````javascript 
   $("#id").bootgrid("getCurrentRows");
````
2. 增加行级锁，``rowLocked = 1``的时候，无法被选中。

## 关于初始请求参数otherParams的说明
主要用于固定参数的查询。比如通过url传入某个固定的参数，查询项无论如何选择，该参数始终要参与查询。    
参数可以接受``json``或者``function``，默认使用``{}``。    

有一种特例，即传入的url仅做初始化查询，后续的查询可以变化。这种情况可以参考下面的代码
````javascript
// 定义一个全局的状态标记，记录是否初始化
var isInit = true;

// 配置otherParams参数
"otherParams": function () {
  if (isInit) {
    isInit = false;
    return {
      "name": "..."
    };
  } else {
    return {};
  }
}
````
当初始化的时候，``otherParams``有值，否则使用空值。


