package gtnhintergalactic.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gtnhintergalactic.GTNHIntergalactic;
import micdoodle8.mods.galacticraft.api.block.ITerraformableBlock;

public class BlockCasingGasSiphon extends Block implements ITerraformableBlock {

    public BlockCasingGasSiphon() {
        super(Material.rock);
        this.setBlockName("GasSiphonCasing");
        this.setHardness(2.0f);
        this.setStepSound(soundTypeStone);
        this.setHarvestLevel("pickaxe", 3);
        this.setCreativeTab(GTNHIntergalactic.tab);
        this.setBlockTextureName(GTNHIntergalactic.MODID + ":gasSiphon/SiphonCasing");
        GregTechAPI.registerMachineBlock(this, -1);
        ItemList.PlanetaryGasSiphonCasing.set(new ItemStack(this));
    }

    @Override
    public boolean isTerraformable(final World world, final int x, final int y, final int z) {
        return false;
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        if (GregTechAPI.isMachineBlock(this, world.getBlockMetadata(x, y, z))) {
            GregTechAPI.causeMachineUpdate(world, x, y, z);
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        if (GregTechAPI.isMachineBlock(block, meta)) {
            GregTechAPI.causeMachineUpdate(world, x, y, z);
        }
    }
}
