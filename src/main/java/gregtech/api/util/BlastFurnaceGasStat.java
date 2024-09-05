package gregtech.api.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.minecraftforge.fluids.FluidStack;

import bartworks.system.material.Werkstoff;
import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.Materials;

public class BlastFurnaceGasStat {

    public enum Gases {

        Nitrogen(new BlastFurnaceGasStat(Materials.Nitrogen.getGas(1L), 1.0, 1.0)),
        Helium(new BlastFurnaceGasStat(Materials.Helium.getGas(1L), 0.9, 1.0)),
        Argon(new BlastFurnaceGasStat(Materials.Argon.getGas(1L), 0.8, 0.85)),
        Radon(new BlastFurnaceGasStat(Materials.Radon.getGas(1L), 0.7, 0.7)),
        Neon(new BlastFurnaceGasStat(WerkstoffLoader.Neon)),
        Krypton(new BlastFurnaceGasStat(WerkstoffLoader.Krypton)),
        Xenon(new BlastFurnaceGasStat(WerkstoffLoader.Xenon)),
        Oganesson(new BlastFurnaceGasStat(WerkstoffLoader.Oganesson));

        public final BlastFurnaceGasStat stat;

        Gases(BlastFurnaceGasStat stat) {
            this.stat = stat;
        }
    }

    public static List<BlastFurnaceGasStat> NOBLE_GASES;
    public static List<BlastFurnaceGasStat> ANAEROBE_GASES;
    public static List<BlastFurnaceGasStat> NOBLE_AND_ANAEROBE_GASES;
    FluidStack gas;
    double recipeTimeMultiplier;
    double recipeConsumedAmountMultiplier;

    public BlastFurnaceGasStat(FluidStack gas, double recipeTimeMultiplier, double recipeConsumedAmountMultiplier) {
        this.gas = gas;
        this.recipeTimeMultiplier = recipeTimeMultiplier;
        this.recipeConsumedAmountMultiplier = recipeConsumedAmountMultiplier;
    }

    public BlastFurnaceGasStat(Werkstoff material) {
        this(
            material.getFluidOrGas(1),
            material.getStats()
                .getEbfGasRecipeTimeMultiplier(),
            material.getStats()
                .getEbfGasRecipeConsumedAmountMultiplier());
    }

    public static Collection<BlastFurnaceGasStat> getNobleGases() {
        if (NOBLE_GASES == null) {
            NOBLE_GASES = Arrays.asList(
                Gases.Helium.stat,
                Gases.Argon.stat,
                Gases.Radon.stat,
                Gases.Neon.stat,
                Gases.Krypton.stat,
                Gases.Xenon.stat,
                Gases.Oganesson.stat);
        }
        return NOBLE_GASES;
    }

    public static Collection<BlastFurnaceGasStat> getAnaerobeGases() {
        if (ANAEROBE_GASES == null) {
            ANAEROBE_GASES = Arrays.asList(Gases.Nitrogen.stat, Gases.Xenon.stat, Gases.Oganesson.stat);
        }
        return ANAEROBE_GASES;
    }

    public static Collection<BlastFurnaceGasStat> getNobleAndAnaerobeGases() {
        if (NOBLE_AND_ANAEROBE_GASES == null) {
            NOBLE_AND_ANAEROBE_GASES = Arrays.asList(
                Gases.Nitrogen.stat,
                Gases.Helium.stat,
                Gases.Argon.stat,
                Gases.Radon.stat,
                Gases.Neon.stat,
                Gases.Krypton.stat,
                Gases.Xenon.stat,
                Gases.Oganesson.stat);
        }
        return NOBLE_AND_ANAEROBE_GASES;
    }
}
