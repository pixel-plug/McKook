package com.meteor.mckook.model.sub;

import com.meteor.mckook.McKook;
import com.meteor.mckook.model.AbstractKookMessage;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import snw.jkook.entity.abilities.Accessory;
import snw.jkook.entity.channel.Channel;
import snw.jkook.entity.channel.TextChannel;
import snw.jkook.message.component.card.CardBuilder;
import snw.jkook.message.component.card.Size;
import snw.jkook.message.component.card.Theme;
import snw.jkook.message.component.card.element.ImageElement;
import snw.jkook.message.component.card.element.PlainTextElement;
import snw.jkook.message.component.card.module.SectionModule;

import java.util.HashMap;
import java.util.Map;

public class PlayerJoinMessage extends AbstractKookMessage {

    private String avatarUrl;
    private String message;

    public PlayerJoinMessage(McKook plugin,YamlConfiguration yamlConfiguration) {
        super(plugin,yamlConfiguration);
        this.avatarUrl = yamlConfiguration.getString("avatar-url");
        this.message = yamlConfiguration.getString("message");
    }

    @Override
    public String getName() {
        return "玩家加入消息";
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent joinEvent){
        CardBuilder cardBuilder = new CardBuilder();
        cardBuilder.setSize(Size.SM);
        cardBuilder.setTheme(Theme.INFO);
        String playerName = joinEvent.getPlayer().getName();
        cardBuilder.addModule(new SectionModule(new PlainTextElement(message.replace("{player}",playerName)),new ImageElement(
                avatarUrl.replace("{player}",playerName),"join",Size.SM,false), Accessory.Mode.LEFT));
        getPlugin().getKookBot().sendMessage(getUseChannelList(),cardBuilder.build());
    }
}
