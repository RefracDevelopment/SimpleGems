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
package me.refracdevelopment.simplegems.plugin.manager;

import me.refracdevelopment.simplegems.plugin.SimpleGems;
import me.refracdevelopment.simplegems.plugin.utilities.Manager;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Author:  Zachary (Refrac) Baldwin
 * Created: 2021-10-8
 */
public class ProfileManager extends Manager {
    private Map<UUID, Profile> profiles = new HashMap<>();

    public ProfileManager(SimpleGems plugin) {
        super(plugin);
    }

    public void handleProfileCreation(UUID uuid, String name) {
        if (!this.profiles.containsKey(uuid)) {
            profiles.put(uuid, new Profile(uuid, name));
        }
    }

    public Profile getProfile(Object object) {
        if (object instanceof Player) {
            Player target = (Player) object;
            if (!this.profiles.containsKey(target.getUniqueId())) {
                return null;
            }
            return profiles.get(target.getUniqueId());
        }
        if (object instanceof UUID) {
            UUID uuid = (UUID) object;
            if (!this.profiles.containsKey(uuid)) {
                return null;
            }
            return profiles.get(uuid);
        }
        if (object instanceof String) {
            return this.profiles.values().stream().filter(profile -> profile.getPlayerName().equalsIgnoreCase(object.toString())).findFirst().orElse(null);
        }
        return null;
    }

    public Map<UUID, Profile> getProfiles() {
        return this.profiles;
    }

    public void setProfiles(Map<UUID, Profile> profiles) {
        this.profiles = profiles;
    }
}