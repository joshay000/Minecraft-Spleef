package me.jdcomputers.commands.spleef;

import me.jdcomputers.commands.Command;
import org.bukkit.entity.Player;

import java.util.List;

public class SpleefLobbyCommand extends Command {
    public SpleefLobbyCommand(Command parent) {
        super("Lobby", "Teleport and edit the lobby in creative mode.", parent);
    }

    @Override
    public void run(Player player, String[] args) {
        player.sendMessage("Work in progress :)");
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        return null;
    }
}
