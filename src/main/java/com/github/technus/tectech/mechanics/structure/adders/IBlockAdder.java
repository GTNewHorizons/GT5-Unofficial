package com.github.technus.tectech.mechanics.structure.adders;


import net.minecraft.block.Block;

public interface IBlockAdder<MultiBlock> {
    /**
     * Callback on block added, needs to check if block is valid (and add it)
     * @param block block attempted to add
     * @param meta meta of block attempted to add
     * @return is structure still valid
     */
    boolean apply(MultiBlock multiBlock, Block block, int meta);
}
