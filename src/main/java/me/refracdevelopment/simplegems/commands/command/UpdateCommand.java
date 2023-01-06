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
import org.bukkit.entity.Player;

public class UpdateCommand extends RoseCommand {

    public UpdateCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
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

        SimpleGems.getInstance().getLeaderboardManager().update();
    }

    @Override
    protected String getDefaultName() {
        return "update";
    }

    @Override
    public String getDescriptionKey() {
        return "command-update-description";
    }

    @Override
    public String getRequiredPermission() {
        return Permissions.GEMS_UPDATE;
    }
}