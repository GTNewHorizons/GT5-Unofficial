package tectech.voidcraft.ship;

import net.minecraftforge.fluids.Fluid;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.Materials;

/**
 * The fuel FLUIDS of the Voidcraft mechanics (runtime registry — the bare-JVM model only knows engine types and
 * mB amounts; the fluid identity is a load-time concern).
 *
 * <p>
 * Engine families (the fuel tank, consumed by travel legs):
 *
 * <ul>
 * <li>Ion — Xenon (liquid)</li>
 * <li>Fusion — Water (liquid)</li>
 * <li>Antimatter — Semi-Stable Antimatter (liquid)</li>
 * </ul>
 *
 * <p>
 * Reactor launch fees (paid at the Gateway, scaled by the number of reactors of each type):
 *
 * <ul>
 * <li>Fusion Reactor — Deuterium (liquid)</li>
 * <li>Antimatter Reactor — Semi-Stable Antimatter (liquid)</li>
 * </ul>
 *
 * <p>
 * The baseline nozzles burn no fuel. {@link #init()} must run in the POST-init phase on both sides: BartWorks
 * registers its Werkstoff fluids (Xenon) during its own init, which is declared {@code after:tectech}, so the
 * load phase is too early (WerkstoffLoader.fluids is still empty there).
 */
public final class VoidcraftFuel {

    private static Fluid ionFuel;
    private static Fluid fusionFuel;
    private static Fluid antimatterFuel;
    private static Fluid reactorFusionFuel;
    private static Fluid reactorAntimatterFuel;
    private static boolean initialized;

    private VoidcraftFuel() {
        throw new AssertionError("Static registry");
    }

    /**
     * Resolve all fuel fluids (post-init phase, both sides). Idempotent.
     */
    public static synchronized void init() {
        if (initialized) {
            return;
        }
        ionFuel = WerkstoffLoader.Xenon.getFluidOrGas(1_000)
            .getFluid();
        fusionFuel = Materials.Water.mFluid;
        antimatterFuel = Materials.Antimatter.mFluid;
        reactorFusionFuel = Materials.Deuterium.mFluid;
        reactorAntimatterFuel = Materials.Antimatter.mFluid;
        initialized = true;
    }

    /**
     * @param type the engine family (null or a fuel-less family → null).
     * @return the tank fluid for that family, or null when the family burns no fuel (or is unresolved).
     */
    public static synchronized Fluid engineFuel(VoidcraftEngineType type) {
        if (type == null || !type.requiresFuel() || !initialized) {
            return null;
        }
        switch (type) {
            case ION:
                return ionFuel;
            case FUSION:
                return fusionFuel;
            case ANTIMATTER:
                return antimatterFuel;
            default:
                return null;
        }
    }

    /**
     * @param reactor the reactor cover (must be a reactor, per {@link VoidcraftCoverComponent#isReactor()}).
     * @return the launch-fee fluid for that reactor type, or null when it is not a reactor (or is unresolved).
     */
    public static synchronized Fluid reactorLaunchFluid(VoidcraftCoverComponent reactor) {
        if (!initialized) {
            return null;
        }
        if (reactor == VoidcraftCoverComponent.FUSION_REACTOR) {
            return reactorFusionFuel;
        }
        if (reactor == VoidcraftCoverComponent.ANTIMATTER_REACTOR) {
            return reactorAntimatterFuel;
        }
        return null;
    }
}
