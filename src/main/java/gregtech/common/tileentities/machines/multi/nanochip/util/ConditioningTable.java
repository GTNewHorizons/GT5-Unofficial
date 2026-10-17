package gregtech.common.tileentities.machines.multi.nanochip.util;

import static gtPlusPlus.core.material.MaterialsElements.STANDALONE.DRAGON_METAL;
import static gtPlusPlus.core.material.MaterialsElements.STANDALONE.HYPOGEN;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.Materials;
import gtPlusPlus.core.material.Material;

/**
 * Entropy, enthalpy and consumption of the fluids that condition the Exotic Entangle Engine. Enthalpy
 * runs from -1.25 to 1.25 and entropy from 0 to 0.1. Stability is A*exp(S - H/T).
 */
public class ConditioningTable {

    // Stability of an empty input hatch, the A of the formula.
    public static final float STABILITY_SCALE = 100f;
    // Floor on the operating temperature, to keep the 1/T term bounded.
    public static final float MIN_TEMPERATURE = 0.2f;
    // Entropy of the entanglement process itself.
    public static final float MODULE_ENTROPY = 0.1f;
    // Enthalpy of the entanglement process itself.
    public static final float MODULE_ENTHALPY = 1.25f;

    public static class Entry {

        public final FluidStack fluid;
        public final float enthalpy;
        public final float entropy;
        public final int consumption;
        public final int consumptionDivisor;

        Entry(FluidStack fluid, float enthalpy, float entropy, int consumption, int consumptionDivisor) {
            this.fluid = fluid;
            this.enthalpy = enthalpy;
            this.entropy = entropy;
            this.consumption = consumption;
            this.consumptionDivisor = consumptionDivisor;
        }
    }

    private static final List<Entry> ENTRIES = new ArrayList<>();

    static {
        add(Materials.ExcitedDTEC, 0.125f, 0.04f, 10000);
        add(Materials.ExcitedDTSC, 0.125f, 0.04f, 10000, 2);
        add(Materials.QuarkGluonPlasma, 0.375f, 0.1f, 10000);
        add(Materials.DTR, 0.875f, 0.075f, 20000);
        add(Materials.RawStarMatter, 1.25f, 0.085f, 200, 2);
        add(Materials.SpaceTime, -1.25f, 0.025f, 1152);
        add(Materials.Protomatter, -1.125f, 0.025f, 1000);
        add(Materials.Antimatter, 0.25f, 0.04f, 1000);
        add(DRAGON_METAL, -0.375f, 0.055f, 576);
        add(HYPOGEN, -0.375f, 0.06f, 2304);
        add(Materials.Infinity, -0.625f, 0.015f, 4608);
        add(Materials.ZeroEntropyCoolant, -1.25f, 0f, 50000);
        add(Materials.SixPhasedCopper, 0.25f, 0.075f, 1152);
        add(Materials.MagMatter, 0.875f, 0.1f, 9216, 3);
        add(Materials.Eternity, -1.25f, 0.005f, 9216, 3);
    }

    private static void add(Materials material, float enthalpy, float entropy, int consumption) {
        add(material, enthalpy, entropy, consumption, 1);
    }

    private static void add(Materials material, float enthalpy, float entropy, int consumption,
        int consumptionDivisor) {
        add(fluidOf(material), enthalpy, entropy, consumption, consumptionDivisor);
    }

    private static void add(Material material, float enthalpy, float entropy, int consumption) {
        add(material.getFluidStack(1), enthalpy, entropy, consumption, 1);
    }

    private static void add(FluidStack fluid, float enthalpy, float entropy, int consumption, int consumptionDivisor) {
        if (fluid == null) return;
        ENTRIES.add(new Entry(fluid, enthalpy, entropy, consumption, consumptionDivisor));
    }

    private static FluidStack fluidOf(Materials material) {
        FluidStack fluid = material.getFluid(1);
        if (fluid == null) fluid = material.getMolten(1);
        if (fluid == null) fluid = material.getPlasma(1);
        return fluid;
    }

    public static List<Entry> getEntries() {
        return Collections.unmodifiableList(ENTRIES);
    }

    private static Entry find(FluidStack stack) {
        if (stack == null) return null;
        for (Entry entry : ENTRIES) {
            if (entry.fluid.isFluidEqual(stack)) return entry;
        }
        return null;
    }

    public static float[] getHS(FluidStack stack) {
        Entry entry = find(stack);
        return entry == null ? null : new float[] { entry.enthalpy, entry.entropy };
    }

    public static int getConsumption(FluidStack stack) {
        Entry entry = find(stack);
        return entry == null ? 0 : entry.consumption;
    }

    public static float getConsumptionMultiplier(List<FluidStack> fluids) {
        Set<Integer> applied = new HashSet<>();
        float multiplier = 1f;
        for (FluidStack fluid : fluids) {
            Entry entry = find(fluid);
            if (entry != null && entry.consumptionDivisor > 1 && applied.add(entry.consumptionDivisor)) {
                multiplier /= entry.consumptionDivisor;
            }
        }
        return multiplier;
    }

    public static float getStabilityTarget(float enthalpy, float entropy, float temperatureRatio) {
        float temperature = Math.max(temperatureRatio, MIN_TEMPERATURE);
        float exponent = entropy + MODULE_ENTROPY - (enthalpy + MODULE_ENTHALPY) / temperature;
        float value = STABILITY_SCALE * (float) Math.exp(exponent);
        return Math.max(0f, Math.min(100f, value));
    }
}
