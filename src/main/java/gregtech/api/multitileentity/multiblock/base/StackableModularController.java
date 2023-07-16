package gregtech.api.multitileentity.multiblock.base;

import gregtech.api.multitileentity.interfaces.UpgradableModularMuTE;
import gregtech.api.util.GT_StructureUtilityMuTE.UpgradeCasings;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class StackableModularController<T extends StackableModularController<T>> extends StackableController<T> implements UpgradableModularMuTE {

    private Map<UpgradeCasings, Integer[]> mucMap;

    protected Map<UpgradeCasings, Integer[]> getMucMap() {
        if (mucMap == null) {
            mucMap = createMucMap();
        }
        return mucMap;
    }

    protected static Map<UpgradeCasings, Integer[]> createMucMap() {
        Map<UpgradeCasings, Integer[]> mucCount = new HashMap<>();
        mucCount.put(UpgradeCasings.Heater, new Integer[]{0, 0, 0, 0, 0});
        mucCount.put(UpgradeCasings.Insulator, new Integer[]{0, 0, 0, 0, 0});
        return mucCount;
    }

    @Override
    public void increaseMucCount(UpgradeCasings casingType, int tier) {
        Map<UpgradeCasings, Integer[]> mucCounters = getMucMap();
        Integer[] casingCount = mucCounters.get(casingType);

        switch (tier) {
            case 0, 1, 2 -> casingCount[0] += 1;
            case 3, 4, 5 -> casingCount[1] += 1;
            case 6, 7, 8 -> casingCount[2] += 1;
            case 9, 10, 11 -> casingCount[3] += 1;
            default -> casingCount[4] += 1;
        }
    }

    @Override
    public void resetMucCount() {
        Map<UpgradeCasings, Integer[]> mucCounters = getMucMap();
        mucCounters.forEach((type, casingCount) -> {
            Arrays.fill(casingCount, 0);
        });
    }
}
