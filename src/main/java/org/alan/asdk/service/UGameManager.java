package org.alan.asdk.service;

import org.alan.asdk.cache.impl.logic.UChannelCache;
import org.alan.asdk.cache.impl.logic.UGameCache;
import org.alan.asdk.dao.logic.UGameDao;
import org.alan.asdk.entity.UChannel;
import org.alan.asdk.entity.UGame;
import org.alan.asdk.entity.admin.TAdmin;
import org.alan.asdk.utils.IDGenerator;
import org.alan.asdk.utils.RSAUtils;
import org.alan.asdk.utils.UGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("gameManager")
public class UGameManager {

    @Autowired
    private UGameDao gameDao;

    public UGame generateGame(String name, String payCallback, String payCallbackDebug , int createAdminID) {

        UGame game = new UGame();
        game.setCreateAdminID(createAdminID);
        int appID = IDGenerator.getInstance().nextAppID();
        long currTime = System.currentTimeMillis();
        game.setAppID(appID);
        game.setAppkey(UGenerator.generateAppKey(appID, currTime));
        game.setName(name);
        game.setPayCallbackDebug(payCallback);
        game.setPayCallback(payCallbackDebug);

        try {
            Map<String, Object> keys = RSAUtils.generateKeys();
            game.setAppRSAPubKey(RSAUtils.getPublicKey(keys));
            game.setAppRSAPriKey(RSAUtils.getPrivateKey(keys));
        } catch (Exception e) {
            e.printStackTrace();
        }

        saveGame(game);


        return game;
    }


    public void saveGame(UGame game) {
        UGameCache.getInstance().save(game);
        gameDao.save(game);
    }

    public void deleteGame(UGame game) {

        if (game == null) {
            return;
        }
        UGameCache.getInstance().remove(game.getAppID());
        gameDao.delete(game);

    }

    public UGame queryGame(int appID) {

        return UGameCache.getInstance().get(appID);
    }

    public List<UGame> queryAllGames() {

        return UGameCache.getInstance().getAll();
    }

    public int getGameCount() {
        return queryAllGames().size();
    }

    public int getGameCount(TAdmin tAdmin) {
        return getGameByAdmin(tAdmin).size();
    }

    public List<UChannel> queryChannels(int appID) {
        List<UChannel> lst = UChannelCache.getInstance().getAll();

        List<UChannel> result = new ArrayList<UChannel>();
        for (UChannel c : lst) {
            if (c.getAppID() == appID) {
                result.add(c);
            }
        }

        return result;
    }

    //分页查找
    public List<UGame> queryPage(int currPage, int num , TAdmin tAdmin) {
        if (currPage==0){
            return getGameByAdmin(tAdmin);
        }
        Collections.sort(getGameByAdmin(tAdmin), new Comparator<UGame>() {
            @Override
            public int compare(UGame o1, UGame o2) {
                return o1.getAppID() - o2.getAppID();
            }
        });
        int fromIndex = (currPage - 1) * num;

        if (fromIndex >= getGameByAdmin(tAdmin).size()) {

            return null;
        }
        int endIndex = Math.min(fromIndex + num, getGameByAdmin(tAdmin).size());

        return getGameByAdmin(tAdmin).subList(fromIndex, endIndex);
    }

    private List<UGame> getGameByAdmin(TAdmin tAdmin){
        List<UGame> masters = UGameCache.getInstance().getAll();
        if (tAdmin !=null&& tAdmin.getPermission()!=0){
            List<UGame> list = new ArrayList<UGame>();
            for (UGame game : masters){
                if (game.getCreateAdminID()== tAdmin.getId().intValue()){
                    list.add(game);
                }
            }
            masters = list;
        }
        return masters;
    }
}
