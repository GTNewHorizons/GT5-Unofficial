package detrav.utils;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import gregtech.GTMod;
import gregtech.api.enums.UndergroundFluidNames;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;

public class FluidColors {

    private static boolean initialized;
    private static final Int2IntOpenHashMap fluidIDToARGB = new Int2IntOpenHashMap();

    private static void generateFluidColors() {
        for (UndergroundFluidNames value : UndergroundFluidNames.values()) {
            final Fluid fluid = FluidRegistry.getFluid(value.name);
            if (fluid != null) {
                if (value.colorRGB != -1) {
                    fluidIDToARGB.put(fluid.getID(), value.colorRGB);
                } else {
                    fluidIDToARGB.put(fluid.getID(), fluid.getColor());
                }
            } else {
                GTMod.GT_FML_LOGGER.error("[FluidColors] no registered fluid named " + value.name);
            }
        }
    }

    public static int getColorARGB(int fluidID) {
        if (!initialized) {
            generateFluidColors();
            initialized = true;
        }
        return fluidIDToARGB.get(fluidID);
    }

}
