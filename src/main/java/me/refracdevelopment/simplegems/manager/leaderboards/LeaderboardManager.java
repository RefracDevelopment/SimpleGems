package me.refracdevelopment.simplegems.manager.leaderboards;

import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.manager.data.DataType;
import me.refracdevelopment.simplegems.player.Profile;
import me.refracdevelopment.simplegems.utilities.Methods;
import me.refracdevelopment.simplegems.utilities.Tasks;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.*;

public class LeaderboardManager {

    private final Map<String, Long> cachedMap;

    public LeaderboardManager() {
        cachedMap = new HashMap<>();
        updateTask();
        Color.log("&aLoaded Leaderboards!");
    }

    private void load() {
        if (Bukkit.getOnlinePlayers().isEmpty()) return;
        cachedMap.clear();
        if (SimpleGems.getInstance().getDataType() == DataType.MONGO) {
            List<Document> documents = SimpleGems.getInstance().getMongoManager().getStatsCollection().find().into(new ArrayList<>());

            documents.forEach(document -> {
                String name = document.getString("name");
                long gems = document.getLong("gems");

                cachedMap.put(name, gems);
            });
        } else if (SimpleGems.getInstance().getDataType() == DataType.MYSQL) {
            SimpleGems.getInstance().getMySQLManager().select("SELECT * FROM SimpleGems", resultSet -> {
                try {
                    while (resultSet.next()) {
                        String name = resultSet.getString("name");
                        long gems = resultSet.getLong("gems");

                        cachedMap.put(name, gems);
                    }
                } catch (SQLException exception) {
                    Color.log(exception.getMessage());
                }
            });
        } else if (SimpleGems.getInstance().getDataType() == DataType.SQLITE) {
            SimpleGems.getInstance().getSqLiteManager().select("SELECT * FROM SimpleGems", resultSet -> {
                try {
                    while (resultSet.next()) {
                        String name = resultSet.getString("name");
                        long gems = resultSet.getLong("gems");

                        cachedMap.put(name, gems);
                    }
                } catch (SQLException exception) {
                    Color.log(exception.getMessage());
                }
            });
        } else if (SimpleGems.getInstance().getDataType() == DataType.FLAT_FILE) {
            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
                Profile profile = SimpleGems.getInstance().getProfileManager().getProfile(onlinePlayer.getUniqueId());

                String name = onlinePlayer.getName();
                long gems = profile.getData().getGems().getAmount();

                cachedMap.put(name, gems);
            });

            Collection<OfflinePlayer> offlinePlayers = Arrays.asList(Bukkit.getOfflinePlayers());
            offlinePlayers.forEach(offlinePlayer -> {
                String name = offlinePlayer.getName();
                long gems = SimpleGems.getInstance().getPlayerMapper().getGems(offlinePlayer.getUniqueId());

                cachedMap.put(name, gems);
            });
        }
    }

    public void sendLeaderboard(Player player) {
        Tasks.runAsync(() -> {
            if (cachedMap.isEmpty()) {
                load();
            }

            Tasks.run(() -> {
                Map<String, Long> sortedMap = sortByValue(cachedMap);

                Color.sendCustomMessage(player, Color.translate(player, SimpleGems.getInstance().getSettings().GEMS_TOP_TITLE
                        .replace("%entries%", String.valueOf(SimpleGems.getInstance().getSettings().GEMS_TOP_ENTRIES))
                ));

                int number = 1;
                for (Map.Entry<String, Long> entry : sortedMap.entrySet()) {
                    String key = entry.getKey();
                    long gems = entry.getValue();

                    if (number == 11) break;

                    Color.sendCustomMessage(player, Color.translate(player, SimpleGems.getInstance().getSettings().GEMS_TOP_FORMAT
                            .replace("%number%", String.valueOf(number))
                            .replace("%value%", String.valueOf(gems))
                            .replace("%gems%", String.valueOf(gems))
                            .replace("%gems_formatted%", Methods.format(gems))
                            .replace("%gems_decimal%", Methods.formatDecimal(gems))
                            .replace("%player%", key)
                    ));

                    number++;
                }
            });
        });
    }

    private Map<String, Long> sortByValue(Map<String, Long> unsortMap) {
        List<Map.Entry<String, Long>> list = new LinkedList<>(unsortMap.entrySet());
        list.sort(Map.Entry.comparingByValue(Collections.reverseOrder()));

        Map<String, Long> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Long> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public void update() {
        Tasks.runAsync(() -> new LeaderBoardUpdate().update());
    }

    public void updateTask() {
        Tasks.runAsyncTimer(() -> new LeaderBoardUpdate().update(),
                SimpleGems.getInstance().getSettings().LEADERBOARD_UPDATE_INTERVAL * 20L,
                SimpleGems.getInstance().getSettings().LEADERBOARD_UPDATE_INTERVAL * 20L);
    }

    private class LeaderBoardUpdate {
        private void update() {
            load();

            Tasks.run(() -> {
                Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
                    Color.sendMessage(onlinePlayer, "leaderboard-update");
                });
            });
        }
    }
}