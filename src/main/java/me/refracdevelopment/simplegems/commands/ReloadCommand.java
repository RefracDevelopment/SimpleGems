package me.refracdevelopment.simplegems.commands;

import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.utilities.Permissions;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import me.refracdevelopment.simplegems.utilities.command.SubCommand;
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
        reloadFiles();
        SimpleGems.getInstance().getGemShop().load();
        SimpleGems.getInstance().getLeaderboardManager().updateTask();
        Color.sendMessage(commandSender, "command-reload-success");
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }

    private void reloadFiles() {
        // Files
        SimpleGems.getInstance().getConfigFile().reload();
        SimpleGems.getInstance().getMenusFile().reload();
        SimpleGems.getInstance().getCommandsFile().reload();
        SimpleGems.getInstance().getLocaleFile().reload();

        // Cache
        SimpleGems.getInstance().getSettings().loadConfig();
        SimpleGems.getInstance().getMenus().loadConfig();
        SimpleGems.getInstance().getCommands().loadConfig();

        Color.log("&c==========================================");
        Color.log("&eAll files have been reloaded correctly!");
        Color.log("&c==========================================");
    }
}