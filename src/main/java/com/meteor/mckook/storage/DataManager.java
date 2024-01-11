package com.meteor.mckook.storage;

import com.meteor.mckook.McKook;
import com.meteor.mckook.storage.database.SqliteDatabase;
import com.meteor.mckook.storage.mapper.BaseMapper;
import com.meteor.mckook.storage.mapper.LinkRepository;
import com.meteor.mckook.storage.mapper.impl.LinkRepositoryImpl;

import java.util.HashMap;
import java.util.Map;

public class DataManager {

    private static DataManager instance;

    private McKook plugin;

    private Database database;

    private Map<Class<?>, BaseMapper> baseMapperMap;

    private DataManager(){
        this.baseMapperMap = new HashMap<>();
    }

    public static void init(){
        instance = new DataManager();
    }


    public <T> T getMapper(Class<T> mapper) {
        return mapper.cast(baseMapperMap.get(mapper));
    }
}
