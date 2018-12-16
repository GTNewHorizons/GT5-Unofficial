package com.github.bartimaeusnek.bartworks.common.tileentities;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_DrillerBase;
import net.minecraft.entity.player.InventoryPlayer;

public class GT_TileEntity_DEHP extends GT_MetaTileEntity_DrillerBase {

    public GT_TileEntity_DEHP(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_DEHP(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_TileEntity_DEHP(this.mName);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), "DrillingRig.png");
    }

    @Override
    public String[] getDescription() {
        return new String[0];
    }

    @Override
    protected ItemList getCasingBlockItem() {
        return ItemList.Casing_HeatProof;
    }

    @Override
    protected Materials getFrameMaterial() {
        return Materials.Tungsten;
    }

    @Override
    protected int getCasingTextureIndex() {
        return 13;
    }

    @Override
    protected int getMinTier() {
        return 5;
    }

    @Override
    protected boolean checkHatches() {
        return !this.mMaintenanceHatches.isEmpty() && !this.mOutputHatches.isEmpty() && !this.mInputHatches.isEmpty();
    }

    @Override
    protected void setElectricityStats() {



    }
}
