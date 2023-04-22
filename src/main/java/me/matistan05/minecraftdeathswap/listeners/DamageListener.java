package me.matistan05.minecraftdeathswap.listeners;

import me.matistan05.minecraftdeathswap.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import static me.matistan05.minecraftdeathswap.commands.DeathSwapCommand.inGame;
import static me.matistan05.minecraftdeathswap.commands.DeathSwapCommand.players;

public class DamageListener implements Listener {
    Main main;
    public DamageListener(Main main) {
        this.main = main;
        Bukkit.getPluginManager().registerEvents(this, main);
    }
    @EventHandler
    public void DamageEvent(EntityDamageByEntityEvent e) {
        if(e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player p = (Player) e.getEntity();
            if(inGame && players.contains(p.getName()) && !main.getConfig().getBoolean("pvpEnabled")) {
                e.setCancelled(true);
            }
        }
    }
}