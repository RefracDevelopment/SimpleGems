package me.refracdevelopment.simplegems.manager.leaderboards;

import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.utilities.Methods;
import me.refracdevelopment.simplegems.utilities.Tasks;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.*;

public class LeaderboardManager {

    private final Map<String, Long> cachedMap;

    public LeaderboardManager() {
        cachedMap = new HashMap<>();

        update();
        updateTask();

        Color.log("&aLoaded Leaderboards!");
    }

    private void load() {
        if (Bukkit.getOnlinePlayers().isEmpty())
            return;

        cachedMap.clear();

        switch (SimpleGems.getInstance().getDataType()) {
            case MYSQL:
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
                break;
            default:
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
                break;
        }
    }

    public void sendLeaderboard(Player player) {
        Tasks.runAsync(() -> {
            if (cachedMap.isEmpty())
                load();

            Tasks.run(() -> {
                Map<String, Long> sortedMap = sortByValue(cachedMap);

                Color.sendCustomMessage(player, Color.translate(player, SimpleGems.getInstance().getSettings().GEMS_TOP_TITLE
                        .replace("%entries%", String.valueOf(SimpleGems.getInstance().getSettings().GEMS_TOP_ENTRIES))
                ));

                int placement = 1;
                for (Map.Entry<String, Long> entry : sortedMap.entrySet()) {
                    String key = entry.getKey();
                    long gems = entry.getValue();

                    if (placement == 11) break;

                    Color.sendCustomMessage(player, Color.translate(player, SimpleGems.getInstance().getSettings().GEMS_TOP_FORMAT
                            .replace("%number%", String.valueOf(placement))
                            .replace("%value%", String.valueOf(gems))
                            .replace("%gems%", String.valueOf(gems))
                            .replace("%gems_formatted%", Methods.format(gems))
                            .replace("%gems_decimal%", Methods.formatDecimal(gems))
                            .replace("%player%", key)
                    ));

                    placement++;
                }
            });
        });
    }

    private Map<String, Long> sortByValue(Map<String, Long> unsortMap) {
        List<Map.Entry<String, Long>> list = new LinkedList<>(unsortMap.entrySet());
        list.sort(Map.Entry.comparingByValue(Collections.reverseOrder()));

        Map<String, Long> sortedMap = new LinkedHashMap<>();

        for (Map.Entry<String, Long> entry : list)
            sortedMap.put(entry.getKey(), entry.getValue());

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

            Tasks.run(() -> Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
                Color.sendMessage(onlinePlayer, "leaderboard-update");
            }));
        }
    }
}