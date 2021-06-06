package com.neutralplasma.oregen.checks;

import com.neutralplasma.oregen.generators.BasicGenerator;

import com.wasteofplastic.askyblock.ASkyBlockAPI;
import com.wasteofplastic.askyblock.Island;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ASkyBlock extends PermissionCheck {
    private BasicGenerator basicGenerator;

    public ASkyBlock(BasicGenerator basicGenerator){
        super(basicGenerator);
        this.basicGenerator = basicGenerator;
    }

    @Override
    public long getLevel(Location location) {
        Island island = ASkyBlockAPI.getInstance().getIslandAt(location);
        if(island == null){
            return 0;
        }
        return island.getLevelHandicap();
    }

    @Override
    public boolean isOnIsland(Location location) {
        Island island = ASkyBlockAPI.getInstance().getIslandAt(location);
        return island != null;
    }

    @Override
    public long getLevel(Player player) {
        Island island = ASkyBlockAPI.getInstance().getIslandOwnedBy(player.getUniqueId());
        return island.getLevelHandicap();
    }

    @Override
    public List<Player> getPossiblePlayers(Player player) {
        Island island = ASkyBlockAPI.getInstance().getIslandOwnedBy(player.getUniqueId());
        if(island == null) return Collections.emptyList();
        List<Player> possiblePlayers = new ArrayList<>();
        for(UUID uuid : island.getMembers()){
            Player p = Bukkit.getPlayer(uuid);
            if(p != null){
                possiblePlayers.add(p);
            }
        }
        return  possiblePlayers;
    }

    @Override
    public List<Player> getPossiblePlayers(Location location) {
        Island island = ASkyBlockAPI.getInstance().getIslandAt(location);
        if(island == null) return Collections.emptyList();
        List<Player> possiblePlayers = new ArrayList<>();
        for(UUID uuid : island.getMembers()){
            Player p = Bukkit.getPlayer(uuid);
            if(p != null){
                possiblePlayers.add(p);
            }
        }
        return  possiblePlayers;
    }
}
