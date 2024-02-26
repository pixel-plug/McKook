package com.meteor.mckook.message.sub;

import com.meteor.mckook.McKook;
import com.meteor.mckook.message.AbstractKookMessage;
import com.meteor.mckook.util.BaseConfig;
import com.meteor.mckook.util.TextComponentHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import snw.jkook.entity.User;
import snw.jkook.event.EventHandler;
import snw.jkook.event.channel.ChannelMessageEvent;

import java.util.Arrays;
import java.util.List;
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


    private boolean isInPassWorld(Player player){
        List<String> blackWorlds = BaseConfig.instance.getConfig().getStringList("setting.black-worlds");
        return blackWorlds==null || !blackWorlds.contains(player.getWorld().getName());
    }

    @EventHandler
    public void onChannelMessage(ChannelMessageEvent channelMessageEvent){
        if(channelMessageEvent.getChannel().getId().equalsIgnoreCase(getPlugin().getKookBot().getChannelMap().get(channel)
                .getId())){
            Bukkit.getOnlinePlayers().forEach(player -> {
                if(isInPassWorld(player))
                    player.sendMessage(getMinecraftMessage(channelMessageEvent));
            });
        }
    }


    @org.bukkit.event.EventHandler
    public void onChat(AsyncPlayerChatEvent asyncPlayerChatEvent){
        Player player = asyncPlayerChatEvent.getPlayer();
        if(isInPassWorld(player)){
            Map<String, String> context = context(asyncPlayerChatEvent);
            context.put("message",asyncPlayerChatEvent.getMessage());
            getPlugin().getKookBot().sendMessage(Arrays.asList(channel), TextComponentHelper.json2CardComponent(this.kookMessage,context));
        }
    }
}
