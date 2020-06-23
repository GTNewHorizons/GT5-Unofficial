package com.github.technus.tectech.mechanics.structure;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public interface ICustomMetaBlock {
    default void setBlock(World world, int x, int y, int z, int meta){
        world.setBlock(x, y, z, (Block)this, meta, 2);
    }
}
