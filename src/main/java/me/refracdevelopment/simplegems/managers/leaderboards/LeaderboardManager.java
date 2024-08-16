package me.refracdevelopment.simplegems.managers.leaderboards;

import lombok.Data;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.utilities.Methods;
import me.refracdevelopment.simplegems.utilities.Tasks;
import me.refracdevelopment.simplegems.utilities.chat.RyMessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Data
public class LeaderboardManager {

    private Map<String, Double> cachedMap;
    private final List<String> players;

    public LeaderboardManager() {
        cachedMap = new LinkedHashMap<>();
        players = new LinkedList<>();

        update();
        updateTask();

        RyMessageUtils.sendConsole(true, "&aLoaded leaderboards.");
    }

    private void load() {
        players.clear();
        cachedMap.clear();

        switch (SimpleGems.getInstance().getDataType()) {
            case MYSQL:
                SimpleGems.getInstance().getMySQLManager().select("SELECT * FROM SimpleGems ORDER BY gems DESC", resultSet -> {
                    while (resultSet.next()) {
                        String name = resultSet.getString("name");
                        double gems = resultSet.getDouble("gems");

                        players.add(name);
                        cachedMap.put(name, gems);
                    }
                });
                break;
            default:
                SimpleGems.getInstance().getSqLiteManager().select("SELECT * FROM SimpleGems ORDER BY gems DESC", resultSet -> {
                    while (resultSet.next()) {
                        String name = resultSet.getString("name");
                        double gems = resultSet.getDouble("gems");

                        players.add(name);
                        cachedMap.put(name, gems);
                    }
                });
                break;
        }

        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
            RyMessageUtils.sendPluginMessage(onlinePlayer, "leaderboard-update");
        });

        cachedMap = sortByValue(cachedMap);
    }

    public void sendLeaderboard(Player player) {
        if (cachedMap.isEmpty() || players.isEmpty())
            update();

        RyMessageUtils.sendPlayer(player, SimpleGems.getInstance().getSettings().GEMS_TOP_TITLE
                .replace("%entries%", String.valueOf(SimpleGems.getInstance().getSettings().GEMS_TOP_ENTRIES))
        );

        int placement = 1;

        for (Map.Entry<String, Double> entry : cachedMap.entrySet()) {
            String key = entry.getKey();
            double gems = entry.getValue();

            if (placement == SimpleGems.getInstance().getSettings().GEMS_TOP_ENTRIES + 1)
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
    }

    public Map<String, Double> sortByValue(Map<String, Double> unsortMap) {
        List<Map.Entry<String, Double>> list = new LinkedList<>(unsortMap.entrySet());
        list.sort(Map.Entry.comparingByValue(Collections.reverseOrder()));

        Map<String, Double> sortedMap = new LinkedHashMap<>();

        for (Map.Entry<String, Double> entry : list)
            sortedMap.put(entry.getKey(), entry.getValue());

        return sortedMap;
    }

    public void update() {
        Tasks.runAsync(this::load);
    }

    public void updateTask() {
        try {
            Tasks.runAsyncTimer(this::load, SimpleGems.getInstance().getSettings().LEADERBOARD_UPDATE_INTERVAL,
                    SimpleGems.getInstance().getSettings().LEADERBOARD_UPDATE_INTERVAL, TimeUnit.SECONDS);
        } catch (Throwable throwable) {
            RyMessageUtils.sendConsole(true, "An error occurred while updating the leaderboard.");
            throwable.printStackTrace();
        }
    }
}