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
package me.refracdevelopment.simplegems.plugin.listeners;

import me.refracdevelopment.simplegems.plugin.SimpleGems;
import me.refracdevelopment.simplegems.plugin.manager.Profile;
import me.refracdevelopment.simplegems.plugin.utilities.Manager;
import me.refracdevelopment.simplegems.plugin.utilities.Settings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Author:  Zachary (Refrac) Baldwin
 * Created: 2021-10-8
 */
public class JoinListener extends Manager implements Listener {

    public JoinListener(SimpleGems plugin) {
        super(plugin);
    }

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        try {
            plugin.getProfileManager().handleProfileCreation(event.getUniqueId(), event.getName());
        } catch (NullPointerException e) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, "Error: Profile could not be created");
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Profile profile = plugin.getProfileManager().getProfile(player);

        try {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> profile.getData().load());
        } catch (NullPointerException e) {
            player.kickPlayer("Error: Player profile could not be loaded!");
        }

        if (!player.getUniqueId().toString().equalsIgnoreCase(Settings.getDevUUID)) return;

        Settings.devMessage(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = plugin.getProfileManager().getProfile(player);

        if (profile != null) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> profile.getData().save());
            profile.getData().getGems().remove(player.getUniqueId());
        }
    }
}