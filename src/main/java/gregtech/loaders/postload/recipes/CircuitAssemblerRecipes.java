package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.recipe.RecipeMaps.circuitAssemblerRecipes;
import static gregtech.api.util.GT_ModHandler.getModItem;
import static gregtech.api.util.RecipeBuilder.SECONDS;
import static gregtech.loaders.postload.GT_MachineRecipeLoader.solderingMats;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.OreDictUnificator;

public class CircuitAssemblerRecipes implements Runnable {

    @Override
    public void run() {
        registerRailcraftRecipes();
        registerForestryRecipes();

    }

    public void registerRailcraftRecipes() {
        if (!Railcraft.isModLoaded()) {
            return;
        }

        for (Materials tMat : solderingMats) {
            int tMultiplier = tMat.contains(SubTag.SOLDERING_MATERIAL_GOOD) ? 1
                : tMat.contains(SubTag.SOLDERING_MATERIAL_BAD) ? 4 : 2;

            // Railcraft Circuits

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Circuit_Board_Coated_Basic.get(1),
                    OreDictUnificator.get(OrePrefixes.circuit, Materials.LV, 1),
                    ItemList.Cover_Controller.get(1))
                .itemOutputs(getModItem(Railcraft.ID, "part.circuit", 4L, 0))
                .fluidInputs(tMat.getMolten(144L * tMultiplier / 2))
                .duration(15 * SECONDS)
                .eut((int) TierEU.RECIPE_LV)
                .addTo(circuitAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Circuit_Board_Coated_Basic.get(1),
                    OreDictUnificator.get(OrePrefixes.circuit, Materials.LV, 1),
                    ItemList.Sensor_LV.get(1))
                .itemOutputs(getModItem(Railcraft.ID, "part.circuit", 4L, 1))
                .fluidInputs(tMat.getMolten(144L * tMultiplier / 2))
                .duration(15 * SECONDS)
                .eut((int) TierEU.RECIPE_LV)
                .addTo(circuitAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Circuit_Board_Coated_Basic.get(1),
                    OreDictUnificator.get(OrePrefixes.circuit, Materials.LV, 1),
                    getModItem(Railcraft.ID, "part.signal.lamp", 1L, 0))
                .itemOutputs(getModItem(Railcraft.ID, "part.circuit", 4L, 2))
                .fluidInputs(tMat.getMolten(144L * tMultiplier / 2))
                .duration(15 * SECONDS)
                .eut((int) TierEU.RECIPE_LV)
                .addTo(circuitAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Circuit_Board_Phenolic_Good.get(1),
                    OreDictUnificator.get(OrePrefixes.circuit, Materials.MV, 1),
                    ItemList.Cover_Controller.get(1))
                .itemOutputs(getModItem(Railcraft.ID, "part.circuit", 8L, 0))
                .fluidInputs(tMat.getMolten(144L * tMultiplier / 2))
                .duration(20 * SECONDS)
                .eut((int) TierEU.RECIPE_LV)
                .addTo(circuitAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Circuit_Board_Phenolic_Good.get(1),
                    OreDictUnificator.get(OrePrefixes.circuit, Materials.MV, 1),
                    ItemList.Sensor_LV.get(1))
                .itemOutputs(getModItem(Railcraft.ID, "part.circuit", 8L, 1))
                .fluidInputs(tMat.getMolten(144L * tMultiplier / 2))
                .duration(20 * SECONDS)
                .eut((int) TierEU.RECIPE_LV)
                .addTo(circuitAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Circuit_Board_Phenolic_Good.get(1),
                    OreDictUnificator.get(OrePrefixes.circuit, Materials.MV, 1),
                    getModItem(Railcraft.ID, "part.signal.lamp", 1L, 0))
                .itemOutputs(getModItem(Railcraft.ID, "part.circuit", 8L, 2))
                .fluidInputs(tMat.getMolten(144L * tMultiplier / 2))
                .duration(20 * SECONDS)
                .eut((int) TierEU.RECIPE_LV)
                .addTo(circuitAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Circuit_Board_Epoxy_Advanced.get(1),
                    OreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 1),
                    ItemList.Cover_Controller.get(1))
                .itemOutputs(getModItem(Railcraft.ID, "part.circuit", 16L, 0))
                .fluidInputs(tMat.getMolten(144L * tMultiplier / 2))
                .duration(25 * SECONDS)
                .eut((int) TierEU.RECIPE_LV)
                .addTo(circuitAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Circuit_Board_Epoxy_Advanced.get(1),
                    OreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 1),
                    ItemList.Sensor_LV.get(1))
                .itemOutputs(getModItem(Railcraft.ID, "part.circuit", 16L, 1))
                .fluidInputs(tMat.getMolten(144L * tMultiplier / 2))
                .duration(25 * SECONDS)
                .eut((int) TierEU.RECIPE_LV)
                .addTo(circuitAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Circuit_Board_Epoxy_Advanced.get(1),
                    OreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 1),
                    getModItem(Railcraft.ID, "part.signal.lamp", 1L, 0))
                .itemOutputs(getModItem(Railcraft.ID, "part.circuit", 16L, 2))
                .fluidInputs(tMat.getMolten(144L * tMultiplier / 2))
                .duration(25 * SECONDS)
                .eut((int) TierEU.RECIPE_LV)
                .addTo(circuitAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Circuit_Board_Coated_Basic.get(1),
                    OreDictUnificator.get(OrePrefixes.circuit, Materials.ULV, 2),
                    OreDictUnificator.get(OrePrefixes.foil, Materials.Iron, 2),
                    OreDictUnificator.get(OrePrefixes.screw, Materials.Iron, 4),
                    OreDictUnificator.get(OrePrefixes.wireFine, Materials.Iron, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(getModItem(Forestry.ID, "chipsets", 1L, 0))
                .fluidInputs(tMat.getMolten(1152L * tMultiplier / 2))
                .duration(10 * SECONDS)
                .eut((int) TierEU.RECIPE_LV)
                .addTo(circuitAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Circuit_Board_Coated_Basic.get(1),
                    OreDictUnificator.get(OrePrefixes.circuit, Materials.LV, 2),
                    OreDictUnificator.get(OrePrefixes.foil, Materials.Bronze, 2),
                    OreDictUnificator.get(OrePrefixes.screw, Materials.Bronze, 4),
                    OreDictUnificator.get(OrePrefixes.wireFine, Materials.Bronze, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(getModItem(Forestry.ID, "chipsets", 1L, 1))
                .fluidInputs(tMat.getMolten(1152L * tMultiplier / 2))
                .duration(10 * SECONDS)
                .eut((int) TierEU.RECIPE_LV)
                .addTo(circuitAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Circuit_Board_Phenolic_Good.get(1),
                    OreDictUnificator.get(OrePrefixes.circuit, Materials.MV, 2),
                    OreDictUnificator.get(OrePrefixes.foil, Materials.Steel, 2),
                    OreDictUnificator.get(OrePrefixes.screw, Materials.Steel, 4),
                    OreDictUnificator.get(OrePrefixes.wireFine, Materials.Steel, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(getModItem(Forestry.ID, "chipsets", 1L, 2))
                .fluidInputs(tMat.getMolten(1152L * tMultiplier / 2))
                .duration(10 * SECONDS)
                .eut((int) TierEU.RECIPE_LV)
                .addTo(circuitAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Circuit_Board_Phenolic_Good.get(1),
                    OreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 2),
                    OreDictUnificator.get(OrePrefixes.foil, Materials.Electrum, 2),
                    OreDictUnificator.get(OrePrefixes.screw, Materials.Electrum, 4),
                    OreDictUnificator.get(OrePrefixes.wireFine, Materials.Electrum, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(getModItem(Forestry.ID, "chipsets", 1L, 3))
                .fluidInputs(tMat.getMolten(1152L * tMultiplier / 2))
                .duration(10 * SECONDS)
                .eut((int) TierEU.RECIPE_LV)
                .addTo(circuitAssemblerRecipes);

        }
    }

    public void registerForestryRecipes() {
        if (!Forestry.isModLoaded()) {
            return;
        }

        // alternative version of the copper electron tube

        GT_Values.RA.stdBuilder()
            .itemInputs(
                OreDictUnificator.get(OrePrefixes.plate, Materials.RedAlloy, 1),
                OreDictUnificator.get(OrePrefixes.bolt, Materials.Gold, 2),
                OreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 2),
                OreDictUnificator.get(OrePrefixes.stickLong, Materials.AnnealedCopper, 2))
            .itemOutputs(getModItem(Forestry.ID, "thermionicTubes", 4L, 0))
            .fluidInputs(Materials.Glass.getMolten(576))
            .duration(10 * SECONDS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(circuitAssemblerRecipes);

        // alternative version of the iron electron tube

        GT_Values.RA.stdBuilder()
            .itemInputs(
                OreDictUnificator.get(OrePrefixes.plate, Materials.RedAlloy, 1),
                OreDictUnificator.get(OrePrefixes.bolt, Materials.Gold, 2),
                OreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 2),
                OreDictUnificator.get(OrePrefixes.stickLong, Materials.WroughtIron, 2))
            .itemOutputs(getModItem(Forestry.ID, "thermionicTubes", 4L, 3))
            .fluidInputs(Materials.Glass.getMolten(576))
            .duration(10 * SECONDS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(circuitAssemblerRecipes);

        ItemStack[] rodMaterials = new ItemStack[] { OreDictUnificator.get(OrePrefixes.stickLong, Materials.Copper, 2L),
            OreDictUnificator.get(OrePrefixes.stickLong, Materials.Tin, 2L),
            OreDictUnificator.get(OrePrefixes.stickLong, Materials.Bronze, 2L),
            OreDictUnificator.get(OrePrefixes.stickLong, Materials.Iron, 2L),
            OreDictUnificator.get(OrePrefixes.stickLong, Materials.Gold, 2L),
            OreDictUnificator.get(OrePrefixes.stickLong, Materials.Diamond, 2L),
            getModItem(NewHorizonsCoreMod.ID, "item.LongObsidianRod", 2L, 0),
            OreDictUnificator.get(OrePrefixes.stickLong, Materials.Blaze, 2L),
            OreDictUnificator.get(OrePrefixes.stickLong, Materials.Rubber, 2L),
            OreDictUnificator.get(OrePrefixes.stickLong, Materials.Emerald, 2L),
            OreDictUnificator.get(OrePrefixes.stickLong, Materials.Apatite, 2L),
            OreDictUnificator.get(OrePrefixes.stickLong, Materials.Lapis, 2L),
            OreDictUnificator.get(OrePrefixes.stickLong, Materials.EnderEye, 2L),
            OreDictUnificator.get(OrePrefixes.stickLong, Materials.Uranium, 2L), };

        for (int metaid = 0; metaid < rodMaterials.length; metaid++) {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    OreDictUnificator.get(OrePrefixes.plate, Materials.RedAlloy, 1),
                    OreDictUnificator.get(OrePrefixes.bolt, Materials.Gold, 2),
                    OreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 2),
                    rodMaterials[metaid])
                .itemOutputs(getModItem(Forestry.ID, "thermionicTubes", 4L, metaid))
                .fluidInputs(Materials.Glass.getMolten(576))
                .duration(10 * SECONDS)
                .eut((int) TierEU.RECIPE_LV)
                .addTo(circuitAssemblerRecipes);
        }

        for (Materials tMat : solderingMats) {

            int tMultiplier = tMat.contains(SubTag.SOLDERING_MATERIAL_GOOD) ? 1
                : tMat.contains(SubTag.SOLDERING_MATERIAL_BAD) ? 4 : 2;

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Circuit_Board_Coated_Basic.get(1),
                    OreDictUnificator.get(OrePrefixes.circuit, Materials.ULV, 2),
                    OreDictUnificator.get(OrePrefixes.foil, Materials.Iron, 2),
                    OreDictUnificator.get(OrePrefixes.screw, Materials.Iron, 4),
                    OreDictUnificator.get(OrePrefixes.wireFine, Materials.Iron, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(getModItem(Forestry.ID, "chipsets", 1L, 0))
                .fluidInputs(tMat.getMolten(1152L * tMultiplier / 2))
                .duration(10 * SECONDS)
                .eut((int) TierEU.RECIPE_LV)
                .addTo(circuitAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Circuit_Board_Coated_Basic.get(1),
                    OreDictUnificator.get(OrePrefixes.circuit, Materials.LV, 2),
                    OreDictUnificator.get(OrePrefixes.foil, Materials.Bronze, 2),
                    OreDictUnificator.get(OrePrefixes.screw, Materials.Bronze, 4),
                    OreDictUnificator.get(OrePrefixes.wireFine, Materials.Bronze, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(getModItem(Forestry.ID, "chipsets", 1L, 1))
                .fluidInputs(tMat.getMolten(1152L * tMultiplier / 2))
                .duration(10 * SECONDS)
                .eut((int) TierEU.RECIPE_LV)
                .addTo(circuitAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Circuit_Board_Phenolic_Good.get(1),
                    OreDictUnificator.get(OrePrefixes.circuit, Materials.MV, 2),
                    OreDictUnificator.get(OrePrefixes.foil, Materials.Steel, 2),
                    OreDictUnificator.get(OrePrefixes.screw, Materials.Steel, 4),
                    OreDictUnificator.get(OrePrefixes.wireFine, Materials.Steel, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(getModItem(Forestry.ID, "chipsets", 1L, 2))
                .fluidInputs(tMat.getMolten(1152L * tMultiplier / 2))
                .duration(10 * SECONDS)
                .eut((int) TierEU.RECIPE_LV)
                .addTo(circuitAssemblerRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    ItemList.Circuit_Board_Phenolic_Good.get(1),
                    OreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 2),
                    OreDictUnificator.get(OrePrefixes.foil, Materials.Electrum, 2),
                    OreDictUnificator.get(OrePrefixes.screw, Materials.Electrum, 4),
                    OreDictUnificator.get(OrePrefixes.wireFine, Materials.Electrum, 1),
                    GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(getModItem(Forestry.ID, "chipsets", 1L, 3))
                .fluidInputs(tMat.getMolten(1152L * tMultiplier / 2))
                .duration(10 * SECONDS)
                .eut((int) TierEU.RECIPE_LV)
                .addTo(circuitAssemblerRecipes);

        }
    }
}
