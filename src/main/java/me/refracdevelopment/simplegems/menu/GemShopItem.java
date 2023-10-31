package me.refracdevelopment.simplegems.menu;

import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.interfaces.Skull;
import com.cryptomorin.xseries.ReflectionUtils;
import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;
import lombok.Setter;
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
@Setter
public class GemShopItem {

    private final XMaterial material;
    private final String skullOwner, name, categoryName;
    private final boolean skulls, headDatabase, messageEnabled, broadcastMessage, customData, glow, action;
    private final int data, slot, customModelData;
    private final List<String> lore, actions, commands, messages;
    private final long cost;
    private final GemShopCategory category;

    public GemShopItem(GemShopCategory category, String item) {
        this.category = category;
        this.categoryName = category.getCategoryName();
        this.material = Utilities.getMaterial(SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getString(categoryName + ".items." + item + ".material"));
        if (SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(categoryName + ".items." + item + ".head-database")) {
            this.headDatabase = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(categoryName + ".items." + item + ".head-database", false);
        } else {
            this.headDatabase = false;
        }
        if (SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(categoryName + ".items." + item + ".skulls")) {
            this.skulls = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(categoryName + ".items." + item + ".skulls", false);
        } else {
            this.skulls = false;
        }
        if (SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(categoryName + ".items." + item + ".customData")) {
            this.customData = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(categoryName + ".items." + item + ".customData", false);
        } else {
            this.customData = false;
        }
        this.skullOwner = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getString(categoryName + ".items." + item + ".skullOwner");
        if (SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(categoryName + ".items." + item + ".glow")) {
            this.glow = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(categoryName + ".items." + item + ".glow");
        } else {
            this.glow = false;
        }
        this.data = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getInt(categoryName + ".items." + item + ".data");
        this.customModelData = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getInt(categoryName + ".items." + item + ".customModelData");
        this.name = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getString(categoryName + ".items." + item + ".name");
        this.lore = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getStringList(categoryName + ".items." + item + ".lore");
        if (SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(categoryName + ".items." + item + ".action.enabled")) {
            this.action = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(categoryName + ".items." + item + ".action.enabled", false);
        } else {
            this.action = false;
        }
        this.actions = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getStringList(categoryName + ".items." + item + ".action.actions");
        this.commands = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getStringList(categoryName + ".items." + item + ".commands");
        this.messageEnabled = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(categoryName + ".items." + item + ".message.enabled");
        this.broadcastMessage = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(categoryName + ".items." + item + ".message.broadcast");
        this.messages = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getStringList(categoryName + ".items." + item + ".message.text");
        this.cost = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getLong(categoryName + ".items." + item + ".cost");
        this.slot = SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getInt(categoryName + ".items." + item + ".slot");
    }

    public void sendMessage(Player player) {
        if (!this.messageEnabled) return;

        if (this.broadcastMessage) {
            Bukkit.getOnlinePlayers().forEach(p -> {
                this.messages.forEach(message -> {
                    Color.sendCustomMessage(p, Color.translate(player, message
                            .replace("%item%", Color.translate(this.name))
                            .replace("%cost%", Methods.formatDecimal(this.cost))
                            .replace("%price%", String.valueOf(this.cost))
                    ));
                });
            });
        } else {
            this.messages.forEach(message -> {
                Color.sendCustomMessage(player, Color.translate(player, message
                        .replace("%item%", Color.translate(this.name))
                        .replace("%cost%", Methods.formatDecimal(this.cost))
                        .replace("%price%", String.valueOf(this.cost))
                ));
            });
        }
    }

