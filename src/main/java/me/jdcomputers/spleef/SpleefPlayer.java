package me.jdcomputers.spleef;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public final class SpleefPlayer {
    private final Player player;

    public SpleefPlayer(Player player) {
        this.player = player;
    }

    public void setup() {
        player.getInventory().clear();
        player.setGameMode(GameMode.SURVIVAL);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(20.0f);
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
