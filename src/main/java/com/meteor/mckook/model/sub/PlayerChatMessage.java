package com.meteor.mckook.model.sub;

import com.meteor.mckook.McKook;
import com.meteor.mckook.model.AbstractKookMessage;
import com.meteor.mckook.util.TextComponentHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import snw.jkook.entity.User;
import snw.jkook.event.EventHandler;
import snw.jkook.event.channel.ChannelMessageEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 聊天事件
 */
public class PlayerChatMessage extends AbstractKookMessage {

    private String kookMessage;
    private String minecraftMessage;

    private String channel;



    public PlayerChatMessage(McKook plugin, YamlConfiguration yamlConfiguration) {
        super(plugin, yamlConfiguration);

        this.kookMessage = yamlConfiguration.getString("message.kook");
        this.minecraftMessage = yamlConfiguration.getString("message.minecraft");
        this.channel = yamlConfiguration.getString("channel");

    }

    private String getMinecraftMessage(ChannelMessageEvent channelMessageEvent){

        User sender = channelMessageEvent.getMessage().getSender();

        return ChatColor.translateAlternateColorCodes('&',
                minecraftMessage.replace("{user}",sender.getName())
                        .replace("{message}",channelMessageEvent.getMessage().getComponent().toString()));
    }


    @Override
    public String getName() {
        return "聊天消息";
    }


    @EventHandler
    public void onChannelMessage(ChannelMessageEvent channelMessageEvent){
        if(channelMessageEvent.getChannel().getId().equalsIgnoreCase(getPlugin().getKookBot().getChannelMap().get(channel)
                .getId())){
            Bukkit.broadcastMessage(getMinecraftMessage(channelMessageEvent));
        }
    }


    @org.bukkit.event.EventHandler
    public void onChat(AsyncPlayerChatEvent asyncPlayerChatEvent){
        Map<String, String> context = context(asyncPlayerChatEvent);
        context.put("message",asyncPlayerChatEvent.getMessage());
        getPlugin().getKookBot().sendMessage(Arrays.asList(channel), TextComponentHelper.json2CardComponent(this.kookMessage,context));
    }


}
