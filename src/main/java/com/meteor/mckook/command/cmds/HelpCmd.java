package com.meteor.mckook.command.cmds;

import com.meteor.mckook.McKook;
import com.meteor.mckook.command.SubCmd;
import com.meteor.mckook.util.BaseConfig;
import org.bukkit.command.CommandSender;

public class HelpCmd extends SubCmd {
    public HelpCmd(McKook plugin) {
        super(plugin);
    }

    @Override
    public String label() {
        return "help";
    }

    @Override
    public String getPermission() {
        return "mckook.use.help";
    }

    @Override
    public boolean playersOnly() {
        return false;
    }

    @Override
    public String usage() {
        return null;
    }

    @Override
    public void perform(CommandSender p0, String[] p1) {
        BaseConfig.instance.getMessageBox().getMessageList(null,"message.help")
                .forEach(s->p0.sendMessage(s));
    }
}
