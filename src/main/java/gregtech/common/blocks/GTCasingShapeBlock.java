package gregtech.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import com.ruling_0.materiallib.api.ShapeBlock;

import gregtech.api.GregTechAPI;

/// The `blockCasing`/`blockCasingAdvanced` MaterialLib shapes (bartworks' bolted/rebolted werkstoff casings),
/// backing every material with metadata equal to the material's global index. Ports the behavior overrides
/// legacy `bartworks.system.material.BWMetaGeneratedBlocksCasing` applied uniformly regardless of material:
/// wrench harvesting at level 2, hardness/resistance matching vanilla's iron block, and a machine-structure
/// update on placement and removal (casings are multiblock structure parts).
public class GTCasingShapeBlock extends ShapeBlock {

    public GTCasingShapeBlock(String modid, String name, String displayNameFormat, String... oreDicts) {
        super(modid, name, displayNameFormat, oreDicts);
        // Mirrors BWMetaGeneratedBlocksCasing#doRegistrationStuff: machines re-check their structure when a
        // neighboring casing changes.
        GregTechAPI.registerMachineBlock(this, -1);
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
        return 2;
    }

    @Override
    public String getHarvestTool(int meta) {
        return "wrench";
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        GregTechAPI.causeMachineUpdate(world, x, y, z);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        GregTechAPI.causeMachineUpdate(world, x, y, z);
        super.breakBlock(world, x, y, z, block, meta);
    }
}
