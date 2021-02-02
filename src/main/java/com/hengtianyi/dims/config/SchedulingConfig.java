package com.hengtianyi.dims.config;

import com.hengtianyi.common.core.util.HttpUtil;
import com.hengtianyi.common.core.util.JsonUtil;
import com.hengtianyi.common.core.util.sequence.IdGenUtil;
import com.hengtianyi.common.core.util.sequence.SystemClock;
import com.hengtianyi.dims.service.api.SysUserService;
import com.hengtianyi.dims.service.entity.SysUserEntity;
import com.hengtianyi.dims.utils.RSAUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 定时任务
 *
 * @author LY
 */
@Configuration
@EnableScheduling
public class SchedulingConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(SchedulingConfig.class);

  @Resource
  private SysUserService sysUserService;

  /**
   * 每天0点定时执行一次
   */
  @Scheduled(cron = "0 0 0 * * ?")
  public void syncUserData() {
    String url = "http://10.1.1.4:9023/syleader/showSystem/gridInfo";
    String appSecret = "wgyxxyz/GFUFsRbiE=";
    String token = "cf4da70a4179e83a6284c92789aa96f1";
    String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCf+VFB5bMtK6OLIonTBXR/LnQGt6zv/9emqqLAW7i3UnpHhJN1KWpHW1sdbaWDrxycVplE6WyQqakEHmK+IByI8oDOCTa/PLtQshSRyha/B8xndDjl6VcpWPUXemcpaVf4J7xMYB1e05II2xTD16vc+ZmcI3GZ8ytF94RbuyW8ZQIDAQAB";
    Map<String, String> params = new HashMap<>();
    params.put("token", token);
    try {
      String text = HttpUtil.post(url, params);
      String data = RSAUtil.decryptByPublicKey(text, publicKey);
      List<SysUserEntity> userEntityList = JsonUtil
          .fromJsonList(JsonUtil.fromJsonObj(data).getJSONObject("data")
              .getString("result"), SysUserEntity.class);
      for (SysUserEntity userEntity : userEntityList) {
        if (sysUserService.checkRepeat("", userEntity.getAreaCode())) {
          userEntity.setId(IdGenUtil.uuid32());
          userEntity.setUserAccount(userEntity.getAreaCode());
          userEntity.setRoleId(1001);
          userEntity.setEnabled(1);
          userEntity.setCreateTime(SystemClock.now());
          sysUserService.insertData(userEntity);
          LOGGER.info("insert====>{}", JsonUtil.toJson(userEntity));
        }
      }
    } catch (Exception e) {
      LOGGER.error("[syncUserData]出错,{}", e.getMessage(), e);
    }
  }

}
