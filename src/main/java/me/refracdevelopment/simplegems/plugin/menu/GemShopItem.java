/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2022 RefracDevelopment
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package me.refracdevelopment.simplegems.plugin.menu;

import ca.tweetzy.skulls.Skulls;
import ca.tweetzy.skulls.api.interfaces.Skull;
import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;
import lombok.Setter;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.refracdevelopment.simplegems.plugin.SimpleGems;
import me.refracdevelopment.simplegems.plugin.manager.Profile;
import me.refracdevelopment.simplegems.plugin.utilities.Methods;
import me.refracdevelopment.simplegems.plugin.utilities.VersionCheck;
import me.refracdevelopment.simplegems.plugin.utilities.chat.Color;
import me.refracdevelopment.simplegems.plugin.utilities.ItemBuilder;
import me.refracdevelopment.simplegems.plugin.utilities.chat.Placeholders;
import me.refracdevelopment.simplegems.plugin.utilities.files.Menus;
import me.refracdevelopment.simplegems.plugin.utilities.files.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * Author:  Zachary (Refrac) Baldwin
 * Created: 2021-10-8
 */
@Getter
@Setter
public class GemShopItem {

    private final XMaterial material;
    private final String skullOwner, name;
    private final boolean skulls, headDatabase, messageEnabled, broadcastMessage, customData, glow;
    private final int data, slot, customModelData;
    private final List<String> lore, commands, messages;
    private final double cost;

    public GemShopItem(String item) {
        this.material = XMaterial.matchXMaterial(Material.getMaterial(Menus.GEM_SHOP_ITEMS.getString(item + ".material")));
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
        this.cost = Menus.GEM_SHOP_ITEMS.getDouble(item + ".cost");
        this.slot = Menus.GEM_SHOP_ITEMS.getInt(item + ".slot");
    }

    public void sendMessage(Player player) {
        if (!this.messageEnabled) return;
        if (this.broadcastMessage) {
            for (Player p : SimpleGems.getInstance().getServer().getOnlinePlayers()) {
                for (String message : this.messages)
                    Color.sendMessage(p, message.replace("%item%", this.name).replace("%cost%", Methods.formatDec(this.cost)), true, true);
            }
        } else {
            for (String message : this.messages)
                Color.sendMessage(player, message.replace("%item%", this.name).replace("%cost%", Methods.formatDec(this.cost)), true, true);
        }
    }

    public void runCommands(Player player) {
        for (String command : this.commands)
            SimpleGems.getInstance().getServer().dispatchCommand(SimpleGems.getInstance().getServer().getConsoleSender(), Placeholders.setPlaceholders(player, command));
    }

    public ItemStack getItem(Player player) {
        if (headDatabase) {
            HeadDatabaseAPI api = new HeadDatabaseAPI();
            ItemBuilder item = new ItemBuilder(api.getItemHead(this.skullOwner));

            if (glow) {
                if (Bukkit.getVersion().contains("1.7")) {
                    item.addEnchant(Enchantment.ARROW_DAMAGE, 1);
                } else if (VersionCheck.isOnePointNinePlus()) {
                    item.addEnchant(SimpleGems.getInstance().getGlow(), 1);
                } else {
                    item.addEnchant(Enchantment.ARROW_DAMAGE, 1);
                    ItemMeta itemMeta = item.toItemStack().getItemMeta();
                    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    item.toItemStack().setItemMeta(itemMeta);
                }
            }

            item.setName(Color.translate(player, this.name));
            for (String s : this.lore)
                item.addLoreLine(Color.translate(player, s.replace("%cost%", String.valueOf(this.cost))));

            return item.toItemStack();
        } else if (skulls) {
            Skull api = Skulls.getAPI().getSkull(Integer.parseInt(this.skullOwner));
            ItemBuilder item = new ItemBuilder(api.getItemStack());

            if (glow) {
                if (Bukkit.getVersion().contains("1.7")) {
                    item.addEnchant(Enchantment.ARROW_DAMAGE, 1);
                } else if (VersionCheck.isOnePointNinePlus()) {
                    item.addEnchant(SimpleGems.getInstance().getGlow(), 1);
                } else {
                    item.addEnchant(Enchantment.ARROW_DAMAGE, 1);
                    ItemMeta itemMeta = item.toItemStack().getItemMeta();
                    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    item.toItemStack().setItemMeta(itemMeta);
                }
            }

            item.setName(Color.translate(player, this.name));
            for (String s : this.lore)
                item.addLoreLine(Color.translate(player, s.replace("%cost%", String.valueOf(this.cost))));

            return item.toItemStack();
        } else if (customData) {
            ItemBuilder item = new ItemBuilder(this.material.parseMaterial());

            if (glow) {
                if (Bukkit.getVersion().contains("1.7")) {
                    item.addEnchant(Enchantment.ARROW_DAMAGE, 1);
                } else if (VersionCheck.isOnePointNinePlus()) {
                    item.addEnchant(SimpleGems.getInstance().getGlow(), 1);
                } else {
                    item.addEnchant(Enchantment.ARROW_DAMAGE, 1);
                    ItemMeta itemMeta = item.toItemStack().getItemMeta();
                    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    item.toItemStack().setItemMeta(itemMeta);
                }
            }

            if (VersionCheck.isOnePointFourteenPlus()) {
                item.setCustomModelData(this.customModelData);
            } else {
                Color.log("&cAn error occurred when trying to set custom model data. Make sure your only using custom model data when on 1.14+.");
            }
            item.setName(this.name);
            for (String s : this.lore)
                item.addLoreLine(Color.translate(player, s.replace("%cost%", String.valueOf(this.cost))));
            item.setDurability(this.data);
            item.setSkullOwner(this.skullOwner);

            return item.toItemStack();
        } else {
            ItemBuilder item = new ItemBuilder(this.material.parseMaterial());

            if (glow) {
                if (Bukkit.getVersion().contains("1.7")) {
                    item.addEnchant(Enchantment.ARROW_DAMAGE, 1);
                } else if (VersionCheck.isOnePointNinePlus()) {
                    item.addEnchant(SimpleGems.getInstance().getGlow(), 1);
                } else {
                    item.addEnchant(Enchantment.ARROW_DAMAGE, 1);
                    ItemMeta itemMeta = item.toItemStack().getItemMeta();
                    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    item.toItemStack().setItemMeta(itemMeta);
                }
            }

            item.setName(Color.translate(player, this.name));
            for (String s : this.lore)
                item.addLoreLine(Color.translate(player, s.replace("%item%", this.name).replace("%cost%", String.valueOf(this.cost))));

            item.setDurability(this.data);
            item.setSkullOwner(this.skullOwner);

            return item.toItemStack();
        }
    }

    public void handlePurchase(Player player) {
        Profile profile = SimpleGems.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (profile.getData().getGems().hasStat(this.cost)) {
            profile.getData().getGems().decrementStat(this.cost);
            this.runCommands(player);
            this.sendMessage(player);
        } else Color.sendMessage(player, Messages.NOT_ENOUGH_GEMS.replace("%item%", this.name)
                            .replace("%cost%", Methods.formatDec(this.cost)), true, true);
    }
}