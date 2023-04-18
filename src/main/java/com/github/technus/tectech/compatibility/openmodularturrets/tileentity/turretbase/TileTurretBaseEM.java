package com.github.technus.tectech.compatibility.openmodularturrets.tileentity.turretbase;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import openmodularturrets.tileentity.turretbase.TurretBaseTierFiveTileEntity;

import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMInstanceStackMap;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_InputElemental;

import cpw.mods.fml.common.Optional;
import gregtech.api.enums.Mods;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

/**
 * Created by Bass on 27/07/2017.
 */
public class TileTurretBaseEM extends TurretBaseTierFiveTileEntity {

    public TileTurretBaseEM(int MaxEnergyStorage, int MaxIO) {
        super(MaxEnergyStorage, MaxIO);
    }

    @Override
    @Optional.Method(modid = Mods.Names.OPEN_COMPUTERS)
    public String getComponentName() {
        return "turretBaseEM";
    }

    public final EMInstanceStackMap getContainerHandler() {
        World worldIn = getWorldObj();
        TileEntity te;
        if ((te = worldIn.getTileEntity(xCoord + 1, yCoord, zCoord)) instanceof IGregTechTileEntity
                && ((IGregTechTileEntity) te).getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_InputElemental) {
            return getFromHatch(
                    (GT_MetaTileEntity_Hatch_InputElemental) ((IGregTechTileEntity) te).getMetaTileEntity());
        }

        if ((te = worldIn.getTileEntity(xCoord - 1, yCoord, zCoord)) instanceof IGregTechTileEntity
                && ((IGregTechTileEntity) te).getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_InputElemental) {
            return getFromHatch(
                    (GT_MetaTileEntity_Hatch_InputElemental) ((IGregTechTileEntity) te).getMetaTileEntity());
        }

        if ((te = worldIn.getTileEntity(xCoord, yCoord + 1, zCoord)) instanceof IGregTechTileEntity
                && ((IGregTechTileEntity) te).getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_InputElemental) {
            return getFromHatch(
                    (GT_MetaTileEntity_Hatch_InputElemental) ((IGregTechTileEntity) te).getMetaTileEntity());
        }

        if ((te = worldIn.getTileEntity(xCoord, yCoord - 1, zCoord)) instanceof IGregTechTileEntity
                && ((IGregTechTileEntity) te).getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_InputElemental) {
            return getFromHatch(
                    (GT_MetaTileEntity_Hatch_InputElemental) ((IGregTechTileEntity) te).getMetaTileEntity());
        }

        if ((te = worldIn.getTileEntity(xCoord, yCoord, zCoord + 1)) instanceof IGregTechTileEntity
                && ((IGregTechTileEntity) te).getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_InputElemental) {
            return getFromHatch(
                    (GT_MetaTileEntity_Hatch_InputElemental) ((IGregTechTileEntity) te).getMetaTileEntity());
        }

        if ((te = worldIn.getTileEntity(xCoord, yCoord, zCoord - 1)) instanceof IGregTechTileEntity
                && ((IGregTechTileEntity) te).getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_InputElemental) {
            return getFromHatch(
                    (GT_MetaTileEntity_Hatch_InputElemental) ((IGregTechTileEntity) te).getMetaTileEntity());
        }

        return null;
    }

    private EMInstanceStackMap getFromHatch(GT_MetaTileEntity_Hatch_InputElemental hatch) {
        hatch.onValueUpdate((byte) 4);
        hatch.onTexturePageUpdate((byte) 8);
        return hatch.getContentHandler();
    }
}
