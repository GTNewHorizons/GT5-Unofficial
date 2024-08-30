package gregtech.common.tileentities.machines.multi.nanochip.util;

import static gregtech.api.util.GT_Utility.filterValidMTEs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import gregtech.common.tileentities.machines.multi.nanochip.hatches.GT_MetaTileEntity_Hatch_VacuumConveyor;

// To keep track of colors of the conveyor hatches for faster routing
public class VacuumConveyorHatchMap<T extends GT_MetaTileEntity_Hatch_VacuumConveyor> {

    // Note that each color can result in multiple conveyor hatches. It's up to the user to determine how to interpret
    // this:
    // This class does not assume you want to pick the first hatch or distribute the components across all hatches.
    private final Map<Byte, ArrayList<T>> conveyorsByColor = new HashMap<>();
    private int totalNumHatches = 0;

    private boolean putWithColorUnchecked(byte color, T hatch) {
        ArrayList<T> hatches = findColoredHatches(color);
        if (hatches != null) {
            return hatches.add(hatch);
        }
        conveyorsByColor.put(color, new ArrayList<>(Collections.singletonList(hatch)));
        return true;
    }

    /**
     * Clear the internal data structure of hatches. This will need to be called on every structure check to avoid
     * duplicate entries
     */
    public void clear() {
        this.conveyorsByColor.clear();
        totalNumHatches = 0;
    }

    public boolean addHatch(T hatch) {
        totalNumHatches += 1;
        byte color = hatch.getColorization();
        return putWithColorUnchecked(color, hatch);
    }

    public void fixConsistency() {
        // For each valid color, check all entries in the map by that color to ensure they still have their color
        for (byte color = 0; color < 16; ++color) {
            ArrayList<T> hatches = findColoredHatches(color);
            if (hatches == null) continue;
            final byte finalColor = color;
            hatches.removeIf(hatch -> {
                byte realColor = hatch.getColorization();
                // If color doesn't match the real color, put it in the correct entry and remove it from this list
                if (finalColor != realColor) {
                    putWithColorUnchecked(realColor, hatch);
                    return true;
                }
                return false;
            });
        }
    }

    public ArrayList<T> findColoredHatches(byte color) {
        return conveyorsByColor.get(color);
    }

    public T findAnyColoredHatch(byte color) {
        ArrayList<T> hatches = findColoredHatches(color);
        if (hatches == null) return null;
        for (T hatch : filterValidMTEs(hatches)) {
            // Ensure that the color matches the expected color, since hatches can be recolored in between rebuilds
            // of the hatch map
            if (hatch.getBaseMetaTileEntity()
                .getColorization() != color) {
                // If the color did not match, we found an inconsistency in the hatch map, so fix it immediately
                // and skip this hatch, since it's not a match
                fixConsistency();
                continue;
            }
            return hatch;
        }
        return null;
    }

    public Collection<ArrayList<T>> allHatches() {
        return conveyorsByColor.values();
    }

    public Map<Byte, ArrayList<T>> hatchMap() {
        return this.conveyorsByColor;
    }

    // Count all hatches in the map
    public int size() {
        return totalNumHatches;
    }
}
