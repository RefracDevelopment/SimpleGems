package me.refracdevelopment.simplegems.manager.leaderboards;

import lombok.Getter;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.utilities.Methods;
import me.refracdevelopment.simplegems.utilities.Tasks;
import me.refracdevelopment.simplegems.utilities.chat.RyMessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.*;

@Getter
public class LeaderboardManager {

    private final Map<String, Double> cachedMap;
    private final List<String> players;

    public LeaderboardManager() {
        cachedMap = new HashMap<>();
        players = new ArrayList<>();

        update();
        updateTask();

        RyMessageUtils.sendConsole(true, "&aLoaded leaderboards.");
    }

    private void load() {
        if (Bukkit.getOnlinePlayers().isEmpty())
            return;

        players.clear();
        cachedMap.clear();

        switch (SimpleGems.getInstance().getDataType()) {
            case MYSQL:
                SimpleGems.getInstance().getMySQLManager().select("SELECT * FROM SimpleGems", resultSet -> {
                    try {
                        while (resultSet.next()) {
                            String name = resultSet.getString("name");
                            double gems = resultSet.getDouble("gems");

                            players.add(name);
                            cachedMap.put(name, gems);
                        }
                    } catch (SQLException exception) {
                        RyMessageUtils.sendConsole(true, exception.getMessage());
                    }
                });
                break;
            default:
                SimpleGems.getInstance().getSqLiteManager().select("SELECT * FROM SimpleGems", resultSet -> {
                    try {
                        while (resultSet.next()) {
                            String name = resultSet.getString("name");
                            double gems = resultSet.getDouble("gems");

                            players.add(name);
                            cachedMap.put(name, gems);
                        }
                    } catch (SQLException exception) {
                        RyMessageUtils.sendConsole(true, exception.getMessage());
                    }
                });
                break;
        }
    }

    public void sendLeaderboard(Player player) {
        Tasks.runAsync(() -> {
            if (cachedMap.isEmpty() || players.isEmpty())
                load();

            Map<String, Double> sortedMap = sortByValue(cachedMap);

            RyMessageUtils.sendPlayer(player, SimpleGems.getInstance().getSettings().GEMS_TOP_TITLE
                    .replace("%entries%", String.valueOf(SimpleGems.getInstance().getSettings().GEMS_TOP_ENTRIES))
            );

            int placement = 1;

            for (Map.Entry<String, Double> entry : sortedMap.entrySet()) {
                String key = entry.getKey();
                double gems = entry.getValue();

                if (placement == 11)
                    break;

                RyMessageUtils.sendPlayer(player, SimpleGems.getInstance().getSettings().GEMS_TOP_FORMAT
                        .replace("%number%", String.valueOf(placement))
                        .replace("%value%", String.valueOf(gems))
                        .replace("%gems%", String.valueOf(gems))
                        .replace("%gems_formatted%", Methods.format(gems))
                        .replace("%gems_decimal%", Methods.formatDecimal(gems))
                        .replace("%player%", key)
                );

                placement++;
            }
        });
    }

    private Map<String, Double> sortByValue(Map<String, Double> unsortMap) {
        List<Map.Entry<String, Double>> list = new LinkedList<>(unsortMap.entrySet());
        list.sort(Map.Entry.comparingByValue(Collections.reverseOrder()));

        Map<String, Double> sortedMap = new LinkedHashMap<>();

        for (Map.Entry<String, Double> entry : list)
            sortedMap.put(entry.getKey(), entry.getValue());

        return sortedMap;
    }

    public void update() {
        Tasks.runAsync(() -> new LeaderBoardUpdate().update());
    }

    public void updateTask() {
        Tasks.runAsyncTimer(() -> new LeaderBoardUpdate().update(),
                SimpleGems.getInstance().getSettings().LEADERBOARD_UPDATE_INTERVAL * 20L);
    }

    private class LeaderBoardUpdate {
        private void update() {
            load();

            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
                RyMessageUtils.sendPluginMessage(onlinePlayer, "leaderboard-update");
            });
        }
    }
}