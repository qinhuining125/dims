//插件加载完毕后，会执行初始化化操作
$(function () {
  WebVideoCtrl.insertPluginObject("divPlugin",100,100);

  //初始化插件
  WebVideoCtrl.initPlugin("Dahua2",function () {
        //设置视频窗口的显示
        var num = parseInt($("#wndNum").find("option:selected").val());
        //设置窗口分割数
        WebVideoCtrl.setSplitNum(num);
        //初始化路径
        var videoPath = WebVideoCtrl.getUserDirectory(2);
        $("#LiveRecord").val(videoPath);

        var picpath = WebVideoCtrl.getUserDirectory(1);
        $("#LiveSnapshot").val(picpath);
        $("#loaddownPos").val("0");
        $("#loaddownSpeed").val("0");
        $("#loaddownEnd").val("YES");
        //设置双击可全屏功能
        WebVideoCtrl.EnablePreviewDBClickFullSreen();
      }
  );

  $("#closePtzLocate").hide();
  $("#openPtzLocate").show();

  for(var i=0;i<36;i++){
    $("#winIDs").append("<option value='"+i+"'>"+(i+1)+"</option>");
  }
});

//视频窗口
/**
 *@description 处理窗口切换事件
 *@param{Num} iNodeIndex  节点序号
 *@param{Num} iViewIndex  视图序号
 *@param{Num} iWinID      窗口ID
 */
function responseSelectedViewSignal(iNodeIndex,iViewIndex,iWinID){
  //更新对应播放器的信息
  var playrInfo = WebVideoCtrl.getPlayerInfo(iWinID);
  //更新UI信息
  if(typeof playrInfo != "undefined"){
    //设备信息
    var deviceInfo = WebVideoCtrl.getDeviceInfo(playrInfo.ip);
    if(typeof deviceInfo != "undefined"){
      DemoUI.updateDeviceInfo(playrInfo.ip);
      DemoUI.setCurChannel(playrInfo.channle);
      DemoUI.setCurStreamType(playrInfo.streamType);
    }
  }
}

//显示操作信息
function showOPInfo(szInfo, status, error) {
  var szTip = "<div>" + Foundation.dateFormat(new Date(), "yyyy-MM-dd hh:mm:ss") + " " + szInfo;
  if (typeof status != "undefined")
  {
    szTip += "(" + status.toString() + ", " + error.toString() + ")";
  }
  szTip += "</div>";
  $("#opinfo").html(szTip + $("#opinfo").html());
}

//窗口切换事件
function changeWndNum(num)
{
  //设置视频窗口的显示
  var num = parseInt($("#wndNum").find("option:selected").val());
  WebVideoCtrl.setSplitNum(num);
}

function clickReversePlayback()
{
  WebVideoCtrl.SetPlayBackDirection();
}

function changeDelayTime(level)
{
  var level = parseInt($("#netsPreach").find("option:selected").val());
  WebVideoCtrl.setDelayTime(level);
}
function changePlaySpeed(speed)
{
  WebVideoCtrl.setPlaySpeed(speed);
}

//设备登录
function clickLogin()
{
  var szIP = $("#loginip").val();
  szPort = $("#port").val() - 0;
  szUsername = $("#username").val();
  szPassword = $("#password").val();
  rtspPort = $("#rtspport").val() - 0;
  protocol = $("#protocolType").val() - 0;
  if ("" == szIP || "" == szPort) {
    return;
  }
  var port = parseInt($("#port").val());
  //判断当前设备是否已经登录
  /*var deviceInfo = WebVideoCtrl.getDeviceInfo(szIP);
  if(typeof deviceInfo != "undefined"){
    if(WebVideoCtrl.logout(szIP))
    {
      //添加提示
      showOPInfo(szIP + " 登出设备！");
      //删除设备信息
      DemoUI.removeDeviceInfo(szIP);
    }
  }*/
  var ret = WebVideoCtrl.login(szIP,port,szUsername,szPassword,0);
  if(ret==0)
  {
    console.log("login successed");
  }else{
    console.log("login failed: "+ret);
    return;
  }
  $('#localconfigField').prop('disabled', false);
  $('#previewField').prop('disabled', false);
  $('#ptzField').prop('disabled', false);
  $('#playbackField').prop('disabled', false);
  $('#audioVideoControl').prop('disabled', false);

  //回放模块及下载样式控制
  $('#startPlay').prop('disabled', true);
  $('#stopPlay').prop('disabled', true);
  $('#pause').prop('disabled', true);
  $('#resume').prop('disabled', true);
  $('#playspeed').prop('disabled', true);
}

