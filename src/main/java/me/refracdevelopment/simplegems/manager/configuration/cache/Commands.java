package me.refracdevelopment.simplegems.manager.configuration.cache;

import me.refracdevelopment.simplegems.SimpleGems;

import java.util.List;

public class Commands {

    public boolean GEMS_COMMAND_ENABLED;
    public List<String> GEMS_COMMAND_ALIASES;

    public boolean BALANCE_COMMAND_ENABLED;
    public List<String> BALANCE_COMMAND_ALIASES;

    public boolean SHOP_COMMAND_ENABLED;
    public List<String> SHOP_COMMAND_ALIASES;

    public boolean TOP_COMMAND_ENABLED;
    public List<String> TOP_COMMAND_ALIASES;

    public boolean PAY_COMMAND_ENABLED;
    public List<String> PAY_COMMAND_ALIASES;

    public boolean WITHDRAW_COMMAND_ENABLED;
    public List<String> WITHDRAW_COMMAND_ALIASES;

    public boolean RELOAD_COMMAND_ENABLED;
    public List<String> RELOAD_COMMAND_ALIASES;

    public boolean VERSION_COMMAND_ENABLED;
    public List<String> VERSION_COMMAND_ALIASES;

    public boolean UPDATE_COMMAND_ENABLED;
    public List<String> UPDATE_COMMAND_ALIASES;

    public boolean GIVE_COMMAND_ENABLED;
    public List<String> GIVE_COMMAND_ALIASES;

    public boolean TAKE_COMMAND_ENABLED;
    public List<String> TAKE_COMMAND_ALIASES;

    public boolean SET_COMMAND_ENABLED;
    public List<String> SET_COMMAND_ALIASES;

    public Commands() {
        loadConfig();
    }

    public void loadConfig() {
        GEMS_COMMAND_ENABLED = SimpleGems.getInstance().getCommandsFile().getBoolean("commands.gems.enabled");
        GEMS_COMMAND_ALIASES = SimpleGems.getInstance().getCommandsFile().getStringList("commands.gems.aliases");
        
        BALANCE_COMMAND_ENABLED = SimpleGems.getInstance().getCommandsFile().getBoolean("commands.balance.enabled");
        BALANCE_COMMAND_ALIASES = SimpleGems.getInstance().getCommandsFile().getStringList("commands.balance.aliases");

        SHOP_COMMAND_ENABLED = SimpleGems.getInstance().getCommandsFile().getBoolean("commands.shop.enabled");
        SHOP_COMMAND_ALIASES = SimpleGems.getInstance().getCommandsFile().getStringList("commands.shop.aliases");

        TOP_COMMAND_ENABLED = SimpleGems.getInstance().getCommandsFile().getBoolean("commands.top.enabled");
        TOP_COMMAND_ALIASES = SimpleGems.getInstance().getCommandsFile().getStringList("commands.top.aliases");

        PAY_COMMAND_ENABLED = SimpleGems.getInstance().getCommandsFile().getBoolean("commands.pay.enabled");
        PAY_COMMAND_ALIASES = SimpleGems.getInstance().getCommandsFile().getStringList("commands.pay.aliases");

        WITHDRAW_COMMAND_ENABLED = SimpleGems.getInstance().getCommandsFile().getBoolean("commands.withdraw.enabled");
        WITHDRAW_COMMAND_ALIASES = SimpleGems.getInstance().getCommandsFile().getStringList("commands.withdraw.aliases");

        RELOAD_COMMAND_ENABLED = SimpleGems.getInstance().getCommandsFile().getBoolean("commands.reload.enabled");
        RELOAD_COMMAND_ALIASES = SimpleGems.getInstance().getCommandsFile().getStringList("commands.reload.aliases");

        VERSION_COMMAND_ENABLED = SimpleGems.getInstance().getCommandsFile().getBoolean("commands.version.enabled");
        VERSION_COMMAND_ALIASES = SimpleGems.getInstance().getCommandsFile().getStringList("commands.version.aliases");

        UPDATE_COMMAND_ENABLED = SimpleGems.getInstance().getCommandsFile().getBoolean("commands.update.enabled");
        UPDATE_COMMAND_ALIASES = SimpleGems.getInstance().getCommandsFile().getStringList("commands.update.aliases");

        GIVE_COMMAND_ENABLED = SimpleGems.getInstance().getCommandsFile().getBoolean("commands.give.enabled");
        GIVE_COMMAND_ALIASES = SimpleGems.getInstance().getCommandsFile().getStringList("commands.give.aliases");

        TAKE_COMMAND_ENABLED = SimpleGems.getInstance().getCommandsFile().getBoolean("commands.take.enabled");
        TAKE_COMMAND_ALIASES = SimpleGems.getInstance().getCommandsFile().getStringList("commands.take.aliases");

        SET_COMMAND_ENABLED = SimpleGems.getInstance().getCommandsFile().getBoolean("commands.set.enabled");
        SET_COMMAND_ALIASES = SimpleGems.getInstance().getCommandsFile().getStringList("commands.set.aliases");
    }
}