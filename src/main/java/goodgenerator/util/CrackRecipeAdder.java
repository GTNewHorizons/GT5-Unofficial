package goodgenerator.util;

import static gregtech.api.recipe.RecipeMaps.crackingRecipes;
import static gregtech.api.recipe.RecipeMaps.distillationTowerRecipes;
import static gregtech.api.recipe.RecipeMaps.distilleryRecipes;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.util.GTRecipeBuilder;

public class CrackRecipeAdder {

    static float[] coe1 = { 1.25f, 1.2f, 1.1f, 0.9f, 0.85f, 0.8f, 0.75f };
    static float[] coe2 = { 1.4f, 1.25f, 1.2f, 0.8f, 0.75f, 0.7f, 0.65f };
    static float[] coe3 = { 1.6f, 1.5f, 1.45f, 0.7f, 0.6f, 0.55f, 0.45f };

    public static void crackerAdder(FluidStack inputFluid, FluidStack cracker, FluidStack[] outputFluids,
        ItemStack outputItem, int num, int EUt, int Duration) {

        String name;
        FluidStack[] actOutput = new FluidStack[num];
        name = inputFluid.getFluid()
            .getName()
            .replace(" ", "");

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(inputFluid, cracker)
            .fluidOutputs(FluidRegistry.getFluidStack("lightlycracked" + name, 1000))
            .duration(Math.max((long) (Duration * 0.8), 1L) * TICKS)
            .eut(EUt)
            .addTo(crackingRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(inputFluid, cracker)
            .fluidOutputs(FluidRegistry.getFluidStack("moderatelycracked" + name, 1000))
            .duration(Math.max(Duration, 1L) * TICKS)
            .eut(EUt)
            .addTo(crackingRecipes);

        GTValues.RA.stdBuilder()
            .circuit(3)
            .fluidInputs(inputFluid, cracker)
            .fluidOutputs(FluidRegistry.getFluidStack("heavilycracked" + name, 1000))
            .duration(Math.max((long) (Duration * 1.2), 1L) * TICKS)
            .eut(EUt)
            .addTo(crackingRecipes);

        for (int i = num - 1, j = 0; i >= 0; i--, j++) {
            Fluid tmp1 = outputFluids[i].getFluid();
            int tmp2 = (int) (outputFluids[i].amount * coe1[i]);
            actOutput[j] = new FluidStack(tmp1, tmp2);
        }

        addUniversalDistillationRecipe(
            FluidRegistry.getFluidStack("lightlycracked" + name, 1000),
            actOutput,
            outputItem,
            Duration / 2,
            EUt / 3);

        for (int i = num - 1, j = 0; i >= 0; i--, j++) {
            Fluid tmp1 = outputFluids[i].getFluid();
            int tmp2 = (int) (outputFluids[i].amount * coe2[i]);
            actOutput[j] = new FluidStack(tmp1, tmp2);
        }

        addUniversalDistillationRecipe(
            FluidRegistry.getFluidStack("moderatelycracked" + name, 1000),
            actOutput,
            outputItem,
            Duration / 2,
            EUt / 3);

        for (int i = num - 1, j = 0; i >= 0; i--, j++) {
            Fluid tmp1 = outputFluids[i].getFluid();
            int tmp2 = (int) (outputFluids[i].amount * coe3[i]);
            actOutput[j] = new FluidStack(tmp1, tmp2);
        }

        addUniversalDistillationRecipe(
            FluidRegistry.getFluidStack("heavilycracked" + name, 1000),
            actOutput,
            outputItem,
            Duration / 2,
            EUt / 3);
    }

    public static void addUniversalDistillationRecipewithCircuit(FluidStack aInput, ItemStack[] aCircuit,
        FluidStack[] aOutputs, ItemStack aOutput2, int aDuration, long aEUt) {
        for (int i = 0; i < Math.min(aOutputs.length, 11); i++) {
            GTRecipeBuilder buildDistillation = GTValues.RA.stdBuilder()
                .circuit(i + 1);
            if (aOutput2 != GTValues.NI) {
                buildDistillation.itemOutputs(aOutput2);
            }
            buildDistillation.fluidInputs(aInput)
                .fluidOutputs(aOutputs[i])
                .duration(2 * aDuration)
                .eut(aEUt / 4)
                .addTo(distilleryRecipes);
        }
        GTRecipeBuilder buildDT = GTValues.RA.stdBuilder()
            .itemInputs(aCircuit);
        if (aOutput2 != GTValues.NI) {
            buildDT.itemOutputs(aOutput2);
        }
        buildDT.fluidInputs(aInput)
            .fluidOutputs(aOutputs)
            .duration(aDuration)
            .eut(aEUt)
            .addTo(distillationTowerRecipes);
    }

    public static void addUniversalDistillationRecipe(FluidStack aInput, FluidStack[] aOutputs, ItemStack aOutput2,
        int aDuration, long aEUt) {
        for (int i = 0; i < Math.min(aOutputs.length, 11); i++) {
            GTRecipeBuilder buildDistillation = GTValues.RA.stdBuilder()
                .circuit(i + 1);
            if (aOutput2 != GTValues.NI) {
                buildDistillation.itemOutputs(aOutput2);
            }
            buildDistillation.fluidInputs(aInput)
                .fluidOutputs(aOutputs[i])
                .duration(2 * aDuration)
                .eut(aEUt / 4)
                .addTo(distilleryRecipes);
        }
        GTRecipeBuilder buildDT = GTValues.RA.stdBuilder();
        if (aOutput2 != GTValues.NI) {
            buildDT.itemOutputs(aOutput2);
        }
        buildDT.fluidInputs(aInput)
            .fluidOutputs(aOutputs)
            .duration(aDuration)
            .eut(aEUt)
            .addTo(distillationTowerRecipes);
    }

    public static FluidStack copyFluidWithAmount(FluidStack fluid, int amount) {
        if (fluid == null || amount <= 0) return null;
        return new FluidStack(fluid.getFluid(), amount);
    }
}
