package me.refracdevelopment.simplegems.utilities.menu.actions;

import me.gabytm.util.actions.Action;
import me.refracdevelopment.simplegems.utilities.chat.RyMessageUtils;
import me.refracdevelopment.simplegems.utilities.exceptions.MenuManagerNotSetupException;
import me.refracdevelopment.simplegems.utilities.menu.MenuManager;
import org.bukkit.entity.Player;

public class BackAction implements Action {

    @Override
    public String getID() {
        return "back";
    }

    @Override
    public String getDescription() {
        return "Allows you to go back to a previous menu.";
    }

    @Override
    public String getUsage() {
        return "[back]";
    }

    @Override
    public void run(Player player, String data) {
        try {
            MenuManager.getPlayerMenuUtil(player).lastMenu().open();
        } catch (MenuManagerNotSetupException menuManagerNotSetupException) {
            RyMessageUtils.sendPluginError("THE MENU MANAGER HAS NOT BEEN CONFIGURED. CALL MENUMANAGER.SETUP()");
            player.closeInventory();
        }
    }
}
