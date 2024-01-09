package com.meteor.mckook.kook;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.meteor.mckook.McKook;
import com.meteor.mckook.util.TextComponentHelper;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import snw.jkook.HttpAPI;
import snw.jkook.command.JKookCommand;
import snw.jkook.config.file.YamlConfiguration;
import snw.jkook.entity.Guild;
import snw.jkook.entity.abilities.Accessory;
import snw.jkook.entity.channel.Channel;
import snw.jkook.entity.channel.TextChannel;
import snw.jkook.event.Listener;
import snw.jkook.message.component.BaseComponent;
import snw.jkook.message.component.TextComponent;
import snw.jkook.message.component.card.CardBuilder;
import snw.jkook.message.component.card.MultipleCardComponent;
import snw.jkook.message.component.card.Size;
import snw.jkook.message.component.card.Theme;
import snw.jkook.message.component.card.element.ImageElement;
import snw.jkook.message.component.card.element.MarkdownElement;
import snw.jkook.message.component.card.element.PlainTextElement;
import snw.jkook.message.component.card.module.ImageGroupModule;
import snw.jkook.message.component.card.module.SectionModule;
import snw.kookbc.impl.CoreImpl;
import snw.kookbc.impl.KBCClient;
import snw.kookbc.impl.entity.builder.MessageBuilder;
import snw.kookbc.impl.entity.channel.TextChannelImpl;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KookBot {

    private McKook plugin;

    private KBCClient kbcClient;

    private boolean invalid;

    /**
     * 使用频道
     */
    private Map<String,Channel> channelMap;

    public KookBot(McKook plugin){

        this.plugin = plugin;

        try {
            InputStreamReader reader = new InputStreamReader(plugin.getResource(
                    "kbc.yml"
            ));
            kbcClient = new KBCClient(new CoreImpl(), YamlConfiguration
                    .loadConfiguration(reader),
                    new File(plugin.getDataFolder(),"plugins"),
                    plugin.getConfig().getString("kook.bot-token")
                    );
            kbcClient.start();

            channelMap = new HashMap<>();
            ConfigurationSection channelConfig = plugin.getConfig().getConfigurationSection("setting.channel");
            channelConfig.getKeys(false).forEach(name->channelMap.put(name,httpAPI().getChannel(channelConfig.getString(name))));

            plugin.getLogger().info("已连接kook bot");
        } catch (Exception e) {
            invalid = true;
            plugin.getLogger().info("token填写错误,当前插件不可用.检查配置正确后使用 /mkook reload 重载");
        }

    }

    /**
     * 是否不可用
     * @return
     */
    public boolean isInvalid() {
        return invalid;
    }

    public void registerKookListener(Listener listener){
        kbcClient.getCore().getEventManager().registerHandlers(kbcClient.getInternalPlugin(),listener);
    }

    public void unRegisterKookListener(){
        kbcClient.getCore().getEventManager().unregisterAllHandlers(kbcClient.getInternalPlugin());
    }

    public Map<String, Channel> getChannelMap() {
        return channelMap;
    }

    /**
     * 关闭链接
     */
    public void close(){
        if(isInvalid()) return;
        kbcClient.shutdown();
    }

    public KBCClient getKbcClient() {
        return kbcClient;
    }

    public HttpAPI httpAPI(){
        return getKbcClient().getCore().getHttpAPI();
    }

    public void sendMessage(List<String> channels,BaseComponent baseComponent){
        Bukkit.getScheduler().runTaskAsynchronously(plugin,()->{
            channels.forEach(channelId->{
                Channel channel = channelMap.get(channelId);
                if(channel instanceof TextChannel textChannel){
                    textChannel.sendComponent(baseComponent);
                }
            });
        });
    }

}
