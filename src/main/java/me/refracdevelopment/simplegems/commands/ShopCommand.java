package me.refracdevelopment.simplegems.commands;

import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.menu.GemShopCategory;
import me.refracdevelopment.simplegems.utilities.Permissions;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import me.refracdevelopment.simplegems.utilities.chat.Placeholders;
import me.refracdevelopment.simplegems.utilities.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ShopCommand extends SubCommand {

    @Override
    public String getName() {
        return SimpleGems.getInstance().getCommands().SHOP_COMMAND_NAME;
    }

    @Override
    public List<String> getAliases() {
        return SimpleGems.getInstance().getCommands().SHOP_COMMAND_ALIASES;
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
    public void perform(CommandSender commandSender, String[] args) {
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

        if (args.length != 2) {
            SimpleGems.getInstance().getGemShop().getCategories().forEach((gemShopCategory, gemShopItems) -> {
                GemShopCategory category = new GemShopCategory(SimpleGems.getInstance().getMenuManager().getPlayerMenuUtility(player), gemShopCategory);

                if (!category.isDefault())
                    return;

                if (!category.isEnabled()) {
                    Color.sendMessage(player, "shop-disabled");
                    return;
                }

                if (!player.hasPermission(category.getPermission()) && !category.getPermission().isEmpty()) {
                    player.closeInventory();
                    Color.sendMessage(player, "no-permission");
                    return;
                }

                category.open();
            });

            return;
        }

        SimpleGems.getInstance().getGemShop().getCategories().forEach((gemShopCategory, gemShopItems) -> {
            if (!gemShopCategory.equalsIgnoreCase(args[1])) {
                Color.log("The 'categories." + args[1] + "' menu category in 'menus.yml' config file doesn't exist.");
                Color.sendMessage(player, "invalid-category");
                return;
            }

            GemShopCategory category = new GemShopCategory(SimpleGems.getInstance().getMenuManager().getPlayerMenuUtility(player), gemShopCategory);

            if (!category.isEnabled()) {
                Color.sendMessage(player, "shop-disabled");
                return;
            }

            if (!player.hasPermission(category.getPermission()) && !category.getPermission().isEmpty()) {
                player.closeInventory();
                Color.sendMessage(player, "no-permission");
                return;
            }

            category.open();
        });
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }

}