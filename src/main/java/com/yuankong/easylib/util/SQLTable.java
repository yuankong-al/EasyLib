package com.yuankong.easylib.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 底层是基于HashMap的表存储类，需要指定key类型
 *
 */
@Deprecated
public class SQLTable<k,c,v> {
    private final Map<k,HashMap<c,v>> map;

    public SQLTable() {
        map = new HashMap<>();
    }

    /**
     * @param key key;
     * @param column 字段，每个key对应字段唯一，相当于HashMap的key;
     * @param value 值
     */
    public void put(k key,c column,v value){
        if(!map.containsKey(key)){
            HashMap<c,v> columnMap = new HashMap<>();
            columnMap.put(column,value);
            map.put(key,columnMap);
        }else{
            map.get(key).put(column,value);
        }
    }

    public v get(k key,c column){
        return map.get(key).get(column);
    }

    public Set<k> keySet(){
        return map.keySet();
    }

    public Collection<HashMap<c, v>> getValues(){
        return map.values();
    }

    public Map<k,HashMap<c,v>> getKeyMap(){
        return map;
    }

    public HashMap<c,v> getColumnMap(k key){
        return map.get(key);
    }
}
