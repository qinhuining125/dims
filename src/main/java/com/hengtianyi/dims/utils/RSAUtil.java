package com.hengtianyi.dims.utils;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;
import org.apache.commons.codec.binary.Base64;

public class RSAUtil {

  public static final String PUBLIC_KEY = "";
  public static final String PRIVATE_KEY = "";
  public static final String KEY_ALGORITHM = "RSA";
  public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
  private static final int MAX_ENCRYPT_BLOCK = 117;
  private static final int MAX_DECRYPT_BLOCK = 128;

  public static Map<String, Object> genKeyPair()
      throws Exception {
    KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
    keyPairGen.initialize(1024);
    KeyPair keyPair = keyPairGen.generateKeyPair();
    RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
    RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
    Map<String, Object> keyMap = new HashMap(2);
    keyMap.put("", publicKey);
    keyMap.put("", privateKey);
    return keyMap;
  }

  public static String decryptByPrivateKey(String text, String privateKey)
      throws Exception {
    byte[] encryptedData = Base64.decodeBase64(text);
    byte[] keyBytes = Base64Util.decode(privateKey);
    PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
    Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
    cipher.init(2, privateK);
    int inputLen = encryptedData.length;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int offSet = 0;

    int i = 0;
    while (inputLen - offSet > 0) {
      byte[] cache;
      if (inputLen - offSet > 128) {
        cache = cipher.doFinal(encryptedData, offSet, 128);
      } else {
        cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
      }
      out.write(cache, 0, cache.length);
      i++;
      offSet = i * 128;
    }
    byte[] decryptedData = out.toByteArray();
    out.close();
    return new String(decryptedData);
  }

  public static String decryptByPublicKey(String text, String publicKey)
      throws Exception {
    byte[] encryptedData = Base64.decodeBase64(text);
    byte[] keyBytes = Base64Util.decode(publicKey);
    X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    Key publicK = keyFactory.generatePublic(x509KeySpec);
    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    cipher.init(2, publicK);
    int inputLen = encryptedData.length;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int offSet = 0;

    int i = 0;
    while (inputLen - offSet > 0) {
      byte[] cache;
      if (inputLen - offSet > 128) {
        cache = cipher.doFinal(encryptedData, offSet, 128);
      } else {
        cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
      }
      out.write(cache, 0, cache.length);
      i++;
      offSet = i * 128;
    }
    byte[] decryptedData = out.toByteArray();
    out.close();
    return new String(decryptedData, "UTF-8");
  }

  public static String encryptByPublicKey(String text, String publicKey)
      throws Exception {
    byte[] data = text.getBytes();
    byte[] keyBytes = Base64Util.decode(publicKey);
    X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    Key publicK = keyFactory.generatePublic(x509KeySpec);

    Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
    cipher.init(1, publicK);
    int inputLen = data.length;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int offSet = 0;

    int i = 0;
    while (inputLen - offSet > 0) {
      byte[] cache;
      if (inputLen - offSet > 117) {
        cache = cipher.doFinal(data, offSet, 117);
      } else {
        cache = cipher.doFinal(data, offSet, inputLen - offSet);
      }
      out.write(cache, 0, cache.length);
      i++;
      offSet = i * 117;
    }
    byte[] encryptedData = out.toByteArray();
    out.close();
    return Base64.encodeBase64String(encryptedData);
  }

  public static String encryptByPrivateKey(String text, String privateKey)
      throws Exception {
    byte[] data = text.getBytes("UTF-8");
    byte[] keyBytes = Base64Util.decode(privateKey);
    PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    cipher.init(1, privateK);
    int inputLen = data.length;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int offSet = 0;

    int i = 0;
    while (inputLen - offSet > 0) {
      byte[] cache;
      if (inputLen - offSet > 117) {
        cache = cipher.doFinal(data, offSet, 117);
      } else {
        cache = cipher.doFinal(data, offSet, inputLen - offSet);
      }
      out.write(cache, 0, cache.length);
      i++;
      offSet = i * 117;
    }
    byte[] encryptedData = out.toByteArray();
    out.close();
    return Base64.encodeBase64String(encryptedData);
  }

  public static String getPrivateKey(Map<String, Object> keyMap)
      throws Exception {
    Key key = (Key) keyMap.get("");
    return Base64Util.encode(key.getEncoded());
  }

  public static String getPublicKey(Map<String, Object> keyMap)
      throws Exception {
    Key key = (Key) keyMap.get("");
    return Base64Util.encode(key.getEncoded());
  }
}
