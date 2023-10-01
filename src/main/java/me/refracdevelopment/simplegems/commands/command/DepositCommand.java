package me.refracdevelopment.simplegems.commands.command;

import de.tr7zw.nbtapi.NBTItem;
import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import me.refracdevelopment.simplegems.api.SimpleGemsAPI;
import me.refracdevelopment.simplegems.manager.configuration.LocaleManager;
import me.refracdevelopment.simplegems.utilities.Methods;
import me.refracdevelopment.simplegems.utilities.Permissions;
import me.refracdevelopment.simplegems.utilities.chat.Placeholders;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DepositCommand extends RoseCommand {

    public DepositCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
        super(rosePlugin, parent);
    }

    @RoseExecutable
    public void execute(CommandContext context) {
        final LocaleManager locale = this.rosePlugin.getManager(LocaleManager.class);

        // Make sure the sender is a player.
        if (!(context.getSender() instanceof Player)) {
            locale.sendMessage(context.getSender(), "no-console", Placeholders.setPlaceholders(context.getSender()));
            return;
        }

        Player player = (Player) context.getSender();

        long deposited = 0;

        for (ItemStack itemStack : player.getInventory().getContents()) {
            ItemStack gemsItem;

            NBTItem nbtItem = new NBTItem(itemStack);

            if(nbtItem.hasTag("gems-item-value")) {
                long foundValue = nbtItem.getLong("gems-item-value");

                gemsItem = SimpleGemsAPI.INSTANCE.getGemsItem(foundValue);

                if (!itemStack.isSimilar(gemsItem)) return;

                try {
                    player.getInventory().removeItem(itemStack);
                } catch (Exception ignored) {}
                SimpleGemsAPI.INSTANCE.giveGems(player, foundValue);
            }
            deposited++;
        }

        StringPlaceholders placeholders = StringPlaceholders.builder()
                .addAll(Placeholders.setPlaceholders(player))
                .add("value", String.valueOf(deposited))
                .add("gems", String.valueOf(deposited))
                .add("gems_formatted", Methods.format(deposited))
                .add("gems_decimal", Methods.formatDecimal(deposited))
                .build();

        locale.sendMessage(player, "gems-deposited", placeholders);
    }

    @Override
    protected String getDefaultName() {
        return "deposit";
    }

    @Override
    public String getDescriptionKey() {
        return "command-deposit-description";
    }

    @Override
    public String getRequiredPermission() {
        return Permissions.GEMS_DEPOSIT;
    }
}