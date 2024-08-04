package gregtech.common.tileentities.machines.multi;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;

public abstract class GT_MetaTileEntity_AbstractMultiFurnace<T extends GT_MetaTileEntity_AbstractMultiFurnace<T>>
    extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<T> {

    private HeatingCoilLevel mCoilLevel;

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

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    public HeatingCoilLevel getCoilLevel() {
        return mCoilLevel;
    }

    public void setCoilLevel(HeatingCoilLevel aCoilLevel) {
        mCoilLevel = aCoilLevel;
    }
}
