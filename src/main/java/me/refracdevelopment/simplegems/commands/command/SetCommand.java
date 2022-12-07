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

public class SetCommand extends RoseCommand {

    public SetCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
        super(rosePlugin, parent);
    }

    @RoseExecutable
    public void execute(CommandContext context, Player target, double amount, @Optional String silent) {
        final LocaleManager locale = this.rosePlugin.getManager(LocaleManager.class);

        if (Bukkit.getPlayer(target.getName()) != null) {
            Player targetPlayer = Bukkit.getPlayer(target.getName());
            ProfileData targetProfile = SimpleGems.getInstance().getProfileManager().getProfile(target.getUniqueId()).getData();

            targetProfile.getGems().setStat(amount);

            StringPlaceholders placeholders = StringPlaceholders.builder()
                    .addAll(Placeholders.setPlaceholders(target))
                    .addPlaceholder("player", targetPlayer.getName())
                    .addPlaceholder("gems", Methods.format(amount))
                    .addPlaceholder("gems-formatted", Methods.formatDec(amount))
                    .build();

            locale.sendMessage(context.getSender(), "gems-set", placeholders);
            if (silent != null && silent.equals("-s")) return;
            locale.sendMessage(targetPlayer, "gems-setted", placeholders);
        } else if (Bukkit.getOfflinePlayer(target.getUniqueId()) != null && Bukkit.getOfflinePlayer(target.getUniqueId()).hasPlayedBefore()) {
            OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(target.getUniqueId());

            Methods.setOfflineGems(target, amount);

            StringPlaceholders placeholders = StringPlaceholders.builder()
                    .addAll(Placeholders.setPlaceholders(context.getSender()))
                    .addPlaceholder("player", targetPlayer.getName())
                    .addPlaceholder("gems", Methods.format(amount))
                    .addPlaceholder("gems-formatted", Methods.formatDec(amount))
                    .build();

            locale.sendMessage(context.getSender(), "gems-set", placeholders);
        } else locale.sendMessage(context.getSender(), "invalid-player", Placeholders.setPlaceholders(context.getSender()));
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