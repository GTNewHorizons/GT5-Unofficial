package gtPlusPlus.xmod.thermalfoundation.fluid;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class TFFluids {

    public static Fluid fluidPyrotheum;
    public static Fluid fluidCryotheum;
    public static Fluid fluidEnder;

    public static void preInit() {
        final Fluid pyrotheum = FluidRegistry.getFluid("pyrotheum");
        final Fluid cryotheum = FluidRegistry.getFluid("cryotheum");
        final Fluid ender = FluidRegistry.getFluid("ender");

        fluidPyrotheum = pyrotheum;
        fluidCryotheum = cryotheum;
        fluidEnder = ender;

    }

    public static void init() {}

    public static void postInit() {}
}
