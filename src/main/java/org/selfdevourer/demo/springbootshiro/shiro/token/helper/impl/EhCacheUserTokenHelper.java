package org.selfdevourer.demo.springbootshiro.shiro.token.helper.impl;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.selfdevourer.demo.springbootshiro.shiro.token.helper.UserTokenOperHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class EhCacheUserTokenHelper implements UserTokenOperHelper {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 对应ehcache.xml cache Name
     */
    private String userTokenCacheName = "userTokenCache";

    /**
     * ehcache缓存管理器
     */
    private CacheManager cacheManager;

    public String getUserToken(String userCode) {
        logger.info("EhCacheUserTokenHelper.getUserToken");
        Cache cache = getUserTokenCache();
        if (cache == null) {
            return null;
        } else {
//            Element element = cache.get(userCode);
            Set keys = cache.keys();
            for (Object object : keys) {
                logger.info(object.toString());
            }
            Object object = cache.get(userCode);
            if (object == null) {
                return null;
            } else {
                return (String) object;
            }
//            if (element == null) {
//                return null;
//            } else {
//                Object objectValue = element.getObjectValue();
//                if (objectValue == null) {
//                    return null;
//                } else {
//                    return (String) objectValue;
//                }
//            }
        }
    }

    public Cache getUserTokenCache() {
        logger.info("EhCacheUserTokenHelper.getUserTokenCache");
        Cache cache = cacheManager.getCache(userTokenCacheName);
        return cache;
    }

    public void updateUserToken(String userCode, String token, long seconds) {
        logger.info("EhCacheUserTokenHelper.updateUserToken");
        Cache cache = getUserTokenCache();
//        Element e = new Element(userCode, token);
//        e.setTimeToLive(new Long(seconds).intValue());
        cache.put(userCode, token);
        Set keys = cache.keys();
        for (Object object : keys) {
            logger.info(object.toString());
        }
    }

    public void deleteUserToken(String userCode) {
        logger.info("EhCacheUserTokenHelper.deleteUserToken");
        Cache cache = getUserTokenCache();
        cache.remove(userCode);
    }

    public String getUserTokenCacheName() {
        return userTokenCacheName;
    }

    public void setUserTokenCacheName(String userTokenCacheName) {
        this.userTokenCacheName = userTokenCacheName;
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

}
