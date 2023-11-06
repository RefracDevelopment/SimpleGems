package me.refracdevelopment.simplegems.manager.configuration.cache;

import me.refracdevelopment.simplegems.SimpleGems;

import java.util.List;

public class Commands {

    public String GEMS_COMMAND_NAME;
    public List<String> GEMS_COMMAND_ALIASES;

    public String HELP_COMMAND_NAME;
    public List<String> HELP_COMMAND_ALIASES;

    public String BALANCE_COMMAND_NAME;
    public List<String> BALANCE_COMMAND_ALIASES;

    public String SHOP_COMMAND_NAME;
    public List<String> SHOP_COMMAND_ALIASES;

    public String TOP_COMMAND_NAME;
    public List<String> TOP_COMMAND_ALIASES;

    public String PAY_COMMAND_NAME;
    public List<String> PAY_COMMAND_ALIASES;

    public String WITHDRAW_COMMAND_NAME;
    public List<String> WITHDRAW_COMMAND_ALIASES;

    public String UPDATE_COMMAND_NAME;
    public List<String> UPDATE_COMMAND_ALIASES;

    public String GIVE_COMMAND_NAME;
    public List<String> GIVE_COMMAND_ALIASES;

    public String TAKE_COMMAND_NAME;
    public List<String> TAKE_COMMAND_ALIASES;

    public String SET_COMMAND_NAME;
    public List<String> SET_COMMAND_ALIASES;

    public String RESET_COMMAND_NAME;
    public List<String> RESET_COMMAND_ALIASES;

    public Commands() {
        loadConfig();
    }

    public void loadConfig() {
        GEMS_COMMAND_NAME = SimpleGems.getInstance().getCommandsFile().getString("name");
        GEMS_COMMAND_ALIASES = SimpleGems.getInstance().getCommandsFile().getStringList("aliases");

        HELP_COMMAND_NAME = SimpleGems.getInstance().getCommandsFile().getString("subcommands.help.name");
        HELP_COMMAND_ALIASES = SimpleGems.getInstance().getCommandsFile().getStringList("subcommands.help.aliases");

        BALANCE_COMMAND_NAME = SimpleGems.getInstance().getCommandsFile().getString("subcommands.balance.name");
        BALANCE_COMMAND_ALIASES = SimpleGems.getInstance().getCommandsFile().getStringList("subcommands.balance.aliases");

        SHOP_COMMAND_NAME = SimpleGems.getInstance().getCommandsFile().getString("subcommands.shop.name");
        SHOP_COMMAND_ALIASES = SimpleGems.getInstance().getCommandsFile().getStringList("subcommands.shop.aliases");

        TOP_COMMAND_NAME = SimpleGems.getInstance().getCommandsFile().getString("subcommands.top.name");
        TOP_COMMAND_ALIASES = SimpleGems.getInstance().getCommandsFile().getStringList("subcommands.top.aliases");

        PAY_COMMAND_NAME = SimpleGems.getInstance().getCommandsFile().getString("subcommands.pay.name");
        PAY_COMMAND_ALIASES = SimpleGems.getInstance().getCommandsFile().getStringList("subcommands.pay.aliases");

        WITHDRAW_COMMAND_NAME = SimpleGems.getInstance().getCommandsFile().getString("subcommands.withdraw.name");
        WITHDRAW_COMMAND_ALIASES = SimpleGems.getInstance().getCommandsFile().getStringList("subcommands.withdraw.aliases");

        UPDATE_COMMAND_NAME = SimpleGems.getInstance().getCommandsFile().getString("subcommands.update.name");
        UPDATE_COMMAND_ALIASES = SimpleGems.getInstance().getCommandsFile().getStringList("subcommands.update.aliases");

        GIVE_COMMAND_NAME = SimpleGems.getInstance().getCommandsFile().getString("subcommands.give.name");
        GIVE_COMMAND_ALIASES = SimpleGems.getInstance().getCommandsFile().getStringList("subcommands.give.aliases");

        TAKE_COMMAND_NAME = SimpleGems.getInstance().getCommandsFile().getString("subcommands.take.name");
        TAKE_COMMAND_ALIASES = SimpleGems.getInstance().getCommandsFile().getStringList("subcommands.take.aliases");

        SET_COMMAND_NAME = SimpleGems.getInstance().getCommandsFile().getString("subcommands.set.name");
        SET_COMMAND_ALIASES = SimpleGems.getInstance().getCommandsFile().getStringList("subcommands.set.aliases");

        RESET_COMMAND_NAME = SimpleGems.getInstance().getCommandsFile().getString("subcommands.reset.name");
        RESET_COMMAND_ALIASES = SimpleGems.getInstance().getCommandsFile().getStringList("subcommands.reset.aliases");
    }
}