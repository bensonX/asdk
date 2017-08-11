package org.alan.asdk.web.selfSDK;

import net.sf.json.JSONObject;
import org.alan.asdk.cache.LanguageCache;
import org.alan.asdk.cache.WebCacheManager;
import org.alan.asdk.common.Log;
import org.alan.asdk.common.UActionSupport;
import org.alan.asdk.dto.AccountState;
import org.alan.asdk.entity.*;
import org.alan.asdk.sdk.SDKVerifyResult;
import org.alan.asdk.service.*;
import org.alan.asdk.utils.*;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.text.MessageFormat;
import java.util.Date;

/**
 * @author Lance Chow
 * @create 2016-08-23 15:21
 */
@Controller
@Scope("prototype")
@Namespace("/account")
public class TAccountAction extends UActionSupport {

    /**
     * TSDK Server分配给当前游戏的appID
     */
    private int appID;

    /**
     * 当前客户端的渠道ID
     */
    private int channelID;

    /**
     * 用户名
     */
    private String username;

    /**
     * 通过MD5以后的密码串
     */
    private String psw;

    /**
     * 邮箱，考虑到ios版本apple会不允许强制用户输入邮箱，这里邮箱在ios平台非必填
     */
    private String email;

    /**
     * 验证码
     */
    private String captcha;
    /**
     * 用于邮件验证的验证码
     */
    private int mailCaptcha;

    /**
     * 老密码
     */
    private String oldPsw;

    /**
     * 登录分配的token
     */
    private String token;

    /**
     * 设备信息
     */
    private String deviceInfo;

    @Autowired
    private UUserManager userManager;
    @Autowired
    private UChannelManager channelManager;
    @Autowired
    private UGameManager gameManager;
    @Autowired
    private TAccountManager manager;
    @Autowired
    private MailManager mailManager;
//    @Autowired
//    BasicDataSource dataSource;

    @Autowired
    private CustomGenericManageableCaptchaService imageCaptchaService;

    /**
     * 注册
     */
    @Action("register")
    public void register() {
        if (!registerValid()) {
            return;
        }
        if (username == null) {
            return;
        }
        if (psw == null) {
            return;
        }
        if (!usrPswValid(username, psw, null)) {
            renderState(AccountState.REGISTER_ERR, LanguageCache.INS.get("action_register_1",request));//用户名密码格式错误 (用户名或密码至少包含一个英文字母和数字!)
            return;
        }
        //验证用户名是否被注册
        UAccount account = manager.getAccountByUserName(username);
        if (account != null) {
            renderState(AccountState.ACCOUNT_USED, LanguageCache.INS.get("action_register_2",request));//该账号已被使用
            return;
        }
        account = manager.getAccountByMail(email);
        if (account != null) {
            renderState(AccountState.EMAIL_USED, LanguageCache.INS.get("action_register_3",request));//E-mail已被使用
            return;
        }

        manager.generateAccount(appID, channelID, username, EncryptUtils.md5(psw), email , psw);
        renderState(AccountState.SUCCESS, LanguageCache.INS.get("action_register_4",request));//注册成功
        Log.i("User [" + username + "]  registered!");
    }

    /**
     * 试玩接口
     *  void
     */
    @Action("guest")
    public void guest() {
        if (!registerValid()) {
            return;
        }
        username = IDGenerator.generateShortUuid();
        psw = "tsixi123456";
        manager.generateAccount(appID, channelID, username, EncryptUtils.md5(psw), null , psw);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("guestName", username);
        jsonObject.put("psw", psw);
        renderState(AccountState.SUCCESS, jsonObject);
    }

