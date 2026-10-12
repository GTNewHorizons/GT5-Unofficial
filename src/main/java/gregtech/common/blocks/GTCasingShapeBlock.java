package gregtech.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import com.ruling_0.materiallib.api.ShapeBlock;

import gregtech.api.GregTechAPI;

/// The `blockCasing` and `blockCasingAdvanced` MaterialLib shape blocks (bolted and rebolted casings). Metadata
/// is the material index. Casings act as multiblock structure parts.
public class GTCasingShapeBlock extends ShapeBlock {

    public GTCasingShapeBlock(String modid, String name, String... oreDicts) {
        super(modid, name, oreDicts);
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
