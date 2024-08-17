package me.refracdevelopment.simplegems.menu;

import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.interfaces.Skull;
import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XItemStack;
import com.cryptomorin.xseries.reflection.XReflection;
import dev.lone.itemsadder.api.CustomStack;
import lombok.Data;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.utilities.ItemBuilder;
import me.refracdevelopment.simplegems.utilities.Methods;
import me.refracdevelopment.simplegems.utilities.chat.Placeholders;
import me.refracdevelopment.simplegems.utilities.chat.RyMessageUtils;
import me.refracdevelopment.simplegems.utilities.chat.StringPlaceholders;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Data
public class GemShopItem {

    private final String category, item;

    private String material;
    private String skullOwner, name, permission;
    private boolean skulls, headDatabase, messageEnabled, broadcastMessage, customData, glow, action, buyable, itemsAdder;
    private int durability, slot, customModelData, amount;
    private List<String> lore, actions, commands, messages;
    private double cost;

    public GemShopItem(String category, String item) {
        this.category = category;
        this.item = item;

        setupMenuItemData();
    }

    private void setupMenuItemData() {
        this.material = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getString(getCategory() + ".items." + getItem() + ".material");
        this.durability = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getInt(getCategory() + ".items." + getItem() + ".durability");
        this.customModelData = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getInt(getCategory() + ".items." + getItem() + ".customModelData");
        this.name = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getString(getCategory() + ".items." + getItem() + ".name");
        this.lore = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getStringList(getCategory() + ".items." + getItem() + ".lore");
        this.actions = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getStringList(getCategory() + ".items." + getItem() + ".action.actions");
        this.commands = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getStringList(getCategory() + ".items." + getItem() + ".commands");
        this.messageEnabled = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(getCategory() + ".items." + getItem() + ".message.enabled");
        this.broadcastMessage = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(getCategory() + ".items." + getItem() + ".message.broadcast");
        this.messages = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getStringList(getCategory() + ".items." + getItem() + ".message.text");
        this.cost = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getDouble(getCategory() + ".items." + getItem() + ".cost");
        this.slot = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getInt(getCategory() + ".items." + getItem() + ".slot");
        this.permission = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getString(getCategory() + ".items." + getItem() + ".permission", "");
        this.amount = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getInt(getCategory() + ".items." + getItem() + ".amount");

        if (SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.get(getCategory() + ".items." + getItem() + ".head-database") != null)
            this.headDatabase = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(getCategory() + ".items." + getItem() + ".head-database", false);
        else
            this.headDatabase = false;

        if (SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.get(getCategory() + ".items." + getItem() + ".skulls") != null)
            this.skulls = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(getCategory() + ".items." + getItem() + ".skulls", false);
        else
            this.skulls = false;

        if (SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.get(getCategory() + ".items." + getItem() + ".customData") != null)
            this.customData = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(getCategory() + ".items." + getItem() + ".customData", false);
        else
            this.customData = false;

        this.skullOwner = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getString(getCategory() + ".items." + getItem() + ".skullOwner");

        if (SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.get(getCategory() + ".items." + getItem() + ".glow") != null)
            this.glow = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(getCategory() + ".items." + getItem() + ".glow", false);
        else
            this.glow = false;

        if (SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.get(getCategory() + ".items." + getItem() + ".action.enabled") != null)
            this.action = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(getCategory() + ".items." + getItem() + ".action.enabled", false);
        else
            this.action = false;

        if (SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.get(getCategory() + ".items." + getItem() + ".buyable") != null)
            this.buyable = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(getCategory() + ".items." + getItem() + ".buyable", true);
        else
            this.buyable = true;

        if (SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.get(getCategory() + ".items." + getItem() + ".itemsAdder") != null)
            this.itemsAdder = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(getCategory() + ".items." + getItem() + ".itemsAdder", false);
        else
            this.itemsAdder = false;
    }

