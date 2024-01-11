package com.meteor.mckook.storage.mapper.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.meteor.mckook.model.link.KookUser;
import com.meteor.mckook.reflect.orm.ReflectFactory;
import com.meteor.mckook.storage.AbstractDatabase;
import com.meteor.mckook.storage.Database;
import com.meteor.mckook.storage.mapper.LinkRepository;
import com.meteor.mckook.storage.mapper.BaseMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LinkRepositoryImpl implements LinkRepository, BaseMapper {


    private AbstractDatabase database;

    private Cache<String,KookUser> kookUserCache;


    private final String KOOK_USER_TABLE_NAME = "KOOK_USER";


    public LinkRepositoryImpl(AbstractDatabase database){
        this.database = database;

        this.kookUserCache = Caffeine.newBuilder()
                .expireAfterWrite(25, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build();

        try {

            Map<String,String> params = new HashMap<>();

            params.put("table",KOOK_USER_TABLE_NAME);

            // 创建kook用户表
            this.database.executeQuery("CREATE TABLE IF NOT EXISTS {table} (\n" +
                    "    id VARCHAR(255) PRIMARY KEY,\n" +
                    "    playerName VARCHAR(25),\n" +
                    "    userName VARCHAR(255),\n" +
                    "    nickName VARCHAR(255),\n" +
                    "    identifyNum VARCHAR(50),\n" +
                    "    avatar TEXT,\n" +
                    "    vip BOOLEAN,\n" +
                    "    bot BOOLEAN,\n" +
                    "    mobileVerified BOOLEAN,\n" +
                    "    joinedAt BIGINT\n" +
                    ");\n",params);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public boolean isLinked(String player) {

        // 当有缓存时，必定已经绑定过了
        if(kookUserCache.getIfPresent(player)!=null) return true;

        String sql = "select id from "+KOOK_USER_TABLE_NAME+" where player = ?";
        ResultSet resultSet = null;
        try {
            resultSet = this.database.executeQuery(sql, null, Arrays.asList(player));
            if(resultSet.next()){
                kookUserCache.put(player,getLinkedKookUser(player));
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    @Override
    public KookUser getLinkedKookUser(String player) {
        // 先走缓存
        KookUser cacheIfPresent = kookUserCache.getIfPresent(player);
        if(cacheIfPresent!=null){
            return cacheIfPresent;
        }

        String sql = "select * from "+KOOK_USER_TABLE_NAME+" where player = ?";
        ResultSet resultSet = null;
        try {
            resultSet = this.database.executeQuery(sql, null, Arrays.asList(player));
            if(resultSet.next()){
                KookUser andPopulate = ReflectFactory.createAndPopulate(KookUser.class, resultSet);
                kookUserCache.put(player,andPopulate);
                return getLinkedKookUser(player);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    @Override
    public void link(String player, KookUser kookUser) {
        String sql = "INSERT INTO "+KOOK_USER_TABLE_NAME+" (id, playerName, userName, nickName, identifyNum, avatar, vip, bot, mobileVerified, joinedAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            this.database.executeUpdate(sql,null,kookUser.getFieldList());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
