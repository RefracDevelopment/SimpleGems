package me.refracdevelopment.simplegems.manager;

import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.config.Config;
import me.refracdevelopment.simplegems.database.DataType;
import me.refracdevelopment.simplegems.utilities.Methods;
import me.refracdevelopment.simplegems.utilities.Tasks;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import me.refracdevelopment.simplegems.utilities.chat.Placeholders;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.*;

public class LeaderboardManager {

    private SimpleGems plugin;

    private Map<String, Long> unsortedMap;
    private Map<String, Long> cachedMap;

    public LeaderboardManager(SimpleGems plugin) {
        this.plugin = plugin;
        this.unsortedMap = new HashMap<>();
        this.cachedMap = new HashMap<>();
        load();
        updateTask();
    }

    public void load() {
        if (cachedMap.isEmpty() || unsortedMap.isEmpty()) {

            if (plugin.getDataType() == DataType.MONGO) {
                List<Document> documents = plugin.getMongoManager().getStatsCollection().find().limit(Config.GEMS_TOP_ENTRIES).into(new ArrayList<>());

                documents.forEach(document -> {
                    String name = document.getString("name");
                    long gems = document.getLong("gems");

                    unsortedMap.putIfAbsent(name, gems);
                    cachedMap.putIfAbsent(name, gems);
                });
            } else if (plugin.getDataType() == DataType.MYSQL) {
                this.plugin.getMySQLManager().select("SELECT * FROM SimpleGems ORDER BY gems DESC LIMIT " + Config.GEMS_TOP_ENTRIES, resultSet -> {
                    try {
                        // order from highest to lowest
                        while (resultSet.next()) {
                            String name = resultSet.getString("name");
                            long gems = resultSet.getLong("gems");
                            unsortedMap.putIfAbsent(name, gems);
                            cachedMap.putIfAbsent(name, gems);
                        }
                    } catch (SQLException exception) {
                        Color.log(exception.getMessage());
                    }
                });
            } else if (plugin.getDataType() == DataType.FLAT_FILE) {
                Bukkit.getOnlinePlayers().stream().limit(Config.GEMS_TOP_ENTRIES).forEach(onlinePlayer -> {
                    String name = onlinePlayer.getName();
                    long gems = plugin.getPlayerMapper().getGems(onlinePlayer.getUniqueId());
                    if (this.unsortedMap.size() >= Config.GEMS_TOP_ENTRIES-1) return;
                    unsortedMap.putIfAbsent(name, gems);
                    cachedMap.putIfAbsent(name, gems);
                });

                Collection<OfflinePlayer> offlinePlayers = Arrays.asList(Bukkit.getOfflinePlayers());
                offlinePlayers.stream().limit(Config.GEMS_TOP_ENTRIES).forEach(offlinePlayer -> {
                    String name = offlinePlayer.getName();
                    long gems = plugin.getPlayerMapper().getGems(offlinePlayer.getUniqueId());
                    if (this.unsortedMap.size() >= Config.GEMS_TOP_ENTRIES-1) return;
                    unsortedMap.putIfAbsent(name, gems);
                    cachedMap.putIfAbsent(name, gems);
                });
            }
        }
    }

    public void sendLeaderboard(Player player) {
        final LocaleManager locale = plugin.getManager(LocaleManager.class);

        if (!cachedMap.isEmpty()) {
            Map<String, Long> sortedMap = sortByValue(cachedMap);

            locale.sendCustomMessage(player, Color.translate(player, Config.GEMS_TOP_TITLE
                    .replace("%entries%", String.valueOf(Config.GEMS_TOP_ENTRIES))
            ));
            int number = 1;
            for (Map.Entry<String, Long> entry : sortedMap.entrySet()) {
                String key = entry.getKey();
                long gems = entry.getValue();

                locale.sendCustomMessage(player, Color.translate(player, Config.GEMS_TOP_FORMAT
                        .replace("%number%", String.valueOf(number))
                        .replace("%value%", String.valueOf(gems))
                        .replace("%gems%", String.valueOf(gems))
                        .replace("%gems_formatted%", Methods.format(gems))
                        .replace("%gems_decimal%", Methods.formatDecimal(gems))
                        .replace("%player%", key)
                ));
                number++;
            }
        } else {
            load();
            Map<String, Long> sortedMap = sortByValue(unsortedMap);

            locale.sendCustomMessage(player, Color.translate(player, Config.GEMS_TOP_TITLE));
            int number = 1;
            for (Map.Entry<String, Long> entry : sortedMap.entrySet()) {
                String key = entry.getKey();
                long gems = entry.getValue();

                locale.sendCustomMessage(player, Color.translate(player, Config.GEMS_TOP_FORMAT
                        .replace("%number%", String.valueOf(number))
                        .replace("%value%", String.valueOf(gems))
                        .replace("%gems%", String.valueOf(gems))
                        .replace("%gems_formatted%", Methods.format(gems))
                        .replace("%gems_decimal%", Methods.formatDecimal(gems))
                        .replace("%player%", key)
                ));
                number++;
            }
        }
    }

    private Map<String, Long> sortByValue(Map<String, Long> unsortMap) {
        List<Map.Entry<String, Long>> list = new LinkedList<>(unsortMap.entrySet());
        list.sort(Map.Entry.comparingByValue());

        Map<String, Long> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Long> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public void update() {
        Tasks.runAsync(plugin, new Update());
    }

    public void updateTask() {
        Tasks.runAsyncTimer(plugin, new Update(), Config.LEADERBOARD_UPDATE_INTERVAL*20L, Config.LEADERBOARD_UPDATE_INTERVAL*20L);
    }

    private class Update implements Runnable {

        @Override
        public void run() {
            final LocaleManager locale = plugin.getManager(LocaleManager.class);
            cachedMap.clear();
            unsortedMap.clear();
            load();
            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
                locale.sendMessage(onlinePlayer, "leaderboard-update", Placeholders.setPlaceholders(onlinePlayer));
            });
        }
    }
}