package com.hengtianyi.dims.constant;

/**
 * 举报方式枚举
 */

public enum RevealTypeEnum {

    NONAME(Short.valueOf("0"), "匿名"),
    REALNAME(Short.valueOf("1"), "实名");

    public final Short type;
    public final String value;

    RevealTypeEnum(Short type, String value) {
        this.type = type;
        this.value = value;
    }

    public Short getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
