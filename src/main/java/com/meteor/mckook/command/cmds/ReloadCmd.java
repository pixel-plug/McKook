package com.meteor.mckook.command.cmds;

import com.meteor.mckook.McKook;
import com.meteor.mckook.command.SubCmd;
import com.meteor.mckook.util.BaseConfig;
import org.bukkit.command.CommandSender;

public class ReloadCmd extends SubCmd {
    public ReloadCmd(McKook plugin) {
        super(plugin);
    }

    @Override
    public String label() {
        return "reload";
    }

    @Override
    public String getPermission() {
        return "mckook.admin.reload";
    }

    @Override
    public boolean playersOnly() {
        return false;
    }

    @Override
    public String usage() {
        return "重载";
    }

    @Override
    public void perform(CommandSender p0, String[] p1) {
        plugin.reload();
        BaseConfig.instance.reload();
        p0.sendMessage(BaseConfig.instance.getMessageBox().getMessage(null,"message.reload"));
    }
}
