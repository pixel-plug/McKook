package com.meteor.mckook.command.cmds;

import com.meteor.mckook.McKook;
import com.meteor.mckook.command.SubCmd;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LinkCmd extends SubCmd {
    public LinkCmd(McKook plugin) {
        super(plugin);
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




    }
}
