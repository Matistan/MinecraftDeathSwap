package me.matistan05.minecraftdeathswap.commands;

import me.matistan05.minecraftdeathswap.Main;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class DeathSwapCommand implements CommandExecutor {
    private static BukkitTask gameStarted;
    private static BukkitTask starting;
    public static BukkitTask game;
    private static Scoreboard s = Bukkit.getScoreboardManager().getNewScoreboard();
    private static Objective o = s.registerNewObjective("timer", "dummy", ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Death Swap");
    Team score2 = s.registerNewTeam("time");
    Team score5 = s.registerNewTeam("players");
    private static int swap = 300;
    public static boolean timer = true;
    public static int timeSeconds = 0;
    public static int timeMinutes = 0;
    public static int timeHours = 0;
    private static List<String> teleport = new ArrayList<>();
    public static int seconds = 10;
    public static boolean inGame = false;
    public static List<Boolean> ops = new ArrayList<>();
    public static List<Location> location = new ArrayList<>();
    public static List<String> players = new ArrayList<>();
    private final Main main;
    public DeathSwapCommand(Main main) {
        this.main = main;
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
            p.sendMessage(ChatColor.YELLOW + "/deathswap swaptime <seconds> " + ChatColor.AQUA + "- change amount of seconds before swaps");
            p.sendMessage(ChatColor.YELLOW + "/deathswap start " + ChatColor.AQUA + "- starts a death swap game");
            p.sendMessage(ChatColor.YELLOW + "/deathswap reset " + ChatColor.AQUA + "- deletes a death swap game");
            p.sendMessage(ChatColor.YELLOW + "/deathswap list " + ChatColor.AQUA + "- shows a list of players in death swap game");
            p.sendMessage(ChatColor.YELLOW + "/deathswap timer <boolean>" + ChatColor.AQUA + "- choose the timer to be visible or not");
            p.sendMessage(ChatColor.YELLOW + "/deathswap help " + ChatColor.AQUA + "- shows a list of death swap commands");
            p.sendMessage(ChatColor.GREEN + "----------------------------------");
            return true;
        }
        if(args[0].equals("swaptime")) {
            if(args.length != 2) {
                p.sendMessage(ChatColor.RED + "Wrong usage of this command. For help, type: /deathswap help");
                return true;
            }
            if(!isANumber(args[1])) {
                p.sendMessage(ChatColor.RED + "This is not a valid number. For help, type: /deathswap help");
                return true;
            }
            if(Integer.parseInt(args[1]) < 10) {
                p.sendMessage(ChatColor.RED + "You must set at least 10 seconds!");
                return true;
            }
            if(Integer.parseInt(args[1]) > 3600) {
                p.sendMessage(ChatColor.RED + "You can't set more than an hour!");
                return true;
            }
            p.sendMessage(ChatColor.AQUA + "Changed the time before swaps!");
            swap = Integer.parseInt(args[1]);
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
            for(int i = 0; i < players.size(); i++) {
                p.sendMessage(ChatColor.AQUA + players.get(i));
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
        if(args[0].equals("timer")) {
            if (args.length != 2) {
                p.sendMessage(ChatColor.RED + "Wrong usage of this command. For help, type: /deathswap help");
                return true;
            }
            if(args[1].equals("true")) {
                if(timer) {
                    p.sendMessage(ChatColor.RED + "Timer has already been set to be visible!");
                    return true;
                }
                p.sendMessage(ChatColor.AQUA + "Timer will be visible!");
                timer = true;
                return true;
            }
            if(args[1].equals("false")) {
                if(!timer) {
                    p.sendMessage(ChatColor.RED + "Timer has already been set to be invisible!");
                    return true;
                }
                p.sendMessage(ChatColor.AQUA + "Timer is invisible!");
                timer = false;
                return true;
            }
            p.sendMessage(ChatColor.RED + "Wrong argument. For help, type: /deathswap help");
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
            Player tar;
            for (int i = 0; i < players.size(); i++) {
                tar = Bukkit.getPlayerExact(players.get(i));
                tar.getInventory().clear();
                tar.setGameMode(GameMode.SURVIVAL);
                if (tar.isOp()) {
                    ops.add(true);
                } else {
                    ops.add(false);
                }
                tar.setOp(false);
                tar.setHealth(20);
                tar.setFoodLevel(20);
                tar.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 255));
                Iterator<Advancement> advancements = Bukkit.getServer().advancementIterator();
                while (advancements.hasNext()) {
                    AdvancementProgress progress = tar.getAdvancementProgress(advancements.next());
                    for (String s : progress.getAwardedCriteria())
                        progress.revokeCriteria(s);
                }
            }
            inGame = true;
            starting = new BukkitRunnable() {
                @Override
                public void run() {
                    if(!inGame) {
                        this.cancel();
                    }
                    for(int i = 0; i < players.size(); i++) {
                        Player player = Bukkit.getPlayerExact(players.get(i));
                        player.sendMessage(ChatColor.BLUE + String.valueOf(seconds) + " seconds remaining!");
                        player.sendTitle(ChatColor.DARK_PURPLE + String.valueOf(seconds), "", 0, 20, 10);
                        player.setLevel(seconds);
                        player.setExp((float) seconds/10);
                    }
                    seconds -= 1;
                }
            }.runTaskTimerAsynchronously(main, 0, 20);
            gameStarted = new BukkitRunnable() {
                @Override
                public void run() {
                    for(int i = 0; i < players.size(); i++) {
                        Player player = Bukkit.getPlayerExact(players.get(i));
                        player.sendMessage(ChatColor.AQUA + "START!");
                        player.sendTitle(ChatColor.DARK_PURPLE + "START!", "", 0, 20, 10);
                        player.setLevel(0);
                        player.setExp((float) 0);
                    }
                    starting.cancel();
                    seconds = 10;
                    if(timer) {
                        o.setDisplaySlot(DisplaySlot.SIDEBAR);
                        o.getScore(ChatColor.YELLOW + "" + ChatColor.BOLD + "TIME:").setScore(4);
                        score2.addEntry(ChatColor.LIGHT_PURPLE + ":");
                        score2.setSuffix("");
                        score2.setPrefix("");
                        o.getScore(ChatColor.LIGHT_PURPLE + ":").setScore(3);
                        o.getScore("").setScore(2);
                        o.getScore(ChatColor.BLUE + "" + ChatColor.BOLD + "Players Alive:").setScore(1);
                        score5.addEntry(ChatColor.LIGHT_PURPLE + "");
                        score5.setPrefix("");
                        score5.setSuffix("");
                        o.getScore(ChatColor.LIGHT_PURPLE + "").setScore(0);
                        game = new BukkitRunnable() {
                            @Override
                            public void run() {
                                score5.setSuffix(ChatColor.LIGHT_PURPLE + String.valueOf(players.size()));
                                if(timeHours >= 10) {
                                    score2.setPrefix(ChatColor.LIGHT_PURPLE + String.valueOf(timeHours));
                                } else {
                                    score2.setPrefix(ChatColor.LIGHT_PURPLE + "0" + timeHours);
                                }
                                if(timeMinutes >= 10) {
                                    if(timeSeconds >= 10) {
                                        score2.setSuffix(ChatColor.LIGHT_PURPLE + String.valueOf(timeMinutes) + ":" + timeSeconds);
                                    } else {
                                        score2.setSuffix(ChatColor.LIGHT_PURPLE + String.valueOf(timeMinutes) + ":0" + timeSeconds);
                                    }
                                } else {
                                    if(timeSeconds >= 10) {
                                        score2.setSuffix(ChatColor.LIGHT_PURPLE + "0" + timeMinutes + ":" + timeSeconds);
                                    } else {
                                        score2.setSuffix(ChatColor.LIGHT_PURPLE + "0" + timeMinutes + ":0" + timeSeconds);
                                    }
                                }
                                if((timeHours * 3600 + timeMinutes * 60 + timeSeconds + 10) % swap == 0) {
                                    playersTitle(10);
                                } else if((timeHours * 3600 + timeMinutes * 60 + timeSeconds + 9) % swap == 0) {
                                    playersTitle(9);
                                } else if((timeHours * 3600 + timeMinutes * 60 + timeSeconds + 8) % swap == 0) {
                                    playersTitle(8);
                                } else if((timeHours * 3600 + timeMinutes * 60 + timeSeconds + 7) % swap == 0) {
                                    playersTitle(7);
                                } else if((timeHours * 3600 + timeMinutes * 60 + timeSeconds + 6) % swap == 0) {
                                    playersTitle(6);
                                } else if((timeHours * 3600 + timeMinutes * 60 + timeSeconds + 5) % swap == 0) {
                                    playersTitle(5);
                                } else if((timeHours * 3600 + timeMinutes * 60 + timeSeconds + 4) % swap == 0) {
                                    playersTitle(4);
                                } else if((timeHours * 3600 + timeMinutes * 60 + timeSeconds + 3) % swap == 0) {
                                    playersTitle(3);
                                } else if((timeHours * 3600 + timeMinutes * 60 + timeSeconds + 2) % swap == 0) {
                                    playersTitle(2);
                                } else if((timeHours * 3600 + timeMinutes * 60 + timeSeconds + 1) % swap == 0) {
                                    playersTitle(1);
                                } else if((timeHours * 3600 + timeMinutes * 60 + timeSeconds) % swap == 0 && timeHours + timeMinutes + timeSeconds != 0) {
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
                                        location.add(Bukkit.getPlayerExact(teleport.get(i)).getLocation());
                                    }
                                    for(int i = 0; i < players.size(); i++) {
                                        Player player = Bukkit.getPlayerExact(players.get(i));
                                        player.sendMessage(ChatColor.DARK_AQUA + "You have been teleported to " + teleport.get(i));
//                                        location.get(i).getChunk().load();
                                        player.teleport(location.get(i));
                                    }
                                    teleport.clear();
                                    location.clear();
                                }
                                timeSeconds += 1;
                                if(timeSeconds == 60) {
                                    timeSeconds = 0;
                                    timeMinutes += 1;
                                    if(timeMinutes == 60) {
                                        timeMinutes = 0;
                                        timeHours += 1;
                                    }
                                }
                                for(int i = 0; i < players.size(); i++) {
                                    Player target = Bukkit.getPlayerExact(players.get(i));
                                    target.setScoreboard(s);
                                }
                            }
                        }.runTaskTimer(main, 0, 20);
                    }
                }
            }.runTaskLater(main, 200);
            return true;
        }
        p.sendMessage(ChatColor.RED + "Wrong argument. For help, type: /deathswap help");
        return true;
    }
    public static void reset() {
        if(inGame) {
            if(seconds != 10) {
                starting.cancel();
                gameStarted.cancel();
            } else {
                game.cancel();
            }
        }
        if(inGame && timer && seconds == 10) {
            o.setDisplaySlot(null);
        }
        timeSeconds = 0;
        timeMinutes = 0;
        timeHours = 0;
        if(inGame) {
            for(int i = 0; i < players.size(); i++) {
                Player tar = Bukkit.getPlayerExact(players.get(i));
                if(ops.get(i)) {
                    tar.setOp(true);
                }
            }
        }
        ops.clear();
        players.clear();
        inGame = false;
        seconds = 10;
    }
    private static boolean isANumber(String s) {
        for(int i = 0; i < s.length(); i++) {
            if(s.charAt(i) != '0' && s.charAt(i) != '1' && s.charAt(i) != '2' && s.charAt(i) != '3' && s.charAt(i) != '4' && s.charAt(i) != '5' && s.charAt(i) != '6' && s.charAt(i) != '7' && s.charAt(i) != '8' && s.charAt(i) != '9') {
                return false;
            }
        }
        return true;
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
            player.sendMessage(s);
        }
    }
    public static void playersTitle(int a) {
        for (String value : players) {
            Player player = Bukkit.getPlayerExact(value);
            player.sendTitle(ChatColor.LIGHT_PURPLE + "" + a, ChatColor.DARK_GREEN + "" + a + " seconds before swap!", 0, 20, 10);
        }
    }
}