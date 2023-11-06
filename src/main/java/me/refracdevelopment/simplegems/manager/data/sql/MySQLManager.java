package me.refracdevelopment.simplegems.manager.data.sql;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.manager.data.sql.entities.PlayerGems;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MySQLManager {

    private final Dao<PlayerGems, String> playerGemsDao;

    public MySQLManager() throws ClassNotFoundException, SQLException {
        Color.log("&aConnecting to MySQL...");
        Class.forName("org.mariadb.jdbc.Driver");
        String host = SimpleGems.getInstance().getConfigFile().getString("mysql.host");
        String username = SimpleGems.getInstance().getConfigFile().getString("mysql.username");
        String password = SimpleGems.getInstance().getConfigFile().getString("mysql.password");
        String database = SimpleGems.getInstance().getConfigFile().getString("mysql.database");
        String port = SimpleGems.getInstance().getConfigFile().getString("mysql.port");
        ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:mariadb://" +
                host + ':' + port + '/' + database, username, password);
        TableUtils.createTableIfNotExists(connectionSource, PlayerGems.class);
        playerGemsDao = DaoManager.createDao(connectionSource, PlayerGems.class);
        Color.log("&aConnected to MySQL!");
    }

    public PlayerGems addPlayer(Player player) throws SQLException {
        PlayerGems playerGems = new PlayerGems();
        playerGems.setUuid(player.getUniqueId().toString());
        playerGems.setName(player.getName());
        playerGemsDao.create(playerGems);
        return playerGems;
    }

    public void deletePlayer(UUID uuid) throws SQLException {
        playerGemsDao.deleteById(uuid.toString());
    }

    public void delete() throws SQLException {
        List<PlayerGems> playerGemsList = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            playerGemsList.add(getPlayerGems(player.getUniqueId()));
        }
        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            playerGemsList.add(getPlayerGems(player.getUniqueId()));
        }
        playerGemsDao.delete(playerGemsList);
    }

    public boolean playerExists(UUID uuid) throws SQLException {
        return playerGemsDao.idExists(uuid.toString());
    }

    public void updatePlayerGems(UUID uuid, long gems) throws SQLException {
        PlayerGems playerGems = playerGemsDao.queryForId(uuid.toString());
        if (playerGems != null) {
            playerGems.setGems(gems);
            playerGemsDao.update(playerGems);
        }
    }

    public void updatePlayerName(UUID uuid, String name) throws SQLException {
        PlayerGems playerGems = playerGemsDao.queryForId(uuid.toString());
        if (playerGems != null) {
            playerGems.setName(name);
            playerGemsDao.update(playerGems);
        }
    }

    public PlayerGems getPlayerGems(UUID uuid) throws SQLException {
        return playerGemsDao.queryForId(uuid.toString());
    }

    public List<PlayerGems> getAllPlayers() throws SQLException {
        List<PlayerGems> playerGemsList = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            playerGemsList.add(getPlayerGems(player.getUniqueId()));
        }
        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            playerGemsList.add(getPlayerGems(player.getUniqueId()));
        }
        return playerGemsList;
    }

}