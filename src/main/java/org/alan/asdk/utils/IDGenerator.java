package org.alan.asdk.utils;

import org.alan.asdk.cache.impl.manager.TAdminCache;
import org.alan.asdk.cache.impl.manager.TModuleCache;
import org.alan.asdk.entity.*;
import org.alan.asdk.entity.admin.TAdmin;
import org.alan.asdk.entity.admin.TModule;
import org.alan.asdk.cache.impl.logic.*;

import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 这个是一个简单的唯一ID生成器
 * 在这个应用中，我们需要生成appID, channelID，orderID等
 */
public class IDGenerator {

    private static IDGenerator instance;

    private AtomicInteger currAppID;
    private AtomicInteger currMasterID;
    private AtomicInteger currChannelID;
    private AtomicInteger currGoodsID;
    private AtomicInteger currGoodsConfigID;
    private AtomicInteger currChargeChannelID;
    private AtomicInteger currSelfChannelConfigID;

    private AtomicInteger adminID;
    private AtomicInteger moduleID;

    private AtomicInteger currOrderSequence = new AtomicInteger(1);

    private IDGenerator() {

        loadCurrMaxID();

    }

    public static IDGenerator getInstance() {
        if (instance == null) {
            instance = new IDGenerator();
        }

        return instance;
    }

    private void loadCurrMaxID() {
        int maxID = 0;

        for (UGame game : UGameCache.getInstance().getAll()) {
            if (maxID < game.getAppID()) {
                maxID = game.getAppID();
            }
        }

        this.currAppID = new AtomicInteger(maxID);

        maxID = 0;
        for (UChannelMaster master : UChannelMasterCache.getInstance().getAll()) {
            if (maxID < master.getMasterID()) {
                maxID = master.getMasterID();
            }
        }

        this.currMasterID = new AtomicInteger(maxID);

        maxID = 0;
        for (UChannel channel : UChannelCache.getInstance().getAll()) {
            if (maxID < channel.getChannelID()) {
                maxID = channel.getChannelID();
            }
        }

        this.currChannelID = new AtomicInteger(maxID);

        maxID = 0;
        for (UGoods config : UGoodsCache.getInstance().getAll()){
            if (maxID < config.getGoodsID()){
                maxID = config.getGoodsID();
            }
        }
        this.currGoodsID = new AtomicInteger(maxID);

        maxID = 0 ;
        for (UGoodsConfig config : UGoodsConfigCache.getInstance().getAll()){
            if (maxID < config.getId()){
                maxID = config.getId();
            }
        }
        this.currGoodsConfigID = new AtomicInteger(maxID);
        maxID = 0;
        for (TAdmin admin : TAdminCache.getInstance().getAll()) {
            if (maxID < admin.getId()){
                maxID = admin.getId();
            }
        }
        this.adminID = new AtomicInteger(maxID);

        maxID = 0;
        for (TModule module : TModuleCache.getInstance().getAll()) {
            if (maxID<module.getId()){
                maxID = module.getId();
            }
        }
        this.moduleID = new AtomicInteger(maxID);
        maxID = 0;
        for (UChargeChannel chargeChannel : UChargeChannelCache.getInstance().getAll()) {
            if (maxID< chargeChannel.getId()){
                maxID = chargeChannel.getId();
            }
        }
        this.currChargeChannelID = new AtomicInteger(maxID);
        maxID = 0;
        for (SelfChannel selfChannel : SelfChannelCache.getInstance().getAll()) {
            if (maxID< selfChannel.getConfigID()){
                maxID = selfChannel.getConfigID();
            }
        }
        this.currSelfChannelConfigID = new AtomicInteger(maxID);

    }

    public int nextAppID() {


        return this.currAppID.incrementAndGet();
    }

    public int nextMasterID() {
        return this.currMasterID.incrementAndGet();
    }

    public int nextChannelID() {

        return this.currChannelID.incrementAndGet();
    }

    public int nextGoodsID(){
        return this.currGoodsID.incrementAndGet();
    }
    public int nextGoodsConfigID() {
        return this.currGoodsConfigID.incrementAndGet();
    }
    public int nextAdminID(){
        return this.adminID.incrementAndGet();
    }
    public int nextModuleID(){
        return this.moduleID.incrementAndGet();
    }
    public int nextChargeChannelID(){
        return this.currChargeChannelID.incrementAndGet();
    }
    public int nextSelfChannelID(){
        return this.currSelfChannelConfigID.incrementAndGet();
    }
    public long nextOrderID() {

        Calendar can = Calendar.getInstance();
        int year = can.get(Calendar.YEAR) - 2013;
        int month = can.get(Calendar.MONTH) + 1;
        int day = can.get(Calendar.DAY_OF_MONTH);
        int hour = can.get(Calendar.HOUR_OF_DAY);
        int min = can.get(Calendar.MINUTE);
        int sec = can.get(Calendar.SECOND);

        long orderId = year;
        orderId = orderId << 4 | month;
        orderId = orderId << 5 | day;
        orderId = orderId << 5 | hour;
        orderId = orderId << 6 | min;
        orderId = orderId << 6 | sec;
        orderId = orderId << 32 | this.currOrderSequence.getAndIncrement();

        return orderId;
    }

    public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z" };


    public static String generateShortUuid() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();
    }

}
