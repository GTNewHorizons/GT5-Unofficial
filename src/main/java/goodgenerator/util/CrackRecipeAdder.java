package goodgenerator.util;

import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.crackingRecipes;
import static gregtech.api.recipe.RecipeMaps.distillationTowerRecipes;
import static gregtech.api.recipe.RecipeMaps.distilleryRecipes;
import static gregtech.api.recipe.RecipeMaps.extruderRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.recipe.RecipeMaps.wiremillRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import bartworks.system.material.Werkstoff;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.metatileentity.implementations.MTECable;
import gregtech.api.metatileentity.implementations.MTEFluid;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTUtility;

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
            .replaceAll(" ", "");

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .fluidInputs(inputFluid, cracker)
            .fluidOutputs(FluidRegistry.getFluidStack("lightlycracked" + name, 1000))
            .duration(Math.max((long) (Duration * 0.8), 1L) * TICKS)
            .eut(EUt)
            .addTo(crackingRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(2))
            .fluidInputs(inputFluid, cracker)
            .fluidOutputs(FluidRegistry.getFluidStack("moderatelycracked" + name, 1000))
            .duration(Math.max((long) (Duration), 1L) * TICKS)
            .eut(EUt)
            .addTo(crackingRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(3))
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

    public static void reAddBlastRecipe(Werkstoff material, int duration, int EUt, int level, boolean gas) {
        ItemStack input = material.get(OrePrefixes.dust, 1);
        ItemStack output = level > 1750 ? material.get(OrePrefixes.ingotHot, 1) : material.get(OrePrefixes.ingot, 1);
        if (gas) {
            GTValues.RA.stdBuilder()
                .itemInputs(input, GTUtility.getIntegratedCircuit(11))
                .fluidInputs(Materials.Helium.getGas(1000))
                .itemOutputs(output)
                .duration(duration * TICKS)
                .eut(EUt)
                .metadata(COIL_HEAT, level)
                .addTo(blastFurnaceRecipes);
        } else {
            GTValues.RA.stdBuilder()
                .itemInputs(input, GTUtility.getIntegratedCircuit(1))
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
                .itemInputs(GTUtility.getIntegratedCircuit(i + 1));
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
                .itemInputs(GTUtility.getIntegratedCircuit(i + 1));
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

    public static void registerPipe(int ID, Werkstoff material, int flow, int temp, boolean gas) {
        String unName = material.getDefaultName()
            .replace(" ", "_");
        String Name = material.getDefaultName();
        GTOreDictUnificator.registerOre(
            OrePrefixes.pipeTiny.get(material.getBridgeMaterial()),
            new MTEFluid(
                ID,
                "GT_Pipe_" + unName + "_Tiny",
                "Tiny " + Name + " Fluid Pipe",
                0.25F,
                material.getBridgeMaterial(),
                flow / 6,
                temp,
                gas).getStackForm(1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.pipeSmall.get(material.getBridgeMaterial()),
            new MTEFluid(
                ID + 1,
                "GT_Pipe_" + unName + "_Small",
                "Small " + Name + " Fluid Pipe",
                0.375F,
                material.getBridgeMaterial(),
                flow / 3,
                temp,
                gas).getStackForm(1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.pipeMedium.get(material.getBridgeMaterial()),
            new MTEFluid(
                ID + 2,
                "GT_Pipe_" + unName,
                Name + " Fluid Pipe",
                0.5F,
                material.getBridgeMaterial(),
                flow,
                temp,
                gas).getStackForm(1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.pipeLarge.get(material.getBridgeMaterial()),
            new MTEFluid(
                ID + 3,
                "GT_Pipe_" + unName + "_Large",
                "Large " + Name + " Fluid Pipe",
                0.75F,
                material.getBridgeMaterial(),
                flow * 2,
                temp,
                gas).getStackForm(1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.pipeHuge.get(material.getBridgeMaterial()),
            new MTEFluid(
                ID + 4,
                "GT_Pipe_" + unName + "_Huge",
                "Huge " + Name + " Fluid Pipe",
                0.875F,
                material.getBridgeMaterial(),
                flow * 4,
                temp,
                gas).getStackForm(1L));
        GTValues.RA.stdBuilder()
            .itemInputs(material.get(OrePrefixes.ingot, 1), ItemList.Shape_Extruder_Pipe_Tiny.get(0))
            .itemOutputs(material.get(OrePrefixes.pipeTiny, 2))
            .duration(
                material.getStats()
                    .getMass() * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(extruderRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(material.get(OrePrefixes.ingot, 1), ItemList.Shape_Extruder_Pipe_Small.get(0))
            .itemOutputs(material.get(OrePrefixes.pipeSmall, 1))
            .duration(
                material.getStats()
                    .getMass() * 2
                    * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(extruderRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(material.get(OrePrefixes.ingot, 3), ItemList.Shape_Extruder_Pipe_Medium.get(0))
            .itemOutputs(material.get(OrePrefixes.pipeMedium, 1))
            .duration(
                material.getStats()
                    .getMass() * 6
                    * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(extruderRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(material.get(OrePrefixes.ingot, 6), ItemList.Shape_Extruder_Pipe_Large.get(0))
            .itemOutputs(material.get(OrePrefixes.pipeLarge, 1))
            .duration(
                material.getStats()
                    .getMass() * 12
                    * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(extruderRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(material.get(OrePrefixes.ingot, 12), ItemList.Shape_Extruder_Pipe_Huge.get(0))
            .itemOutputs(material.get(OrePrefixes.pipeHuge, 1))
            .duration(
                material.getStats()
                    .getMass() * 24
                    * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(extruderRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Pipe_Tiny.get(0))
            .fluidInputs(material.getMolten(72))
            .itemOutputs(material.get(OrePrefixes.pipeTiny, 1))
            .duration(
                material.getStats()
                    .getMass() * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidSolidifierRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Pipe_Small.get(0))
            .fluidInputs(material.getMolten(144))
            .itemOutputs(material.get(OrePrefixes.pipeSmall, 1))
            .duration(
                material.getStats()
                    .getMass() * 2
                    * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidSolidifierRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Pipe_Medium.get(0))
            .fluidInputs(material.getMolten(432))
            .itemOutputs(material.get(OrePrefixes.pipeMedium, 1))
            .duration(
                material.getStats()
                    .getMass() * 6
                    * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidSolidifierRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Pipe_Large.get(0))
            .fluidInputs(material.getMolten(864))
            .itemOutputs(material.get(OrePrefixes.pipeLarge, 1))
            .duration(
                material.getStats()
                    .getMass() * 12
                    * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidSolidifierRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Pipe_Huge.get(0))
            .fluidInputs(material.getMolten(1728))
            .itemOutputs(material.get(OrePrefixes.pipeHuge, 1))
            .duration(
                material.getStats()
                    .getMass() * 24
                    * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidSolidifierRecipes);
    }

    public static void registerWire(int ID, Werkstoff material, int aAmperage, int aVoltage, int aLoss, boolean cover) {
        String unName = material.getDefaultName()
            .replace(" ", "_")
            .toLowerCase();
        String Name = material.getDefaultName();
        String aTextWire1 = "wire.";
        String aTextCable1 = "cable.";
        String aTextWire2 = " Wire";
        String aTextCable2 = " Cable";
        int aLossInsulated = aLoss / 4;
        GTOreDictUnificator.registerOre(
            OrePrefixes.wireGt01,
            material.getBridgeMaterial(),
            new MTECable(
                ID + 0,
                aTextWire1 + unName + ".01",
                "1x " + Name + aTextWire2,
                0.125F,
                material.getBridgeMaterial(),
                aLoss,
                1L * aAmperage,
                aVoltage,
                false,
                true).getStackForm(1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.wireGt02,
            material.getBridgeMaterial(),
            new MTECable(
                ID + 1,
                aTextWire1 + unName + ".02",
                "2x " + Name + aTextWire2,
                0.25F,
                material.getBridgeMaterial(),
                aLoss,
                2L * aAmperage,
                aVoltage,
                false,
                true).getStackForm(1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.wireGt04,
            material.getBridgeMaterial(),
            new MTECable(
                ID + 2,
                aTextWire1 + unName + ".04",
                "4x " + Name + aTextWire2,
                0.375F,
                material.getBridgeMaterial(),
                aLoss,
                4L * aAmperage,
                aVoltage,
                false,
                true).getStackForm(1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.wireGt08,
            material.getBridgeMaterial(),
            new MTECable(
                ID + 3,
                aTextWire1 + unName + ".08",
                "8x " + Name + aTextWire2,
                0.5F,
                material.getBridgeMaterial(),
                aLoss,
                8L * aAmperage,
                aVoltage,
                false,
                true).getStackForm(1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.wireGt12,
            material.getBridgeMaterial(),
            new MTECable(
                ID + 4,
                aTextWire1 + unName + ".12",
                "12x " + Name + aTextWire2,
                0.625F,
                material.getBridgeMaterial(),
                aLoss,
                12L * aAmperage,
                aVoltage,
                false,
                true).getStackForm(1L));
        GTOreDictUnificator.registerOre(
            OrePrefixes.wireGt16,
            material.getBridgeMaterial(),
            new MTECable(
                ID + 5,
                aTextWire1 + unName + ".16",
                "16x " + Name + aTextWire2,
                0.75F,
                material.getBridgeMaterial(),
                aLoss,
                16L * aAmperage,
                aVoltage,
                false,
                true).getStackForm(1L));
        if (cover) {
            GTOreDictUnificator.registerOre(
                OrePrefixes.cableGt01,
                material.getBridgeMaterial(),
                new MTECable(
                    ID + 6,
                    aTextCable1 + unName + ".01",
                    "1x " + Name + aTextCable2,
                    0.25F,
                    material.getBridgeMaterial(),
                    aLossInsulated,
                    1L * aAmperage,
                    aVoltage,
                    true,
                    false).getStackForm(1L));
            GTOreDictUnificator.registerOre(
                OrePrefixes.cableGt02,
                material.getBridgeMaterial(),
                new MTECable(
                    ID + 7,
                    aTextCable1 + unName + ".02",
                    "2x " + Name + aTextCable2,
                    0.375F,
                    material.getBridgeMaterial(),
                    aLossInsulated,
                    2L * aAmperage,
                    aVoltage,
                    true,
                    false).getStackForm(1L));
            GTOreDictUnificator.registerOre(
                OrePrefixes.cableGt04,
                material.getBridgeMaterial(),
                new MTECable(
                    ID + 8,
                    aTextCable1 + unName + ".04",
                    "4x " + Name + aTextCable2,
                    0.5F,
                    material.getBridgeMaterial(),
                    aLossInsulated,
                    4L * aAmperage,
                    aVoltage,
                    true,
                    false).getStackForm(1L));
            GTOreDictUnificator.registerOre(
                OrePrefixes.cableGt08,
                material.getBridgeMaterial(),
                new MTECable(
                    ID + 9,
                    aTextCable1 + unName + ".08",
                    "8x " + Name + aTextCable2,
                    0.625F,
                    material.getBridgeMaterial(),
                    aLossInsulated,
                    8L * aAmperage,
                    aVoltage,
                    true,
                    false).getStackForm(1L));
            GTOreDictUnificator.registerOre(
                OrePrefixes.cableGt12,
                material.getBridgeMaterial(),
                new MTECable(
                    ID + 10,
                    aTextCable1 + unName + ".12",
                    "12x " + Name + aTextCable2,
                    0.75F,
                    material.getBridgeMaterial(),
                    aLossInsulated,
                    12L * aAmperage,
                    aVoltage,
                    true,
                    false).getStackForm(1L));
            GTOreDictUnificator.registerOre(
                OrePrefixes.cableGt16,
                material.getBridgeMaterial(),
                new MTECable(
                    ID + 11,
                    aTextCable1 + unName + ".16",
                    "16x " + Name + aTextCable2,
                    0.875F,
                    material.getBridgeMaterial(),
                    aLossInsulated,
                    16L * aAmperage,
                    aVoltage,
                    true,
                    false).getStackForm(1L));
        }
        GTValues.RA.stdBuilder()
            .itemInputs(material.get(OrePrefixes.ingot, 1), GTUtility.getIntegratedCircuit(1))
            .itemOutputs(material.get(OrePrefixes.wireGt01, 2))
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(wiremillRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(material.get(OrePrefixes.ingot, 1), GTUtility.getIntegratedCircuit(2))
            .itemOutputs(material.get(OrePrefixes.wireGt02, 1))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(4)
            .addTo(wiremillRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(material.get(OrePrefixes.ingot, 2), GTUtility.getIntegratedCircuit(4))
            .itemOutputs(material.get(OrePrefixes.wireGt04, 1))
            .duration(10 * SECONDS)
            .eut(4)
            .addTo(wiremillRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(material.get(OrePrefixes.ingot, 4), GTUtility.getIntegratedCircuit(8))
            .itemOutputs(material.get(OrePrefixes.wireGt08, 1))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(4)
            .addTo(wiremillRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(material.get(OrePrefixes.ingot, 6), GTUtility.getIntegratedCircuit(12))
            .itemOutputs(material.get(OrePrefixes.wireGt12, 1))
            .duration(15 * SECONDS)
            .eut(4)
            .addTo(wiremillRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(material.get(OrePrefixes.ingot, 8), GTUtility.getIntegratedCircuit(16))
            .itemOutputs(material.get(OrePrefixes.wireGt16, 1))
            .duration(17 * SECONDS + 10 * TICKS)
            .eut(4)
            .addTo(wiremillRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(material.get(OrePrefixes.stick, 1), GTUtility.getIntegratedCircuit(1))
            .itemOutputs(material.get(OrePrefixes.wireGt01, 1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(4)
            .addTo(wiremillRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(material.get(OrePrefixes.stick, 2), GTUtility.getIntegratedCircuit(2))
            .itemOutputs(material.get(OrePrefixes.wireGt02, 1))
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(wiremillRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(material.get(OrePrefixes.stick, 4), GTUtility.getIntegratedCircuit(4))
            .itemOutputs(material.get(OrePrefixes.wireGt04, 1))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(4)
            .addTo(wiremillRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(material.get(OrePrefixes.stick, 8), GTUtility.getIntegratedCircuit(8))
            .itemOutputs(material.get(OrePrefixes.wireGt08, 1))
            .duration(10 * SECONDS)
            .eut(4)
            .addTo(wiremillRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(material.get(OrePrefixes.stick, 12), GTUtility.getIntegratedCircuit(12))
            .itemOutputs(material.get(OrePrefixes.wireGt12, 1))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(4)
            .addTo(wiremillRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(material.get(OrePrefixes.stick, 16), GTUtility.getIntegratedCircuit(16))
            .itemOutputs(material.get(OrePrefixes.wireGt16, 1))
            .duration(15 * SECONDS)
            .eut(4)
            .addTo(wiremillRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(material.get(OrePrefixes.ingot, 1), GTUtility.getIntegratedCircuit(3))
            .itemOutputs(material.get(OrePrefixes.wireFine, 8))
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(wiremillRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(material.get(OrePrefixes.stick, 1), GTUtility.getIntegratedCircuit(3))
            .itemOutputs(material.get(OrePrefixes.wireFine, 4))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(4)
            .addTo(wiremillRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(material.get(OrePrefixes.wireGt01, 1), GTUtility.getIntegratedCircuit(1))
            .itemOutputs(material.get(OrePrefixes.wireFine, 4))
            .duration(10 * SECONDS)
            .eut(8)
            .addTo(wiremillRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(material.get(OrePrefixes.ingot, 1), ItemList.Shape_Extruder_Wire.get(0))
            .itemOutputs(material.get(OrePrefixes.wireGt01, 2))
            .duration(
                material.getStats()
                    .getMass() * 8
                    * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(extruderRecipes);
    }
}
