# PixelmonInfoCommand
Command for displaying pixelmon information in chat.

Usage:
/spawninfo [argument 1] [argument 2] ... etc.  
Arguments can be a Pixelmon name OR a group of tags containing biome names, weathers, or other pixelmon spawn conditions.  
Names with spaces should be separated by underscores. Eg: "Mesa_Plateau_M"

Known Issues:

1) (Potentially Fixed on 1.2 - Needs Testing) The command does not work with technic because PixelmonInfoCommand looks for files in the vanilla minecraft folder. This will be fixed, but for now the workaround involves copying a couple of files into the vanilla minecraft folders. You'll need to run the mod at least once first so BetterSpawnerConfig.json is created, and then...

		a) Copy BetterSpawnerConfig.json from "%appdata%\roaming\.technic\modpacks\pixelmon-reforged\config\pixelmon" into "%appdata%\roaming\.minecraft\config\pixelmon". If the folders inside .minecraft do not exist, create them. Creating a shortcut instead may work, but it's untested.

		b) Copy the Pixelmon JAR file from "%appdata%\roaming\.technic\modpacks\pixelmon-reforged\mods" into "%appdata%\roaming\.minecraft\mods". If the mods folder inside .minecraft does not exist, create it. Creating a shortcut instead may work, but it's untested.

2) Tab auto-completion only works for Pixelmon names. The algorithm for performing the auto-completions does not seem to play well with multiple arguments. Attempting to code this to complete biomes causes problems like turning "Extreme H" into "Extreme Extreme Hills" and such. Perhaps I'll work on this later. Maybe it's possible to override whatever method forge is using to change what the player has written in the chat box, if I can locate the method.
