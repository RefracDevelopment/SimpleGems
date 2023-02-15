package me.refracdevelopment.simplegems.commands.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import me.refracdevelopment.simplegems.manager.LocaleManager;
import me.refracdevelopment.simplegems.utilities.Methods;
import me.refracdevelopment.simplegems.utilities.Permissions;
import me.refracdevelopment.simplegems.utilities.chat.Placeholders;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;
import java.util.List;

public class BalanceCommand extends RoseCommand {

    public BalanceCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
        super(rosePlugin, parent);
    }

    @RoseExecutable
    public void execute(CommandContext context, OfflinePlayer target) {
        final LocaleManager locale = this.rosePlugin.getManager(LocaleManager.class);

        if (target.isOnline()) {
            locale.sendCommandMessage(context.getSender(), "gems-balance", Placeholders.setPlaceholders(target.getPlayer()));
        } else if (!target.isOnline() && target.hasPlayedBefore()) {
            long amount = Methods.getOfflineGems(target);

            StringPlaceholders placeholders = StringPlaceholders.builder()
                    .addAll(Placeholders.setPlaceholders(context.getSender()))
                    .addPlaceholder("player", target.getName())
                    .addPlaceholder("gems", String.valueOf(amount))
                    .addPlaceholder("gems_formatted", Methods.format(amount))
                    .addPlaceholder("gems_decimal", Methods.formatDec(amount))
                    .build();

            locale.sendCommandMessage(context.getSender(), "gems-balance", placeholders);
        } else locale.sendCommandMessage(context.getSender(), "invalid-player", Placeholders.setPlaceholders(context.getSender()));
    }

    @Override
    protected String getDefaultName() {
        return "balance";
    }

    @Override
    protected List<String> getDefaultAliases() {
        return Arrays.asList("bal");
    }

    @Override
    public String getDescriptionKey() {
        return "command-balance-description";
    }

    @Override
    public String getRequiredPermission() {
        return Permissions.GEMS_BALANCE;
    }
}