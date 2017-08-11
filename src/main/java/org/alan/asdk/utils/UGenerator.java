package org.alan.asdk.utils;

import org.alan.asdk.entity.UMsdkOrder;
import org.alan.asdk.entity.UOrder;
import org.alan.asdk.entity.UUser;

public class UGenerator {


    /**
     * 生成appkey
     *
     * @param appID
     * @param createTime
     * @return
     */
    public static String generateAppKey(int appID, long createTime) {

        String txt = appID + "" + createTime;

        return EncryptUtils.md5(txt);
    }

    /**
     * 生成用于生成登录认证使用的Token
     *
     * @param user
     * @return
     */
    public static String generateToken(UUser user, String appKey, String ip) {
        String txt = user.getId() + "-" + user.getChannelUserID() + "-" + appKey + ip;

        return EncryptUtils.md5(txt);
    }

    /**
     * 生成sign
     *
     * @param order
     * @return
     */
    public static String generateSign(UOrder order) {

        StringBuilder sb = new StringBuilder();
        sb.append("appID=").append(order.getAppID())
                .append("channelID=").append(order.getChannelID())
                .append("currency").append(order.getCurrency())
                .append("extension").append(order.getExtension())
                .append("money=").append(order.getMoney())
                .append("orderID=").append(order.getOrderID())
                .append("userID=").append(order.getUserID());

        return EncryptUtils.md5(sb.toString());
    }

    public static String generateSign(int state, UOrder order, String appKey) {

        String sb = "state=" + state +
                "&channelID=" + order.getChannelID() +
                "&extension=" + order.getExtension() +
                "&money=" + order.getMoney() +
                "&orderID=" + order.getOrderID() +
                "&userID=" + order.getUserID() +
                "&" + appKey;

        return EncryptUtils.md5(sb);
    }

    /**
     * 生成应用宝sign
     *
     * @param order
     * @return
     */
    public static String generateMsdkSign(UMsdkOrder order) {

        StringBuilder sb = new StringBuilder();

        sb.append("appID=").append(order.getAppID())
                .append("channelID=").append(order.getChannelID())
                .append("coinNum=").append(order.getCoinNum())
                .append("firstPay=").append(order.getFirstPay())
                .append("allMoney=").append(order.getAllMoney())
                .append("userID=").append(order.getUserID());

        return EncryptUtils.md5(sb.toString());
    }
}
