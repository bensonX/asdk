package org.alan.asdk.web;

import net.sf.json.JSONObject;
import org.alan.asdk.common.Log;
import org.alan.asdk.common.UActionSupport;
import org.alan.asdk.dto.StateCode;
import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UChannelMaster;
import org.alan.asdk.entity.UGame;
import org.alan.asdk.entity.UUser;
import org.alan.asdk.sdk.ISDKScript;
import org.alan.asdk.sdk.ISDKVerifyListener;
import org.alan.asdk.sdk.SDKVerifyResult;
import org.alan.asdk.service.UChannelManager;
import org.alan.asdk.service.UGameManager;
import org.alan.asdk.service.UUserManager;
import org.alan.asdk.utils.UGenerator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.Date;

/**
 * NOTE: 大部分渠道SDK都需要进行登录认证操作。客户端SDK登陆成功之后，渠道SDK一般会返回一个sessionId，token等数据，用于二次登录认证的凭据。
 *
 * 游戏客户端SDK登录成功之后，渠道SDK一般会返回token等数据，当玩具登录游戏的时候，游戏服务器需要用客户端传上来的token等数据，去SDK服务器进行登录认证，只有认证成功，才能允许当前用户登录。否则，登录失败。
 * U8Server采用统一的架构设计，将用户登录认证的操作集中在U8Server,作为一个所有游戏所有渠道的统一的用户登录认证中心。
 * 如果游戏服务器去做这个登录认证工作，那么如果公司同时有多款游戏，那么每款游戏都需要做这个重复的工作。所以，最好的解决方案是，我们会将这个工作集中在一起，多个游戏都能直接用，而不用每个游戏都去重复实现。这就是U8Server的意义。
 * 这样，我们就将每个游戏对多个渠道SDK服务器的流程，改成了每个游戏只对统一的U8Server，U8Server再对多个渠道SDK服务器的流程 。
 */
@Controller
@Scope("prototype")
@Namespace("/user")
public class UserAction extends UActionSupport {
    /**
     * U8Server分配给当前游戏的appID
     */
    private int appID;
    /**
     * 当前客户端的渠道ID
     */
    private int channelID;
    /**
     * 当前渠道登录成功的参数(sid,token,sessionId等，一个或者多个)，这里格式各个渠道SDK可能不一样。
     */
    private String extension;
    /**
     * 签名md5("appID="+appID+"channelID="+channelID+"extension="+extension+appKey);这里U8SDK抽象层按照格式，生成一个md5串，appKey是U8Server分配给游戏的AppKey
     */
    private String sign;
    /**
     * U8Server生成的唯一用户ID
     */
    private int userID;
    /**
     * U8Server生成的token，用于游戏服务器登录认证使用
     */
    private String token;

    @Autowired
    private UGameManager gameManager;

    @Autowired
    private UChannelManager channelManager;

    @Autowired
    private UUserManager userManager;


