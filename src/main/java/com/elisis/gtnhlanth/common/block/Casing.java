package com.elisis.gtnhlanth.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.elisis.gtnhlanth.Tags;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;

public class Casing extends Block {

    @SideOnly(Side.CLIENT)
    protected IIcon[] texture;

    private String name;

    public Casing(String name) {
        super(Material.iron);
        this.name = name;
        this.setBlockTextureName(Tags.MODID + ":casing." + name);
        GregTech_API.registerMachineBlock(this, -1);
    }

    public Casing(String name, Material material) {
        super(material);
        this.name = name;
        this.setBlockTextureName(Tags.MODID + ":casing." + name);
        GregTech_API.registerMachineBlock(this, -1);
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
        GregTech_API.causeMachineUpdate(aWorld, aX, aY, aZ);
        super.breakBlock(aWorld, aX, aY, aZ, aBlock, aMetaData);
    }

    @Override
    public void onBlockAdded(World aWorld, int aX, int aY, int aZ) {
        super.onBlockAdded(aWorld, aX, aY, aZ);
        GregTech_API.causeMachineUpdate(aWorld, aX, aY, aZ);
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
