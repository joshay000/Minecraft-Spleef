package me.jdcomputers.commands.spleef;

import me.jdcomputers.commands.Command;
import me.jdcomputers.commands.spleef.lobby.LobbySetCommand;
import me.jdcomputers.commands.spleef.lobby.LobbyTeleportCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SpleefLobbyCommand extends Command {
    public SpleefLobbyCommand(Command parent) {
        super("Lobby", "Teleport and edit the lobby in creative mode.", parent);

        addSubCommand(new LobbySetCommand(this));
        addSubCommand(new LobbyTeleportCommand(this));
    }

    @Override
    public void run(Player player, String[] args) {
        player.sendMessage(ChatColor.RED + "Correct usage: /spleef lobby <set|teleport>");
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        List<String> output = new ArrayList<>();

        output.add("set");
        output.add("teleport");

        return output;
    }
}
