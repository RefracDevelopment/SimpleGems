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
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.refracdevelopment.simplegems.plugin.utilities.Methods;
import me.refracdevelopment.simplegems.plugin.utilities.chat.Color;
import me.refracdevelopment.simplegems.plugin.utilities.ItemBuilder;
import me.refracdevelopment.simplegems.plugin.utilities.chat.Placeholders;
import me.refracdevelopment.simplegems.plugin.utilities.files.Menus;
import me.refracdevelopment.simplegems.plugin.utilities.files.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Author:  Zachary (Refrac) Baldwin
 * Created: 2021-10-8
 */
public class GemShopItem {
    private final Material material;
    private final String skullOwner;
    private final boolean skulls, headDatabase;
    private final int data;
    private final String name;
    private final List<String> lore;
    private final List<String> commands;
    private final boolean messageEnabled;
    private final boolean broadcastMessage;
    private final List<String> messages;
    private final double cost;
    private final int slot;

    public GemShopItem(String item) {
        this.material = Material.getMaterial(Objects.requireNonNull(Menus.GEM_SHOP_ITEMS.getString(item + ".material")));
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
        this.skullOwner = Menus.GEM_SHOP_ITEMS.getString(item + ".skullOwner");
        this.data = Menus.GEM_SHOP_ITEMS.getInt(item + ".data");
        this.name = Menus.GEM_SHOP_ITEMS.getString(item + ".name");
        this.lore = Menus.GEM_SHOP_ITEMS.getStringList(item + ".lore");
        this.commands = Menus.GEM_SHOP_ITEMS.getStringList(item + ".commands");
        this.messageEnabled = Menus.GEM_SHOP_ITEMS.getBoolean(item + ".message.enabled");
        this.broadcastMessage = Menus.GEM_SHOP_ITEMS.getBoolean(item + ".message.broadcast");
        this.messages = Menus.GEM_SHOP_ITEMS.getStringList(item + ".message.text");
        this.cost = Menus.GEM_SHOP_ITEMS.getDouble(item + ".cost");
        this.slot = Menus.GEM_SHOP_ITEMS.getInt(item + ".slot");
    }

    public String getName() {
        return this.name;
    }

    public int getSlot() {
        return this.slot;
    }

    public double getCost() {
        return this.cost;
    }

    public void sendMessage(Player player) {
        if (!this.messageEnabled) return;
        if (this.broadcastMessage) {
            for (Player p : Bukkit.getOnlinePlayers()) {
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
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Placeholders.setPlaceholders(player, command));
    }

    public ItemStack getItem(Player player) {
        if (headDatabase) {
            HeadDatabaseAPI api = new HeadDatabaseAPI();
            ItemBuilder item = new ItemBuilder(api.getItemHead(this.skullOwner));

            item.setName(Color.translate(player, this.name));
            for (String s : this.lore)
                item.addLoreLine(Color.translate(player, s.replace("%cost%", String.valueOf(this.cost))));

            return item.toItemStack();
        } else if (skulls) {
            Skull api = Skulls.getAPI().getSkull(Integer.parseInt(this.skullOwner));
            ItemBuilder item = new ItemBuilder(api.getItemStack());

            item.setName(Color.translate(player, this.name));
            for (String s : this.lore)
                item.addLoreLine(Color.translate(player, s.replace("%cost%", String.valueOf(this.cost))));

            return item.toItemStack();
        } else {
            ItemBuilder item = new ItemBuilder(this.material);

            item.setName(Color.translate(player, this.name));
            for (String s : this.lore)
                item.addLoreLine(Color.translate(player, s.replace("%item%", this.name).replace("%cost%", String.valueOf(this.cost))));

            item.setDurability(this.data);
            item.setSkullOwner(this.skullOwner);

            return item.toItemStack();
        }
    }

    public void handlePurchase(Player player) {
        if (Methods.hasGems(player, this.cost)) {
            Methods.takeGems(player, this.cost);
            this.runCommands(player);
            this.sendMessage(player);
        } else Color.sendMessage(player, Messages.NOT_ENOUGH_GEMS.replace("%item%", this.name)
                            .replace("%cost%", Methods.formatDec(this.cost)), true, true);
    }
}