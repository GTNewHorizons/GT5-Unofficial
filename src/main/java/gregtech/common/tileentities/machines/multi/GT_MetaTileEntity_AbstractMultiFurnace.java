package gregtech.common.tileentities.machines.multi;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.interfaces.IHeatingCoil;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public abstract class GT_MetaTileEntity_AbstractMultiFurnace<T extends GT_MetaTileEntity_AbstractMultiFurnace<T>> extends GT_MetaTileEntity_EnhancedMultiBlockBase<T> {

    protected HeatingCoilLevel mCoilLevel;

    protected GT_MetaTileEntity_AbstractMultiFurnace(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected GT_MetaTileEntity_AbstractMultiFurnace(String aName) {
        super(aName);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    protected final boolean addCoil(Block aBlock, int aMeta) {
        if (aBlock instanceof IHeatingCoil) {
            if (mCoilLevel != null)
                return mCoilLevel == ((IHeatingCoil) aBlock).getCoilHeat(aMeta);
            mCoilLevel = ((IHeatingCoil) aBlock).getCoilHeat(aMeta);
            return true;
        }
        return false;
    }

    protected boolean addBottomHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return addMaintenanceToMachineList(aTileEntity, aBaseCasingIndex) ||
                addInputToMachineList(aTileEntity, aBaseCasingIndex) ||
                addOutputToMachineList(aTileEntity, aBaseCasingIndex) ||
                addEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerTick(ItemStack aStack) {
        return 20;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        // even though flipping doesn't really change the overall structure, but maybe someone want it somehow
        return (direction, rotation, flip) -> direction.offsetY == 0 && rotation.isNotRotated();
    }
}
