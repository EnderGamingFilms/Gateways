package me.endergamingfilms.gateways.commands;

import me.endergamingfilms.gateways.Gateways;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    private final Gateways plugin;
    public List<BaseCommand> commandList = new ArrayList<>();
    public GatewaysCommand gatewaysCmd;
    public ReloadCommand reloadCmd;

    public CommandManager(@NotNull final Gateways instance) {
        this.plugin = instance;
    }

    public void registerCommands() {
        // Make Commands Accessible
        commandList.add(gatewaysCmd = new GatewaysCommand("gateways", plugin));

        // Register Sub-Commands "/market command"
        reloadCmd = new ReloadCommand(plugin);

        // Register BaseCommands "/command"
        for (BaseCommand command : commandList) {
            command.register();
        }
    }
}
