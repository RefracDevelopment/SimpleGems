package me.refracdevelopment.simplegems.manager.data;

import dev.rosewood.rosegarden.lib.hikaricp.HikariConfig;
import dev.rosewood.rosegarden.lib.hikaricp.HikariDataSource;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.manager.configuration.ConfigurationManager;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQLManager {

    private final SimpleGems plugin;
    private HikariDataSource dataSource;
    private final String host = ConfigurationManager.Setting.MYSQL_HOST.getString();
    private final String username = ConfigurationManager.Setting.MYSQL_USERNAME.getString();
    private final String password = ConfigurationManager.Setting.MYSQL_PASSWORD.getString();
    private final String database = ConfigurationManager.Setting.MYSQL_DATABASE.getString();
    private final String port = ConfigurationManager.Setting.MYSQL_PORT.getString();

    public MySQLManager(SimpleGems plugin) {
        this.plugin = plugin;
    }

    public void createT() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> createTables());
    }

    public boolean connect() {
        try {
            Color.log("&eConnecting to MySQL...");
            HikariConfig config = new HikariConfig();
            Class.forName("com.mysql.cj.jdbc.Driver");
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");
            config.setJdbcUrl("jdbc:mysql://" + host + ':' + port + '/' + database);
            config.setUsername(username);
            config.setPassword(password);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            dataSource = new HikariDataSource(config);
            Color.log("&eConnected to MySQL!");
            return true;
        } catch (Exception exception) {
            Color.log("&cCould not connect to MySQL! Error: " + exception.getMessage());
            return false;
        }
    }

    public void shutdown() {
        close();
    }



    public void createTables() {
        createTable("SimpleGems", "uuid VARCHAR(36) NOT NULL PRIMARY KEY, name VARCHAR(16), gems BIGINT");
    }

    public boolean isInitiated() {
        return dataSource != null;
    }

    public void close() {
        this.dataSource.close();
    }


    /**
     * @return A new database connecting, provided by the Hikari pool.
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
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