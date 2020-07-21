package me.endergamingfilms.gateways.commands;

import me.endergamingfilms.gateways.Gateways;
import me.endergamingfilms.gateways.gateway.PortalKey;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class GatewaysCommand extends BaseCommand {
    private final Gateways plugin;

    public GatewaysCommand(String command, @NotNull final Gateways instance, String... aliases) {
        super(command, null, null, null, Arrays.asList(aliases));
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
        } else if (args[0].equalsIgnoreCase("test")) { // --------- Test Command --------- \\
//            if (args.length == 3) {
//                if (args[1].equalsIgnoreCase("getkey")) {
                    String fullPortalName = "gateway_" + args[1];
                    System.out.println("---->passedName: " + fullPortalName);
                    System.out.println("---->portalIsActive: " + plugin.portalManager.getActivePortals().containsKey(fullPortalName));
                    if (!plugin.portalManager.getActivePortals().containsKey(fullPortalName)) return false;
                    PortalKey portalKey = plugin.portalManager.getPortal(fullPortalName).getPortalKey();
                    System.out.println("---->portalKey: " + portalKey);
                    if (portalKey == null) return false;
                    player.getInventory().addItem(portalKey.getKeyItem());
//                } else if (args[1].equalsIgnoreCase("head")) {
//                    player.getInventory().addItem(plugin.hdbHook.hdb.getItemHead(args[2]));
//                }
//            } else if (args.length == 2) {
//                if (args[1].equalsIgnoreCase("save")) {
//                    plugin.fileManager.saveGateways();
//                }
//            } else {
//                plugin.messageUtils.send(player, "ItemID = " + plugin.hdbHook.hdb.getItemID(player.getItemInHand()));
//            }
        } else if (args[0].matches("rm|rem|remove")) {
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
