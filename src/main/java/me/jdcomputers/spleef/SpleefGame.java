package me.jdcomputers.spleef;

import me.jdcomputers.files.FileManager;
import me.jdcomputers.spleef.timers.GameOverTimer;
import me.jdcomputers.spleef.timers.GameTimer;
import me.jdcomputers.spleef.timers.NotInGameTimer;
import me.jdcomputers.spleef.timers.WaitingGameTimer;
import me.jdcomputers.src.Spleef;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SpleefGame {
    private static final int GAME_SETUP_TIMER_MAX = 5;
    private static final int GAME_IN_WAIT_TIMER_MAX = 5;
    private static final int GAME_POST_TIMER_MAX = 5;
    private static final int GAME_STATEMENT_INCREMENT = 5;

    private final Spleef spleef;
    private final List<SpleefPlayer> players;

    private String arena;
    private int gameTimer;
    private int levelTimer;
    private GameTimer timer;

    public SpleefGame(Spleef spleef) {
        this.spleef = spleef;

        players = new ArrayList<>();
    }

    public void start() {
        timer = new NotInGameTimer(this, 40L);
    }

    public void setTimer(GameTimer timer) {
        this.timer = timer;
    }

    public void end(SpleefPlayer winner) {
        String name = "Nobody";

        if (winner != null) {
            name = winner.getPlayer().getName();

            winner.getPlayer().playSound(winner.getPlayer().getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1.0f, 1.0f);
            winner.getPlayer().sendTitle(ChatColor.GOLD + "You Won", "", 10, 60, 10);
            winner.sendMessage(ChatColor.GOLD + "You will be teleported to the lobby in " + ChatColor.WHITE + GAME_POST_TIMER_MAX + ChatColor.GOLD + " seconds.");
        }

        for (SpleefPlayer player : getPlayingPlayers()) {
            if (player != winner) {
                player.sendMessage(ChatColor.GOLD + "The game is over! " + ChatColor.GREEN + name + ChatColor.GOLD + " won. You will be teleported to the lobby in " + ChatColor.WHITE + GAME_POST_TIMER_MAX + ChatColor.GOLD + " seconds.");
                player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 1.0f, 0.8f);
            }
        }

        gameTimer = 0;
        levelTimer = 0;

        timer.end();
        timer = new GameOverTimer(this, 0L);
    }

    public SpleefPlayer addPlayer(Player player) {
        SpleefPlayer output = new SpleefPlayer(player);

        players.add(output);

        return output;
    }

    public boolean hasPlayer(Player player) {
        for (SpleefPlayer p : players)
            if (player.getUniqueId().equals(p.getPlayer().getUniqueId()))
                return true;

        return false;
    }

    public void removePlayer(Player player) {
        for (int i = players.size() - 1; i >= 0; i--) {
            if (player.getUniqueId().equals(players.get(i).getPlayer().getUniqueId())) {
                players.remove(i);

                return;
            }
        }
    }

    public void clearPlayers() {
        players.clear();
    }

    public SpleefPlayer getPlayer(Player player) {
        for (SpleefPlayer p : players)
            if (player.getUniqueId().equals(p.getPlayer().getUniqueId()))
                return p;

        return null;
    }

    public List<SpleefPlayer> getPlayers() {
        return players;
    }

    public List<SpleefPlayer> getPlayingPlayers() {
        return players.stream().filter(SpleefPlayer::isPlaying).toList();
    }

    public List<SpleefPlayer> getDeadPlayers() {
        return players.stream().filter(SpleefPlayer::isDead).toList();
    }

    public List<SpleefPlayer> getLivingPlayers() {
        return players.stream().filter(x -> !x.isDead()).toList();
    }

    public String getArena() {
        return arena;
    }

    public Spleef getPlugin() {
        return spleef;
    }

    public int getGameTimer() {
        return gameTimer;
    }

    public int getLevelTimer() {
        return levelTimer;
    }

    public boolean isInGame() {
        return !(timer instanceof NotInGameTimer);
    }

    public boolean isInWait() {
        return timer instanceof WaitingGameTimer;
    }
}
