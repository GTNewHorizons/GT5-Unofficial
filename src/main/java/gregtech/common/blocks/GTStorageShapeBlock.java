package gregtech.common.blocks;

import java.util.Locale;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.ShapeBlock;

import gregtech.api.material.MaterialUtils;

/// The `block` MaterialLib shape (compressed/storage blocks, e.g. "Block of Iron"), backing every cut-over
/// material with metadata equal to the material's global index. Every storage block harvests pickaxe-only at
/// level 1, counts as a beacon base, and takes vanilla's iron block hardness and resistance.
public class GTStorageShapeBlock extends ShapeBlock {

    public GTStorageShapeBlock(String modid, String name, String displayNameFormat, String... oreDicts) {
        super(modid, name, displayNameFormat, oreDicts);
    }

    /// The per-material storage-block art, keyed by [MaterialUtils#internalName] rather than the material's
    /// MaterialLib name, since the art is per-material and the two names can differ. A material whose art file
    /// does not exist falls back to this shape's texture-set candidates.
    @Override
    protected String iconPathFor(Material material) {
        return "gregtech:materials/blocks/" + MaterialUtils.internalName(material)
            .toLowerCase(Locale.ROOT);
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
