package me.refracdevelopment.simplegems.menu;

import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.utilities.ItemBuilder;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import me.refracdevelopment.simplegems.utilities.config.Menus;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class GemShopGUI implements Listener {

    public GemShopGUI() {
        Bukkit.getPluginManager().registerEvents(this, SimpleGems.getInstance());
    }

    private String homeTitle;
    private int homeSize;

    public void openInventory(Player player) {
        homeTitle = Color.translate(player, Menus.GEM_SHOP_TITLE);
        homeSize = Menus.GEM_SHOP_SIZE;
        Inventory inv = Bukkit.createInventory(null, homeSize, homeTitle);

        SimpleGems.getInstance().getGemShop().getItems().values().forEach(item -> inv.setItem(item.getSlot(), item.getItem(player)));

        for (int i = 0; i < homeSize; i++) {
            if (inv.getItem(i) == null) {
                String name = Menus.GEM_SHOP_FILL_NAME;
                Material material = Menus.GEM_SHOP_FILL_MATERIAL;
                int data = Menus.GEM_SHOP_FILL_DATA;
                ItemBuilder item = new ItemBuilder(material);

                item.setName(Color.translate(player, name));
                item.setDurability(data);

                inv.setItem(i, item.toItemStack());
            }
        }

        player.openInventory(inv);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getTitle().equals(homeTitle)) {
            event.setCancelled(true);
            SimpleGems.getInstance().getGemShop().getItems().values().forEach(item -> {
                if (item.getSlot() == event.getRawSlot()) {
                    item.handlePurchase(player);
                }
            });
        }
    }
}