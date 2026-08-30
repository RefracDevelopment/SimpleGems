package me.refracdevelopment.simplegems.menu.utilities;

import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.menu.GemShopItem;
import me.refracdevelopment.simplegems.utilities.ItemBuilder;
import me.refracdevelopment.simplegems.utilities.Methods;
import me.refracdevelopment.simplegems.utilities.chat.RyMessageUtils;
import me.refracdevelopment.simplegems.utilities.exceptions.MenuManagerException;
import me.refracdevelopment.simplegems.utilities.exceptions.MenuManagerNotSetupException;
import me.refracdevelopment.simplegems.utilities.menu.Menu;
import me.refracdevelopment.simplegems.utilities.menu.PlayerMenuUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ConfirmationMenu extends Menu {

    private final GemShopItem gemShopItem;

    public ConfirmationMenu(PlayerMenuUtil playerMenuUtil, GemShopItem item) {
        super(playerMenuUtil);
        gemShopItem = item;
    }

    @Override
    public String getMenuName() {
        return RyMessageUtils.translate(playerMenuUtil.getOwner(), SimpleGems.getInstance().getMenus().CONFIRMATION_MENU.getString("title"));
    }

    public String getConfigName() {
        return "confirmation-menu";
    }

    @Override
    public int getSlots() {
        return SimpleGems.getInstance().getMenus().CONFIRMATION_MENU.getInt("size");
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        event.setCancelled(true);

        SimpleGems.getInstance().getGemShop().getItems(getConfigName()).forEach(item -> {
            if (item.getCategory().equalsIgnoreCase(getConfigName()) && item.getSlot() == event.getRawSlot()) {
                if (item.getItem().equalsIgnoreCase("confirm"))
                    gemShopItem.handleItem(player, true, false);
                else if (item.getItem().equalsIgnoreCase("cancel"))
                    gemShopItem.handleItem(player, false, true);
            }
        });
    }

    @Override
    public void setMenuItems() {
        if (playerMenuUtil == null)
            return;

        ConfigurationSection section = SimpleGems.getInstance().getMenus().CONFIRMATION_MENU;

        SimpleGems.getInstance().getGemShop().getItems(getConfigName()).forEach(item -> {
            if (item.getCategory().equalsIgnoreCase(getConfigName()))
                getInventory().setItem(item.getSlot(), item.getItem(playerMenuUtil.getOwner()));
        });

        if (section.getBoolean("fill.enabled")) {
            for (int i = 0; i < getSlots(); i++) {
                if (getInventory().getItem(i) != null)
                    continue;

                String name = section.getString("fill.name");
                Material material = Methods.getMaterial(section.getString("fill.material")).parseMaterial();
                int durability = section.getInt("fill.durability");
                ItemBuilder item = new ItemBuilder(material);

                item.setName(RyMessageUtils.translate(playerMenuUtil.getOwner(), name));
                item.setDurability(durability);

                getInventory().setItem(i, item.toItemStack());
            }
        }
    }
}
