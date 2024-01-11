package com.meteor.mckook.kook.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.cache.CacheBuilder;
import com.meteor.mckook.model.link.LinkCache;

import java.util.concurrent.TimeUnit;

/**
 * KOOK账户绑定
 */
public class LinkService {

    /**
     * 验证码缓存
     */
    private Cache<String, LinkCache> linkCacheCache;

    public LinkService(){

        linkCacheCache = Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES) // 验证码有效期为5分钟
                .maximumSize(500)
                .build();
    }






}
