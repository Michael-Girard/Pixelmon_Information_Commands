# PixelmonInfoCommands
Commands for displaying pixelmon information in chat.

Usage of Spawn Information Command:  
/Spawns [argument 1] [argument 2] ...   
Arguments can be a Pixelmon name OR a group of tags containing biome names, weathers, and/or locations.  
Names with spaces should be separated by underscores. Eg: "Mesa_Plateau_M"  
  
Usage of Drop Information Command:  
/Drops [argument 1]  
Argument can be a Pixelmon name OR an item name, with spaces separated by underscores. Eg: "Ghost_Gem"
  
  
Known Issues:
1) Tab auto-completion only works for Pixelmon names. The algorithm for performing the auto-completions does not seem to play well with multiple arguments. Attempting to code this to complete biomes causes problems like turning "Extreme H" into "Extreme Extreme Hills" and such. Perhaps I'll work on this later. Maybe it's possible to override whatever method forge is using to change what the player has written in the chat box, if I can locate the method.
