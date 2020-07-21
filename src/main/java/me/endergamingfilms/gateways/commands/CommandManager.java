package me.endergamingfilms.gateways.commands;

import me.endergamingfilms.gateways.Gateways;
import me.endergamingfilms.gateways.gateway.listeners.OnKeyBlockClick;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    private final Gateways plugin;
    public List<BaseCommand> commandList = new ArrayList<>();
    public List<String> subCommandList = new ArrayList<>();
    public GatewaysCommand gatewaysCmd;
    public ReloadCommand reloadCmd;
    public CreateCommand createCmd;
    public DeleteCommand deleteCmd;

    public CommandManager(@NotNull final Gateways instance) {
        this.plugin = instance;
    }

    public void registerCommands() {
        // Make Commands Accessible
        commandList.add(gatewaysCmd = new GatewaysCommand("gateways", plugin, "gates", "gw"));
        // Register Sub-Commands "/market command"
        reloadCmd = new ReloadCommand(plugin);
        subCommandList.add("reload");
        createCmd = new CreateCommand(plugin);
        subCommandList.add("create");
        deleteCmd = new DeleteCommand(plugin);
        subCommandList.add("remove");
        subCommandList.add("list");
        subCommandList.add("givekey");
        subCommandList.add("help");
        // Register BaseCommands "/command"
        for (BaseCommand command : commandList) {
            command.register();
        }
    }
}
