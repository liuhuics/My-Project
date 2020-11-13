package com.smk.admin.vo.req;


import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class LoginVO {
    private String username;

    private String password;

    private String rememberMe;

}
