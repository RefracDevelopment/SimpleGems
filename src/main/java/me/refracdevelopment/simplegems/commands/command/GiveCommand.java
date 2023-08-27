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
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class GiveCommand extends RoseCommand {

    public GiveCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
        super(rosePlugin, parent);
    }

    @RoseExecutable
    public void execute(CommandContext context, String target, long amount, @Optional String silent) {
        final LocaleManager locale = this.rosePlugin.getManager(LocaleManager.class);

        // note: used to prevent adding/removing negative numbers.
        if (context.getArgs()[1].contains("-")) return;

        if (Bukkit.getPlayer(target) != null) {
            Player player = Bukkit.getPlayer(target);
            SimpleGemsAPI.INSTANCE.giveGems(player, amount);

            StringPlaceholders placeholders = StringPlaceholders.builder()
                    .addAll(Placeholders.setPlaceholders(player))
                    .add("player", player.getName())
                    .add("gems", String.valueOf(amount))
                    .add("gems_formatted", Methods.format(amount))
                    .add("gems_decimal", Methods.formatDecimal(amount))
                    .build();

            locale.sendMessage(context.getSender(), "gems-given", placeholders);
            if (silent != null && silent.equalsIgnoreCase("-s")) return;
            locale.sendMessage(player, "gems-gained", placeholders);
        } else if (Bukkit.getOfflinePlayer(target).hasPlayedBefore()) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(target);
            SimpleGemsAPI.INSTANCE.giveOfflineGems(offlinePlayer, amount);

            StringPlaceholders placeholders = StringPlaceholders.builder()
                    .addAll(Placeholders.setPlaceholders(context.getSender()))
                    .add("player", offlinePlayer.getName())
                    .add("gems", String.valueOf(amount))
                    .add("gems_formatted", Methods.format(amount))
                    .add("gems_decimal", Methods.formatDecimal(amount))
                    .build();

            locale.sendMessage(context.getSender(), "gems-given", placeholders);
        } else locale.sendMessage(context.getSender(), "invalid-player", Placeholders.setPlaceholders(context.getSender()));
    }

    @Override
    protected String getDefaultName() {
        return "give";
    }

    @Override
    protected List<String> getDefaultAliases() {
        return Collections.singletonList("add");
    }

    @Override
    public String getDescriptionKey() {
        return "command-give-description";
    }

    @Override
    public String getRequiredPermission() {
        return Permissions.GEMS_GIVE;
    }
}