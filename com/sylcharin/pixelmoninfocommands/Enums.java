package com.sylcharin.pixelmoninfocommands;

import com.google.gson.internal.LinkedTreeMap;

import java.util.*;

public class Enums {
    static TreeMap<Biomes, String> BIOME_MAP = new TreeMap<>();
    static TreeMap<String, String> BLOCK_MAP = new TreeMap<>();

    static{
        buildBiomeMap();
        buildBlockMap();
    }

    private Enums(){}

    public static void populateTagTypes(HashMap<String, JSONTag> results, String[] args) throws IllegalArgumentException{
        Boolean error = false;
        TreeSet<String> unknownTags = null;

        for (String arg : args) {
            try {
                TreeMap<String, Pixelmon> monMap = JSONHelper.getPixelmon();
                if (monMap.keySet().stream().anyMatch(arg::equals)) {
                    if (results.values().contains(JSONTag.name)){
                        error = true;
                    }
                    if (results.values().contains(JSONTag.stringBiomes)){
                        error = true;
                    }
                    results.put(arg, JSONTag.name);
                    continue;
                }
            } catch (IllegalArgumentException ex) {            }

            try {
                if (BIOME_MAP.values().stream().anyMatch(arg::equals)) {
                    if (results.values().contains(JSONTag.name)){
                        error = true;
                    }
                    results.put(arg, JSONTag.stringBiomes);
                    continue;
                }
            } catch (IllegalArgumentException ex) {            }

            try {
                Times.valueOf(arg);
                if (results.values().contains(JSONTag.name)){
                    error = true;
                }
                results.put(arg, JSONTag.times);
                continue;
            } catch (IllegalArgumentException ex) {            }

            try {
                Temperatures.valueOf(arg);
                if (results.values().contains(JSONTag.name)){
                    error = true;
                }
                results.put(arg, JSONTag.temperature);
                continue;
            } catch (IllegalArgumentException ex) {            }

            try {
                Weathers.valueOf(arg);
                if (results.values().contains(JSONTag.name)){
                    error = true;
                }
                results.put(arg, JSONTag.weathers);
                continue;
            } catch (IllegalArgumentException ex) {            }

            try {
                Locations.valueOf(arg);
                if (results.values().contains(JSONTag.name)){
                    error = true;
                }
                results.put(arg, JSONTag.stringLocationTypes);
                continue;
            } catch (IllegalArgumentException ex) {            }
            results.put(arg, null);
            error = true;
            if (unknownTags == null) unknownTags = new TreeSet<>();
            unknownTags.add(arg);
        }
        if (error) throw new IllegalArgumentException((unknownTags == null) ? "" : unknownTags.toString());
    }

    protected static List<String> formatBiome(String biomeName){
        List<String> results = new ArrayList<>();

        if (biomeName != null){
            //Get a TreeMap containing all biome groupings with the name of the biome.
            Object biomeGroup = ((LinkedTreeMap)JSONHelper.getConfig().getBiomeCategories()).get(biomeName);

            if (biomeGroup != null){
                //If a grouping is found with that name add all of the biomes in that group to the list and return the list
                String[] biomeArray;
                String biomeGroupAsString = biomeGroup.toString();
                biomeGroupAsString = biomeGroupAsString.substring(1, biomeGroupAsString.length() - 1);
                biomeArray = biomeGroupAsString.split(", ");
                for (String biome : biomeArray){
                    if (biome.startsWith("minecraft:")){
                        String temp = biome.substring("minecraft:".length());
                        results.add(BIOME_MAP.get(Biomes.valueOf(temp)));
                    }
                }
            }
            else{
                //If a grouping was not found, it's a single minecraft biome. Format the string and return it.
                if (biomeName.startsWith("minecraft:")){
                    biomeName = biomeName.substring("minecraft:".length());
                }
                results.add(BIOME_MAP.get(Biomes.valueOf(biomeName)));
            }
        }
        return results;
    }

