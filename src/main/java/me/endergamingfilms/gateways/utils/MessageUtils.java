package me.endergamingfilms.gateways.utils;

import me.endergamingfilms.gateways.Gateways;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.logging.Logger;

public class MessageUtils {
    private final Gateways plugin;
    public String prefix = "&b&lGateways &7";
    public final static String NL = "\n";
    public final static String SPACE = " ";

    public MessageUtils(@NotNull final Gateways instance) {
        plugin = instance;
    }

    /**
     * @param path String
     * @param type BOOLEAN, INT, DOUBLE, STRING[], STRING
     */
    public boolean grabConfig(final String path, boolean type) {
        return plugin.fileManager.getConfig().getBoolean(path);
    }

    /**
     * @param path String
     * @param type BOOLEAN, INT, DOUBLE, STRING[], STRING
     */
    public int grabConfig(final String path, int type) {
        return plugin.fileManager.getConfig().getInt(path);
    }

    /**
     * @param path String[]
     * @param type BOOLEAN, INT, DOUBLE, STRING[], STRING
     */
    public List<?> grabConfig(final String path, String[] type) {
        return plugin.fileManager.getConfig().getList(path);
    }

    /**
     * @param path String
     * @param type BOOLEAN, INT, DOUBLE, STRING[], STRING
     */
    public String grabConfig(final String path, String type) {
        return plugin.fileManager.getConfig().getString(path);
    }

    /**
     * @param path String
     * @param type BOOLEAN, INT, DOUBLE, STRING[], STRING
     */
    public double grabConfig(final String path, double type) {
        return plugin.fileManager.getConfig().getDouble(path);
    }

    /**
     * @param path String
     * @param file FileConfiguration (ex: plugin.fileManager.getMessages())
     * @return raw string
     */
    public String grabRaw(final String path, final FileConfiguration file) {
        String msg = file.getString(path);
        if (msg != null) {
            return msg;
        }
        return msg;
    }

    public String getFormattedMessage(final String path) {
        return format(grabRaw(path, plugin.fileManager.getMessages()));
    }

    public String getFormattedMessage(final String path, final boolean hasPrefix) {
        return format(grabRaw(path, plugin.fileManager.getMessages()), hasPrefix);
    }

    public String getFormattedMessage(final String path, final String name) {
        return format(replace(grabRaw(path, plugin.fileManager.getMessages()), name));
    }

    public String format(final String msg) {
        return prefix(msg);
    }

    public String format(final String msg, final boolean hasPrefix) {
        if (hasPrefix) {
            return prefix(msg);
        } else {
            return colorize(msg);
        }
    }

    public String replace(String msg, final String name) {
        msg = msg.replace("%portal%", name);
        return msg;
    }

    public int checkAmount(String amount) {
        if (amount.matches("[0-9]+")) {
            int number = Integer.parseInt(amount);
            if ( number < 64 )
                return  number;
        }
        return 1;
    }

    public void send(Player player, String message) {
        player.sendMessage(message);
    }

    public void send(Server server, String message) {
        server.broadcastMessage(message);
    }

    public void send(CommandSender sender, String message) {
        sender.sendMessage(message);
    }

    public void send(Player player, BaseComponent message) {
        player.spigot().sendMessage(message);
    }

    /**
     * Credit:
     * @YourCoal
     * https://www.spigotmc.org/threads/how-to-get-a-user-friendly-item-name.373484/
     */
    public String capitalize(String input) { //
        StringBuilder output = new StringBuilder();
        for (String s : input.split("_")) {
            output.append(s.substring(0, 1).toUpperCase()).append(s.substring(1).toLowerCase()).append(" ");
        }
        return output.substring(0, output.length() - 1);
    }

    /**
     * Credit:
     * @lokka30
     * https://github.com/lokka30/PhantomLib
     *
     * This method will join a prefix and message together and return a chat color translated string.
     *
     * @param msg    the message string
     * @return the colorized string of the prefix and msg joined together
     */
    private String prefix(final String msg) {
        return colorize(prefix + msg);
    }

    /**
     * This method will translate colour codes (e.g. &a) in the specified message.
     *
     * @param msg the message which should have colour codes translated
     * @return the translated string
     */
    public String colorize(final String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    /**
     * INFO = Logger.info
     * WARNING = Logger.warning
     * SEVERE = Logger.severe
     */
    public enum LogLevel {
        INFO, WARNING, SEVERE
    }

    /**
     * This method will log a message to the console.
     *
     * @param logLevel the LogLevel enum determining the severity of the logged message
     * @param msg      the message which should be sent in the log
     */
    public void log(final LogLevel logLevel, String msg) {
        msg = colorize(prefix + msg);
        Logger logger = Bukkit.getLogger();
        switch (logLevel) {
            case INFO:
                logger.info(msg);
                break;
            case WARNING:
                logger.warning(msg);
                break;
            case SEVERE:
                logger.severe(msg);
                break;
            default:
                throw new IllegalStateException("Undefined LogLevel: " + logLevel.toString());
        }
    }
}
