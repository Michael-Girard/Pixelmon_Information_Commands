package com.sylcharin.pixelmoninfocommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Pixelmon implements Comparable{
    private boolean naturallySpawning = false;
    private String id;
    private Object[] spawnInfos;
    private List<HashMap<String, ArrayList<Object>>> information = new ArrayList<>();
    private List<String>[] drops = new ArrayList[Enums.DropType.values().length];

    protected void init(){
        if (spawnInfos != null) {
            setNaturallySpawning(true);
            information.addAll(Arrays.asList(JSONHelper.parseSpawnInfos(spawnInfos)));
        }
        else{
            HashMap<String, ArrayList<Object>> spawnInfo = new HashMap<>();
            ArrayList<Object> levelObject = new ArrayList<>();
            levelObject.add("70");
            ArrayList<Object> structureObject = new ArrayList<>();
            ArrayList<Object> itemObject = new ArrayList<>();
            if (id.equals("Articuno")){
                structureObject.add("Frozen Shrine");
                itemObject.add("Orb of Frozen Souls");
                spawnInfo.put(Enums.JSONTag.neededStructure.toString(), structureObject);
                spawnInfo.put(Enums.JSONTag.neededItem.toString(), itemObject);
                spawnInfo.put(Enums.JSONTag.level.toString(), levelObject);
            }
            else if (id.equals("Zapdos")){
                structureObject.add("Static Shrine");
                itemObject.add("Orb of Static Souls");
                spawnInfo.put(Enums.JSONTag.neededStructure.toString(), structureObject);
                spawnInfo.put(Enums.JSONTag.neededItem.toString(), itemObject);
                spawnInfo.put(Enums.JSONTag.level.toString(), levelObject);
            }
            else if (id.equals("Moltres")){
                structureObject.add("Fiery Shrine");
                itemObject.add("Orb of Fiery Souls");
                spawnInfo.put(Enums.JSONTag.neededStructure.toString(), structureObject);
                spawnInfo.put(Enums.JSONTag.neededItem.toString(), itemObject);
                spawnInfo.put(Enums.JSONTag.level.toString(), levelObject);
            }
            else if (id.equals("Mewtwo")){
                structureObject.add("Cloning Machine");
                itemObject.add("Ditto");
                spawnInfo.put(Enums.JSONTag.neededStructure.toString(), structureObject);
                spawnInfo.put(Enums.JSONTag.neededItem.toString(), itemObject);
                spawnInfo.put(Enums.JSONTag.level.toString(), levelObject);
            }
            else if (id.equals("Dialga") || id.equals("Palkia") || id.equals("Giratina")){
                structureObject.add("Timespace Altar");
                itemObject.add("Red Chain");
                if (id.equals("Dialga")) itemObject.add("Adamant Orb");
                if (id.equals("Palkia")) itemObject.add("Lustrous Orb");
                if (id.equals("Giratina")) itemObject.add("Griseous Orb");
                spawnInfo.put(Enums.JSONTag.neededStructure.toString(), structureObject);
                spawnInfo.put(Enums.JSONTag.neededItem.toString(), itemObject);
                spawnInfo.put(Enums.JSONTag.level.toString(), levelObject);
            }
            information.add(spawnInfo);
        }
        for (int index = 0; index < Enums.DropType.values().length; index++) {
            drops[index] = new ArrayList<>();
        }
    }

    public List<HashMap<String, ArrayList<Object>>> getInformation() {
        return information;
    }

    public boolean isNaturallySpawning(){
        return naturallySpawning;
    }

    public void setNaturallySpawning(boolean bool){
        this.naturallySpawning = bool;
    }

    public void setInformation(List<HashMap<String, ArrayList<Object>>> information) {
        this.information = information;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object[] getSpawnInfo() {
        return spawnInfos;
    }

    public void setSpawnInfo(Object[] spawnInfos) {
        this.spawnInfos = spawnInfos;
    }

    public void addDrop(String drop, int rarity){
        drops[rarity].add(drop);
    }

    public List[] getDrops(){
        return drops;
    }

    public void setDrops(List<String>[] newDrops){
        drops = newDrops;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(id);
        sb.append("\n");
        for (Object o : spawnInfos){
            sb.append(o.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Pixelmon){
            return getId().compareTo(((Pixelmon) o).getId());
        }
        else{
            return 0;
        }
    }
}
