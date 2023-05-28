package me.refracdevelopment.simplegems.commands.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.utilities.Permissions;

public class UpdateCommand extends RoseCommand {

    public UpdateCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
        super(rosePlugin, parent);
    }

    @RoseExecutable
    public void execute(CommandContext context) {
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