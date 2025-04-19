package gregtech.common.covers.modes;

import com.cleanroommc.modularui.api.drawable.IKey;

import gregtech.api.interfaces.modularui.KeyProvider;
import gregtech.api.util.GTUtility;

public enum MachineProcessingCondition implements KeyProvider {

    ALWAYS(IKey.str(GTUtility.trans("224", "Always On"))),
    CONDITIONAL(IKey.str(GTUtility.trans("343", "Use Machine Processing " + "State"))),
    INVERTED(IKey.str(GTUtility.trans("343.1", "Use Inverted Machine " + "Processing State")));

    private final IKey key;

    MachineProcessingCondition(IKey key) {
        this.key = key;
    }

    @Override
    public IKey getKey() {
        return this.key;
    }
}
