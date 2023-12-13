package me.refracdevelopment.simplegems.menu;

import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.interfaces.Skull;
import com.cryptomorin.xseries.ReflectionUtils;
import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.utilities.ItemBuilder;
import me.refracdevelopment.simplegems.utilities.Methods;
import me.refracdevelopment.simplegems.utilities.Utilities;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import me.refracdevelopment.simplegems.utilities.chat.Placeholders;
import me.refracdevelopment.simplegems.utilities.chat.StringPlaceholders;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public class GemShopItem {

    private final XMaterial material;
    private final String skullOwner, name, category, item;
    private final boolean skulls, headDatabase, messageEnabled, broadcastMessage, customData, glow, action, buyable;
    private final int data, slot, customModelData;
    private final List<String> lore, actions, commands, messages;
    private final long cost;

    public GemShopItem(String category, String item) {
        this.category = category;
        this.item = item;
        this.material = Utilities.getMaterial(SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getString(getCategory() + ".items." + getItem() + ".material"));
        if (SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(getCategory() + ".items." + getItem() + ".head-database")) {
            this.headDatabase = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(getCategory() + ".items." + getItem() + ".head-database", false);
        } else {
            this.headDatabase = false;
        }
        if (SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(getCategory() + ".items." + getItem() + ".skulls")) {
            this.skulls = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(getCategory() + ".items." + getItem() + ".skulls", false);
        } else {
            this.skulls = false;
        }
        if (SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(getCategory() + ".items." + getItem() + ".customData")) {
            this.customData = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(getCategory() + ".items." + getItem() + ".customData", false);
        } else {
            this.customData = false;
        }
        this.skullOwner = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getString(getCategory() + ".items." + getItem() + ".skullOwner");
        if (SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(getCategory() + ".items." + getItem() + ".glow")) {
            this.glow = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(getCategory() + ".items." + getItem() + ".glow");
        } else {
            this.glow = false;
        }
        this.data = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getInt(getCategory() + ".items." + getItem() + ".data");
        this.customModelData = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getInt(getCategory() + ".items." + getItem() + ".customModelData");
        this.name = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getString(getCategory() + ".items." + getItem() + ".name");
        this.lore = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getStringList(getCategory() + ".items." + getItem() + ".lore");
        if (SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(getCategory() + ".items." + getItem() + ".action.enabled")) {
            this.action = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(getCategory() + ".items." + getItem() + ".action.enabled", false);
        } else {
            this.action = false;
        }
        this.actions = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getStringList(getCategory() + ".items." + getItem() + ".action.actions");
        this.commands = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getStringList(getCategory() + ".items." + getItem() + ".commands");
        this.messageEnabled = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(getCategory() + ".items." + getItem() + ".message.enabled");
        this.broadcastMessage = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(getCategory() + ".items." + getItem() + ".message.broadcast");
        this.messages = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getStringList(getCategory() + ".items." + getItem() + ".message.text");
        this.cost = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getLong(getCategory() + ".items." + getItem() + ".cost");
        this.slot = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getInt(getCategory() + ".items." + getItem() + ".slot");
        if (SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(getCategory() + ".items." + getItem() + ".buyable")) {
            this.buyable = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(getCategory() + ".items." + getItem() + ".buyable");
        } else {
            this.buyable = true;
        }
    }

    public void sendMessage(Player player) {
        if (!isMessageEnabled()) return;

        if (isBroadcastMessage()) {
            Bukkit.getOnlinePlayers().forEach(p -> {
                getMessages().forEach(message -> {
                    Color.sendCustomMessage(p, Color.translate(player, message
                            .replace("%item%", Color.translate(getName()))
                            .replace("%cost%", Methods.formatDecimal(getCost()))
                            .replace("%price%", String.valueOf(getCost()))
                    ));
                });
            });
            return;
        }

        getMessages().forEach(message -> {
            Color.sendCustomMessage(player, Color.translate(player, message
                    .replace("%item%", Color.translate(getName()))
                    .replace("%cost%", Methods.formatDecimal(getCost()))
                    .replace("%price%", String.valueOf(getCost()))
            ));
        });
    }

    public void runCommands(Player player) {
        getCommands().forEach(command -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Placeholders.setPlaceholders(player, command
                    .replace("%item%", Color.translate(getName()))
                    .replace("%cost%", String.valueOf(getCost()))
                    .replace("%price%", String.valueOf(getCost()))
            ));
        });
    }

    public void runActions(Player player) {
        for (String action : getActions()) {
            if (action.startsWith("{openmenu:")) {
                SimpleGems.getInstance().getGemShop().getCategories().forEach((gemShopCategory, gemShopItems) -> {
                    String menu = action.replace("{openmenu:", "").replace("}", "");

                    if (!gemShopCategory.equalsIgnoreCase(menu)) return;

                    GemShopCategory category = new GemShopCategory(SimpleGems.getInstance().getMenuManager().getPlayerMenuUtility(player), gemShopCategory);

                    if (!category.isEnabled()) {
                        player.closeInventory();
                        Color.sendMessage(player, "shop-disabled");
                        return;
                    }

                    category.open();
                });
            }

            SimpleGems.getInstance().getActionManager().execute(player, Color.translate(player, action
                    .replace("%item%", Color.translate(getName()))
                    .replace("%cost%", String.valueOf(getCost()))
                    .replace("%price%", String.valueOf(getCost()))
            ));
        }
    }

    public ItemStack getItem(Player player) {
        ItemBuilder item = new ItemBuilder(getMaterial().parseMaterial());

        if (isHeadDatabase()) {
            HeadDatabaseAPI api = new HeadDatabaseAPI();
            item = new ItemBuilder(api.getItemHead(getSkullOwner()));
        } else if (isSkulls()) {
            Skull api = Skulls.getAPI().getSkull(Integer.parseInt(getSkullOwner()));
            item = new ItemBuilder(api.getItemStack());
        } else if (isCustomData()) {
            if (ReflectionUtils.MINOR_NUMBER >= 14) {
                item.setCustomModelData(getCustomModelData());
            } else {
                Color.log("&cAn error occurred when trying to set custom model data. Make sure your only using custom model data when on 1.14+.");
            }
        }

        ItemBuilder finalItem = item;

        if (isGlow()) {
            finalItem.addEnchant(Enchantment.ARROW_DAMAGE, 1);
            finalItem.setItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        finalItem.setName(getName());
        getLore().forEach(s -> finalItem.addLoreLine(Color.translate(player, s
                .replace("%item%", getName())
                .replace("%cost%", String.valueOf(getCost()))
                .replace("%price%", String.valueOf(getCost())))));

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

        if (isAction()) {
            if (isBuyable()) {
                if (!SimpleGems.getInstance().getGemsAPI().hasGems(player, getCost())) {
                    Color.sendMessage(player, "not-enough-gems", placeholders);
                    return;
                }

                SimpleGems.getInstance().getGemsAPI().takeGems(player, getCost());
            }

            runActions(player);
            return;
        }

        if (!isBuyable()) {
            runCommands(player);
            sendMessage(player);
            return;
        }

        if (!SimpleGems.getInstance().getGemsAPI().hasGems(player, getCost())) {
            Color.sendMessage(player, "not-enough-gems", placeholders);
            return;
        }

        SimpleGems.getInstance().getGemsAPI().takeGems(player, getCost());
        runCommands(player);
        sendMessage(player);
    }
}