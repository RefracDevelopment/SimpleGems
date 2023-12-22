package me.refracdevelopment.simplegems.utilities.menu;

import lombok.Getter;
import org.bukkit.entity.Player;

/**
 * Companion class to all menus. This is needed to pass information across the entire
 * menu system no matter how many inventories are opened or closed.
 * Each player has one of these objects, and only one.
 */
@Getter
public class PlayerMenuUtility {

    private final Player owner;

    public PlayerMenuUtility(Player p) {
        this.owner = p;
    }

}