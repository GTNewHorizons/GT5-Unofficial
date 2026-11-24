package gregtech.common.ec.multiblock;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.component.multiblock.ISmartHatchLogicComponent;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.common.ec.multiblock.internal.BaseMultiblockComponent;
import gregtech.common.tileentities.machines.IHatchWatcher;
import gregtech.common.tileentities.machines.ISmartInputHatch;

public class SmartHatchLogicComponent extends BaseMultiblockComponent implements ISmartHatchLogicComponent {

    private final List<ISmartInputHatch> mSmartInputHatches = new ArrayList<>();
    private boolean doPeriodicChecks = false;

    @Override
    public boolean tryAttachSmartHatch(@NotNull IMetaTileEntity part, int baseCasingIndex) {
        if (part instanceof ISmartInputHatch hatch) {
            if (!mSmartInputHatches.contains(hatch)) {
                mSmartInputHatches.add(hatch);
                hatch.addWatcher((IHatchWatcher) getMachine());
                doPeriodicChecks |= hatch.needsPeriodicChecks();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean needsPeriodicChecks() {
        return doPeriodicChecks;
    }

    @Override
    public void detachAllHatches() {
        for (var hatch : mSmartInputHatches) {
            hatch.removeWatcher(getMachine());
        }
        mSmartInputHatches.clear();
        doPeriodicChecks = false;
    }
}
