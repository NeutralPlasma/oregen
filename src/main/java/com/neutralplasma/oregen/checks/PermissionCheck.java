package com.neutralplasma.oregen.checks;

import com.neutralplasma.oregen.generators.BasicGenerator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public abstract class PermissionCheck {
    private BasicGenerator basicGenerator;

    public PermissionCheck(BasicGenerator basicGenerator){
        this.basicGenerator = basicGenerator;
    }

    public List<Player> getPossiblePlayers(Location location){
        return Collections.emptyList();
    }

    public List<Player> getPossiblePlayers(Player player){
        return Collections.emptyList();
    }

    public long getLevel(Location location){
        return 0;
    }

    public long getLevel(Player player){
        return 0;
    }

    public boolean isOnIsland(Location location){
        return false;
    }

    public Player getCorrectPlayer(Location location){
        int max_gen = 0;
        Player correctPlayer = null;
        List<Player> possiblePlayers = getPossiblePlayers(location);
        for(Player p : possiblePlayers) {
            if (p != null && p.hasPermission("oregen.use")) {
               // to.setType(Material.getMaterial(oreGen.getConfig().getString("fall-back-material")));
                int currentmaxgen = basicGenerator.getMaxGenerator(p);
                if(currentmaxgen > max_gen){
                    max_gen = currentmaxgen;
                    correctPlayer = p;
                }
            }
        }
        return correctPlayer;
    }

    public Player getCorrectPlayer(Player player){
        int max_gen = 0;
        Player correctPlayer = null;
        List<Player> possiblePlayers = getPossiblePlayers(player);
        for(Player p : possiblePlayers) {
            if (p != null && p.hasPermission("oregen.use")) {
                // to.setType(Material.getMaterial(oreGen.getConfig().getString("fall-back-material")));
                int currentmaxgen = basicGenerator.getMaxGenerator(p);
                if(currentmaxgen > max_gen){
                    max_gen = currentmaxgen;
                    correctPlayer = p;
                }
            }
        }
        return correctPlayer;
    }
}