function switchDeviceInfo(ip)
{
  DemoUI.updateDeviceInfo(ip);
}

function clickLogout()
{
  WebVideoCtrl.logout();
  $('#pfile_rec_tbody').empty();
  alert("login out successed");
  $('#localconfigField').prop('disabled', true);
  $('#previewField').prop('disabled', true);
  $('#ptzField').prop('disabled', true);
  $('#playbackField').prop('disabled', true);
  $('#downloadField').prop('disabled', true);
  $('#audioVideoControl').prop('disabled', true);
}

function GetFileTimeLenth(){
  WebVideoCtrl.GetFileTimeLenth();
}

function downloadFile(){
  WebVideoCtrl.downloadFile();
}

function clearDownloadInfo(){
  $("#loaddownPos").val("0");
  $("#loaddownSpeed").val("0");
  $("#loaddownEnd").val("YES");
  $("#loadFileLenth").val("0");
}

function stopDownloadFile(){
  WebVideoCtrl.stopDownloadFile();
}
function clickPause(){
  WebVideoCtrl.PausePlayBack();
}
function clickResume(){
  WebVideoCtrl.ResumePlayback();
}

function clickStopPlayback(){
  WebVideoCtrl.StopPlayBack();
}

function clickStartPlayback(){
  WebVideoCtrl.PlayBackByTime();
}

function clickRecordSearch(){
  $('#pfile_rec_tbody').empty();//先清空上次搜索结果
  var ch = $("#playbackChannel").val();
  var streamType=parseInt($("#streamtype").val(), 10);
  var startTime=$("#starttime").val();
  var endTime=$("#endtime").val();
  var maxNumber=500;
  var recType=0;
  var str = '{"channel":'+ch+', "streamType":'+streamType+',"startTime":"'+startTime+'","endTime":"'+endTime+'","maxNumber":'+maxNumber+',"recType":'+recType+'}';
  WebVideoCtrl.QueryRecordInfoByTimeEx(str);

  //回放及下载模块控制
  $('#startPlay').prop('disabled', false);
  $('#stopPlay').prop('disabled', false);
  $('#pause').prop('disabled', false);
  $('#resume').prop('disabled', false);
  $('#playspeed').prop('disabled', false);
  $('#downloadField').prop('disabled', false);
  $('#loadFileLenthButton').prop('disabled', false);
}

function clickStartRealPlay(type){
  //获得通道号
  // var iChannel = $("#channels").val() - 0;
  var iChannel = type;
  //获得码流类型
  var iStreamType = parseInt($("#streamtype").val(), 10);
  WebVideoCtrl.connectRealVideo(iChannel,iStreamType);
}

function clickStartRealPlayEx(){
  //获得通道号
  var iChannel = $("#channels").val() - 0;
  //获得码流类型
  var iStreamType = parseInt($("#streamtype").val(), 10);
  //窗口号
  var iWinID = $("#winIDs").val() - 0;
  WebVideoCtrl.connectRealVideoEx(iChannel,iStreamType,iWinID);
}

function changeStreamType(streamtype){
  //获得播放器信息
  if(0 != WebVideoCtrl.getSelectedPlayerID()){
    clickStartRealPlay();
  }
}

function changedownLoadFormat(format){
  downloadFormat=format;
}

//关闭选中窗口的实时监视
function clickStopRealPlay(){
  var iChannel = $("#channels").val() - 0;
  WebVideoCtrl.closePlayer(iChannel);
}

//开启对讲
function clickStartVoiceTalk(){
  WebVideoCtrl.startVoiceTalk();
}

//关闭对讲
function clickStopVoiceTalk(){
  WebVideoCtrl.stopVoiceTalk();
}

// 打开选择框
function clickOpenFileDlg(id,type) {
  WebVideoCtrl.showFileBrowse(type);
  WebVideoCtrl.selectDirectory(type,$("#id").val);
}

function clickSetVolume(){
  //设置选中窗口的音量
  WebVideoCtrl.setVolume(parseInt($("#volume").val(), 10),{
        cbSuccess:function(winIndex){
        },
        cbFailed:function(winIndex){
        }
      }
  );
}

