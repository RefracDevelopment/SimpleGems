package me.refracdevelopment.simplegems.plugin.manager.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.refracdevelopment.simplegems.plugin.SimpleGems;
import me.refracdevelopment.simplegems.plugin.utilities.Manager;
import me.refracdevelopment.simplegems.plugin.utilities.chat.Color;
import me.refracdevelopment.simplegems.plugin.utilities.files.Files;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLManager extends Manager {

    private HikariDataSource hikariDataSource;
    private final String host = Files.getConfig().getString("mysql.host");
    private final String username = Files.getConfig().getString("mysql.username");
    private final String password = Files.getConfig().getString("mysql.password");
    private final String database = Files.getConfig().getString("mysql.database");
    private final String port = Files.getConfig().getString("mysql.port");

    public SQLManager(SimpleGems plugin) {
        super(plugin);
        Color.log("&eEnabling MySQL support!");
        Exception ex = connect();
        if (ex != null) {
            Color.log("&cThere was an error connecting to your database. Here's the suspect: &e" + ex.getLocalizedMessage());
            ex.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(plugin);
        } else {
            Color.log("&aManaged to successfully connect to: &e" + database + "&a!");
        }
        Bukkit.getScheduler().runTaskAsynchronously(plugin, this::createTables);
    }

    public Exception connect() {
        try {
            HikariConfig hikariConfig = new HikariConfig();
            Class.forName("com.mysql.cj.jdbc.Driver");
            hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
            hikariConfig.setJdbcUrl("jdbc:mysql://" + host + ':' + port + '/' + database);
            hikariConfig.setUsername(username);
            hikariConfig.setPassword(password);
            hikariConfig.setConnectionTimeout(10 * 1000);
            hikariConfig.setMaxLifetime(30 * 1000);

            hikariDataSource = new HikariDataSource(hikariConfig);
        } catch (Exception exception) {
            hikariDataSource = null;
            return exception;
        }
        return null;
    }

    public void createTables() {
        createTable("simplegems", "uuid VARCHAR(36) NOT NULL PRIMARY KEY, name VARCHAR(16), gems BIGINT(50) DEFAULT 0");
    }

    public boolean isInitiated() {
        return hikariDataSource != null;
    }

    public void close() {
        this.hikariDataSource.close();
    }


    /**
     * @return A new database connecting, provided by the Hikari pool.
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }

    /**
     * Create a new table in the database.
     *
     * @param name The name of the table.
     * @param info The table info between the round VALUES() brackets.
     */
    public void createTable(String name, String info) {
        new Thread(() -> {
            try (Connection resource = getConnection(); PreparedStatement statement = resource.prepareStatement("CREATE TABLE IF NOT EXISTS " + name + "(" + info + ");")) {
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
            try (Connection resource = getConnection(); PreparedStatement statement = resource.prepareStatement(query)) {
                for (int i = 0; i < values.length; i++) {
                    statement.setObject((i + 1), values[i]);
                }
                statement.execute();
            } catch (SQLException exception) {
                Color.log("An error occurred while executing an update on the database.");
                Color.log("MySQL#execute : " + query);
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
            try (Connection resource = getConnection(); PreparedStatement statement = resource.prepareStatement(query)) {
                for (int i = 0; i < values.length; i++) {
                    statement.setObject((i + 1), values[i]);
                }
                callback.call(statement.executeQuery());
            } catch (SQLException exception) {
                Color.log("An error occurred while executing a query on the database.");
                Color.log("MySQL#select : " + query);
                exception.printStackTrace();
            }
        }).start();
    }

}