    protected static void buildBiomeMap(){
        BIOME_MAP.put(Biomes.beaches, "Beach");
        BIOME_MAP.put(Biomes.birch_forest, "Birch Forest");
        BIOME_MAP.put(Biomes.birch_forest_hills, "Birch Forest Hills");
        BIOME_MAP.put(Biomes.cold_beach, "Cold Beach");
        BIOME_MAP.put(Biomes.cold_deep_ocean, "Cold Deep Ocean");
        BIOME_MAP.put(Biomes.cold_ocean, "Cold Ocean");
        BIOME_MAP.put(Biomes.deep_ocean, "Deep Ocean");
        BIOME_MAP.put(Biomes.desert, "Desert");
        BIOME_MAP.put(Biomes.desert_hills, "Desert Hills");
        BIOME_MAP.put(Biomes.extreme_hills, "Extreme Hills");
        BIOME_MAP.put(Biomes.extreme_hills_with_trees, "Extreme Hills +");
        BIOME_MAP.put(Biomes.forest, "Forest");
        BIOME_MAP.put(Biomes.forest_hills, "Forest Hills");
        BIOME_MAP.put(Biomes.frozen_deep_ocean, "Frozen Deep Ocean");
        BIOME_MAP.put(Biomes.frozen_ocean, "Frozen Ocean");
        BIOME_MAP.put(Biomes.frozen_river, "Frozen River");
        BIOME_MAP.put(Biomes.hell, "The Nether");
        BIOME_MAP.put(Biomes.ice_flats, "Ice Plains");
        BIOME_MAP.put(Biomes.ice_mountains, "Ice Mountains");
        BIOME_MAP.put(Biomes.jungle, "Jungle");
        BIOME_MAP.put(Biomes.jungle_edge, "Jungle Edge");
        BIOME_MAP.put(Biomes.jungle_hills, "Jungle Hills");
        BIOME_MAP.put(Biomes.lukewarm_deep_ocean, "Lukewarm Deep Ocean");
        BIOME_MAP.put(Biomes.lukewarm_ocean, "Lukewarm Ocean");
        BIOME_MAP.put(Biomes.mesa, "Mesa");
        BIOME_MAP.put(Biomes.mesa_clear_rock, "Mesa Plateau");
        BIOME_MAP.put(Biomes.mesa_rock, "Mesa Plateau F");
        BIOME_MAP.put(Biomes.mushroom_island, "Mushroom Island");
        BIOME_MAP.put(Biomes.mushroom_island_shore, "Mushroom Island Shore");
        BIOME_MAP.put(Biomes.mutated_birch_forest, "Birch Forest M");
        BIOME_MAP.put(Biomes.mutated_birch_forest_hills, "Birch Forest Hills M");
        BIOME_MAP.put(Biomes.mutated_desert, "Desert M");
        BIOME_MAP.put(Biomes.mutated_extreme_hills, "Extreme Hills M");
        BIOME_MAP.put(Biomes.mutated_extreme_hills_with_trees, "Extreme Hills + M");
        BIOME_MAP.put(Biomes.mutated_forest, "Flower Forest");
        BIOME_MAP.put(Biomes.forest_hills, "Forest Hills M");
        BIOME_MAP.put(Biomes.mutated_ice_flats, "Ice Plains Spikes");
        BIOME_MAP.put(Biomes.mutated_jungle, "Jungle M");
        BIOME_MAP.put(Biomes.mutated_jungle_edge, "Jungle Edge M");
        BIOME_MAP.put(Biomes.mutated_mesa, "Mesa (Bryce)");
        BIOME_MAP.put(Biomes.mutated_mesa_clear_rock, "Mesa Plateau M");
        BIOME_MAP.put(Biomes.mutated_mesa_rock, "Mesa Plateau F M");
        BIOME_MAP.put(Biomes.mutated_plains, "Sunflower Plains");
        BIOME_MAP.put(Biomes.mutated_redwood_taiga, "Mega Taiga M");
        BIOME_MAP.put(Biomes.mutated_redwood_taiga_hills, "Mega Taiga Hills M");
        BIOME_MAP.put(Biomes.mutated_roofed_forest, "Roofed Forest M");
        BIOME_MAP.put(Biomes.mutated_savanna, "Savanna M");
        BIOME_MAP.put(Biomes.mutated_savanna_rock, "Savanna Plateau M");
        BIOME_MAP.put(Biomes.mutated_swampland, "Swampland M");
        BIOME_MAP.put(Biomes.mutated_taiga, "Taiga M");
        BIOME_MAP.put(Biomes.mutated_taiga_cold, "Cold Taiga M");
        BIOME_MAP.put(Biomes.ocean, "Ocean");
        BIOME_MAP.put(Biomes.plains, "Plains");
        BIOME_MAP.put(Biomes.redwood_taiga, "Mega Taiga");
        BIOME_MAP.put(Biomes.redwood_taiga_hills, "Mega Taiga Hills");
        BIOME_MAP.put(Biomes.river, "River");
        BIOME_MAP.put(Biomes.roofed_forest, "Roofed Forest");
        BIOME_MAP.put(Biomes.savanna, "Savanna");
        BIOME_MAP.put(Biomes.savanna_rock, "Savanna Plateau");
        BIOME_MAP.put(Biomes.sky, "The End");
        BIOME_MAP.put(Biomes.sky_island_barren, "The End Barren Island");
        BIOME_MAP.put(Biomes.sky_island_high, "The End High Island");
        BIOME_MAP.put(Biomes.sky_island_low, "The End Low Island");
        BIOME_MAP.put(Biomes.sky_island_medium, "The End Medium Island");
        BIOME_MAP.put(Biomes.smaller_extreme_hills, "Extreme Hills Edge");
        BIOME_MAP.put(Biomes.stone_beach, "Stone Beach");
        BIOME_MAP.put(Biomes.swampland, "Swampland");
        BIOME_MAP.put(Biomes.taiga, "Taiga");
        BIOME_MAP.put(Biomes.taiga_cold, "Cold Taiga");
        BIOME_MAP.put(Biomes.taiga_cold_hills, "Cold Taiga Hills");
        BIOME_MAP.put(Biomes.taiga_hills, "Taiga Hills");
        BIOME_MAP.put(Biomes.the_void, "The Void");
        BIOME_MAP.put(Biomes.warm_deep_ocean, "Warm Deep Ocean");
        BIOME_MAP.put(Biomes.warm_ocean, "Warm Ocean");
    }

