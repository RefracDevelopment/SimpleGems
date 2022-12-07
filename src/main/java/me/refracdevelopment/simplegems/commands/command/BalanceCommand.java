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
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class BalanceCommand extends RoseCommand {

    public BalanceCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
        super(rosePlugin, parent);
    }

    @RoseExecutable
    public void execute(CommandContext context, Player target) {
        final LocaleManager locale = this.rosePlugin.getManager(LocaleManager.class);

        if (Bukkit.getPlayer(target.getName()) != null) {
            Player targetPlayer = Bukkit.getPlayer(target.getName());

            locale.sendMessage(context.getSender(), "gems-balance", Placeholders.setPlaceholders(targetPlayer));
        } else if (Bukkit.getOfflinePlayer(target.getUniqueId()) != null && Bukkit.getOfflinePlayer(target.getUniqueId()).hasPlayedBefore()) {
            OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(target.getUniqueId());

            double amount = Methods.getOfflineGems(target);

            StringPlaceholders placeholders = StringPlaceholders.builder()
                    .addAll(Placeholders.setPlaceholders(context.getSender()))
                    .addPlaceholder("player", targetPlayer.getName())
                    .addPlaceholder("gems", String.valueOf(amount))
                    .build();

            locale.sendMessage(context.getSender(), "gems-balance", placeholders);
        } else locale.sendMessage(context.getSender(), "invalid-player", Placeholders.setPlaceholders(context.getSender()));
    }

    @Override
    protected String getDefaultName() {
        return "balance";
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