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
        return null;
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
        return null;
    }

    @Override
    protected Materials getFrameMaterial() {
        return null;
    }

    @Override
    protected int getCasingTextureIndex() {
        return 0;
    }

    @Override
    protected int getMinTier() {
        return 0;
    }

    @Override
    protected boolean checkHatches() {
        return false;
    }

    @Override
    protected void setElectricityStats() {

    }
}
