package me.refracdevelopment.simplegems.manager.data;

import lombok.Getter;
import me.refracdevelopment.simplegems.utilities.Tasks;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

@Getter
public class SQLiteManager {

    private SQLiteDataSource dataSource;

    public void createT() {
        Tasks.runAsync(this::createTables);
    }

    public boolean connect(String path) {
        try {
            Color.log("&aConnecting to SQLite...");
            Class.forName("org.sqlite.JDBC");
            dataSource = new SQLiteDataSource();
            dataSource.setUrl("jdbc:sqlite:" + path);
            Color.log("&aConnected to SQLite!");
            return true;
        } catch (Exception exception) {
            Color.log("&cCould not connect to SQLite! Error: " + exception.getMessage());
            exception.printStackTrace();
            return false;
        }
    }

    public void shutdown() {
        close();
    }

    public void createTables() {
        createTable("SimpleGems",
                "uuid VARCHAR(36) NOT NULL PRIMARY KEY, " +
                        "name VARCHAR(16) NOT NULL, " +
                        "gems BIGINT(50) NOT NULL DEFAULT 0"
        );
    }

    public boolean isInitiated() {
        return dataSource != null;
    }

    public void close() {
        try {
            this.dataSource.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a new table in the database.
     *
     * @param name The name of the table.
     * @param info The table info between the round VALUES() brackets.
     */
    public void createTable(String name, String info) {
        new Thread(() -> {
            try (Connection resource = dataSource.getConnection(); PreparedStatement statement = resource.prepareStatement("CREATE TABLE IF NOT EXISTS " + name + "(" + info + ");")) {
                statement.execute();
            } catch (SQLException exception) {
                Color.log("An error occurred while creating database table " + name + ".");
                exception.printStackTrace();
            }
        }).start();
    }

    /**
     * Execute an update to the database.
     *
     * @param query  The statement to the database.
     * @param values The values to be inserted into the statement.
     */
    public void execute(String query, Object... values) {
        new Thread(() -> {
            try (Connection resource = dataSource.getConnection(); PreparedStatement statement = resource.prepareStatement(query)) {
                for (int i = 0; i < values.length; i++) {
                    statement.setObject((i + 1), values[i]);
                }
                statement.execute();
            } catch (SQLException exception) {
                Color.log("An error occurred while executing an update on the database.");
                Color.log("SQLite#execute : " + query);
                exception.printStackTrace();
            }
        }).start();
    }

    /**
     * Execute a query to the database.
     *
     * @param query    The statement to the database.
     * @param callback The data callback (Async).
     * @param values   The values to be inserted into the statement.
     */
    public void select(String query, SelectCall callback, Object... values) {
        new Thread(() -> {
            try (Connection resource = dataSource.getConnection(); PreparedStatement statement = resource.prepareStatement(query)) {
                for (int i = 0; i < values.length; i++) {
                    statement.setObject((i + 1), values[i]);
                }
                callback.call(statement.executeQuery());
            } catch (SQLException exception) {
                Color.log("An error occurred while executing a query on the database.");
                Color.log("SQLite#select : " + query);
                exception.printStackTrace();
            }
        }).start();
    }

    public void updatePlayerGems(UUID uuid, long gems) {
        execute("UPDATE SimpleGems SET gems=? WHERE uuid=?", gems, uuid.toString());
    }

    public void updatePlayerName(UUID uuid, String name) {
        execute("UPDATE SimpleGems SET name=? WHERE uuid=?", name, uuid.toString());
    }

    public void delete() {
        execute("DELETE FROM SimpleGems");
    }

    public void deletePlayer(UUID uuid) {
        execute("DELETE FROM SimpleGems WHERE uuid=?", uuid.toString());
    }
}