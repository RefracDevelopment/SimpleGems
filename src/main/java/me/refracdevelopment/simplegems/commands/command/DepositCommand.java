package me.refracdevelopment.simplegems.commands.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.manager.LocaleManager;
import me.refracdevelopment.simplegems.manager.data.ProfileData;
import me.refracdevelopment.simplegems.utilities.Methods;
import me.refracdevelopment.simplegems.utilities.Permissions;
import me.refracdevelopment.simplegems.utilities.chat.Placeholders;
import org.bukkit.Material;
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

        ProfileData profile = SimpleGems.getInstance().getProfileManager().getProfile(player.getUniqueId()).getData();

        ItemStack gemsItem = Methods.getGemsItem();

        for (ItemStack item : player.getInventory().getContents()) {
            if (item.getItemMeta() == null) return;
            if (!item.hasItemMeta()) return;
            if (item.getType() != gemsItem.getType()) return;
            if (!item.getItemMeta().getDisplayName().equals(gemsItem.getItemMeta().getDisplayName())) return;

            StringPlaceholders placeholders = StringPlaceholders.builder()
                    .addAll(Placeholders.setPlaceholders(context.getSender()))
                    .addPlaceholder("gems", item.getAmount())
                    .build();

            profile.getGems().incrementStat(item.getAmount());
            item.setType(Material.AIR);
            locale.sendMessage(player, "gems-deposited", placeholders);
            return;
        }
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