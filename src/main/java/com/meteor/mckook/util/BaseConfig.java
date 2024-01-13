package com.meteor.mckook.util;

import com.meteor.mckook.McKook;

import java.io.File;
import java.util.Arrays;

public class BaseConfig {

    private McKook plugin;

    private MessageBox messageBox;

    public static BaseConfig instance;

    public static void init(McKook plugin){
        instance = new BaseConfig(plugin);
    }


    private BaseConfig(McKook plugin){
        this.plugin = plugin;

        File file = new File(plugin.getDataFolder(),"plugins");
        if(!file.exists()) file.mkdirs();

        this.reload();

    }

    public void reload(){
        File dataFolder = plugin.getDataFolder();
        Arrays.asList("config.yml","message.yml")
                .forEach(fileName->{
                    File file = new File(dataFolder,fileName);
                    if(!file.exists()) plugin.saveResource(fileName,false);
                });
        plugin.reloadConfig();
        messageBox = MessageBox.createMessageBox(plugin,"message.yml");
    }

    public MessageBox getMessageBox() {
        return messageBox;
    }
}
