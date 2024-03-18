package me.refracdevelopment.simplegems.player.data;

import lombok.Getter;
import lombok.Setter;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.player.stats.Stat;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.UUID;

@Getter
@Setter
public class ProfileData {

    private final SimpleGems plugin = SimpleGems.getInstance();
    private final String name;
    private final UUID uuid;

    private Stat gems = new Stat();

    public ProfileData(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    // Check if the player exists already if not add them to the database then load and cache their data
    public void load(Player player) {
        switch (getPlugin().getDataType()) {
            case MYSQL:
                getPlugin().getMySQLManager().select("SELECT * FROM SimpleGems WHERE uuid=?", resultSet -> {
                    try {
                        if (resultSet.next()) {
                            getGems().setAmount(resultSet.getLong("gems"));
                            getPlugin().getMySQLManager().updatePlayerName(player.getUniqueId().toString(), player.getName());
                        } else {
                            getPlugin().getMySQLManager().execute("INSERT INTO SimpleGems (uuid, name, gems) VALUES (?,?,?)",
                                    player.getUniqueId().toString(), player.getName(), 0L);
                        }
                    } catch (SQLException exception) {
                        Color.log("MySQL Error: " + exception.getMessage());
                    }
                }, player.getUniqueId().toString());
                break;
            default:
                getPlugin().getSqLiteManager().select("SELECT * FROM SimpleGems WHERE uuid=?", resultSet -> {
                    try {
                        if (resultSet.next()) {
                            getGems().setAmount(resultSet.getLong("gems"));
                            getPlugin().getSqLiteManager().updatePlayerName(player.getUniqueId().toString(), player.getName());
                        } else {
                            getPlugin().getSqLiteManager().execute("INSERT INTO SimpleGems (uuid, name, gems) VALUES (?,?,?)",
                                    player.getUniqueId().toString(), player.getName(), 0L);
                        }
                    } catch (SQLException exception) {
                        Color.log("SQLite Error: " + exception.getMessage());
                    }
                }, player.getUniqueId().toString());
                break;
        }
    }

    // Save the player to the database
    public void save(Player player) {
        switch (getPlugin().getDataType()) {
            case MYSQL:
                getPlugin().getMySQLManager().updatePlayerGems(player.getUniqueId().toString(), getGems().getAmount());
                break;
            default:
                getPlugin().getSqLiteManager().updatePlayerGems(player.getUniqueId().toString(), getGems().getAmount());
                break;
        }
    }
}