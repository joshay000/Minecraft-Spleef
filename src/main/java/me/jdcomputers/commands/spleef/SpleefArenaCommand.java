package me.jdcomputers.commands.spleef;

import me.jdcomputers.commands.Command;
import org.bukkit.entity.Player;

import java.util.List;

public class SpleefArenaCommand extends Command {
    public SpleefArenaCommand(Command parent) {
        super("Arena", "Create, list, update, or remove arenas.", parent);
    }

    @Override
    public void run(Player player, String[] args) {
        player.sendMessage("Work in progress :(");
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        return null;
    }
}
