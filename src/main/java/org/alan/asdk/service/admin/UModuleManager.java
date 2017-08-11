package org.alan.asdk.service.admin;

import org.alan.asdk.cache.impl.manager.TModuleCache;
import org.alan.asdk.dao.manager.TModuleDao;
import org.alan.asdk.entity.admin.TModule;
import org.alan.asdk.utils.IDGenerator;
import org.alan.asdk.utils.ListSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 模块管理层
 *
 * @author Lance Chow
 * @create 2016-01-14 15:09
 */
@Service("moduleManager")
public class UModuleManager {


    @Autowired
    private TModuleDao dao;

    /**
     * 根据权限获取模块
     * @return
     */
    public List<TModule> getModuleByPermission(int permission){

        List<TModule> list = TModuleCache.getInstance().getAll();
        ListSort<TModule> sort = new ListSort<>();
        sort.Sort(list,"getId");
        List<TModule> result = new ArrayList<>();
        for (TModule module : list){
            if (module.getPermission() >= permission){
                result.add(module);
            }
        }
        return result;

    }
    public void saveModule(TModule module){
        if (module.getId() == 0){
            module.setId(IDGenerator.getInstance().nextModuleID());
        }
        TModuleCache.getInstance().save(module);
        dao.save(module);
    }

    public TModule getModule(int id){
        return TModuleCache.getInstance().get(id);
    }

    public void removeModuleByID(Integer id) {
        TModuleCache.getInstance().remove(id);
        dao.delete(id);
    }

    public List<Map<String,Object>> moduleToTree(List<TModule> modules , int pid){
        List<Map<String,Object>> array = new ArrayList<>();
        List<TModule> tempModules = new ArrayList<>();
        for (TModule module : modules) {
            if (module.getPid() == pid){
                tempModules.add(module);
            }
        }
        for (TModule module : tempModules) {
            Map<String,Object> js = new HashMap<>();
            js.put("text",module.getName());
            js.put("icon","fa "+module.getIconCls());
            js.put("href","#"+module.getId());
            if (!module.isLeaf()){
                js.put("nodes",moduleToTree(modules,module.getId()));
            }
            array.add(js);
        }
        return array;
    }

    public List<TModule> getPModules() {

        List<TModule> modules = TModuleCache.getInstance().getAll();
        List<TModule> res = new ArrayList<>();
        for (TModule module : modules) {
            if (!module.isLeaf()){
                res.add(module);
            }
        }
        return res;

    }
}
