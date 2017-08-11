package org.alan.asdk.cache.impl.logic;

import org.alan.asdk.entity.UUser;

import java.util.HashMap;
import java.util.Map;

public class UPlayerCache_ {

    private static UPlayerCache_ instance;

//    private Map<Integer, UGame> games;
//    private Map<Integer, UChannelMaster> masters;
//    private Map<Integer, UChannel> channels;
//    private Map<Integer, UGoods> goodses;
//    private Map<Integer, UGoodsConfig> goodsConfigs;
    /**
     * 玩家缓存
     */
    private Map<String , UUser> users = new HashMap<>();

    public UPlayerCache_() {

    }

    public static UPlayerCache_ getInstance() {
        if (instance == null) {
            instance = new UPlayerCache_();
        }
        return instance;
    }

//    public List<UGame> getGameList() {
//        return new ArrayList<>(games.values());
//    }
//    public List<UChannel> getChannelList() {
//        return new ArrayList<>(channels.values());
//    }
//    public List<UChannelMaster> getMasterList() {
//        return new ArrayList<>(masters.values());
//    }
//    public List<UGoodsConfig> getGoodsConfigList(){
//        return new ArrayList<>(goodsConfigs.values());
//
//    }
//    public List<UGoods> getGoodsList() {
//        return new ArrayList<>(goodses.values());
//    }

//    public UGame getGame(int appID) {
//        if (this.games.containsKey(appID)) {
//            return this.games.get(appID);
//        }
//        return null;
//    }

//    public UChannelMaster getMaster(int masterID) {
//        if (this.masters.containsKey(masterID)) {
//            return this.masters.get(masterID);
//        }
//        return null;
//    }

//    public UChannel getChannel(int channelID) {
//        if (this.channels.containsKey(channelID)) {
//            return this.channels.get(channelID);
//        }
//        return null;
//    }
//    public UGoodsConfig getGoodsConfig(int goodsConfigID) {
//        if (this.goodsConfigs.containsKey(goodsConfigID)){
//            return this.goodsConfigs.get(goodsConfigID);
//        }
//        return null;
//    }
    public UUser getUUser(String token){
        if (token==null){
            return null;
        }
        if (this.users.containsKey(token)){
            return this.users.get(token);
        }
        return null;
    }


    public void saveUUser(UUser user){
        if (users.containsKey(user.getToken())){
            users.remove(user.getToken());
        }
        users.put(user.getToken(),user);
    }

//    public void addGame(UGame game) {
//        if (games.containsKey(game.getAppID())) {
//            Log.e("The appID is already is exists. add game failed." + game.getAppID());
//            return;
//        }
//        games.put(game.getAppID(), game);
//    }

//    public void saveGame(UGame game) {
//        if (games.containsKey(game.getAppID())) {
//            games.remove(game.getAppID());
//        }
//        games.put(game.getAppID(), game);
//    }

//    public void addMaster(UChannelMaster master) {
//        if (masters.containsKey(master.getMasterID())) {
//            Log.e("The channel master ID is already is exists. add channel master faild." + master.getMasterID());
//            return;
//        }
//
//        masters.put(master.getMasterID(), master);
//    }

//    public void saveMaster(UChannelMaster master) {
//        if (masters.containsKey(master.getMasterID())) {
//            masters.remove(master.getMasterID());
//        }
//        masters.put(master.getMasterID(), master);
//    }
//
//    public void removeMaster(int masterID) {
//        if (masters.containsKey(masterID)) {
//            masters.remove(masterID);
//        }
//    }

//    public void addChannel(UChannel channel) {
//        if (channels.containsKey(channel.getChannelID())) {
//            Log.e("The channelID is already is exists. add channel faild." + channel.getChannelID());
//            return;
//        }
//
//        channels.put(channel.getChannelID(), channel);
//    }

    //添加或者修改渠道
//    public void saveChannel(UChannel channel) {
//        if (channels.containsKey(channel.getChannelID())) {
//            channels.remove(channel.getChannelID());
//        }
//        channels.put(channel.getChannelID(), channel);
//    }
//
//    public void removeChannel(int channelID) {
//        if (channels.containsKey(channelID)) {
//            channels.remove(channelID);
//        }
//    }

//    public void removeGame(int appID) {
//        if (games.containsKey(appID)) {
//            games.remove(appID);
//        }
//    }

    //增加或者修改商品配置
//    public void saveGoods(UGoods uGoods){
//        if(goodses.containsKey(uGoods.getGoodsID())){
//            goodses.remove(uGoods.getGoodsID());
//        }
//        goodses.put(uGoods.getGoodsID() , uGoods);
//    }
//    public void removeGoods(int goodsId) {
//        if (goodses.containsKey(goodsId)) {
//            goodses.remove(goodsId);
//        }
//    }

    //增加或者修改商品配置
//    public void saveGoodsConfig(UGoodsConfig config) {
//        if (goodsConfigs.containsKey(config.getId())){
//            goodsConfigs.remove(config.getId());
//        }
//        goodsConfigs.put(config.getId(),config);
//    }
//    public void removeGoodsConfig(int id) {
//        if (goodsConfigs.containsKey(id)){
//            goodsConfigs.remove(id);
//        }
//    }
////
//    public void loadGameData(List<UGame> gameLst) {
//        games = new HashMap<>();
//        for (UGame game : gameLst) {
//            games.put(game.getAppID(), game);
//        }
//        Log.i("Load games :" + games.size());
//    }

//    public void loadMasterData(List<UChannelMaster> masterLst) {
//        masters = new HashMap<>();
//
//        for (UChannelMaster master : masterLst) {
//            masters.put(master.getMasterID(), master);
//        }
//        Log.i("Load masters:" + masters.size());
//    }

//    public void loadChannelData(List<UChannel> channelLst) {
//        channels = new HashMap<>();
//        for (UChannel channel : channelLst) {
//            channels.put(channel.getChannelID(), channel);
//        }
//        Log.i("Load channels:" + channels.size());
//    }
//
//    public void loadGoods(List<UGoods> goodsList) {
//        goodses = new HashMap<>();
//        for (UGoods goods : goodsList) {
//            this.goodses.put(goods.getGoodsID(), goods);
//        }
//        Log.i("Load goods:" + goodses.size());
//    }
    public void loadUsers(){
        users = new HashMap<>();
    }

//    public void loadGoodsConfigs(List<UGoodsConfig> list) {
//        goodsConfigs = new HashMap<>();
//        for (UGoodsConfig config : list) {
//            this.goodsConfigs.put(config.getId(), config);
//        }
//        Log.i("Load goodsConfigs:" + goodsConfigs.size());
//    }



}
