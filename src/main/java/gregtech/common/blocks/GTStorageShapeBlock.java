package gregtech.common.blocks;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.ruling_0.materiallib.api.ShapeBlock;

/// The `block` MaterialLib shape (compressed/storage blocks, e.g. "Block of Iron"), backing every cut-over
/// material with metadata equal to the material's global index. Ports the behavior overrides legacy
/// `gregtech.common.blocks.BlockStorage`/`BlockMetal` applied uniformly to every storage block regardless of
/// material: pickaxe-only harvesting at level 1, beacon-base eligibility, and hardness/resistance matching
/// vanilla's iron block. `BlockStorage` also overrode `damageDropped`/`getDamageValue`/`canBeReplacedByLeaves`/
/// `isNormalCube`, but [ShapeBlock] and vanilla `Block` already default to the same values, so those are not
/// repeated here.
public class GTStorageShapeBlock extends ShapeBlock {

    public GTStorageShapeBlock(String modid, String name, String displayNameFormat, String... oreDicts) {
        super(modid, name, displayNameFormat, oreDicts);
    }

    @Override
    public float getBlockHardness(World world, int x, int y, int z) {
        return Blocks.iron_block.getBlockHardness(world, x, y, z);
    }

    @Override
    public float getExplosionResistance(Entity exploder) {
        return Blocks.iron_block.getExplosionResistance(exploder);
    }

    @Override
    public int getHarvestLevel(int meta) {
        return 1;
    }

    @Override
    public String getHarvestTool(int meta) {
        return "pickaxe";
    }

    @Override
    public boolean isBeaconBase(IBlockAccess world, int x, int y, int z, int beaconX, int beaconY, int beaconZ) {
        return true;
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
        return true;
    }
}
