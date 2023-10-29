package me.refracdevelopment.simplegems.commands;

import me.kodysimpson.simpapi.command.SubCommand;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.menu.GemShopCategory;
import me.refracdevelopment.simplegems.utilities.Permissions;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import me.refracdevelopment.simplegems.utilities.chat.Placeholders;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ShopCommand extends SubCommand {

    @Override
    public String getName() {
        return "shop";
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return SimpleGems.getInstance().getLocaleFile().getString("command-shop-description");
    }

    @Override
    public String getSyntax() {
        return "[category]";
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        // Make sure the sender is a player.
        if (!(commandSender instanceof Player)) {
            Color.sendMessage(commandSender, "no-console", Placeholders.setPlaceholders(commandSender));
            return;
        }

        Player player = (Player) commandSender;

        if (!player.hasPermission(Permissions.GEMS_SHOP_COMMAND)) {
            Color.sendMessage(commandSender, "no-permission");
            return;
        }

        if (strings.length == 1) {
            SimpleGems.getInstance().getGemShop().getCategories().forEach((gemShopCategory, gemShopItems) -> {
                if (gemShopCategory.isDefaultCategory()) {
                    if (!gemShopCategory.isEnabled()) {
                        Color.sendMessage(player, "shop-disabled");
                        return;
                    }

                    new GemShopCategory(SimpleGems.getInstance().getMenuManager().getPlayerMenuUtility(player), gemShopCategory.getCategoryName()).open();
                    return;
                }
            });
            return;
        }

        if (strings.length == 2) {
            if (SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.contains(strings[1]) && SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.getBoolean(strings[1] + ".enabled")) {
                new GemShopCategory(SimpleGems.getInstance().getMenuManager().getPlayerMenuUtility(player), strings[1]).open();
            } else if (!SimpleGems.getInstance().getMenus().GEM_SHOP_CATEGORIES.contains(strings[1])) {
                Color.log("");
                Color.log("");
                Color.log("The 'categories." + strings[1] + "' menu category in 'menus.yml' config file doesn't exist.");
                Color.log("");
                Color.log("");
                Color.sendMessage(player, "invalid-category");
            } else Color.sendMessage(player, "shop-disabled");
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] strings) {
        return null;
    }

}