function clickOpenSound(){
  WebVideoCtrl.openSound();
}

function clickCloseSound(){
  WebVideoCtrl.closeSound();
}

function clickEnableEZoom(){
  WebVideoCtrl.enableEZoom();
}

function clickDisableEZoom(){
  WebVideoCtrl.disableEZoom();
}

function clickFullScreen(){
  WebVideoCtrl.setFullscreen();
}

function clickEnableIpcTalk(){
  WebVideoCtrl.enableIpcTalk();
}

function clickDisableIpcTalk(){
  WebVideoCtrl.disableIpcTalk();
}

function clickOpenSound(){
  var winID = WebVideoCtrl.getSelectedWinID();
  var playerID = PlayerMgr.getPlayerID(winID);
  if((playerID > 0) && WebVideoCtrl.controlAudio(playerID,true)){
    showOPInfo("播放器: [" + winID.toString() + ","+ playerID.toString() + "]" + "打开声音成功！");
  }else{
    showOPInfo("播放器: [" + winID.toString() + ","+ playerID.toString() + "]" + "打开声音失败！");
  }
}

function clickCloseSound(){
  var winID = WebVideoCtrl.getSelectedWinID();
  var playerID = PlayerMgr.getPlayerID(winID);
  if((playerID > 0) && WebVideoCtrl.controlAudio(playerID,false)){
    showOPInfo("播放器: [" + winID.toString() + ","+ playerID.toString() + "]" + "关闭声音成功！");
  }else{
    showOPInfo("播放器: [" + winID.toString() + ","+ playerID.toString() + "]" + "关闭声音失败！");
  }
}

function clickCapturePic(){
  var picName = $("#snapPicName").val();
  WebVideoCtrl.crabOnePicture(picName);
}

function clickStartRecord(){
  WebVideoCtrl.startRecordingVideo();
}

function clickStopRecord(){
  WebVideoCtrl.stopRecordingVideo();
}

function mouseUPLeftPTZControl(flag){
  var iChannel = $("#channels").val() - 0;
  //获得移动速度
  var speed = parseInt($("#ptzspeed").val(), 10)
  WebVideoCtrl.moveUpperLeft(iChannel,speed,speed,flag);
}

function mouseUpPTZControl(flag){
  var iChannel = $("#channels").val() - 0;
  var speed = parseInt($("#ptzspeed").val(), 10)
  WebVideoCtrl.moveUpwards(iChannel,speed,flag);
}

function mouseUPRightPTZControl(flag){
  var iChannel = $("#channels").val() - 0;
  //获得移动速度
  var speed = parseInt($("#ptzspeed").val(), 10)
  WebVideoCtrl.moveUpperRight(iChannel,speed,speed,flag);
}

function mouseLefPTZControl(flag){
  var iChannel = $("#channels").val() - 0;
  var speed = parseInt($("#ptzspeed").val(), 10)
  WebVideoCtrl.moveLeft(iChannel,speed,flag);
}

function mouseRightPTZControl(flag){
  var iChannel = $("#channels").val() - 0;
  //获得移动速度
  var speed = parseInt($("#ptzspeed").val(), 10)
  WebVideoCtrl.moveRight(iChannel,speed,flag);
}

function mouseDownLeftPTZControl(flag){
  var iChannel = $("#channels").val() - 0;
  //获得移动速度
  var speed = parseInt($("#ptzspeed").val(), 10)
  WebVideoCtrl.moveLowerLeft(iChannel,speed,speed,flag);
}

function mouseDownRightPTZControl(flag){
  var iChannel = $("#channels").val() - 0;
  //获得移动速度
  var speed = parseInt($("#ptzspeed").val(), 10)
  WebVideoCtrl.moveLowerRight(iChannel,speed,speed,flag);
}

function mouseDownPTZControl(flag){
  var iChannel = $("#channels").val() - 0;
  var speed = parseInt($("#ptzspeed").val(), 10)
  WebVideoCtrl.moveLower(iChannel,speed,flag);
}

function openPtzLocate(){
  var iChannel = $("#channels").val() - 0;
  WebVideoCtrl.enablePTZLocate(iChannel);
  //隐藏开启按钮
  $("#openPtzLocate").hide();
  //显示关闭按钮
  $("#closePtzLocate").show();
}

