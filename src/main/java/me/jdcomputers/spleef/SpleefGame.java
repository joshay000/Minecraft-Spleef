package me.jdcomputers.spleef;

import me.jdcomputers.files.FileManager;
import me.jdcomputers.src.Spleef;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class SpleefGame {
    private final Spleef spleef;
    private final List<SpleefPlayer> players;

    private String arena;
    private int gameTimer;
    private int levelTimer;
    private boolean running;
    private boolean inGame;
    private boolean inWait;
    private BukkitTask runnable;

    public SpleefGame(Spleef spleef) {
        this.spleef = spleef;

        players = new ArrayList<>();

        reset();
    }

    public void start() {
        reset();

        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                gameTimer++;
                levelTimer++;

                if (getPlayingPlayers().isEmpty()) {
                    gameTimer = 0;
                    levelTimer = 0;
                    inGame = false;
                    inWait = false;
                    running = true;

                    return;
                }

                if (!inGame) {
                    notInGameSetup();

                    return;
                }
            }
        }.runTaskTimer(spleef, 0L, 20L);
    }

    private void notInGameSetup() {
        if (gameTimer > 30) {
            inGame = true;
            inWait = true;
            gameTimer = 0;
            levelTimer = 0;

            Random random = new Random();

            FileManager config = spleef.getSpleefConfig().load();
            FileManager arena = spleef.getArenas().load();

            List<String> names = new ArrayList<>(arena.getSection("arenas").getKeys(false));

            String name = "arenas." + names.get(random.nextInt(names.size()));

            ItemStack pickaxe = config.getItemStack("digger");

            for (SpleefPlayer p : players) {
                p.setup();

                p.getPlayer().sendMessage(ChatColor.GOLD + "The game has begun! You have 5 seconds before you can break blocks.");
                p.getPlayer().getInventory().setItem(0, pickaxe);
                p.getPlayer().teleport(arena.getLocation(name + ".spawn"));
                p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.BLOCK_END_PORTAL_SPAWN, 1.0f, 1.0f);
            }
        } else if (gameTimer % 5 == 0 && gameTimer < 26) {
            for (SpleefPlayer p : getPlayingPlayers()) {
                p.getPlayer().sendMessage(ChatColor.GREEN + "The game will begin in " + ChatColor.WHITE + (30 - gameTimer) + " seconds.");
                p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            }
        } else if (gameTimer > 26) {
            for (SpleefPlayer p : getPlayingPlayers()) {
                float pitch = (30.0f - gameTimer) / 6.0f + 0.5f;

                p.getPlayer().sendMessage(ChatColor.RED + "The game will begin in " + ChatColor.WHITE + (30 - gameTimer) + " seconds.");
                p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, pitch);
            }
        }
    }

    public void reset() {
        gameTimer = 0;
        levelTimer = 0;
        running = true;
        inGame = false;

        if (runnable != null && !runnable.isCancelled())
            runnable.cancel();
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
