package com.meteor.mckook.model.link;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.entity.Player;

/**
 * 绑定账户缓存
 */
@Data
@AllArgsConstructor
public class LinkCache {
    private String playerName;
}
