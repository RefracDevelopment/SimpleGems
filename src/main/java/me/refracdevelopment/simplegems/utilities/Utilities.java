package me.refracdevelopment.simplegems.utilities;

import com.cryptomorin.xseries.XMaterial;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.manager.configuration.LocaleManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Utilities {
    public static final UUID getDevUUID = UUID.fromString("d9c670ed-d7d5-45fb-a144-8b8be86c4a2d");
    public static final UUID getDevUUID2 = UUID.fromString("ab898e40-9088-45eb-9d69-e0b78e872627");

    public static XMaterial getMaterial(String source) {
        XMaterial material;
        try {
            material = XMaterial.matchXMaterial(source).get();
        } catch (Exception e) {
            material = XMaterial.REDSTONE_BLOCK;
        }
        return material;
    }

    public static void sendDevMessage(Player player) {
        final LocaleManager locale = SimpleGems.getInstance().getManager(LocaleManager.class);

        player.sendMessage("");
        locale.sendCustomMessage(player, "&aWelcome " + SimpleGems.getInstance().getDescription().getName() + " Developer!");
        locale.sendCustomMessage(player, "&aThis server is currently running " + SimpleGems.getInstance().getDescription().getName() + " &bv" + SimpleGems.getInstance().getDescription().getVersion() + "&a.");
        locale.sendCustomMessage(player, "&aPlugin name&7: &f" + SimpleGems.getInstance().getDescription().getName());
        player.sendMessage("");
        locale.sendCustomMessage(player, "&aServer version&7: &f" + Bukkit.getVersion());
        player.sendMessage("");
    }

}