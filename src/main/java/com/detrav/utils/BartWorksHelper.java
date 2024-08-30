package com.detrav.utils;

import net.minecraft.block.Block;
import net.minecraft.world.chunk.Chunk;

import com.github.bartimaeusnek.bartworks.system.material.BW_MetaGenerated_Ores;
import com.github.bartimaeusnek.bartworks.system.material.BW_MetaGenerated_SmallOres;

public class BartWorksHelper {

    public static boolean isOre(Block tBlock) {
        return tBlock instanceof BW_MetaGenerated_Ores;
    }

    public static boolean isSmallOre(Block tBlock) {
        return tBlock instanceof BW_MetaGenerated_SmallOres;
    }

    public static short getMetaFromBlock(Chunk c, int x, int y, int z, Block tBlock) {
        return (short) (tBlock.getDamageValue(c.worldObj, c.xPosition * 16 + x, y, c.zPosition * 16 + z) * -1);
    }

}
