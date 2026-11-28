package gregtech.api.ec.component.multiblock;

import static gregtech.api.util.GTUtility.validMTEList;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.ComponentKey;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.util.GTUtility;

public interface IMultiblockEnergyHatchComponent extends IHatchContainerComponent {

    ComponentKey.ListKey<IMultiblockEnergyHatchComponent> KEY = ComponentKey.list();

    @NotNull
    List<MTEHatch> getEnergyHatches();

    @NotNull
    default Iterable<MTEHatch> getValidEnergyHatches() {
        return validMTEList(getEnergyHatches());
    }

    default long getStoredEnergy() {
        long stored = 0;
        for (var hatch : validMTEList(getEnergyHatches())) {
            var igte = hatch.getBaseMetaTileEntity();
            if (igte != null) {
                long eu = igte.getStoredEU();
                if (eu > 0) stored = GTUtility.addSafe(stored, eu);
            }
        }
        return stored;
    }
}
