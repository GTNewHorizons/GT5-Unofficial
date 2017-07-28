package com.github.technus.tectech.thing.tileEntity;

import com.github.technus.tectech.elementalMatter.classes.cElementalInstanceStackMap;
import com.github.technus.tectech.elementalMatter.interfaces.iElementalInstanceContainer;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_InputElemental;
import cpw.mods.fml.common.Optional;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import openmodularturrets.tileentity.turretbase.TurretBaseTierFiveTileEntity;

/**
 * Created by Bass on 27/07/2017.
 */

public class TileTurretBaseEM extends TurretBaseTierFiveTileEntity implements iElementalInstanceContainer {
    public TileTurretBaseEM(int MaxEnergyStorage, int MaxIO) {
        super(MaxEnergyStorage, MaxIO);
    }

    @Override
    public int getSizeInventory() {
        return 13;
    }

    public String getInventoryName() {
        return "modtur.turretbasefive";
    }

    @Optional.Method(
            modid = "OpenComputers"
    )
    public String getComponentName() {
        return "turretBaseEM";
    }

    @Override
    public int getBaseTier() {
        return 5;
    }

    @Override
    public cElementalInstanceStackMap getContainerHandler() {
        World worldIn=getWorldObj();
        TileEntity te;
        if((te=worldIn.getTileEntity(xCoord + 1, yCoord, zCoord)) instanceof IGregTechTileEntity)
            if(((IGregTechTileEntity)te).getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_InputElemental)
                return ((GT_MetaTileEntity_Hatch_InputElemental)((IGregTechTileEntity) te).getMetaTileEntity()).getContainerHandler();

        if((te=worldIn.getTileEntity(xCoord - 1, yCoord, zCoord)) instanceof IGregTechTileEntity)
            if(((IGregTechTileEntity)te).getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_InputElemental)
                return ((GT_MetaTileEntity_Hatch_InputElemental)((IGregTechTileEntity) te).getMetaTileEntity()).getContainerHandler();

        if((te=worldIn.getTileEntity(xCoord, yCoord+1, zCoord)) instanceof IGregTechTileEntity)
            if(((IGregTechTileEntity)te).getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_InputElemental)
                return ((GT_MetaTileEntity_Hatch_InputElemental)((IGregTechTileEntity) te).getMetaTileEntity()).getContainerHandler();

        if((te=worldIn.getTileEntity(xCoord, yCoord-1, zCoord)) instanceof IGregTechTileEntity)
            if(((IGregTechTileEntity)te).getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_InputElemental)
                return ((GT_MetaTileEntity_Hatch_InputElemental)((IGregTechTileEntity) te).getMetaTileEntity()).getContainerHandler();

        if((te=worldIn.getTileEntity(xCoord, yCoord, zCoord+1)) instanceof IGregTechTileEntity)
            if(((IGregTechTileEntity)te).getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_InputElemental)
                return ((GT_MetaTileEntity_Hatch_InputElemental)((IGregTechTileEntity) te).getMetaTileEntity()).getContainerHandler();

        if((te=worldIn.getTileEntity(xCoord, yCoord, zCoord-1)) instanceof IGregTechTileEntity)
            if(((IGregTechTileEntity)te).getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_InputElemental)
                return ((GT_MetaTileEntity_Hatch_InputElemental)((IGregTechTileEntity) te).getMetaTileEntity()).getContainerHandler();

        return null;
    }

    @Override
    @Deprecated
    public float purgeOverflow() {
        throw new NoSuchMethodError("This is not a valid use");
    }
}
