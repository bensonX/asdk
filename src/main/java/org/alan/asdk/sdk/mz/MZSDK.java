package org.alan.asdk.sdk.mz;

import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.entity.UUser;
import org.alan.asdk.sdk.ISDKOrderListener;
import org.alan.asdk.sdk.ISDKScript;
import org.alan.asdk.sdk.ISDKVerifyListener;
import org.alan.asdk.sdk.SDKVerifyResult;
import org.alan.asdk.utils.EncryptUtils;
import net.sf.json.JSONObject;

/**
 * 拇指游戏 摩奇卡卡 登录SDK
 * @author 周麟东 on 17:06
 */
public class MZSDK implements ISDKScript {
    @Override
    public void verify(UChannel channel, String extension, ISDKVerifyListener callback) {
        try {
            JSONObject json = JSONObject.fromObject(extension);
            String username = json.getString("username");
            String userId = json.getString("userid");
            String sign = json.getString("sign");
            //验证是否正确
            String str = username+channel.getCpAppKey();
            String md5 =  EncryptUtils.md5(str);
            if (!md5.equalsIgnoreCase(sign)){
                str = username+channel.getCpAppSecret();
                md5 = EncryptUtils.md5(str);
                if (!md5.equalsIgnoreCase(sign)){
                    callback.onFailed("<摩奇卡卡>用户登录验证失败!  md5 = " + md5 +" , extension = "+ extension);
                    return;
                }
            }
            SDKVerifyResult vResult = new SDKVerifyResult(true, userId, username, username);
            callback.onSuccess(vResult);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onGetOrderID(UUser user, UOrder order, ISDKOrderListener callback) {
        if (callback != null) {
            callback.onSuccess("");
        }
    }
}
