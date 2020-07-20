package me.endergamingfilms.gateways.commands;

import me.endergamingfilms.gateways.Gateways;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GatewaysCommand extends BaseCommand {
    private final Gateways plugin;

    public GatewaysCommand(String command, @NotNull final Gateways instance) {
        super(command);
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            plugin.messageUtils.send(sender, plugin.respond.nonPlayer());
            return false;
        }

        Player player = (Player) sender;
        if (args.length < 1) {
            plugin.messageUtils.send(player, plugin.respond.getHelp(player));
            return false;
        }

        if (args[0].matches("rl|reload")) {
            plugin.cmdManager.reloadCmd.run(player);
        } else if (args[0].equalsIgnoreCase("create")) {
            if (!player.hasPermission("gateways.command.create")) return false;
            if (args.length < 2) {
                plugin.messageUtils.send(player, plugin.respond.getHelp(args[0]));
                return false;
            }
            plugin.portalManager.beginPortalCreation(player, args[1]);
        } else if (args[0].matches("rm|remove")) {
            if (!player.hasPermission("gateways.command.create")) return false;
            if (args.length < 2) {
                plugin.messageUtils.send(player, plugin.respond.getHelp("remove"));
                return false;
            }
            plugin.portalManager.getActivePortals().remove(args[1]);
            if (plugin.cmiHook.portalModule.getPortals().containsKey(args[1]))
                plugin.cmiHook.portalModule.removePortal(plugin.cmiHook.portalModule.getByName(args[1]));
        } else if (args[0].equalsIgnoreCase("list")) {
            plugin.messageUtils.send(player, plugin.respond.listPortals());
        } else {
            plugin.messageUtils.send(player, plugin.respond.getHelp(player));
            return false;
        }
        return true;
    }
}
