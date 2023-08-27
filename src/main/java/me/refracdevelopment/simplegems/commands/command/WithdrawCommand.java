package me.refracdevelopment.simplegems.commands.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import me.refracdevelopment.simplegems.api.SimpleGemsAPI;
import me.refracdevelopment.simplegems.manager.configuration.LocaleManager;
import me.refracdevelopment.simplegems.utilities.Permissions;
import me.refracdevelopment.simplegems.utilities.chat.Placeholders;
import org.bukkit.entity.Player;

public class WithdrawCommand extends RoseCommand {

    public WithdrawCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
        super(rosePlugin, parent);
    }

    @RoseExecutable
    public void execute(CommandContext context, long amount) {
        final LocaleManager locale = this.rosePlugin.getManager(LocaleManager.class);

        // Make sure the sender is a player.
        if (!(context.getSender() instanceof Player)) {
            locale.sendMessage(context.getSender(), "no-console", Placeholders.setPlaceholders(context.getSender()));
            return;
        }

        Player player = (Player) context.getSender();

        // note: used to prevent adding/removing negative numbers.
        if (context.getArgs()[0].contains("-")) return;

        SimpleGemsAPI.INSTANCE.withdrawGems(player, amount);
    }

    @Override
    protected String getDefaultName() {
        return "withdraw";
    }

    @Override
    public String getDescriptionKey() {
        return "command-withdraw-description";
    }

    @Override
    public String getRequiredPermission() {
        return Permissions.GEMS_WITHDRAW;
    }
}