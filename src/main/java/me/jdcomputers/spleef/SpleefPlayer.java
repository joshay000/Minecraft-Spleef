package me.jdcomputers.spleef;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public final class SpleefPlayer {
    private final Player player;

    public SpleefPlayer(Player player) {
        this.player = player;
    }

    public boolean isPlaying() {
        return player.getGameMode() != GameMode.CREATIVE;
    }

    public boolean isDead() {
        return player.getGameMode() == GameMode.SPECTATOR;
    }

    public Player getPlayer() {
        return player;
    }
}
