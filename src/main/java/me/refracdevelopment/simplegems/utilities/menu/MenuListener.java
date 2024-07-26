package me.refracdevelopment.simplegems.utilities.menu;

import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.utilities.chat.RyMessageUtils;
import me.refracdevelopment.simplegems.utilities.exceptions.MenuManagerException;
import me.refracdevelopment.simplegems.utilities.exceptions.MenuManagerNotSetupException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class MenuListener implements Listener {

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();

        if (holder instanceof Menu menu) {
            if (e.getCurrentItem() == null)
                return;

            if (menu.cancelAllClicks())
                e.setCancelled(true);

            try {
                menu.handleMenu(e);
            } catch (MenuManagerNotSetupException menuManagerNotSetupException) {
                RyMessageUtils.sendPluginError("THE MENU MANAGER HAS NOT BEEN CONFIGURED. CALL MENUMANAGER.SETUP()");
            } catch (MenuManagerException menuManagerException) {
                menuManagerException.printStackTrace();
            }
        }
    }
}
