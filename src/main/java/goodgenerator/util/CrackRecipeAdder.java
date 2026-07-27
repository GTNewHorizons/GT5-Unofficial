package goodgenerator.util;

import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.crackingRecipes;
import static gregtech.api.recipe.RecipeMaps.distillationTowerRecipes;
import static gregtech.api.recipe.RecipeMaps.distilleryRecipes;
import static gregtech.api.recipe.RecipeMaps.extruderRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.recipe.RecipeMaps.wiremillRecipes;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.ADDITIVE_AMOUNT;
import static gregtech.api.util.GTRecipeConstants.BlastFurnaceWithGas;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.material.MU;
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

    public static void reAddBlastRecipe(Material material, int duration, int EUt, int level, boolean gas) {
        ItemStack input = MU.stack(OrePrefixes.dust, material, 1);
        ItemStack output = level > 1750 ? MU.stack(OrePrefixes.ingotHot, material, 1)
            : MU.stack(OrePrefixes.ingot, material, 1);
        if (gas) {
            GTValues.RA.stdBuilder()
                .itemInputs(input)
                .circuit(11)
                .itemOutputs(output)
                .duration(duration * TICKS)
                .eut(EUt)
                .metadata(COIL_HEAT, level)
                .metadata(ADDITIVE_AMOUNT, 1000)
                .addTo(BlastFurnaceWithGas);
        } else {
            GTValues.RA.stdBuilder()
                .itemInputs(input)
                .circuit(1)
                .itemOutputs(output)
                .duration(duration * TICKS)
                .eut(EUt)
                .metadata(COIL_HEAT, level)
                .addTo(blastFurnaceRecipes);
        }
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

    public static void registerPipe(Material material) {
        GTValues.RA.stdBuilder()
            .itemInputs(MU.stack(OrePrefixes.ingot, material, 1), ItemList.Shape_Extruder_Pipe_Tiny.get(0))
            .itemOutputs(MU.stack(OrePrefixes.pipeTiny, material, 2))
            .duration(MU.mass(material) * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(extruderRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MU.stack(OrePrefixes.ingot, material, 1), ItemList.Shape_Extruder_Pipe_Small.get(0))
            .itemOutputs(MU.stack(OrePrefixes.pipeSmall, material, 1))
            .duration(MU.mass(material) * 2 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(extruderRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MU.stack(OrePrefixes.ingot, material, 3), ItemList.Shape_Extruder_Pipe_Medium.get(0))
            .itemOutputs(MU.stack(OrePrefixes.pipeMedium, material, 1))
            .duration(MU.mass(material) * 6 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(extruderRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MU.stack(OrePrefixes.ingot, material, 6), ItemList.Shape_Extruder_Pipe_Large.get(0))
            .itemOutputs(MU.stack(OrePrefixes.pipeLarge, material, 1))
            .duration(MU.mass(material) * 12 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(extruderRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MU.stack(OrePrefixes.ingot, material, 12), ItemList.Shape_Extruder_Pipe_Huge.get(0))
            .itemOutputs(MU.stack(OrePrefixes.pipeHuge, material, 1))
            .duration(MU.mass(material) * 24 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(extruderRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Pipe_Tiny.get(0))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(material, Materials2FluidShapes.fluidMolten, (int) (1 * HALF_INGOTS)))
            .itemOutputs(MU.stack(OrePrefixes.pipeTiny, material, 1))
            .duration(MU.mass(material) * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidSolidifierRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Pipe_Small.get(0))
            .fluidInputs(MaterialLibAPI.getFluidStack(material, Materials2FluidShapes.fluidMolten, (int) (1 * INGOTS)))
            .itemOutputs(MU.stack(OrePrefixes.pipeSmall, material, 1))
            .duration(MU.mass(material) * 2 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidSolidifierRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Pipe_Medium.get(0))
            .fluidInputs(MaterialLibAPI.getFluidStack(material, Materials2FluidShapes.fluidMolten, (int) (3 * INGOTS)))
            .itemOutputs(MU.stack(OrePrefixes.pipeMedium, material, 1))
            .duration(MU.mass(material) * 6 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidSolidifierRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Pipe_Large.get(0))
            .fluidInputs(MaterialLibAPI.getFluidStack(material, Materials2FluidShapes.fluidMolten, (int) (6 * INGOTS)))
            .itemOutputs(MU.stack(OrePrefixes.pipeLarge, material, 1))
            .duration(MU.mass(material) * 12 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidSolidifierRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Pipe_Huge.get(0))
            .fluidInputs(MaterialLibAPI.getFluidStack(material, Materials2FluidShapes.fluidMolten, (int) (1728)))
            .itemOutputs(MU.stack(OrePrefixes.pipeHuge, material, 1))
            .duration(MU.mass(material) * 24 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidSolidifierRecipes);
    }

    public static void registerWire(Material material) {
        GTValues.RA.stdBuilder()
            .itemInputs(MU.stack(OrePrefixes.ingot, material, 1))
            .circuit(1)
            .itemOutputs(MU.stack(OrePrefixes.wireGt01, material, 2))
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(wiremillRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MU.stack(OrePrefixes.ingot, material, 1))
            .circuit(2)
            .itemOutputs(MU.stack(OrePrefixes.wireGt02, material, 1))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(4)
            .addTo(wiremillRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MU.stack(OrePrefixes.ingot, material, 2))
            .circuit(4)
            .itemOutputs(MU.stack(OrePrefixes.wireGt04, material, 1))
            .duration(10 * SECONDS)
            .eut(4)
            .addTo(wiremillRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MU.stack(OrePrefixes.ingot, material, 4))
            .circuit(8)
            .itemOutputs(MU.stack(OrePrefixes.wireGt08, material, 1))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(4)
            .addTo(wiremillRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MU.stack(OrePrefixes.ingot, material, 6))
            .circuit(12)
            .itemOutputs(MU.stack(OrePrefixes.wireGt12, material, 1))
            .duration(15 * SECONDS)
            .eut(4)
            .addTo(wiremillRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MU.stack(OrePrefixes.ingot, material, 8))
            .circuit(16)
            .itemOutputs(MU.stack(OrePrefixes.wireGt16, material, 1))
            .duration(17 * SECONDS + 10 * TICKS)
            .eut(4)
            .addTo(wiremillRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MU.stack(OrePrefixes.stick, material, 1))
            .circuit(1)
            .itemOutputs(MU.stack(OrePrefixes.wireGt01, material, 1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(4)
            .addTo(wiremillRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MU.stack(OrePrefixes.stick, material, 2))
            .circuit(2)
            .itemOutputs(MU.stack(OrePrefixes.wireGt02, material, 1))
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(wiremillRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MU.stack(OrePrefixes.stick, material, 4))
            .circuit(4)
            .itemOutputs(MU.stack(OrePrefixes.wireGt04, material, 1))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(4)
            .addTo(wiremillRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MU.stack(OrePrefixes.stick, material, 8))
            .circuit(8)
            .itemOutputs(MU.stack(OrePrefixes.wireGt08, material, 1))
            .duration(10 * SECONDS)
            .eut(4)
            .addTo(wiremillRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MU.stack(OrePrefixes.stick, material, 12))
            .circuit(12)
            .itemOutputs(MU.stack(OrePrefixes.wireGt12, material, 1))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(4)
            .addTo(wiremillRecipes);
    }
}
