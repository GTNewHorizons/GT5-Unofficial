package gregtech.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import com.ruling_0.materiallib.api.ShapeBlock;

import gregtech.api.GregTechAPI;

/// The `blockCasing`/`blockCasingAdvanced` MaterialLib shapes (bartworks' bolted and rebolted werkstoff
/// casings), backing every material with metadata equal to the material's global index. Every casing is
/// wrench-harvested at level 2, takes vanilla's iron block hardness and resistance, and causes a
/// machine-structure update on placement and removal.
public class GTCasingShapeBlock extends ShapeBlock {

    public GTCasingShapeBlock(String modid, String name, String displayNameFormat, String... oreDicts) {
        super(modid, name, displayNameFormat, oreDicts);
        // Casings are multiblock structure parts: machines re-check their structure when a neighbor changes.
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
