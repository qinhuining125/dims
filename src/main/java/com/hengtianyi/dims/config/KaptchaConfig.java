package com.hengtianyi.dims.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import java.util.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 验证码配置
 *
 * @author BBF
 */
@Configuration
public class KaptchaConfig {

  @Bean(name = "captchaProducer")
  public DefaultKaptcha getKaptchaBean() {
    DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
    Properties properties = new Properties();
    //渲染效果：水纹：com.google.code.kaptcha.impl.WaterRipple；鱼眼：FishEyeGimpy；阴影：ShadowGimpy -->
    //使用自定义的渲染器
    properties
        .setProperty("kaptcha.obscurificator.impl", "com.hengtianyi.matou.feature.KaptchaGimpy");
    //图片边框
    properties.setProperty("kaptcha.border", "yes");
    //边框颜色
    properties.setProperty("kaptcha.border.color", "yellow");
    //边框厚度
    properties.setProperty("kaptcha.border.thickness", "2");
    //图片宽度
    properties.setProperty("kaptcha.image.width", "135");
    //图片高度
    properties.setProperty("kaptcha.image.height", "45");
    //验证码字体颜色
    properties.setProperty("kaptcha.textproducer.font.color", "black");
    //验证码字体大小
    properties.setProperty("kaptcha.textproducer.font.size", "42");
    //验证码长度
    properties.setProperty("kaptcha.textproducer.char.length", "5");
    //验证码从此集合中获取
    properties.setProperty("kaptcha.textproducer.char.string", "abcdefhkmnrstuvx2345678");
    //验证码字体
    // properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
    Config config = new Config(properties);
    defaultKaptcha.setConfig(config);
    return defaultKaptcha;
  }
}
