package toxiceverglades.gen;

import net.minecraft.block.Block;
import net.minecraft.world.gen.feature.WorldGenMinable;

public class WorldGenMinableCustom extends WorldGenMinable {

    public WorldGenMinableCustom(Block block, int count) {
        super(block, count);
    }

    public WorldGenMinableCustom(Block block, int count, Block target) {
        super(block, count, target);
    }

    public WorldGenMinableCustom(Block block, int meta, int count, Block target) {
        super(block, meta, count, target);
    }
}
