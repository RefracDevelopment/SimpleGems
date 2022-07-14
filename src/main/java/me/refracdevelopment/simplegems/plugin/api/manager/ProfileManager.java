package me.refracdevelopment.simplegems.plugin.api.manager;

import me.refracdevelopment.simplegems.plugin.SimpleGems;
import me.refracdevelopment.simplegems.plugin.manager.Profile;

import java.util.Map;
import java.util.UUID;

public class ProfileManager {

    /**
     * Used to create a player's profile.
     * This is done on the first join of the player.
     *
     * @param uuid Player's UUID
     * @param name Player's name
     */
    public void handleProfileCreation(UUID uuid, String name) {
        SimpleGems.getInstance().getProfileManager().handleProfileCreation(uuid, name);
    }

    /**
     * Used to get a player's profile.
     *
     * @param object Player's UUID or Name
     * @return Player's profile
     */
    public Profile getProfile(Object object) {
        return SimpleGems.getInstance().getProfileManager().getProfile(object);
    }

    /**
     * Used to get the list of profiles.
     *
     * @return List of profiles
     */
    public Map<UUID, Profile> getProfiles() {
        return SimpleGems.getInstance().getProfileManager().getProfiles();
    }

    /**
     * Used to set the list of profiles.
     *
     * @param profiles List of profiles
     */
    public void setProfiles(Map<UUID, Profile> profiles) {
        SimpleGems.getInstance().getProfileManager().setProfiles(profiles);
    }
}
