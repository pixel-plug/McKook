package com.meteor.mckook.message.sub;

import com.meteor.mckook.McKook;
import com.meteor.mckook.kook.service.LinkService;
import com.meteor.mckook.message.AbstractKookMessage;
import com.meteor.mckook.model.link.KookUser;
import com.meteor.mckook.model.link.LinkCache;
import com.meteor.mckook.util.BaseConfig;
import com.meteor.mckook.util.TextComponentHelper;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import snw.jkook.event.EventHandler;
import snw.jkook.event.pm.PrivateMessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;

public class PlayerLinkMessage extends AbstractKookMessage {

    private LinkService linkService;

    private String successLinkMessage;
    private String successLinkMessageMinecraft;

    private boolean enableWhiteList;


    public PlayerLinkMessage(McKook plugin, YamlConfiguration yamlConfiguration) {
        super(plugin, yamlConfiguration);
        this.linkService = plugin.getKookBot().getService(LinkService.class);
        this.successLinkMessage = yamlConfiguration.getString("message.success.kook");
        this.successLinkMessageMinecraft = yamlConfiguration.getString("message.success.minecraft");
    }

    @Override
    public String getName() {
        return "绑定KOOK账号";
    }

    @EventHandler
    public void inputVerifyCode(PrivateMessageReceivedEvent privateMessageReceivedEvent){

        String verifyCode = privateMessageReceivedEvent.getMessage().getComponent().toString();
        getPlugin().getLogger().info("收到私聊消息: "+verifyCode);
        LinkCache linkCache = linkService.getLinkCache(verifyCode);
        if(linkCache!=null){
            String playerName = linkCache.getPlayerName();
            Map<String,String> params = new HashMap<>();
            params.put("player",linkCache.getPlayerName());
            KookUser kookUser = linkService.link(playerName, privateMessageReceivedEvent.getUser());
            privateMessageReceivedEvent.getUser().sendPrivateMessage(TextComponentHelper.json2CardComponent(successLinkMessage,params));

            Player player = Bukkit.getPlayerExact(playerName);
            if(player!=null){
                params.put("@user-nickname@",kookUser.getNickName());
                BaseConfig.instance.getMessageBox().getMessageList(params,this.successLinkMessageMinecraft)
                        .forEach(s->{
                            player.sendMessage(s);
                        });
            }
        }

    }

}
