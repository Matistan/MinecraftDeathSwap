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
    public DeathListener(Main main) {
        Bukkit.getPluginManager().registerEvents(this, main);
    }
    @EventHandler
    public void DeathEvent(PlayerDeathEvent e) {
        Player p = e.getEntity();
        if(inGame && players.contains(p.getName())) {
            if(players.size() >= 3) {
                playersMessage(ChatColor.DARK_RED + p.getName() + " died!");
                playersMessage(ChatColor.DARK_RED + String.valueOf(players.size() - 1) + " players left!");
                if(ops.get(players.indexOf(p.getName()))) {
                    p.setOp(true);
                }
                ops.remove(players.indexOf(p.getName()));
            } else {
                playersMessage(ChatColor.DARK_RED + p.getName() + " died!");
                playersMessage(ChatColor.DARK_AQUA + players.get(Math.abs(players.indexOf(p.getName()) - 1)) + " won!");
                reset();
            }
            players.remove(p.getName());
        }
    }
}