package com.hengtianyi.dims.service.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * @author LY
 */
public class UserDto {
    /**
     * 用户昵称
     */
    @Excel(name = "用户姓名")
    private String userName;

    @Excel(name = "用户登录账号")
    private String account;

    @Excel(name = "用户登录密码")
    private String password;

    @Excel(name = "所属区划编码")
    private String areaCode;

    @Excel(name = "所属区划名称")
    private String areaName;

    @Excel(name = "性别")
    private String sex;

    @Excel(name = "身份证")
    private String idCard;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
}
