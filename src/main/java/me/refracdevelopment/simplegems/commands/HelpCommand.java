package me.refracdevelopment.simplegems.commands;

import me.kodysimpson.simpapi.command.SubCommand;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.utilities.chat.RyMessageUtils;
import me.refracdevelopment.simplegems.utilities.chat.StringPlaceholders;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class HelpCommand extends SubCommand {

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public List<String> getAliases() {
        return Collections.emptyList();
    }

    @Override
    public String getDescription() {
        return SimpleGems.getInstance().getLocaleFile().getString("command-help-description");
    }

    @Override
    public String getSyntax() {
        return "";
    }

    @Override
    public void perform(CommandSender commandSender, String[] args) {
        RyMessageUtils.sendPluginMessage(commandSender, "command-help-title");

        SimpleGems.getInstance().getCommandsList().forEach(command -> {
            StringPlaceholders placeholders;

            if (!command.getSyntax().isEmpty()) {
                placeholders = StringPlaceholders.builder()
                        .add("cmd", SimpleGems.getInstance().getCommands().GEMS_COMMAND_NAME)
                        .add("subcmd", command.getName())
                        .add("args", command.getSyntax())
                        .add("desc", command.getDescription())
                        .build();
                RyMessageUtils.sendPluginMessage(commandSender, "command-help-list-description", placeholders);
            } else {
                placeholders = StringPlaceholders.builder()
                        .add("cmd", SimpleGems.getInstance().getCommands().GEMS_COMMAND_NAME)
                        .add("subcmd", command.getName())
                        .add("desc", command.getDescription())
                        .build();
                RyMessageUtils.sendPluginMessage(commandSender, "command-help-list-description-no-args", placeholders);
            }
        });
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return List.of();
    }
}