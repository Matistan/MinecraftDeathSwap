# Minecraft Death Swap

---

View on [Spigot](https://www.spigotmc.org/resources/death-swap.109401/) •
Inspired by [Dream](https://www.youtube.com/@dream) •
Download [here](https://github.com/Matistan/MinecraftDeathSwap/releases)

---

> **Having issues?** Feel free to report them on the [Issues tab](https://github.com/Matistan/MinecraftDeathSwap/issues). I'll be glad to hear your opinion about the plugin as well as extra features you would like me to add!

## Welcome to readme!

Hi! I just want to thank you for your interest in this plugin. I put a lot of effort into this project and I would really love someone to use it!

### Minecraft version

This plugin runs on a Minecraft version 1.16+.

## What is Death Swap?

Death Swap is a mini-game where after 5 minutes every player teleports to someone else. The goal is to kill someone after a teleportation.

## How to use it

- drag the .jar file from the [Release tab](https://github.com/Matistan/MinecraftDeathSwap/releases) to your plugins folder on your server.
- type `/deathswap start` to start the match!
- if you don't want to play with every player on the server, change the rule `playWithEveryone` to false, and choose the players using `/deathswap add` command

## Commands

- `/deathswap add <player> <player> ... ` - adds players
- `/deathswap add @a` - adds all players
- `/deathswap remove @a` - removes all players
- `/deathswap remove <player> <player> ... ` - removes players
- `/deathswap start` - starts a game
- `/deathswap reset` - resets a game
- `/deathswap list` - shows a list of players in a death swap game
- `/deathswap rules <rule> value(optional)` - changes some additional rules of the game (in config.yml)
- `/deathswap help` - shows a list of death swap commands

## Configuration Options

Use the command `/deathswap rules` or edit the `plugins/MinecraftDeathSwap/config.yml` file to change the following options:

| Key                 | Description                                                                                                       | Type    | recommended                                                   |
|---------------------|-------------------------------------------------------------------------------------------------------------------|---------|---------------------------------------------------------------|
| timeSetDayOnStart   | Set to true to set the time to day automatically when the game starts.                                            | boolean | true                                                          |
| weatherClearOnStart | Set to true to set the weather to clear automatically when the game starts.                                       | boolean | true                                                          |
| playWithEveryone    | Set to true to not have to use '/deathswap add' every time, and instead play with every player on the server      | boolean | true                                                          |
| takeAwayOps         | Set to true to take away OPs for the duration of the game.                                                        | boolean | true                                                          |
| clearInventories    | Set to true to clear players inventories when the game starts.                                                    | boolean | true                                                          |
| time                | Set the time for a swap (10sec - 3600sec).                                                                        | int     | 300                                                           |
| varyingTime         | Set the maximum time that can be added or subtracted from the time (time must be always between 10sec - 3600sec). | int     | 0                                                             |
| pvpEnabled          | Set to true to enable PvP during the match.                                                                       | boolean | false                                                         |
| scoreboard          | Set to true to show scoreboard with the timer.                                                                    | boolean | true                                                          |
| usePermissions      | Set to true to require users to have permission to use certain commands.                                          | boolean | false; true if you don't trust the people you're playing with |

## Permissions

If `usePermissions` is set to `true` in the `config.yml` file, players without ops will need the following permissions to use the commands:

| Permission          | Description                                               |
|---------------------|-----------------------------------------------------------|
| deathswap.deathswap | Allows the player to use all `/deathswap` commands.       |
| deathswap.add       | Allows the player to use the `/deathswap add` command.    |
| deathswap.remove    | Allows the player to use the `/deathswap remove` command. |
| deathswap.start     | Allows the player to use the `/deathswap start` command.  |
| deathswap.reset     | Allows the player to use the `/deathswap reset` command.  |
| deathswap.list      | Allows the player to use the `/deathswap list` command.   |
| deathswap.rules     | Allows the player to use the `/deathswap rules` command.  |
| deathswap.help      | Allows the player to use the `/deathswap help` command.   |

### Bugs & Issues

> **Having issues?** Feel free to report them on the [Issues tab](https://github.com/Matistan/MinecraftDeathSwap/issues). I'll be glad to hear your opinion about the plugin as well as extra features you would like me to add!


Made by [Matistan](https://github.com/Matistan)