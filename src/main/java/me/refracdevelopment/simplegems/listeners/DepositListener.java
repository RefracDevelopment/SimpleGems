package me.refracdevelopment.simplegems.listeners;

import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.manager.LocaleManager;
import me.refracdevelopment.simplegems.manager.data.Profile;
import me.refracdevelopment.simplegems.utilities.Methods;
import me.refracdevelopment.simplegems.utilities.chat.Placeholders;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class DepositListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Profile profile = SimpleGems.getInstance().getProfileManager().getProfile(player.getUniqueId());
        final LocaleManager locale = SimpleGems.getInstance().getManager(LocaleManager.class);

        ItemStack gemsItem = Methods.getGemsItem();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getItemMeta() == null) return;
        if (!item.hasItemMeta()) return;
        if (item.getType() != gemsItem.getType()) return;
        if (!item.getItemMeta().getDisplayName().equals(gemsItem.getItemMeta().getDisplayName())) return;

        player.getInventory().setItemInMainHand(null);
        profile.getData().getGems().incrementStat(item.getAmount());
        locale.sendMessage(player, "gems-deposited", Placeholders.setPlaceholders(player));
    }
}