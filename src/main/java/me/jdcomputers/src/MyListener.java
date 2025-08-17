package me.jdcomputers.src;

import me.jdcomputers.files.FileManager;
import me.jdcomputers.spleef.SpleefGame;
import me.jdcomputers.spleef.SpleefPlayer;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class MyListener implements Listener {
    private final Spleef spleef;

    public MyListener(Spleef spleef) {
        this.spleef = spleef;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FileManager config = spleef.getSpleefConfig();
        FileManager arena = spleef.getArenas();
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
}
