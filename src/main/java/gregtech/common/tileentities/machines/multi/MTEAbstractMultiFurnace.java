package gregtech.common.tileentities.machines.multi;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;

public abstract class MTEAbstractMultiFurnace<T extends MTEAbstractMultiFurnace<T>>
    extends MTEExtendedPowerMultiBlockBase<T> {

    private HeatingCoilLevel mCoilLevel;

    protected MTEAbstractMultiFurnace(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected MTEAbstractMultiFurnace(String aName) {
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
