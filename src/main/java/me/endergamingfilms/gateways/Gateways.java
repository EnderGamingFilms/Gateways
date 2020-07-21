package me.endergamingfilms.gateways;

import me.endergamingfilms.gateways.commands.CommandManager;
import me.endergamingfilms.gateways.gateway.PortalManager;
import me.endergamingfilms.gateways.integrations.CMIHook;
import me.endergamingfilms.gateways.integrations.HeadDatabaseHook;
import me.endergamingfilms.gateways.utils.FileManager;
import me.endergamingfilms.gateways.utils.HookPlaceholderAPI;
import me.endergamingfilms.gateways.utils.MessageUtils;
import me.endergamingfilms.gateways.utils.Responses;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public final class Gateways extends JavaPlugin {
    public final FileManager fileManager = new FileManager(this);
    public final MessageUtils messageUtils = new MessageUtils(this);
    public final Responses respond = new Responses(this);
    public final CommandManager cmdManager = new CommandManager(this);
    public final CMIHook cmiHook = new CMIHook(this);
    public HeadDatabaseHook hdbHook = new HeadDatabaseHook(this);
    public PortalManager portalManager;

    @Override
    public void onEnable() {
        //Load Files
        messageUtils.log(MessageUtils.LogLevel.INFO, "&9Loading config files.");
        loadFiles();

        // Register commands
        messageUtils.log(MessageUtils.LogLevel.INFO, "&9Loading plugin commands.");
        cmdManager.registerCommands();

        // Register PlaceHolderAPI hook
        messageUtils.log(MessageUtils.LogLevel.INFO, "&9Loading plugin hooks.");
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new HookPlaceholderAPI(this).register(); // Hook into PAPI
        } else messageUtils.log(MessageUtils.LogLevel.WARNING, "&9Unable to load PAPI hook.");
        if (Bukkit.getPluginManager().isPluginEnabled("CMI")) {
            cmiHook.setup(); // Hook into CMI
        } else messageUtils.log(MessageUtils.LogLevel.WARNING, "&9Unable to load CMI hook.");
        if (Bukkit.getPluginManager().isPluginEnabled("HeadDatabase")) {
            hdbHook.setup(); // Hook into
        } else messageUtils.log(MessageUtils.LogLevel.WARNING, "&9Unable to load HeadDatabase]hook.");
        messageUtils.log(MessageUtils.LogLevel.INFO, "&9Plugin hooks finished loading.");

        // Register Listeners/Managers
        messageUtils.log(MessageUtils.LogLevel.INFO, "&9Loading in gateways.");
        portalManager = new PortalManager(this);

        // Setup Gateways
        if (!Bukkit.getPluginManager().isPluginEnabled("HeadDatabase")){
            fileManager.readGateways();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        fileManager.saveGateways();
        HandlerList.unregisterAll(this);
    }

    public void loadFiles() {
        fileManager.setup();
    }
}
