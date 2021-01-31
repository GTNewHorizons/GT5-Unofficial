package com.github.bartimaeusnek.crossmod.tectech.tileentites.multi.GT_Replacement;

import com.github.technus.tectech.mechanics.constructable.IConstructable;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_DynamoMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_Container_MultiMachineEM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_GUIContainer_MultiMachineEM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.util.GT_Recipe;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public abstract class TT_Abstract_GT_Replacement extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {

    protected TT_Abstract_GT_Replacement(int newId, String aName, String aNameRegional) {
        super(32765, aName, aNameRegional);
        GregTech_API.METATILEENTITIES[32765] = null;
        GregTech_API.METATILEENTITIES[newId] = this;
    }

    protected TT_Abstract_GT_Replacement(String aName) {
        super(aName);
    }

    protected void setInputFilters() {
        this.mInputBusses.forEach(x -> x.mRecipeMap = this.getRecipeMap());
        this.mInputHatches.forEach(x -> x.mRecipeMap = this.getRecipeMap());
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachineEM(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "EMDisplay.png", this.ePowerPassCover, false, true);
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_MultiMachineEM(aPlayerInventory, aBaseMetaTileEntity, this.ePowerPassCover, false, true);
    }

    @Override
    protected boolean cyclicUpdate_EM() {
        return false;
    }

    @Override
    public boolean isMachineBlockUpdateRecursive() {
        return true;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    protected void setEfficiencyAndOc(GT_Recipe gtRecipe) {
        this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;

        calculateOverclockedNessMulti(gtRecipe.mEUt, gtRecipe.mDuration, 1, getMaxInputVoltage());
    }

    public final boolean addEBFInputsBottom(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null)
            return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch)
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
        else
            return false;

        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus)
            return this.mInputBusses.add((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity);
        else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus)
            return this.mOutputBusses.add((GT_MetaTileEntity_Hatch_OutputBus) aMetaTileEntity);
        else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output)
            return this.mOutputHatches.add((GT_MetaTileEntity_Hatch_Output) aMetaTileEntity);
        else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input)
            return this.mInputHatches.add((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity);
        else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy)
            return this.mEnergyHatches.add((GT_MetaTileEntity_Hatch_Energy) aMetaTileEntity);
        else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo)
            return this.mDynamoHatches.add((GT_MetaTileEntity_Hatch_Dynamo) aMetaTileEntity);
        else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance)
            return this.mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance) aMetaTileEntity);
        else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_EnergyMulti)
            return this.eEnergyMulti.add((GT_MetaTileEntity_Hatch_EnergyMulti) aMetaTileEntity);
        else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_DynamoMulti)
            return this.eDynamoMulti.add((GT_MetaTileEntity_Hatch_DynamoMulti) aMetaTileEntity);
        return false;
    }
}
