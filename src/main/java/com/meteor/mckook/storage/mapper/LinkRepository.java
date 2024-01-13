package com.meteor.mckook.storage.mapper;

import com.meteor.mckook.model.link.KookUser;

public interface LinkRepository{

    /**
     * 是否已绑定KOOK账户
     * @param player
     * @return
     */
    boolean isLinked(String player);

    /**
     * 获取绑定KOOK账户
     * @param player
     * @return
     */
    KookUser getLinkedKookUser(String player);

    /**
     * 绑定账号
     * @param player
     * @param kookUser
     */
    void link(String player,KookUser kookUser);

    boolean kookUserIsLinked(String kookId);
}
