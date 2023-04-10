package com.github.technus.tectech.thing.block;

import static com.github.technus.tectech.Reference.MODID;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import eu.usrv.yamcore.blocks.BlockBase;

/**
 * Created by danie_000 on 17.12.2016.
 */
public final class QuantumStuffBlock extends BlockBase {

    public static IIcon stuff;
    public static int renderID;
    public static QuantumStuffBlock INSTANCE;

    public QuantumStuffBlock() {
        super(Material.iron);
        setBlockBounds(0, 0, 0, 1, 1, 1);
        setBlockName("quantumStuff");
        setHarvestLevel("wrench", 0);
        setHardness(500);
        setResistance(1);
        setLightOpacity(0);
        setBlockTextureName(MODID + ":blockQuantumStuff");
    }

    @Override
    public boolean isBeaconBase(IBlockAccess worldObj, int x, int y, int z, int beaconX, int beaconY, int beaconZ) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        super.registerBlockIcons(p_149651_1_);
        stuff = blockIcon;
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
    public boolean shouldSideBeRendered(IBlockAccess p_149646_1_, int p_149646_2_, int p_149646_3_, int p_149646_4_,
            int p_149646_5_) {
        return false;
    }

    @Override
    public int getRenderType() {
        return renderID;
    }

    public static void run() {
        INSTANCE = new QuantumStuffBlock();
        GameRegistry.registerBlock(INSTANCE, INSTANCE.getUnlocalizedName());
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        return new ArrayList<>();
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return null;
    }
}
