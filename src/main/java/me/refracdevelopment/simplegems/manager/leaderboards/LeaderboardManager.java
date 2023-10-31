package me.refracdevelopment.simplegems.manager.leaderboards;

import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.manager.data.DataType;
import me.refracdevelopment.simplegems.player.data.ProfileData;
import me.refracdevelopment.simplegems.utilities.Methods;
import me.refracdevelopment.simplegems.utilities.Tasks;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class LeaderboardManager {

    private final Map<String, Long> cachedMap;

    public LeaderboardManager() {
        this.cachedMap = new HashMap<>();
        load();
        Color.log("&aLoaded Leaderboards!");
    }

    public void load() {
        if (Bukkit.getOnlinePlayers().isEmpty()) return;
        if (SimpleGems.getInstance().getDataType() == DataType.MONGO) {
            List<Document> documents = SimpleGems.getInstance().getMongoManager().getStatsCollection().find().limit(SimpleGems.getInstance().getSettings().GEMS_TOP_ENTRIES).into(new ArrayList<>());

            documents.forEach(document -> {
                String name = document.getString("name");
                long gems = document.getLong("gems");

                cachedMap.putIfAbsent(name, gems);
            });
        } else if (SimpleGems.getInstance().getDataType() == DataType.MYSQL) {
            try {
                SimpleGems.getInstance().getMySQLManager().getAllPlayers().forEach(playerGems -> {
                    cachedMap.putIfAbsent(playerGems.getName(), playerGems.getGems());
                });
            } catch (SQLException exception) {
                Color.log("&cMySQL Error: " + exception.getMessage());
            }
        } else if (SimpleGems.getInstance().getDataType() == DataType.SQLITE) {
            try {
                SimpleGems.getInstance().getSqLiteManager().getAllPlayers().forEach(playerGems -> {
                    cachedMap.putIfAbsent(playerGems.getName(), playerGems.getGems());
                });
            } catch (SQLException exception) {
                Color.log("&cSQLite Error: " + exception.getMessage());
            }
        } else if (SimpleGems.getInstance().getDataType() == DataType.FLAT_FILE) {
            Bukkit.getOnlinePlayers().stream().limit(SimpleGems.getInstance().getSettings().GEMS_TOP_ENTRIES - 1).forEach(onlinePlayer -> {
                ProfileData profile = SimpleGems.getInstance().getProfileManager().getProfile(onlinePlayer.getUniqueId()).getData();
                String name = onlinePlayer.getName();
                long gems = profile.getGems().getAmount();
                cachedMap.putIfAbsent(name, gems);
            });

            Collection<OfflinePlayer> offlinePlayers = Arrays.asList(Bukkit.getOfflinePlayers());
            offlinePlayers.stream().limit(SimpleGems.getInstance().getSettings().GEMS_TOP_ENTRIES - 1).forEach(offlinePlayer -> {
                String name = offlinePlayer.getName();
                long gems = SimpleGems.getInstance().getPlayerMapper().getGems(offlinePlayer.getUniqueId());
                cachedMap.putIfAbsent(name, gems);
            });
        }
    }

    public void sendLeaderboard(Player player) {
        cachedMap.clear();
        load();

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
}