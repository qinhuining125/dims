var WebVideoCtrl = (function(e)
{
  //事件响应函数列表
  var eventHandler = {
    selectDir:function(path){
    }
  }

  //插件对象
  var pluginObject;
  //初始化成功函数
  var initSuccess;
  //事件信号列表
  var SignalMap = new Map();
  //挂载窗口选中信号
  SignalMap.put("SelectedView",new Array());
  //设备信息表
  var deviceInfoMap = new Map();
  //播放信息表
  var playerInfoMap = new Map();
  //网络协议,二代,三代
  var sProtocol;
  //当前选择的路径
  var typePath;
  //当前下载的文件名称
  var downloadFileName="";

  //事件处理函数
  function FileDialogInfoEvent(strFilePath, strExt){
    if(typePath==2){
      $("#LiveRecord").val(strFilePath);
    }
    else{
      $("#LiveSnapshot").val(strFilePath);
    }
  }

  function InsertNetRecordFileInfoEvent(nChannel, nEnd, strValue){
    if(strValue != ''){
      var recInfo = [];
      recInfo = strValue.split(':');
      addRecInfoToTable(recInfo);
    }
  }

  function NetPlayTimeInformEvent(strTime){ //strTime:00112349|
    timeArray=strTime.split('|');
    var time=timeArray[0].substr(2,2)+":"+timeArray[0].substr(4,2)+":"+timeArray[0].substr(6,2);
    $('#playtime').val(time);
    if(strTime=="")
    {
      pluginObject.StopPlayBack();
      $('#playtime').val("00:00:00");
    }
  }

  function tagscheck(a){
    var trList=$("#pfile_rec_tbody").children("tr");
    for(i=0;i<trList.length;i++)
    {
      if(a==trList[i]){
        trList[i].style.background="#ccc"
      }else{
        trList[i].style.background=""
      }
    }
  }
  function addRecInfoToTable(recInfo){
    for(var i = 0; i < recInfo.length; i++){
      var tmpInfo = recInfo[i];
      if(tmpInfo == '') continue;
      var recArry = tmpInfo.split('-');
      var time = getFormatTimeStr(recArry[0]);
      var channel = recArry[5] - 0 + 1;
      var $tr=$("<tr onclick='WebVideoCtrl.tagscheck(this)'>"+"<td>"+recArry[2]+'KB'+"</td>"+"<td>"+time[0]+"</td>"+"<td>"+time[1]+"</td>"+"<td>"+'D'+channel+"</td>"+"</tr>");
      $('#pfile_rec_tbody').append($tr);
    }
    var trList=$("#pfile_rec_tbody").children("tr");
    trList[0].style.background="#007eff";
  }

  function getFormatTimeStr(time){
    var st = time.slice(0, 14);
    var et = time.slice(14);
    var convertTime = function(str) {
      return str.slice(0, 4)+'-'+str.slice(4, 6)+'-'+str.slice(6, 8)+' '+str.slice(8, 10)+':'+str.slice(10, 12)+':'+str.slice(12, 14);
    }
    return [convertTime(st), convertTime(et)];
  }
  //事件处理函数
  function handleEvent(message,callback){
    var messageObject = $.parseJSON(message);
    if(("EventName" in  messageObject))
    {
      var eventType = messageObject["EventName"];
      //根据不同的事件类型来处理
      if("DownloadByTimePos" == eventType){
        var pos = messageObject["EventParam"]["pos"];
        var speed = messageObject["EventParam"]["speed"];
        var end = messageObject["EventParam"]["end"];
        $("#loaddownPos").val(pos);
        $("#loaddownSpeed").val(speed);
        if(end==true){
          $("#loaddownEnd").val("YES");
          $("#loaddownPos").val("100");
        }else{
          $("#loaddownEnd").val("NO");
        }
      }else if("byTimeDownFileName" == eventType){
        downloadFileName = messageObject["EventParam"]["FileName"];
        var tips = "file name:"+$("#LiveRecord").val()+downloadFileName;
        $("#fileName").val(tips);
      }else if("DownloadFileTimeLenth"== eventType){
        $("#loadFileLenth").val(messageObject["EventParam"]["Lenth"]);
      }else if("QueryItemInfo" == eventType){
        var end = messageObject["EventParam"]["LastItem"];
        var ItemTotal = messageObject["EventParam"]["ItemTotal"];
        if(end==true && ItemTotal==0)
        {
          //回放模块及下载样式控制
          $('#startPlay').prop('disabled', true);
          $('#stopPlay').prop('disabled', true);
          $('#pause').prop('disabled', true);
          $('#resume').prop('disabled', true);
          $('#playspeed').prop('disabled', true);
          $('#downloadField').prop('disabled', true);
        }else{
          $('#startPlay').prop('disabled', false);
          $('#stopPlay').prop('disabled', false);
          $('#pause').prop('disabled', false);
          $('#resume').prop('disabled', false);
          $('#playspeed').prop('disabled', false);
          $('#downloadField').prop('disabled', false);
        }
      }
    }else if(("ChnlInfo" in  messageObject)){
      var channelNum = messageObject["ChnlInfo"]["ChanNum"];
      if(channelNum==0){
        $('#previewField').prop('disabled', true);
        $('#audioVideoControl').prop('disabled', true);
      }else{
        $('#previewField').prop('disabled', false);
        $('#audioVideoControl').prop('disabled', false);
      }
      for(var i=0;i<channelNum;i++){
        $("#playbackChannel").append("<option value='"+i+"'>"+"C"+(i+1)+"</option>");
        $("#channels").append("<option value='"+i+"'>"+"C"+(i+1)+"</option>");
      }
    callback();
    }
  }

  /**
   *@description 判断插件是否安装
   *@return Boolean
   */
  var checkPluginInstall = function()
  {
    var e = false;
    if(browser().msie)
    {
      try{
        new ActiveXObject("WebActiveEXE.Plugin.1");
        e = true;
      }
      catch(n)
      {
        e = false;
      }
    }
    else
    {
      for(var r=0,s=navigator.mimeTypes.length;s>r;r++)
      {
        if("application/media-plugin-version-3.1.0.2"==navigator.mimeTypes[r].type.toLowerCase())
        {
          e = true;
          break;
        }
      }
    }
    return e;
  };

  //获得浏览器类型
  var browser = function(){
    var e=/(chrome)[ \/]([\w.]+)/,
        t=/(safari)[ \/]([\w.]+)/,
        n=/(opera)(?:.*version)?[ \/]([\w.]+)/,
        r=/(msie) ([\w.]+)/,
        s=/(trident.*rv:)([\w.]+)/,
        o=/(mozilla)(?:.*? rv:([\w.]+))?/,
        i=navigator.userAgent.toLowerCase(),
        a=e.exec(i)||t.exec(i)||n.exec(i)||r.exec(i)||s.exec(i)||i.indexOf("compatible")<0&&o.exec(i)||["unknow","0"];
    a.length>0&&a[1].indexOf("trident")>-1&&(a[1]="msie");
    var c={};
    return c[a[1]]=!0,c.version=a[2],c
  }

  /**
   *@description 插入插件
   *@param{String} sContainerID 插件的容器ID
   *@param{Num}    iWidth       插件的宽
   *@param{Num}    iHeight      插件的高
   *@return void
   */
  function insertPluginObject(sContainerID,iWidth,iHeight){
    //如果是IE浏览器的话
    if (browser().msie) {
      var sSize = " width=" + "\"" + iWidth.toString() + "%" + "\"" + " height=" + "\"" + iHeight.toString() + "%" + "\"";
      var sHtmlValue = "<object classid=\"CLSID:7F9063B6-E081-49DB-9FEC-D72422F2727F\" codebase=\"webrec.cab\""  + sSize + "id=\"dhVideo\">" + "</object>"
      $("#" + sContainerID).html(sHtmlValue);
    } else {
      var sSize = " width=" + "\"" + iWidth.toString() + "%" + "\"" + " height=" + "\"" + iHeight.toString() + "%" + "\"";
      var sHtmlValue = "<object type=\"application/media-plugin-version-3.1.0.2\"" + sSize + "id=\"dhVideo\">" + "</object>";
      $("#" + sContainerID).html(sHtmlValue);
    }
    return true;
  }

  /**
   *@description 初始化插件
   *@param{String} sp    协议类型
   *@param{Function} fnCallback 初始化成功后的回调函数
   */
  var initPlugin = function(sp,fnCallback){
    initSuccess = fnCallback;
    sProtocol = sp;
    checkReady();
    return true;
  }

  function checkReady(){

    pluginObject = document.getElementById("dhVideo");
    try {
      //获得插件
      pluginObject = document.getElementById("dhVideo");
      //监听事件
      pluginObject.AddEventListener("TransEvent",handleEvent);
      pluginObject.AddEventListener("FileDialogInfo",FileDialogInfoEvent);
      pluginObject.AddEventListener("InsertNetRecordFileInfo",InsertNetRecordFileInfoEvent);
      pluginObject.AddEventListener("NetPlayTimeInform",NetPlayTimeInformEvent);

      WebVideoCtrl.SetInitParams();//主要是配置machinename值，方便在录像下载名称中使用

      //回调
      initSuccess();
    } catch (e){
      setTimeout(checkReady,500);
    }
  }

  /**
   *@description 解析错误信息
   *@param{String} 错误信息
   *@return String 错误描述信息
   */
  var parseError = function(errorInfo){
    var errorObject = $.parseJSON(errorInfo);
    if(("error" in  errorObject))
    {
      return errorObject["error"];
    }
  };

  var QueryRecordInfoByTimeEx = function(str){
    pluginObject.QueryRecordInfoByTimeEx(str);
  };
  /**
   *@description 设置窗口的显示数目
   *@param{Num}  iNum 要显示的数目
   *@return Boolean
   */
  var setSplitNum = function(iNum){
    if (browser().msie){
      pluginObject.put_lVideoWindNum(iNum);
    }else{
      pluginObject.SetMonitorShowWinNumber(iNum);
    }
  }

  var SetPlayBackDirection=function(){
    pluginObject.SetPlayBackDirection(true);
  }

  var setPlaySpeed=function(speed){
    pluginObject.SpeedPlayBack(speed);
  }
  var setDelayTime = function(level){
    pluginObject.SetAdjustFluency(level);
  }
  /**获得应用错误
   *@description 获得应用错误码
   *@param{String} name 方法名称
   *@return 错误码
   */
  var getLastError = function(name){
    return pluginObject.GetLastError(name);
  }

  /**登录设备
   *@description 初始化插件
   *@param{String} sIp         设备IP
   *@param{Num} iPort          服务端口
   *@param{String} sUserName   用户名
   *@param{String} sPassword   密码
   *@param{Num} iRtspPort      Rtsp端口
   *@param{Num} iProtocol      通信协议
   *@param{Function} fnSuccess 登录成功后的回调函数
   *@param{Function} fnFail    登录失败后的回调函数
   */
  var login = function(sIp,iPort,sUserName,sPassword,iProtocol){
    var ret = pluginObject.LoginDeviceEx(sIp,iPort,sUserName,sPassword,iProtocol);
    return ret;
  }

  /**
   *@description 插入一个设备信息
   *@param{Num} deviceID     设备ID
   *@param{String} ip        设备IP
   *@param{Num} port         服务端口
   *@param{String} userName  用户名
   *@param{String} password  密码
   *@param{Num} rtspPort     rtsp端口
   *@param{Num} channelNum   通道总数
   *@param{Num} deviceID     设备ID
   */
  function insertDeviceInfo(ip,port,userName,password,rtspPort,protocol,channelNum,deviceID)
  {
    var info = {
      ip:ip,
      port:port,
      userName:userName,
      password:password,
      rtspPort:rtspPort,
      channelNum:channelNum,
      deviceID:deviceID,
      protocol:protocol
    }
    deviceInfoMap.put(ip,info);
  }

  /**
   *@description 获得设备信息
   */
  function getDeviceInfo(ip)
  {
    var info = deviceInfoMap.get(ip);
    return info;
  }

  /**
   *@description 插入一个播放器
   *@param{Num} iWinID       窗口ID
   *@param{Num} iDeviceID    设备ID
   *@param{Num} iPlayerID    播放器ID
   *@param{string} sIP       设备IP
   *@param{Num} iProtocol    协议类型
   *@param{Num} iChannle     通道号
   *@param{Num} iStreamType  码流类型
   *@param{Num} iPlayerType  播放器类型 0:实时监视  1:网络回放
   */
  function insertPlayer(iWinID,iDeviceID,iPlayerID,sIP,iProtocol,iChannle,iStreamType,iPlayerType)
  {
    var info = {
      winID:iWinID,
      deviceID:iDeviceID,
      ip:sIP,
      channle:iChannle,
      streamType:iStreamType,
      protocol:iProtocol,
      playerID:iPlayerID,
      type:iPlayerType
    }
    playerInfoMap.put(iWinID,info);
  }


  /**
   *@description 获得指定设备的通道总数
   *@param{Num} deviceID  设备ID
   */
  var getChannelNumber = function(deviceID){
    return pluginObject.GetChannelTotal(deviceID);
  }

  /**
   *@description 登出设备
   *@param{String} ip
   *@return Boolean
   */
  var logout = function(){
    pluginObject.LogoutDevice();
  }

  var EnablePreviewDBClickFullSreen = function(){
    var str = '{"Protocol":"EnablePreviewDBClickFullSreen","Params":{"Enable":true}}';
    pluginObject.ProtocolPluginWithWebCall(str);
  }
  /**
   *@description 选中的视频窗口上播放视频
   *@param{String} sIP
   *@param{Num} iChannel
   *@param{Num} iStream
   *@param{Function} fnSuccess
   *@param{Function} fnFail
   *@return Num
   */
  var connectRealVideo = function(iChannel,iStream){
    pluginObject.SetModuleMode(1);
    pluginObject.ConnectRealVideo(iChannel,iStream);
  }

  var connectRealVideoEx = function(iChannel,iStream,iWinID){
    pluginObject.SetModuleMode(1);
    pluginObject.ConnectRealVideoEx(iChannel,iStream,iWinID);
  }

  //获得选中的视图序号
  var getSelectedWinIndex = function(){
    return true;
  }

  /**
   *@description 关闭当前选中窗口的播放器
   */
  var closePlayer = function(iChannel){
    pluginObject.DisConnectRealVideo(iChannel);
    return true;
  }

  /**
   *@description 获得播放器信息
   *@param{Num} iWinID 窗口ID
   */
  function getPlayerInfo(iWinID)
  {
    return playerInfoMap.get(iWinID);
  }


  /**
   *@description 获得选中窗口上的播放器ID
   */
  function getSelectedPlayerID()
  {
    var iWinID = WebVideoCtrl.getSelectedWinID();
    var info = playerInfoMap.get(iWinID);
    if(typeof info != "undefined")
    {
      return info.playerID;
    }else{
      return 0;
    }
  }

  //语音对讲
  var startVoiceTalk = function(){
    var ret = pluginObject.ControlTalking(1);
    if(ret > 0){
      alert("stalking open seccessed！");
    }
    else{
      alert("stalking open failed！");
    }
  }

  //关闭对讲
  var stopVoiceTalk = function(){
    pluginObject.ControlTalking(0);
  }

  var showFileBrowse = function(type){
    typePath = type;
    pluginObject.showFileBrowse();
  }

  //选择路径
  var selectDirectory = function(type,path){
    pluginObject.SetConfigPath(type,path);
  }

  /**
   *@description 抓取当前选中窗口上播放视频的实时图片
   *@param{Num} iFormat 存档图片的格式
   *@param{Num} sPath   图片的存储路径
   *@param{Boolean} bOpen   图片的存储路径
   */
  var crabOnePicture = function(picName){
    var str = '{"Protocol":"EnableVideoFunc","Params":[{"index":0,"funcName":"Snapshot", "enable":true,"picName":"'+picName+'"}]}';
    pluginObject.ProtocolPluginWithWebCall(str);
    return true;
  }

  /**
   *@description 开启当前选中窗口上播放视频的录像功能
   *@param{Num} iFormat 录像格式
   *@param{Num} sPath   录像的存储路径
   */
  var startRecordingVideo = function(){
    var str = '{"Protocol":"EnableVideoFunc","Params":[{"index":0,"funcName":"Record", "enable":true}]}';
    pluginObject.ProtocolPluginWithWebCall(str);
    return true;
  }

  //停止录像
  var stopRecordingVideo = function(){
    var str = '{"Protocol":"EnableVideoFunc","Params":[{"index":0,"funcName":"Record", "enable":false}]}';
    pluginObject.ProtocolPluginWithWebCall(str);
    return true;
  }

  //设置音量
  var setVolume = function(volume,cb){
  }

  //打开声音
  var openSound = function(){
    var str = '{"Protocol":"EnableVideoFunc","Params":[{"index":0,"funcName":"Audio", "enable":true}]}';
    pluginObject.ProtocolPluginWithWebCall(str);
  }

  //关闭声音
  var closeSound = function(cb){
    var str = '{"Protocol":"EnableVideoFunc","Params":[{"index":0,"funcName":"Audio", "enable":false}]}';
    pluginObject.ProtocolPluginWithWebCall(str);
  }

  /**
   *@description 开启电子放大
   */
  var enableEZoom = function(){
    var str = '{"Protocol":"EnableVideoFunc","Params":[{"index":0,"funcName":"ZoomIn", "enable":true}]}';
    pluginObject.ProtocolPluginWithWebCall(str);
    return true;
  }

  /**
   *@description 关闭电子放大
   */
  var disableEZoom = function(){
    var str = '{"Protocol":"EnableVideoFunc","Params":[{"index":0,"funcName":"ZoomIn", "enable":false}]}';
    pluginObject.ProtocolPluginWithWebCall(str);
    return true;
  }
  /**
   *@description 开启IPC对讲
   */
  var enableIpcTalk=function(){

    var strtalkMode = '{"Protocol":"IPCTalkAduioFromat","Params":{"channel":0,"fromat": {"encodeType": 2,	"nAudioBit": 16,"dwSampleRate": 16000,	"nPacketPeriod": 25}}}';
    pluginObject.ProtocolPluginWithWebCall(strtalkMode);
    var str = '{"Protocol":"EnableVideoFunc","Params":[{"index":0,"funcName":"TalkToIpc", "enable":true}]}';
    pluginObject.ProtocolPluginWithWebCall(str);
    return true;
  }
  /**
   *@description 关闭IPC对讲
   */
  var disableIpcTalk=function(){
    var str = '{"Protocol":"EnableVideoFunc","Params":[{"index":0,"funcName":"TalkToIpc", "enable":false}]}';
    pluginObject.ProtocolPluginWithWebCall(str);
    return true;
  }


  /**
   *@description 切换到全屏
   *@param{Boolean}
   */
  var setFullscreen = function(){
    pluginObject.OnFullScreenClk();
    return true;
  }
  var SetInitParams=function(){
    pluginObject.SetInitParams('{"MachineName":"device","LocalChannelsNumber":' + 4 + ',"IsMultiPreviewShow":'+ false +',"MachineType":"DVR"}')
  }
  //获得用户路径
  var getUserDirectory = function(type){
    return pluginObject.GetConfigPath(type);
  }

  //获得选中的窗口ID
  var getSelectedWinID = function(){
    return true;
  }

  /**
   *@description 控制播放器的声音
   *@param{Num} playerID    播放器ID
   *@param{Boolean} enable  开启关闭标志
   */
  var controlAudio = function(playerID,enable){
    return true;
  }

  /**
   *@description 左上移动
   *@param{Num} iVerticalSpeed    垂直速度
   *@param{Num} iLevelSpeed       水平速度
   *@param{Boolean} flag  开启停止标志
   */
  var moveUpperLeft = function(iChannel,iVerticalSpeed,iLevelSpeed,flag){
    pluginObject.ControlPtzEx(iChannel, 32, iVerticalSpeed, iLevelSpeed, 0, flag);
  }

  /**
   *@description 右上移动
   *@param{Num} iVerticalSpeed    垂直速度
   *@param{Num} iLevelSpeed       水平速度
   *@param{Boolean} flag  开启停止标志
   */
  var moveUpperRight = function(iChannel,iVerticalSpeed,iLevelSpeed,flag){
    pluginObject.ControlPtzEx(iChannel, 33, iVerticalSpeed, iLevelSpeed, 0, flag);
  }

  /**
   *@description 左下移动
   *@param{Num} iVerticalSpeed    垂直速度
   *@param{Num} iLevelSpeed       水平速度
   *@param{Boolean} flag  开启停止标志
   */
  var moveLowerLeft = function(iChannel,iVerticalSpeed,iLevelSpeed,flag){
    pluginObject.ControlPtzEx(iChannel, 34, iVerticalSpeed, iLevelSpeed, 0, flag);
  }

  /**
   *@description 右下移动
   *@param{Num} iVerticalSpeed    垂直速度
   *@param{Num} iLevelSpeed       水平速度
   *@param{Boolean} flag  开启停止标志
   */
  var moveLowerRight = function(iChannel,iVerticalSpeed,iLevelSpeed,flag){
    pluginObject.ControlPtzEx(iChannel, 35, iVerticalSpeed, iLevelSpeed, 0, flag);
  }

  /**
   *@description 上移动
   *@param{Num} iVerticalSpeed   垂直速度
   *@param{Boolean} flag         开启停止标志
   */
  var moveUpwards = function(iChannel,iVerticalSpeed,flag){
    pluginObject.ControlPtzEx(iChannel, 0, 0, iVerticalSpeed, 0, flag);
  }

  /**
   *@description 下移动
   *@param{Num} iVerticalSpeed   垂直速度
   *@param{Boolean} flag         开启停止标志
   */
  var moveLower = function(iChannel,iVerticalSpeed,flag){
    pluginObject.ControlPtzEx(iChannel, 1, 0,iVerticalSpeed,  0, flag);
  }

  /**
   *@description 左移动
   *@param{Num} iLevelSpeed   水平速度
   *@param{Boolean} flag      开启停止标志
   */
  var moveLeft = function(iChannel,iLevelSpeed,flag){
    pluginObject.ControlPtzEx(iChannel, 2, 0, iLevelSpeed, 0, flag);
  }

  /**
   *@description 右移动
   *@param{Num} iLevelSpeed   水平速度
   *@param{Boolean} flag      开启停止标志
   */
  var moveRight = function(iChannel,iLevelSpeed,flag){
    pluginObject.ControlPtzEx(iChannel, 3, 0, iLevelSpeed, 0, flag);
  }

  /**
   *@description 使能PTZ定位
   */
  var enablePTZLocate = function(iChannel){
    pluginObject.ControlPtzEx(iChannel,51,0,0,0,0);
  }

  /**
   *@description 非使能PTZ定位
   */
  var disablePTZLocate = function(iChannel){
    pluginObject.ControlPtzEx(iChannel,51,0,0,0,1);
  }

  /**
   *@description 控制变倍
   *@param{Num} iSpeed     倍数
   *@param{Num} flag      增加或减少标志
   *       - 0:增加
   *       - 1:减少
   *@param{Boolean} flag1      开启停止标志
   */
  var controlZoom = function(iChannel,iSpeed,flag,flag1){
    if(flag==0){
      pluginObject.ControlPtzEx(iChannel,4,0,iSpeed,0,flag1);
    }
    else{
      pluginObject.ControlPtzEx(iChannel,5,0,iSpeed,0,flag1);
    }
  }

  /**
   *@description 控制变焦
   *@param{Num} speed     倍数
   *@param{Num} flag      增加或减少标志
   *       - 0:增加
   *       - 1:减少
   *@param{Boolean} flag1      开启停止标志
   */
  var controlFocus = function(iChannel,speed,flag,flag1){
    if(flag==0){
      pluginObject.ControlPtzEx(iChannel,7,0,speed,0,flag1);
    }
    else{
      pluginObject.ControlPtzEx(iChannel,6,0,speed,0,flag1);
    }
  }

  /**
   *@description 控制光圈
   *@param{Num} speed     倍数
   *@param{Num} flag      增加或减少标志
   *       - 0:增加
   *       - 1:减少
   *@param{Boolean} flag1      开启停止标志
   */
  var controlAperture = function(iChannel,speed,flag,flag1){
    if(flag==0){
      pluginObject.ControlPtzEx(iChannel,8,0,speed,0,flag1);
    }
    else{
      pluginObject.ControlPtzEx(iChannel,9,0,speed,0,flag1);
    }
  }

  /**
   *@description 获取预置点信息
   *@param{Num} deviceID  设备ID
   *@param{Num} channel   通道号
   */
  var getPresets = function(deviceID,channel,cb){
    var objID;
    var info;
    if(info != ""){
      DemoUI.clearPresets();
      var dataObject = $.parseJSON(info);
      $.each(dataObject,function(i,item){
        cb(item.Index,item.Name);
      });
    }
  }

  /**
   *@description 定位到预置点
   *@param{Num} deviceID  设备ID
   *@param{Num} channel   通道号
   *@param{Num} index     预置点序号
   *@param{Num} speed     预置点序号
   */
  var gotoPreset = function(deviceID,channel,index,speed){
    return true;
  }

  /**
   *@description 删除预置点
   *@param{Num} deviceID  设备ID
   *@param{Num} channel   通道号
   *@param{Num} index     预置点序号
   */
  var removePreset = function(deviceID,channel,index){
    return true;
  }

  /**
   *@description 设置预置点
   *@param{Num} deviceID  设备ID
   *@param{Num} channel   通道号
   *@param{Num} index     预置点序号
   *@param{Num} name      预置点名称
   */
  var setPreset = function(deviceID,channel,index,name){
    return true;
  }

  /**
   *@description 开启人流量统计查询
   *@param{String} sIP
   *@param{Num} iChannel
   *@param{String} sStartTime
   *@param{String} sEndTime
   *@param{Num} iRuleType
   *@param{Num} iSpan
   *@param{Num} iMinStayTime
   *@return Num
   */
  var startTrafficDataQuery = function(sIP,iChannel,sStartTime,sEndTime,iRuleType,iSpan,iMinStayTime){
    //获得设备信息
    var ODeviceInfo = getDeviceInfo(sIP);
    if(typeof ODeviceInfo == "undefined"){
      return 0;
    }
    return true;
  }

  /**
   *@description 获得信息条数
   *@param{Num} iHandle
   *@return Num
   */
  var getTrafficDataTotalCount = function(iHandle){
    return true;
  }

  /**
   *@description 获得信息
   *@param{Num} iHandle
   *@return Num
   */
  var queryTrafficData = function(iHandle,iBeginIndex,iCount){
    return true;
  }

  var stopTrafficDataQuery = function(iHandle){
    return true;
  }

  var stopDownloadFile=function(){
    pluginObject.StopDownloadByTime();
  }

  var GetFileTimeLenth=function(){
    //downloadFileName = "device_ch2_main_20190228091817_20190228092619.mp4";
    var end = $("#loaddownEnd").val()
    if(end != "YES")
    {
      alert("download not finished,please wait a minitues,then get file timeLenth!");
    }
    if(downloadFileName ==""){alert("file name is empty!");}
    var str = '{"Protocol":"GetFileTimeLenth","Params":{"FileName":"'+downloadFileName+'"}}';
    pluginObject.ProtocolPluginWithWebCall(str);
  }

  var downloadFile=function(){
    var format=$('#downLoadFormat').find("option:selected").text();
    var trList=$("#pfile_rec_tbody").children("tr");
    for(i=0;i<trList.length;i++)
    {
      if(trList[i].style.background=="rgb(204, 204, 204)"||trList[i].style.background=="#ccc")
      {
        var startTime=$(trList[i]).find('td').eq(1).text();
        var endTime=$(trList[i]).find('td').eq(2).text();
        var channelTxt = $(trList[i]).find('td').eq(3).text().substr(1);
        var channel = parseInt(channelTxt)-1;
        var streamType=parseInt($("#record_streamtype").val(), 10);
        pluginObject.DownloadRecordByTimeEx(channel,streamType,startTime,endTime,$("#LiveRecord").val()+'\\',format);
        break;
      }
    }
  }

  var PausePlayBack=function(){
    pluginObject.PausePlayBack();
  }

  var ResumePlayback=function(){
    var str = '{"Protocol":"ResumePlayback","Params":{}}';
    pluginObject.ProtocolPluginWithWebCall(str);
  }

  var StopPlayBack=function(){
    pluginObject.StopPlayBack();
  }

  var PlayBackByTime = function(){
    //pluginObject.SetWinBindedChannel(4,0,0,3);
    pluginObject.SetModuleMode(4);
    var trList=$("#pfile_rec_tbody").children("tr");
    for(i=0;i<trList.length;i++)
    {
      if(trList[i].style.background=="rgb(204, 204, 204)"||trList[i].style.background=="#ccc")
      {
        var startTime=$(trList[i]).find('td').eq(1).text();
        var endTime=$(trList[i]).find('td').eq(2).text();
        var channelTxt = $(trList[i]).find('td').eq(3).text().substr(1);
        var channel = parseInt(channelTxt)-1;
        pluginObject.SetWinBindedChannel(1,0,channel,channel);
        var str = '{"Protocol":"RecordPlayByTime","Params":{"index": 0,"startTime":"'+startTime+'","endTime":"'+endTime+'"}}';
        pluginObject.ProtocolPluginWithWebCall(str);
        break;
      }
    }
  }

  return {
    checkPluginInstall:checkPluginInstall,
    browser:browser,
    insertPluginObject:insertPluginObject,
    initPlugin:initPlugin,
    setSplitNum:setSplitNum,
    login:login,
    getDeviceInfo:getDeviceInfo,
    logout:logout,
    connectRealVideo:connectRealVideo,
    connectRealVideoEx:connectRealVideoEx,
    getChannelNumber:getChannelNumber,
    closePlayer:closePlayer,
    getSelectedPlayerID:getSelectedPlayerID,
    getPlayerInfo:getPlayerInfo,
    getSelectedWinIndex:getSelectedWinIndex,
    startVoiceTalk:startVoiceTalk,
    stopVoiceTalk:stopVoiceTalk,
    selectDirectory:selectDirectory,
    crabOnePicture:crabOnePicture,
    startRecordingVideo:startRecordingVideo,
    stopRecordingVideo:stopRecordingVideo,
    setVolume:setVolume,
    openSound:openSound,
    closeSound:closeSound,
    enableEZoom:enableEZoom,
    disableEZoom:disableEZoom,
    setFullscreen:setFullscreen,
    getUserDirectory:getUserDirectory,
    getSelectedWinID:getSelectedWinID,
    controlAudio:controlAudio,
    moveUpperLeft:moveUpperLeft,
    moveUpperRight:moveUpperRight,
    moveLowerLeft:moveLowerLeft,
    moveLowerRight:moveLowerRight,
    moveLeft:moveLeft,
    moveRight:moveRight,
    moveUpwards:moveUpwards,
    moveLower:moveLower,
    enablePTZLocate:enablePTZLocate,
    disablePTZLocate:disablePTZLocate,
    controlZoom:controlZoom,
    controlFocus:controlFocus,
    controlAperture:controlAperture,
    getPresets:getPresets,
    gotoPreset:gotoPreset,
    removePreset:removePreset,
    setPreset:setPreset,
    startTrafficDataQuery:startTrafficDataQuery,
    getTrafficDataTotalCount:getTrafficDataTotalCount,
    queryTrafficData:queryTrafficData,
    stopTrafficDataQuery:stopTrafficDataQuery,
    setDelayTime:setDelayTime,
    showFileBrowse:showFileBrowse,
    selectDirectory:selectDirectory,
    QueryRecordInfoByTimeEx:QueryRecordInfoByTimeEx,
    tagscheck:tagscheck,
    PlayBackByTime:PlayBackByTime,
    downloadFile:downloadFile,
    stopDownloadFile:stopDownloadFile,
    StopPlayBack:StopPlayBack,
    PausePlayBack:PausePlayBack,
    ResumePlayback:ResumePlayback,
    setPlaySpeed:setPlaySpeed,
    SetPlayBackDirection:SetPlayBackDirection,
    SetInitParams:SetInitParams,
    enableIpcTalk:enableIpcTalk,
    disableIpcTalk:disableIpcTalk,
    EnablePreviewDBClickFullSreen:EnablePreviewDBClickFullSreen,
    GetFileTimeLenth:GetFileTimeLenth
  };

})(this);

$(function () {
  // 检查插件是否已经安装过
  var iRet = WebVideoCtrl.checkPluginInstall();
  if (-1 == iRet) {
    alert("您还未安装过插件，双击开发包目录里下的Package里的webplugin.exe进行安装！");
    return;
  }
});





