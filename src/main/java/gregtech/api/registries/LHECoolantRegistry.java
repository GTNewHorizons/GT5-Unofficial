package gregtech.api.registries;

import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.common.tileentities.machines.multi.MTEHeatExchanger;

public class LHECoolantRegistry {

    private static final Map<String, LHECoolantInfo> lheCoolants = new HashMap<>();

    /**
     * Registers a coolant for use in Large Heat Exchangers and Whakawhiti Weras.
     * See the constants in {@link #registerBaseCoolants()} as a reference for what the multipliers should be.
     * The multipliers are used in
     * {@link MTEHeatExchanger#checkProcessing()}
     * and {@link MTEHeatExchanger#onRunningTick()}.
     *
     * @param coldFluidName        The fluid name of the resulting cold coolant
     * @param hotFluidName         The fluid name of the input hot coolant
     * @param steamMultiplier      The steam multiplier
     * @param superheatedThreshold The super heated threshold multiplier - see the constants in
     *                             {@link #registerBaseCoolants()} for a reference
     */
    public static void registerCoolant(String coldFluidName, String hotFluidName, double steamMultiplier,
        double superheatedThreshold) {
        var coolant = new LHECoolantInfo(coldFluidName, hotFluidName, steamMultiplier, superheatedThreshold);

        lheCoolants.put(coldFluidName, coolant);
        lheCoolants.put(hotFluidName, coolant);
    }

    public static LHECoolantInfo getCoolant(String fluidName) {
        return lheCoolants.get(fluidName);
    }

    public static LHECoolantInfo getCoolant(Fluid fluid) {
        return lheCoolants.get(fluid.getName());
    }

    public static void registerBaseCoolants() {
        // I have no idea where these constants originally came from, but I've preserved the comments from
        // GT_MetaTileEntity_HeatExchanger

        registerCoolant(
            "ic2pahoehoelava",
            "lava",
            1.0 / 5.0, // lava is not boosted
            1.0 / 4.0 // unchanged
        );

        registerCoolant(
            "ic2coolant",
            "ic2hotcoolant",
            1.0 / 2.0, // was boosted x2 on top of x5 -> total x10 -> nerf with this code back to 5x
            1.0 / 5.0 // 10x smaller since the Hot Things production in reactor is the same
        );

        registerCoolant(
            "molten.solarsaltcold",
            "molten.solarsalthot",
            2.5, // Solar Salt to Steam ratio is 5x higher than Hot Coolant's ratio
            1.0 / 25.0 // Given that, multiplier is 5x higher and threshold is 5x lower
        );
    }

    public static class LHECoolantInfo {

        public final String coldFluidName, hotFluidName;
        public final double steamMultiplier, superheatedThreshold;

        private Fluid coldFluid, hotFluid;

        public LHECoolantInfo(String coldFluidName, String hotFluidName, double steamMultiplier,
            double superheatedThreshold) {
            this.coldFluidName = coldFluidName;
            this.hotFluidName = hotFluidName;
            this.steamMultiplier = steamMultiplier;
            this.superheatedThreshold = superheatedThreshold;
        }

        public Fluid getColdFluid() {
            if (coldFluid == null) {
                coldFluid = FluidRegistry.getFluid(coldFluidName);
            }

            return coldFluid;
        }

        public Fluid getHotFluid() {
            if (hotFluid == null) {
                hotFluid = FluidRegistry.getFluid(hotFluidName);
            }

            return hotFluid;
        }

        public FluidStack getColdFluid(int amount) {
            return new FluidStack(getColdFluid(), amount);
        }

        public FluidStack getHotFluid(int amount) {
            return new FluidStack(getHotFluid(), amount);
        }
    }
}
