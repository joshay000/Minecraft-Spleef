package me.jdcomputers.src;

import me.jdcomputers.files.FileManager;
import me.jdcomputers.spleef.SpleefGame;
import me.jdcomputers.spleef.SpleefPlayer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;

import java.util.List;
import java.util.Random;

public class MyListener implements Listener {
    private final Spleef spleef;

    public MyListener(Spleef spleef) {
        this.spleef = spleef;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FileManager config = spleef.getSpleefConfig().load();
        FileManager arena = spleef.getArenas().load();
        SpleefGame game = spleef.getGame();

        if (!game.hasPlayer(player))
            game.addPlayer(player).setup();

        if (!game.isInGame()) {
            if (config.has("lobby"))
                player.teleport(config.getLocation("lobby"));

            player.setGameMode(GameMode.SURVIVAL);
            player.sendMessage(ChatColor.GREEN + "A new game is about to begin. Prepare yourself!");

            return;
        }

        player.teleport(arena.getLocation("arenas." + game.getArena() + ".spawn"));
        player.setGameMode(GameMode.SPECTATOR);
        player.sendMessage(ChatColor.RED + "A game is already in progress. Please spectate until the next game.");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        SpleefGame game = spleef.getGame();

        game.removePlayer(player);

        List<SpleefPlayer> players = game.getPlayingPlayers();

        for (SpleefPlayer p : players)
            p.getPlayer().playSound(p.getPlayer().getLocation().add(0, 50, 0), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.0f);
    }

    @EventHandler
    public void onHitPlayer(EntityDamageByEntityEvent event) {
        Entity start = event.getEntity();
        Entity end = event.getDamager();

        if (!(start instanceof Player target))
            return;

        if (!(end instanceof Player damager))
            return;

        event.setCancelled(true);

        damager.sendMessage(ChatColor.RED + "You cannot PVP on the spleef server.");
    }

    @EventHandler
    public void onPrematureDig(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (player.getGameMode() == GameMode.CREATIVE)
            return;

        SpleefGame game = spleef.getGame();

        if (game.isInGame() && game.isRunning() && !game.isInWait())
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onNormalDig(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (player.getGameMode() == GameMode.CREATIVE)
            return;

        SpleefGame game = spleef.getGame();

        if (!game.isInGame() || !game.isRunning() || game.isInWait())
            return;

        FileManager config = spleef.getSpleefConfig();

        int percent = config.getInt("drop_concrete_likelihood");

        Random random = new Random();

        if (random.nextInt(100 / percent) == 0)
            return;

        event.setDropItems(false);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if (player.getGameMode() == GameMode.CREATIVE)
            return;

        SpleefGame game = spleef.getGame();

        if (game.isInGame() && game.isRunning() && !game.isInWait())
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.getGameMode() != GameMode.SURVIVAL)
            return;

        Location loc = player.getLocation();

        Block in = loc.getBlock();
        Block under = loc.subtract(0, 1, 0).getBlock();

        if (in.getType() != Material.WATER && in.getType() != Material.LAVA && under.getType() != Material.BEDROCK)
            return;

        SpleefGame game = spleef.getGame();

        if (!game.isInGame() || !game.isRunning() || game.isInWait())
            return;

        SpleefPlayer sp = game.getPlayer(player);

        sp.kill();

        List<SpleefPlayer> remainingPlayers = game.getLivingPlayers();

        int remaining = remainingPlayers.size();

        if (remaining > 0) {
            for (SpleefPlayer p : game.getPlayingPlayers()) {
                p.sendMessage(ChatColor.GREEN + sp.getPlayer().getName() + ChatColor.GOLD + " has died. There are now " + ChatColor.WHITE + remaining + ChatColor.GOLD + " " + (remaining == 1 ? "player" : "players") + " remaining...");
                p.getPlayer().playSound(p.getPlayer().getLocation().add(0, 50, 0), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.0f);
            }

            return;
        }

        game.end(null);

        // TODO: State the final winning player!
    }
}
