package org.alan.asdk.sdk.qihoo360;

import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.entity.UUser;
import org.alan.asdk.sdk.*;
import org.alan.asdk.utils.JsonUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ant on 2015/2/27.
 */
public class Qihoo360SDK implements ISDKScript {

    @Override
    public void verify(final UChannel channel, String extension, final ISDKVerifyListener callback) {

        try {

            Map<String, String> data = new HashMap<String, String>();
            data.put("access_token", extension);
            data.put("fields", "id,name,avatar,sex,area,nick");
            String url = channel.getMaster().getAuthUrl();

            UHttpAgent.getInstance().get(url, data, new UHttpCallback() {
                @Override
                public void completed(String result) {

                    try {

                        QihooUserInfo userInfo = (QihooUserInfo) JsonUtils.decodeJson(result, QihooUserInfo.class);

                        if (userInfo != null && !StringUtils.isEmpty(userInfo.getId())) {
                            SDKVerifyResult vResult = new SDKVerifyResult(true, userInfo.getId(), userInfo.getName(), userInfo.getNick());
                            callback.onSuccess(vResult);
                            return;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();

                        QihooErrorInfo error = (QihooErrorInfo) JsonUtils.decodeJson(result, QihooErrorInfo.class);
                        if (error != null) {
                            callback.onFailed(channel.getMaster().getSdkName() + " verify failed. msg:" + error.getError());
                            return;
                        }

                    }


                    callback.onFailed(channel.getMaster().getSdkName() + " verify failed. the get result is " + result);
                }

                @Override
                public void failed(String e) {
                    callback.onFailed(channel.getMaster().getSdkName() + " verify failed. " + e);
                }


            });


        } catch (Exception e) {
            callback.onFailed(channel.getMaster().getSdkName() + " verify execute failed. the exception is " + e.getMessage());
            e.printStackTrace();
        }


    }

    @Override
    public void onGetOrderID(UUser user, UOrder order, ISDKOrderListener callback) {

        JSONObject json = new JSONObject();

        try {

            String notifyUrl = order.getChannel().getMaster().getPayCallbackUrl();
            json.put("notifyUrl", notifyUrl);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (callback != null) {
            callback.onSuccess(json.toString());
        }
    }
}
