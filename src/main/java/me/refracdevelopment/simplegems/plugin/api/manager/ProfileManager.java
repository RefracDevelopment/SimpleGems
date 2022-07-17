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
package me.refracdevelopment.simplegems.plugin.api.manager;

import me.refracdevelopment.simplegems.plugin.SimpleGems;
import me.refracdevelopment.simplegems.plugin.manager.Profile;

import java.util.Map;
import java.util.UUID;

/**
 * Author:  Zachary (Refrac) Baldwin
 * Created: 2022-7-14
 */
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
