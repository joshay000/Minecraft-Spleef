package me.jdcomputers.spleef.timers;

import me.jdcomputers.events.SpleefGameCancelledEvent;
import me.jdcomputers.spleef.SpleefGame;
import org.bukkit.Bukkit;

public class InGameTimer extends GameTimer {
    public InGameTimer(SpleefGame game, long delay) {
        super(game, 30, 10, delay, 20L, true);
    }

    @Override
    protected void timerTick() {
        if (game.getPlayingPlayers().isEmpty()) {
            SpleefGameCancelledEvent event = new SpleefGameCancelledEvent(game);

            Bukkit.getPluginManager().callEvent(event);
        }
    }

    @Override
    protected void timerInitialized() {

    }

    @Override
    protected boolean timerPast() {
        return true;
    }

    @Override
    protected void timerIncrement(int second) {

    }

    @Override
    protected void timerUpcoming(int second) {

    }
}
