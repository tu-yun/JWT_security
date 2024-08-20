package com.xi.fmcs.support.util;

import java.util.HashMap;
import java.util.Map;

public class TypeDictionary<TKey, TValue> {

    private Map<TKey,TValue> typeDic;

    public TypeDictionary () {
        typeDic = new HashMap<>();
    }

    public void put(TKey key, TValue value) {
        typeDic.put(key,value);
    }

    public Object getKey(TValue value) {
        Object key = null;
        for (TKey k : typeDic.keySet()) {
            if (value.equals(typeDic.get(key))) {
                key = k;
            }
        }
        return key;
    }

    public TValue getValue(TKey key) {
        return typeDic.get(key);
    }

}
