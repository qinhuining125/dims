<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="utf-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <meta name="renderer" content="webkit"/>
  <title>纪检监察管理系统</title>
  <link href="${global.staticPath!}static/css/login.min.css" rel="stylesheet"/>
  <style>
    .bg {
      background-image: url('${global.staticPath!}static/img/bg.jpg') !important;
      width: 100%;
      background-repeat: no-repeat;
      background-size: 100% 100%;
    }

    .u-bg-main {
      border-radius: 10px;
      background-color: rgba(255, 255, 255, 0.9) !important;
      height: 100%;
      box-shadow: 0 10px 10px rgba(0, 0, 0, 0.5);
    }

    .o-block__cell mar_top {
      margin-bottom: 5% !important;
    }

    .c-control__input {
      background-color: white !important;
      border: 1px solid #ccc !important;
      margin: 4% 4%;
      width: 80% !important;
      height: 100%;
      border-radius: 5px;
      padding: 1rem;
      font-size: 15px;
    }

    .c-control__code {
      background-color: white !important;
      border: 1px solid #ccc !important;
      margin: 4% 4%;
      width: 50% !important;
      height: 100%;
      border-radius: 5px;
      padding: 1rem;

    }

    .c-control__input-box {
      text-align: right;
    }

    .c-button--block {
      margin-top: 5%;
      width: 92% !important;
      border-radius: 5px;
      font-size: 16px;
      font-weight: bold;
    }

    #code_img {
      text-align: center;
    }

    .code {
      text-align: right;
    }

    input:-webkit-autofill {
      -webkit-text-fill-color: black !important;
      box-shadow: 0 0 0px 1000px white inset !important;
    }

    #code_img {
      margin-right: 17px;
      width: 120px;
      text-align: center;
    }
  </style>
</head>
<body>
<div class="o-block u-text-center u-fullscreen u-overflow-auto bg">
  <div class="o-block__cell u-flex u-flexGrow-1 u-mb-medium u-p-big"
       style="position:relative;z-index:10">

    <div class="u-m-auto">
      <div
          class="c-formCard o-block u-collapse u-bg-main u-shadow-medium u-p-medium u-pt-none _u-width-100p _u-mb-small"
          style="width: 550px;">
        <div class="o-block__cell">
        </div>
        <form action="login.html" method="post" autocomplete="off">
          <div class="o-block__cell">
            <div class=" u-width-100p">
              <div class="c-control__input-box">
                <label for="username">用户名</label>
                <input class="c-control__input" type="text" id="uid" name="uid" placeholder="请输入用户名"
                       autocomplete="off"/>
              </div>
            </div>
          </div>
          <div class="o-block__cell">
            <div class=" u-width-100p">
              <div class="c-control__input-box">
                <label for="pwd">密码</label>
                <input class="c-control__input" type="password" id="pwd" name="pwd"
                       placeholder="请输入密码"
                       autocomplete="new-password"/>
              </div>
            </div>
          </div>
            <#if captchaShow>
            <#--<div class="code">-->
            <#--<input class="c-control__input code_text" type="text" name="code" id="code"-->
            <#--placeholder="验证码" autocomplete="off"/>-->
            <#--<div class="img" style="visibility: hidden; cursor:pointer">-->
            <#--<img id="code_img"-->
            <#--src="data:image/gif;base64,R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw=="-->
            <#--data-src="hty-captcha" title="点我换图" alt="验证码"/>-->
            <#--</div>-->
            <#--</div>-->
              <div class="o-block__cell">
                <div class=" u-width-100p">
                  <div class="c-control__input-box">
                    <input class="c-control__code code_text " type="text" name="code" id="code"
                           placeholder="验证码" autocomplete="off"/>
                    <div class="img"
                         style="visibility: hidden; cursor:pointer;display: inline-block">
                      <img id="code_img"
                           src="data:image/gif;base64,R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw=="
                           data-src="hty-captcha" title="点我换图" alt="验证码"/>
                    </div>
                  </div>
                </div>
              </div>
            </#if>
          <div class="o-block__cell error_msg" id="error_msg">${msg!}</div>
          <div class="o-block__cell" id="login_btn">
            <input type="submit" class="c-button c-button--block" value="登录"/></div>
        </form>
      </div>
    </div>
  </div>
    <#--<div id="canvasSwitch" data-toggle="tooltip" data-placement="left" title=""-->
    <#--data-original-title="如果您的电脑配置较低，请关闭背景特效">关闭背景特效-->
    <#--</div>-->
    <#--<iframe id="canvasFrame" sandbox="allow-scripts allow-same-origin"></iframe>-->
</div>
<script>
  <#-- -->
  var path = "${global.staticPath!}static/login/";
  var $$ = function (id) {
    return document.getElementById(id);
  };

  function dtbg(canvasObj) {
    try {
      if (/Android|webOS|iPhone|iPad|iPod|BlackBerry/i["test"](navigator["userAgent"])) {
        $$("canvasSwitch").style.display = "none";
      } else {
        canvasObj.setAttribute("src", path + "b" + getRandom(11) + ".html");
      }
    } catch (e) {
    }

    function getRandom(num) {
      // return Math.floor(Math.random() * num) + 1;
      return 11;
    }
  }

  window.onload = function () {
    sessionStorage && sessionStorage.clear();
    if (window.top !== window.self) {
      window.top.location = window.location;
    }
    <#-- canvas背景特效 -->
//    var canvasFrame = $$("canvasFrame"), canvasSwitch = $$("canvasSwitch");
//    if (typeof localStorage.bgNy === 'undefined') {
//      dtbg(canvasFrame);
//    } else if (localStorage.bgNy === 'n') {
//      canvasFrame.setAttribute("src", path + "bbg.html");
//      canvasSwitch.innerHTML = "开启背景特效";
//    } else {
//      dtbg(canvasFrame);
//      canvasSwitch.innerHTML = "关闭背景特效";
//    }
////    canvasSwitch.onclick = function () {
//      if (typeof localStorage.bgNy === 'undefined') {
//        localStorage.bgNy = 'n';
//        canvasFrame.setAttribute("src", path + "bbg.html");
//        this.innerHTML = "开启背景特效";
//      } else if (localStorage.bgNy === 'n') {
//        localStorage.bgNy = 'y';
//        dtbg(canvasFrame);
//        this.innerHTML = "关闭背景特效";
//      } else {
//        localStorage.bgNy = 'n';
//        canvasFrame.setAttribute("src", path + "bbg.html");
//        this.innerHTML = "开启背景特效";
//      }
//    };

    <#if captchaShow>
    //验证码
    var imgObj = $$("code_img"), imgSrc = imgObj.getAttribute("data-src");

    function showCodeImg() {
      imgObj.setAttribute("src", imgSrc);
      imgObj.parentNode.style.visibility = "visible";
    }

    imgObj.onclick = function () {
      this.src = imgSrc + "?" + Math.random();
    };
    showCodeImg();
    </#if>
  };
</script>
<#include "/common/console.ftl"/>
</body>
</html>