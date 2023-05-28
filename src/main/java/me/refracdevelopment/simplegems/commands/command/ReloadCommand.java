package me.refracdevelopment.simplegems.commands.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.manager.LocaleManager;
import me.refracdevelopment.simplegems.menu.GemShopItem;
import me.refracdevelopment.simplegems.utilities.Permissions;
import me.refracdevelopment.simplegems.utilities.config.Config;
import me.refracdevelopment.simplegems.utilities.config.Menus;
import org.bukkit.Bukkit;

public class ReloadCommand extends RoseCommand {

    public ReloadCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
        super(rosePlugin, parent);
    }

    @RoseExecutable
    public void execute(CommandContext context) {
        this.rosePlugin.reload();
        Config.loadConfig();
        SimpleGems.getInstance().getMenusFile().load();
        Bukkit.getScheduler().cancelTasks(SimpleGems.getInstance());
        SimpleGems.getInstance().getLeaderboardManager().updateTask();
        SimpleGems.getInstance().getGemShop().getItems().clear();
        Menus.GEM_SHOP_ITEMS.getKeys(false).forEach(item -> SimpleGems.getInstance().getGemShop().getItems().put(item, new GemShopItem(item)));
        this.rosePlugin.getManager(LocaleManager.class).sendCommandMessage(context.getSender(), "command-reload-success");
    }

    @Override
    public String getDefaultName() {
        return "reload";
    }

    @Override
    public String getDescriptionKey() {
        return "command-reload-description";
    }

    @Override
    public String getRequiredPermission() {
        return Permissions.GEMS_RELOAD;
    }

}