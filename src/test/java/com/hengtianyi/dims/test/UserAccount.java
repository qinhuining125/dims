package com.hengtianyi.dims.test;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class UserAccount {
    /**
     * 用户昵称
     */
    @Excel(name = "用户名")
    private String userName;

    /**
     * 电话
     */
    @Excel(name = "登录名")
    private String account;

    @Excel(name = "密码")
    private String password;

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
}