function closePtzLocate(){
  var iChannel = $("#channels").val() - 0;
  WebVideoCtrl.disablePTZLocate(iChannel);
  $("#closePtzLocate").hide();
  $("#openPtzLocate").show();
}

function PTZZoomout(flag){
  var iChannel = $("#channels").val() - 0;
  var speed = parseInt($("#ptzspeed").val(), 10)
  WebVideoCtrl.controlZoom(iChannel,speed,1,flag);
}

function PTZZoomIn(flag){
  var iChannel = $("#channels").val() - 0;
  var speed = parseInt($("#ptzspeed").val(), 10)
  WebVideoCtrl.controlZoom(iChannel,speed,0,flag);
}

function PTZFocusIn(flag){
  var iChannel = $("#channels").val() - 0;
  var speed = parseInt($("#ptzspeed").val(), 10)
  WebVideoCtrl.controlFocus(iChannel,speed,0,flag);
}

function PTZFoucusOut(flag){
  var iChannel = $("#channels").val() - 0;
  var speed = parseInt($("#ptzspeed").val(), 10)
  WebVideoCtrl.controlFocus(iChannel,speed,1,flag);
}

function PTZIrisIn(flag){
  var iChannel = $("#channels").val() - 0;
  //获得移动速度
  var speed = parseInt($("#ptzspeed").val(), 10)
  WebVideoCtrl.controlAperture(iChannel,speed,0,flag);
}

function PTZIrisOut(flag){
  var iChannel = $("#channels").val() - 0;
  var speed = parseInt($("#ptzspeed").val(), 10)
  WebVideoCtrl.controlAperture(iChannel,speed,1,flag);
}

function GetPresets(){
  //获得选中的窗口ID
  var winID = WebVideoCtrl.getSelectedWinID();
  //获得播放器信息
  var player = PlayerMgr.getPlayerInfo(winID);
  if(typeof player == "undefined")
  {
    showOPInfo("播放器信息有误！！！");
  }else{
    //设备ID
    var deviceID = player.deviceID;
    //通道号
    var channel = player.channle;
    WebVideoCtrl.getPresets(deviceID,channel,
        function(index,name){
          showOPInfo("获取预置点成功！！！");
          var subNode = "<option value=" + index.toString() + ">" + name + "</option>"
          $("#presetList").append(subNode);
        }
    );
    $('#presetList option:last').attr('selected','selected');

  }
}

function GotoPreset(){
  //获得选中的窗口ID
  var winID = WebVideoCtrl.getSelectedWinID();
  //获得播放器信息
  var player = PlayerMgr.getPlayerInfo(winID);
  if(typeof player == "undefined")
  {
    showOPInfo("播放器信息有误！！！");
  }else{
    //设备ID
    var deviceID = player.deviceID;
    //通道号
    var channel = player.channle;
    //获得预置点序号
    var index = parseInt($("#presetList").val(), 10);
    WebVideoCtrl.gotoPreset(deviceID,channel,index,0);

  }
}

function RemovePreset(){
  //获得选中的窗口ID
  var winID = WebVideoCtrl.getSelectedWinID();
  //获得播放器信息
  var player = PlayerMgr.getPlayerInfo(winID);
  if(typeof player == "undefined")
  {
    showOPInfo("播放器信息有误！！！");
  }else{
    //设备ID
    var deviceID = player.deviceID;
    //通道号
    var channel = player.channle;
    //获得预置点序号
    var index = parseInt($("#presetList").val(), 10);
    if(WebVideoCtrl.removePreset(deviceID,channel,index)){
      $("#presetList" + " option[value='" + index.toString() + "']").remove();
      $("#presetList option:last").attr("selected","selected");
    }

  }
}

function SetPreset(){
  //获得选中的窗口ID
  var winID = WebVideoCtrl.getSelectedWinID();
  //获得播放器信息
  var player = PlayerMgr.getPlayerInfo(winID);
  if(typeof player == "undefined")
  {
    showOPInfo("播放器信息有误！！！");
  }else{
    //设备ID
    var deviceID = player.deviceID;
    //通道号
    var channel = player.channle;
    //获得预置点长度
    var length = $("#presetList option").length;
    var name = "预置点" + (length + 1).toString();
    WebVideoCtrl.setPreset(deviceID,channel,length + 1,name);
  }
}















