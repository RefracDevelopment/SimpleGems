package me.refracdevelopment.simplegems.utilities.menu.actions;

import me.gabytm.util.actions.Action;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.menu.GemShopCategory;
import me.refracdevelopment.simplegems.utilities.chat.RyMessageUtils;
import me.refracdevelopment.simplegems.utilities.exceptions.MenuManagerNotSetupException;
import me.refracdevelopment.simplegems.utilities.menu.MenuManager;
import org.bukkit.entity.Player;

public class MenuAction implements Action {

    @Override
    public String getID() {
        return "menu";
    }

    @Override
    public String getDescription() {
        return "Open a new menu by it's name";
    }

    @Override
    public String getUsage() {
        return "[menu] menu-name";
    }

    @Override
    public void run(Player player, String data) {
        if (!SimpleGems.getInstance().getGemShop().getCategories().containsKey(data))
            return;

        GemShopCategory category;
        try {
            category = new GemShopCategory(MenuManager.getPlayerMenuUtil(player), data);
        } catch (MenuManagerNotSetupException e) {
            RyMessageUtils.sendPluginError("THE MENU MANAGER HAS NOT BEEN CONFIGURED. CALL MENUMANAGER.SETUP()");
            player.closeInventory();
            return;
        }

        if (!player.hasPermission(category.getPermission()) && !category.getPermission().isEmpty()) {
            player.closeInventory();
            RyMessageUtils.sendPluginMessage(player, "no-permission");
            return;
        }

        if (!category.isEnabled()) {
            player.closeInventory();
            RyMessageUtils.sendPluginMessage(player, "shop-disabled");
            return;
        }

        category.open();
    }
}
