package me.jdcomputers.spleef.timers;

import me.jdcomputers.files.FileManager;
import me.jdcomputers.spleef.SpleefGame;
import me.jdcomputers.spleef.SpleefPlayer;
import me.jdcomputers.src.Spleef;
import me.jdcomputers.worldedit.WorldEditCreations;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NotInGameTimer extends GameTimer {
    private final SpleefGame game;

    public NotInGameTimer(SpleefGame game) {
        super(game.getPlugin(), 5, 5, 20L, false);

        this.game = game;

        timerInitialized();
    }

    @Override
    protected void timerInitialized() {
        FileManager config = spleef.getSpleefConfig().load();

        for (SpleefPlayer p : game.getPlayingPlayers()) {
            if (config.has("lobby"))
                p.getPlayer().teleport(config.getLocation("lobby"));

            p.setup();
        }

        World world = spleef.getArenaWorld().getWorld();

        if (world == null)
            return;

        List<Entity> items = world.getEntities();

        for (int i = items.size() - 1; i >= 0; i--)
            if (items.get(i) instanceof Item)
                items.get(i).remove();
    }

    @Override
    protected void timerPast() {
        FileManager config = spleef.getSpleefConfig().load();

        ItemStack pickaxe = config.getItemStack("digger");

        for (SpleefPlayer p : game.getPlayingPlayers()) {
            p.setup();
            p.getPlayer().getInventory().setItem(0, pickaxe);

            p.sendMessage(ChatColor.GOLD + "Creating arena; please be patient...");
        }
    }

    @Override
    protected void timerIncrement(int second) {
        for (SpleefPlayer p : game.getPlayingPlayers()) {
            p.sendMessage(ChatColor.GREEN + "The game will begin in " + ChatColor.WHITE + second + " seconds.");
            p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        }
    }

    @Override
    protected void timerUpcoming(int second) {
        for (SpleefPlayer p : game.getPlayingPlayers()) {
            float pitch = second / 6.0f + 0.5f;

            p.sendMessage(ChatColor.RED + "The game will begin in " + ChatColor.WHITE + second + ChatColor.RED + " " + (second == 1 ? "second." : "seconds."));
            p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, pitch);
        }
    }
}
