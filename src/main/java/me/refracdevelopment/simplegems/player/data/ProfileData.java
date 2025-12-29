package me.refracdevelopment.simplegems.player.data;

import lombok.Data;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.player.stats.Stat;
import org.bukkit.entity.Player;

import java.util.UUID;

@Data
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
    public void load() {
        switch (getPlugin().getDataType()) {
            case MYSQL:
                getPlugin().getMySQLManager().select("SELECT * FROM SimpleGems WHERE uuid=?", resultSet -> {
                    if (resultSet.next()) {
                        getGems().setAmount(resultSet.getDouble("gems"));
                        getPlugin().getMySQLManager().updatePlayerName(uuid.toString(), name);
                    } else {
                        getPlugin().getMySQLManager().execute("INSERT INTO SimpleGems (uuid, name, gems) VALUES (?,?,?)",
                                uuid.toString(), name, 0L);
                    }
                }, uuid.toString());
                break;
            default:
                getPlugin().getSqLiteManager().select("SELECT * FROM SimpleGems WHERE uuid=?", resultSet -> {
                    if (resultSet.next()) {
                        getGems().setAmount(resultSet.getDouble("gems"));
                        getPlugin().getSqLiteManager().updatePlayerName(uuid.toString(), name);
                    } else {
                        getPlugin().getSqLiteManager().execute("INSERT INTO SimpleGems (uuid, name, gems) VALUES (?,?,?)",
                                uuid.toString(), name, 0L);
                    }
                }, uuid.toString());
                break;
        }
    }

    // Save the player to the database
    public void save() {
        switch (getPlugin().getDataType()) {
            case MYSQL:
                getPlugin().getMySQLManager().updatePlayerGems(uuid.toString(), getGems().getAmount());
                break;
            default:
                getPlugin().getSqLiteManager().updatePlayerGems(uuid.toString(), getGems().getAmount());
                break;
        }
    }
}