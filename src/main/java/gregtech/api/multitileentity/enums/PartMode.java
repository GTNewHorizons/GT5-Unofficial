package gregtech.api.multitileentity.enums;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.StatCollector;

public enum PartMode {

    NOTHING,
    ITEM_INPUT,
    ITEM_OUTPUT,
    FLUID_INPUT,
    FLUID_OUTPUT,
    ENERGY_INPUT,
    ENERGY_OUTPUT;

    private static final String MODE_START = "gt.mode.name.";
    private final int val;
    private final String unlocalized;

    PartMode() {
        val = 1 << ordinal();
        unlocalized = MODE_START + toString().toLowerCase();
    }

    public int getValue() {
        return val;
    }

    public String getTranslated() {
        return StatCollector.translateToLocal(unlocalized);
    }

    public static List<PartMode> getPartModesFromBitmask(int bitmask) {
        List<PartMode> list = new ArrayList<>();
        for (PartMode mode : values()) {
            if ((mode.getValue() & bitmask) == 0) continue;
            list.add(mode);
        }
        return list;
    }
}
