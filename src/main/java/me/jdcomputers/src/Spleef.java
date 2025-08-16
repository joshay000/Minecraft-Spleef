package me.jdcomputers.src;

import me.jdcomputers.commands.CommandCollection;
import me.jdcomputers.files.FileManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class Spleef extends JavaPlugin {
    // TODO:
    // 1) Add arenas /spleef arena <list/edit/add/delete>
    // 2) Add permissions for /spleef arena (op only OR require detailed permissions [LOW PRIORITY]
    // 3) Exclude those in CREATIVE; everyone else forced participation
    // 4) Add /spleef lobby (edit the lobby)
    // 5) Add the spleef game
    // 6) Give Efficiency X pickaxe; arenas MUST be concrete
    // 7) Add flexibility between being able to retrieve and place concrete or not [Perhaps a %]
    // 8) Touch bedrock [step on], water [inside], or lava [inside]: SPECTATOR mode (game over)
    // 9) Battle royale aspect: Rising water/lava levels

    private final FileManager config = new FileManager(this, "", "config.yml");

    @Override
    public void onEnable() {
        this.getLogger().info("Enabled");
        this.getServer().getPluginManager().registerEvents(new MyListener(), this);
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Disabled");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        for (me.jdcomputers.commands.Command cmd : CommandCollection.COMMANDS) {
            if (!label.equalsIgnoreCase(cmd.getName()))
                continue;

            if (!(sender instanceof Player player)) {
                sender.sendMessage(ChatColor.RED + "This command can only be run by in-game players.");

                return false;
            }

            if (args.length == 0 || cmd.getSubCommands().isEmpty()) {
                cmd.run(player, args);

                return true;
            }

            for (String arg : args) {
                me.jdcomputers.commands.Command previous = cmd;

                cmd = cmd.find(arg);

                if (cmd == null) {
                    previous.run(player, args);

                    return true;
                }
            }

            cmd.run(player, args);

            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        for (me.jdcomputers.commands.Command cmd : CommandCollection.COMMANDS) {
            if (!label.equalsIgnoreCase(cmd.getName()))
                continue;

            if (!(sender instanceof Player player))
                return null;

            if (args.length == 0 || cmd.getSubCommands().isEmpty())
                return cmd.tabComplete(player, args);

            for (String arg : args) {
                me.jdcomputers.commands.Command previous = cmd;

                cmd = cmd.find(arg);

                if (cmd == null)
                    return previous.tabComplete(player, args);
            }

            return cmd.tabComplete(player, args);
        }

        return null;
    }
}
