package me.endergamingfilms.gateways.utils;


import me.endergamingfilms.gateways.Gateways;
import me.endergamingfilms.gateways.gateway.Portal;
import me.endergamingfilms.gateways.gateway.PortalKey;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
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
    private FileConfiguration keys;
    private File configFile;
    private File messageFile;
    private File gatewaysFile;
    private File keysFile;
    //------------------------------------------

    public void setup() {
        setupConfig();
        // Load settings
        reloadSettings();
        // Load everything else
        setupMessages();
        // Setup gateways.yml
        setupGateways();
        // Setup keys.yml
        setupKeys();
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
                gatewaysFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                plugin.messageUtils.log(MessageUtils.LogLevel.SEVERE, "&cThere was an issue creating Gateways.yml");
            }
        }
        gateways = YamlConfiguration.loadConfiguration(gatewaysFile);
    }

    public FileConfiguration getGateways() {
        return gateways;
    }

    public File getGatewaysFile() {
        return gatewaysFile;
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
            gateways.set(str + ".CustomName", portal.getCustomName());
            gateways.set(str + ".World", portal.getWorld().getName());
            gateways.set(str + ".IsOpen", portal.isOpened());
            gateways.set(str + ".KeyBlock.x", portal.getKeyBlockLocation().getX());
            gateways.set(str + ".KeyBlock.y", portal.getKeyBlockLocation().getY());
            gateways.set(str + ".KeyBlock.z", portal.getKeyBlockLocation().getZ());
            gateways.set(str + ".Destination.world", portal.getDestination().getWorld().getName());
            gateways.set(str + ".Destination.x", portal.getDestination().getX());
            gateways.set(str + ".Destination.y", portal.getDestination().getY());
            gateways.set(str + ".Destination.z", portal.getDestination().getZ());
            gateways.set(str + ".Destination.yaw", portal.getDestination().getYaw());
            gateways.set(str + ".Destination.pitch", portal.getDestination().getPitch());
            gateways.set(str + ".Bounds.Pos1.x", portal.getPos1().getX());
            gateways.set(str + ".Bounds.Pos1.y", portal.getPos1().getY());
            gateways.set(str + ".Bounds.Pos1.z", portal.getPos1().getZ());
            gateways.set(str + ".Bounds.Pos2.x", portal.getPos2().getX());
            gateways.set(str + ".Bounds.Pos2.y", portal.getPos2().getY());
            gateways.set(str + ".Bounds.Pos2.z", portal.getPos2().getZ());
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
     * |-------------- Keys.yml --------------|
     */
    public void setupKeys() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        keysFile = new File(plugin.getDataFolder(), "keys.yml");

        if (!keysFile.exists()) {
            try {
                keysFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                plugin.messageUtils.log(MessageUtils.LogLevel.SEVERE, "&cThere was an issue creating Keys.yml");
            }
        }
        keys = YamlConfiguration.loadConfiguration(keysFile);
    }

    public FileConfiguration getKeys() {
        return keys;
    }

    public File getKeysFile() {
        return keysFile;
    }

    public void readKeys() {
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
            PortalKey key = new PortalKey();
            key.setPortal(plugin.portalManager.getPortal(str));
            // If the portal could not be found in activePortals map
            if (key.getPortal() == null) continue;
            // Build new Key Item
            ItemStack itemStack;
            if (String.valueOf(keys.get(str + ".Type")).matches("(<hdb:.+>)")) { // If itemType is using HeadDB
                System.out.println("---->Trimmed TYPE: " + String.valueOf(keys.get(str + ".Type")).replaceAll("[<>]", "").split(":")[1]);
                itemStack = plugin.hdbHook.hdb.getItemHead(String.valueOf(keys.get(str + ".Type")).replaceAll("[<>]", "").split(":")[1]);
            } else { // If itemType is vanilla minecraft
                Material material = Material.getMaterial(String.valueOf(keys.get(str + ".Type")));
                itemStack = new ItemStack(material);
            }
            key.setKeyItem(itemStack);
            // Build new Key Item Meta
            ItemMeta itemMeta = itemStack.getItemMeta();
            assert itemMeta != null;
            itemMeta.setDisplayName(plugin.messageUtils.colorize(String.valueOf(keys.get(str + ".DisplayName"))));
            List<String> matLore = (List<String>) keys.getList(str + ".Lore");
            if (matLore != null) {
                List<String> colorized = new ArrayList<>();
                for (String s : matLore) {
                    colorized.add(plugin.messageUtils.colorize(s));
                }
                matLore = colorized;
            }
            itemMeta.setLore(matLore);
            itemMeta.getPersistentDataContainer().set(plugin.portalManager.isPortalKey, PersistentDataType.STRING, "true");
            itemMeta.setUnbreakable(true);
            itemMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE); //, ItemFlag.HIDE_ENCHANTS);
            key.setKeyMeta(itemMeta);
            // Update Key
            key.update();

            plugin.portalManager.addPortalKey(key);
        }

    }

    public void saveKeys() {
        List<String> keyList = new ArrayList<>();
        plugin.portalManager.getActivePortals().forEach((k,v) -> {
            keyList.add(k);
        });
        System.out.println("---->keyList: " + keyList.toString());

        for (String str : keyList) {
            System.out.println("---->currentPortalKey: " + str);
            System.out.println("---->inKeyChain? " + plugin.portalManager.getPortalKeys().containsKey(str));
            System.out.println("---->portalKeys: " + plugin.portalManager.getPortalKeys().toString());
            System.out.println("---->getKey: " + plugin.portalManager.getKey(plugin.portalManager.getPortal(str)).toString());
            PortalKey portalKey = plugin.portalManager.getKey(plugin.portalManager.getPortal(str));
            System.out.println("---->portalKeyNull? " + (portalKey == null));
            if (str == null) continue; // Fixes 99% of my probles
            String keyItemID;
            try {
                keyItemID = plugin.hdbHook.hdb.getItemID(portalKey.getKeyItem());
            } catch (NullPointerException ignored) {
                keyItemID = null;
            }
//            if (true) continue;
            if (keyItemID == null) {
                System.out.println("---->KeyItm: " + portalKey.getKeyItem().toString());
                keys.set(str + ".Type", portalKey.getKeyItem().getType().getKey().getKey());
            } else {
                keys.set(str + ".Type", "<hdb:" + plugin.hdbHook.hdb.getItemID(portalKey.getKeyItem()) + ">");
            }
            keys.set(str + ".DisplayName", portalKey.getKeyMeta().getDisplayName());
            if (portalKey.getKeyMeta().getLore() == null)
                keys.set(str + ".Lore", null);
            else
                keys.set(str + ".Lore", portalKey.getKeyMeta().getLore());
        }

        // Save Gateways File
        try {
            keys.save(keysFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeKeyFromFile(String path) {
        keys.set(path, null);
    }

    public void reloadKeys() {
        keys = YamlConfiguration.loadConfiguration(keysFile);
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
        reloadKeys();
        readGateways();
    }
    //------------------------------------------
}
