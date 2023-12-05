package me.refracdevelopment.simplegems.menu;

import lombok.Getter;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.utilities.ItemBuilder;
import me.refracdevelopment.simplegems.utilities.Utilities;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import me.refracdevelopment.simplegems.utilities.menu.Menu;
import me.refracdevelopment.simplegems.utilities.menu.PlayerMenuUtility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.Nullable;

@Getter
public class GemShopCategory extends Menu {

    private final String categoryName;
    private final boolean enabled, defaultCategory;

    public GemShopCategory(@Nullable PlayerMenuUtility playerMenuUtility, String categoryName) {
        super(playerMenuUtility);
        this.categoryName = categoryName;
        this.enabled = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(categoryName + ".enabled");
        this.defaultCategory = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(categoryName + ".default");
    }

    @Override
    public String getMenuName() {
        return Color.translate(playerMenuUtility.getOwner(), SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getString(categoryName + ".title"));
    }

    @Override
    public int getSlots() {
        return SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getInt(categoryName + ".size");
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (playerMenuUtility == null) return;
        if (event.getCurrentItem() == null) return;
        if (event.getCurrentItem().getItemMeta() == null) return;

        SimpleGems.getInstance().getGemShop().getItems().forEach(item -> {
            if (item.getCategoryName().equalsIgnoreCase(categoryName) && item.getSlot() == event.getRawSlot()) {
                item.handleItem(player);
            }
        });
    }

    @Override
    public void setMenuItems() {
        if (playerMenuUtility == null) return;

        SimpleGems.getInstance().getGemShop().getItems().forEach(item -> {
            if (item.getCategoryName().equalsIgnoreCase(categoryName)) {
                getInventory().setItem(item.getSlot(), item.getItem(playerMenuUtility.getOwner()));
            }
        });

        if (SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(categoryName + ".fill.enabled")) {
            for (int i = 0; i < getSlots(); i++) {
                if (getInventory().getItem(i) != null) continue;

                String name = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getString(categoryName + ".fill.name");
                Material material = Utilities.getMaterial(SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getString(categoryName + ".fill.material")).parseMaterial();
                int data = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getInt(categoryName + ".fill.data");
                ItemBuilder item = new ItemBuilder(material);

                item.setName(name);
                item.setDurability(data);

                getInventory().setItem(i, item.toItemStack());
            }
        }
    }
}