package com.neutralplasma.oregen.checks;

import com.iridium.iridiumskyblock.IridiumColorAPI;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.neutralplasma.oregen.generators.BasicGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;


public class IridiumSkyBlock extends PermissionCheck {
    private BasicGenerator basicGenerator;
    private IridiumSkyblockAPI api;

    public IridiumSkyBlock(BasicGenerator basicGenerator){
        super(basicGenerator);
        this.basicGenerator = basicGenerator;
        this.api = IridiumSkyblockAPI.getInstance();
    }

    @Override
    public long getLevel(Location location) {
        if(this.api == null) getAPI();
        Optional<Island> island = api.getIslandViaLocation(location);
        return island.map(Island::getLevel).orElse(0);
    }

    @Override
    public boolean isOnIsland(Location location) {
        if(this.api == null) getAPI();
        Optional<Island> island = api.getIslandViaLocation(location);
        return island.isPresent();
    }


    @Override
    public long getLevel(Player player) {
        if(this.api == null) getAPI();
        Optional<Island> island = api.getIslandViaLocation(player.getLocation());
        return island.map(Island::getLevel).orElse(0);
    }

    @Override
    public List<Player> getPossiblePlayers(Player player) {
        if(this.api == null) getAPI();
        Optional<Island> island = api.getIslandViaLocation(player.getLocation());
        if(!island.isPresent()) return Collections.emptyList();
        List<Player> possiblePlayers = new ArrayList<>();
        for(User uuid : island.get().getMembers()){
            Player p = Bukkit.getPlayer(uuid.getUuid());
            if(p != null){
                possiblePlayers.add(p);
            }
        }
        return  possiblePlayers;
    }

    @Override
    public List<Player> getPossiblePlayers(Location location) {
        if(this.api == null) getAPI();
        Optional<Island> island = api.getIslandViaLocation(location);
        if(!island.isPresent()) return Collections.emptyList();
        List<Player> possiblePlayers = new ArrayList<>();
        for(User uuid : island.get().getMembers()){
            Player p = Bukkit.getPlayer(uuid.getUuid());
            if(p != null){
                possiblePlayers.add(p);
            }
        }
        return  possiblePlayers;
    }

    public void getAPI(){
        this.api = IridiumSkyblockAPI.getInstance();
    }
}
