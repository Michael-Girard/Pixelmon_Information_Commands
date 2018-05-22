package com.sylcharin.pixelmoninfocommands;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class JSONHelper {
    private static Gson gson = new Gson();
    private static JSONHelper instance = null;

    private final Path BETTER_SPAWNER_CONFIG_JSON = Paths.get(System.getProperty("user.dir") + "/config/pixelmon/BetterSpawnerConfig.json");
    private static BetterSpawnerConfig betterSpawnerConfig = null;

    private final Path MINECRAFT_MODS_PATH = Paths.get(System.getProperty("user.dir") + "/mods");
    private static final String LOCATION_OF_SPAWN_JSONS_IN_JAR = "assets/pixelmon/spawning/".toLowerCase();
    private static final String LOCATION_OF_DROP_JSONS_IN_JAR = "assets/pixelmon/drops/pokedrops.json".toLowerCase();
    private static final List<JarEntry> SPAWN_JSON_FILES = new ArrayList<>();
    private static JarEntry DROPS_JSON_FILES = null;
    private static JarFile pixelmonJar;

    protected static String betterSpawnerConfigError;
    protected static String pixelmonJarError;
    protected static String buildJSONListError;

    private static final TreeMap<String, Pixelmon> PIXELMON = new TreeMap<>();

    private JSONHelper() throws URISyntaxException {
        parseBetterSpawnerConfig();
        if (betterSpawnerConfigError == null) pixelmonJar = getPixelmonJar();
        if (pixelmonJarError == null) {
            buildJSONList();
            if (buildJSONListError == null) {
                buildPixelmonMap();
                buildDropList();
            }
        }
    }

    public static JSONHelper getInstance() throws URISyntaxException {
        if (instance == null){
            instance = new JSONHelper();
        }
        return instance;
    }

    public static BetterSpawnerConfig getConfig(){
        return betterSpawnerConfig;
    }

    public static TreeMap<String, Pixelmon> getPixelmon() { return PIXELMON; }

    protected void parseBetterSpawnerConfig(){
        //If BetterSpawnerConfig.json exists in the config/pixelmon folder, create a BetterSpawnerConfig object from it
        //Otherwise, put an error message inside of the string
        if (Files.exists(BETTER_SPAWNER_CONFIG_JSON)){
            try(JsonReader configReader = new JsonReader(new InputStreamReader(new FileInputStream(BETTER_SPAWNER_CONFIG_JSON.toFile()), "UTF-8"))){
                betterSpawnerConfig = new Gson().fromJson(configReader, BetterSpawnerConfig.class);
            }
            catch (IOException ex) {
                betterSpawnerConfigError = ex.getMessage();
                return;
            }
        }
        else{
            betterSpawnerConfigError = "ERROR: No BetterSpawnerConfig.json found in " + BETTER_SPAWNER_CONFIG_JSON.toAbsolutePath() + "!";
            return;
        }
        betterSpawnerConfigError = null;
    }

    protected JarFile getPixelmonJar(){
        try {
            List<Path> jarFileName = Files.list(MINECRAFT_MODS_PATH)
                    .filter(Files::isRegularFile)   //Filters out directories
                    .filter(file -> {
                        //Filters out small files that don't contain PIXELMON and end with .JAR
                        String fileName = file.getFileName().toString().toUpperCase();
                        return fileName.contains("PIXELMON") && fileName.endsWith(".JAR") && file.toFile().length() > 200000000;
                    })
                    .collect(Collectors.toList());  //Collects the remaining files in the list
            if (!jarFileName.isEmpty()){
                //If the stream found a file, return the first one - there should only be one in the directory anyway
                pixelmonJarError = null;
                return new JarFile(jarFileName.get(0).toFile());
            }
            else{
                pixelmonJarError = "ERROR: No Pixelmon Jar File Found in " + MINECRAFT_MODS_PATH.toAbsolutePath() + "!";
            }
        } catch (IOException ex) {
            pixelmonJarError = ex.getMessage();
        }
        return null;
    }

    protected static void buildJSONList(){
        //Opening the jar file
        //Stream isolates all of the pokemon spawn json files into a list of JarEntries
        pixelmonJar.stream()
                .filter(filePath -> {
                    String filePathString = filePath.getName().toLowerCase();
                    return (filePathString.startsWith(LOCATION_OF_SPAWN_JSONS_IN_JAR) && filePathString.endsWith(".json")) || filePathString.equals(LOCATION_OF_DROP_JSONS_IN_JAR);
                })
                .forEach(file -> {
                    if (file.getName().contains("pokedrops")){
                        DROPS_JSON_FILES = new JarEntry(file);
                    }
                    else {
                        SPAWN_JSON_FILES.add(file);
                    }
                });
    }

    protected static void buildPixelmonMap(){
        SPAWN_JSON_FILES.forEach((entry) -> {
            String pokemonName = entry.getName();
            if (!pokemonName.contains("fish")) {
                Pixelmon pixelmon;

                try(JsonReader reader = new JsonReader(new InputStreamReader(pixelmonJar.getInputStream(entry)))){
                    pixelmon = gson.fromJson(reader, Pixelmon.class);
                    pixelmon.init();
                    PIXELMON.put(pixelmon.getId(), pixelmon);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    protected static void buildDropList(){
        try (JsonReader reader = new JsonReader(new InputStreamReader(pixelmonJar.getInputStream(DROPS_JSON_FILES)))){
            Pixelmon currentPixelmon = null;
            Map<String, String> blockMap = Enums.BLOCK_MAP;
            reader.beginArray();    //Begins the array of pokemon entries
            while (reader.hasNext()) {
                reader.beginObject();
                while (reader.hasNext()) {
                    String currentName = reader.nextName();
                    String currentValue = reader.nextString();
                    if (currentName.equals("pokemon")) {
                        currentPixelmon = PIXELMON.get(currentValue);
                        if (currentPixelmon == null){
                            currentPixelmon = new Pixelmon();
                            currentPixelmon.setId(currentValue);
                            currentPixelmon.init();
                            PIXELMON.put(currentValue, currentPixelmon);
                        }
                    }
                    if (currentName.contains("drop")) {
                        currentValue = JSONHelper.formatTitleCase(currentValue);
                        if (blockMap.containsKey(currentValue)) currentValue = blockMap.get(currentValue);

                        if (currentName.equals("maindropdata")) {
                            currentPixelmon.getDrops()[Enums.DropType.valueOf("Normal").ordinal()].add(currentValue);
                        } else if (currentName.equals("raredropdata")) {
                            currentPixelmon.getDrops()[Enums.DropType.valueOf("Rare").ordinal()].add(currentValue);

                        } else if (currentName.startsWith("opt") && currentName.endsWith("data")) {
                            currentPixelmon.getDrops()[Enums.DropType.valueOf("Optional").ordinal()].add(currentValue);
                        }
                    }
                    else {
//                        reader.skipValue();
//                        reader.skipValue();
                    }
                }
                reader.endObject();
            }
            reader.endArray();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static HashMap<String, ArrayList<Object>>[] parseSpawnInfos(Object[] spawnInfos){
        HashMap<String, ArrayList<Object>>[] results = new HashMap[spawnInfos.length];

        for (int index = 0; index < spawnInfos.length; index++){
            results[index] = new HashMap<>();
            parseSpawnInfos(null, "spawnInfos", spawnInfos[index], results[index]);
        }

        return results;
    }

    private static void parseSpawnInfos(Object previousLevelName, Object levelName, Object dataStructure, HashMap<String, ArrayList<Object>> results) {
        //If the object passed in is a LinkedTreeMap, this method calls itself to walk down the structure

        if (dataStructure instanceof AbstractMap){
            AbstractMap<Object, Object> map = (AbstractMap) dataStructure;

            map.keySet().forEach(nextLevelName -> {
                Object nextDataStructure = map.get(nextLevelName);

                //if the next data structure is some sort of map, recursively call this method
                if (nextDataStructure instanceof ArrayList || nextDataStructure instanceof AbstractMap) {
                    parseSpawnInfos(levelName, nextLevelName, nextDataStructure, results);
                }
                //Otherwise, it's probably a data type that can be added right to the results
                else{
                    ArrayList<Object> values;
                    //Add the value to an existing arraylist if one exists at the key
                    if (results.containsKey(nextLevelName.toString())){
                        values = results.get(nextLevelName.toString());
                        values.add(formatTitleCase(nextDataStructure.toString()));
                    }

                    //Else create a new one
                    else{
                        values = new ArrayList<>();
                        values.add(formatTitleCase(nextDataStructure.toString()));
                        results.put(nextLevelName.toString(), values);
                    }
                }
            });
        }

        //If the object passed in is an ArrayList, we can get the data from the list
        else if (dataStructure instanceof List){
            List<Object> list = (List) dataStructure;

            list.stream().map(value -> {
                if (levelName.toString().equals("stringBiomes")){
                    //Convert a biome category into a set of biomes, changing those biomes to easily readable names
                    String biomeName = Enums.formatBiome(value.toString()).toString();
                    biomeName = biomeName.replaceAll("\\[", "").replaceAll("]", "");
                    value = biomeName;
                }
                return value;
            }).forEach(value -> {
                if (value instanceof String) {
                    String valueAsString = value.toString();
                    ArrayList<Object> values;

                    //Time to add the values to the results
                    //If the results already contained the type of value, add the value to the existing list
                    if (results.containsKey(levelName.toString())) {
                        if (previousLevelName.equals("anticondition")) {
                            //Anticonditions are added under their own header
                            //If it's an anticondition, check if that key exists (if so, add to the key, else make a new one)
                            if (results.containsKey(previousLevelName.toString())) {
                                values = results.get(previousLevelName.toString());
                            }
                            else{
                                values = new ArrayList<>();
                                values.add(formatTitleCase(value.toString()));
                                results.put(previousLevelName.toString(), values);
                                return;
                            }
                        } else {
                            //If it was a normal condition and the key existed, get the results
                            values = results.get(levelName.toString());
                        }
                        //Add the value to the existing condition or anticondition
                        values.add(formatTitleCase(valueAsString));
                    } else {
                        values = new ArrayList<>();
                        values.add(formatTitleCase(value.toString()));
                        results.put(levelName.toString(), values);
                    }
                }
            });
        }
    }

    public static String formatTitleCase(String target){
        //Remove things like "minecraft:" and "pixelmon:"
        if (target.contains(":")) {
            target = target.substring(target.indexOf(":") + 1);
        }

        target = target.replaceAll("_", " ");                       //Replace underscores with spaces
        target = target.toLowerCase();                                                //Lowercase all letters
        if (target.length() == 1){
            target = target.toUpperCase();
        }
        else if (target.length() > 1){
            target = target.substring(0, 1).toUpperCase() + target.substring(1);   //Capitalize first letter
        }

        //The following code converts the strings to Title Case
        if (target.contains(" ")) {
            String[] splitBySpace = target.split(" ");
            StringBuilder sb = new StringBuilder();
            for (int index = 0; index < splitBySpace.length; index++) {
                splitBySpace[index] = splitBySpace[index].substring(0, 1).toUpperCase() + splitBySpace[index].substring(1);
                sb.append(splitBySpace[index]);
                if (index + 1 != splitBySpace.length) {
                    sb.append(" ");
                }
            }
            target = sb.toString();
        }
        if (target.equals("Mimejr")) target = "MimeJr";   //Account for MimeJr edge case
        if (target.equals("Ho-oh")) target = "Ho-Oh";
        if (target.equals("Mesa (bryce)") || target.equals("Mesa Bryce") || target.equals("Bryce Mesa")) target = "Mesa (Bryce)";   //Another edge case
        return target;
    }

    public static String formatForSearching(String target){
        target = target.replaceAll(" ", "").replaceAll("_", "").toLowerCase();
        return target;
    }

    protected class BetterSpawnerConfig{
        private Object globalCompositeCondition;
        private Object intervalSeconds;
        private Object blockCategories;
        private Object biomeCategories;

        public Object getGlobalCompositeCondition() {
            return globalCompositeCondition;
        }

        public void setGlobalCompositeCondition(Object globalCompositeCondition) {
            this.globalCompositeCondition = globalCompositeCondition;
        }

        public Object getIntervalSeconds() {
            return intervalSeconds;
        }

        public void setIntervalSeconds(Object intervalSeconds) {
            this.intervalSeconds = intervalSeconds;
        }

        public Object getBlockCategories() {
            return blockCategories;
        }

        public void setBlockCategories(Object blockCategories) {
            this.blockCategories = blockCategories;
        }

        public Object getBiomeCategories() {
            return biomeCategories;
        }

        public void setBiomeCategories(Object biomeCategories) {
            this.biomeCategories = biomeCategories;
        }
    }
}
