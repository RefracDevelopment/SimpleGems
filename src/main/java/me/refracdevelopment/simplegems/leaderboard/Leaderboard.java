package me.refracdevelopment.simplegems.leaderboard;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.data.DataType;
import me.refracdevelopment.simplegems.data.Profile;
import me.refracdevelopment.simplegems.utilities.Methods;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import me.refracdevelopment.simplegems.utilities.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.sql.SQLException;
import java.util.TreeMap;

@RequiredArgsConstructor
@Getter
@Setter
public class Leaderboard {

    private final SimpleGems plugin;
    private final TreeMap<String, Double> topGems;

    public void load() {
        this.topGems.clear();

        if (this.plugin.getDataType() == DataType.MYSQL) {

            // get top 10 gems from database in order of highest to lowest to the map
            this.plugin.getSqlManager().select("SELECT * FROM SimpleGems ORDER BY gems DESC LIMIT " + Config.GEMS_TOP_ENTRIES, resultSet -> {
                try {
                    // order from highest to lowest
                    while (resultSet.next()) {
                        String name = resultSet.getString("name");
                        double gems = resultSet.getDouble("gems");
                        this.topGems.put(name, gems);
                    }
                } catch (SQLException exception) {
                    Color.log(exception.getMessage());
                }
            });

            ValueComparator<String> vc = new ValueComparator<>(this.topGems);
            TreeMap<String, Double> sorted = new TreeMap<>(vc);
            sorted.putAll(this.topGems);
            this.topGems.clear();
            this.topGems.putAll(sorted);
        } else if (this.plugin.getDataType() == DataType.YAML) {
            Bukkit.getOnlinePlayers().forEach(player -> {
                Profile profile = plugin.getProfileManager().getProfile(player.getUniqueId());
                String name = player.getName();
                double gems = profile.getData().getGems().getAmount();
                if (this.topGems.size() >= Config.GEMS_TOP_ENTRIES-1) return;
                this.topGems.put(name, gems);
            });

            for (OfflinePlayer op : Bukkit.getOfflinePlayers()) {
                String name = op.getName();
                double gems = Methods.getOfflineGems(op);
                if (this.topGems.size() >= Config.GEMS_TOP_ENTRIES-1) return;
                this.topGems.put(name, gems);
            }

            ValueComparator<String> vc = new ValueComparator<>(this.topGems);
            TreeMap<String, Double> sorted = new TreeMap<>(vc);
            sorted.putAll(this.topGems);
            this.topGems.clear();
            this.topGems.putAll(sorted);
        }
    }
}