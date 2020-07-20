package me.endergamingfilms.gateways.commands;

import me.endergamingfilms.gateways.Gateways;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand {
    private final Gateways plugin;

    public ReloadCommand(@NotNull final Gateways instance) {
        this.plugin = instance;
    }

    public void run(Player player) {
        if (!player.hasPermission("gateways.reload")) {
            plugin.messageUtils.send(player, plugin.respond.noPerms());
            return;
        }

        // Reload Actions
        long start = System.currentTimeMillis();
        plugin.fileManager.reloadAll();
        long end = System.currentTimeMillis();
        // Send Response
        plugin.messageUtils.send(player, plugin.respond.pluginReload(end - start));
    }
}
