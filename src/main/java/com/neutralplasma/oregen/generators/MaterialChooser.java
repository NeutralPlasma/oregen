package com.neutralplasma.oregen.generators;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaterialChooser {


    public Map getMaterials(List<String> ores){
        Map<Material, Double> materials = new HashMap<>();
        for(String ore : ores){
            String[] results = ore.split(":");
            materials.put(Material.getMaterial(results[0]), Double.valueOf(results[1]));
        }

        return materials;
    }
}
