package me.matistan05.minecraftdeathswap.commands;

import me.matistan05.minecraftdeathswap.Main;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DeathSwapCommand implements CommandExecutor {
    public static BukkitTask game;
    public static ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
    public static Scoreboard scoreboard;
    static Objective objective;
    public static int seconds = 0;
    public static int time = 0;
    public static int round = 0;
    private static List<String> teleport = new ArrayList<>();
    public static boolean inGame = false;
    public static List<Boolean> ops = new ArrayList<>();
    public static List<Location> location = new ArrayList<>();
    public static List<String> players = new ArrayList<>();
    private static Main main;
    public DeathSwapCommand(Main main) {
        DeathSwapCommand.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player p = (Player) sender;
        if(args.length == 0) {
            p.sendMessage(ChatColor.RED + "You must type an argument. For help, type: /deathswap help");
            return true;
        }
        if(args[0].equals("help")) {
            if(args.length != 1)
            {
                p.sendMessage(ChatColor.RED + "Wrong usage of this command. For help, type: /deathswap help");
                return true;
            }
            p.sendMessage(ChatColor.GREEN + "------- " + ChatColor.WHITE + " Minecraft Death Swap " + ChatColor.GREEN + "----------");
            p.sendMessage(ChatColor.BLUE + "Here is a list of death swap commands:");
            p.sendMessage(ChatColor.YELLOW + "/deathswap add <player name>" + ChatColor.AQUA + "- adds a player to a death swap game");
            p.sendMessage(ChatColor.YELLOW + "/deathswap remove <player name> " + ChatColor.AQUA + "- removes a player from you death swap game");
            p.sendMessage(ChatColor.YELLOW + "/deathswap start " + ChatColor.AQUA + "- starts a death swap game");
            p.sendMessage(ChatColor.YELLOW + "/deathswap reset " + ChatColor.AQUA + "- deletes a death swap game");
            p.sendMessage(ChatColor.YELLOW + "/deathswap list " + ChatColor.AQUA + "- shows a list of players in death swap game");
            p.sendMessage(ChatColor.YELLOW + "/deathswap help " + ChatColor.AQUA + "- shows a list of death swap commands");
            p.sendMessage(ChatColor.GREEN + "----------------------------------");
            return true;
        }
        if(args[0].equals("list")) {
            if(args.length != 1)
            {
                p.sendMessage(ChatColor.RED + "Wrong usage of this command. For help, type: /deathswap help");
                return true;
            }
            if(players.size() == 0) {
                p.sendMessage(ChatColor.RED + "There is no player in your game!");
                return true;
            }
            p.sendMessage(ChatColor.GREEN + "------- " + ChatColor.WHITE + " Minecraft Death Swap " + ChatColor.GREEN + "----------");
            for (String player : players) {
                p.sendMessage(ChatColor.AQUA + player);
            }
            p.sendMessage(ChatColor.GREEN + "----------------------------------");
            return true;
        }
        if(args[0].equals("add")) {
            if(args.length != 2) {
                p.sendMessage(ChatColor.RED + "Wrong usage of this command. For help, type: /deathswap help");
                return true;
            }
            Player target = Bukkit.getPlayerExact(args[1]);
            if(target == null) {
                p.sendMessage(ChatColor.RED + "This player does not exist or he is offline");
                return true;
            }
            if(players.contains(target.getName())) {
                p.sendMessage(ChatColor.RED + "This player is already in a death swap game!");
                return true;
            }
            if(inGame) {
                p.sendMessage(ChatColor.RED + "The game has already started!");
                return true;
            }
            players.add(target.getName());
            p.sendMessage(ChatColor.AQUA + "Successfully added new player " + target.getName() + " to the game!");
            return true;
        }
        if(args[0].equals("remove")) {
            if(args.length != 2) {
                p.sendMessage(ChatColor.RED + "Wrong usage of this command. For help, type: /deathswap help");
                return true;
            }
            Player target = Bukkit.getPlayerExact(args[1]);
            if(target == null) {
                p.sendMessage(ChatColor.RED + "This player does not exist or he is offline");
                return true;
            }
            if (players.contains(target.getName())) {
                players.remove(target.getName());
                p.sendMessage(ChatColor.AQUA + "Successfully removed " + target.getName() + " from a game");
                return true;
            }
            p.sendMessage(ChatColor.RED + "This player is not in your deathswap game");
            return true;
        }
        if(args[0].equals("reset")) {
            if (args.length != 1) {
                p.sendMessage(ChatColor.RED + "Wrong usage of this command. For help, type: /deathswap help");
                return true;
            }
            p.sendMessage(ChatColor.AQUA + "Death Swap game has been reset!");
            reset();
            return true;
        }
        if(args[0].equals("start")) {
            if (args.length != 1) {
                p.sendMessage(ChatColor.RED + "Wrong usage of this command. For help, type: /deathswap help");
                return true;
            }
            if (players.size() == 0) {
                p.sendMessage(ChatColor.RED + "There are no players in your game!");
                return true;
            }
            if (players.size() == 1) {
                p.sendMessage(ChatColor.RED + "There must be at least 2 players!");
                return true;
            }
            if (inGame) {
                p.sendMessage(ChatColor.YELLOW + "The game has already started!");
                return true;
            }
            if(main.getConfig().getBoolean("timeSetDayOnStart")) {
                p.getWorld().setTime(0);
            }
            if(main.getConfig().getBoolean("weatherClearOnStart")) {
                p.getWorld().setStorm(false);
            }
            for (String t : players) {
                Player player = Bukkit.getPlayerExact(t);
                if(player == null) {continue;}
                if(main.getConfig().getBoolean("clearInventories")) {
                    player.getInventory().clear();
                }
                if(main.getConfig().getBoolean("takeAwayOps")) {
                    ops.add(player.isOp());
                    player.setOp(false);
                }
                player.setGameMode(GameMode.SURVIVAL);
                player.setHealth(20);
                player.setFoodLevel(20);
                player.setSaturation(20);
            }
            inGame = true;
            playersMessage(ChatColor.AQUA + "START!");
            time = main.getConfig().getInt("time");
            game = new BukkitRunnable() {
                @Override
                public void run() {
                    if(main.getConfig().getBoolean("scoreboard")) {
                        for(int i = 0; i < players.size(); i++) {
                            Player player = Bukkit.getPlayerExact(players.get(i));
                            if(player == null) {continue;}
                                scoreboard = scoreboardManager.getNewScoreboard();
                                objective = scoreboard.registerNewObjective("sb", "dummy", ChatColor.BLUE + "" + ChatColor.BOLD + "Death Swap");
                                objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                                Score timer = objective.getScore(ChatColor.YELLOW + "Time left: " + (time - (seconds % time)));
                                Score score2;
                                score2 = objective.getScore(ChatColor.BLUE + "Players left: " + players.size());
                                Score score = objective.getScore(ChatColor.AQUA + "Round: " + round);
                                timer.setScore(3);
                                score2.setScore(2);
                                score.setScore(1);
                                player.setScoreboard(scoreboard);
                        }
                    }
                    for(int i = 1; i <= 10; i++) {
                        if((seconds + i) % time == 0) {
                            playersTitle(i);
                            break;
                        }
                    }
                    if((seconds + 10) % time == 0) {
                        playersTitle(10);
                    } else if((seconds + 9) % time == 0) {
                        playersTitle(9);
                    } else if((seconds + 8) % time == 0) {
                        playersTitle(8);
                    } else if((seconds + 7) % time == 0) {
                        playersTitle(7);
                    } else if((seconds + 6) % time == 0) {
                        playersTitle(6);
                    } else if((seconds + 5) % time == 0) {
                        playersTitle(5);
                    } else if((seconds + 4) % time == 0) {
                        playersTitle(4);
                    } else if((seconds + 3) % time == 0) {
                        playersTitle(3);
                    } else if((seconds + 2) % time == 0) {
                        playersTitle(2);
                    } else if((seconds + 1) % time == 0) {
                        playersTitle(1);
                    } else if(seconds % time == 0 && seconds != 0) {
                        while(true) {
                            for(int i = 0; i < players.size(); i++) {
                                while(true) {
                                    Random random = new Random();
                                    teleport.add(players.get(random.nextInt(players.size())));
                                    if(different(teleport)) {
                                        break;
                                    }
                                    teleport.remove(teleport.size() - 1);
                                }
                            }
                            if(!toThemself()) {
                                break;
                            }
                            teleport.clear();
                        }
                        for(int i = 0; i < players.size(); i++) {
                            Player player = Bukkit.getPlayerExact(teleport.get(i));
                            if(player == null) {continue;}
                            location.add(player.getLocation());
                        }
                        for(int i = 0; i < players.size(); i++) {
                            Player player = Bukkit.getPlayerExact(players.get(i));
                            if(player == null) {continue;}
                            player.sendMessage(ChatColor.DARK_AQUA + "You have been teleported to " + teleport.get(i));
                            player.teleport(location.get(i));
                        }
                        teleport.clear();
                        location.clear();
                    }
                    seconds += 1;
                }
            }.runTaskTimer(main, 0, 20);
            return true;
        }
        p.sendMessage(ChatColor.RED + "Wrong argument. For help, type: /deathswap help");
        return true;
    }
    public static void reset() {
        if(inGame) {
            inGame = false;
            game.cancel();
            if(main.getConfig().getBoolean("scoreboard")) {
                objective.setDisplaySlot(null);
                for(String s : players) {
                    Player player = Bukkit.getPlayerExact(s);
                    if(player == null) {continue;}
                    player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
                }
            }
            if(main.getConfig().getBoolean("takeAwayOps")) {
                for (int i = 0; i < players.size(); i++) {
                    OfflinePlayer target = Bukkit.getOfflinePlayer(players.get(i));
                    target.setOp(ops.get(i));
                }
            }
        }
        round = 0;
        seconds = 0;
        ops.clear();
        players.clear();
    }
    private static boolean different(List<String> teleport) {
        for(int i = 0; i < teleport.size() - 1; i++) {
            for(int n = i + 1; n < teleport.size(); n++) {
                if(teleport.get(i).equals(teleport.get(n))) {
                    return false;
                }
            }
        }
        return true;
    }
    private static boolean toThemself() {
        for(int i = 0; i < players.size(); i++) {
            if(players.get(i).equals(teleport.get(i))) {
                return true;
            }
        }
        return false;
    }
    public static void playersMessage(String s) {
        for (String value : players) {
            Player player = Bukkit.getPlayerExact(value);
            if(player == null) {continue;}
            player.sendMessage(s);
        }
    }
    public static void playersTitle(int a) {
        for (String value : players) {
            Player player = Bukkit.getPlayerExact(value);
            if(player == null) {continue;}
            player.sendTitle(ChatColor.LIGHT_PURPLE + "" + a, ChatColor.DARK_GREEN + "" + a + " second" + (a == 1 ? "" : "s") + " before swap!", 0, 20, 10);
        }
    }
}