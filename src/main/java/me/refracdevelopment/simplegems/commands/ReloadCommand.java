package me.refracdevelopment.simplegems.commands;

import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.utilities.Permissions;
import me.refracdevelopment.simplegems.utilities.chat.RyMessageUtils;
import me.refracdevelopment.simplegems.utilities.command.SubCommand;
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
            RyMessageUtils.sendPluginMessage(commandSender, "no-permission");
            return;
        }

        SimpleGems.getInstance().getPaperLib().scheduling().cancelGlobalTasks();
        reloadFiles();
        SimpleGems.getInstance().getGemShop().setupCustomMenuData();
        SimpleGems.getInstance().getLeaderboardManager().updateTask();
        RyMessageUtils.sendPluginMessage(commandSender, "command-reload-success");
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return List.of();
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

        RyMessageUtils.sendConsole(true, "&aReloaded all files.");
    }
}