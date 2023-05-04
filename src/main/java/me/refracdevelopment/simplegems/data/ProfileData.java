package me.refracdevelopment.simplegems.data;

import lombok.Getter;
import lombok.Setter;
import me.refracdevelopment.simplegems.SimpleGems;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
@Setter
public class ProfileData {

    private final SimpleGems plugin = SimpleGems.getInstance();
    private final String name;
    private final UUID uuid;

    private Stat gems = new Stat();

    public ProfileData(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public void load() {
        plugin.getPlayerMapper().loadPlayerFile(uuid);
    }

    public void save() {
        plugin.getPlayerMapper().savePlayer(this);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

}