<#if global.hasSepcialDomain()>
  <!DOCTYPE html>
  <html lang="zh-CN">
  <head>
    <meta charset="utf-8"/>
    <script>document.domain = "${global.domain!}";</script>
  </head>
  <body>
  ${json}
  </body>
  </html>
<#else>
    ${json}
</#if>