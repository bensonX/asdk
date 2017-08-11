package org.alan.asdk.dao.logic;

import org.alan.asdk.common.UHibernateTemplate;
import org.alan.asdk.entity.UGame;
import org.springframework.stereotype.Repository;


/**
 * 游戏对象数据访问类
 */
@Repository("gameDao")
public class UGameDao extends UHibernateTemplate<UGame, Integer> {


    public void saveGame(UGame game) {
        super.save(game);
    }

    public UGame queryGame(int appID) {

        return super.get(appID);
    }

}
