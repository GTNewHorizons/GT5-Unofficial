package gregtech.common.blocks;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * The legacy ores. Must still be registered so that postea can transform them into the new ore blocks.
 */
public class BlockOresLegacy extends BlockOresAbstractLegacy {

    private static final String UNLOCALIZED_NAME = "gt.blockores";

    public BlockOresLegacy() {
        super(UNLOCALIZED_NAME, 7, false, Material.rock);
    }

    @Override
    public String getUnlocalizedName() {
        return UNLOCALIZED_NAME;
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isFireSource(World world, int x, int y, int z, ForgeDirection side) {
        return (side == ForgeDirection.UP && getDamageValue(world, x, y, z) / 1000 % 16 == 1);
    }

    /**
     * @inheritDoc
     */
    @Override
    public MapColor getMapColor(int meta) {
        return meta == 1 ? MapColor.netherrackColor : MapColor.stoneColor;
    }
}
