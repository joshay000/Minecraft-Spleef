package me.jdcomputers.commands.spleef;

import me.jdcomputers.commands.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class SpleefHelpCommand extends Command {
    public SpleefHelpCommand(Command parent) {
        super("Help", "Display all spleef commands.", parent);
    }

    @Override
    public void run(Player player, String[] args) {
        player.sendMessage(ChatColor.GREEN + "-=-=-=-=-=- SPLEEF HELP -=-=-=-=-=-");

        for (Command command : parent.getSubCommands()) {
            if (command == this)
                continue;

            player.sendMessage(ChatColor.GOLD + "/spleef " + command.getName().toLowerCase() + ChatColor.WHITE + " - " + ChatColor.RED + command.getDescription());
        }
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        return null;
    }
}
