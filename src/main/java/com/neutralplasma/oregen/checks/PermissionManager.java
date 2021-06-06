package com.neutralplasma.oregen.checks;

import com.neutralplasma.oregen.OreGen;
import com.neutralplasma.oregen.generators.BasicGenerator;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class PermissionManager {
    private PermissionCheck permissionCheck;


    public PermissionCheck getPermissionCheck(BasicGenerator basicGenerator, OreGen pl) {
        PluginManager pm = Bukkit.getPluginManager();
        if(pm.getPlugin("FabledSkyBlock") != null){
            permissionCheck = new FabledSkyBlock(basicGenerator);
        }else if(pm.getPlugin("ASkyBlock") != null){
            permissionCheck = new ASkyBlock(basicGenerator);
        }else if(pm.getPlugin("IridiumSkyBlock") != null){
            permissionCheck = new IridiumSkyBlock(basicGenerator);
        }

        if(permissionCheck == null){
            Bukkit.getLogger().warning("no skyblock plugin found, disabling plugin.");
            pm.disablePlugin(pl);
        }

        return permissionCheck;
    }
}
