package me.refracdevelopment.simplegems.listeners;

import dev.rosewood.rosegarden.utils.StringPlaceholders;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.data.Profile;
import me.refracdevelopment.simplegems.manager.LocaleManager;
import me.refracdevelopment.simplegems.utilities.Methods;
import me.refracdevelopment.simplegems.utilities.Permissions;
import me.refracdevelopment.simplegems.utilities.Utilities;
import me.refracdevelopment.simplegems.utilities.chat.Placeholders;
import me.refracdevelopment.simplegems.utilities.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PlayerListener implements Listener {

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        try {
            SimpleGems.getInstance().getProfileManager().handleProfileCreation(event.getUniqueId(), event.getName());
        } catch (NullPointerException exception) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, "&cERROR: Could not create profile!");
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Profile profile = SimpleGems.getInstance().getProfileManager().getProfile(player.getUniqueId());

        try {
            Bukkit.getScheduler().runTaskAsynchronously(SimpleGems.getInstance(), () -> profile.getData().load(player));
        } catch (NullPointerException exception) {
            player.kickPlayer(ChatColor.RED + "ERROR: Profile returned null.");
            return;
        }

        if (profile == null || profile.getData() == null) {
            player.kickPlayer(ChatColor.RED + "ERROR: Profile returned null.");
            return;
        }

        if (player.getUniqueId().equals(Utilities.getDevUUID)) {
            Utilities.sendDevMessage(player);
        } else if (player.getUniqueId().equals(Utilities.getDevUUID2)) {
            Utilities.sendDevMessage(player);
        }

        if (Config.UPDATE_ON_JOIN) {
            if (!player.hasPermission(Permissions.UPDATE_ON_JOIN)) return;

            SimpleGems.getInstance().updateCheck(player, false);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = SimpleGems.getInstance().getProfileManager().getProfile(player.getUniqueId());
        if (profile == null) return;
        if (profile.getData() == null) return;

        Bukkit.getScheduler().runTaskAsynchronously(SimpleGems.getInstance(), () -> profile.getData().save(player));
    }

    @EventHandler
    public void onDeposit(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Profile profile = SimpleGems.getInstance().getProfileManager().getProfile(player.getUniqueId());
        final LocaleManager locale = SimpleGems.getInstance().getManager(LocaleManager.class);

        if (profile == null) return;
        if (profile.getData() == null) return;

        ItemStack item = player.getInventory().getItemInMainHand();
        ItemStack gemsItem;
        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta == null) return;

        NamespacedKey key = new NamespacedKey(SimpleGems.getInstance(), "gems-item-value");
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        if (container.has(key, PersistentDataType.INTEGER)) {
            int foundValue = container.get(key, PersistentDataType.INTEGER);

            gemsItem = Methods.getGemsItem(foundValue);
        } else {
            gemsItem = Methods.getGemsItem(1);
        }

        if (!item.isSimilar(gemsItem)) return;
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) return;

        if (container.has(key, PersistentDataType.INTEGER)) {
            int foundValue = container.get(key, PersistentDataType.INTEGER);

            StringPlaceholders placeholders = StringPlaceholders.builder()
                    .addAll(Placeholders.setPlaceholders(player))
                    .addPlaceholder("gems", Methods.format(foundValue))
                    .addPlaceholder("gems_decimal", Methods.formatDec(foundValue))
                    .build();

            player.getInventory().setItemInMainHand(null);
            profile.getData().getGems().incrementAmount(foundValue);
            Bukkit.getScheduler().runTaskAsynchronously(SimpleGems.getInstance(), () -> profile.getData().save(player));
            locale.sendMessage(player, "gems-deposited", placeholders);
        }
    }
}