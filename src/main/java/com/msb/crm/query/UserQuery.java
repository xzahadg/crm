package com.msb.crm.query;

import com.msb.crm.base.BaseQuery;

public class UserQuery extends BaseQuery {
    private String UserName;// 用户名
    private String email;// 邮箱
    private String phone;// 手机号

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
