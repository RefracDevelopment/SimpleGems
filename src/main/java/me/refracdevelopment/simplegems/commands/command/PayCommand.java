package me.refracdevelopment.simplegems.commands.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.command.framework.annotation.Optional;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import me.refracdevelopment.simplegems.manager.LocaleManager;
import me.refracdevelopment.simplegems.utilities.Methods;
import me.refracdevelopment.simplegems.utilities.Permissions;
import me.refracdevelopment.simplegems.utilities.chat.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PayCommand extends RoseCommand {

    public PayCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
        super(rosePlugin, parent);
    }

    @RoseExecutable
    public void execute(CommandContext context, Player target, double amount, @Optional String silent) {
        final LocaleManager locale = this.rosePlugin.getManager(LocaleManager.class);

        // Make sure the sender is a player.
        if (!(context.getSender() instanceof Player)) {
            locale.sendMessage(context.getSender(), "no-console", Placeholders.setPlaceholders(context.getSender()));
            return;
        }

        Player player = (Player) context.getSender();

        if (Bukkit.getPlayer(target.getName()) != null) {
            Player targetPlayer = Bukkit.getPlayer(target.getName());

            if (silent == null || !silent.equals("-s")) {
                Methods.payGems(player, targetPlayer, amount, false);
            } else {
                Methods.payGems(player, targetPlayer, amount, true);
            }
        } else if (Bukkit.getOfflinePlayer(target.getUniqueId()) != null && Bukkit.getOfflinePlayer(target.getUniqueId()).hasPlayedBefore()) {
            OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(target.getUniqueId());

            Methods.payOfflineGems(player, targetPlayer, amount);
        } else locale.sendMessage(player, "invalid-player", Placeholders.setPlaceholders(player));
    }

    @Override
    protected String getDefaultName() {
        return "pay";
    }

    @Override
    public String getDescriptionKey() {
        return "command-pay-description";
    }

    @Override
    public String getRequiredPermission() {
        return Permissions.GEMS_PAY;
    }
}