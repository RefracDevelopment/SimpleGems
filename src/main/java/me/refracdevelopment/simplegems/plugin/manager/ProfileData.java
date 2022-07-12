/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2022 RefracDevelopment
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package me.refracdevelopment.simplegems.plugin.manager;

import lombok.Getter;
import me.refracdevelopment.simplegems.plugin.SimpleGems;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
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

    private final Map<UUID, Double> gems = new HashMap<>();
    
    public ProfileData(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }
    
    /**
     * The #load method allows you to
     * load a specified player's data
     */
    public void load() {
        try {
            PreparedStatement statement = plugin.getSqlManager().getConnection().prepareStatement("SELECT * FROM simplegems WHERE uuid=?");
            statement.setString(1, this.uuid.toString());
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                this.gems.put(this.uuid, result.getDouble("gems"));
            } else this.save();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * The #save method allows you to
     * save a specified player's data
     */
    public void save() {
        try {
            PreparedStatement statement = plugin.getSqlManager().getConnection().prepareStatement("SELECT * FROM simplegems WHERE uuid=?");
            statement.setString(1, this.uuid.toString());

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                PreparedStatement update = plugin.getSqlManager().getConnection().prepareStatement("UPDATE simplegems SET "
                        + "name=?,uuid=?,gems=? WHERE uuid=?");

                update.setString(1, this.name);
                update.setString(2, this.uuid.toString());
                update.setDouble(3, this.gems.getOrDefault(this.uuid, 0.0));
                update.setString(4, this.uuid.toString());

                update.executeUpdate();
                update.close();
            } else {
                PreparedStatement insert = plugin.getSqlManager().getConnection().prepareStatement("INSERT INTO simplegems ("
                        + "name,uuid,gems) VALUES ("
                        + "?,?,?)");

                insert.setString(1, this.name);
                insert.setString(2, this.uuid.toString());
                insert.setDouble(3, this.gems.getOrDefault(this.uuid, 0.0));

                insert.executeUpdate();
                insert.close();
            }

            plugin.getSqlManager().close(statement, result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}