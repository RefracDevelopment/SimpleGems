package me.refracdevelopment.simplegems.utilities.menu;

import me.refracdevelopment.simplegems.utilities.exceptions.MenuManagerException;
import me.refracdevelopment.simplegems.utilities.exceptions.MenuManagerNotSetupException;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.UUID;

/**
 * Used to interface with the Menu Manager API
 */
public class MenuManager {

    //each player will be assigned their own PlayerMenuUtility object
    private static final HashMap<UUID, PlayerMenuUtil> playerMenuUtilityMap = new HashMap<>();
    private static boolean isSetup = false;

    private static void registerMenuListener(Server server, Plugin plugin) {
        boolean isAlreadyRegistered = false;
        for (RegisteredListener rl : InventoryClickEvent.getHandlerList().getRegisteredListeners()) {
            if (rl.getListener() instanceof MenuListener) {
                isAlreadyRegistered = true;
                break;
            }
        }

        if (!isAlreadyRegistered) {
            server.getPluginManager().registerEvents(new MenuListener(), plugin);
        }
    }

    /**
     * @param server The instance of your server. Provide by calling getServer()
     * @param plugin The instance of the plugin using this API. Can provide in plugin class by passing this keyword
     */
    public static void setup(Server server, Plugin plugin) {
        registerMenuListener(server, plugin);
        isSetup = true;
    }

    /**
     * @param menuClass The class reference of the Menu you want to open for a player
     * @param player    The player to open the menu for
     * @throws MenuManagerNotSetupException Thrown if the setup() method has not been called and used properly
     */
    public static void openMenu(Class<? extends Menu> menuClass, Player player) throws MenuManagerException, MenuManagerNotSetupException {
        try {
            menuClass.getConstructor(PlayerMenuUtil.class).newInstance(getPlayerMenuUtil(player)).open();
        } catch (InstantiationException e) {
            throw new MenuManagerException("Failed to instantiate menu class", e);
        } catch (IllegalAccessException e) {
            throw new MenuManagerException("Illegal access while trying to instantiate menu class", e);
        } catch (InvocationTargetException e) {
            throw new MenuManagerException("An error occurred while trying to invoke the menu class constructor", e);
        } catch (NoSuchMethodException e) {
            throw new MenuManagerException("The menu class constructor could not be found", e);
        }
    }

    public static PlayerMenuUtil getPlayerMenuUtil(Player p) throws MenuManagerNotSetupException {
        if (!isSetup) {
            throw new MenuManagerNotSetupException();
        }

        PlayerMenuUtil playerMenuUtil;
        if (!(playerMenuUtilityMap.containsKey(p.getUniqueId()))) { //See if the player has a pmu "saved" for them

            //Construct PMU
            playerMenuUtil = new PlayerMenuUtil(p);
            playerMenuUtilityMap.put(p.getUniqueId(), playerMenuUtil);

            return playerMenuUtil;
        } else {
            return playerMenuUtilityMap.get(p.getUniqueId()); //Return the object by using the provided player
        }
    }

    public static void remove(Player p) throws MenuManagerNotSetupException {
        if (!isSetup) {
            throw new MenuManagerNotSetupException();
        }

        playerMenuUtilityMap.remove(p.getUniqueId());
    }
}
