package gregtech.api.multitileentity.multiblock.base;

import gregtech.api.multitileentity.interfaces.UpgradableModularMuTE;

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
        if (tier <= 2) {
            casingCount[0] += 1;
        }
        else if (tier <= 5) {
            casingCount[1] += 1;
        }
        else if (tier <= 8) {
            casingCount[2] += 1;
        }
        else if (tier <= 11) {
            casingCount[3] += 1;
        }
        else {
            casingCount[4] += 1;
        }
        mucMap.put(casingType, casingCount);
    }

    @Override
    public void resetMucCount() {
        mucMap = createMucMap();
    }
}
