package gregtech.common.tileentities.machines.multi.purification;

import net.minecraft.item.ItemStack;

import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;

public abstract class GT_MetaTileEntity_PurificationUnitBase
    extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<GT_MetaTileEntity_PurificationUnitBase> {

    protected GT_MetaTileEntity_PurificationUnitBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected GT_MetaTileEntity_PurificationUnitBase(String aName) {
        super(aName);
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
}
