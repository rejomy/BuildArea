package me.rejomy.buildarea.command;

import me.rejomy.buildarea.BuildArea;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class BuildAreaCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
        } else {
            switch (args[0].toLowerCase()) {
                case "help" -> {
                    Player player = null;

                    if (args.length == 2) {
                        String playerName = args[1];
                        player = Bukkit.getPlayer(playerName);
                    }

                    if (player == null) {
                        if (sender instanceof ConsoleCommandSender) {
                            sender.sendMessage(ChatColor.RED + "You should choose player name or player with selected name doesnt exists!");
                            sender.sendMessage(ChatColor.RED + "/ba build nick");
                        } else player = Bukkit.getPlayer(sender.getName());
                    }

                    if (player != null) {
                        boolean exists = BuildArea.getInstance().getUserManager().getWhitePlayerList().remove(player);
                        sender.sendMessage("Your build status is " + (!exists ? ChatColor.GREEN + " enabled " : ChatColor.RED + " disabled "));

                        if (!exists)
                            BuildArea.getInstance().getUserManager().getWhitePlayerList().add(player);
                    }
                }

                default -> sendHelp(sender);
            }
        }

        return false;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "");
        sender.sendMessage(ChatColor.GOLD + "BuildArea Command:");
        sender.sendMessage(ChatColor.GOLD + "/ba build [player] or yourself - let you enable or disable the build ability.");
        sender.sendMessage(ChatColor.GOLD + "");
    }
}
