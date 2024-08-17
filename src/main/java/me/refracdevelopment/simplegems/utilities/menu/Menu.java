package me.refracdevelopment.simplegems.utilities.menu;

import me.refracdevelopment.simplegems.utilities.Methods;
import me.refracdevelopment.simplegems.utilities.chat.RyMessageUtils;
import me.refracdevelopment.simplegems.utilities.exceptions.MenuManagerException;
import me.refracdevelopment.simplegems.utilities.exceptions.MenuManagerNotSetupException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Collectors;

/*
    Defines the behavior and attributes of all menus in our plugin
 */
public abstract class Menu implements InventoryHolder {

    //Protected values that can be accessed in the menus
    protected PlayerMenuUtil playerMenuUtil;
    protected Player player;
    protected Inventory inventory;
    protected ItemStack FILLER_GLASS = makeItem(Methods.getMaterial("GRAY_STAINED_GLASS_PANE").parseMaterial(), " ");

    public Menu(PlayerMenuUtil playerMenuUtil) {
        this.playerMenuUtil = playerMenuUtil;
        this.player = playerMenuUtil.getOwner();
    }

    public abstract String getMenuName();

    public abstract int getSlots();

    public abstract void handleMenu(InventoryClickEvent e) throws MenuManagerNotSetupException, MenuManagerException;

    public abstract void setMenuItems();

    public void open() {
        inventory = Bukkit.createInventory(this, getSlots(), getMenuName());

        this.setMenuItems();

        playerMenuUtil.getOwner().openInventory(inventory);
        playerMenuUtil.pushMenu(this);
    }

    public void back() throws MenuManagerException, MenuManagerNotSetupException {
        MenuManager.openMenu(playerMenuUtil.lastMenu().getClass(), playerMenuUtil.getOwner());
    }

    protected void reloadItems() {
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, null);
        }
        setMenuItems();
    }

    protected void reload() throws MenuManagerException, MenuManagerNotSetupException {
        player.closeInventory();
        MenuManager.openMenu(this.getClass(), player);
    }

    //Overridden method from the InventoryHolder interface
    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    /**
     * This will fill all the empty slots with "filler glass"
     */
    //Helpful utility method to fill all remaining slots with "filler glass"
    public void setFillerGlass() {
        for (int i = 0; i < getSlots(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, FILLER_GLASS);
            }
        }
    }

    /**
     * @param itemStack Placed into every empty slot when ran
     */
    public void setFillerGlass(ItemStack itemStack) {
        for (int i = 0; i < getSlots(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, itemStack);
            }
        }
    }

    /**
     * @param material    The material to base the ItemStack on
     * @param displayName The display name of the ItemStack
     * @param lore        The lore of the ItemStack, with the Strings being automatically color coded with ColorTranslator
     * @return The constructed ItemStack object
     */
    public ItemStack makeItem(Material material, String displayName, String... lore) {

        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(RyMessageUtils.translate(displayName));

        //Automatically translate color codes provided
        itemMeta.setLore(Arrays.stream(lore).map(RyMessageUtils::translate).collect(Collectors.toList()));
        item.setItemMeta(itemMeta);

        return item;
    }

}
