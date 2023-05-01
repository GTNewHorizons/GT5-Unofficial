package com.gtnewhorizons.gtnhintergalactic.block;

import net.minecraft.block.Block;

import com.gtnewhorizons.gtnhintergalactic.item.ItemBlockSpaceElevatorCable;

import cpw.mods.fml.common.registry.GameRegistry;

/**
 * List of all blocks of this mod
 */
public class IGBlocks {

    public static Block SpaceElevatorCable;
    public static BlockCasingSpaceElevator SpaceElevatorCasing;
    public static BlockCasingSpaceElevatorMotor SpaceElevatorMotor;

    /**
     * Initialize the blocks of this mod
     */
    public static void init() {
        SpaceElevatorCable = new BlockSpaceElevatorCable();
        GameRegistry.registerBlock(SpaceElevatorCable, ItemBlockSpaceElevatorCable.class, "spaceelevatorcable");
        SpaceElevatorCasing = new BlockCasingSpaceElevator();
        SpaceElevatorMotor = new BlockCasingSpaceElevatorMotor();
    }
}
