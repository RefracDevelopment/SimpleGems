package me.refracdevelopment.simplegems.commands.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import me.refracdevelopment.simplegems.api.SimpleGemsAPI;
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
            locale.sendMessage(context.getSender(), "gems-balance", Placeholders.setPlaceholders(target.getPlayer()));
        } else if (!target.isOnline() && target.hasPlayedBefore()) {
            double amount = SimpleGemsAPI.INSTANCE.getOfflineGems(target);

            StringPlaceholders placeholders = StringPlaceholders.builder()
                    .addAll(Placeholders.setPlaceholders(context.getSender()))
                    .add("player", target.getName())
                    .add("gems", String.valueOf(amount))
                    .add("gems_formatted", Methods.format(amount))
                    .add("gems_decimal", Methods.formatDecimal(amount))
                    .build();

            locale.sendMessage(context.getSender(), "gems-balance", placeholders);
        } else locale.sendMessage(context.getSender(), "invalid-player", Placeholders.setPlaceholders(context.getSender()));
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