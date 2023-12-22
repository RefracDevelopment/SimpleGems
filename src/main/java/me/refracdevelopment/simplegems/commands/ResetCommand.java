package me.refracdevelopment.simplegems.commands;

import com.mongodb.client.model.Filters;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.utilities.Permissions;
import me.refracdevelopment.simplegems.utilities.Tasks;
import me.refracdevelopment.simplegems.utilities.chat.Color;
import me.refracdevelopment.simplegems.utilities.command.SubCommand;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ResetCommand extends SubCommand {

    @Override
    public String getName() {
        return SimpleGems.getInstance().getCommands().RESET_COMMAND_NAME;
    }

    @Override
    public List<String> getAliases() {
        return SimpleGems.getInstance().getCommands().RESET_COMMAND_ALIASES;
    }

    @Override
    public String getDescription() {
        return SimpleGems.getInstance().getLocaleFile().getString("command-reset-description");
    }

    @Override
    public String getSyntax() {
        return "[player]";
    }

    @Override
    public void perform(CommandSender commandSender, String[] args) {
        if (!commandSender.hasPermission(Permissions.GEMS_RESET_COMMAND)) {
            Color.sendMessage(commandSender, "no-permission");
            return;
        }

        if (args.length == 1) {
            Tasks.runAsync(() -> {
                switch (SimpleGems.getInstance().getDataType()) {
                    case MONGO:
                        List<Document> documents = SimpleGems.getInstance().getMongoManager().getStatsCollection().find().into(new ArrayList<>());

                        documents.forEach(document -> {
                            SimpleGems.getInstance().getMongoManager().getStatsCollection().deleteMany(document);
                        });

                        Tasks.run(() -> {
                            Bukkit.getOnlinePlayers().forEach(player -> {
                                player.kickPlayer(Color.translate(player, SimpleGems.getInstance().getLocaleFile().getString("kick-messages-error")));
                            });
                        });
                        break;
                    case MYSQL:
                        SimpleGems.getInstance().getMySQLManager().delete();

                        Tasks.run(() -> {
                            Bukkit.getOnlinePlayers().forEach(player -> {
                                player.kickPlayer(Color.translate(player, SimpleGems.getInstance().getLocaleFile().getString("kick-messages-error")));
                            });
                        });
                        break;
                    case SQLITE:
                        SimpleGems.getInstance().getSqLiteManager().delete();

                        Tasks.run(() -> {
                            Bukkit.getOnlinePlayers().forEach(player -> {
                                player.kickPlayer(Color.translate(player, SimpleGems.getInstance().getLocaleFile().getString("kick-messages-error")));
                            });
                        });
                        break;
                    default:
                        Tasks.run(() -> {
                            Color.sendCustomMessage(commandSender, "This command is only available for MySQL, MariaDB, SQLite and MongoDB.");
                        });
                        break;
                }
            });
        } else if (args.length == 2) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

            if (target.getPlayer() != null && target.getPlayer().isOnline()) {
                Tasks.runAsync(() -> {
                    switch (SimpleGems.getInstance().getDataType()) {
                        case MONGO:
                            Document document = SimpleGems.getInstance().getMongoManager().getStatsCollection().find(Filters.eq("uuid", target.getPlayer().getUniqueId().toString())).first();

                            if (document != null) {
                                SimpleGems.getInstance().getMongoManager().getStatsCollection().deleteOne(document);

                                Tasks.run(() -> {
                                    Color.sendMessage(commandSender, "gems-reset-player");

                                    target.getPlayer().kickPlayer(Color.translate(target.getPlayer(), SimpleGems.getInstance().getLocaleFile().getString("kick-messages-error")));
                                });
                            }
                            break;
                        case MYSQL:
                            SimpleGems.getInstance().getMySQLManager().deletePlayer(target.getPlayer().getUniqueId());

                            Tasks.run(() -> {
                                Color.sendMessage(commandSender, "gems-reset-player");

                                target.getPlayer().kickPlayer(Color.translate(target.getPlayer(), SimpleGems.getInstance().getLocaleFile().getString("kick-messages-error")));
                            });
                            break;
                        case SQLITE:
                            SimpleGems.getInstance().getSqLiteManager().deletePlayer(target.getPlayer().getUniqueId());

                            Tasks.run(() -> {
                                Color.sendMessage(commandSender, "gems-reset-player");

                                target.getPlayer().kickPlayer(Color.translate(target.getPlayer(), SimpleGems.getInstance().getLocaleFile().getString("kick-messages-error")));
                            });
                            break;
                        default:
                            Tasks.run(() -> {
                                Color.sendCustomMessage(commandSender, "This command is only available for MySQL, MariaDB, SQLite and MongoDB.");
                            });
                            break;
                    }
                });
            } else if (target.hasPlayedBefore()) {
                switch (SimpleGems.getInstance().getDataType()) {
                    case MONGO:
                        Document document = SimpleGems.getInstance().getMongoManager().getStatsCollection().find(Filters.eq("uuid", target.getUniqueId().toString())).first();

                        if (document != null) {
                            SimpleGems.getInstance().getMongoManager().getStatsCollection().deleteOne(document);

                            Tasks.run(() -> {
                                Color.sendMessage(commandSender, "gems-reset-player");
                            });
                        }
                        break;
                    case MYSQL:
                        SimpleGems.getInstance().getMySQLManager().deletePlayer(target.getUniqueId());

                        Tasks.run(() -> {
                            Color.sendMessage(commandSender, "gems-reset-player");
                        });
                        break;
                    case SQLITE:
                        SimpleGems.getInstance().getSqLiteManager().deletePlayer(target.getUniqueId());

                        Tasks.run(() -> {
                            Color.sendMessage(commandSender, "gems-reset-player");
                        });
                        break;
                    default:
                        Tasks.run(() -> {
                            Color.sendCustomMessage(commandSender, "This command is only available for MySQL, MariaDB, SQLite and MongoDB.");
                        });
                        break;
                }
            }
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        List<String> names = new ArrayList<>();

        Bukkit.getOnlinePlayers().forEach(p -> {
            names.add(p.getName());
        });

        if (args.length == 2) {
            return names;
        }
        return null;
    }
}