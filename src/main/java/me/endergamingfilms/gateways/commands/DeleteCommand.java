package me.endergamingfilms.gateways.commands;

import me.endergamingfilms.gateways.Gateways;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DeleteCommand {
    private final Gateways plugin;

    public DeleteCommand(@NotNull final Gateways instance) {
        this.plugin = instance;
    }

    public void run(Player player, String[] args) {
        if (!player.hasPermission("gateways.command.remove")) {
            plugin.messageUtils.send(player, plugin.respond.noPerms());
            return;
        }

        if (args.length < 2) {
            plugin.messageUtils.send(player, plugin.respond.getHelp("remove"));
            return;
        }
        if (plugin.portalManager.getActivePortals().containsKey(args[1])) {
            plugin.portalManager.removePortal(args[1]);
            plugin.fileManager.removePortalFromFile(args[1]);
            // Send success message
            plugin.messageUtils.send(player, plugin.respond.gatewayDeleted());
        } else
            plugin.messageUtils.send(player, plugin.respond.gatewayDoesNotExists());
    }
}
