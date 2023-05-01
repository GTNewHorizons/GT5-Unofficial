package com.github.technus.tectech.compatibility.openmodularturrets.blocks.turretbases;

import static com.github.technus.tectech.TecTech.creativeTabTecTech;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.github.technus.tectech.Reference;
import com.github.technus.tectech.compatibility.openmodularturrets.tileentity.turretbase.TileTurretBaseEM;

import cpw.mods.fml.common.registry.GameRegistry;
import openmodularturrets.blocks.turretbases.BlockAbstractTurretBase;
import openmodularturrets.handler.ConfigHandler;

/**
 * Created by Tec on 27/07/2017.
 */
public class TurretBaseEM extends BlockAbstractTurretBase {

    private final int MaxCharge = ConfigHandler.getBaseTierFiveMaxCharge();
    private final int MaxIO = ConfigHandler.getBaseTierFiveMaxIo();
    public static TurretBaseEM INSTANCE;

    public TurretBaseEM() {
        setCreativeTab(creativeTabTecTech);
        setResistance(16);
        setBlockName("turretBaseEM");
        setStepSound(Block.soundTypeMetal);
        setBlockTextureName(Reference.MODID + ":turretBaseEM");
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileTurretBaseEM(MaxCharge, MaxIO);
    }

    @Override
    public void registerBlockIcons(IIconRegister reg) {
        super.registerBlockIcons(reg);
        blockIcon = reg.registerIcon(Reference.MODID + ":turretBaseEM");
    }

    public static void run() {
        INSTANCE = new TurretBaseEM();
        GameRegistry.registerBlock(INSTANCE, TurretBaseItemEM.class, INSTANCE.getUnlocalizedName());
        GameRegistry.registerTileEntity(TileTurretBaseEM.class, "TileTurretBaseEM");
    }
}
