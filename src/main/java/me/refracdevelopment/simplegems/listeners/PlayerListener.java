package me.refracdevelopment.simplegems.listeners;

import de.tr7zw.nbtapi.NBTItem;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.player.Profile;
import me.refracdevelopment.simplegems.utilities.Methods;
import me.refracdevelopment.simplegems.utilities.Tasks;
import me.refracdevelopment.simplegems.utilities.chat.Placeholders;
import me.refracdevelopment.simplegems.utilities.chat.RyMessageUtils;
import me.refracdevelopment.simplegems.utilities.chat.StringPlaceholders;
import org.bukkit.Bukkit;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class PlayerListener implements Listener {

    private final UUID getDevUUID = UUID.fromString("d9c670ed-d7d5-45fb-a144-8b8be86c4a2d");
    private final UUID getDevUUID2 = UUID.fromString("ab898e40-9088-45eb-9d69-e0b78e872627");

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        SimpleGems.getInstance().getProfileManager().handleProfileCreation(event.getPlayer().getUniqueId(), event.getPlayer().getName());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Profile profile = SimpleGems.getInstance().getProfileManager().getProfile(player.getUniqueId());

        Tasks.runAsync(() -> profile.getData().load(player));

        if (profile == null || profile.getData() == null) {
            player.kick(RyMessageUtils.translate(player, SimpleGems.getInstance().getLocaleFile().getString("kick-messages-error")));
            return;
        }

        if (!player.hasPlayedBefore())
            SimpleGems.getInstance().getGemsAPI().giveGems(player, SimpleGems.getInstance().getSettings().STARTING_GEMS);

        if (player.getUniqueId().equals(getDevUUID))
            sendDevMessage(player);
        else if (player.getUniqueId().equals(getDevUUID2))
            sendDevMessage(player);
    }

    @EventHandler
    public void onReload(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        if (!event.getMessage().equalsIgnoreCase("/reload confirm"))
            return;

        RyMessageUtils.sendPlayer(player, "&cUse of /reload is not recommended as it can cause issues often cases. Please restart your server when possible.");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = SimpleGems.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (profile == null || profile.getData() == null)
            return;

        Tasks.runAsync(() -> profile.getData().save(player));
    }

    @EventHandler
    public void onDeposit(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Profile profile = SimpleGems.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (profile == null || profile.getData() == null)
            return;

        ItemStack item = player.getInventory().getItemInMainHand();
        ItemStack gemsItem;
        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta == null)
            return;

        NBTItem nbtItem = new NBTItem(item);

        if (nbtItem.hasTag("gems-item-value")) {
            double foundValue = nbtItem.getDouble("gems-item-value");

            gemsItem = SimpleGems.getInstance().getGemsAPI().getGemsItem(player, foundValue);

            if (!item.isSimilar(gemsItem))
                return;

            if (event.getAction().isRightClick() || event.getAction() == Action.PHYSICAL) {
                event.setCancelled(true);
                player.updateInventory();
                return;
            }

            StringPlaceholders placeholders = StringPlaceholders.builder()
                    .addAll(Placeholders.setPlaceholders(player))
                    .add("value", String.valueOf(foundValue))
                    .add("gems", String.valueOf(foundValue))
                    .add("gems_formatted", Methods.format(foundValue))
                    .add("gems_decimal", Methods.formatDecimal(foundValue))
                    .build();

            if (item.getAmount() == 1)
                player.getInventory().setItemInMainHand(null);
            else
                item.setAmount(item.getAmount() - 1);

            player.updateInventory();

            SimpleGems.getInstance().getGemsAPI().depositGems(player, foundValue);

            RyMessageUtils.sendPluginMessage(player, "gems-deposited", placeholders);
        }
    }

    @EventHandler
    public void onItemFrame(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof ItemFrame))
            return;

        Player player = event.getPlayer();

        ItemStack item = player.getInventory().getItemInMainHand();
        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta == null)
            return;

        NBTItem nbtItem = new NBTItem(item);

        if (nbtItem.hasTag("gems-item-value"))
            event.setCancelled(true);
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta == null)
            return;

        NBTItem nbtItem = new NBTItem(item);

        if (nbtItem.hasTag("gems-item-value"))
            event.setCancelled(true);
    }

    private void sendDevMessage(Player player) {
        RyMessageUtils.sendPlayer(player, " ");
        RyMessageUtils.sendPlayer(player, "&aWelcome " + SimpleGems.getInstance().getDescription().getName() + " Developer!");
        RyMessageUtils.sendPlayer(player, "&aThis server is currently running " + SimpleGems.getInstance().getDescription().getName() + " &bv" + SimpleGems.getInstance().getDescription().getVersion() + "&a.");
        RyMessageUtils.sendPlayer(player, "&aPlugin name&7: &f" + SimpleGems.getInstance().getDescription().getName());
        RyMessageUtils.sendPlayer(player, " ");
        RyMessageUtils.sendPlayer(player, "&aServer version&7: &f" + Bukkit.getVersion());
        RyMessageUtils.sendPlayer(player, " ");
    }
}