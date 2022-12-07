package me.refracdevelopment.simplegems.commands.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.command.framework.annotation.Optional;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.manager.LocaleManager;
import me.refracdevelopment.simplegems.manager.data.ProfileData;
import me.refracdevelopment.simplegems.utilities.Methods;
import me.refracdevelopment.simplegems.utilities.Permissions;
import me.refracdevelopment.simplegems.utilities.chat.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class TakeCommand extends RoseCommand {

    public TakeCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
        super(rosePlugin, parent);
    }

    @RoseExecutable
    public void execute(CommandContext context, Player target, double amount, @Optional String silent) {
        final LocaleManager locale = this.rosePlugin.getManager(LocaleManager.class);

        if (Bukkit.getPlayer(target.getName()) != null) {
            Player targetPlayer = Bukkit.getPlayer(target.getName());
            ProfileData targetProfile = SimpleGems.getInstance().getProfileManager().getProfile(targetPlayer.getUniqueId()).getData();

            StringPlaceholders placeholders = StringPlaceholders.builder()
                    .addAll(Placeholders.setPlaceholders(targetPlayer))
                    .addPlaceholder("player", targetPlayer.getName())
                    .addPlaceholder("gems", Methods.format(amount))
                    .addPlaceholder("gems-formatted", Methods.formatDec(amount))
                    .build();

            if (!targetProfile.getGems().hasStat(amount)) {
                locale.sendMessage(context.getSender(), "invalid-gems", placeholders);
                return;
            }

            targetProfile.getGems().decrementStat(amount);

            locale.sendMessage(context.getSender(), "gems-taken", placeholders);
            if (silent != null && silent.equals("-s")) return;
            locale.sendMessage(context.getSender(), "gems-lost", placeholders);
        } else if (Bukkit.getOfflinePlayer(target.getUniqueId()) != null && Bukkit.getOfflinePlayer(target.getUniqueId()).hasPlayedBefore()) {
            OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(target.getUniqueId());

            StringPlaceholders placeholders = StringPlaceholders.builder()
                    .addAll(Placeholders.setPlaceholders(context.getSender()))
                    .addPlaceholder("player", targetPlayer.getName())
                    .addPlaceholder("gems", Methods.format(amount))
                    .addPlaceholder("gems-formatted", Methods.formatDec(amount))
                    .build();

            if (!Methods.hasOfflineGems(target, amount)) {
                locale.sendMessage(context.getSender(), "invalid-gems", placeholders);
                return;
            }

            Methods.takeOfflineGems(target, amount);

            locale.sendMessage(context.getSender(), "gems-taken", placeholders);
        } else locale.sendMessage(context.getSender(), "invalid-player");
    }

    @Override
    protected String getDefaultName() {
        return "take";
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
