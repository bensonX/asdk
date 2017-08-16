package org.alan.asdk.sdk.pp;

import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.entity.UUser;
import org.alan.asdk.sdk.*;
import org.alan.asdk.utils.EncryptUtils;
import net.sf.json.JSONObject;
import org.apache.http.entity.ByteArrayEntity;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ant on 2015/8/10.
 */
public class PPSDK implements ISDKScript {
    @Override
    public void verify(final UChannel channel, String extension, final ISDKVerifyListener callback) {

        try {
            JSONObject json = JSONObject.fromObject(extension);
            String sid = json.getString("token");

            JSONObject params = new JSONObject();
            params.put("id", System.currentTimeMillis() / 1000);
            params.put("dao", "account.verifySession");

            JSONObject data = new JSONObject();
            data.put("sid", sid);

            JSONObject game = new JSONObject();
            game.put("gameId", channel.getCpAppID());

            params.put("game", game);
            params.put("encrypt", "MD5");
            params.put("sign", EncryptUtils.md5("sid=" + sid + channel.getCpAppKey()));
            params.put("data",data);



            String url = channel.getMaster().getAuthUrl();

            Map<String, String> headers = new HashMap<String, String>();
            headers.put("Content-Type", "application/json");

            THttpAgent.getInstance().post(url, headers, new ByteArrayEntity(params.toString().getBytes(Charset.forName("UTF-8"))), new UHttpCallback() {

                @Override
                public void completed(String result) {

                    try {

                        JSONObject json = JSONObject.fromObject(result);
                        int state = json.getJSONObject("state").getInt("code");
                        String accountID = json.getJSONObject("data").getString("accountId");
                        String nickName = json.getJSONObject("data").getString("nickName");
                        if (state == 1) {
                            SDKVerifyResult vResult = new SDKVerifyResult(true, accountID, accountID,nickName);

                            callback.onSuccess(vResult);

                            return;
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    callback.onFailed(channel.getMaster().getSdkName() + " verify failed. the result is " + result);
                }

                @Override
                public void failed(String e) {
                    callback.onFailed(channel.getMaster().getSdkName() + " verify failed. " + e);
                }

            });

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
