package me.jdcomputers.spleef;

import me.jdcomputers.src.Spleef;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SpleefGame {
    private final Spleef spleef;
    private final List<SpleefPlayer> players;

    private String arena;
    private int gameTimer;
    private int levelTimer;
    private boolean running;
    private boolean inGame;

    public SpleefGame(Spleef spleef) {
        this.spleef = spleef;

        players = new ArrayList<>();

        gameTimer = 0;
        levelTimer = 0;
        running = true;
        inGame = false;
    }

    public void addPlayer(Player player) {
        players.add(new SpleefPlayer(player));
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
        return inGame;
    }

    public boolean isRunning() {
        return running;
    }
}
