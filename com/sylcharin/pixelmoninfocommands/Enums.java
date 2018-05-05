package com.sylcharin.pixelmoninfocommands;

import com.google.gson.internal.LinkedTreeMap;

import java.util.*;

public class Enums {
    static TreeMap<Biomes, String> BIOME_MAP = new TreeMap<>();

    static{
        buildBiomeMap();
    }

    private Enums(){}

    public static void populateTagTypes(HashMap<String, JSONTag> results, String[] tags) throws IllegalArgumentException{
        Boolean error = false;
        TreeSet<String> unknownTags = null;

        for (String tag : tags) {
            try {
                TreeMap<String, Pixelmon> monMap = JSONHelper.getPixelmon();
                if (monMap.keySet().stream().anyMatch(tag::equals)) {
                    if (results.values().contains(JSONTag.name)){
                        error = true;
                    }
                    if (results.values().contains(JSONTag.stringBiomes)){
                        error = true;
                    }
                    results.put(tag, JSONTag.name);
                    continue;
                }
            } catch (IllegalArgumentException ex) {            }

            try {
                if (BIOME_MAP.values().stream().anyMatch(tag::equals)) {
                    if (results.values().contains(JSONTag.name)){
                        error = true;
                    }
                    results.put(tag, JSONTag.stringBiomes);
                    continue;
                }
            } catch (IllegalArgumentException ex) {            }

            try {
                Times.valueOf(tag);
                if (results.values().contains(JSONTag.name)){
                    error = true;
                }
                results.put(tag, JSONTag.times);
                continue;
            } catch (IllegalArgumentException ex) {            }

            try {
                Temperatures.valueOf(tag);
                if (results.values().contains(JSONTag.name)){
                    error = true;
                }
                results.put(tag, JSONTag.temperature);
                continue;
            } catch (IllegalArgumentException ex) {            }

            try {
                Weathers.valueOf(tag);
                if (results.values().contains(JSONTag.name)){
                    error = true;
                }
                results.put(tag, JSONTag.weathers);
                continue;
            } catch (IllegalArgumentException ex) {            }

            try {
                Locations.valueOf(tag);
                if (results.values().contains(JSONTag.name)){
                    error = true;
                }
                results.put(tag, JSONTag.stringLocationTypes);
                continue;
            } catch (IllegalArgumentException ex) {            }
            results.put(tag, null);
            error = true;
            if (unknownTags == null) unknownTags = new TreeSet<>();
            unknownTags.add(tag);
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
        BIOME_MAP.put(Biomes.mutated_ice_flats, "Ice Spikes Plains");
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
        neededNearbyBlocks,
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
