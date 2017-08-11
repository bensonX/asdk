package org.alan.asdk.sdk.shuowan;

import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.entity.UUser;
import org.alan.asdk.sdk.ISDKOrderListener;
import org.alan.asdk.sdk.ISDKScript;
import org.alan.asdk.sdk.ISDKVerifyListener;
import org.alan.asdk.sdk.SDKVerifyResult;
import org.alan.asdk.utils.EncryptUtils;
import net.sf.json.JSONObject;

import java.util.Objects;


public class ShuoWanSDK implements ISDKScript {
    @Override
    /**
     * 验证sign
     */
    public void verify(final UChannel channel, String extension, final ISDKVerifyListener callback) {

        try {
            JSONObject json = JSONObject.fromObject(extension);

            String sign = json.getString("sign");
            String username = json.getString("username");
            String loginTime = json.getString("loginTime");

            String appKey = channel.getCpAppKey();
            String str = "username=" + username + "&appkey=" + appKey + "&logintime=" + loginTime;
            String _sign = EncryptUtils.md5(str);

            if (Objects.equals(_sign, sign)) {
                SDKVerifyResult vResult = new SDKVerifyResult(true, username, username, username);

                callback.onSuccess(vResult);

                return;
            }

            callback.onFailed("<说玩>登陆签名（"+sign+"）错误 , 当前签名为："+_sign+"("+str+")");

        } catch (Exception e) {
            e.printStackTrace();
            callback.onFailed(channel.getMaster().getSdkName() + " verify execute failed. the exception is " + e.getMessage());
        }

    }

    @Override
    public void onGetOrderID(UUser user, UOrder order, ISDKOrderListener callback) {
        if (callback != null) {
            callback.onSuccess("");
        }
    }

}