    public void sendMessage(Player player) {
        if (!isMessageEnabled())
            return;

        if (isBroadcastMessage()) {
            getMessages().forEach(message -> {
                RyMessageUtils.broadcast(player, message
                        .replace("%item%", getName())
                        .replace("%cost%", Methods.formatDecimal(getCost()))
                        .replace("%price%", String.valueOf(getCost())));
            });
            return;
        }

        getMessages().forEach(message -> {
            RyMessageUtils.sendPlayer(player, message
                    .replace("%item%", getName())
                    .replace("%cost%", Methods.formatDecimal(getCost()))
                    .replace("%price%", String.valueOf(getCost()))
            );
        });
    }

    public void runCommands(Player player) {
        getCommands().forEach(command -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Placeholders.setPlaceholders(player, command
                    .replace("%item%", getName())
                    .replace("%cost%", String.valueOf(getCost()))
                    .replace("%price%", String.valueOf(getCost()))
            ));
        });
    }

    public void runActions(Player player) {
        for (String action : getActions()) {
            SimpleGems.getInstance().getActionManager().execute(player, Placeholders.setPlaceholders(player, action
                    .replace("%item%", getName())
                    .replace("%cost%", String.valueOf(getCost()))
                    .replace("%price%", String.valueOf(getCost()))
            ));
        }
    }

    public ItemStack getItem(Player player) {
        ItemBuilder item = new ItemBuilder(Methods.getMaterial(getMaterial()).parseMaterial(), getAmount());

        if (isHeadDatabase()) {
            HeadDatabaseAPI api = new HeadDatabaseAPI();
            item = new ItemBuilder(api.getItemHead(getSkullOwner()));
        } else if (isSkulls()) {
            Skull api = Skulls.getAPI().getSkull(Integer.parseInt(getSkullOwner()));
            item = new ItemBuilder(api.getItemStack());
        } else if (isCustomData()) {
            item.setCustomModelData(getCustomModelData());
        } else if (isItemsAdder()) {
            CustomStack api = CustomStack.getInstance(getMaterial());

            if (api != null)
                item = new ItemBuilder(api.getItemStack());
            else
                RyMessageUtils.sendPluginError("&cAn error occurred when trying to set an items adder custom item. Make sure you are typing the correct namespaced id.");
        }

        ItemBuilder finalItem = item;

        if (isGlow()) {
            finalItem.addEnchant(XEnchantment.POWER.getEnchant(), 1);
            finalItem.setItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        finalItem.setName(RyMessageUtils.translate(player, getName()));
        getLore().forEach(line -> finalItem.addLoreLine(RyMessageUtils.translate(player, line
                .replace("%item%", getName())
                .replace("%cost%", String.valueOf(getCost()))
                .replace("%price%", String.valueOf(getCost())))));
        finalItem.setDurability(getDurability());

        if (!isSkulls() && !isHeadDatabase())
            finalItem.setSkullOwner(getSkullOwner());

        if (XReflection.supports(20))
            finalItem.setItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);

        return finalItem.toItemStack();
    }

    /**
     * Handles an item's parameters such as whether to
     * open a category or purchase an item.
     */
    public void handleItem(Player player) {
        StringPlaceholders placeholders = StringPlaceholders.builder()
                .addAll(Placeholders.setPlaceholders(player))
                .add("item", getName())
                .add("cost", String.valueOf(getCost()))
                .add("price", String.valueOf(getCost()))
                .build();

        if (!player.hasPermission(getPermission()) && !getPermission().isEmpty()) {
            player.closeInventory();
            RyMessageUtils.sendPluginMessage(player, "no-permission", placeholders);
            return;
        }

        if (isBuyable()) {
            if (!SimpleGems.getInstance().getGemsAPI().hasGems(player, getCost())) {
                RyMessageUtils.sendPluginMessage(player, "not-enough-gems", placeholders);
                return;
            }

            SimpleGems.getInstance().getGemsAPI().takeGems(player, getCost());
        }

        if (isAction()) {
            runActions(player);
            return;
        }

        runCommands(player);
        sendMessage(player);
    }
}