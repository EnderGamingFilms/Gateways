package me.endergamingfilms.gateways.utils;


import me.endergamingfilms.gateways.Gateways;
import me.endergamingfilms.gateways.gateway.Portal;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    public static final boolean BOOLEAN = false;
    public static final int INT = 0;
    public static final double DOUBLE = 0.0;
    public static final String STRING = "";
    public static final String[] LIST = new String[0];
    private final Gateways plugin;

    public FileManager(@NotNull final Gateways instance) {
        this.plugin = instance;
    }

    /**
     * |-------------- Settings --------------|
     */
    public int defaultPortalOnTime;
    public boolean debug;
    public String selectionToolName;
    public String selectionToolType;
    public List<String> selectionToolLore;
    //------------------------------------------

    /**
     * |-------------- Files --------------|
     */
    private FileConfiguration config;
    private FileConfiguration messages;
    private FileConfiguration gateways;
    private File configFile;
    private File messageFile;
    private File gatewaysFile;
    //------------------------------------------

    public void setup() {
        setupConfig();
        // Load settings
        reloadSettings();
        // Load everything else
        setupMessages();
        // Setup gateways.yml
        setupGateways();
    }

    /**
     * |-------------- Config.yml --------------|
     */
    public void setupConfig() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        configFile = new File(plugin.getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            try {
                plugin.saveResource("config.yml", true);
                plugin.messageUtils.log(MessageUtils.LogLevel.WARNING, "&eConfig.yml did not exist so one was created");
            } catch (Exception e) {
                plugin.messageUtils.log(MessageUtils.LogLevel.SEVERE, "&cThere was an issue creating Config.yml");
            }
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public File getConfigFile() {
        return configFile;
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void reloadSettings() {
        this.selectionToolName = plugin.messageUtils.grabConfig("SelectionTool.name", STRING);
        this.selectionToolType = plugin.messageUtils.grabConfig("SelectionTool.type", STRING);
        this.selectionToolLore = (List<String>) plugin.messageUtils.grabConfig("SelectionTool.lore", LIST);
        this.defaultPortalOnTime = plugin.messageUtils.grabConfig("default-open-time", INT);
        this.debug = plugin.messageUtils.grabConfig("debug", BOOLEAN);
    }
    //------------------------------------------

    /**
     * |-------------- Message.yml --------------|
     */
    public void setupMessages() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        messageFile = new File(plugin.getDataFolder(), "messages.yml");

        if (!messageFile.exists()) {
            try {
                plugin.saveResource("messages.yml", true);
                plugin.messageUtils.log(MessageUtils.LogLevel.WARNING, "&eMessages.yml did not exist so one was created");
            } catch (Exception e) {
                plugin.messageUtils.log(MessageUtils.LogLevel.SEVERE, "&cThere was an issue creating Messages.yml");
            }
        }
        messages = YamlConfiguration.loadConfiguration(messageFile);
    }

    public FileConfiguration getMessages() {
        return messages;
    }

    public File getMessagesFile() {
        return messageFile;
    }

    public void reloadMessages() {
        messages = YamlConfiguration.loadConfiguration(messageFile);
        plugin.messageUtils.prefix = messages.getString("prefix");
    }
    //------------------------------------------

    /**
     * |-------------- Gateways.yml --------------|
     */
    public void setupGateways() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        gatewaysFile = new File(plugin.getDataFolder(), "gateways.yml");

        if (!gatewaysFile.exists()) {
            try {
//                plugin.saveResource("gateways.yml", true);
                gatewaysFile.createNewFile();
//                plugin.messageUtils.log(MessageUtils.LogLevel.WARNING, "&eGateways.yml did not exist so one was created");
            } catch (IOException e) {
                e.printStackTrace();
                plugin.messageUtils.log(MessageUtils.LogLevel.SEVERE, "&cThere was an issue creating Gateways.yml");
            }
        }
        gateways = YamlConfiguration.loadConfiguration(gatewaysFile);
    }

    public FileConfiguration getGateways() {
        return messages;
    }

    public File getGatewaysFile() {
        return messageFile;
    }

    public void readGateways() {
        // Check if the gateways.yml is empty
        try {
            BufferedReader reader = new BufferedReader(new FileReader(gatewaysFile));
            boolean empty = reader.readLine() == null;
            if (empty) return;
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // If the file is not empty
        List<String> portalList = gateways.getStringList("portals");
        for (String str : portalList) {
            if (str == null) continue;
            // Create new portal object
            Portal portal = new Portal(str, Bukkit.getWorld(String.valueOf(gateways.get(str + ".World"))));
            portal.setCustomName(String.valueOf(gateways.get(str + ".CustomName")));
            portal.setIsOpen(Boolean.parseBoolean(String.valueOf(gateways.get(str + ".IsOpen"))));
            Location keyBlockLoc = new Location(portal.getWorld(),
                    Double.parseDouble(String.valueOf(gateways.get(str + ".KeyBlock.x"))),
                    Double.parseDouble(String.valueOf(gateways.get(str + ".KeyBlock.y"))),
                    Double.parseDouble(String.valueOf(gateways.get(str + ".KeyBlock.z"))));
            portal.setKeyBlock(keyBlockLoc);

            Location destination = new Location(Bukkit.getWorld(String.valueOf(gateways.get(str + ".Destination.world"))),
                    Double.parseDouble(String.valueOf(gateways.get(str + ".Destination.x"))),
                    Double.parseDouble(String.valueOf(gateways.get(str + ".Destination.y"))),
                    Double.parseDouble(String.valueOf(gateways.get(str + ".Destination.z"))),
                    Float.parseFloat(String.valueOf(gateways.get(str + ".Destination.yaw"))),
                    Float.parseFloat(String.valueOf(gateways.get(str + ".Destination.pitch"))));
            portal.setDestination(destination);

            Location pos1 = new Location(portal.getWorld(),
                    Double.parseDouble(String.valueOf(gateways.get(str + ".Bounds.Pos1.x"))),
                    Double.parseDouble(String.valueOf(gateways.get(str + ".Bounds.Pos1.y"))),
                    Double.parseDouble(String.valueOf(gateways.get(str + ".Bounds.Pos1.z"))));
            portal.setPos1(pos1);

            Location pos2 = new Location(portal.getWorld(),
                    Double.parseDouble(String.valueOf(gateways.get(str + ".Bounds.Pos2.x"))),
                    Double.parseDouble(String.valueOf(gateways.get(str + ".Bounds.Pos2.y"))),
                    Double.parseDouble(String.valueOf(gateways.get(str + ".Bounds.Pos2.z"))));
            portal.setPos2(pos2);

            plugin.portalManager.addPortal(portal);
            plugin.cmiHook.createCMIPortal(portal);
        }

    }

    public void saveGateways() {
        List<String> portalList = new ArrayList<>();
        plugin.portalManager.getActivePortals().forEach((k,v) -> {
            portalList.add(k);
        });
        gateways.set("portals", portalList);

        portalList.forEach(str -> {
            Portal portal = plugin.portalManager.getPortal(str);
            String currentPortal = portal.getPortalName();
            gateways.set(currentPortal + ".CustomName", portal.getCustomName());
            gateways.set(currentPortal + ".World", portal.getWorld().getName());
            gateways.set(currentPortal + ".IsOpen", portal.isOpened());
            gateways.set(currentPortal + ".KeyBlock.x", portal.getKeyBlockLocation().getX());
            gateways.set(currentPortal + ".KeyBlock.y", portal.getKeyBlockLocation().getY());
            gateways.set(currentPortal + ".KeyBlock.z", portal.getKeyBlockLocation().getZ());
            gateways.set(currentPortal + ".Destination.world", portal.getDestination().getWorld().getName());
            gateways.set(currentPortal + ".Destination.x", portal.getDestination().getX());
            gateways.set(currentPortal + ".Destination.y", portal.getDestination().getY());
            gateways.set(currentPortal + ".Destination.z", portal.getDestination().getZ());
            gateways.set(currentPortal + ".Destination.yaw", portal.getDestination().getYaw());
            gateways.set(currentPortal + ".Destination.pitch", portal.getDestination().getPitch());
            gateways.set(currentPortal + ".Bounds.Pos1.x", portal.getPos1().getX());
            gateways.set(currentPortal + ".Bounds.Pos1.y", portal.getPos1().getY());
            gateways.set(currentPortal + ".Bounds.Pos1.z", portal.getPos1().getZ());
            gateways.set(currentPortal + ".Bounds.Pos2.x", portal.getPos2().getX());
            gateways.set(currentPortal + ".Bounds.Pos2.y", portal.getPos2().getY());
            gateways.set(currentPortal + ".Bounds.Pos2.z", portal.getPos2().getZ());
        });

        // Save Gateways File
        try {
            gateways.save(gatewaysFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removePortalFromFile(String path) {
        gateways.set(path, null);
    }

    public void reloadGateways() {
        gateways = YamlConfiguration.loadConfiguration(gatewaysFile);
    }

    //------------------------------------------

    /**
     * |-------------- General File Functions --------------|
     */
    public void reloadAll() {
        // Stage 1 - Reload messages.yml
        reloadMessages();
        // Stage 2 - Reload config.yml and load settings into plugin
        reloadConfig();
        reloadSettings();
        // Stage 3 - Remake Items
        plugin.portalManager.selectionHandler.makeSelectionTool();
        // Stage 4 - Read in portals and keys
        reloadGateways();
        readGateways();
    }
    //------------------------------------------
}
