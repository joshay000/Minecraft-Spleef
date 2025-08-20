package me.jdcomputers.spleef.timers;

import me.jdcomputers.spleef.SpleefGame;
import me.jdcomputers.spleef.SpleefPlayer;
import me.jdcomputers.src.Spleef;
import org.bukkit.ChatColor;

public class GameOverTimer extends GameTimer {
    private final SpleefGame game;

    public GameOverTimer(SpleefGame game) {
        super(game.getPlugin(), 5, 5, 20L, false);

        this.game = game;

        timerInitialized();
    }

    @Override
    protected void timerInitialized() {

    }

    @Override
    protected void timerPast() {

    }

    @Override
    protected void timerIncrement(int second) {
        for (SpleefPlayer p : game.getPlayingPlayers())
            p.sendMessage(ChatColor.GREEN + "Teleporting back to lobby in " + ChatColor.WHITE + second + " seconds.");
    }

    @Override
    protected void timerUpcoming(int second) {
        for (SpleefPlayer p : game.getPlayingPlayers())
            p.sendMessage(ChatColor.RED + "Teleporting back to lobby in " + ChatColor.WHITE + second + ChatColor.RED + " " + (second == 1 ? "second." : "seconds."));
    }
}
