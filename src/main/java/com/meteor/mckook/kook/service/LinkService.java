package com.meteor.mckook.kook.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.meteor.mckook.kook.KookBot;
import com.meteor.mckook.kook.KookService;
import com.meteor.mckook.model.link.KookUser;
import com.meteor.mckook.model.link.LinkCache;
import com.meteor.mckook.storage.DataManager;
import com.meteor.mckook.storage.mapper.LinkRepository;
import com.meteor.mckook.util.VerificationCodeGenerator;
import snw.jkook.entity.User;

import java.util.concurrent.TimeUnit;

/**
 * KOOK账户绑定
 */
public class LinkService implements KookService {

    private KookBot kookBot;

    private LinkRepository linkRepository;

    /**
     * 验证码缓存
     */
    private Cache<String, LinkCache> linkCacheCache;

    public LinkService(KookBot kookBot){

        this.kookBot = kookBot;

        this.linkCacheCache = Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES) // 验证码有效期为5分钟
                .maximumSize(500)
                .build();

        this.linkRepository = DataManager.instance.getMapper(LinkRepository.class);
    }


    public boolean isLinked(String player){
        return linkRepository.isLinked(player);
    }

    public String buildVerifyCode(String player){
        String verifyCode = VerificationCodeGenerator.generateVerificationCode();
        linkCacheCache.put(verifyCode,new LinkCache(player));
        return verifyCode;
    }

    public KookUser getLinkUser(String player){
        return linkRepository.getLinkedKookUser(player);
    }

    public KookUser link(String player,User user){
        KookUser kookUser = new KookUser();
        kookUser.setId(user.getId());
        kookUser.setIdentifyNum(String.valueOf(user.getIdentifyNumber()));
        kookUser.setUserName(user.getName());
        kookUser.setNickName(user.getNickName(kookBot.getGuild()));
        kookUser.setJoinedAt(System.currentTimeMillis());
        kookUser.setPlayer(player);
        kookUser.setMobileVerified(true);
        kookUser.setBot(user.isBot());
        kookUser.setVip(user.isVip());
        kookUser.setAvatar(user.getAvatarUrl(false));
        linkRepository.link(player,kookUser);
        return kookUser;
    }

    public LinkCache getLinkCache(String verifyCode){
        return linkCacheCache.getIfPresent(verifyCode);
    }


    /**
     * 处理绑定
     * @param verifyCode
     * @param user
     * @return
     */
    public void handler(String verifyCode,User user){
        LinkCache linkCache = getLinkCache(verifyCode);
        link(linkCache.getPlayerName(),user);
        linkCacheCache.invalidate(verifyCode);
    }


}
