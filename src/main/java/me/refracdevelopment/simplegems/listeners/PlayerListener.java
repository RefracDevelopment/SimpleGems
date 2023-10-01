package me.refracdevelopment.simplegems.listeners;

import de.tr7zw.nbtapi.NBTItem;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.api.SimpleGemsAPI;
import me.refracdevelopment.simplegems.manager.configuration.LocaleManager;
import me.refracdevelopment.simplegems.manager.configuration.cache.Config;
import me.refracdevelopment.simplegems.player.Profile;
import me.refracdevelopment.simplegems.utilities.Methods;
import me.refracdevelopment.simplegems.utilities.Tasks;
import me.refracdevelopment.simplegems.utilities.Utilities;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import me.refracdevelopment.simplegems.utilities.chat.Placeholders;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerListener implements Listener {

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        SimpleGems.getInstance().getProfileManager().handleProfileCreation(player.getUniqueId(), player.getName());

        Profile profile = SimpleGems.getInstance().getProfileManager().getProfile(player.getUniqueId());
        Tasks.runAsync(SimpleGems.getInstance(), () -> profile.getData().load());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Profile profile = SimpleGems.getInstance().getProfileManager().getProfile(player.getUniqueId());
        if (profile == null || profile.getData() == null) {
            player.kickPlayer(Color.translate(Config.KICK_MESSAGES_ERROR));
            return;
        }

        if (!player.hasPlayedBefore()) {
            SimpleGemsAPI.INSTANCE.giveGems(player, Config.STARTING_GEMS);
        }

        if (player.getUniqueId().equals(Utilities.getDevUUID)) {
            Utilities.sendDevMessage(player);
        } else if (player.getUniqueId().equals(Utilities.getDevUUID2)) {
            Utilities.sendDevMessage(player);
        }
    }

    @EventHandler
    public void onReload(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        if (event.getMessage().equalsIgnoreCase("/reload confirm")) {
            Color.sendMessage(player, "%prefix%&cUse of /reload is not recommended as it can cause issues often cases. Please restart your server when possible.");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = SimpleGems.getInstance().getProfileManager().getProfile(player.getUniqueId());
        if (profile == null) return;
        if (profile.getData() == null) return;

        Tasks.runAsync(SimpleGems.getInstance(), () -> profile.getData().save());
        SimpleGems.getInstance().getProfileManager().getProfiles().remove(player.getUniqueId());
    }

    @EventHandler
    public void onDeposit(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Profile profile = SimpleGems.getInstance().getProfileManager().getProfile(player.getUniqueId());
        final LocaleManager locale = SimpleGems.getInstance().getManager(LocaleManager.class);

        if (profile == null) return;
        if (profile.getData() == null) return;

        ItemStack item = player.getItemInHand();
        ItemStack gemsItem;
        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta == null) return;

        NBTItem nbtItem = new NBTItem(item);

        if(nbtItem.hasTag("gems-item-value")) {
            long foundValue = nbtItem.getLong("gems-item-value");

            gemsItem = SimpleGemsAPI.INSTANCE.getGemsItem(foundValue);

            if (!item.isSimilar(gemsItem)) return;
            if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) return;

            StringPlaceholders placeholders = StringPlaceholders.builder()
                    .addAll(Placeholders.setPlaceholders(player))
                    .add("value", String.valueOf(foundValue))
                    .add("gems", String.valueOf(foundValue))
                    .add("gems_formatted", Methods.format(foundValue))
                    .add("gems_decimal", Methods.formatDecimal(foundValue))
                    .build();

            player.getInventory().removeItem(item);
            SimpleGemsAPI.INSTANCE.giveGems(player, foundValue);
            locale.sendMessage(player, "gems-deposited", placeholders);
        }
    }
}