package com.github.technus.tectech.thing.block;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import eu.usrv.yamcore.blocks.BlockBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import static com.github.technus.tectech.auxiliary.Reference.MODID;

/**
 * Created by danie_000 on 17.12.2016.
 */
public final class QuantumGlass extends BlockBase {
    public static IIcon stuff;
    public static QuantumGlass INSTANCE;

    public QuantumGlass() {
        super(Material.iron);
        this.setBlockBounds(0, 0, 0, 1, 1, 1);
        setBlockName("quantumGlass");
        setHarvestLevel("wrench", 3);
        setHardness(50);
        setResistance(30);
        setLightOpacity(0);
        setStepSound(Block.soundTypeMetal);
        setBlockTextureName(MODID + ":blockQuantumGlass");
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

    //@Override
    //public boolean canRenderInPass(int pass) {
    //    return true;
    //}

    @Override
    public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        return 1;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess p_149646_1_, int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_) {
        Block block = p_149646_1_.getBlock(p_149646_2_, p_149646_3_, p_149646_4_);
        return block != this;// && super.shouldSideBeRendered(p_149646_1_, p_149646_2_, p_149646_3_, p_149646_4_, p_149646_5_);
    }

    @Override
    public int getRenderType() {
        return QuantumGlassRender.renderID;
    }

    @Override
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        super.registerBlockIcons(p_149651_1_);
        stuff = this.blockIcon;
    }

    public static void run(){
        INSTANCE=new QuantumGlass();
        GameRegistry.registerBlock(INSTANCE, INSTANCE.getUnlocalizedName());
    }
}
