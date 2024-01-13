package com.meteor.mckook.message.sub;

import com.meteor.mckook.McKook;
import com.meteor.mckook.kook.service.LinkService;
import com.meteor.mckook.message.AbstractKookMessage;
import com.meteor.mckook.model.link.KookUser;
import com.meteor.mckook.util.TextComponentHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import snw.jkook.entity.User;
import snw.jkook.event.EventHandler;
import snw.jkook.event.channel.ChannelMessageEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WhitelistMessage extends AbstractKookMessage {

    private String channel;
    private boolean enable;

    // 是否绕过原版白名单玩家的检测
    private boolean bypassWhitelist;

    private String reg;

    private String broadcastMessage;

    private String linkSuccessMessage;

    private String alreadyLinedMessage;
    private String playerAlreadyLinedMessage;
    private String bypassMessage;

    private LinkService linkService;

    public WhitelistMessage(McKook plugin, YamlConfiguration yamlConfiguration) {
        super(plugin, yamlConfiguration);
        this.channel = yamlConfiguration.getString("channel");
        this.enable = yamlConfiguration.getBoolean("setting.enable");
        this.bypassWhitelist = yamlConfiguration.getBoolean("setting.bypass-whitelisted");
        this.reg = yamlConfiguration.getString("setting.message");

        this.broadcastMessage = yamlConfiguration.getString("message.kook.broadcast");
        this.linkSuccessMessage = yamlConfiguration.getString("message.kook.link-success");
        this.alreadyLinedMessage = yamlConfiguration.getString("message.kook.already-link");
        this.playerAlreadyLinedMessage = yamlConfiguration.getString("message.kook.player-already-link");
        this.linkService = getPlugin().getKookBot().getService(LinkService.class);

        this.bypassMessage = yamlConfiguration.getString("message.bypass");
    }

    @Override
    public String getName() {
        return "白名单申请";
    }

    @org.bukkit.event.EventHandler
    public void onJoin(PlayerJoinEvent joinEvent){
        Player player = joinEvent.getPlayer();
        if(this.enable&&!linkService.isLinked(player.getName())){

            if(bypassWhitelist&&Bukkit.getWhitelistedPlayers().contains(Bukkit.getOfflinePlayer(joinEvent.getPlayer().getName()))) return;

            Bukkit.getScheduler().runTaskLater(getPlugin(),()->{
                player.kickPlayer(ChatColor.translateAlternateColorCodes('&',this.bypassMessage
                        .replace("{player}",player.getName())));
            },5L);

        }
    }

    @EventHandler
    public void onLink(ChannelMessageEvent channelMessageEvent){
        if(channelMessageEvent.getChannel().getId().equalsIgnoreCase(getPlugin().getKookBot().getChannelMap().get(channel)
                .getId())){

            String message = channelMessageEvent.getMessage().getComponent().toString();
            if(message.matches(reg)){

                Pattern compile = Pattern.compile(reg);
                Matcher matcher = compile.matcher(message);


                while (matcher.find()){
                    String player = matcher.group(1);
                    User sender = channelMessageEvent.getMessage().getSender();
                    if(linkService.isLinked(player)){
                        channelMessageEvent.getMessage().reply(TextComponentHelper.json2CardComponent(playerAlreadyLinedMessage
                        ,null));
                        return;
                    }else if(linkService.kookUserIsLinked(sender.getId())){
                        channelMessageEvent.getMessage().reply(TextComponentHelper.json2CardComponent(alreadyLinedMessage
                                ,null));
                        return;
                    }

                    linkService.link(player, sender);

                    Map<String,String> context = new HashMap<>();
                    context.put("player",player);

                    channelMessageEvent.getMessage().reply(TextComponentHelper.json2CardComponent(linkSuccessMessage,context));


                }

            }

        }
    }

}
