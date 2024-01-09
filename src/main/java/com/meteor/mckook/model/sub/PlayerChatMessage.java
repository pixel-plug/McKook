package com.meteor.mckook.model.sub;

import com.meteor.mckook.McKook;
import com.meteor.mckook.model.AbstractKookMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import snw.jkook.entity.User;
import snw.jkook.entity.abilities.Accessory;
import snw.jkook.entity.channel.TextChannel;
import snw.jkook.event.EventHandler;
import snw.jkook.event.channel.ChannelMessageEvent;
import snw.jkook.message.component.card.CardBuilder;
import snw.jkook.message.component.card.Size;
import snw.jkook.message.component.card.Theme;
import snw.jkook.message.component.card.element.ImageElement;
import snw.jkook.message.component.card.element.PlainTextElement;
import snw.jkook.message.component.card.module.SectionModule;

import java.util.Arrays;

public class PlayerChatMessage extends AbstractKookMessage {

    private String kookMessage;
    private String minecraftMessage;

    private String channel;

    private String avatarUrl;


    public PlayerChatMessage(McKook plugin, YamlConfiguration yamlConfiguration) {
        super(plugin, yamlConfiguration);

        this.kookMessage = yamlConfiguration.getString("message.kook");
        this.minecraftMessage = yamlConfiguration.getString("message.minecraft");
        this.channel = yamlConfiguration.getString("channel");
        this.avatarUrl = yamlConfiguration.getString("avatar-url");


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
        CardBuilder cardBuilder = new CardBuilder();
        cardBuilder.setSize(Size.SM);
        cardBuilder.setTheme(Theme.INFO);
        String playerName = asyncPlayerChatEvent.getPlayer().getName();
        cardBuilder.addModule(new SectionModule(new PlainTextElement(kookMessage.replace("{player}",playerName)
                .replace("{message}",asyncPlayerChatEvent.getMessage())),new ImageElement(
                avatarUrl.replace("{player}",playerName),"chat",Size.SM,false), Accessory.Mode.LEFT));
        getPlugin().getKookBot().sendMessage(Arrays.asList(channel),cardBuilder.build());
    }


}
