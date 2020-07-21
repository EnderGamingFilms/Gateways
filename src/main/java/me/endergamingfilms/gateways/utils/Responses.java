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
                plugin.fileManager.getMessages().getBoolean("no-permission.prefix"));
    }

    public String pluginReload(final long time) {
        return plugin.messageUtils.format("&dAll config files have been reloaded. &7(" + time + "ms)");
    }

    public String nonPlayer() {
        return plugin.messageUtils.getFormattedMessage("non-player");
    }

    //------------------------------------------

    /** |-------------- Gateway Responses --------------| */

    public String gatewayWrongKey() {
        return plugin.messageUtils.getFormattedMessage("gateway-invalid-key");
    }

    public String gatewayOpeningMessage(final String name) {
        return plugin.messageUtils.getFormattedMessage("gateway-opening-soon", name);
    }

    public String gatewayOpened(final String name) {
        return plugin.messageUtils.getFormattedMessage("gateway-opened", name);
    }

    public String gatewayClosed(final String name) {
        return plugin.messageUtils.getFormattedMessage("gateway-closed", name);
    }

    public String gatewayAlreadyOpen() {
        return plugin.messageUtils.getFormattedMessage("gateway-already-opened");
    }

    public String gatewayAlreadyClosed() {
        return plugin.messageUtils.getFormattedMessage("gateway-already-closed");
    }

    public String gatewayExists() {
        return plugin.messageUtils.getFormattedMessage("gateway-exists");
    }

    public String gatewayDoesNotExists() {
        return plugin.messageUtils.getFormattedMessage("gateway-missing");
    }

    public String gatewayCreationTimeout() {
        return plugin.messageUtils.getFormattedMessage("gateway-creation-timeout");
    }

    public String gatewayDeleted() {
        return plugin.messageUtils.getFormattedMessage("gateway-deletion");
    }

    public TextComponent listGateways() {
        TextComponent message = new TextComponent();
        message.addExtra(plugin.messageUtils.colorize("&9----- &bGateways &9-----") + NL);
        for (Map.Entry<String, Portal> entry : plugin.portalManager.getActivePortals().entrySet()) {
            Portal portal = entry.getValue();
            message.addExtra(plugin.messageUtils.colorize("&7 -> Portal Name: &b" + portal.getPortalName()) + NL);
            message.addExtra(plugin.messageUtils.colorize("&7 -> Custom Name: &f" + portal.getCustomName()) + NL);
            message.addExtra(plugin.messageUtils.colorize("&7 -> World: &6" + portal.getWorld().getName()) + NL);
            message.addExtra(plugin.messageUtils.colorize("&7 -> PortalKey:" + NL +
                    "&7     -> name=" + portal.getPortalKey().getKeyMeta().getDisplayName() + NL +
                    "&7     -> lore=" + portal.getPortalKey().getKeyMeta().getLore()) + NL);
            message.addExtra(plugin.messageUtils.colorize("&7 -> KeyBlock:&d" +
                    " x=" + portal.getKeyBlockLocation().getBlockX() +
                    ", y=" + portal.getKeyBlockLocation().getBlockY() +
                    ", z=" + portal.getKeyBlockLocation().getBlockZ()) + NL);
            message.addExtra(plugin.messageUtils.colorize("&7 -> Destination:&a" +
                    " x=" + portal.getDestination().getBlockX() +
                    ", y=" + portal.getDestination().getBlockY() +
                    ", z=" + portal.getDestination().getBlockZ()) +
                    " (world=" + portal.getDestination().getWorld().getName() + ")" + NL);
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
        return plugin.messageUtils.getFormattedMessage("help." + cmd, false);
    }

    public TextComponent getHelp(Player player) {
        TextComponent message = new TextComponent();
        message.addExtra(plugin.messageUtils.getFormattedMessage("help.header", false) + NL);
        if (player.hasPermission("gateways.reload"))
            message.addExtra(getHelp("reload") + NL);
        if (player.hasPermission("gateways.command.create"))
            message.addExtra(getHelp("create") + NL);
        if (player.hasPermission("gateways.command.remove"))
            message.addExtra(getHelp("remove") + NL);
        if (player.hasPermission("gateways.command.remove"))
            message.addExtra(getHelp("list") + NL);
        message.addExtra(plugin.messageUtils.colorize("       &7Author: " + plugin.getDescription().getAuthors().get(0) +
                "&7       |       Version: " + plugin.getDescription().getVersion()));
        return message;
    }
    //------------------------------------------

}
