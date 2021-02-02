
#通用AJAX的写法
1. 调用关闭``confirm``的回调方法 ``fun && fun();``    
2. 如果使用``ajax``，建议使用通用``showLoading``方法。
3. 在``ajax``的完成事件（``complete``）中，关闭``showLoading``。
4. 在LayerUtil和AjaxUtil里面涉及到layer弹窗的方法,统一增加了一个参数target,用于控制弹窗的target,默认是top

````javascript
  function ajaxTest() {
    CommonUtils.LayerUtil.showConfirm("confirm标题", "正文", function (fun) {
      //点yes后，关闭confirm窗口
      fun && fun();
      //打开loading窗口，1-读取，2-提交
      var frmIdx = CommonUtils.LayerUtil.showLoading(2);
      $.ajax({
        "url": "a/footmark/footmark-reply.json",
        "type": "POST",
        "data": {},
        "dataType": "json",
        "success": function (data) {
          top.CommonUtils.ToastrUtil.success("发送成功");
        },
        "error": function (req, textStatus, errorThrown) {

        },
        "complete": function () {
          //关闭loading窗口
          CommonUtils.LayerUtil.close(frmIdx);
        }
      });
    });
  }
````
