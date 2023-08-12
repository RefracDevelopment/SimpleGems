package me.refracdevelopment.simplegems.player.data;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import lombok.Setter;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.manager.data.DataType;
import me.refracdevelopment.simplegems.player.stats.Stat;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import org.bson.Document;
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

    public void load() {
        if (plugin.getDataType() == DataType.MONGO) {
            Document document = plugin.getMongoManager().getStatsCollection().find(Filters.eq("uuid", uuid.toString())).first();

            if (document != null) {
                gems.setAmount(document.getLong("gems"));
            }
        } else if (plugin.getDataType() == DataType.MYSQL) {
            plugin.getMySQLManager().select("SELECT * FROM SimpleGems WHERE uuid=?", resultSet -> {
                try {
                    if (resultSet.next()) {
                        this.gems.setAmount(resultSet.getLong("gems"));
                        plugin.getMySQLManager().execute("UPDATE SimpleGems SET name=? WHERE uuid=?",
                                name, uuid.toString());
                    } else {
                        plugin.getMySQLManager().execute("INSERT INTO SimpleGems (uuid, name, gems) VALUES (?,?,?)",
                                uuid.toString(), name, 0);
                    }
                } catch (SQLException exception) {
                    Color.log("MySQL Error: " + exception.getMessage());
                }
            }, uuid.toString());
        } else if (plugin.getDataType() == DataType.FLAT_FILE) {
            plugin.getPlayerMapper().loadPlayerFile(uuid);
        }
    }

    public void save() {
        if (plugin.getDataType() == DataType.MONGO) {
            Document document = new Document();

            document.put("name", name);
            document.put("uuid", uuid.toString());
            document.put("gems", gems.getAmount());

            plugin.getMongoManager().getStatsCollection().replaceOne(Filters.eq("uuid", uuid.toString()), document, new ReplaceOptions().upsert(true));
        } else if (plugin.getDataType() == DataType.MYSQL) {
            plugin.getMySQLManager().execute("UPDATE SimpleGems SET gems=? WHERE uuid=?",
                    gems.getAmount(), uuid.toString());
        } else if (plugin.getDataType() == DataType.FLAT_FILE) {
            plugin.getPlayerMapper().savePlayer(uuid, name, gems.getAmount());
        }
    }

    public Player getPlayer() {
        return plugin.getServer().getPlayer(uuid);
    }

}