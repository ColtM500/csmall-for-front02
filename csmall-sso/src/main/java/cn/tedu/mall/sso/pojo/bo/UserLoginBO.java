package cn.tedu.mall.sso.pojo.bo;

import lombok.Data;
@Data
public class UserLoginBO {


    private String username;

    private String password;


    private String redirectUrl;


    private String state;

    private String ip;

    private String userAgent;




}
