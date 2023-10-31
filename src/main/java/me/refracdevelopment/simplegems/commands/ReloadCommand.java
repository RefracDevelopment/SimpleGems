package me.refracdevelopment.simplegems.commands;

import me.kodysimpson.simpapi.command.SubCommand;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.utilities.Permissions;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class ReloadCommand extends SubCommand {

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public List<String> getAliases() {
        return Collections.emptyList();
    }

    @Override
    public String getDescription() {
        return SimpleGems.getInstance().getLocaleFile().getString("command-reload-description");
    }

    @Override
    public String getSyntax() {
        return "";
    }

    @Override
    public void perform(CommandSender commandSender, String[] args) {
        if (!commandSender.hasPermission(Permissions.GEMS_RELOAD_COMMAND)) {
            Color.sendMessage(commandSender, "no-permission");
            return;
        }

        Bukkit.getScheduler().cancelTasks(SimpleGems.getInstance());
        SimpleGems.getInstance().reloadFiles();
        SimpleGems.getInstance().getGemShop().load();
        Color.sendMessage(commandSender, "command-reload-success");
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] strings) {
        return null;
    }

}