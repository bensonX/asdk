package org.alan.asdk.dto;

/**
 * Created by Administrator on 2015-12-03.
 */
public class AccountState {

    public static final int SUCCESS = 1;  //成功
    public static final int NEED_CAPTCHA = 2; //需要验证码
    public static final int CAPTCHA_ERR = 3;//验证码错误
    public static final int TOO_MARCH_REQUEST = 4;//请求被拒绝
    public static final int LOGIN_ERR = 5; //登录失败
    public static final int EMAIL_ERR = 6;//邮件格式错误
    public static final int OUT_TIME = 7;//请等待
    public static final int REGISTER_ERR = 8;//注册时用户名密码格式错误
    public static final int ACCOUNT_USED = 9;//用户名已经被使用
    public static final int EMAIL_USED = 10;//邮箱已经被使用
    public static final int AUTH_ERR = 11;//登录验证出错
}
