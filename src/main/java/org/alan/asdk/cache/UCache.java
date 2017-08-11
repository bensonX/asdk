package org.alan.asdk.cache;


import org.alan.asdk.common.Log;

import javax.persistence.Id;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 缓存继承类
 *
 * @author Lance Chow
 *         2016-09-06 15:01
 */
public class UCache<T extends Serializable, PK> {

    private Map<T,PK> entities;

    public List<PK> getAll(){
        return new ArrayList<>(entities.values());
    }

    public PK get(T object){
        return entities.get(object);
    }

    public void save(PK pk){
        if(entities.containsKey(getIdValue(pk))){
            entities.remove(getIdValue(pk));
        }
        entities.put(getIdValue(pk),pk);
    }

    public void remove(T id){
        if (entities.containsKey(id)){
            entities.remove(id);
        }
    }

    public void load(List<PK> pks){

        entities = new HashMap<>();
        if (pks == null || pks.isEmpty()){
            return;
        }
        for (PK pk : pks) {
            T id = getIdValue(pk);
            entities.put(id,pk);
        }
        String name = pks.get(0).getClass().getName();
        Log.i("load " + pks.size()+" " +name.substring(name.lastIndexOf(".")+1,name.length()) +" success!");
    }

    private T getIdValue(PK pk){
        Field[] fields = pk.getClass().getDeclaredFields();
        if (fields == null){
            return null;
        }
        for (Field field : fields) {
            Id id =  field.getAnnotation(Id.class);
            if (id!=null){
                try {
                    field.setAccessible(true);
                    return (T) field.get(pk);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
