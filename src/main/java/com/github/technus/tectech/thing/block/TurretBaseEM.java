package com.github.technus.tectech.thing.block;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.auxiliary.Reference;
import com.github.technus.tectech.thing.tileEntity.TileTurretBaseEM;
import cpw.mods.fml.common.Optional;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import openmodularturrets.blocks.turretbases.BlockAbstractTurretBase;
import openmodularturrets.tileentity.turretbase.TurretBaseTierFiveTileEntity;

/**
 * Created by Bass on 27/07/2017.
 */
public class TurretBaseEM extends BlockAbstractTurretBase {
    public TurretBaseEM(){
        super();
        setCreativeTab(TecTech.mainTab);
        this.setResistance(16);
        this.setBlockName("turretBaseEM");
        this.setStepSound(Block.soundTypeMetal);
        this.setBlockTextureName(Reference.MODID+":turretBaseEM");
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileTurretBaseEM();
    }

    public void registerBlockIcons(IIconRegister p_149651_1_) {
        super.registerBlockIcons(p_149651_1_);
        this.blockIcon = p_149651_1_.registerIcon(Reference.MODID+":turretBaseEM");
    }
}
