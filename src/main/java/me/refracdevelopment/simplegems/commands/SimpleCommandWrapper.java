package me.refracdevelopment.simplegems.commands;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SimpleCommandWrapper extends RoseCommandWrapper {

    public SimpleCommandWrapper(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @Override
    public String getDefaultName() {
        return "gems";
    }

    @Override
    public List<String> getDefaultAliases() {
        return Arrays.asList(
                "simplegems",
                "consolegems",
                "cgems"
        );
    }

    @Override
    public List<String> getCommandPackages() {
        return Collections.singletonList("me.refracdevelopment.simplegems.commands.command");
    }

    @Override
    public boolean includeBaseCommand() {
        return false;
    }

    @Override
    public boolean includeHelpCommand() {
        return true;
    }

    @Override
    public boolean includeReloadCommand() {
        return true;
    }
}