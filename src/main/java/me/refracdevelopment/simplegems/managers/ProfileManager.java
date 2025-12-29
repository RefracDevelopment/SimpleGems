package me.refracdevelopment.simplegems.managers;

import lombok.Data;
import me.refracdevelopment.simplegems.player.Profile;
import me.refracdevelopment.simplegems.utilities.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
public class ProfileManager {

    private Map<UUID, Profile> profiles = new HashMap<>();

    public void handleProfileCreation(UUID uuid, String name) {
        if (!this.profiles.containsKey(uuid))
            profiles.put(uuid, new Profile(uuid, name));
    }

    public Profile getProfile(Object object) {
        if (object instanceof Player target) {
            if (!this.profiles.containsKey(target.getUniqueId()))
                return null;

            return profiles.get(target.getUniqueId());
        }

        if (object instanceof UUID uuid) {
            if (!this.profiles.containsKey(uuid))
                return null;

            return profiles.get(uuid);
        }

        if (object instanceof String)
            return this.profiles.values().stream().filter(profile -> profile.getPlayerName().equalsIgnoreCase(object.toString())).findFirst().orElse(null);

        return null;
    }
}