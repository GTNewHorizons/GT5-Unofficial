package com.minecraft7771.gtnhintergalactic.block;

import net.minecraft.block.Block;

import com.minecraft7771.gtnhintergalactic.item.ItemBlockSpaceElevatorCable;
import cpw.mods.fml.common.registry.GameRegistry;

public class IGBlocks {

    public static Block SpaceElevatorCable;

    public static void init() {
        SpaceElevatorCable = new BlockSpaceElevatorCable();
        GameRegistry.registerBlock(SpaceElevatorCable, ItemBlockSpaceElevatorCable.class, "spaceelevatorcable");
    }
}