    //http://bzddtcenter.gz.1251123371.clb.myqcloud.com:8997/qx/center/game.net?type=1001/USER_ID=50395134&PLATFORM=0&DEVICE=win&TOKEN=afa3948d49b84d1bb901c548379f5d98&CHANNEL=1
    /**
     * 游戏客户端获取Token签名
     * 渠道SDK的统一登录认证地址 http://yourURL:yourPort/user/getToken
     * 渠道SDK登录成功之后，U8SDK抽象层会来U8Server进行渠道SDK的二次登录认证。这里的地址，就是各个渠道SDK统一的登录认证地址。
     * 需要参数: appID channelID extension sign
     */
    @Action("getToken")
    public void getLoginToken() {
        try {
            final long[] useTime = {System.currentTimeMillis()};
            //通过appID获取当前游戏的对象
            final UGame game = gameManager.queryGame(this.appID);
            if (game == null) { //如果不能获取到所属游戏则报错
                Log.i("Could not find game from appID["+this.appID+"]! getToken return");
                renderState(StateCode.CODE_GAME_NONE, JSONObject.fromObject("{msg:\"gameID错误\"}"));
                return;
            }

            //通过channelID获取当前游戏的渠道
            final UChannel channel = channelManager.queryChannel(this.channelID);
            if (channel == null) { //如果不能获取所属渠道则报错
                Log.i("Could not find channel from channelID["+ this.channelID+"] getToken return");
                renderState(StateCode.CODE_CHANNEL_NONE, JSONObject.fromObject("{msg:\"渠道不存在\"}"));
                return;
            }

            //获取渠道商
            UChannelMaster master = channel.getMaster();
            if (master == null) { //获取不到渠道商则报错
                Log.i("Could not find channelMaster from channel["+channel.getAppID()+"]");
                renderState(StateCode.CODE_CHANNEL_NONE, JSONObject.fromObject("{msg:\"渠道商不存在\"}"));
                return;
            }

            //验证签名
            StringBuilder sb = new StringBuilder();
            sb.append("appID=").append(this.appID)
                    .append("channelID=").append(this.channelID)
                    .append("extension=").append(this.extension).append(game.getAppkey());

//            //验证签名正确性
//            if (!userManager.isSignOK(sb.toString(), sign)) {
//                renderState(StateCode.CODE_SIGN_ERROR, null);
//                return;
//            }


            //获取SDK的操作类
            ISDKScript verifier = (ISDKScript) Class.forName(master.getVerifyClass()).newInstance();

            //到SDK去验证
            verifier.verify(channel, extension, new ISDKVerifyListener() {
                @Override
                public void onSuccess(SDKVerifyResult sdkResult) {

                    try {
                        if (sdkResult.isSuccess()) {
                            useTime[0] = System.currentTimeMillis() - useTime[0];
                            Log.i("Channel["+channel.getMaster().getMasterName()+"("+channel.getChannelID()+")]User login verify is successful , Result:" + sdkResult.toString()+" , use " +useTime[0] +" MS");

                            UUser user = userManager.getUserByCpID(appID, channelID, sdkResult.getUserID());

                            if (user == null) {
                                user = userManager.generateUser(channel, sdkResult);
                            } else {
                                user.setLastLoginTime(new Date());
                            }
                            user.setToken(UGenerator.generateToken(user, game.getAppkey(), null));
                            user.setChannelUserName(sdkResult.getUserName());
                            userManager.saveUser(user);
                            JSONObject data = new JSONObject();
                            data.put("userID", user.getId());
                            data.put("sdkUserID", user.getChannelUserID());
                            data.put("userName", user.getName());
                            data.put("sdkUserName", user.getChannelUserName());
                            data.put("token", user.getToken());
                            data.put("extension", sdkResult.getExtension());
                            renderState(StateCode.CODE_AUTH_SUCCESS, data);
                        } else {
                            Log.i("The userID["+sdkResult.getUserID()+"] verify do not success");
                            renderState(StateCode.CODE_AUTH_FAILED, JSONObject.fromObject("{msg:\"签名错误\"}"));
                        }

                    } catch (Exception e) {
                        Log.e("The userID["+sdkResult.getUserID()+"] verify do not success , program has an EXCEPTION:",e);
                        renderState(StateCode.CODE_AUTH_FAILED, JSONObject.fromObject("{msg:\"验证失败\"}"));
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailed(String errorMsg) {
                    Log.e("The user verify failed. errorMsg:" + errorMsg);
                    renderState(StateCode.CODE_AUTH_FAILED, JSONObject.fromObject("{msg:\"验证失败\"}"));
                }
            });


        } catch (Exception e) {
            Log.e(e.getMessage(),e);
            renderState(StateCode.CODE_AUTH_FAILED, JSONObject.fromObject("{msg:\"验证失败\"}"));
        }
    }

    private void renderState(int state, JSONObject data) {
        try {
            JSONObject json = new JSONObject();
            json.put("state", state);
            json.put("data", data);
            renderJson(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(e.getMessage());
        }
    }

    /**
     * 游戏服务器登录认证地址
     * 客户端去U8Server二次登录认证成功之后，U8Server返回客户端U8Server生成的token，客户端紧接着开始登录游戏，同时携带这个token，游戏服务器拿着这个token，要来U8Server进行一次登录认证。这个就是游戏服务器的登录认证地址。
     * 需要参数: userID token sign
     */
    @Action("verifyAccount")
    public void loginVerify() {
        try {
            long useTime = System.currentTimeMillis();
            //通过ID获取UUser对象
            UUser user = userManager.getUser(this.userID);
            if (user == null) {
                Log.i("verify userID["+this.userID +"] failed , cannot find userID ["+this.userID+"]");
                renderState(StateCode.CODE_USER_NONE, null);
                return;
            }

            //验证签名
            StringBuilder sb = new StringBuilder();
            if (user.getGame()==null){
                Log.i("verify userID["+this.userID +"] failed , cannot find GAME ["+user.getAppID()+"]");
                renderState(StateCode.CODE_VERIFY_FAILED, null);
                return;
            }
            sb.append("userID=").append(this.userID)
                    .append("token=").append(this.token)
                    .append(user.getGame().getAppkey());
            if (!userManager.isSignOK(sb.toString(), sign)) {
                Log.i("verify userID["+this.userID +"] failed , sign(sign)["+sign+"] is error");
                renderState(StateCode.CODE_SIGN_ERROR, null);
                return;
            }

            //验证token的正确性
            if (StringUtils.isEmpty(this.token)) {
                Log.i("verify userID["+userID+"] failed , token is null ");
                renderState(StateCode.CODE_VERIFY_FAILED, null);
                return;
            }
            if (!userManager.checkUser(user, token)) {
                Log.i("verify userID ["+this.userID +"] failed, token["+token+"] is error");
                renderState(StateCode.CODE_TOKEN_ERROR, null);
                return;
            }

            JSONObject data = new JSONObject();
            data.put("userID", user.getId());
            data.put("username", user.getName());
            data.put("channelUserID",user.getChannelUserID());
            renderState(StateCode.CODE_AUTH_SUCCESS, data);
            useTime = System.currentTimeMillis() - useTime;
            Log.i("verify userID ["+this.userID +"] success! use " + useTime +" MS");
            return;

        } catch (Exception e) {
            Log.e(e.getMessage());
            e.printStackTrace();
        }

        renderState(StateCode.CODE_VERIFY_FAILED, null);
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

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
