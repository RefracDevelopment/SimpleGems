package me.refracdevelopment.simplegems.menu;

import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.interfaces.Skull;
import com.cryptomorin.xseries.XMaterial;
import dev.rosewood.rosegarden.utils.NMSUtil;
import lombok.Getter;
import lombok.Setter;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.api.SimpleGemsAPI;
import me.refracdevelopment.simplegems.manager.configuration.LocaleManager;
import me.refracdevelopment.simplegems.manager.configuration.cache.Menus;
import me.refracdevelopment.simplegems.utilities.ItemBuilder;
import me.refracdevelopment.simplegems.utilities.Methods;
import me.refracdevelopment.simplegems.utilities.Utilities;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import me.refracdevelopment.simplegems.utilities.chat.Placeholders;
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
        this.material = Utilities.getMaterial(Menus.GEM_SHOP_CATEGORIES.getString(categoryName + ".items." + item + ".material"));
        if (Menus.GEM_SHOP_CATEGORIES.getBoolean(categoryName + ".items." + item + ".head-database")) {
            this.headDatabase = Menus.GEM_SHOP_CATEGORIES.getBoolean(categoryName + ".items." + item + ".head-database", false);
        } else {
            this.headDatabase = false;
        }
        if (Menus.GEM_SHOP_CATEGORIES.getBoolean(categoryName + ".items." + item + ".skulls")) {
            this.skulls = Menus.GEM_SHOP_CATEGORIES.getBoolean(categoryName  + ".items." + item + ".skulls", false);
        } else {
            this.skulls = false;
        }
        if (Menus.GEM_SHOP_CATEGORIES.getBoolean(categoryName + ".items." + item + ".customData")) {
            this.customData = Menus.GEM_SHOP_CATEGORIES.getBoolean(categoryName + ".items." + item + ".customData", false);
        } else {
            this.customData = false;
        }
        this.skullOwner = Menus.GEM_SHOP_CATEGORIES.getString(categoryName + ".items." + item + ".skullOwner");
        if (Menus.GEM_SHOP_CATEGORIES.getBoolean(categoryName + ".items." + item + ".glow")) {
            this.glow = Menus.GEM_SHOP_CATEGORIES.getBoolean(categoryName + ".items." + item + ".glow");
        } else {
            this.glow = false;
        }
        this.data = Menus.GEM_SHOP_CATEGORIES.getInt(categoryName + ".items." + item + ".data");
        this.customModelData = Menus.GEM_SHOP_CATEGORIES.getInt(categoryName + ".items." + item + ".customModelData");
        this.name = Menus.GEM_SHOP_CATEGORIES.getString(categoryName + ".items." + item + ".name");
        this.lore = Menus.GEM_SHOP_CATEGORIES.getStringList(categoryName + ".items." + item + ".lore");
        if (Menus.GEM_SHOP_CATEGORIES.getBoolean(categoryName + ".items." + item + ".action.enabled")) {
            this.action = Menus.GEM_SHOP_CATEGORIES.getBoolean(categoryName + ".items." + item + ".action.enabled", false);
        } else {
            this.action = false;
        }
        this.actions = Menus.GEM_SHOP_CATEGORIES.getStringList(categoryName + ".items." + item + ".action.actions");
        this.commands = Menus.GEM_SHOP_CATEGORIES.getStringList(categoryName + ".items." + item + ".commands");
        this.messageEnabled = Menus.GEM_SHOP_CATEGORIES.getBoolean(categoryName + ".items." + item + ".message.enabled");
        this.broadcastMessage = Menus.GEM_SHOP_CATEGORIES.getBoolean(categoryName + ".items." + item + ".message.broadcast");
        this.messages = Menus.GEM_SHOP_CATEGORIES.getStringList(categoryName + ".items." + item + ".message.text");
        this.cost = Menus.GEM_SHOP_CATEGORIES.getLong(categoryName + ".items." + item + ".cost");
        this.slot = Menus.GEM_SHOP_CATEGORIES.getInt(categoryName + ".items." + item + ".slot");
    }

    public void sendMessage(Player player) {
        if (!this.messageEnabled) return;

        final LocaleManager locale = SimpleGems.getInstance().getManager(LocaleManager.class);

        if (this.broadcastMessage) {
            Bukkit.getOnlinePlayers().forEach(p -> {
                this.messages.forEach(message -> {
                    locale.sendCustomMessage(p, Color.translate(player, message
                            .replace("%item%", Color.translate(this.name))
                            .replace("%cost%", Methods.formatDecimal(this.cost))
                            .replace("%price%", String.valueOf(this.cost))
                    ));
                });
            });
        } else {
            this.messages.forEach(message -> {
                locale.sendCustomMessage(player, Color.translate(player, message
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

    public void runActions(Player player) {
        this.actions.forEach(action -> {
            SimpleGems.getInstance().getActionManager().execute(player, Placeholders.setPlaceholders(player, action
                    .replace("%item%", Color.translate(this.name))
                    .replace("%cost%", String.valueOf(this.cost))
                    .replace("%price%", String.valueOf(this.cost))
            ));
        });
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

            if (NMSUtil.getVersionNumber() >= 14) {
                item.setCustomModelData(this.customModelData);
            } else {
                Color.log("&cAn error occurred when trying to set custom model data. Make sure your only using custom model data when on 1.14+.");
            }
            item.setName(this.name);
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

    public void handlePurchase(Player player) {
        final LocaleManager locale = SimpleGems.getInstance().getManager(LocaleManager.class);

        if (SimpleGemsAPI.INSTANCE.hasGems(player, this.cost)) {
            SimpleGemsAPI.INSTANCE.takeGems(player, this.cost);
            if (action) {
                this.runActions(player);
            } else {
                this.runCommands(player);
            }
            this.sendMessage(player);
        } else locale.sendMessage(player, "not-enough-gems", Placeholders.setPlaceholders(player));
    }
}