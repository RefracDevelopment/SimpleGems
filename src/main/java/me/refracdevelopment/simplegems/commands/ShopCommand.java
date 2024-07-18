package me.refracdevelopment.simplegems.commands;

import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.menu.GemShopCategory;
import me.refracdevelopment.simplegems.utilities.Permissions;
import me.refracdevelopment.simplegems.utilities.chat.Placeholders;
import me.refracdevelopment.simplegems.utilities.chat.RyMessageUtils;
import me.refracdevelopment.simplegems.utilities.command.SubCommand;
import me.refracdevelopment.simplegems.utilities.exceptions.MenuManagerNotSetupException;
import me.refracdevelopment.simplegems.utilities.paginated.MenuManager;
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
        if (!(commandSender instanceof Player player)) {
            RyMessageUtils.sendPluginMessage(commandSender, "no-console", Placeholders.setPlaceholders(commandSender));
            return;
        }

        if (!player.hasPermission(Permissions.GEMS_SHOP_COMMAND)) {
            RyMessageUtils.sendPluginMessage(commandSender, "no-permission");
            return;
        }

        if (args.length != 2) {
            SimpleGems.getInstance().getGemShop().getCategories().forEach((gemShopCategory, gemShopItems) -> {
                GemShopCategory category;

                try {
                    category = new GemShopCategory(MenuManager.getPlayerMenuUtil(player), gemShopCategory);
                } catch (MenuManagerNotSetupException e) {
                    RyMessageUtils.sendPluginError("MenuManager not setup!", e, true, true);
                    return;
                }

                if (!category.isDefault())
                    return;

                if (!category.isEnabled()) {
                    RyMessageUtils.sendPluginMessage(player, "shop-disabled");
                    return;
                }

                if (!player.hasPermission(category.getPermission()) && !category.getPermission().isEmpty()) {
                    player.closeInventory();
                    RyMessageUtils.sendPluginMessage(player, "no-permission");
                    return;
                }

                category.open();
            });

            return;
        }

        SimpleGems.getInstance().getGemShop().getCategories().forEach((gemShopCategory, gemShopItems) -> {
            if (!gemShopCategory.equalsIgnoreCase(args[1])) {
                RyMessageUtils.sendConsole(true, "The 'categories." + args[1] + "' menu category in 'menus.yml' config file doesn't exist.");
                RyMessageUtils.sendPluginMessage(player, "invalid-category");
                return;
            }

            GemShopCategory category;

            try {
                category = new GemShopCategory(MenuManager.getPlayerMenuUtil(player), gemShopCategory);
            } catch (MenuManagerNotSetupException e) {
                RyMessageUtils.sendPluginError("MenuManager not setup!", e, true, true);
                return;
            }

            if (!category.isEnabled()) {
                RyMessageUtils.sendPluginMessage(player, "shop-disabled");
                return;
            }

            if (!player.hasPermission(category.getPermission()) && !category.getPermission().isEmpty()) {
                player.closeInventory();
                RyMessageUtils.sendPluginMessage(player, "no-permission");
                return;
            }

            category.open();
        });
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return List.of();
    }

}