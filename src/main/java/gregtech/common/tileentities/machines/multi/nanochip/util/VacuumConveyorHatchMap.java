package gregtech.common.tileentities.machines.multi.nanochip.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import gregtech.common.tileentities.machines.multi.nanochip.hatches.GT_MetaTileEntity_Hatch_VacuumConveyor;

// To keep track of colors of the conveyor hatches for faster routing
public class VacuumConveyorHatchMap {

    // Note that each color can result in multiple conveyor hatches. It's up to the user to determine how to interpret
    // this:
    // This class does not assume you want to pick the first hatch or distribute the components across all hatches.
    private final Map<Byte, ArrayList<GT_MetaTileEntity_Hatch_VacuumConveyor>> conveyorsByColor = new HashMap<>();
    private int totalNumHatches = 0;

    private boolean putWithColorUnchecked(byte color, GT_MetaTileEntity_Hatch_VacuumConveyor hatch) {
        ArrayList<GT_MetaTileEntity_Hatch_VacuumConveyor> hatches = findColoredHatches(color);
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

    public boolean addHatch(GT_MetaTileEntity_Hatch_VacuumConveyor hatch) {
        totalNumHatches += 1;
        byte color = hatch.getColorization();
        return putWithColorUnchecked(color, hatch);
    }

    /*
     * This method is probably not worth the added complexity, since the map gets rebuilt on every structure check
     * anyway
     * public void ensureConsistency() {
     * // For each valid color, check all entries in the map by that color to ensure they still have their color
     * for (byte color = 0; color < 16; ++color) {
     * ArrayList<GT_MetaTileEntity_Hatch_VacuumConveyor> hatches = findColoredHatches(color);
     * final byte finalColor = color;
     * hatches.removeIf(hatch -> {
     * byte realColor = hatch.getColorization();
     * // If color doesn't match the real color, put it in the correct entry and remove it from this list
     * if (finalColor != realColor) {
     * putWithColorUnchecked(realColor, hatch);
     * return true;
     * }
     * return false;
     * });
     * }
     * }
     */

    public ArrayList<GT_MetaTileEntity_Hatch_VacuumConveyor> findColoredHatches(byte color) {
        return conveyorsByColor.get(color);
    }

    // Count all hatches in the map
    public int size() {
        return totalNumHatches;
    }
}
