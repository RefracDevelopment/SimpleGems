package me.refracdevelopment.simplegems.commands;

import me.kodysimpson.simpapi.command.SubCommand;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.utilities.Permissions;
import me.refracdevelopment.simplegems.utilities.chat.RyMessageUtils;
import me.refracdevelopment.simplegems.utilities.chat.StringPlaceholders;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class VersionCommand extends SubCommand {

    @Override
    public String getName() {
        return "version";
    }

    @Override
    public List<String> getAliases() {
        return Collections.emptyList();
    }

    @Override
    public String getDescription() {
        return SimpleGems.getInstance().getLocaleFile().getString("command-version-description");
    }

    @Override
    public String getSyntax() {
        return "";
    }

    @Override
    public void perform(CommandSender commandSender, String[] args) {
        if (!commandSender.hasPermission(Permissions.GEMS_VERSION_COMMAND)) {
            RyMessageUtils.sendPluginMessage(commandSender, "no-permission");
            return;
        }

        String baseColor = SimpleGems.getInstance().getLocaleFile().getString("base-command-color");
        RyMessageUtils.sendSender(commandSender, baseColor + "Running <gradient:#8A2387:#E94057:#F27121:0>" + SimpleGems.getInstance().getDescription().getName() + baseColor + " v" + SimpleGems.getInstance().getDescription().getVersion());
        RyMessageUtils.sendSender(commandSender, baseColor + "Plugin created by: <gradient:#41E0F0:#FF8DCE:0>" + SimpleGems.getInstance().getDescription().getAuthors().get(0));
        RyMessageUtils.sendPluginMessage(commandSender, "base-command-help", StringPlaceholders.of("cmd", SimpleGems.getInstance().getCommands().GEMS_COMMAND_NAME));
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return List.of();
    }
}