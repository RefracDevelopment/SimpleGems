package me.refracdevelopment.simplegems.data;

import dev.rosewood.rosegarden.lib.hikaricp.HikariConfig;
import dev.rosewood.rosegarden.lib.hikaricp.HikariDataSource;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.manager.ConfigurationManager;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLManager {

    private final SimpleGems plugin = SimpleGems.getInstance();
    private HikariDataSource hikariDataSource;
    private final String host = ConfigurationManager.Setting.MYSQL_HOST.getString();
    private final String username = ConfigurationManager.Setting.MYSQL_USERNAME.getString();
    private final String password = ConfigurationManager.Setting.MYSQL_PASSWORD.getString();
    private final String database = ConfigurationManager.Setting.MYSQL_DATABASE.getString();
    private final String port = ConfigurationManager.Setting.MYSQL_PORT.getString();

    public SQLManager() {
        Color.log("&aEnabling MySQL support!");
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

    public void shutdown() {
        close();
    }

    public Exception connect() {
        try {
            HikariConfig hikariConfig = new HikariConfig();
            Class.forName("com.mysql.cj.jdbc.Driver");
            hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
            hikariConfig.setJdbcUrl("jdbc:mysql://" + host + ':' + port + '/' + database);
            hikariConfig.setUsername(username);
            hikariConfig.setPassword(password);
            hikariDataSource = new HikariDataSource(hikariConfig);
        } catch (Exception exception) { // Failsafe method.
            hikariDataSource = null;
            return exception;
        }
        return null;
    }

    public void createTables() {
        createTable("SimpleGems", "uuid VARCHAR(36) NOT NULL PRIMARY KEY, name VARCHAR(16), gems DOUBLE");
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