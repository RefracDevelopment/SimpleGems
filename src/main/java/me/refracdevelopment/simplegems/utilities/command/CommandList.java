package me.refracdevelopment.simplegems.utilities.command;

import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * A functional interface used to allow the dev to specify how the listing of the subcommands on a core command works.
 */
@FunctionalInterface
public interface CommandList {

    /**
     * @param commandSender  The thing that ran the command
     * @param subCommandList A list of all the subcommands you can display
     */
    void displayCommandList(CommandSender commandSender, List<SubCommand> subCommandList);

}