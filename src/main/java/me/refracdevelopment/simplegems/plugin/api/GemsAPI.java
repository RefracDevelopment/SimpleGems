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
package me.refracdevelopment.simplegems.plugin.api;

import me.refracdevelopment.simplegems.plugin.SimpleGems;
import me.refracdevelopment.simplegems.plugin.api.manager.*;
import me.refracdevelopment.simplegems.plugin.utilities.Logger;

/**
 * Author:  Zachary (Refrac) Baldwin
 * Created: 2021-10-8
 */
public class GemsAPI {

    /**
     * The SimpleGemsAPI allows you to hook into SimpleGems to either modify and grab data
     * or to add new features and events.
     */
    public static SimpleGems plugin = SimpleGems.getInstance();
    public static GemsAPI INSTANCE;
    private final ProfileManager profileManager;
    private final ProfileData profileData;

    public GemsAPI() {
        INSTANCE = this;
        profileManager = new ProfileManager();
        profileData = new ProfileData();
        Logger.NONE.out("&eSimpleGemsAPI has been enabled!");
        Logger.NONE.out("&aWiki & Download: https://refracdevelopment.gitbook.io/simplegems-1/");
    }

    /**
     * @return Is the SimpleGemsAPI enabled and registered?
     */
    public static boolean isRegistered() {
        return INSTANCE != null;
    }

    /**
     * The #getProfileManager method allows you to use settings inside the
     * profile management class.
     *
     * @return player's profile.
     */
    public ProfileManager getProfileManager() {
        return profileManager;
    }

    /**
     * The #getProfileData method allows you to get a player's profile data.
     *
     * @return player's profile data.
     */
    public ProfileData getProfileData() {
        return profileData;
    }
}