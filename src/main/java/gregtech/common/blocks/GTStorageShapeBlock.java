package gregtech.common.blocks;

import java.util.Locale;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.ShapeBlock;

import gregtech.api.enums.Materials;
import gregtech.api.material.MU;

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

    /// The converted legacy per-material storage-block art (`scripts/mu/convert_textures.py`'s
    /// `convert_per_material_blocks`), keyed by the material's legacy name rather than its MaterialLib one --
    /// legacy art is per-material, not per-texture-set, and the two names can differ (a unified material takes
    /// its canonical name from whichever legacy material won unification). Every material generating this shape
    /// has a legacy counterpart (`block` membership is entirely legacy-derived; see `Materials2BlockShapes`), so
    /// [MU#materialOf] is not expected to return null here, but a defensive null still falls back to this
    /// shape's texture-set candidates the same as a converted file that does not exist.
    @Override
    protected String iconPathFor(Material material) {
        Materials legacy = MU.materialOf(material);
        return legacy != null ? "gregtech:materials/blocks/" + legacy.mName.toLowerCase(Locale.ROOT) : null;
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
