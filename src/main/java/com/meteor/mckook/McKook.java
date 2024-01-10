package com.meteor.mckook;

import com.meteor.mckook.kook.KookBot;
import com.meteor.mckook.model.AbstractKookMessage;
import com.meteor.mckook.model.sub.PlayerChatMessage;
import com.meteor.mckook.model.sub.PlayerJoinMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class McKook extends JavaPlugin {

    private KookBot kookBot;
    private List<AbstractKookMessage> abstractKookMessages;

    private Metrics metrics;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        File file = new File(getDataFolder(),"plugins");
        if(!file.exists()) file.mkdirs();

        this.reload();

        metrics = new Metrics(this,20690);
        getLogger().info("谢谢你的使用！");
        getLogger().info("插件问题请加群反馈 653440235");
    }

    public void reload(){

        reloadConfig();

        if(getKookBot()!=null&&!getKookBot().isInvalid()){
            getKookBot().unRegisterKookListener();
        }

        this.kookBot = new KookBot(this);

        if(abstractKookMessages!=null){
            abstractKookMessages.forEach(abstractKookMessage->{
                abstractKookMessage.unRegister();
            });
        }

        abstractKookMessages = new ArrayList<>();

        Arrays.asList("PlayerJoinMessage","PlayerChatMessage").forEach(message -> {
            String r;
            File file = new File(getDataFolder(),r = "message/"+message+".yml");
            if(!file.exists()) saveResource(r,false);
        });

        PlayerJoinMessage playerJoinMessage = new PlayerJoinMessage(this, YamlConfiguration.loadConfiguration(
                new File(getDataFolder(), "message/PlayerJoinMessage.yml")
        ));

        abstractKookMessages.add(playerJoinMessage);
        playerJoinMessage.register();

        PlayerChatMessage playerChatMessage = new PlayerChatMessage(this, YamlConfiguration.loadConfiguration(
                new File(getDataFolder(), "message/PlayerChatMessage.yml")
        ));

        abstractKookMessages.add(playerChatMessage);
        playerChatMessage.register();
    }

    @Override
    public void onDisable() {
        getKookBot().close();
    }

    public KookBot getKookBot() {
        return kookBot;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender.isOp()){
            reload();
            sender.sendMessage("reload done!");
        }
        return true;
    }
}
