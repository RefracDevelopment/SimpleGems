package me.refracdevelopment.simplegems.commands.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.command.framework.annotation.Optional;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import me.refracdevelopment.simplegems.api.SimpleGemsAPI;
import me.refracdevelopment.simplegems.manager.configuration.LocaleManager;
import me.refracdevelopment.simplegems.utilities.Methods;
import me.refracdevelopment.simplegems.utilities.Permissions;
import me.refracdevelopment.simplegems.utilities.chat.Placeholders;
import org.bukkit.OfflinePlayer;

import java.util.Collections;
import java.util.List;

public class TakeCommand extends RoseCommand {

    public TakeCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
        super(rosePlugin, parent);
    }

    @RoseExecutable
    public void execute(CommandContext context, OfflinePlayer target, long amount, @Optional String silent) {
        final LocaleManager locale = this.rosePlugin.getManager(LocaleManager.class);

        if (context.getArgs()[1].contains("-")) return;

        if (target.isOnline()) {
            StringPlaceholders placeholders = StringPlaceholders.builder()
                    .addAll(Placeholders.setPlaceholders(context.getSender()))
                    .add("player", target.getName())
                    .add("gems", String.valueOf(amount))
                    .add("gems_formatted", Methods.format(amount))
                    .add("gems_decimal", Methods.formatDecimal(amount))
                    .build();

            if (!SimpleGemsAPI.INSTANCE.hasGems(target.getPlayer(), amount)) {
                locale.sendMessage(context.getSender(), "invalid-gems", placeholders);
                return;
            }

            SimpleGemsAPI.INSTANCE.takeGems(target.getPlayer(), amount);

            locale.sendMessage(context.getSender(), "gems-taken", placeholders);
            if (silent != null && silent.equalsIgnoreCase("-s")) return;
            locale.sendMessage(target.getPlayer(), "gems-lost", placeholders);
        } else if (!target.isOnline() && target.hasPlayedBefore()) {
            StringPlaceholders placeholders = StringPlaceholders.builder()
                    .addAll(Placeholders.setPlaceholders(context.getSender()))
                    .add("player", target.getName())
                    .add("gems", String.valueOf(amount))
                    .add("gems_formatted", Methods.format(amount))
                    .add("gems_decimal", Methods.formatDecimal(amount))
                    .build();

            if (!SimpleGemsAPI.INSTANCE.hasOfflineGems(target, amount)) {
                locale.sendMessage(context.getSender(), "invalid-gems", placeholders);
                return;
            }

            SimpleGemsAPI.INSTANCE.takeOfflineGems(target, amount);

            locale.sendMessage(context.getSender(), "gems-taken", placeholders);
        } else locale.sendMessage(context.getSender(),"invalid-player");
    }

    @Override
    protected String getDefaultName() {
        return "take";
    }

    @Override
    protected List<String> getDefaultAliases() {
        return Collections.singletonList("remove");
    }

    @Override
    public String getDescriptionKey() {
        return "command-take-description";
    }

    @Override
    public String getRequiredPermission() {
        return Permissions.GEMS_TAKE;
    }
}
