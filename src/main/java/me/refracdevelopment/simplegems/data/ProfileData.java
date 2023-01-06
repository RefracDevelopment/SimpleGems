package me.refracdevelopment.simplegems.data;

import lombok.Getter;
import lombok.Setter;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import me.refracdevelopment.simplegems.utilities.files.Files;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.UUID;

@Getter
@Setter
public class ProfileData {

    private final SimpleGems plugin = SimpleGems.getInstance();
    private final String name;
    private final UUID uuid;

    private final Stat gems = new Stat();

    public ProfileData(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    /**
     * The #load method allows you to
     * load a specified player's data
     */
    public void load(Player player) {
        if (plugin.getDataType() == DataType.MYSQL) {
            plugin.getSqlManager().select("SELECT * FROM SimpleGems WHERE uuid=?", resultSet -> {
                try {
                    if (resultSet.next()) {
                        this.gems.setAmount(resultSet.getLong("gems"));
                        plugin.getSqlManager().execute("UPDATE SimpleGems SET name=? WHERE uuid=?",
                                player.getName(), player.getUniqueId().toString());
                    } else {
                        plugin.getSqlManager().execute("INSERT INTO SimpleGems (uuid, name, gems) VALUES (?,?,?)",
                                player.getUniqueId().toString(), player.getName(), 0);
                    }
                } catch (SQLException exception) {
                    Color.log(exception.getMessage());
                }
            }, player.getUniqueId().toString());
        } else if (plugin.getDataType() == DataType.YAML) {
            this.gems.setAmount(Files.getData().getLong("data." + player.getUniqueId().toString() + ".gems"));
            Files.getData().set("data." + player.getUniqueId().toString() + ".name", player.getName());
            Files.saveData();
        }
    }

    /**
     * The #save method allows you to
     * save a specified player's data
     */
    public void save(Player player) {
        if (plugin.getDataType() == DataType.MYSQL) {
            plugin.getSqlManager().execute("UPDATE SimpleGems SET gems=? WHERE uuid=?",
                    this.gems.getAmount(), player.getUniqueId().toString());
        } else if (plugin.getDataType() == DataType.YAML) {
            Files.getData().set("data." + player.getUniqueId().toString() + ".gems", this.gems.getAmount());
            Files.saveData();
        }
    }
}