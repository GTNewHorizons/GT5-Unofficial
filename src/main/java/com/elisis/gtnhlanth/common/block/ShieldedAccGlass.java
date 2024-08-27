package com.elisis.gtnhlanth.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.elisis.gtnhlanth.Tags;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;

public class ShieldedAccGlass extends Block {

    private static final String name = "shielded_accelerator_glass";

    public ShieldedAccGlass() {
        super(Material.glass);
        this.setBlockName("casing." + name);
        this.setBlockTextureName(Tags.MODID + ":casing." + name);
        GregTech_API.registerMachineBlock(this, -1);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public void onBlockAdded(World aWorld, int aX, int aY, int aZ) {
        if (GregTech_API.isMachineBlock(this, aWorld.getBlockMetadata(aX, aY, aZ))) {
            GregTech_API.causeMachineUpdate(aWorld, aX, aY, aZ);
        }
    }

    @Override
    public void breakBlock(World aWorld, int aX, int aY, int aZ, Block aBlock, int aMetaData) {
        if (GregTech_API.isMachineBlock(this, aMetaData)) {
            GregTech_API.causeMachineUpdate(aWorld, aX, aY, aZ);
        }
    }

    @Override
    public float getBlockHardness(World aWorld, int aX, int aY, int aZ) {
        return Blocks.glass.getBlockHardness(aWorld, aX, aY, aZ);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess worldClient, int xCoord, int yCoord, int zCoord, int aSide) {
        if (worldClient.getBlock(xCoord, yCoord, zCoord) instanceof ShieldedAccGlass) return false;
        return super.shouldSideBeRendered(worldClient, xCoord, yCoord, zCoord, aSide);
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
        return false;
    }

}
