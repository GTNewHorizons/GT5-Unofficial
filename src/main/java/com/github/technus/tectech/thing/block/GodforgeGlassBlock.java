package com.github.technus.tectech.thing.block;

import static com.github.technus.tectech.Reference.MODID;
import static com.github.technus.tectech.TecTech.creativeTabTecTech;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import eu.usrv.yamcore.blocks.BlockBase;
import gregtech.api.GregTech_API;

public class GodforgeGlassBlock extends BlockBase {

    public static IIcon Icon;
    public static int renderID;
    public static GodforgeGlassBlock INSTANCE;

    public GodforgeGlassBlock() {
        super(Material.iron);
        setBlockBounds(0, 0, 0, 1, 1, 1);
        setBlockName("spatiallyTranscendentGravitationalLens");
        setHarvestLevel("wrench", 3);
        setHardness(50);
        setResistance(30);
        setLightOpacity(0);
        setStepSound(Block.soundTypeGlass);
        setBlockTextureName(MODID + ":blockSpatiallyTranscendentGravitationalLens");
        setCreativeTab(creativeTabTecTech);
    }

    @Override
    public boolean isBeaconBase(IBlockAccess worldObj, int x, int y, int z, int beaconX, int beaconY, int beaconZ) {
        return true;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean getCanBlockGrass() {
        return false;
    }

    @Override
    public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z) {
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
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess worldIn, int x, int y, int z, int side) {
        Block block = worldIn.getBlock(x, y, z);
        return block != this;
    }

    @Override
    public int getRenderType() {
        return renderID;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        super.registerBlockIcons(reg);
        Icon = blockIcon;
    }

    public static void run() {
        INSTANCE = new GodforgeGlassBlock();
        GameRegistry.registerBlock(INSTANCE, GodforgeGlassItem.class, INSTANCE.getUnlocalizedName());
        GregTech_API.registerMachineBlock(INSTANCE, -1);
    }

    @Override
    public void breakBlock(World aWorld, int aX, int aY, int aZ, Block aBlock, int aMeta) {
        if (GregTech_API.isMachineBlock(this, aWorld.getBlockMetadata(aX, aY, aZ))) {
            GregTech_API.causeMachineUpdate(aWorld, aX, aY, aZ);
        }
    }

    @Override
    public void onBlockAdded(World aWorld, int aX, int aY, int aZ) {
        if (GregTech_API.isMachineBlock(this, aWorld.getBlockMetadata(aX, aY, aZ))) {
            GregTech_API.causeMachineUpdate(aWorld, aX, aY, aZ);
        }
    }
}
