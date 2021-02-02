package com.hengtianyi.dims.service.dto;

import com.hengtianyi.common.core.base.BaseBean;

/**
 * 简单的key-value实体
 *
 * @author BBF
 */
public class KeyValueDto extends BaseBean {

  private static final long serialVersionUID = -5676200506390879638L;
  private String key;
  private String value;

  public KeyValueDto() { /* compiled code */ }

  public KeyValueDto(java.lang.String key, java.lang.String value) {
    this.setKey(key);
    this.setValue(value);
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return "KeyValueDTO{" +
        "key='" + key + '\'' +
        ", value='" + value + '\'' +
        '}';
  }
}
