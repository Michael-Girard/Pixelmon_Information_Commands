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
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author Sylcharin
 */
@Mod(modid = "pixelmondropinfo")
public class PixelmonDropInfo extends CommandBase implements IClientCommand{
    private final List<String> ALIASES;

    public PixelmonDropInfo() throws URISyntaxException {
        JSONHelper.getInstance();
        ALIASES = new ArrayList<>();
        ALIASES.add("drops");
        ALIASES.add("Drops");
        ALIASES.add("Dropinfo");
        ALIASES.add("DropInfo");
    }

    private StringBuilder getCommandResults(String[] args, ICommandSender sender){
        StringBuilder dropInfo = new StringBuilder();
        TreeMap<String, Pixelmon> monMap = JSONHelper.getPixelmon();
        String argument = JSONHelper.formatTitleCase(args[0]);



        //Check to see if the argument a pixelmon
        monMap.values().stream().filter(pixelmon -> {
            if (pixelmon.getId().equals(argument)) {
                return true;
            }
            return false;
        }).forEach(pixelmon -> {
            //If it is, we can get all of the drops for that pixelmon
            List<String>[] dropList = pixelmon.getDrops();
            Enums.DropType[] dropTypes = Enums.DropType.values();

            dropInfo.append("-----------\n\u00A7a").append(pixelmon.getId()).append("\u00A7f\n-----------\n");
            for (int index = 0; index < dropList.length; index++) {
                if (!dropList[index].isEmpty()) {
                    String dropType = dropTypes[index].toString();
                    dropInfo.append(String.format("%1$20s", "\u00A7a" + dropType + " drops:\u00A7e "));
                    for (int index2 = 0; index2 < dropList[index].size(); index2++) {
                        dropInfo.append(dropList[index].get(index2));
                        if (index2 + 1 != dropList[index].size()) dropInfo.append(", ");
                    }
                    dropInfo.append("\u00A7f\n");
                }
            }
        });

        if (!dropInfo.toString().isEmpty()) return dropInfo;

        TreeSet<String> matchSet = new TreeSet<>();
        monMap.values().stream().forEach(pixelmon -> {
            for (List<String> dropList : pixelmon.getDrops()){
                dropList.stream().forEach(drop -> {
                    if (drop.equals(argument)) matchSet.add(pixelmon.getId());
                });
            }
        });

        if (!matchSet.isEmpty()){
            dropInfo.append("-----------\n").append("Pixelmon that drop \u00A7a").append(argument).append(":").append("\u00A7f\n\u00A7e  ");

            Object[] matches = matchSet.toArray();
            for (int index = 1; index < matches.length + 1; index++) {
                dropInfo.append(matches[index - 1]);

                if (index != matches.length) {
                    dropInfo.append("\u00A7f, \u00A7e");
                    if (index % 3 == 0) dropInfo.append("\n\u00A7e  ");
                }
            }

            dropInfo.append("\n").append("\u00A7f-----------");
            return dropInfo;
        }

        return new StringBuilder("There were no results found for " + argument + ".");
    }

    @Override
    public int compareTo(ICommand arg0) {
        return 0;
    }

    @Override
    public String getName() {
        return "Drops";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/drops [argument 1]\n" +
                "The Argument can be a Pixelmon name an item name.\n" +
                "Item names with spaces should be separated by underscores. Eg: \"Sun_Stone\"";
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
            StringBuilder results = getCommandResults(args, sender);
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
                    " up single Pixelmon names and cannot be used for item names."));
        }
        else {
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
        ClientCommandHandler.instance.registerCommand(new PixelmonDropInfo());
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

