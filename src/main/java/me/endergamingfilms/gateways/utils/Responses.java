package me.endergamingfilms.gateways.utils;

import me.endergamingfilms.gateways.Gateways;
import me.endergamingfilms.gateways.gateway.Portal;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static me.endergamingfilms.gateways.utils.MessageUtils.NL;

public class Responses {
    private final Gateways plugin;
    public Responses(@NotNull final Gateways instance) {
        plugin = instance;
    }
    /** |-------------- Basic Responses --------------| */
    public String noPerms() {
        return plugin.messageUtils.getFormattedMessage("no-permission.message",
                plugin.fileManager.getMessages(),
                plugin.fileManager.getMessages().getBoolean("no-permission.prefix"));
    }

    public String pluginReload(final long time) {
        return plugin.messageUtils.format("&dAll config files have been reloaded. &7(" + time + "ms)");
    }

    public String nonPlayer() {
        return plugin.messageUtils.getFormattedMessage("non-player", plugin.fileManager.getMessages());
    }

    public TextComponent listPortals() {
        TextComponent message = new TextComponent();
        message.addExtra(plugin.messageUtils.colorize("&9----- &bPortals &9-----") + NL);
        for (Map.Entry<String, Portal> entry : plugin.portalManager.getActivePortals().entrySet()) {
            Portal portal = entry.getValue();
            message.addExtra(plugin.messageUtils.colorize("&7 -> Portal Name: &b" + portal.getPortalName()) + NL);
            message.addExtra(plugin.messageUtils.colorize("&7 -> World: &6" + portal.getWorld().getName()) + NL);
            message.addExtra(plugin.messageUtils.colorize("&7 -> KeyBlock:&d" +
                    " x=" + portal.getKeyBlockLocation().getBlockX() +
                    ", y=" + portal.getKeyBlockLocation().getBlockY() +
                    ", z=" + portal.getKeyBlockLocation().getBlockZ()) + NL);
            message.addExtra(plugin.messageUtils.colorize("&7 -> Pos1:&e" +
                    " x=" + portal.getPos1().getBlockX() +
                    ", y=" + portal.getPos1().getBlockX() +
                    ", z=" + portal.getPos1().getBlockX()) + NL);
            message.addExtra(plugin.messageUtils.colorize("&7 -> Pos2:&e" +
                    " x=" + portal.getPos2().getBlockX() +
                    ", y=" + portal.getPos2().getBlockX() +
                    ", z=" + portal.getPos2().getBlockX()) + NL + NL);
        }
        message.addExtra(plugin.messageUtils.colorize("&9-------------------"));
        return message;
    }

    //------------------------------------------

    /** |-------------- Help/Usage Responses --------------| */
    public String getHelp(final String cmd) {
        return plugin.messageUtils.getFormattedMessage("help." + cmd, plugin.fileManager.getMessages(), false);
    }

    public TextComponent getHelp(Player player) {
        TextComponent message = new TextComponent();
        message.addExtra(plugin.messageUtils.getFormattedMessage("help.header", plugin.fileManager.getMessages(), false) + NL);
        if (player.hasPermission("gateways.reload"))
            message.addExtra(getHelp("reload") + NL);
        if (player.hasPermission("gateways.command.create"))
            message.addExtra(getHelp("create") + NL);
        if (player.hasPermission("gateways.command.set"))
            message.addExtra(getHelp("remove") + NL);
        if (player.hasPermission("gateways.command.remove"))
            message.addExtra(getHelp("list") + NL);
        message.addExtra(plugin.messageUtils.colorize("       &7Author: " + plugin.getDescription().getAuthors().get(0) +
                "&7       |       Version: " + plugin.getDescription().getVersion()));
        return message;
    }
    //------------------------------------------

}
