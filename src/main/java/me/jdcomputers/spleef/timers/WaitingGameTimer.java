package me.jdcomputers.spleef.timers;

import me.jdcomputers.files.FileManager;
import me.jdcomputers.spleef.SpleefGame;
import me.jdcomputers.spleef.SpleefPlayer;
import me.jdcomputers.src.Spleef;
import me.jdcomputers.worldedit.WorldEditCreations;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WaitingGameTimer extends GameTimer {
    private final SpleefGame game;

    public WaitingGameTimer(SpleefGame game) {
        super(game.getPlugin(), 5, 5, 20L, true);

        this.game = game;

        timerInitialized();
    }

    @Override
    protected void timerInitialized() {
        Random random = new Random();

        FileManager arena = spleef.getArenas().load();

        List<String> names = new ArrayList<>(arena.getSection("arenas").getKeys(false));

        String name = "arenas." + names.get(random.nextInt(names.size()));

        Location from = arena.getLocation(name + ".from");
        Location to = arena.getLocation(name + ".to");
        Location spawnStored = arena.getLocation(name + ".spawn");
        Location arenaLoc = WorldEditCreations.ARENA_LOCATION;

        WorldEditCreations.clear(arenaLoc, 100, 50);
        WorldEditCreations.copy(from, to, arenaLoc);

        int xOffset = spawnStored.getBlockX() - Math.min(from.getBlockX(), to.getBlockX());
        int yOffset = spawnStored.getBlockY() - Math.min(from.getBlockY(), to.getBlockY());
        int zOffset = spawnStored.getBlockZ() - Math.min(from.getBlockZ(), to.getBlockZ());

        Location spawn = arenaLoc.clone().add(xOffset, yOffset, zOffset);

        for (SpleefPlayer p : game.getPlayingPlayers()) {
            p.sendMessage(ChatColor.GOLD + "Get ready! You have " + getMaximum() + " seconds before you can break blocks.");
            p.getPlayer().teleport(spawn);
            p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.BLOCK_END_PORTAL_SPAWN, 1.0f, 1.0f);
        }
    }

    @Override
    protected void timerPast() {
        for (SpleefPlayer p : game.getPlayingPlayers()) {
            p.sendMessage(ChatColor.GOLD + "The game has begun!");
            p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 0.8f);
        }
    }

    @Override
    protected void timerIncrement(int second) {
        for (SpleefPlayer p : game.getPlayingPlayers()) {
            p.sendMessage(ChatColor.GREEN + "Breaking blocks begins in " + ChatColor.WHITE + second + " seconds.");
            p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        }
    }

    @Override
    protected void timerUpcoming(int second) {
        for (SpleefPlayer p : game.getPlayingPlayers()) {
            p.sendMessage(ChatColor.RED + "Breaking blocks begins in " + ChatColor.WHITE + second + ChatColor.RED + " " + (second == 1 ? "second." : "seconds."));
            p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        }
    }
}
