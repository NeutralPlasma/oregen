package com.neutralplasma.oregen.commands;

import com.neutralplasma.oregen.OreGen;
import com.neutralplasma.oregen.checks.PermissionCheck;
import com.neutralplasma.oregen.util.TextFormater;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class mainCommand implements CommandExecutor {
    private OreGen oreGen;
    private ConfigurationSection permissionGens;
    private ConfigurationSection levelGens;
    private PermissionCheck permissionCheck;


    public mainCommand(OreGen oreGen, PermissionCheck permissionCheck){
        this.oreGen = oreGen;
        this.permissionCheck = permissionCheck;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(!(commandSender instanceof Player) && args.length >= 1 && !args[0].equalsIgnoreCase("reload")){
            commandSender.sendMessage(TextFormater.colorCode(oreGen.getConfig().getString("level-command.useronly")));
            return true;
        }
        if(args.length >= 1 && args[0].equalsIgnoreCase("reload") && commandSender.hasPermission("oregen.reload")){
            commandSender.sendMessage("Reloaded!");
            oreGen.reload();
            return true;
        }
        if (args.length >= 1 && args[0].equalsIgnoreCase("level") && commandSender.hasPermission("oregen.level.see")){
            List<String> ores = new ArrayList<>();
            String message = "";
            List<String> unFormatedMessage = oreGen.getConfig().getStringList("level-command.format");
            // Variables
            permissionGens = oreGen.getConfig().getConfigurationSection("ore-generators.permission-based");
            levelGens = oreGen.getConfig().getConfigurationSection("ore-generators.level-based");

            long levelbasedpriorty = oreGen.getConfig().getInt("ore-generators.level-based.priority");
            long permissionbasedpriority = oreGen.getConfig().getInt("ore-generators.permission-based.priority");

            Player player = (Player) commandSender;

            long level = permissionCheck.getLevel(permissionCheck.getCorrectPlayer(player));

            String levels = "";

            // Code
            if(oreGen.getConfig().getBoolean("ore-generators.permission-based.enabled") && permissionbasedpriority >  levelbasedpriorty) {
                if(getOresPermission(player) != null){
                    ores = getOresPermission(player);
                }else{
                    if(getOresLevel(level) != null && oreGen.getConfig().getBoolean("ore-generators.level-based.enabled")){
                        ores = getOresLevel(level);
                    }
                }
                levels = getLevel(player, level, "permission");
                if(levels == null){
                    if(oreGen.getConfig().getBoolean("ore-generators.level-based.enabled")){
                        levels = getLevel(player, level, "level");
                    }
                }
            }
            if (oreGen.getConfig().getBoolean("ore-generators.level-based.enabled") && permissionbasedpriority < levelbasedpriorty) {
                if(getOresLevel(level) != null){
                    ores = getOresLevel(level);
                }else{
                    if(getOresPermission(player) != null && oreGen.getConfig().getBoolean("ore-generators.permission-based.enabled")){
                        ores = getOresPermission(player);
                    }
                }
                levels = getLevel(player, level, "level");
                if(levels == null){
                    if(oreGen.getConfig().getBoolean("ore-generators.permission-based.enabled")){
                        levels = getLevel(player, level, "permission");
                    }
                }
            }
            for(String row : unFormatedMessage){
                message = message + "\n" + row;
            }
            String oresString = "";
            for(String ore : ores){
                oresString = oresString + "\n" + ore;
            }
            message = message.replace("{ores}" ,  oresString);
            message = message.replace("{level}", levels);
            player.sendMessage(TextFormater.colorCode(message));
            return true;
        }

        commandSender.sendMessage(TextFormater.colorCode(oreGen.getConfig().getString("level-command.noperm")));
        return true;
    }

    public List<String> getOresPermission(Player player){
        String maxGenerator = null;

        for(String key : permissionGens.getKeys(true)) {
            List<String> ores = permissionGens.getStringList(key + ".ores");
            if(!ores.isEmpty()) {
                if (player.hasPermission("oregen." + key)) {
                    maxGenerator = key;
                }
            }
        }
        if(maxGenerator == null){
            return null;
        }
        return permissionGens.getStringList(maxGenerator + ".ores");

    }

    public String getLevel(Player player, long level, String type){
        String maxGenerator = null;
        if(type.equalsIgnoreCase("permission")){
            for(String key : permissionGens.getKeys(true)) {
                List<String> ores = permissionGens.getStringList(key + ".ores");
                if(!ores.isEmpty()) {
                    if (player.hasPermission("oregen." + key)) {
                        maxGenerator = key;
                    }
                }
            }
        }
        if(type.equalsIgnoreCase("level")){
            for(String key : levelGens.getKeys(true)) {
                long minlevel = levelGens.getLong(key + ".min-level");
                List<String> ores = permissionGens.getStringList(key + ".ores");
                if(!ores.isEmpty()) {
                    if (level > minlevel) {
                        maxGenerator = key;
                    }
                }
            }
        }

        return maxGenerator;
    }

    public List<String> getOresLevel(long level){
        String maxGenerator = null;

        for(String key : levelGens.getKeys(true)) {
            long minlevel = levelGens.getLong(key + ".min-level");
            List<String> ores = permissionGens.getStringList(key + ".ores");
            if(!ores.isEmpty()) {
                if (level > minlevel) {
                    maxGenerator = key;
                }
            }
        }
        if(maxGenerator == null){
            return null;
        }

        return levelGens.getStringList(maxGenerator + ".ores");
    }
}
