package detrav.utils;

import java.util.HashMap;

import javax.annotation.Nonnull;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import gregtech.GTMod;
import gregtech.api.enums.UndergroundFluidNames;

public class FluidColors {

    private static boolean initialized;
    private static final HashMap<Integer, short[]> fluidColors = new HashMap<>();

    private static void generateFluidColors() {
        for (UndergroundFluidNames value : UndergroundFluidNames.values()) {
            final Fluid fluid = FluidRegistry.getFluid(value.name);
            if (fluid != null) {
                if (value.renderColor != null) {
                    fluidColors.put(fluid.getID(), value.renderColor);
                } else {
                    fluidColors.put(fluid.getID(), convertColorInt(fluid.getColor()));
                }
            } else {
                GTMod.GT_FML_LOGGER.error("[FluidColors] no registered fluid named " + value.name);
            }
        }
    }

    private static short[] convertColorInt(int color) {
        return new short[] { (short) (color << 16 & 0xff), (short) (color << 8 & 0xff), (short) (color & 0xff) };
    }

    @Nonnull
    public static short[] getColor(int fluidID) {
        if (!initialized) {
            generateFluidColors();
            initialized = true;
        }
        final short[] color = fluidColors.get(fluidID);
        if (color == null) {
            GTMod.GT_FML_LOGGER.error("Unknown fluid ID = " + fluidID + " This shouldn't happen!");
            return new short[] { 0, 0, 0, 0 };
        }
        return color;
    }

}
