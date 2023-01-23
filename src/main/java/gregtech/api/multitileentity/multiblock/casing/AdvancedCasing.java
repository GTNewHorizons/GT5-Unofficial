package gregtech.api.multitileentity.multiblock.casing;

import gregtech.api.multitileentity.interfaces.IMultiBlockController;
import gregtech.api.multitileentity.multiblock.base.MultiBlockPart;

public abstract class AdvancedCasing extends MultiBlockPart {

    @Override
    public void setTarget(IMultiBlockController aTarget, int aAllowedModes) {
        super.setTarget(aTarget, aAllowedModes);
        if (mTarget != null) customWork(mTarget);
    }

    protected abstract void customWork(IMultiBlockController aTarget);
}
