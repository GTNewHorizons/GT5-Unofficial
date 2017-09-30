package com.github.technus.tectech.thing.block;

import com.github.technus.tectech.thing.tileEntity.ReactorSimTileEntity;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.block.TileEntityBlock;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import static com.github.technus.tectech.auxiliary.Reference.MODID;

/**
 * Created by danie_000 on 30.09.2017.
 */
public class ReactorSimBlock extends Block implements ITileEntityProvider {
    public static ReactorSimBlock INSTANCE;
    public static IIcon stuff;

    public ReactorSimBlock() {
        super(Material.iron);
        this.setBlockBounds(0, 0, 0, 1, 1, 1);
        setBlockName("reactorSim");
        setHarvestLevel("wrench", 3);
        setHardness(50);
        setResistance(30);
        setLightOpacity(0);
        setStepSound(Block.soundTypeMetal);
        setBlockTextureName(MODID + ":blockReactorSimulator");
    }

    @Override
    public boolean isOpaqueCube() {
        return true;
    }

    @Override
    public boolean getCanBlockGrass() {
        return true;
    }

    @Override
    public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        super.registerBlockIcons(p_149651_1_);
        stuff = this.blockIcon;
    }

    public static void run() {
        INSTANCE = new ReactorSimBlock();
        GameRegistry.registerBlock(INSTANCE, ReactorSimItem.class, INSTANCE.getUnlocalizedName());
        GameRegistry.registerTileEntity(ReactorSimTileEntity.class,MODID+"_reactorSim");
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new ReactorSimTileEntity();
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float a, float b, float c) {
        if(entityPlayer.isSneaking()) {
            return false;
        } else {
            TileEntity te = world.getTileEntity(x,y,z);
            return te instanceof IHasGui && (!IC2.platform.isSimulating() || IC2.platform.launchGui(entityPlayer, (IHasGui) te));
        }
    }

    public void onNeighborBlockChange(World world, int x, int y, int z, Block srcBlock) {
        TileEntity te = world.getTileEntity(x,y,z);
        if(te instanceof TileEntityBlock) {
            ((TileEntityBlock)te).onNeighborUpdate(srcBlock);
        }

    }
}
