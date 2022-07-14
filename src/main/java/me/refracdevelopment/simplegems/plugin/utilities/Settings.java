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

import me.refracdevelopment.simplegems.plugin.SimpleGems;
import me.refracdevelopment.simplegems.plugin.utilities.chat.Color;
import org.bukkit.entity.Player;

/**
 * Author:  Zachary (Refrac) Baldwin
 * Created: 2021-10-1
 */
public class Settings {

    public static String getName = "SimpleGems";
    public static String getDeveloper = "Refrac";
    public static String getDevUUID = "d9c670ed-d7d5-45fb-a144-8b8be86c4a2d";
    public static String getDevUUID2 = "ab898e40-9088-45eb-9d69-e0b78e872627";
    public static String getVersion = "2.0";

    public static void devMessage(Player player) {
        player.sendMessage(" ");
        Color.sendMessage(player, "&aWelcome " + getDeveloper + " Developer!", true, true);
        Color.sendMessage(player, "&aThis server is currently running " + getName + " &bv" + getVersion + "&a.", true, true);
        Color.sendMessage(player, "&aPlugin name&7: &f" + getName, true, true);
        player.sendMessage(" ");
        Color.sendMessage(player, "&aServer version&7: &f" + SimpleGems.getInstance().getServer().getVersion(), true, true);
        player.sendMessage(" ");
    }

}