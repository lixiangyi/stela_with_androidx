package com.stela.comics_unlimited.data.entity;

import java.io.Serializable;

public class LoginInfo implements Serializable {
    // 使用了加密存储、串码字段值不允许更改
    private static final long serialVersionUID = 3259151408908544658L;
    public String nick_name;
    public String email;
    public String password;
}
