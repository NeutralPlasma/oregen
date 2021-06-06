package com.neutralplasma.oregen.generators;

import com.neutralplasma.oregen.OreGen;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class BasicGenerator {

    OreGen oreGen;
    private ConfigurationSection permissionGens;
    private ConfigurationSection levelGens;
    private MaterialChooser materialChooser;

    public BasicGenerator(OreGen oreGen, MaterialChooser materialChooser){
        this.oreGen = oreGen;
        this.materialChooser = materialChooser;
        permissionGens = oreGen.getConfig().getConfigurationSection("ore-generators.permission-based");
        levelGens = oreGen.getConfig().getConfigurationSection("ore-generators.level-based");
    }

    public boolean generateBlockPermission(Block block, Player player){
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
            return false;
        }
        List<String> ores = permissionGens.getStringList(maxGenerator + ".ores");
        block.setType(randomChance(ores));
        return true;
    }

    public boolean generateBlockLevel(Block block, Player player, long level){
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
            return false;
        }

        List<String> ores = levelGens.getStringList(maxGenerator + ".ores");
        block.setType(randomChance(ores));
        return true;
    }

    private Material randomChance(List<String> ores) { ;
        final Map<Material, Double> chances = materialChooser.getMaterials(ores);
        final Random r = new Random();
        double chance = 100.0 * r.nextDouble();

        for (final Material material : chances.keySet()) {
            chance -= chances.get(material);
            if (chance <= 0.0) {
                return material;
            }
        }

        return Material.getMaterial(oreGen.getConfig().getString("fall-back-material"));
    }

    public void reload(){
        permissionGens = oreGen.getConfig().getConfigurationSection("ore-generators.permission-based");
        levelGens = oreGen.getConfig().getConfigurationSection("ore-generators.level-based");
    }

    public int getMaxGenerator(Player player){
        int maxgen = 0;
        for(String key : permissionGens.getKeys(true)) {
            List<String> ores = permissionGens.getStringList(key + ".ores");
            if(!ores.isEmpty()) {
                if (player.hasPermission("oregen." + key)) {
                    maxgen++;
                }
            }
        }
        return maxgen;
    }
}
