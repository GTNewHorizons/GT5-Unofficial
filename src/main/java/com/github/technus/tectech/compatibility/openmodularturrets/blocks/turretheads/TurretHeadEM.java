package com.github.technus.tectech.compatibility.openmodularturrets.blocks.turretheads;

import static com.github.technus.tectech.TecTech.creativeTabTecTech;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import openmodularturrets.tileentity.turretbase.TurretBase;

import com.github.technus.tectech.compatibility.openmodularturrets.tileentity.turret.TileTurretHeadEM;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Created by Tec on 27/07/2017.
 */
public class TurretHeadEM extends Block implements ITileEntityProvider {

    public static TurretHeadEM INSTANCE;

    public TurretHeadEM() {
        super(Material.glass);
        setCreativeTab(creativeTabTecTech);
        setBlockUnbreakable();
        setResistance(6000000.0F);
        setStepSound(Block.soundTypeMetal);
        setBlockBounds(0.2F, 0.0F, 0.2F, 0.8F, 1F, 0.8F);
        setBlockName("turretHeadEM");
        // this.setBlockTextureName(Reference.MODID+":turretHeadEM");
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, int x, int y, int z) {
        return worldIn.getTileEntity(x + 1, y, z) instanceof TurretBase
                || worldIn.getTileEntity(x - 1, y, z) instanceof TurretBase
                || worldIn.getTileEntity(x, y + 1, z) instanceof TurretBase
                || worldIn.getTileEntity(x, y - 1, z) instanceof TurretBase
                || worldIn.getTileEntity(x, y, z + 1) instanceof TurretBase
                || worldIn.getTileEntity(x, y, z - 1) instanceof TurretBase;
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileTurretHeadEM();
    }

    public static void run() {
        INSTANCE = new TurretHeadEM();
        GameRegistry.registerBlock(INSTANCE, TurretHeadItemEM.class, INSTANCE.getUnlocalizedName());
        GameRegistry.registerTileEntity(TileTurretHeadEM.class, "TileTurretHeadEM");
    }
}
