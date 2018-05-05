package com.sylcharin.pixelmoninfocommands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Pixelmon implements Comparable{
    private String id;
    private Object[] spawnInfos;
    private List<HashMap<String, ArrayList<Object>>> information = new ArrayList<>();

    protected void init(){
        for (HashMap<String, ArrayList<Object>> informationSet : JSONHelper.parseSpawnInfos(spawnInfos)){
            information.add(informationSet);
        }
    }

    public List<HashMap<String, ArrayList<Object>>> getInformation() {
        return information;
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
