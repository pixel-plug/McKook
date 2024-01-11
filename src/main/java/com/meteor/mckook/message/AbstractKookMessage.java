package com.meteor.mckook.message;

import com.meteor.mckook.McKook;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractKookMessage implements Listener, snw.jkook.event.Listener {

    private McKook plugin;
    private List<String> useChannelList;


    public AbstractKookMessage(McKook plugin,YamlConfiguration yamlConfiguration){
        this.plugin = plugin;
        this.useChannelList = yamlConfiguration.getStringList("channels");
    }


    protected McKook getPlugin() {
        return plugin;
    }

    public abstract String getName();

    public List<String> getUseChannelList(){
        return useChannelList;
    }


    public void register(){
        this.plugin.getServer().getPluginManager().registerEvents(this,plugin);
        getPlugin().getKookBot().registerKookListener(this);
    }

    public Map<String,String> context(PlayerEvent playerEvent){
        Map<String,String> context = new HashMap<>();
        context.put("player",playerEvent.getPlayer().getName());
        return context;
    }

    public void unRegister(){
        HandlerList.unregisterAll((Listener)this);
    }
}
