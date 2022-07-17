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
package me.refracdevelopment.simplegems.plugin.manager.database;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.refracdevelopment.simplegems.plugin.SimpleGems;
import me.refracdevelopment.simplegems.plugin.utilities.files.Config;
import me.refracdevelopment.simplegems.plugin.utilities.files.Files;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

/**
 * Author:  Zachary (Refrac) Baldwin
 * Created: 2021-10-8
 */
@Getter
@Setter
@RequiredArgsConstructor
public class SQLManager {

    private final SimpleGems plugin = SimpleGems.getInstance();
    private final String host = Files.getConfig().getString("mysql.host");
    private final String username = Files.getConfig().getString("mysql.username");
    private final String password = Files.getConfig().getString("mysql.password");
    private final String database = Files.getConfig().getString("mysql.database");
    private final int port = Files.getConfig().getInt("mysql.port");

    private Connection connection;

    public void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (Config.DATA_TYPE.equalsIgnoreCase("MYSQL")) {
            try {
                this.connection = DriverManager.getConnection("jdbc:mysql://" +
                        host + ":" + port + "/" + database +
                        "?characterEncoding=latin1&useConfigs=maxPerformance&autoReconnect=true", username, password);
            } catch (SQLException e) {
                this.connection = null;
                e.printStackTrace();
                return;
            }
        } else {
            this.connection = null;
            return;
        }

        this.createTable();
    }

    public void createTable() {
        try {
            PreparedStatement ps = this.connection.prepareStatement("CREATE TABLE IF NOT EXISTS simplegems"
                    + "(name VARCHAR(16) NOT NULL, uuid VARCHAR(48) NOT NULL, gems DOUBLE NOT NULL)");
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close(AutoCloseable... closeables) {
        Arrays.stream(closeables).filter(Objects::nonNull).forEach(closeable -> {
            try {
                closeable.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // DELETE ALL DATA
    public void emptyTable() {
        try {
            PreparedStatement ps = this.connection.prepareStatement("TRUNCATE simplegems");
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void remove(UUID uuid) {
        try {
            PreparedStatement ps = this.connection.prepareStatement("DELETE FROM simplegems WHERE uuid=?");
            ps.setString(1, uuid.toString());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}