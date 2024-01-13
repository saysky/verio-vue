package com.liuyanzhao.common.core.domain.model;

/**
 * 用户注册对象
 *
 * @author saysky
 */
public class RegisterBody extends LoginBody {

    private String nickName;

    private String phonenumber;
    private String email;

    private String role;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
