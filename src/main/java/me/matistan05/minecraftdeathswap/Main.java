package me.matistan05.minecraftdeathswap;

import me.matistan05.minecraftdeathswap.commands.DeathSwapCommand;
import me.matistan05.minecraftdeathswap.commands.DeathSwapCompleter;
import me.matistan05.minecraftdeathswap.listeners.DamageListener;
import me.matistan05.minecraftdeathswap.listeners.DeathListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginCommand("deathswap").setExecutor(new DeathSwapCommand(this));
        getCommand("deathswap").setTabCompleter(new DeathSwapCompleter());
        new DeathListener(this);
        new DamageListener(this);
    }
}