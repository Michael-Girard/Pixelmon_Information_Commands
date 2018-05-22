package com.sylcharin.pixelmoninfocommands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.IClientCommand;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

import java.net.URISyntaxException;
import java.util.*;

/**
 * @author Sylcharin
 */
@Mod(modid = "pixelmonspawninfo")
public class PixelmonSpawnInfo extends CommandBase implements IClientCommand{
    private final List<String> ALIASES;

    public PixelmonSpawnInfo() throws URISyntaxException {
        JSONHelper.getInstance();
        ALIASES = new ArrayList<>();
        ALIASES.add("spawns");
        ALIASES.add("Spawns");
        ALIASES.add("Spawninfo");
        ALIASES.add("SpawnInfo");
    }

    private StringBuilder getCommandResults(String[] args){
        //In this method, we'll be taking a set of tags and seeing what pixelmon match those tags
        //  First, we need to determine if two mandatory tags exist: pixelmon name or biome name
        //  A pixelmon name or biome name is mandatory.

        StringBuilder results = new StringBuilder();

        for (int index = 0; index < args.length; index++){
            args[index] = args[index].replaceAll("_", " ");
            args[index] = JSONHelper.formatTitleCase(args[index]);
        }

        HashMap<String, Enums.JSONTag> tagMap = new HashMap<>();
        try{
            Enums.populateTagTypes(tagMap, args);
        }
        catch(IllegalArgumentException ex){
            if (!ex.getMessage().isEmpty()){
                results.append("ERROR: The following arguments were not recognized: ").append(ex.getMessage());
                return results;
            }
            results.append(getUsage(null));
            return results;
        }

        TreeMap<String, Pixelmon> monMap = JSONHelper.getPixelmon();

        //If the tag supplied was of type "name", which is to say the name of a Pixelmon, we get that Pixelmon's
        // spawn information and return it.
        if (tagMap.values().contains(Enums.JSONTag.name)){
            tagMap.keySet().stream().forEach(key -> {
                if (monMap.keySet().contains(key)){
                    Pixelmon pixelmon = monMap.get(key);
                    TreeMap<String, Double> biomeRarities = new TreeMap<>();
                    results.append("----------\u00A7a").append(pixelmon.getId()).append("\u00A7f----------\n");

                    for (int index = 0; index < pixelmon.getInformation().size(); index++) {
                        HashMap<String, ArrayList<Object>> spawnSet = pixelmon.getInformation().get(index);

                        Object nearbyBlocks = ((HashMap) spawnSet).get(Enums.JSONTag.neededNearbyBlocks.toString());
                        Object locations = ((HashMap) spawnSet).get(Enums.JSONTag.stringLocationTypes.toString());
                        Object times = ((HashMap) spawnSet).get(Enums.JSONTag.times.toString());
                        Object weathers = ((HashMap) spawnSet).get(Enums.JSONTag.weathers.toString());
                        Object temperature = ((HashMap) spawnSet).get(Enums.JSONTag.temperature.toString());
                        Object minY = ((HashMap) spawnSet).get(Enums.JSONTag.minY.toString());
                        Object maxY = ((HashMap) spawnSet).get(Enums.JSONTag.maxY.toString());
                        Object level = ((HashMap) spawnSet).get(Enums.JSONTag.level.toString());
                        Object minLevel = ((HashMap) spawnSet).get(Enums.JSONTag.minLevel.toString());
                        Object maxLevel = ((HashMap) spawnSet).get(Enums.JSONTag.maxLevel.toString());
                        Object rarity = ((HashMap) spawnSet).get(Enums.JSONTag.rarity.toString());
                        Object anticondition = ((HashMap) spawnSet).get(Enums.JSONTag.antiCondition.toString());
                        Object neededStructure = ((HashMap) spawnSet).get(Enums.JSONTag.neededStructure.toString());
                        Object neededItem = ((HashMap) spawnSet).get(Enums.JSONTag.neededItem.toString());

                        if (!pixelmon.isNaturallySpawning() && neededStructure == null){
                            results.append("\u00A7c").append(args[0]).append(" does not spawn naturally.");
                            return;
                        }

                        if (spawnSet.get(Enums.JSONTag.stringBiomes.toString()) != null) {
                            String[] biomeArray = new String[]{spawnSet.get(Enums.JSONTag.stringBiomes.toString()).toString()};

                            biomeArray[0] = biomeArray[0].replaceAll(", ", ",").replaceAll("\\[", "").replaceAll("]+", "");
                            if (biomeArray[0].contains(",")) {
                                biomeArray = biomeArray[0].split(",");
                            }
                            for (String biome : biomeArray) {
                                String results2 = getCommandResults(new String[]{biome}).toString();
                                int pixelmonIndex = results2.indexOf(pixelmon.getId());
                                biomeRarities.put(JSONHelper.formatTitleCase(biome), Double.valueOf(results2.substring(results2.indexOf("(", pixelmonIndex) + 3, results2.indexOf(")", pixelmonIndex) - 3)));
                            }
                        } else if (neededStructure == null) {
                            for (String biome : Enums.BIOME_MAP.values()) {
                                String results2 = getCommandResults(new String[]{biome}).toString();
                                int pixelmonIndex = results2.indexOf(pixelmon.getId());
                                TreeMap<String, Double> tempRarities = new TreeMap<>();
                                tempRarities.put(biome, Double.valueOf(results2.substring(results2.indexOf("(", pixelmonIndex) + 3, results2.indexOf(")", pixelmonIndex) - 3)));
                                double accumulatedRarity = 0;
                                for (Double biomeRarity : tempRarities.values()) {
                                    accumulatedRarity += biomeRarity;
                                }
                                biomeRarities.put("All Biomes", accumulatedRarity / tempRarities.values().size());
                            }
                        }

                        if (minLevel != null && maxLevel != null) {
                            results.append("\u00A7aLevels\u00A7f: \u00A7e");
                            results.append(minLevel.toString(), 1, minLevel.toString().length() - 1).append(" \u00A7f-\u00A7e ");
                            results.append(maxLevel.toString(), 1, maxLevel.toString().length() - 1).append("\n");
                        } else {
                            results.append("\u00A7aLevel\u00A7f: \u00A7e");
                            results.append(level != null ? level.toString().substring(1, level.toString().length() - 1) + "\n" : "?");
                        }

                        if (neededStructure == null) {
                            results.append("\u00A7aBiomes\u00A7f: \u00A7e");
                            biomeRarities.keySet().forEach(biomeKey -> {
                                if (biomeKey.equals("All Biomes")) {
                                    results.append(biomeKey).append("\u00A7f (\u00A7bAverage: ").append(biomeRarities.get("All Biomes")).append("%\u00A7f)");
                                } else {
                                    results.append(biomeKey).append("\u00A7f (\u00A7b").append(biomeRarities.get(biomeKey)).append("%\u00A7f)");
                                }
                                results.append(", \u00A7e");
                            });
                            biomeRarities.clear();
                            results.delete(results.length() - 4, results.length());
                            results.append("\n");
                        }

                        results.append(neededStructure != null ? "\u00A7aStructure Needed: \u00A7f: \u00A7e" + neededStructure.toString().substring(1, neededStructure.toString().length() - 1) + "\n" : "");
                        results.append(neededItem != null ? "\u00A7aNeeded for Summon: \u00A7f: \u00A7e" + neededItem.toString().substring(1, neededItem.toString().length() - 1) + "\n" : "");
                        results.append(locations != null ? "\u00A7aLocations\u00A7f: \u00A7e" + locations.toString().substring(1, locations.toString().length() - 1) + "\n" : "");
                        results.append(nearbyBlocks != null ? "\u00A7aNear\u00A7f: \u00A7e" + nearbyBlocks.toString().substring(1, nearbyBlocks.toString().length() - 1) + "\n" : "");
                        results.append(times != null ? "\u00A7aTimes\u00A7f: \u00A7e" + times.toString().substring(1, times.toString().length() - 1) + "\n" : "");
                        results.append(weathers != null ? "\u00A7aWeathers\u00A7f: \u00A7e" + weathers.toString().substring(1, weathers.toString().length() - 1) + "\n" : "");
                        results.append(temperature != null ? "\u00A7aTemperature\u00A7f: \u00A7e" + temperature.toString().substring(1, temperature.toString().length() - 1) + "\n" : "");
                        results.append(minY != null ? "\u00A7aMin Height\u00A7f: \u00A7e" + minY.toString().substring(1, minY.toString().length() - 1) + "\n" : "");
                        results.append(maxY != null ? "\u00A7aMax Height\u00A7f: \u00A7e" + maxY.toString().substring(1, maxY.toString().length() - 1) + "\n" : "");
                        results.append(rarity != null ? "\u00A7aRarity\u00A7f: \u00A7e" + rarity.toString().substring(1, rarity.toString().length() - 1) + "\n" : "");
                        results.append(anticondition != null ? "\u00A7aAnti-Conditions\u00A7f: \u00A7e" + anticondition.toString().substring(1, anticondition.toString().length() - 1) + "\n" : "");

                        while (results.substring(results.length() - 1, results.length()).equals("\n")) {
                            results.deleteCharAt(results.length() - 1);
                        }

                        if (index + 1 < pixelmon.getInformation().size()) {
                            results.append("\n-----------\n");
                        }
                    }
                }
            });
            return results;
        }
        else {
            HashMap<Enums.JSONTag, Set<Pixelmon>> resultSets = new HashMap<>();

            tagMap.values().stream().forEach(tag -> {
                if (tag != null) resultSets.put(tag, new TreeSet<>());
            });

            //For each pixelmon...
            monMap.keySet().stream().forEach(pixelmon -> {
                Pixelmon currentPixelmon = monMap.get(pixelmon);
                if (currentPixelmon.isNaturallySpawning()) {
                    //For each set of spawn conditions for that pixelmon...
                    currentPixelmon.getInformation().stream().forEach(spawnConditions -> {
                        //For each type of condition in that set of spawn conditions...
                        //And for each type of JSONTag in the map of tags
                        ArrayList<String> tags = new ArrayList<>(tagMap.keySet());
                        for (int index = 0, matches = 0; index < tags.size(); index++) {
                            //If the spawnConditions contain that JSONTag AND the list of spawn conditions
                            //  corresponding contains the condition the user was searching
                            //  for, add that pixelmon to the resultSet under that JSONTag
                            //If no tag list exists, the pixelmon normally spawns in all of the tag, such
                            //  as all biomes, or in all weathers, so add it
                            //Finally, if the number of matches equals the number of tags, the pixelmon is
                            //  a perfect match to all supplied tags and can be added to the results list
                            ArrayList<Object> tagList = spawnConditions.get(tagMap.get(tags.get(index)).toString());
                            Enums.JSONTag currentJSONTag = tagMap.get(tags.get(index));

                            //If the tag supplied was a biome
                            if (tagList != null && currentJSONTag.equals(Enums.JSONTag.stringBiomes) && spawnConditions.containsKey(currentJSONTag.toString())) {
                                //Take the list of biomes in the spawn conditions and turn it into an array for easier searching
                                String[] biomes = new String[]{spawnConditions.get(tagMap.get(tags.get(index)).toString()).toString()};
                                biomes[0] = biomes[0].replaceAll(", ", ",").replaceAll("\\[", "").replaceAll("]+", "");
                                if (biomes[0].contains(",")) {
                                    biomes = biomes[0].split(",");
                                }
                                //Then we can look through that array of biomes to find any matches
                                for (String biome : biomes) {
                                    if (JSONHelper.formatTitleCase(biome).equals(tags.get(index))) {
                                        if (++matches >= tags.size()) {
                                            resultSets.get(currentJSONTag).add(monMap.get(pixelmon));
                                            matches = 0;
                                        }
                                    }
                                }
                            }
                            //If the tag supplied was not a biome...
                            else if (resultSets.get(tagMap.get(tags.get(index))) != null) {
                                //If the list of conditions isn't null and the conditions contain the
                                //  tag being searched for OR the condition doesn't exist (usually
                                //  meaning it spawns for all tag types), it's a match
                                if ((tagList != null && tagList.contains(tags.get(index))) || tagList == null) {
                                    if (++matches >= tags.size()) {
                                        resultSets.get(currentJSONTag).add(monMap.get(pixelmon));
                                        matches = 0;
                                    }
                                }
                            }
                        }
                    });
                }
            });

            //Now we need to take the pixelmon corresponding to each JSONTag and compile each
            //  of the resultSets into a single set
            Set<Pixelmon> pixelmonResults = new TreeSet<>();
            for (Enums.JSONTag key : resultSets.keySet()) {
                pixelmonResults.addAll(resultSets.get(key));
            }

            if (pixelmonResults.isEmpty()) {
                results.append("There were no results found for ").append(Arrays.toString(args)).append(".");
                return results;
            }
            else {

                //Now we can accumulate the rarity to calculate percentages
                double accumulatedRarity = 0;
                for (Pixelmon pixelmon : pixelmonResults) {
                    String rarityString = pixelmon.getInformation().get(0).get("rarity").toString();
                    rarityString = rarityString.substring(1, rarityString.length() - 1);
                    accumulatedRarity += Double.valueOf(rarityString);
                }

                //Use a StringBuilder to accumulate all of the arguments
                StringBuilder arguments = new StringBuilder();
                for (String arg : args) {
                    arguments.append("\"").append(arg).append("\"");
                    arguments.append(", ");
                }
                arguments.delete(arguments.length() - 2, arguments.length());

                final StringBuilder pixelmonList = new StringBuilder();
                pixelmonList.append("Pokemon corresponding to arguments \u00A7e").append(arguments.toString()).append("\u00A7f:\n ");

                //Now to turn the matches into a string
                int index = 0;
                for (Pixelmon pixelmon : pixelmonResults) {
                    String rarityString = pixelmon.getInformation().get(0).get("rarity").toString();
                    rarityString = rarityString.substring(1, rarityString.length() - 1);
                    Double relativeRarity = (Double.valueOf(rarityString) / accumulatedRarity) * 100;

                    pixelmonList.append("\u00A7a").append(pixelmon.getId()).append(" \u00A7f(\u00A7b").append(String.format("%.2f", relativeRarity)).append("%\u00A7f)");
                    pixelmonList.append(", ");
                    if (++index == 3) {
                        pixelmonList.append("\n ");
                        index = 0;
                    }
                }
                pixelmonList.delete(pixelmonList.length() - 2, pixelmonList.length()).append(".");
                return pixelmonList;
            }
        }
    }

