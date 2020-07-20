package me.endergamingfilms.gateways.commands;

import me.endergamingfilms.gateways.Gateways;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CreateCommand {
    private final Gateways plugin;

    public CreateCommand(@NotNull final Gateways instance) {
        this.plugin = instance;
    }

    public void run(Player player, String[] args) {
        if (!player.hasPermission("gateways.command.create")) {
            plugin.messageUtils.send(player, plugin.respond.noPerms());
            return;
        }

        if (args.length < 3) {
            plugin.messageUtils.send(player, plugin.respond.getHelp(args[0]));
            return;
        }
        plugin.portalManager.beginPortalCreation(player, args);
    }
}
