package me.refracdevelopment.simplegems.menu;

import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.interfaces.Skull;
import com.cryptomorin.xseries.XMaterial;
import dev.rosewood.rosegarden.utils.NMSUtil;
import lombok.Getter;
import lombok.Setter;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.data.Profile;
import me.refracdevelopment.simplegems.manager.LocaleManager;
import me.refracdevelopment.simplegems.utilities.ItemBuilder;
import me.refracdevelopment.simplegems.utilities.Methods;
import me.refracdevelopment.simplegems.utilities.Utilities;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import me.refracdevelopment.simplegems.utilities.chat.Placeholders;
import me.refracdevelopment.simplegems.utilities.files.Menus;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

@Getter
@Setter
public class GemShopItem {

    private final XMaterial material;
    private final String skullOwner, name;
    private final boolean skulls, headDatabase, messageEnabled, broadcastMessage, customData, glow;
    private final int data, slot, customModelData;
    private final List<String> lore, commands, messages;
    private final long cost;

    public GemShopItem(String item) {
        this.material = Utilities.getMaterial(Menus.GEM_SHOP_ITEMS.getString(item + ".material"));
        if (Menus.GEM_SHOP_ITEMS.getBoolean(item + ".head-database")) {
            this.headDatabase = Menus.GEM_SHOP_ITEMS.getBoolean(item + ".head-database", false);
        } else {
            this.headDatabase = false;
        }
        if (Menus.GEM_SHOP_ITEMS.getBoolean(item + ".skulls")) {
            this.skulls = Menus.GEM_SHOP_ITEMS.getBoolean(item + ".skulls", false);
        } else {
            this.skulls = false;
        }
        if (Menus.GEM_SHOP_ITEMS.contains(item + ".customData")) {
            this.customData = Menus.GEM_SHOP_ITEMS.getBoolean(item + ".customData", false);
        } else {
            this.customData = false;
        }
        this.skullOwner = Menus.GEM_SHOP_ITEMS.getString(item + ".skullOwner");
        this.data = Menus.GEM_SHOP_ITEMS.getInt(item + ".data");
        this.customModelData = Menus.GEM_SHOP_ITEMS.getInt(item + ".customModelData");
        this.name = Menus.GEM_SHOP_ITEMS.getString(item + ".name");
        this.lore = Menus.GEM_SHOP_ITEMS.getStringList(item + ".lore");
        this.commands = Menus.GEM_SHOP_ITEMS.getStringList(item + ".commands");
        this.messageEnabled = Menus.GEM_SHOP_ITEMS.getBoolean(item + ".message.enabled");
        this.broadcastMessage = Menus.GEM_SHOP_ITEMS.getBoolean(item + ".message.broadcast");
        this.messages = Menus.GEM_SHOP_ITEMS.getStringList(item + ".message.text");
        if (Menus.GEM_SHOP_ITEMS.contains(item + ".glow")) {
            this.glow = Menus.GEM_SHOP_ITEMS.getBoolean(item + ".glow");
        } else {
            this.glow = false;
        }
        this.cost = Menus.GEM_SHOP_ITEMS.getLong(item + ".cost");
        this.slot = Menus.GEM_SHOP_ITEMS.getInt(item + ".slot");
    }

    public void sendMessage(Player player) {
        if (!this.messageEnabled) return;

        final LocaleManager locale = SimpleGems.getInstance().getManager(LocaleManager.class);

        if (this.broadcastMessage) {
            Bukkit.getOnlinePlayers().forEach(p -> {
                this.messages.forEach(message -> {
                    locale.sendCustomMessage(p, Placeholders.setPlaceholders(player, message.replace("%item%", this.name).replace("%cost%", Methods.formatDec(this.cost))));
                });
            });
        } else {
            this.messages.forEach(message -> {
                locale.sendCustomMessage(player, Placeholders.setPlaceholders(player, message.replace("%item%", this.name).replace("%cost%", Methods.formatDec(this.cost))));
            });
        }
    }

    public void runCommands(Player player) {
        this.commands.forEach(command -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Placeholders.setPlaceholders(player, command));
        });
    }

    public ItemStack getItem(Player player) {
        if (headDatabase) {
            HeadDatabaseAPI api = new HeadDatabaseAPI();
            ItemBuilder item = new ItemBuilder(api.getItemHead(this.skullOwner));

            if (this.glow) {
                item.addEnchant(Enchantment.ARROW_DAMAGE, 1);
                ItemMeta itemMeta = item.toItemStack().getItemMeta();
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                item.toItemStack().setItemMeta(itemMeta);
            }

            item.setName(Color.translate(player, this.name));
            this.lore.forEach(s -> item.addLoreLine(Color.translate(player, s.replace("%item%", this.name).replace("%cost%", String.valueOf(this.cost)))));

            return item.toItemStack();
        } else if (skulls) {
            Skull api = Skulls.getAPI().getSkull(Integer.parseInt(this.skullOwner));
            ItemBuilder item = new ItemBuilder(api.getItemStack());

            if (this.glow) {
                item.addEnchant(Enchantment.ARROW_DAMAGE, 1);
                ItemMeta itemMeta = item.toItemStack().getItemMeta();
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                item.toItemStack().setItemMeta(itemMeta);
            }

            item.setName(Color.translate(player, this.name));
            this.lore.forEach(s -> item.addLoreLine(Color.translate(player, s.replace("%item%", this.name).replace("%cost%", String.valueOf(this.cost)))));

            return item.toItemStack();
        } else if (customData) {
            ItemBuilder item = new ItemBuilder(this.material.parseMaterial());

            if (this.glow) {
                item.addEnchant(Enchantment.ARROW_DAMAGE, 1);
                ItemMeta itemMeta = item.toItemStack().getItemMeta();
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                item.toItemStack().setItemMeta(itemMeta);
            }

            if (NMSUtil.getVersionNumber() > 13) {
                item.setCustomModelData(this.customModelData);
            } else {
                Color.log("&cAn error occurred when trying to set custom model data. Make sure your only using custom model data when on 1.14+.");
            }
            item.setName(this.name);
            this.lore.forEach(s -> item.addLoreLine(Color.translate(player, s.replace("%item%", this.name).replace("%cost%", String.valueOf(this.cost)))));
            item.setDurability(this.data);
            item.setSkullOwner(this.skullOwner);

            return item.toItemStack();
        } else {
            ItemBuilder item = new ItemBuilder(this.material.parseMaterial());

            if (this.glow) {
                item.addEnchant(Enchantment.ARROW_DAMAGE, 1);
                ItemMeta itemMeta = item.toItemStack().getItemMeta();
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                item.toItemStack().setItemMeta(itemMeta);
            }

            item.setName(Color.translate(player, this.name));
            this.lore.forEach(s -> item.addLoreLine(Color.translate(player, s.replace("%item%", this.name).replace("%cost%", String.valueOf(this.cost)))));
            item.setDurability(this.data);
            item.setSkullOwner(this.skullOwner);

            return item.toItemStack();
        }
    }

    public void handlePurchase(Player player) {
        Profile profile = SimpleGems.getInstance().getProfileManager().getProfile(player.getUniqueId());
        final LocaleManager locale = SimpleGems.getInstance().getManager(LocaleManager.class);

        if (profile.getData().getGems().hasAmount(this.cost)) {
            profile.getData().getGems().decrementAmount(this.cost);
            this.runCommands(player);
            this.sendMessage(player);
        } else locale.sendMessage(player, "not-enough-gems", Placeholders.setPlaceholders(player));
    }
}