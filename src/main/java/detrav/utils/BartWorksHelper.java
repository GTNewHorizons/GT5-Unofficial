package detrav.utils;

import net.minecraft.block.Block;
import net.minecraft.world.chunk.Chunk;

import bartworks.system.material.BWMetaGeneratedOres;
import bartworks.system.material.BWMetaGeneratedSmallOres;

public class BartWorksHelper {

    public static boolean isOre(Block tBlock) {
        return tBlock instanceof BWMetaGeneratedOres;
    }

    public static boolean isSmallOre(Block tBlock) {
        return tBlock instanceof BWMetaGeneratedSmallOres;
    }

    public static short getMetaFromBlock(Chunk c, int x, int y, int z, Block tBlock) {
        return (short) (tBlock.getDamageValue(c.worldObj, c.xPosition * 16 + x, y, c.zPosition * 16 + z) * -1);
    }

}
