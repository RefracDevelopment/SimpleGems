package me.refracdevelopment.simplegems.player.data;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import lombok.Setter;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.manager.data.sql.entities.PlayerGems;
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
                try {
                    PlayerGems playerGems = SimpleGems.getInstance().getMySQLManager().playerExists(uuid) ?
                            SimpleGems.getInstance().getMySQLManager().getPlayerGems(uuid) :
                            SimpleGems.getInstance().getMySQLManager().addPlayer(getPlayer());
                    gems.setAmount(playerGems.getGems());
                    SimpleGems.getInstance().getMySQLManager().updatePlayerName(uuid, name);
                } catch (SQLException exception) {
                    Color.log("&cMySQL Error: " + exception.getMessage());
                }
                break;
            case SQLITE:
                try {
                    PlayerGems playerGems = SimpleGems.getInstance().getSqLiteManager().playerExists(uuid) ?
                            SimpleGems.getInstance().getSqLiteManager().getPlayerGems(uuid) :
                            SimpleGems.getInstance().getSqLiteManager().addPlayer(getPlayer());
                    gems.setAmount(playerGems.getGems());
                    SimpleGems.getInstance().getSqLiteManager().updatePlayerName(uuid, name);
                } catch (SQLException exception) {
                    Color.log("&cSQLite Error: " + exception.getMessage());
                }
                break;
            default:
                SimpleGems.getInstance().getPlayerMapper().loadPlayerFile(uuid);
                break;
        }
    }

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
                try {
                    SimpleGems.getInstance().getMySQLManager().updatePlayerGems(uuid, gems.getAmount());
                } catch (SQLException exception) {
                    Color.log("&cMySQL Error: " + exception);
                }
                break;
            case SQLITE:
                try {
                    SimpleGems.getInstance().getSqLiteManager().updatePlayerGems(uuid, gems.getAmount());
                } catch (SQLException exception) {
                    Color.log("&cSQLite Error: " + exception);
                }
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