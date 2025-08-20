package me.jdcomputers.spleef.timers;

import me.jdcomputers.spleef.SpleefGame;
import org.bukkit.Bukkit;

public class InGameTimer extends GameTimer {
    public InGameTimer(SpleefGame game, long delay) {
        super(game, 30, 10, delay, 20L, true);
    }

    @Override
    protected void timerTick() {

    }

    @Override
    protected void timerInitialized() {

    }

    @Override
    protected void timerPast() {

    }

    @Override
    protected void timerIncrement(int second) {

    }

    @Override
    protected void timerUpcoming(int second) {

    }
}
