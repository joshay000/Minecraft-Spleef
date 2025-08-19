package me.jdcomputers.spleef;

import me.jdcomputers.files.FileManager;
import me.jdcomputers.src.Spleef;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpleefGame {
    private static final int GAME_SETUP_TIMER_MAX = 5;
    private static final int GAME_IN_WAIT_TIMER_MAX = 5;
    private static final int GAME_STATEMENT_INCREMENT = 5;

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

                if (inWait) {
                    waitingForGameSetup();

                    return;
                }
            }
        }.runTaskTimer(spleef, 0L, 20L);
    }

    public void end(SpleefPlayer winner) {
        String name = "Nobody";

        if (winner != null) {
            name = winner.getPlayer().getName();

            winner.getPlayer().playSound(winner.getPlayer().getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1.0f, 1.0f);
            winner.getPlayer().sendTitle(ChatColor.GOLD + "You Won", "", 10, 60, 10);
        }

        FileManager config = spleef.getSpleefConfig().load();

        for (SpleefPlayer player : getPlayingPlayers()) {
            if (player != winner) {
                player.sendMessage(ChatColor.GOLD + "The game is over! " + ChatColor.GREEN + name + ChatColor.GOLD + " won. Please wait " + ChatColor.WHITE + "30" + ChatColor.GOLD + " seconds for the next game.");
                player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 1.0f, 0.8f);
            }

            if (config.has("lobby"))
                player.getPlayer().teleport(config.getLocation("lobby"));

            player.setup();
        }

        start();
    }

    private void notInGameSetup() {
        if (gameTimer >= GAME_SETUP_TIMER_MAX) {
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

                p.sendMessage(ChatColor.GOLD + "Get ready! You have " + GAME_IN_WAIT_TIMER_MAX + " seconds before you can break blocks.");
                p.getPlayer().getInventory().setItem(0, pickaxe);
                p.getPlayer().teleport(arena.getLocation(name + ".spawn"));
                p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.BLOCK_END_PORTAL_SPAWN, 1.0f, 1.0f);
            }
        } else if (gameTimer % GAME_STATEMENT_INCREMENT == 0) {
            for (SpleefPlayer p : getPlayingPlayers()) {
                p.sendMessage(ChatColor.GREEN + "The game will begin in " + ChatColor.WHITE + (GAME_SETUP_TIMER_MAX - gameTimer) + " seconds.");
                p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            }
        } else if (gameTimer >= GAME_SETUP_TIMER_MAX - 3) {
            for (SpleefPlayer p : getPlayingPlayers()) {
                int second = GAME_SETUP_TIMER_MAX - gameTimer;
                float pitch = second / 6.0f + 0.5f;

                p.sendMessage(ChatColor.RED + "The game will begin in " + ChatColor.WHITE + second + ChatColor.RED + " " + (second == 1 ? "second." : "seconds."));
                p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, pitch);
            }
        }
    }

    private void waitingForGameSetup() {
        if (gameTimer >= GAME_IN_WAIT_TIMER_MAX) {
            inGame = true;
            inWait = false;
            gameTimer = 0;
            levelTimer = 0;

            for (SpleefPlayer p : getPlayingPlayers()) {
                p.sendMessage(ChatColor.GOLD + "The game has begun!");
                p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 0.8f);
            }
        } else if (gameTimer % GAME_STATEMENT_INCREMENT == 0) {
            for (SpleefPlayer p : getPlayingPlayers()) {
                p.sendMessage(ChatColor.GREEN + "Breaking blocks begins in " + ChatColor.WHITE + (GAME_IN_WAIT_TIMER_MAX - gameTimer) + " seconds.");
                p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            }
        } else if (gameTimer >= GAME_IN_WAIT_TIMER_MAX - 3) {
            for (SpleefPlayer p : getPlayingPlayers()) {
                int second = GAME_IN_WAIT_TIMER_MAX - gameTimer;

                p.sendMessage(ChatColor.RED + "Breaking blocks begins in " + ChatColor.WHITE + second + ChatColor.RED + " " + (second == 1 ? "second." : "seconds."));
                p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
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
        return inGame;
    }

    public boolean isInWait() {
        return inWait;
    }

    public boolean isRunning() {
        return running;
    }
}
