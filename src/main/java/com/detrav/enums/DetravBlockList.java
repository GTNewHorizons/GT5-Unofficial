package com.detrav.enums;

import com.detrav.blocks.DetravBlockAnvil;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;

/**
 * Created by Detrav on 30.10.2016.
 */
public class DetravBlockList {
    public static Block Anvil;
    public static void createBlocks() {
        GameRegistry.registerBlock(Anvil = new DetravBlockAnvil(),"detrav_anvil");
    }
}
