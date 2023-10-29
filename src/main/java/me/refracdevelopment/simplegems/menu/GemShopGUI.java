package me.refracdevelopment.simplegems.menu;

import me.refracdevelopment.simplegems.SimpleGems;
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
        return Color.translate(playerMenuUtility.getOwner(), SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getString("gems-menu.title"));
    }

    @Override
    public int getSlots() {
        return SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getInt("gems-menu.size");
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getCurrentItem() == null) return;
        if (event.getCurrentItem().getItemMeta() == null) return;

        SimpleGems.getInstance().getGemShop().getItems().forEach(item -> {
            if (item.getCategoryName().equalsIgnoreCase("gems-menu") && item.getSlot() == event.getRawSlot()) {
                item.handleItem(player);
            }
        });
    }

    @Override
    public void setMenuItems() {
        Player player = playerMenuUtility.getOwner();

        SimpleGems.getInstance().getGemShop().getItems().forEach(item -> {
            if (item.getCategoryName().equalsIgnoreCase("gems-menu")) {
                getInventory().setItem(item.getSlot(), item.getItem(player));
            }
        });

        for (int i = 0; i < getSlots(); i++) {
            if (getInventory().getItem(i) == null) {
                String name = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getString("gems-menu.fill.name");
                Material material = Utilities.getMaterial(SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getString("gems-menu.fill.material")).parseMaterial();
                int data = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getInt("gems-menu.fill.data");;
                ItemBuilder fillItem = new ItemBuilder(material);

                fillItem.setName(Color.translate(player, name));
                fillItem.setDurability(data);

                getInventory().setItem(i, fillItem.toItemStack());
            }
        }
    }
}