    public void runCommands(Player player) {
        this.commands.forEach(command -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Placeholders.setPlaceholders(player, command
                    .replace("%item%", Color.translate(this.name))
                    .replace("%cost%", String.valueOf(this.cost))
                    .replace("%price%", String.valueOf(this.cost))
            ));
        });
    }

    public void runActions(Player player, String action) {
        SimpleGems.getInstance().getActionManager().execute(player, Color.translate(player, action
                .replace("%item%", Color.translate(this.name))
                .replace("%cost%", String.valueOf(this.cost))
                .replace("%price%", String.valueOf(this.cost))
        ));
    }

    public ItemStack getItem(Player player) {
        if (headDatabase) {
            HeadDatabaseAPI api = new HeadDatabaseAPI();
            ItemBuilder item = new ItemBuilder(api.getItemHead(this.skullOwner));

            if (this.glow) {
                item.addEnchant(Enchantment.ARROW_DAMAGE, 1);
                item.setItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            item.setName(Color.translate(player, this.name));
            this.lore.forEach(s -> item.addLoreLine(Color.translate(player, s.replace("%item%", this.name)
                    .replace("%cost%", String.valueOf(this.cost))
                    .replace("%price%", String.valueOf(this.cost)))));

            return item.toItemStack();
        } else if (skulls) {
            Skull api = Skulls.getAPI().getSkull(Integer.parseInt(this.skullOwner));
            ItemBuilder item = new ItemBuilder(api.getItemStack());

            if (this.glow) {
                item.addEnchant(Enchantment.ARROW_DAMAGE, 1);
                item.setItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            item.setName(Color.translate(player, this.name));
            this.lore.forEach(s -> item.addLoreLine(Color.translate(player, s.replace("%item%", this.name)
                    .replace("%cost%", String.valueOf(this.cost))
                    .replace("%price%", String.valueOf(this.cost)))));

            return item.toItemStack();
        } else if (customData) {
            ItemBuilder item = new ItemBuilder(this.material.parseMaterial());

            if (this.glow) {
                item.addEnchant(Enchantment.ARROW_DAMAGE, 1);
                item.setItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            if (ReflectionUtils.MINOR_NUMBER >= 14) {
                item.setCustomModelData(this.customModelData);
            } else {
                Color.log("&cAn error occurred when trying to set custom model data. Make sure your only using custom model data when on 1.14+.");
            }
            item.setName(Color.translate(player, this.name));
            this.lore.forEach(s -> item.addLoreLine(Color.translate(player, s.replace("%item%", this.name)
                    .replace("%cost%", String.valueOf(this.cost))
                    .replace("%price%", String.valueOf(this.cost)))));
            item.setDurability(this.data);
            item.setSkullOwner(this.skullOwner);

            return item.toItemStack();
        } else {
            ItemBuilder item = new ItemBuilder(this.material.parseMaterial());

            if (this.glow) {
                item.addEnchant(Enchantment.ARROW_DAMAGE, 1);
                item.setItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            item.setName(Color.translate(player, this.name));
            this.lore.forEach(s -> item.addLoreLine(Color.translate(player, s.replace("%item%", this.name)
                    .replace("%cost%", String.valueOf(this.cost))
                    .replace("%price%", String.valueOf(this.cost)))));
            item.setDurability(this.data);
            item.setSkullOwner(this.skullOwner);

            return item.toItemStack();
        }
    }

    /**
     * Handles an item's parameters such as whether to
     * open a category or purchase an item.
     */
    public void handleItem(Player player) {
        StringPlaceholders placeholders = StringPlaceholders.builder()
                .addAll(Placeholders.setPlaceholders(player))
                .add("item", this.name)
                .build();

        if (action) {
            this.actions.forEach(action -> {
                if (action.startsWith("{openmenu:")) {
                    new GemShopCategory(SimpleGems.getInstance().getMenuManager().getPlayerMenuUtility(player), action
                            .replace("{openmenu:", "")
                            .replace("}", "")).open();
                } else {
                    if (SimpleGems.getInstance().getGemsAPI().hasGems(player, this.cost)) {
                        SimpleGems.getInstance().getGemsAPI().takeGems(player, this.cost);
                        this.runActions(player, action);
                    } else Color.sendMessage(player, "not-enough-gems", placeholders);
                }
            });
        } else {
            if (SimpleGems.getInstance().getGemsAPI().hasGems(player, this.cost)) {
                SimpleGems.getInstance().getGemsAPI().takeGems(player, this.cost);
                this.runCommands(player);
                this.sendMessage(player);
            } else Color.sendMessage(player, "not-enough-gems", placeholders);
        }
    }
}