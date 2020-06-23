package com.github.technus.tectech.mechanics.structure;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public interface ICustomBlockSetting {
    /**
     * Default block setting calls {@link World#setBlock(int x, int y, int z, Block block, int meta, int updateType)} like:
     * {@code world.setBlock(x,y,z,this/block,meta,2)} where updateType 2 means to update lighting and stuff
     * @param world world that should be affected
     * @param x x position to set
     * @param y y position to set
     * @param z z position to set
     * @param meta required meta
     */
    void setBlock(World world, int x, int y, int z, int meta);
}
