package me.refracdevelopment.simplegems.commands.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.utilities.Permissions;
import org.bukkit.entity.Player;

public class TopCommand extends RoseCommand {

    public TopCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
        super(rosePlugin, parent);
    }

    @RoseExecutable
    public void execute(CommandContext context) {
        if (!(context.getSender() instanceof Player)) return;
        Player player = (Player) context.getSender();

        SimpleGems.getInstance().getLeaderboardManager().sendLeaderboard(player);
    }

    @Override
    protected String getDefaultName() {
        return "top";
    }

    @Override
    public String getDescriptionKey() {
        return "command-top-description";
    }

    @Override
    public String getRequiredPermission() {
        return Permissions.GEMS_TOP;
    }
}