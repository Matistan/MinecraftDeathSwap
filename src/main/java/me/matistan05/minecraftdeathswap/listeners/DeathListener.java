package me.matistan05.minecraftdeathswap.listeners;

import me.matistan05.minecraftdeathswap.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import static me.matistan05.minecraftdeathswap.commands.DeathSwapCommand.*;

public class DeathListener implements Listener {
    Main main;
    public DeathListener(Main main) {
        this.main = main;
        Bukkit.getPluginManager().registerEvents(this, main);
    }
    @EventHandler
    public void DeathEvent(PlayerDeathEvent e) {
        Player p = e.getEntity();
        if(inGame && players.contains(p.getName())) {
            removePlayer(p.getName());
            playersMessage(ChatColor.DARK_RED + p.getName() + " died in " + round + " round" + (round == 1 ? "" : "s") + "!");
            if(players.size() >= 2) {
                playersMessage(ChatColor.DARK_RED + String.valueOf(players.size()) + " players left!");
            } else {
                playersMessage(ChatColor.DARK_AQUA + players.get(0) + " won!");
                reset();
            }
        }
    }
}