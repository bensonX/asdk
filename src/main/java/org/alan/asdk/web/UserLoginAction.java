package org.alan.asdk.web;

import net.sf.json.JSONObject;
import org.alan.asdk.common.Log;
import org.alan.asdk.common.UActionSupport;
import org.alan.asdk.dto.StateCode;
import org.alan.asdk.sdk.UHttpAgent;
import org.alan.asdk.sdk.UHttpCallback;
import org.alan.asdk.utils.JsonUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

/**
 * 这个类是模拟游戏服
 * 客户端-》第三方SDK登录成功之后，访问u8server获取token。
 * 当token获取成功之后，就会开始连接游戏服。这个类就是模拟
 * 登录流程的最后一步操作
 * <p/>
 * 客户端拿着userID,token等信息连接游戏服务器，游戏服需要去u8server
 * 验证token，验证成功，则登录合法，否则登录失败
 * <p/>
 * Created by ant on 2015/4/17.
 */
@Controller
@Scope("prototype")
@Namespace("/user")
public class UserLoginAction extends UActionSupport {

    private static final String AUTH_URL = "http://localhost:8080/user/verifyAccount";

    private int userID;
    private String token;

    private String sign;            //签名


    @Action("loginServer")
    public void login() {

        try {

            Log.d("The userID is " + userID);
            Log.d("The token is " + token);
            Log.d("The sign is " + sign);

            Map<String, String> params = new HashMap<String, String>();
            params.put("userID", "" + userID);
            params.put("token", token);
            params.put("sign", sign);

            UHttpAgent.newInstance().post(AUTH_URL, params, new UHttpCallback() {
                @Override
                public void completed(String content) {
                    Log.d("The loginServer check token result is " + content);
                    AuthResult result = (AuthResult) JsonUtils.decodeJson(content, AuthResult.class);

                    if (result.getState() == StateCode.CODE_AUTH_SUCCESS) {
                        Log.d("login game com.u8.server success. return account info ...");
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("accountID", 1);
                        jsonObject.put("accountName", "game-account-01");
                        renderState(0, jsonObject);
                    } else {
                        renderState(1, null);
                    }


                }

                @Override
                public void failed(String e) {
                    Log.e(e);
                    renderState(1, null);
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
            renderState(1, null);
        }

    }

    private void renderState(int state, JSONObject data) {
        try {

            JSONObject json = new JSONObject();
            json.put("state", state);
            json.put("data", data);

            super.renderJson(json.toString());

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(e.getMessage());
        }


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

    static class AuthResult {
        private int state;
        private AuthUserInfo data;

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public AuthUserInfo getData() {
            return data;
        }

        public void setData(AuthUserInfo data) {
            this.data = data;
        }
    }

    static class AuthUserInfo {
        private String userID;
        private String username;

        public String getUserID() {
            return userID;
        }

        public void setUserID(String userID) {
            this.userID = userID;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