    /**
     * 登录
     * 参数: appID
     *      channelID
     *      username
     *      psw(MD5)
     */
    @Action("login")
    public void login() {
            if (!isValid()) {
                return;
            }
            if (StringUtils.isEmpty(username)) {
                return;
            }
            if (StringUtils.isEmpty(psw)) {
                return;
            }
            //验证用户名密码
            UAccount account;
            //判断是否为邮箱登录
            if (usrPswValid(null, null, username)) {
                account = manager.getAccountByMail(username);
                if (account != null) {
                    String psw = account.getPsw();
                    account = psw.equals(this.psw) ? account : null;
                }
            } else {
                account = manager.getAccountByLoginInfo(username, psw);
            }
            if (account == null) {
                renderState(AccountState.AUTH_ERR, LanguageCache.INS.get("action_login_2", request));//用户名和密码不匹配
                 Log.i("自渠道登录失败: 用户名密码不匹配 , 用户名:"+username+" , 密码: "+psw);
                return;
            }
           UUser user = userManager.getUserByCpID(appID, channelID, account.getId() + "");
            UChannel channel = channelManager.queryChannel(channelID);
            if (channel == null) {
                renderState(AccountState.LOGIN_ERR, LanguageCache.INS.get("action_login_3", request));//###ERR_001!### (渠道错误!)
                Log.i("自渠道登录失败: 渠道错误 channelId = "+channelID);
                return;
            }
            if (user == null) {
                SDKVerifyResult sdkVerifyResult = new SDKVerifyResult();
                sdkVerifyResult.setUserID(account.getId() + "");
                sdkVerifyResult.setSuccess(true);
                sdkVerifyResult.setUserName(account.getUsername());
                sdkVerifyResult.setNickName(account.getUsername() + ".tsixi");
                sdkVerifyResult.setExtension(deviceInfo);
                user = userManager.generateUser(channel, sdkVerifyResult);
            } else {
                user.setLastLoginTime(new Date());
            }
            UGame game = gameManager.queryGame(appID);
            if (game == null) {
                renderState(AccountState.LOGIN_ERR, LanguageCache.INS.get("action_login_4", request));
                Log.i("User[" + username + "]! appID Error, appID = " + appID);
                return;
            }
            user.setToken(UGenerator.generateToken(user, game.getAppkey(), null));
            userManager.saveUser(user);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userID", user.getId());
            jsonObject.put("username", account.getUsername());
            jsonObject.put("token", user.getToken());
            jsonObject.put("email", account.getEmail());
            renderState(AccountState.SUCCESS, jsonObject);
            Log.i("User[" + username + "] login success! userID is :" + user.getId() + " , token is :" + user.getToken());
//            Log.i("当前数据库连接数为:"+dataSource.getNumActive());
    }

    /**
     * 发送绑定邮箱的验证邮件
     * 使用参数
     * token
     * email
     */
    @Action("sendBindMailCaptcha")
    public void sendBindMailCaptcha() {
        if (!registerValid()) {
            return;
        }
        if (!usrPswValid(null, null, email)) {
            renderState(AccountState.EMAIL_ERR, LanguageCache.INS.get("action_bind_1",request));// E-mail格式错误
            return;
        }

        //查找邮箱是否被注册
        UAccount account = manager.getAccountByMail(email);
        if (account != null) {
            renderState(AccountState.EMAIL_ERR, LanguageCache.INS.get("action_bind_2",request));//该E-mail已被注册
            return;
        }
        UUser user = userManager.getUUserByToken(token);

        if (!userManager.checkUser(user, token)) {
            renderState(AccountState.LOGIN_ERR, LanguageCache.INS.get("action_bind_3",request));//用户登录超时
            return;
        }
        username = user.getChannelUserName();

        account = manager.getAccountByUserName(username);
        if (account == null) {
            renderState(AccountState.LOGIN_ERR, LanguageCache.INS.get("action_bind_4",request));//账号不存在
            return;
        }
        //验证邮箱正确性
        sendMail(account, email);
    }

