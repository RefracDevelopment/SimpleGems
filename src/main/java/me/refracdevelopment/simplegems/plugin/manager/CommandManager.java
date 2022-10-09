package me.refracdevelopment.simplegems.plugin.manager;

import me.refracdevelopment.simplegems.plugin.command.*;
import me.refracdevelopment.simplegems.plugin.command.commands.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class CommandManager {

    private static Set<Command> commands = new HashSet<>();

    public static void registerAll(){
        commands.addAll(Arrays.asList(
                new GemsCommand(),
                new GemShopCommand(),
                new GemsReloadCommand(),
                new GemsTopCommand(),
                new ConsoleGemsCommand()
        ));
    }

    public static void register(Command... command){
        commands.addAll(Arrays.asList(command));
    }

    public static Optional<Command> byCommand(String command){
        return commands.stream().filter(all -> {
            if(all.getName().equalsIgnoreCase(command)){
                return true;
            }else{
                for(String alias : all.getNameList()){
                    if(alias.equalsIgnoreCase(command)){
                        return true;
                    }
                }
                return false;
            }
        }).findFirst();
    }

}
