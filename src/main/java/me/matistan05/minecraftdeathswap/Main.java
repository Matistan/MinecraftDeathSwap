package me.matistan05.minecraftdeathswap;

import me.matistan05.minecraftdeathswap.commands.DeathSwapCommand;
import me.matistan05.minecraftdeathswap.commands.DeathSwapCompleter;
import me.matistan05.minecraftdeathswap.listeners.DamageListener;
import me.matistan05.minecraftdeathswap.listeners.DeathListener;
import org.bukkit.plugin.java.JavaPlugin;

import static me.matistan05.minecraftdeathswap.commands.DeathSwapCommand.inGame;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginCommand("deathswap").setExecutor(new DeathSwapCommand(this));
        getCommand("deathswap").setTabCompleter(new DeathSwapCompleter(this));
        new DeathListener(this);
        new DamageListener(this);
        System.out.println("*********************************************************\n" +
                "Thank you for using this plugin! <3\n" +
                "Author: Matistan\n" +
                "If you enjoy this plugin, please rate it on spigotmc.org:\n" +
                "https://www.spigotmc.org/resources/death-swap.109401/\n" +
                "*********************************************************");
    }

    @Override
    public void onDisable() {
        if(inGame) {
            DeathSwapCommand.reset();
        }
    }
}