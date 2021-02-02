package com.hengtianyi.dims.test;

import com.hengtianyi.common.core.util.HttpUtil;
import com.hengtianyi.dims.utils.RSAUtil;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

public class SimpleTest {

  @Test
  public void jdk8() {
//    List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
//    strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());
//    strings.stream().filter(string -> !string.isEmpty()).count();
//    strings.stream().filter(string -> !string.isEmpty()).skip(1);
//    strings.stream().filter(string -> !string.isEmpty()).limit(2);
//    strings.stream().filter(string -> !string.isEmpty()).distinct();
//    strings.stream().filter(string -> !string.isEmpty()).sorted();

    String url = "http://58.49.165.99:9023/syleader/showSystem/gridInfo";
    String appSecret = "wgyxxyz/GFUFsRbiE=";
    String token = "cf4da70a4179e83a6284c92789aa96f1";
    String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCf+VFB5bMtK6OLIonTBXR/LnQGt6zv/9emqqLAW7i3UnpHhJN1KWpHW1sdbaWDrxycVplE6WyQqakEHmK+IByI8oDOCTa/PLtQshSRyha/B8xndDjl6VcpWPUXemcpaVf4J7xMYB1e05II2xTD16vc+ZmcI3GZ8ytF94RbuyW8ZQIDAQAB";
    Map<String, String> params = new HashMap<>();
    params.put("token", token);
    String text = HttpUtil.post(url, params);
    try {
      System.out.println(RSAUtil.decryptByPublicKey(text, publicKey));
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
