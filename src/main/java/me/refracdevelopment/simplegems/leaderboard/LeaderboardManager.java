package me.refracdevelopment.simplegems.leaderboard;

import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.manager.LocaleManager;
import me.refracdevelopment.simplegems.utilities.Methods;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import me.refracdevelopment.simplegems.utilities.chat.Placeholders;
import me.refracdevelopment.simplegems.utilities.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.TreeMap;

public class LeaderboardManager {

    private final SimpleGems plugin = SimpleGems.getInstance();
    private final Leaderboard leaderboard;

    private TreeMap<String, TopGems> gems = new TreeMap<>();

    public LeaderboardManager() {
        this.leaderboard = new Leaderboard(this.plugin, new TreeMap<>());
        this.leaderboard.load();
        this.updateTask();
        Color.log("&aLoaded leaderboards.");
    }

    public void getTop10(Player player) {
        final LocaleManager locale = plugin.getManager(LocaleManager.class);

        if (this.leaderboard.getTopGems().isEmpty() && this.gems.isEmpty()) {
            locale.sendMessage(player, "leaderboard-empty");
            return;
        }

        this.gems.putAll(this.leaderboard.getTopGems());

        locale.sendCustomMessage(player, Placeholders.setPlaceholders(player, Config.GEMS_TOP_TITLE
                .replace("%entries%", String.valueOf(Config.GEMS_TOP_ENTRIES))));
        try {
            for (int i = 0; i < Config.GEMS_TOP_ENTRIES; i++) {
                Map.Entry<String, TopGems> top = this.gems.pollFirstEntry();
                locale.sendCustomMessage(player, Placeholders.setPlaceholders(player, Config.GEMS_TOP_FORMAT
                        .replace("%number%", String.valueOf(i + 1))
                        .replace("%player%", top.getValue().getPlayerName())
                        .replace("%value%", Methods.format(top.getValue().getGems()))
                        .replace("%value_decimal%", Methods.formatDec(top.getValue().getGems()))));
            }
        } catch (Exception ignored) {}
    }

    public void update() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                this.gems.clear();
                this.leaderboard.load();
                this.gems.putAll(this.leaderboard.getTopGems());
            } catch (Exception exception) {
                Color.log("An error occurred while updating the leaderboard: " + exception.getMessage());
            }
        });
    }

    public void updateTask() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, task -> {
            try {
                final LocaleManager locale = plugin.getManager(LocaleManager.class);
                this.gems.clear();
                this.leaderboard.load();
                this.gems.putAll(this.leaderboard.getTopGems());
                Bukkit.getOnlinePlayers().forEach(player -> locale.sendCommandMessage(player, "leaderboard-update"));
            } catch (Exception exception) {
                Color.log("An error occurred while updating the leaderboard: " + exception.getMessage());
                Bukkit.getScheduler().cancelTask(task.getTaskId());
            }
        }, Config.LEADERBOARD_UPDATE_INTERVAL*20L, Config.LEADERBOARD_UPDATE_INTERVAL*20L);
    }
}