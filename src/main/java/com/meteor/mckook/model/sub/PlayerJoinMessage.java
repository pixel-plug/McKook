package com.meteor.mckook.model.sub;

import com.meteor.mckook.McKook;
import com.meteor.mckook.model.AbstractKookMessage;
import com.meteor.mckook.util.TextComponentHelper;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;


import java.util.HashMap;
import java.util.Map;

/**
 * 加入事件
 */
public class PlayerJoinMessage extends AbstractKookMessage {

    private String joinMessage;
    private String quitMessage;

    public PlayerJoinMessage(McKook plugin,YamlConfiguration yamlConfiguration) {
        super(plugin,yamlConfiguration);
        this.joinMessage = yamlConfiguration.getString("message.join");
        this.quitMessage = yamlConfiguration.getString("message.quit");
    }

    @Override
    public String getName() {
        return "玩家加入消息";
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent joinEvent){
        Map<String,String> context = context(joinEvent);
        getPlugin().getKookBot().sendMessage(getUseChannelList(), TextComponentHelper.json2CardComponent(this.joinMessage,context));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent quitEvent){
        Map<String,String> context = context(quitEvent);
        getPlugin().getKookBot().sendMessage(getUseChannelList(), TextComponentHelper.json2CardComponent(this.quitMessage,context));
    }

}