    protected static void buildBlockMap(){
        BLOCK_MAP.put("Stone:1","Granite");
        BLOCK_MAP.put("Stone:2","Polished Granite");
        BLOCK_MAP.put("Stone:3","Diorite");
        BLOCK_MAP.put("Stone:4","Polished Diorite");
        BLOCK_MAP.put("Stone:5","Andesite");
        BLOCK_MAP.put("Stone:6","Polished Andesite");

        BLOCK_MAP.put("Sandstone:1","Chiseled Sandstone");
        BLOCK_MAP.put("Sandstone:2","Smooth Sandstone");

        BLOCK_MAP.put("Dirt:1","Coarse Dirt");
        BLOCK_MAP.put("Dirt:2","Podzol");

        BLOCK_MAP.put("Sapling","Oak Wood Sapling");
        BLOCK_MAP.put("Sapling:1","Spruce Sapling");
        BLOCK_MAP.put("Sapling:2","Birch Sapling");
        BLOCK_MAP.put("Sapling:3","Jungle Sapling");
        BLOCK_MAP.put("Sapling:4","Acacia Sapling");
        BLOCK_MAP.put("Sapling:5","Dark Oak Sapling");

        BLOCK_MAP.put("Log","Oak Wood");
        BLOCK_MAP.put("Log:1","Spruce Wood");
        BLOCK_MAP.put("Log:2","Birch Wood");
        BLOCK_MAP.put("Log:3","Jungle Wood");
        BLOCK_MAP.put("Log2","Acacia Wood");
        BLOCK_MAP.put("Log2:1","Dark Oak Wood");

        BLOCK_MAP.put("Stonebrick","Stone Bricks");
        BLOCK_MAP.put("Stonebrick:1","Mossy Stone Bricks");
        BLOCK_MAP.put("Stonebrick:2","Cracked Stone Bricks");
        BLOCK_MAP.put("Stonebrick:3","Chiseled Stone Bricks");

        BLOCK_MAP.put("Fish","Raw Fish");
        BLOCK_MAP.put("Fish:1","Raw Salmon");
        BLOCK_MAP.put("Fish:2","Clownfish");
        BLOCK_MAP.put("Fish:3","Pufferfish");

        BLOCK_MAP.put("Wool","White Wool");
        BLOCK_MAP.put("Wool:1","Orange Wool");
        BLOCK_MAP.put("Wool:2","Magenta Wool");
        BLOCK_MAP.put("Wool:3","Light Blue Wool");
        BLOCK_MAP.put("Wool:4","Yellow Wool");
        BLOCK_MAP.put("Wool:5","Lime Wool");
        BLOCK_MAP.put("Wool:6","Pink Wool");
        BLOCK_MAP.put("Wool:7","Gray Wool");
        BLOCK_MAP.put("Wool:8","Light Gray wool");
        BLOCK_MAP.put("Wool:9","Cyan Wool");
        BLOCK_MAP.put("Wool:10","Purple Wool");
        BLOCK_MAP.put("Wool:11","Blue Wool");
        BLOCK_MAP.put("Wool:12","Brown Wool");
        BLOCK_MAP.put("Wool:13","Green Wool");
        BLOCK_MAP.put("Wool:14","Red Wool");

        BLOCK_MAP.put("Leaves","Oak Leaves");
        BLOCK_MAP.put("Leaves:1","Spruce Leaves");
        BLOCK_MAP.put("Leaves:2","Birch Leaves");
        BLOCK_MAP.put("Leaves:3","Jungle Leaves");
        BLOCK_MAP.put("Leaves2","Acacia Leaves");
        BLOCK_MAP.put("Leaves2:1","Dark Oak Leaves");

        BLOCK_MAP.put("Dye","Ink Sack");
        BLOCK_MAP.put("Dye:2","Rose Red");
        BLOCK_MAP.put("Dye:3","Cactus Green");
        BLOCK_MAP.put("Dye:4","Coco Beans");
        BLOCK_MAP.put("Dye:5","Lapis Lazuli");
        BLOCK_MAP.put("Dye:6","Purple Dye");
        BLOCK_MAP.put("Dye:7","Cyan Dye");
        BLOCK_MAP.put("Dye:8","Light Gray Dye");
        BLOCK_MAP.put("Dye:9","Pink Dye");
        BLOCK_MAP.put("Dye:10","Lime Dye");
        BLOCK_MAP.put("Dye:11","Dandelion Yellow");
        BLOCK_MAP.put("Dye:12","Light Blue Dye");
        BLOCK_MAP.put("Dye:13","Magenta Dye");
        BLOCK_MAP.put("Dye:14","Orange Dye");
        BLOCK_MAP.put("Dye:15","Bone Meal");

        BLOCK_MAP.put("Yellow Flower","Dandelion");
        BLOCK_MAP.put("Red Flower","Poppy");
        BLOCK_MAP.put("Red Flower:1","Blue Orchid");
        BLOCK_MAP.put("Red Flower:2","Allium");
        BLOCK_MAP.put("Red Flower:3","Azure Bonnet");
        BLOCK_MAP.put("Red Flower:4","Red Tulip");
        BLOCK_MAP.put("Red Flower:5","Orange Tulip");
        BLOCK_MAP.put("Red Flower:6","White Tulip");
        BLOCK_MAP.put("Red Flower:7","Pink Tulip");
        BLOCK_MAP.put("Red Flower:8","Oxeye Daisy");

        BLOCK_MAP.put("Stained Hardened Clay","White Hardened Clay");
        BLOCK_MAP.put("Stained Hardened Clay:1","Orange Hardened Clay");
        BLOCK_MAP.put("Stained Hardened Clay:2","Magenta Hardened Clay");
        BLOCK_MAP.put("Stained Hardened Clay:3","Light Blue Hardened Clay");
        BLOCK_MAP.put("Stained Hardened Clay:4","Yellow Hardened Clay");
        BLOCK_MAP.put("Stained Hardened Clay:5","Lime Hardened Clay");
        BLOCK_MAP.put("Stained Hardened Clay:6","Pink Hardened Clay");
        BLOCK_MAP.put("Stained Hardened Clay:7","Gray Hardened Clay");
        BLOCK_MAP.put("Stained Hardened Clay:8","Light Gray Hardened Clay");
        BLOCK_MAP.put("Stained Hardened Clay:9","Cyan Hardened Clay");
        BLOCK_MAP.put("Stained Hardened Clay:10","Purple Hardened Clay");
        BLOCK_MAP.put("Stained Hardened Clay:11","Blue Hardened Clay");
        BLOCK_MAP.put("Stained Hardened Clay:12","Brown Hardened Clay");
        BLOCK_MAP.put("Stained Hardened Clay:13","Green Hardened Clay");
        BLOCK_MAP.put("Stained Hardened Clay:14","Red Hardened Clay");
        BLOCK_MAP.put("Stained Hardened Clay:15","Black Hardened Clay");

        BLOCK_MAP.put("Stained Glass Pane","White Stained Glass Pane");
        BLOCK_MAP.put("Stained Glass Pane:1","Orange Stained Glass Pane");
        BLOCK_MAP.put("Stained Glass Pane:2","Magenta Stained Glass Pane");
        BLOCK_MAP.put("Stained Glass Pane:3","Light Blue Stained Glass Pane");
        BLOCK_MAP.put("Stained Glass Pane:4","Yellow Stained Glass Pane");
        BLOCK_MAP.put("Stained Glass Pane:5","Lime Stained Glass Pane");
        BLOCK_MAP.put("Stained Glass Pane:6","Pink Stained Glass Pane");
        BLOCK_MAP.put("Stained Glass Pane:7","Gray Stained Glass Pane");
        BLOCK_MAP.put("Stained Glass Pane:8","Light Gray Stained Glass Pane");
        BLOCK_MAP.put("Stained Glass Pane:9","Cyan Stained Glass Pane");
        BLOCK_MAP.put("Stained Glass Pane:10","Purple Stained Glass Pane");
        BLOCK_MAP.put("Stained Glass Pane:11","Blue Stained Glass Pane");
        BLOCK_MAP.put("Stained Glass Pane:12","Brown Stained Glass Pane");
        BLOCK_MAP.put("Stained Glass Pane:13","Green Stained Glass Pane");
        BLOCK_MAP.put("Stained Glass Pane:14","Red Stained Glass Pane");
        BLOCK_MAP.put("Stained Glass Pane:15","Black Stained Glass Pane");

        BLOCK_MAP.put("Carpet","White Carpet");
        BLOCK_MAP.put("Carpet:1","Orange Carpet");
        BLOCK_MAP.put("Carpet:2","Magenta Carpet");
        BLOCK_MAP.put("Carpet:3","Light Blue Carpet");
        BLOCK_MAP.put("Carpet:4","Yellow Carpet");
        BLOCK_MAP.put("Carpet:5","Lime Carpet");
        BLOCK_MAP.put("Carpet:6","Pink Carpet");
        BLOCK_MAP.put("Carpet:7","Gray Carpet");
        BLOCK_MAP.put("Carpet:8","Light Gray Carpet");
        BLOCK_MAP.put("Carpet:9","Cyan Carpet");
        BLOCK_MAP.put("Carpet:10","Purple Carpet");
        BLOCK_MAP.put("Carpet:11","Blue Carpet");
        BLOCK_MAP.put("Carpet:12","Brown Carpet");
        BLOCK_MAP.put("Carpet:13","Green Carpet");
        BLOCK_MAP.put("Carpet:14","Red Carpet");
        BLOCK_MAP.put("Carpet:15","Black Carpet");

        BLOCK_MAP.put("Prismarine","Prismarine");
        BLOCK_MAP.put("Prismarine:1","Prismarine Bricks");
        BLOCK_MAP.put("Prismarine:2","Dark Prismarine");

        BLOCK_MAP.put("Double Plant","Sunflower");
        BLOCK_MAP.put("Double Plant:1","Lilac");
        BLOCK_MAP.put("Double Plant:2","Double Tallgrass");
        BLOCK_MAP.put("Double Plant:3","Large Fern");
        BLOCK_MAP.put("Double Plant:4","Rose Bush");
        BLOCK_MAP.put("Double Plant:5","Peony");

        BLOCK_MAP.put("Tallgrass","Dead Shrub");
        BLOCK_MAP.put("Tallgrass:1","Grass");
        BLOCK_MAP.put("Tallgrass:2","Fern");

        BLOCK_MAP.put("Double Stone Slab","Double Stone Slab");
        BLOCK_MAP.put("Double Stone Slab:1","Double Sandstone Slab");
        BLOCK_MAP.put("Double Stone Slab:2","Double Wooden Slab");
        BLOCK_MAP.put("Double Stone Slab:3","Double Cobblestone Slab");
        BLOCK_MAP.put("Double Stone Slab:4","Double Brick Slab");
        BLOCK_MAP.put("Double Stone Slab:5","Double Stone Brick Slab");
        BLOCK_MAP.put("Double Stone Slab:6","Double Nether Brick Slab");
        BLOCK_MAP.put("Double Stone Slab:7","Double Quartz Slab");

        BLOCK_MAP.put("Stone Slab","Stone Slab");
        BLOCK_MAP.put("Stone Slab:1","Sandstone Slab");
        BLOCK_MAP.put("Stone Slab:2","Wooden Slab");
        BLOCK_MAP.put("Stone Slab:3","Cobblestone Slab");
        BLOCK_MAP.put("Stone Slab:4","Brick Slab");
        BLOCK_MAP.put("Stone Slab:5","Stone Brick Slab");
        BLOCK_MAP.put("Stone Slab:6","Nether Brick Slab");
        BLOCK_MAP.put("Stone Slab:7","Quartz Slab");

        BLOCK_MAP.put("Double Wooden Slab","Double Oak Wood Slab");
        BLOCK_MAP.put("Double Wooden Slab:1","Double Spruce Wood Slab");
        BLOCK_MAP.put("Double Wooden Slab:2","Double Birch Wood Slab");
        BLOCK_MAP.put("Double Wooden Slab:3","Double Jungle Wood Slab");
        BLOCK_MAP.put("Double Wooden Slab:4","Double Acacia Wood Slab");
        BLOCK_MAP.put("Double Wooden Slab:5","Double Dark Oak Wood Slab");

        BLOCK_MAP.put("Wooden Slab","Oak Wood Slab");
        BLOCK_MAP.put("Wooden Slab:1","Spruce Wood Slab");
        BLOCK_MAP.put("Wooden Slab:2","Birch Wood Slab");
        BLOCK_MAP.put("Wooden Slab:3","Jungle Wood Slab");
        BLOCK_MAP.put("Wooden Slab:4","Acacia Wood Slab");
        BLOCK_MAP.put("Wooden Slab:5","Dark Oak Wood Slab");

        BLOCK_MAP.put("Quartz Block:1","Chiseled Quartz Block");
        BLOCK_MAP.put("Quartz Block:2","Pillar Quartz Block");

        BLOCK_MAP.put("Red Sandstone:1","Chiseled Red Sandstone");
        BLOCK_MAP.put("Red Sandstone:2","Smooth Red Sandstone");

        BLOCK_MAP.put("Chicken", "Raw Chicken");
        BLOCK_MAP.put("Golden Rail", "Powered Rail");
        BLOCK_MAP.put("Web", "Cobweb");
        BLOCK_MAP.put("Deadbush", "Dead Bush");
        BLOCK_MAP.put("Snow Layer", "Snow");
        BLOCK_MAP.put("Snow", "Snow Block");
        BLOCK_MAP.put("Reeds", "Sugar Cane");
        BLOCK_MAP.put("Waterlily", "Lily Pad");
        BLOCK_MAP.put("Double Stone Slab2", "Double Red Sandstone Slab");
        BLOCK_MAP.put("Stone Slab2", "Red Sandstone Slab");
        BLOCK_MAP.put("Rabbit", "Raw Rabbit");
        BLOCK_MAP.put("Beef", "Raw Beef");


        //Stopped at 250
    }

