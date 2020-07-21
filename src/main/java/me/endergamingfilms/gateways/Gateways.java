package me.endergamingfilms.gateways;

import me.endergamingfilms.gateways.commands.CommandManager;
import me.endergamingfilms.gateways.gateway.PortalManager;
import me.endergamingfilms.gateways.integrations.cmiHook;
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
    public final cmiHook cmiHook = new cmiHook(this);
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
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI") && Bukkit.getPluginManager().isPluginEnabled("CMI")) {
            // Hook into PAPI
            new HookPlaceholderAPI(this).register();
            // Hook into CMI
            cmiHook.setup();
            messageUtils.log(MessageUtils.LogLevel.INFO, "&9Plugin hooks successfully loaded.");
        } else {
            messageUtils.log(MessageUtils.LogLevel.WARNING, "&9Unable to load hooks.");
        }

        // Register Listeners/Managers
        messageUtils.log(MessageUtils.LogLevel.INFO, "&9Loading in gateways.");
        portalManager = new PortalManager(this);

        // Setup Gateways
        fileManager.readGateways();
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
