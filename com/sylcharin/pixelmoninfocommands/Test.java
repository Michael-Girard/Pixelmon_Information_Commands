package com.sylcharin.pixelmoninfocommands;

import java.net.URISyntaxException;

public class Test {
    public static void main(String[] args) throws URISyntaxException {
        PixelmonSpawnInfo PSI = new PixelmonSpawnInfo();
        if (JSONHelper.pixelmonJarError != null) System.out.println(JSONHelper.pixelmonJarError);
        if (JSONHelper.betterSpawnerConfigError != null) System.out.println(JSONHelper.betterSpawnerConfigError);

        PSI.execute(null, null, new String[]{"Boldore"});
    }
}