    @Override
    public int compareTo(ICommand arg0) {
        return 0;
    }

    @Override
    public String getName() {
        return "Spawns";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/spawns [argument 1] [argument 2] ...\n" +
                "Arguments can be a Pixelmon name OR a group of tags containing biome names, weathers, or other pixelmon spawn conditions.\n" +
                "Names with spaces should be separated by underscores. Eg: \"Mesa_Plateau_M\"";
    }

    @Override
    public List<String> getAliases() {
        return this.ALIASES;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args){
        if (args.length == 0){
            sender.sendMessage(new TextComponentString(getUsage(sender)));
            return;
        }
        boolean errors = checkForErrors(sender);
        if (!errors) {
            StringBuilder results = getCommandResults(args);
            try {
                sender.sendMessage(new TextComponentString(results.toString()));
            } catch (NullPointerException ex) {
                System.out.println(results.toString());
            }
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
                                          BlockPos targetPos) {
        checkForErrors(sender);
        List<String> results = new ArrayList<>();

        if (args.length != 1){
            sender.sendMessage(new TextComponentString("Tab Completions are only functional for looking" +
                " up single Pixelmon names and cannot be used for multiple arguments."));
        }
        else  if (!args[0].trim().isEmpty()){
            String argument = JSONHelper.formatTitleCase(args[0]);

            //Grab the pixelmon map
            TreeMap<String, Pixelmon> monmap = JSONHelper.getPixelmon();

            monmap.keySet().stream().forEach(key -> {
                if (key.startsWith(argument)) {
                    results.add(key);
                }
            });

            if (results.isEmpty()){
                sender.sendMessage(new TextComponentString("Tab Completions found no results - " +
                        "tab completion only works for Pixelmon names."));
            }
        }

        return results;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @EventHandler
    public void init(FMLPostInitializationEvent event) throws URISyntaxException {
        ClientCommandHandler.instance.registerCommand(new PixelmonSpawnInfo());
    }

    @Override
    public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {
        return false;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    private boolean checkForErrors(ICommandSender sender){
        boolean error = false;
        //If there was an error parsing BetterSpawnerConfig, try again. If it fails again, return an error message.
        if (JSONHelper.betterSpawnerConfigError != null){
            error = true;
            try {
                JSONHelper.getInstance().parseBetterSpawnerConfig();
            }
            catch (URISyntaxException e) {
                e.printStackTrace();
            }
            if (JSONHelper.betterSpawnerConfigError != null){
                sender.sendMessage(new TextComponentString("Unable to parse BetterSpawnerConfig.json: " + JSONHelper.betterSpawnerConfigError));
                return error;
            }
        }
        //If there was an using the Pixelmon JAR, try again. If it fails again, return an error message.
        if (JSONHelper.pixelmonJarError != null){
            error = true;
            try {
                JSONHelper.getInstance().getPixelmonJar();
            }
            catch (URISyntaxException e) {
                e.printStackTrace();
            }
            if (JSONHelper.pixelmonJarError != null){
                sender.sendMessage(new TextComponentString("Unable to find or read the Pixelmon JAR file: " + JSONHelper.pixelmonJarError));
                return error;
            }
        }
        if (error){
            JSONHelper.buildJSONList();
            JSONHelper.buildPixelmonMap();
            JSONHelper.buildDropList();
            error = false;
        }
        return error;
    }
}

