package com.dumptruckman.tools.util;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Button;
import org.bukkit.material.Lever;
import org.bukkit.material.MaterialData;
import org.bukkit.material.PistonBaseMaterial;
import org.bukkit.material.PoweredRail;
import org.bukkit.material.RedstoneWire;

import java.util.HashSet;
import java.util.Set;

/**
 * General tools to help with minecraftian things.
 */
public class MinecraftTools {

    private static final int TICKS_PER_SECOND = 20;

    private MinecraftTools() {
    }

    /**
     * Converts an amount of seconds to the appropriate amount of ticks.
     *
     * @param seconds Amount of seconds to convert
     * @return Ticks converted from seconds.
     */
    public static long convertSecondsToTicks(long seconds) {
        return seconds * TICKS_PER_SECOND;
    }

    /**
     * Fills an ItemStack array with air.
     *
     * @param items The ItemStack array to fill.
     * @return The air filled ItemStack array.
     */
    public static ItemStack[] fillWithAir(ItemStack[] items) {
        for (int i = 0; i < items.length; i++) {
            items[i] = new ItemStack(0);
        }
        return items;
    }
    
    public static Set<BlockFace> getAdjacentFaces() {
        Set<BlockFace> faces = new HashSet<BlockFace>();
        faces.add(BlockFace.NORTH);
        faces.add(BlockFace.EAST);
        faces.add(BlockFace.SOUTH);
        faces.add(BlockFace.WEST);
        return faces;
    }
    
    public static void setBlockPower(Block block, boolean power) {
        //
        MaterialData md = block.getState().getData();
        int data = block.getData();
        if (block.getType() == (Material.DIODE_BLOCK_OFF) && power) {
            block.setType(Material.DIODE_BLOCK_ON);
            block.setData((byte) data);
        } else if (block.getType() == (Material.DIODE_BLOCK_ON) && !power) {
            block.setType(Material.DIODE_BLOCK_OFF);
            block.setData((byte) data);
        } else if (md instanceof Lever) {
            ((Lever) md).setPowered(power);
        } else if (md instanceof Button) {
            ((Button) md).setPowered(power);
        } else if (md instanceof PistonBaseMaterial) {
            ((PistonBaseMaterial) md).setPowered(power);
        } else if (md instanceof PoweredRail) {
            ((PoweredRail) md).setPowered(power);
        } else if (md instanceof RedstoneWire) {
            block.setData(((byte)(power ? data | 0xF : data & 0x0)));
        }
    }
}
