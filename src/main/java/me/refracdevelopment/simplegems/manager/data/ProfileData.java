package me.refracdevelopment.simplegems.manager.data;

import lombok.Getter;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import me.refracdevelopment.simplegems.utilities.files.Files;

import java.sql.SQLException;
import java.util.UUID;

/**
 * Author:  Zachary (Refrac) Baldwin
 * Created: 2021-10-8
 */
@Getter
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
    public void load() {
        if (plugin.getDataType() == DataType.MYSQL) {
            plugin.getSqlManager().select("SELECT * FROM simplegems WHERE uuid=?", resultSet -> {
                try {
                    if (resultSet.next()) {
                        this.gems.setStat(resultSet.getDouble("gems"));
                    } else {
                        this.save();
                    }
                } catch (SQLException e) {
                    Color.log(e.getMessage());
                }
            }, uuid.toString());
        } else if (plugin.getDataType() == DataType.YAML) {
            this.gems.setStat(Files.getData().getDouble("data." + this.uuid.toString() + ".gems"));
        }
    }

    /**
     * The #save method allows you to
     * save a specified player's data
     */
    public void save() {
        if (plugin.getDataType() == DataType.MYSQL) {
            SimpleGems.getInstance().getSqlManager().execute("INSERT INTO simplegems (name,uuid,gems) VALUES (?,?,?) ON DUPLICATE KEY UPDATE name=?,gems=?",

                    // INSERT
                    this.name,
                    this.uuid.toString(),
                    this.gems.getStat(),
                    // UPDATE
                    this.name,
                    this.gems.getStat()

            );
        } else if (plugin.getDataType() == DataType.YAML) {
            Files.getData().set("data." + this.uuid.toString() + ".name", this.name);
            Files.getData().set("data." + this.uuid.toString() + ".gems", this.gems.getStat());
            SimpleGems.getInstance().getManager(Files.class).saveData();
        }
    }
}