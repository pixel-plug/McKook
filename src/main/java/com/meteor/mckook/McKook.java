package com.meteor.mckook;

import com.meteor.mckook.command.CommandManager;
import com.meteor.mckook.kook.KookBot;
import com.meteor.mckook.message.AbstractKookMessage;
import com.meteor.mckook.message.sub.PlayerChatMessage;
import com.meteor.mckook.message.sub.PlayerJoinMessage;
import com.meteor.mckook.message.sub.PlayerLinkMessage;
import com.meteor.mckook.message.sub.WhitelistMessage;
import com.meteor.mckook.storage.DataManager;
import com.meteor.mckook.util.BaseConfig;
import com.meteor.mckook.util.UpdateChecker;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class McKook extends JavaPlugin {

    private KookBot kookBot;
    private List<AbstractKookMessage> abstractKookMessages;

    private CommandManager commandManager;

    private Metrics metrics;

    @Override
    public void onEnable() {

        BaseConfig.init(this);
        DataManager.init(this);

        this.reload();

        commandManager = new CommandManager(this);
        commandManager.init();

        getCommand("mckook").setExecutor(commandManager);

        metrics = new Metrics(this,20690);

        getLogger().info("感谢你的使用！");
        getLogger().info("插件问题或建议请加群反馈 653440235");

        UpdateChecker.checkForUpdates(this);

    }

    public void reload(){

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

        Arrays.asList("PlayerJoinMessage","PlayerChatMessage","PlayerLinkKookMessage","WhitelistMessage").forEach(message -> {
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

        PlayerLinkMessage playerLinkMessage = new PlayerLinkMessage(this,YamlConfiguration.loadConfiguration(
                new File(getDataFolder(),"message/PlayerLinkKookMessage.yml")
        ));
        abstractKookMessages.add(playerLinkMessage);
        playerLinkMessage.register();

        WhitelistMessage whitelistMessage = new WhitelistMessage(this,YamlConfiguration.loadConfiguration(
                new File(getDataFolder(),"message/WhitelistMessage.yml")
        ));
        abstractKookMessages.add(whitelistMessage);
        whitelistMessage.register();
    }

    @Override
    public void onDisable() {
        DataManager.instance.close();
        getKookBot().close();
    }

    public KookBot getKookBot() {
        return kookBot;
    }
}