    /**
     * 绑定邮箱
     * 使用参数
     * email
     * mailCaptcha 邮箱验证码
     * token
     */
    @Action("bindEmail")
    public void bindEmail() {
        if (!registerValid()) {
            return;
        }
        //验证密码合法性
        if (!usrPswValid(null, psw , null)) {
            renderState(AccountState.REGISTER_ERR, LanguageCache.INS.get("action_reset_1",request));// 用户名密码格式错误 (用户名和密码至少包含一个英文字母和数字!)
            return;
        }
        //获取用户信息
        UAccount account = manager.getAccountByUserName(username);
        if (!account.getPsw().equals(oldPsw)){
            renderState(AccountState.LOGIN_ERR, "获取账户信息失败");//获取账户信息失败
            return;
        }
        //验证邮箱是否被绑定
        if(!StringUtils.isEmpty(account.getEmail())){
            renderState(AccountState.EMAIL_ERR, "该账户已经绑定了邮箱");//该账户已经绑定了邮箱
            return;
        }
        //验证邮箱是否被使用
        if (manager.getAccountByMail(email)!=null){
            renderState(AccountState.EMAIL_USED, "该邮箱已经被使用");//该邮箱已经被使用
            return;
        }
        account.setPsw(EncryptUtils.md5(psw));
        account.setRealPsw(psw);
        account.setEmail(email);
        manager.saveTAccount(account);
        renderState(AccountState.SUCCESS, LanguageCache.INS.get("action_bind_6",request)); //邮箱绑定成功
    }

    /**
     * 发送重置密码验证码邮件
     * 使用参数:
     * email 将要发送的电子邮件地址
     */
    @Action("sendResetMailCaptcha")
    public void sendResetMailCaptcha() {
        //验证是否申请过
        Long lastTime = (Long) request.getSession().getAttribute("lastTime");
        Long now = new Date().getTime();
        if (lastTime!=null&&lastTime!=0){
            if (now-lastTime<60*1000){
                renderState(AccountState.OUT_TIME, LanguageCache.INS.get("action_bind_10",request));//邮件已发送,请稍后再试
                return;
            }
        }else {
            request.getSession().setAttribute("lastTime",now);
        }
        if (!registerValid()) {
            return;
        }
        if (!usrPswValid(null, null, email)) {
            renderState(AccountState.EMAIL_ERR, LanguageCache.INS.get("action_reset_1",request)); // 用户名密码格式错误 (用户名和密码至少包含一个英文字母和数字!)
            return;
        }
        //查找邮箱是否正确
        UAccount account = manager.getAccountByMail(email);
        if (account == null) {
            renderState(AccountState.EMAIL_ERR, LanguageCache.INS.get("action_bind_11",request));//邮箱不存在
            return;
        }
        sendMail(account, account.getEmail());
    }

    /**
     * 重置密码
     * 使用参数:
     * email 要重置邮箱的地址
     * psw 重置密码 明文
     * mailCaptcha 邮件发送的验证码
     */
    @Action("resetPsw")
    public void resetPsw() {
        if (!registerValid()) {
            return;
        }
        if (!usrPswValid(null, psw, email)) {
            renderState(AccountState.REGISTER_ERR, LanguageCache.INS.get("action_reset_1",request));// 用户名密码格式错误 (用户名和密码至少包含一个英文字母和数字!)
            return;
        }
        String ca = WebCacheManager.getInstance().getMailCaptcha(email);
        WebCacheManager.getInstance().removeMailCaptcha(ca);
        if (ca == null) {
            renderState(AccountState.OUT_TIME, LanguageCache.INS.get("action_reset_2",request));//验证码已超时
            return;
        }
        if (!ca.equals(mailCaptcha + "")) {
            renderState(AccountState.CAPTCHA_ERR, LanguageCache.INS.get("action_reset_3",request));//验证码错误
            return;
        }
        UAccount account = manager.getAccountByMail(email);
        account.setPsw(EncryptUtils.md5(psw));
        manager.saveTAccount(account);
        renderState(AccountState.SUCCESS, LanguageCache.INS.get("action_reset_4",request));//密码已重置
        Log.i("用户[" + account.getUsername() + "(" + account.getId() + ")]密码重置成功!");
    }

