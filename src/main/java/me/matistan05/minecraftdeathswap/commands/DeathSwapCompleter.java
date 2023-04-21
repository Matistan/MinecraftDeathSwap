package me.matistan05.minecraftdeathswap.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DeathSwapCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> list = new ArrayList<>();
        if(args.length == 1) {
            if(startsWith("add", args[0])) {
                list.add("add");
            }
            if(startsWith("remove", args[0])) {
                list.add("remove");
            }
            if(startsWith("start", args[0])) {
                list.add("start");
            }
            if(startsWith("reset", args[0])) {
                list.add("reset");
            }
            if(startsWith("list", args[0])) {
                list.add("list");
            }
            if(startsWith("help", args[0])) {
                list.add("help");
            }
            if(startsWith("timer", args[0])) {
                list.add("timer");
            }
            if(startsWith("swaptime", args[0])) {
                list.add("swaptime");
            }
        } else if(args.length == 2) {
            if(args[0].equals("add") || args[0].equals("remove")) {
                Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
                Bukkit.getOnlinePlayers().toArray(players);
                for (Player player : players) {
                    if (startsWith(player.getName(), args[1])) {
                        list.add(player.getName());
                    }
                }
            } else if(args[0].equals("timer")) {
                if(startsWith("true", args[1])) {
                    list.add("true");
                }
                if(startsWith("false", args[1])) {
                    list.add("false");
                }
            }
        }
        return list;
    }
    private boolean startsWith(String a, String b) {
        if(b.length() <= a.length()) {
            for(int i = 0; i < b.length(); i++) {
                if(b.toLowerCase().charAt(i) != a.toLowerCase().charAt(i)) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }
}