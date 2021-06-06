package com.neutralplasma.oregen.checks;

import com.neutralplasma.oregen.generators.BasicGenerator;
import com.songoda.skyblock.api.SkyBlockAPI;
import com.songoda.skyblock.api.island.Island;
import com.songoda.skyblock.api.island.IslandRole;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class FabledSkyBlock extends PermissionCheck {
    private BasicGenerator basicGenerator;

    public FabledSkyBlock(BasicGenerator basicGenerator){
        super(basicGenerator);
        this.basicGenerator = basicGenerator;
    }

    @Override
    public long getLevel(Location location) {
        Island island = SkyBlockAPI.getIslandManager().getIslandAtLocation(location);
        if(island == null){
            return 0;
        }
        return island.getLevel().getLevel();
    }

    @Override
    public boolean isOnIsland(Location location) {
        Island island = SkyBlockAPI.getIslandManager().getIslandAtLocation(location);
        if(island == null){
            return false;
        }
        return true;
    }

    @Override
    public long getLevel(Player player) {
        Island island = SkyBlockAPI.getIslandManager().getIslandPlayerAt(player);
        if(island == null){
            return 0;
        }
        return island.getLevel().getLevel();
    }

    @Override
    public List<Player> getPossiblePlayers(Player player) {
        Island island = SkyBlockAPI.getIslandManager().getIslandPlayerAt(player);
        List<Player> possiblePlayers = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            boolean isMember = island.hasRole( p.getUniqueId(), IslandRole.OWNER) ||
                    island.hasRole(p.getUniqueId(), com.songoda.skyblock.api.island.IslandRole.MEMBER) ||
                    island.hasRole(p.getUniqueId(), com.songoda.skyblock.api.island.IslandRole.COOP) ||
                    island.hasRole(p.getUniqueId(), com.songoda.skyblock.api.island.IslandRole.OPERATOR);
            if (isMember && SkyBlockAPI.getIslandManager().isLocationAtIsland(island, p.getLocation())) {
                possiblePlayers.add(p);
            }
        }
        return possiblePlayers;
    }

    @Override
    public List<Player> getPossiblePlayers(Location location) {

        Island island = SkyBlockAPI.getIslandManager().getIslandAtLocation(location);
        if(island == null){
            return Collections.emptyList();
        }

        List<Player> possiblePlayers = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            boolean isMember = island.hasRole( p.getUniqueId(), IslandRole.OWNER) ||
                    island.hasRole(p.getUniqueId(), com.songoda.skyblock.api.island.IslandRole.MEMBER) ||
                    island.hasRole(p.getUniqueId(), com.songoda.skyblock.api.island.IslandRole.COOP) ||
                    island.hasRole(p.getUniqueId(), com.songoda.skyblock.api.island.IslandRole.OPERATOR);
            if (isMember && SkyBlockAPI.getIslandManager().isLocationAtIsland(island, p.getLocation())) {
                possiblePlayers.add(p);
            }
        }

        return possiblePlayers;
    }
}
