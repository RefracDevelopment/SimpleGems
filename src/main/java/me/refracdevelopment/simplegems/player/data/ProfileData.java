package me.refracdevelopment.simplegems.player.data;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import lombok.Setter;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.player.stats.Stat;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.UUID;

@Getter
@Setter
public class ProfileData {

    private final String name;
    private final UUID uuid;

    private Stat gems = new Stat();

    public ProfileData(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    // Check if the player exists already if not add them to the database then load and cache their data
    public void load() {
        switch (SimpleGems.getInstance().getDataType()) {
            case MONGO:
                Document document = SimpleGems.getInstance().getMongoManager().getStatsCollection().find(
                        Filters.eq("uuid", uuid.toString())).first();

                if (document != null) {
                    gems.setAmount(document.getLong("gems"));
                }
                break;
            case MYSQL:
                SimpleGems.getInstance().getMySQLManager().select("SELECT * FROM SimpleGems WHERE uuid=?", resultSet -> {
                    try {
                        if (resultSet.next()) {
                            this.gems.setAmount(resultSet.getLong("gems"));
                            SimpleGems.getInstance().getMySQLManager().updatePlayerName(uuid, name);
                        } else {
                            SimpleGems.getInstance().getMySQLManager().execute("INSERT INTO SimpleGems (uuid, name, gems) VALUES (?,?,?)",
                                    uuid.toString(), name, 0);
                        }
                    } catch (SQLException exception) {
                        Color.log("MySQL Error: " + exception.getMessage());
                    }
                }, uuid.toString());
                break;
            case SQLITE:
                SimpleGems.getInstance().getSqLiteManager().select("SELECT * FROM SimpleGems WHERE uuid=?", resultSet -> {
                    try {
                        if (resultSet.next()) {
                            this.gems.setAmount(resultSet.getLong("gems"));
                            SimpleGems.getInstance().getSqLiteManager().updatePlayerName(uuid, name);
                        } else {
                            SimpleGems.getInstance().getSqLiteManager().execute("INSERT INTO SimpleGems (uuid, name, gems) VALUES (?,?,?)",
                                    uuid.toString(), name, 0);
                        }
                    } catch (SQLException exception) {
                        Color.log("SQLite Error: " + exception.getMessage());
                    }
                }, uuid.toString());
                break;
            default:
                SimpleGems.getInstance().getPlayerMapper().loadPlayerFile(uuid);
                break;
        }
    }

    // Save the player to the database
    public void save() {
        switch (SimpleGems.getInstance().getDataType()) {
            case MONGO:
                Document document = new Document();

                document.put("name", name);
                document.put("uuid", uuid.toString());
                document.put("gems", gems.getAmount());

                SimpleGems.getInstance().getMongoManager().getStatsCollection().replaceOne(
                        Filters.eq("uuid", uuid.toString()), document, new ReplaceOptions().upsert(true));
                break;
            case MYSQL:
                SimpleGems.getInstance().getMySQLManager().updatePlayerGems(uuid, gems.getAmount());
                break;
            case SQLITE:
                SimpleGems.getInstance().getSqLiteManager().updatePlayerGems(uuid, gems.getAmount());
                break;
            default:
                SimpleGems.getInstance().getPlayerMapper().savePlayer(uuid, name, gems.getAmount());
                break;
        }
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

}