package me.refracdevelopment.simplegems.menu;

import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.manager.configuration.cache.Menus;
import me.refracdevelopment.simplegems.utilities.ItemBuilder;
import me.refracdevelopment.simplegems.utilities.Utilities;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import me.refracdevelopment.simplegems.utilities.menu.Menu;
import me.refracdevelopment.simplegems.utilities.menu.PlayerMenuUtility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GemShopGUI extends Menu {

    public GemShopGUI(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return Color.translate(playerMenuUtility.getOwner(), Menus.GEM_SHOP_CATEGORIES.getString("default.title"));
    }

    @Override
    public int getSlots() {
        return Menus.GEM_SHOP_CATEGORIES.getInt("default.size");
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getCurrentItem() == null) return;
        if (event.getCurrentItem().getItemMeta() == null) return;

        SimpleGems.getInstance().getGemShop().getItems().forEach(item -> {
            if (item.getCategoryName().equalsIgnoreCase("default") && item.getSlot() == event.getRawSlot()) {
                if (item.isAction()) {
                    item.getActions().forEach(action -> {
                        if (action.startsWith("{openmenu:")) {
                            String menuName = action.replace("{openmenu:", "").replace("}", "");

                            if (Menus.GEM_SHOP_CATEGORIES.isSet(menuName)) {
                                new GemShopCategory(playerMenuUtility, action
                                        .replace("{openmenu:", "")
                                        .replace("}", "")).open();
                            }
                        }
                    });
                } else {
                    item.handlePurchase(player);
                }
            }
        });
    }

    @Override
    public void setMenuItems() {
        Player player = playerMenuUtility.getOwner();

        SimpleGems.getInstance().getGemShop().getItems().forEach(item -> {
            if (item.getCategoryName().equalsIgnoreCase("default")) {
                getInventory().setItem(item.getSlot(), item.getItem(player));
            }
        });

        for (int i = 0; i < getSlots(); i++) {
            if (getInventory().getItem(i) == null) {
                String name = Menus.GEM_SHOP_CATEGORIES.getString("default.fill.name");
                Material material = Utilities.getMaterial(Menus.GEM_SHOP_CATEGORIES.getString("default.fill.material")).parseMaterial();
                int data = Menus.GEM_SHOP_CATEGORIES.getInt("default.fill.data");;
                ItemBuilder fillItem = new ItemBuilder(material);

                fillItem.setName(Color.translate(player, name));
                fillItem.setDurability(data);

                getInventory().setItem(i, fillItem.toItemStack());
            }
        }
    }
}