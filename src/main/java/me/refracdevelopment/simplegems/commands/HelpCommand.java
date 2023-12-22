package me.refracdevelopment.simplegems.commands;

import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import me.refracdevelopment.simplegems.utilities.chat.StringPlaceholders;
import me.refracdevelopment.simplegems.utilities.command.SubCommand;
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
        Color.sendMessage(commandSender, "command-help-title");
        SimpleGems.getInstance().getSubCommands().forEach(command -> {
            StringPlaceholders placeholders;

            if (!command.getSyntax().isEmpty()) {
                placeholders = StringPlaceholders.builder()
                        .add("cmd", SimpleGems.getInstance().getCommands().GEMS_COMMAND_NAME)
                        .add("subcmd", command.getName())
                        .add("args", command.getSyntax())
                        .add("desc", command.getDescription())
                        .build();
                Color.sendMessage(commandSender, "command-help-list-description", placeholders);
            } else {
                placeholders = StringPlaceholders.builder()
                        .add("cmd", SimpleGems.getInstance().getCommands().GEMS_COMMAND_NAME)
                        .add("subcmd", command.getName())
                        .add("desc", command.getDescription())
                        .build();
                Color.sendMessage(commandSender, "command-help-list-description-no-args", placeholders);
            }
        });
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}