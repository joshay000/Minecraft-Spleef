package me.jdcomputers.spleef.timers;

import me.jdcomputers.src.Spleef;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public abstract class GameTimer {
    protected final Spleef spleef;
    private final int maximum;
    private final int increment;
    private final long tick;

    private BukkitTask task;
    private int current;

    public GameTimer(Spleef spleef, int maximum, int increment, long tick, boolean defaultInitialized) {
        this.spleef = spleef;
        this.maximum = maximum;
        this.increment = increment;
        this.tick = tick;

        reset();

        if (defaultInitialized)
            timerInitialized();
    }

    public void reset() {
        current = 0;

        end();

        task = new BukkitRunnable() {
            @Override
            public void run() {
                current++;

                tick();
            }
        }.runTaskTimer(spleef, 0L, tick);
    }

    public void end() {
        if (task != null && !task.isCancelled())
            task.cancel();
    }

    protected void tick() {
        if (current >= maximum) {
            timerPast();

            return;
        }

        if (current % increment == 0) {
            timerIncrement(maximum - current);

            return;
        }

        if (current < maximum - 3)
            return;

        timerUpcoming(maximum - current);
    }

    protected abstract void timerInitialized();
    protected abstract void timerPast();
    protected abstract void timerIncrement(int second);
    protected abstract void timerUpcoming(int second);

    public Spleef getSpleef() {
        return spleef;
    }

    public int getCurrent() {
        return current;
    }

    public int getMaximum() {
        return maximum;
    }

    public int getIncrement() {
        return increment;
    }

    public long getTick() {
        return tick;
    }

    public boolean isComplete() {
        return current >= maximum;
    }

    public boolean isEnded() {
        return task.isCancelled() || current >= maximum;
    }
}
