package me.refracdevelopment.simplegems.commands.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.manager.LocaleManager;
import me.refracdevelopment.simplegems.utilities.Permissions;
import me.refracdevelopment.simplegems.utilities.chat.Placeholders;
import me.refracdevelopment.simplegems.utilities.config.Menus;
import org.bukkit.entity.Player;

public class ShopCommand extends RoseCommand {

    public ShopCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
        super(rosePlugin, parent);
    }

    @RoseExecutable
    public void execute(CommandContext context) {
        final LocaleManager locale = this.rosePlugin.getManager(LocaleManager.class);

        // Make sure the sender is a player.
        if (!(context.getSender() instanceof Player)) {
            locale.sendMessage(context.getSender(), "no-console", Placeholders.setPlaceholders(context.getSender()));
            return;
        }

        Player player = (Player) context.getSender();

        if (Menus.GEM_SHOP_ENABLED) {
            SimpleGems.getInstance().getGemShop().getGemShop().openInventory(player);
        } else locale.sendMessage(player, "shop-disabled", Placeholders.setPlaceholders(player));
    }

    @Override
    protected String getDefaultName() {
        return "shop";
    }

    @Override
    public String getDescriptionKey() {
        return "command-shop-description";
    }

    @Override
    public String getRequiredPermission() {
        return Permissions.GEMS_SHOP;
    }
}