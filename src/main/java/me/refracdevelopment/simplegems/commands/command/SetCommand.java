package me.refracdevelopment.simplegems.commands.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.command.framework.annotation.Optional;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.data.ProfileData;
import me.refracdevelopment.simplegems.manager.LocaleManager;
import me.refracdevelopment.simplegems.utilities.Methods;
import me.refracdevelopment.simplegems.utilities.Permissions;
import me.refracdevelopment.simplegems.utilities.chat.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class SetCommand extends RoseCommand {

    public SetCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
        super(rosePlugin, parent);
    }

    @RoseExecutable
    public void execute(CommandContext context, OfflinePlayer target, long amount, @Optional String silent) {
        final LocaleManager locale = this.rosePlugin.getManager(LocaleManager.class);

        if (context.getArgs()[1].contains("-")) return;

        if (target.isOnline()) {
            ProfileData targetProfile = SimpleGems.getInstance().getProfileManager().getProfile(target.getUniqueId()).getData();

            targetProfile.getGems().setAmount(amount);
            Bukkit.getScheduler().runTaskAsynchronously(SimpleGems.getInstance(), () -> targetProfile.save(target.getPlayer()));

            StringPlaceholders placeholders = StringPlaceholders.builder()
                    .addAll(Placeholders.setPlaceholders(target.getPlayer()))
                    .addPlaceholder("player", target.getName())
                    .addPlaceholder("gems", Methods.format(amount))
                    .addPlaceholder("gems-decimal", Methods.formatDec(amount))
                    .build();

            locale.sendCommandMessage(context.getSender(), "gems-set", placeholders);
            if (silent != null && silent.equals("-s")) return;
            locale.sendCommandMessage(target.getPlayer(), "gems-setted", placeholders);
        } else if (!target.isOnline() && target.hasPlayedBefore()) {
            Methods.setOfflineGems(target, amount);

            StringPlaceholders placeholders = StringPlaceholders.builder()
                    .addAll(Placeholders.setPlaceholders(context.getSender()))
                    .addPlaceholder("player", target.getName())
                    .addPlaceholder("gems", Methods.format(amount))
                    .addPlaceholder("gems-decimal", Methods.formatDec(amount))
                    .build();

            locale.sendCommandMessage(context.getSender(), "gems-set", placeholders);
        } else locale.sendCommandMessage(context.getSender(), "invalid-player", Placeholders.setPlaceholders(context.getSender()));
    }

    @Override
    protected String getDefaultName() {
        return "set";
    }

    @Override
    public String getDescriptionKey() {
        return "command-set-description";
    }

    @Override
    public String getRequiredPermission() {
        return Permissions.GEMS_SET;
    }
}