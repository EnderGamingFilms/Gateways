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
            plugin.cmdManager.createCmd.run(player, args);
        } else if (args[0].matches("rm|remove")) {
            plugin.cmdManager.deleteCmd.run(player, args);
        } else if (args[0].equalsIgnoreCase("list")) {
            if (!player.hasPermission("gateways.command.list")) {
                plugin.messageUtils.send(player, plugin.respond.noPerms());
                return false;
            }
            plugin.messageUtils.send(player, plugin.respond.listGateways());
        } else {
            plugin.messageUtils.send(player, plugin.respond.getHelp(player));
            return false;
        }
        return true;
    }
}
