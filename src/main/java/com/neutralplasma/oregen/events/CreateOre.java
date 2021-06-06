package com.neutralplasma.oregen.events;

import com.neutralplasma.oregen.OreGen;
import com.neutralplasma.oregen.checks.PermissionCheck;
import com.neutralplasma.oregen.generators.BasicGenerator;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;


public class CreateOre implements Listener {

    private final BlockFace[] faces;
    private BasicGenerator basicGenerator;
    private PermissionCheck permissionCheck;
    private OreGen oreGen;

    public CreateOre(BasicGenerator basicGenerator, OreGen oreGen, PermissionCheck permissionCheck){
        this.basicGenerator = basicGenerator;
        this.oreGen = oreGen;
        this.permissionCheck = permissionCheck;
        this.faces = new BlockFace[] { BlockFace.SELF, BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };
    }

    @EventHandler
    public void onFormTo(final BlockFromToEvent e) {
        final Block source = e.getBlock();
        final Block to = e.getToBlock();
        if (source.getType() == Material.WATER || source.getType() == Material.LAVA) {
            if ((to.getType() == Material.AIR || to.getType() == Material.WATER) && this.generatesCobble(source.getType(), to)) {
                if ((source.getType() == Material.LAVA) && !this.isSurroundedByWater(to.getLocation())) {
                    return;
                }

                if(permissionCheck.isOnIsland(to.getLocation())){
                    Player correctPlayer = permissionCheck.getCorrectPlayer(to.getLocation());
                    e.setCancelled(true);

                    if(correctPlayer != null){
                        long levelbasedpriorty = oreGen.getConfig().getInt("ore-generators.level-based.priority");
                        long permissionbasedpriority = oreGen.getConfig().getInt("ore-generators.permission-based.priority");
                        long level = permissionCheck.getLevel(to.getLocation());

                        if (oreGen.getConfig().getBoolean("ore-generators.permission-based.enabled") && permissionbasedpriority > levelbasedpriorty) {
                            if (!basicGenerator.generateBlockPermission(to, correctPlayer)) {
                                if (oreGen.getConfig().getBoolean("ore-generators.level-based.enabled")) {
                                    basicGenerator.generateBlockLevel(to, correctPlayer, level);
                                }
                            }
                        }
                        if (oreGen.getConfig().getBoolean("ore-generators.level-based.enabled") && permissionbasedpriority < levelbasedpriorty) {
                            if (!basicGenerator.generateBlockLevel(to, correctPlayer, level)) {
                                if (oreGen.getConfig().getBoolean("ore-generators.permission-based.enabled")) {
                                    basicGenerator.generateBlockPermission(to, correctPlayer);
                                }
                            }
                        }

                    }else{
                        to.setType(Material.getMaterial(oreGen.getConfig().getString("fall-back-material")));
                    }

                }

            }

        }
    }



    private boolean generatesCobble(final Material material, final Block b) {
        final Material mirMat1 = (material == Material.WATER) ? Material.LAVA : Material.WATER;
        for (final BlockFace face : faces) {
            final Block check = b.getRelative(face, 1);
            if (check.getType() == mirMat1) {
                return true;
            }
        }
        return false;
    }

    private boolean isSurroundedByWater(final Location fromLoc) {
        final Block[] array;
        final Block[] blocks = array = new Block[] { fromLoc.getWorld().getBlockAt(fromLoc.getBlockX() + 1, fromLoc.getBlockY(), fromLoc.getBlockZ()), fromLoc.getWorld().getBlockAt(fromLoc.getBlockX() - 1, fromLoc.getBlockY(), fromLoc.getBlockZ()), fromLoc.getWorld().getBlockAt(fromLoc.getBlockX(), fromLoc.getBlockY(), fromLoc.getBlockZ() + 1), fromLoc.getWorld().getBlockAt(fromLoc.getBlockX(), fromLoc.getBlockY(), fromLoc.getBlockZ() - 1) };
        for (final Block b : array) {
            if (b.getType() == Material.WATER) {
                return true;
            }
        }
        return false;
    }



}
