package com.meteor.mckook.command.cmds;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.meteor.mckook.McKook;
import com.meteor.mckook.command.SubCmd;
import com.meteor.mckook.kook.service.LinkService;
import com.meteor.mckook.util.BaseConfig;
import okhttp3.OkHttpClient;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LinkCmd extends SubCmd {

    private LinkService linkService;

    private Cache<String,String> cache;


    public LinkCmd(McKook plugin) {
        super(plugin);
        this.linkService = plugin.getKookBot().getService(LinkService.class);

        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .maximumSize(500)
                .build();
    }

    @Override
    public String label() {
        return "link";
    }

    @Override
    public String getPermission() {
        return "mckook.use.link";
    }

    @Override
    public boolean playersOnly() {
        return true;
    }

    @Override
    public String usage() {
        return "绑定kook账号";
    }

    @Override
    public void perform(CommandSender p0, String[] p1) {

        Player player = (Player) p0;

        if(linkService.isLinked(player.getName())){
            player.sendMessage(BaseConfig.instance.getMessageBox().getMessage(null,"message.link.already-link"));
            return;
        }

        String playerName = player.getName();

        Map<String,String> params = new HashMap<>();

        // 存在未过期验证码
        if(cache.getIfPresent(playerName)!=null){
            params.put("@verify-code@",cache.getIfPresent(playerName));
            player.sendMessage(BaseConfig.instance.getMessageBox().getMessage(params,"message.link.exist-verify"));
            return;
        }

        String verifyCode = linkService.buildVerifyCode(playerName);
        params.put("@verify-code@",verifyCode);
        BaseConfig.instance.getMessageBox().getMessageList(params,"message.link.build-verify")
                .forEach(s->player.sendMessage(s));

        cache.put(playerName,verifyCode);

    }
}
