var WebVideoCtrl = (function(e)
{
  //�¼���Ӧ�����б�
  var eventHandler = {
    selectDir:function(path){
    }
  }

  //�������
  var pluginObject;
  //��ʼ���ɹ�����
  var initSuccess;
  //�¼��ź��б�
  var SignalMap = new Map();
  //���ش���ѡ���ź�
  SignalMap.put("SelectedView",new Array());
  //�豸��Ϣ��
  var deviceInfoMap = new Map();
  //������Ϣ��
  var playerInfoMap = new Map();
  //����Э��,����,����
  var sProtocol;
  //��ǰѡ���·��
  var typePath;
  //��ǰ���ص��ļ�����
  var downloadFileName="";

  //�¼�������
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
  //�¼�������
  function handleEvent(message,callback){
    var messageObject = $.parseJSON(message);
    if(("EventName" in  messageObject))
    {
      var eventType = messageObject["EventName"];
      //���ݲ�ͬ���¼�����������
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
          //�ط�ģ�鼰������ʽ����
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
   *@description �жϲ���Ƿ�װ
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

  //������������
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
   *@description ������
   *@param{String} sContainerID ���������ID
   *@param{Num}    iWidth       ����Ŀ�
   *@param{Num}    iHeight      ����ĸ�
   *@return void
   */
  function insertPluginObject(sContainerID,iWidth,iHeight){
    //�����IE������Ļ�
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
   *@description ��ʼ�����
   *@param{String} sp    Э������
   *@param{Function} fnCallback ��ʼ���ɹ���Ļص�����
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
      //��ò��
      pluginObject = document.getElementById("dhVideo");
      //�����¼�
      pluginObject.AddEventListener("TransEvent",handleEvent);
      pluginObject.AddEventListener("FileDialogInfo",FileDialogInfoEvent);
      pluginObject.AddEventListener("InsertNetRecordFileInfo",InsertNetRecordFileInfoEvent);
      pluginObject.AddEventListener("NetPlayTimeInform",NetPlayTimeInformEvent);

      WebVideoCtrl.SetInitParams();//��Ҫ������machinenameֵ��������¼������������ʹ��

      //�ص�
      initSuccess();
    } catch (e){
      setTimeout(checkReady,500);
    }
  }

  /**
   *@description ����������Ϣ
   *@param{String} ������Ϣ
   *@return String ����������Ϣ
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
   *@description ���ô��ڵ���ʾ��Ŀ
   *@param{Num}  iNum Ҫ��ʾ����Ŀ
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
  /**���Ӧ�ô���
   *@description ���Ӧ�ô�����
   *@param{String} name ��������
   *@return ������
   */
  var getLastError = function(name){
    return pluginObject.GetLastError(name);
  }

  /**��¼�豸
   *@description ��ʼ�����
   *@param{String} sIp         �豸IP
   *@param{Num} iPort          ����˿�
   *@param{String} sUserName   �û���
   *@param{String} sPassword   ����
   *@param{Num} iRtspPort      Rtsp�˿�
   *@param{Num} iProtocol      ͨ��Э��
   *@param{Function} fnSuccess ��¼�ɹ���Ļص�����
   *@param{Function} fnFail    ��¼ʧ�ܺ�Ļص�����
   */
  var login = function(sIp,iPort,sUserName,sPassword,iProtocol){
    var ret = pluginObject.LoginDeviceEx(sIp,iPort,sUserName,sPassword,iProtocol);
    return ret;
  }

  /**
   *@description ����һ���豸��Ϣ
   *@param{Num} deviceID     �豸ID
   *@param{String} ip        �豸IP
   *@param{Num} port         ����˿�
   *@param{String} userName  �û���
   *@param{String} password  ����
   *@param{Num} rtspPort     rtsp�˿�
   *@param{Num} channelNum   ͨ������
   *@param{Num} deviceID     �豸ID
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
   *@description ����豸��Ϣ
   */
  function getDeviceInfo(ip)
  {
    var info = deviceInfoMap.get(ip);
    return info;
  }

  /**
   *@description ����һ��������
   *@param{Num} iWinID       ����ID
   *@param{Num} iDeviceID    �豸ID
   *@param{Num} iPlayerID    ������ID
   *@param{string} sIP       �豸IP
   *@param{Num} iProtocol    Э������
   *@param{Num} iChannle     ͨ����
   *@param{Num} iStreamType  ��������
   *@param{Num} iPlayerType  ���������� 0:ʵʱ����  1:����ط�
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
   *@description ���ָ���豸��ͨ������
   *@param{Num} deviceID  �豸ID
   */
  var getChannelNumber = function(deviceID){
    return pluginObject.GetChannelTotal(deviceID);
  }

  /**
   *@description �ǳ��豸
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
   *@description ѡ�е���Ƶ�����ϲ�����Ƶ
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

  //���ѡ�е���ͼ���
  var getSelectedWinIndex = function(){
    return true;
  }

  /**
   *@description �رյ�ǰѡ�д��ڵĲ�����
   */
  var closePlayer = function(iChannel){
    pluginObject.DisConnectRealVideo(iChannel);
    return true;
  }

  /**
   *@description ��ò�������Ϣ
   *@param{Num} iWinID ����ID
   */
  function getPlayerInfo(iWinID)
  {
    return playerInfoMap.get(iWinID);
  }


  /**
   *@description ���ѡ�д����ϵĲ�����ID
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

  //�����Խ�
  var startVoiceTalk = function(){
    var ret = pluginObject.ControlTalking(1);
    if(ret > 0){
      alert("stalking open seccessed��");
    }
    else{
      alert("stalking open failed��");
    }
  }

  //�رնԽ�
  var stopVoiceTalk = function(){
    pluginObject.ControlTalking(0);
  }

  var showFileBrowse = function(type){
    typePath = type;
    pluginObject.showFileBrowse();
  }

  //ѡ��·��
  var selectDirectory = function(type,path){
    pluginObject.SetConfigPath(type,path);
  }

  /**
   *@description ץȡ��ǰѡ�д����ϲ�����Ƶ��ʵʱͼƬ
   *@param{Num} iFormat �浵ͼƬ�ĸ�ʽ
   *@param{Num} sPath   ͼƬ�Ĵ洢·��
   *@param{Boolean} bOpen   ͼƬ�Ĵ洢·��
   */
  var crabOnePicture = function(picName){
    var str = '{"Protocol":"EnableVideoFunc","Params":[{"index":0,"funcName":"Snapshot", "enable":true,"picName":"'+picName+'"}]}';
    pluginObject.ProtocolPluginWithWebCall(str);
    return true;
  }

  /**
   *@description ������ǰѡ�д����ϲ�����Ƶ��¼����
   *@param{Num} iFormat ¼���ʽ
   *@param{Num} sPath   ¼��Ĵ洢·��
   */
  var startRecordingVideo = function(){
    var str = '{"Protocol":"EnableVideoFunc","Params":[{"index":0,"funcName":"Record", "enable":true}]}';
    pluginObject.ProtocolPluginWithWebCall(str);
    return true;
  }

  //ֹͣ¼��
  var stopRecordingVideo = function(){
    var str = '{"Protocol":"EnableVideoFunc","Params":[{"index":0,"funcName":"Record", "enable":false}]}';
    pluginObject.ProtocolPluginWithWebCall(str);
    return true;
  }

  //��������
  var setVolume = function(volume,cb){
  }

  //������
  var openSound = function(){
    var str = '{"Protocol":"EnableVideoFunc","Params":[{"index":0,"funcName":"Audio", "enable":true}]}';
    pluginObject.ProtocolPluginWithWebCall(str);
  }

  //�ر�����
  var closeSound = function(cb){
    var str = '{"Protocol":"EnableVideoFunc","Params":[{"index":0,"funcName":"Audio", "enable":false}]}';
    pluginObject.ProtocolPluginWithWebCall(str);
  }

  /**
   *@description �������ӷŴ�
   */
  var enableEZoom = function(){
    var str = '{"Protocol":"EnableVideoFunc","Params":[{"index":0,"funcName":"ZoomIn", "enable":true}]}';
    pluginObject.ProtocolPluginWithWebCall(str);
    return true;
  }

  /**
   *@description �رյ��ӷŴ�
   */
  var disableEZoom = function(){
    var str = '{"Protocol":"EnableVideoFunc","Params":[{"index":0,"funcName":"ZoomIn", "enable":false}]}';
    pluginObject.ProtocolPluginWithWebCall(str);
    return true;
  }
  /**
   *@description ����IPC�Խ�
   */
  var enableIpcTalk=function(){

    var strtalkMode = '{"Protocol":"IPCTalkAduioFromat","Params":{"channel":0,"fromat": {"encodeType": 2,	"nAudioBit": 16,"dwSampleRate": 16000,	"nPacketPeriod": 25}}}';
    pluginObject.ProtocolPluginWithWebCall(strtalkMode);
    var str = '{"Protocol":"EnableVideoFunc","Params":[{"index":0,"funcName":"TalkToIpc", "enable":true}]}';
    pluginObject.ProtocolPluginWithWebCall(str);
    return true;
  }
  /**
   *@description �ر�IPC�Խ�
   */
  var disableIpcTalk=function(){
    var str = '{"Protocol":"EnableVideoFunc","Params":[{"index":0,"funcName":"TalkToIpc", "enable":false}]}';
    pluginObject.ProtocolPluginWithWebCall(str);
    return true;
  }


  /**
   *@description �л���ȫ��
   *@param{Boolean}
   */
  var setFullscreen = function(){
    pluginObject.OnFullScreenClk();
    return true;
  }
  var SetInitParams=function(){
    pluginObject.SetInitParams('{"MachineName":"device","LocalChannelsNumber":' + 4 + ',"IsMultiPreviewShow":'+ false +',"MachineType":"DVR"}')
  }
  //����û�·��
  var getUserDirectory = function(type){
    return pluginObject.GetConfigPath(type);
  }

  //���ѡ�еĴ���ID
  var getSelectedWinID = function(){
    return true;
  }

  /**
   *@description ���Ʋ�����������
   *@param{Num} playerID    ������ID
   *@param{Boolean} enable  �����رձ�־
   */
  var controlAudio = function(playerID,enable){
    return true;
  }

  /**
   *@description �����ƶ�
   *@param{Num} iVerticalSpeed    ��ֱ�ٶ�
   *@param{Num} iLevelSpeed       ˮƽ�ٶ�
   *@param{Boolean} flag  ����ֹͣ��־
   */
  var moveUpperLeft = function(iChannel,iVerticalSpeed,iLevelSpeed,flag){
    pluginObject.ControlPtzEx(iChannel, 32, iVerticalSpeed, iLevelSpeed, 0, flag);
  }

  /**
   *@description �����ƶ�
   *@param{Num} iVerticalSpeed    ��ֱ�ٶ�
   *@param{Num} iLevelSpeed       ˮƽ�ٶ�
   *@param{Boolean} flag  ����ֹͣ��־
   */
  var moveUpperRight = function(iChannel,iVerticalSpeed,iLevelSpeed,flag){
    pluginObject.ControlPtzEx(iChannel, 33, iVerticalSpeed, iLevelSpeed, 0, flag);
  }

  /**
   *@description �����ƶ�
   *@param{Num} iVerticalSpeed    ��ֱ�ٶ�
   *@param{Num} iLevelSpeed       ˮƽ�ٶ�
   *@param{Boolean} flag  ����ֹͣ��־
   */
  var moveLowerLeft = function(iChannel,iVerticalSpeed,iLevelSpeed,flag){
    pluginObject.ControlPtzEx(iChannel, 34, iVerticalSpeed, iLevelSpeed, 0, flag);
  }

  /**
   *@description �����ƶ�
   *@param{Num} iVerticalSpeed    ��ֱ�ٶ�
   *@param{Num} iLevelSpeed       ˮƽ�ٶ�
   *@param{Boolean} flag  ����ֹͣ��־
   */
  var moveLowerRight = function(iChannel,iVerticalSpeed,iLevelSpeed,flag){
    pluginObject.ControlPtzEx(iChannel, 35, iVerticalSpeed, iLevelSpeed, 0, flag);
  }

  /**
   *@description ���ƶ�
   *@param{Num} iVerticalSpeed   ��ֱ�ٶ�
   *@param{Boolean} flag         ����ֹͣ��־
   */
  var moveUpwards = function(iChannel,iVerticalSpeed,flag){
    pluginObject.ControlPtzEx(iChannel, 0, 0, iVerticalSpeed, 0, flag);
  }

  /**
   *@description ���ƶ�
   *@param{Num} iVerticalSpeed   ��ֱ�ٶ�
   *@param{Boolean} flag         ����ֹͣ��־
   */
  var moveLower = function(iChannel,iVerticalSpeed,flag){
    pluginObject.ControlPtzEx(iChannel, 1, 0,iVerticalSpeed,  0, flag);
  }

  /**
   *@description ���ƶ�
   *@param{Num} iLevelSpeed   ˮƽ�ٶ�
   *@param{Boolean} flag      ����ֹͣ��־
   */
  var moveLeft = function(iChannel,iLevelSpeed,flag){
    pluginObject.ControlPtzEx(iChannel, 2, 0, iLevelSpeed, 0, flag);
  }

  /**
   *@description ���ƶ�
   *@param{Num} iLevelSpeed   ˮƽ�ٶ�
   *@param{Boolean} flag      ����ֹͣ��־
   */
  var moveRight = function(iChannel,iLevelSpeed,flag){
    pluginObject.ControlPtzEx(iChannel, 3, 0, iLevelSpeed, 0, flag);
  }

  /**
   *@description ʹ��PTZ��λ
   */
  var enablePTZLocate = function(iChannel){
    pluginObject.ControlPtzEx(iChannel,51,0,0,0,0);
  }

  /**
   *@description ��ʹ��PTZ��λ
   */
  var disablePTZLocate = function(iChannel){
    pluginObject.ControlPtzEx(iChannel,51,0,0,0,1);
  }

  /**
   *@description ���Ʊ䱶
   *@param{Num} iSpeed     ����
   *@param{Num} flag      ���ӻ���ٱ�־
   *       - 0:����
   *       - 1:����
   *@param{Boolean} flag1      ����ֹͣ��־
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
   *@description ���Ʊ佹
   *@param{Num} speed     ����
   *@param{Num} flag      ���ӻ���ٱ�־
   *       - 0:����
   *       - 1:����
   *@param{Boolean} flag1      ����ֹͣ��־
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
   *@description ���ƹ�Ȧ
   *@param{Num} speed     ����
   *@param{Num} flag      ���ӻ���ٱ�־
   *       - 0:����
   *       - 1:����
   *@param{Boolean} flag1      ����ֹͣ��־
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
   *@description ��ȡԤ�õ���Ϣ
   *@param{Num} deviceID  �豸ID
   *@param{Num} channel   ͨ����
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
   *@description ��λ��Ԥ�õ�
   *@param{Num} deviceID  �豸ID
   *@param{Num} channel   ͨ����
   *@param{Num} index     Ԥ�õ����
   *@param{Num} speed     Ԥ�õ����
   */
  var gotoPreset = function(deviceID,channel,index,speed){
    return true;
  }

  /**
   *@description ɾ��Ԥ�õ�
   *@param{Num} deviceID  �豸ID
   *@param{Num} channel   ͨ����
   *@param{Num} index     Ԥ�õ����
   */
  var removePreset = function(deviceID,channel,index){
    return true;
  }

  /**
   *@description ����Ԥ�õ�
   *@param{Num} deviceID  �豸ID
   *@param{Num} channel   ͨ����
   *@param{Num} index     Ԥ�õ����
   *@param{Num} name      Ԥ�õ�����
   */
  var setPreset = function(deviceID,channel,index,name){
    return true;
  }

  /**
   *@description ����������ͳ�Ʋ�ѯ
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
    //����豸��Ϣ
    var ODeviceInfo = getDeviceInfo(sIP);
    if(typeof ODeviceInfo == "undefined"){
      return 0;
    }
    return true;
  }

  /**
   *@description �����Ϣ����
   *@param{Num} iHandle
   *@return Num
   */
  var getTrafficDataTotalCount = function(iHandle){
    return true;
  }

  /**
   *@description �����Ϣ
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
  // ������Ƿ��Ѿ���װ��
  var iRet = WebVideoCtrl.checkPluginInstall();
  if (-1 == iRet) {
    alert("����δ��װ�������˫��������Ŀ¼���µ�Package���webplugin.exe���а�װ��");
    return;
  }
});





