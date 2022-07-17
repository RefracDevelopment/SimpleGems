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
package me.refracdevelopment.simplegems.plugin.utilities;

import com.cryptomorin.xseries.XMaterial;
import me.refracdevelopment.simplegems.plugin.utilities.chat.Color;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ItemBuilder {

    private ItemStack is;

    public ItemBuilder(Material m) {
        this(m, 1);
    }

    public ItemBuilder(ItemStack is) {
        this.is = is;
    }

    public ItemBuilder(Material m, int amount) {
        if (Bukkit.getVersion().contains("1.7")) {
            is = new ItemStack(m, amount);
        } else {
            this.is = new ItemStack(XMaterial.matchXMaterial(m).parseMaterial(), amount);
        }
    }

    public ItemBuilder(Material m, int amount, byte durability) {
        if (Bukkit.getVersion().contains("1.7")) {
            is = new ItemStack(m, amount, durability);
        } else {
            this.is = new ItemStack(XMaterial.matchXMaterial(m).parseMaterial(), amount, (short) durability);
        }
    }

    public ItemBuilder clone() {
        return new ItemBuilder(is);
    }

    public ItemBuilder setDurability(short dur) {
        is.setDurability(dur);
        return this;
    }

    public ItemBuilder setDurability(int dur) {
        is.setDurability((short) dur);
        return this;
    }

    public ItemBuilder setName(String name) {
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(Color.translate(name));
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setUnColoredName(String name) {
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder addUnsafeEnchantment(Enchantment ench, int level) {
        if (level < 1) {
            return this;
        }
        is.addUnsafeEnchantment(ench, level);
        return this;
    }

    public ItemBuilder removeEnchantment(Enchantment ench) {
        is.removeEnchantment(ench);
        return this;
    }

    public ItemBuilder setSkullOwner(String owner) {
        try {
            SkullMeta im = (SkullMeta) is.getItemMeta();
            im.setOwner(owner);
            is.setItemMeta(im);
        } catch (ClassCastException expected) {
        }
        return this;
    }

    public ItemBuilder addEnchant(Enchantment ench, int level) {
        if (level < 1) {
            return this;
        }
        ItemMeta im = is.getItemMeta();
        im.addEnchant(ench, level, true);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment ench, int level) {
        if (level < 1) {
            return this;
        }
        is.addEnchantment(ench, level);
        return this;
    }

    public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments) {
        is.addEnchantments(enchantments);
        return this;
    }

/*    public ItemBuilder setOwner(String owner) {
        if (this.is.getType() == Material.SKULL_ITEM) {
            SkullMeta meta = (SkullMeta) this.is.getItemMeta();
            meta.setOwner(owner);
            this.is.setItemMeta(meta);
            return this;
        }
        throw new IllegalArgumentException("setOwner() only applicable for Skull Item");
    }*/

    public ItemBuilder setInfinityDurability() {
        is.setDurability(Short.MAX_VALUE);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        ItemMeta im = is.getItemMeta();
        im.setLore(Color.translate(Arrays.asList(lore)));
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        ItemMeta im = is.getItemMeta();
        im.setLore(Color.translate(lore));
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder removeLoreLine(String line) {
        ItemMeta im = is.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        if (!lore.contains(line))
            return this;
        lore.remove(line);
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder removeLoreLine(int index) {
        ItemMeta im = is.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        if (index < 0 || index > lore.size())
            return this;
        lore.remove(index);
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder addLoreLine(String line) {
        ItemMeta im = is.getItemMeta();
        List<String> lore = new ArrayList<>();
        if (im.hasLore())
            lore = new ArrayList<>(im.getLore());
        lore.add(Color.translate(line));
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder addUnTranslatedLoreLine(String line) {
        ItemMeta im = is.getItemMeta();
        List<String> lore = new ArrayList<>();
        if (im.hasLore())
            lore = new ArrayList<>(im.getLore());
        lore.add(ChatColor.translateAlternateColorCodes('&', line));
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder addLoreLine(String line, int pos) {
        ItemMeta im = is.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        lore.set(pos, line);
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    @SuppressWarnings("deprecation")
    public ItemBuilder setDyeColor(DyeColor color) {
        this.is.setDurability(color.getDyeData());
        return this;
    }

/*    @Deprecated
    public ItemBuilder setWoolColor(DyeColor color) {
        if (!is.getType().equals(Material.WOOL))
            return this;
        this.is.setDurability(color.getData());
        return this;
    }*/

    public ItemStack toItemStack() {
        return is;
    }
}