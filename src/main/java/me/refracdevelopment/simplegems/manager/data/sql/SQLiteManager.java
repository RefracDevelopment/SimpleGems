package me.refracdevelopment.simplegems.manager.data.sql;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import me.refracdevelopment.simplegems.manager.data.sql.entities.PlayerGems;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SQLiteManager {

    private final Dao<PlayerGems, String> playerGemsDao;

    public SQLiteManager(String path) throws SQLException, ClassNotFoundException {
        Color.log("&aConnecting to SQLite...");
        Class.forName("org.sqlite.JDBC");
        ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:sqlite:" + path);
        TableUtils.createTableIfNotExists(connectionSource, PlayerGems.class);
        playerGemsDao = DaoManager.createDao(connectionSource, PlayerGems.class);
        Color.log("&aConnected to SQLite!");
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