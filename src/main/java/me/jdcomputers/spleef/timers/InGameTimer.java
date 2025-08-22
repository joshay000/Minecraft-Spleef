package me.jdcomputers.spleef.timers;

import me.jdcomputers.events.SpleefGameCancelledEvent;
import me.jdcomputers.files.FileManager;
import me.jdcomputers.spleef.SpleefGame;
import me.jdcomputers.spleef.SpleefPlayer;
import me.jdcomputers.worldedit.WorldEditCreations;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class InGameTimer extends GameTimer {
    private FileManager arena;
    private int times;

    public InGameTimer(SpleefGame game, long delay) {
        super(game, 30, 5, delay, 20L);

        arena = game.getPlugin().getArenas().load();
    }

    @Override
    protected void timerTick() {
        if (game.getPlayingPlayers().size() < 2) {
            SpleefGameCancelledEvent event = new SpleefGameCancelledEvent(game);

            Bukkit.getPluginManager().callEvent(event);
        }
    }

    @Override
    protected void timerInitialized() {
        maximum = Math.min(game.getLivingPlayers().size() * 5, 30);

        times++;

        if (times > 10 && game.getLivingPlayers().size() == 2)
            maximum /= 2;

        arena = game.getPlugin().getArenas().load();

        for (SpleefPlayer player : game.getPlayingPlayers())
            player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 600, 1, true, false, false));
    }

    @Override
    protected boolean timerPast() {
        Location loc = WorldEditCreations.ARENA_LOCATION.clone();

        int minX = loc.getBlockX();
        int minY = loc.getBlockY();
        int minZ = loc.getBlockZ();
        int maxX = minX + 100;
        int maxY = minY + 50;
        int maxZ = minZ + 100;

        String type = arena.getString(game.getArena() + ".type");
        boolean water = type.equalsIgnoreCase("water");

        for (int x = minX; x <= maxX; x++) {
            for (int y = maxY; y >= minY; y--) {
                for (int z = minZ; z <= maxZ; z++) {
                    Location newLoc = new Location(loc.getWorld(), x, y, z);

                    if (!newLoc.getBlock().getType().isAir())
                        continue;

                    Location under = newLoc.clone().subtract(0, 1, 0);

                    Block blockUnder = under.getBlock();

                    if (blockUnder.getType() != Material.BEDROCK && blockUnder.getType() != Material.WATER && blockUnder.getType() != Material.LAVA)
                        continue;

                    newLoc.getBlock().setType(water ? Material.WATER : Material.LAVA);
                }
            }
        }

        return true;
    }

    @Override
    protected void timerIncrement(int second) {
        String type = arena.getString(game.getArena() + ".type");

        for (SpleefPlayer player : game.getPlayers())
            player.sendMessage(ChatColor.GOLD + "The " + type + " level will rise in " + ChatColor.GREEN + second + ChatColor.GOLD + " seconds.");
    }

    @Override
    protected void timerUpcoming(int second) {
        String type = arena.getString(game.getArena() + ".type");

        for (SpleefPlayer player : game.getPlayers())
            player.sendMessage(ChatColor.RED + "The " + type + " level will rise in " + ChatColor.WHITE + second + ChatColor.RED + " " + (second == 1 ? "second." : "seconds."));
    }
}