    /**
     * 发送验证码邮件
     *
     * @param account account
     * @param toAddress 邮件地址
     */
    public void sendMail(UAccount account, String toAddress) {
        Mail mail = new Mail();
        mail.setToAddress(toAddress);
        mail.setSubject(LanguageCache.INS.get("action_send_1",request));//Tsixi 重置验证码
        int ca = (int) (Math.random() * 9000 + 1000);
        String content = MessageFormat.format(LanguageCache.INS.get("action_send_2",request),account.getUsername(),ca+"");//你的用户名 [{0}] 正在重置信息 , 验证码为: <h1>{1}</h1> <p>为了你的账号安全 , 请勿将验证码告诉给第三人</p>
        CharsetUtils utils = new CharsetUtils();
        mail.setContent(utils.toISO_8859_1(content));
        WebCacheManager.getInstance().saveMailCaptcha(toAddress, ca + "");
        mailManager.sendMailByTemplate(mail);
        renderState(AccountState.SUCCESS, LanguageCache.INS.get("action_send_3",request));//验证码发送成功
        Log.i("验证码邮件(email="+account.getEmail()+")[" + account.getUsername() + "][" + ca + "]已加入到线程池");
    }



    /**
     * 验证用户名和密码或邮箱是合法
     *
     * @param username 用户名
     * @param psw 密码
     * @param mail 电子邮箱
     * @return  * 1个不合法就会返回false,不验证是否为空
     */
    public boolean usrPswValid(String username, String psw, String mail) {
        String reg = "(^[A-Za-z0-9]{6,16}$)";

        if (!StringUtils.isEmpty(username)) {
            if (!username.matches(reg)) {
                return false;
            }
        }
//        reg = "/((?=.*\\d)(?=.*[a-z])";
        if (!StringUtils.isEmpty(psw)) {
            if (!psw.matches(reg)) {
                return false;
            }
        }
        reg = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        if (!StringUtils.isEmpty(mail)) {
            if (!mail.matches(reg)) {
                return false;
            }
        }
        return true;
    }

    public boolean isValid() {
        return appID != 0 && channelID != 0;
    }

    public boolean validCaptcha(){
        return  imageCaptchaService.validateResponseForID(request.getSession().getId(),captcha);
    }

    public boolean registerValid() {
        int registerCont = 0;
        Object o = session.get("registerCount");
        if (o != null) {
            registerCont = Integer.valueOf(o.toString());
        }
        registerCont++;
        session.put("registerCount", registerCont);
//        //若请求次数超过100次则不接受请求
//        if (registerCont>=100){
//            renderState(AccountState.TOO_MARCH_REQUEST,"请求被拒绝!");
//            return false;
//        }
//        //若请求次数超过50次则要求输入验证码
//        if(registerCont>=50){
//            if(captcha==null || captcha.equals("")){
//                renderState(AccountState.NEED_CAPTCHA,"请输入验证码");
//                return false;
//            }
//            if(!imageCaptchaService.validateResponseForID(request.getSession().getId(),captcha)){
//                renderState(AccountState.CAPTCHA_ERR,"验证码错误!");
//                return false;
//            }
//        }
        return true;
    }

    private void renderState(int suc, String msg) {
        JSONObject json = new JSONObject();
        json.put("state", suc);
        json.put("des", msg);
        renderText(json.toString());
    }

    private void renderState(int suc, JSONObject data) {
        JSONObject json = new JSONObject();
        json.put("state", suc);
        json.put("data", data);
        renderText(json.toString());
    }

    public int getAppID() {
        return appID;
    }

    public void setAppID(int appID) {
        this.appID = appID;
    }

    public int getChannelID() {
        return channelID;
    }

    public void setChannelID(int channelID) {
        this.channelID = channelID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public int getMailCaptcha() {
        return mailCaptcha;
    }

    public void setMailCaptcha(int mailCaptcha) {
        this.mailCaptcha = mailCaptcha;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOldPsw() {
        return oldPsw;
    }

    public void setOldPsw(String oldPsw) {
        this.oldPsw = oldPsw;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }
}
