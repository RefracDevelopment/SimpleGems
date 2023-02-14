package me.refracdevelopment.simplegems.leaderboard;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.data.DataType;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import me.refracdevelopment.simplegems.utilities.config.Config;
import me.refracdevelopment.simplegems.utilities.config.Files;

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
            Files.getData().getKeys(false).stream().limit(Config.GEMS_TOP_ENTRIES).sorted().forEach(data -> {
                String name = Files.getData().getString("data." + data + ".name");
                double gems = Files.getData().getDouble("data." + data + ".gems");
                this.topGems.put(name, gems);
            });

            ValueComparator<String> vc = new ValueComparator<>(this.topGems);
            TreeMap<String, Double> sorted = new TreeMap<>(vc);
            sorted.putAll(this.topGems);
            this.topGems.clear();
            this.topGems.putAll(sorted);
        }
    }
}