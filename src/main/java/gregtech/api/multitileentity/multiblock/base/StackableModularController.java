package gregtech.api.multitileentity.multiblock.base;

import gregtech.api.multitileentity.interfaces.UpgradableModularMuTE;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class StackableModularController<T extends StackableModularController<T>> extends StackableController<T> implements UpgradableModularMuTE {

    protected Map<String, Integer[]> mucMap = createMucMap();

    protected static Map<String, Integer[]> createMucMap() {
        Map<String, Integer[]> mucCount = new HashMap<>();
        mucCount.put("heater", new Integer[]{0, 0, 0, 0, 0});
        mucCount.put("insulator", new Integer[]{0, 0, 0, 0, 0});
        return mucCount;
    }

    @Override
    public void increaseMucCount(String casingType, int tier) {
        Integer[] casingCount = mucMap.get(casingType);

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
        mucMap.forEach((type, casingCount) -> {
            Arrays.fill(casingCount, 0);
        });
    }
}
