package gtnhlanth.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gtnhlanth.Tags;

public class BlockCasing extends Block {

    @SideOnly(Side.CLIENT)
    protected IIcon[] texture;

    private String name;

    public BlockCasing(String name) {
        super(Material.iron);
        this.name = name;
        this.setBlockTextureName(Tags.MODID + ":casing." + name);
        GregTechAPI.registerMachineBlock(this, -1);
    }

    public BlockCasing(String name, Material material) {
        super(material);
        this.name = name;
        this.setBlockTextureName(Tags.MODID + ":casing." + name);
        GregTechAPI.registerMachineBlock(this, -1);
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    public String getHarvestTool(int aMeta) {
        return "wrench";
    }

    @Override
    public int getHarvestLevel(int aMeta) {
        return 2;
    }

    @Override
    public float getBlockHardness(World aWorld, int aX, int aY, int aZ) {
        return Blocks.iron_block.getBlockHardness(aWorld, aX, aY, aZ);
    }

    @Override
    public void breakBlock(World aWorld, int aX, int aY, int aZ, Block aBlock, int aMetaData) {
        GregTechAPI.causeMachineUpdate(aWorld, aX, aY, aZ);
        super.breakBlock(aWorld, aX, aY, aZ, aBlock, aMetaData);
    }

    @Override
    public void onBlockAdded(World aWorld, int aX, int aY, int aZ) {
        super.onBlockAdded(aWorld, aX, aY, aZ);
        GregTechAPI.causeMachineUpdate(aWorld, aX, aY, aZ);
    }

    @Override
    public String getUnlocalizedName() {
        return "casing." + this.name;
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
        return false;
    }

}
