package com.neutralplasma.oregen;

import com.neutralplasma.oregen.checks.PermissionCheck;
import com.neutralplasma.oregen.checks.PermissionManager;
import com.neutralplasma.oregen.commands.mainCommand;
import com.neutralplasma.oregen.events.CreateOre;
import com.neutralplasma.oregen.generators.BasicGenerator;
import com.neutralplasma.oregen.generators.MaterialChooser;
import com.songoda.skyblock.api.island.Island;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class OreGen extends JavaPlugin {

    private BasicGenerator basicGenerator;
    private MaterialChooser materialChooser;
    private PermissionManager permissionManager;

    @Override
    public void onEnable() {
        setupConfig();

        materialChooser = new MaterialChooser();
        basicGenerator = new BasicGenerator(this, materialChooser);
        permissionManager = new PermissionManager();

        PluginManager pluginManager = Bukkit.getPluginManager();
        PermissionCheck permissionCheck = permissionManager.getPermissionCheck(basicGenerator, this);
        pluginManager.registerEvents(new CreateOre(basicGenerator, this, permissionCheck), this);

        this.getCommand("oregen").setExecutor(new mainCommand(this, permissionCheck));

    }



    public void setupConfig(){
        this.saveDefaultConfig();
    }

    public void reload(){
        reloadConfig();
        basicGenerator.reload();
    }
}
