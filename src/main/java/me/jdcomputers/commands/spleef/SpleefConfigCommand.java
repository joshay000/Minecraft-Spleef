package me.jdcomputers.commands.spleef;

import me.jdcomputers.commands.Command;
import org.bukkit.entity.Player;

import java.util.List;

public class SpleefConfigCommand extends Command {
    public SpleefConfigCommand(Command parent) {
        super("Config", "Modify the configuration file for spleef.", parent);
    }

    @Override
    public void run(Player player, String[] args) {
        player.sendMessage("Work in progress :|");
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        return null;
    }
}
