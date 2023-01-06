package me.refracdevelopment.simplegems.listeners;

import dev.rosewood.rosegarden.utils.StringPlaceholders;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.data.Profile;
import me.refracdevelopment.simplegems.manager.LocaleManager;
import me.refracdevelopment.simplegems.utilities.Methods;
import me.refracdevelopment.simplegems.utilities.Utilities;
import me.refracdevelopment.simplegems.utilities.chat.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

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

        if (profile == null) {
            player.kickPlayer(ChatColor.RED + "ERROR: Profile returned null.");
            return;
        }

        if (player.getUniqueId().equals(Utilities.getDevUUID)) {
            Utilities.sendDevMessage(player);
        } else if (player.getUniqueId().equals(Utilities.getDevUUID2)) {
            Utilities.sendDevMessage(player);
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

        ItemStack gemsItem = Methods.getGemsItem();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (!item.isSimilar(gemsItem)) return;
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) return;

        StringPlaceholders placeholders = StringPlaceholders.builder()
                .addAll(Placeholders.setPlaceholders(player))
                .addPlaceholder("gems", Methods.format(item.getAmount()))
                .addPlaceholder("gems_decimal", Methods.formatDec(item.getAmount()))
                .build();

        player.getInventory().setItemInMainHand(null);
        profile.getData().getGems().incrementAmount(item.getAmount());
        locale.sendMessage(player, "gems-deposited", placeholders);
    }
}