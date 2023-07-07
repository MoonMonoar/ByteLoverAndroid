package com.immo2n.bytelover.CoreClasses;

import java.util.LinkedHashMap;
import java.util.Map;
public class NetCache {
    private static final int MAX_CACHE_SIZE = 100;
    private static NetCache instance;
    private final LinkedHashMap<String, String> netCache;
    private NetCache() {
        netCache = new LinkedHashMap<String, String>(MAX_CACHE_SIZE + 1, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                return size() > MAX_CACHE_SIZE;
            }
        };
    }
    public static synchronized NetCache getInstance() {
        if (instance == null) {
            instance = new NetCache();
        }
        return instance;
    }
    public String getDataFromCache(String url) {
        return netCache.get(url);
    }

    public void storeDataInCache(String url, String data) {
        netCache.put(url, data);
    }
}