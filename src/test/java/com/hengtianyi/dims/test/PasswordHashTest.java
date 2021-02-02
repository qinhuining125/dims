package com.hengtianyi.dims.test;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

public class PasswordHashTest {

  /**
   * PBKDF2算法
   */
  private static final String ALGORITHMS = "PBKDF2WithHmacSHA1";

  /**
   * 验证密钥
   *
   * @param cipherText 密文
   * @param plainText  待验证的明文
   * @param saltStr    盐
   * @param iterations 迭代次数
   * @param dkLen      长度
   * @return true - 验证成功
   */
  public static boolean validate(String plainText, final String cipherText, String saltStr,
      int iterations, int dkLen) {
    try {
      byte[] salt = Hex.decodeHex(saltStr.toCharArray());
      return slowEquals(cipherText, pbkdf2(plainText, salt, iterations, dkLen));
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 生成密钥
   *
   * @param plainText  密码明文字符串
   * @param salt       盐
   * @param iterations 迭代次数
   * @param dkLen      密钥长度，实际长度是这个值的两倍，双字节
   * @return PasswordHashEntity 加密实体对象
   * @throws NoSuchAlgorithmException 加密算法异常
   * @throws InvalidKeySpecException  密钥异常
   */
  public static String pbkdf2(String plainText, byte[] salt, int iterations, int dkLen)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    PBEKeySpec spec = new PBEKeySpec(plainText.toCharArray(), salt, iterations, dkLen * 8);
    SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHMS);
    byte[] cipherText = skf.generateSecret(spec).getEncoded();
    return Hex.encodeHexString(cipherText);
  }

  /**
   * 比较字节数组
   *
   * @param a 要比较的字符串
   * @param b 要比较的字符串
   * @return true - 全部相同
   */
  public static boolean slowEquals(String a, String b) {
    try {
      return slowEqual(Hex.decodeHex(a.toCharArray()), Hex.decodeHex(b.toCharArray()));
    } catch (Exception ex) {
    }
    return false;
  }

  /**
   * 比较字节数组
   *
   * @param a 要比较的字节数组
   * @param b 要比较的字节数组
   * @return true - 全部相同
   */
  public static boolean slowEqual(final byte[] a, final byte[] b) {
    int aLen = a.length;
    int bLen = b.length;
    int diff = aLen ^ bLen;
    for (int i = 0; i < aLen && i < bLen; i++) {
      diff |= a[i] ^ b[i];
    }
    return diff == 0;
  }

  @Test
  public void validPassword() {
    System.out.println(
        validate("1", "460339c6c51a0c36fd4f563c699e6674", "30f6691e435c18e8eebe0b63c8b1da02", 599,
            16));
  }
}