    enum JSONTag{
        antiCondition,
        condition,
        form,
        level,
        minLevel,
        minY,
        maxLevel,
        maxY,
        name,
        neededItem,
        neededNearbyBlocks,
        neededStructure,
        rarity,
        spawnInfos,
        spec,
        stringBiomes,
        stringLocationTypes,
        typeID,
        temperature,
        times,
        weathers
    }

    enum Times{
        Dawn,
        Day,
        Dusk,
        Night
    }

    enum Temperatures{
        Cold,
        Hot,
        Medium
    }

    enum Weathers{
        Clear,
        Storm,
        Rain
    }

    enum Locations{
        Land,
        Air,
        Water,
        Underground,
        Surface_Water,
        Seafloor
    }

    enum DropType{
        Normal,
        Rare,
        Optional
    }

    enum Biomes {
        ocean,
        deep_ocean,
        frozen_ocean,
        plains,
        mutated_plains,
        desert,
        desert_hills,
        mutated_desert,
        extreme_hills,
        smaller_extreme_hills,
        extreme_hills_with_trees,
        mutated_extreme_hills_with_trees,
        mutated_extreme_hills,
        forest,
        forest_hills,
        mutated_forest,
        birch_forest,
        birch_forest_hills,
        mutated_birch_forest,
        mutated_birch_forest_hills,
        roofed_forest,
        mutated_roofed_forest,
        taiga,
        taiga_hills,
        mutated_taiga,
        redwood_taiga,
        redwood_taiga_hills,
        mutated_redwood_taiga,
        mutated_redwood_taiga_hills,
        taiga_cold,
        taiga_cold_hills,
        mutated_taiga_cold,
        swampland,
        mutated_swampland,
        river,
        frozen_river,
        hell,
        sky,
        ice_flats,
        mutated_ice_flats,
        ice_mountains,
        mushroom_island,
        mushroom_island_shore,
        beaches,
        stone_beach,
        cold_beach,
        jungle,
        jungle_hills,
        jungle_edge,
        mutated_jungle,
        mutated_jungle_edge,
        savanna,
        savanna_rock,
        mutated_savanna,
        mutated_savanna_rock,
        mesa,
        mesa_rock,
        mesa_clear_rock,
        mutated_mesa,
        mutated_mesa_rock,
        mutated_mesa_clear_rock,

        sky_island_low,
        sky_island_medium,
        sky_island_high,
        sky_island_barren,
        warm_ocean,
        lukewarm_ocean,
        cold_ocean,
        warm_deep_ocean,
        lukewarm_deep_ocean,
        cold_deep_ocean,
        frozen_deep_ocean,
        the_void
    }
}
