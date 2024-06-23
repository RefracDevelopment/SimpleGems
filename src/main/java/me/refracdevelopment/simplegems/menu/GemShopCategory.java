package me.refracdevelopment.simplegems.menu;

import lombok.Getter;
import me.kodysimpson.simpapi.menu.Menu;
import me.kodysimpson.simpapi.menu.PlayerMenuUtility;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.utilities.ItemBuilder;
import me.refracdevelopment.simplegems.utilities.Methods;
import me.refracdevelopment.simplegems.utilities.chat.RyMessageUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

@Getter
public class GemShopCategory extends Menu {

    private final String categoryName, permission;
    private final boolean enabled, isDefault;

    public GemShopCategory(PlayerMenuUtility playerMenuUtility, String categoryName) {
        super(playerMenuUtility);

        this.categoryName = categoryName;
        this.permission = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getString(categoryName + ".permission", "");
        this.enabled = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(categoryName + ".enabled");
        this.isDefault = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(categoryName + ".default");
    }

    @Override
    public String getMenuName() {
        return RyMessageUtils.translate(playerMenuUtility.getOwner(), SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getString(categoryName + ".title"));
    }

    @Override
    public int getSlots() {
        return SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getInt(categoryName + ".size");
    }

    @Override
    public boolean cancelAllClicks() {
        return true;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (playerMenuUtility == null)
            return;
        if (event.getCurrentItem() == null)
            return;
        if (event.getCurrentItem().getItemMeta() == null)
            return;

        SimpleGems.getInstance().getGemShop().getItems(categoryName).forEach(item -> {
            if (item.getCategory().equalsIgnoreCase(categoryName) && item.getSlot() == event.getRawSlot())
                item.handleItem(player);
        });
    }

    @Override
    public void setMenuItems() {
        if (playerMenuUtility == null)
            return;

        SimpleGems.getInstance().getGemShop().getItems(categoryName).forEach(item -> {
            if (item.getCategory().equalsIgnoreCase(categoryName))
                getInventory().setItem(item.getSlot(), item.getItem(playerMenuUtility.getOwner()));
        });

        if (SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(categoryName + ".fill.enabled")) {
            for (int i = 0; i < getSlots(); i++) {
                if (getInventory().getItem(i) != null)
                    continue;

                String name = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getString(categoryName + ".fill.name");
                Material material = Methods.getMaterial(SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getString(categoryName + ".fill.material")).parseMaterial();
                int durability = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getInt(categoryName + ".fill.durability");
                ItemBuilder item = new ItemBuilder(material);

                item.setName(RyMessageUtils.translate(playerMenuUtility.getOwner(), name));
                item.setDurability(durability);

                getInventory().setItem(i, item.toItemStack());
            }
        }
    }
}