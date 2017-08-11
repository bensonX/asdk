package org.alan.asdk.sdk.selfSDK;

import org.alan.asdk.common.Log;
import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UGoods;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.entity.UUser;
import org.alan.asdk.sdk.ISDKOrderListener;
import org.alan.asdk.sdk.ISDKScript;
import org.alan.asdk.sdk.ISDKVerifyListener;
import org.alan.asdk.sdk.UHttpAgent;
import org.alan.asdk.sdk.iapppay.sign.SignHelper;
import org.alan.asdk.sdk.iapppay.sign.SignUtils;
import net.sf.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TSDK爱贝支付
 */
public class TSDKIPay implements ISDKScript {
    @Override
    public void verify(UChannel channel, String extension, ISDKVerifyListener callback) {

    }

    @Override
    public void onGetOrderID(UUser user, UOrder order, ISDKOrderListener callback) {
        if (callback != null) {
            UChannel channel = order.getChannel();
            List<UGoods> goodsList = channel.getGoodsList();
            UGoods goods = null;
            for (UGoods g : goodsList) {
                if (g.getMoney() == order.getMoney()){
                    goods = g;
                    break;
                }
            }
            String e = getOrderByIapppay(goods,order.getOrderID()+"",channel);
            callback.onSuccess(e);
        }
    }

    private String getOrderByIapppay(UGoods goods , String cporderid , UChannel channel){
        String url = channel.getMaster().getOrderUrl();
        JSONObject json = new JSONObject();
        json.put("appid",channel.getCpAppID());
        json.put("waresid",Integer.parseInt(goods.getSdkGoodsID()));
        json.put("waresname","goods"+goods.getSdkGoodsID());
        json.put("cporderid",cporderid);
        json.put("price",goods.getMoney());
        json.put("currency","RMB");
        json.put("appuserid",cporderid);
        json.put("cpprivateinfo",cporderid);

        Map<String,String> params = new HashMap<>();
        params.put("transdata", json.toString());
        //计算签名
        params.put("signtype","RSA");
        Log.i("爱贝拉取订单:json = " +json );

        params.put("sign", SignHelper.sign(json.toString(),channel.getCpPayKey()));

        String r = UHttpAgent.getInstance().post(url,params);
        try {
            r = URLDecoder.decode(r,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Map<String, String> p =  SignUtils.getParmters(r);
        JSONObject j = JSONObject.fromObject(p.get("transdata"));
        if(!j.containsKey("transid")){
            return null;
        }
        return j.getString("transid");
    }
}
