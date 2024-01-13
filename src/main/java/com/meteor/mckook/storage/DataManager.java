package com.meteor.mckook.storage;

import com.meteor.mckook.McKook;
import com.meteor.mckook.storage.database.SqliteDatabase;
import com.meteor.mckook.storage.mapper.BaseMapper;
import com.meteor.mckook.storage.mapper.LinkRepository;
import com.meteor.mckook.storage.mapper.impl.LinkRepositoryImpl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DataManager {

    public static DataManager instance;

    private McKook plugin;

    private AbstractDatabase abstractDatabase;

    private Map<Class<?>, BaseMapper> baseMapperMap;

    private DataManager(McKook plugin){
        this.plugin = plugin;
        this.abstractDatabase = new SqliteDatabase(plugin);

        try {
            this.abstractDatabase.connect();
            plugin.getLogger().info("已连接数据库");
        } catch (SQLException e) {
            e.printStackTrace();
            plugin.getLogger().info("数据库连接失败");
        }

        this.baseMapperMap = new HashMap<>();

        baseMapperMap.put(LinkRepository.class,new LinkRepositoryImpl(abstractDatabase));

    }

    public void close(){
        try {
            abstractDatabase.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void init(McKook plugin){
        instance = new DataManager(plugin);
    }


    public <T> T getMapper(Class<T> mapper) {
        return mapper.cast(baseMapperMap.get(mapper));
    }
}
