package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.recipe.RecipeMaps.circuitAssemblerRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.loaders.postload.MachineRecipeLoader.solderingMats;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;

public class CircuitAssemblerRecipes implements Runnable {

    @Override
    public void run() {
        registerRailcraftRecipes();
        registerForestryRecipes();

        // Debugging nanochip assembly complex: Add recipes for crystalprocessor line
        Fluid solderIndalloy = FluidRegistry.getFluid("molten.indalloy140") != null
            ? FluidRegistry.getFluid("molten.indalloy140")
            : FluidRegistry.getFluid("molten.solderingalloy");
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Multifiberglass_Elite.get(1L),
                ItemList.Circuit_Chip_CrystalCPU.get(1L),
                ItemList.Circuit_Chip_NanoCPU.get(2L),
                ItemList.Circuit_Parts_CapacitorASMD.get(6),
                ItemList.Circuit_Parts_TransistorASMD.get(6),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.NiobiumTitanium, 8))
            .itemOutputs(ItemList.Circuit_Crystalprocessor.get(1L))
            .fluidInputs(new FluidStack(solderIndalloy, 72))
            .requiresCleanRoom()
            .duration(5 * SECONDS)
            .eut(9600)
            .addTo(circuitAssemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Multifiberglass_Elite.get(1L),
                ItemList.Circuit_Crystalprocessor.get(2L),
                ItemList.Circuit_Parts_InductorASMD.get(6L),
                ItemList.Circuit_Parts_CapacitorASMD.get(8L),
                ItemList.Circuit_Chip_Ram.get(24),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.NiobiumTitanium, 16))
            .itemOutputs(ItemList.Circuit_Crystalcomputer.get(1L))
            .fluidInputs(new FluidStack(solderIndalloy, 144))
            .requiresCleanRoom()
            .duration(10 * SECONDS)
            .eut(9600)
            .addTo(circuitAssemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Multifiberglass_Elite.get(1L),
                ItemList.Circuit_Crystalcomputer.get(2L),
                ItemList.Circuit_Chip_Ram.get(4L),
                ItemList.Circuit_Chip_NOR.get(32L),
                ItemList.Circuit_Chip_NAND.get(64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.NiobiumTitanium, 32))
            .itemOutputs(ItemList.Circuit_Ultimatecrystalcomputer.get(1L))
            .fluidInputs(new FluidStack(solderIndalloy, 144))
            .requiresCleanRoom()
            .duration(20 * SECONDS)
            .eut(9600)
            .addTo(circuitAssemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Aluminium, 2),
                ItemList.Circuit_Ultimatecrystalcomputer.get(2L),
                ItemList.Circuit_Parts_InductorASMD.get(8L),
                ItemList.Circuit_Parts_CapacitorASMD.get(16L),
                ItemList.Circuit_Chip_Ram.get(32L),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 16))
            .itemOutputs(ItemList.Circuit_Crystalmainframe.get(1L))
            .fluidInputs(new FluidStack(solderIndalloy, 288))
            .requiresCleanRoom()
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(circuitAssemblerRecipes);
    }

    public void registerRailcraftRecipes() {
        if (!Railcraft.isModLoaded()) {
            return;
        }

        for (Materials tMat : solderingMats) {
            int tMultiplier = tMat.contains(SubTag.SOLDERING_MATERIAL_GOOD) ? 1
                : tMat.contains(SubTag.SOLDERING_MATERIAL_BAD) ? 4 : 2;

            // Railcraft Circuits

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Circuit_Board_Coated_Basic.get(1),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LV, 1),
                    ItemList.Cover_Controller.get(1))
                .itemOutputs(getModItem(Railcraft.ID, "part.circuit", 4L, 0))
                .fluidInputs(tMat.getMolten(tMultiplier * HALF_INGOTS))
                .duration(15 * SECONDS)
                .eut((int) TierEU.RECIPE_LV)
                .addTo(circuitAssemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Circuit_Board_Coated_Basic.get(1),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LV, 1),
                    ItemList.Sensor_LV.get(1))
                .itemOutputs(getModItem(Railcraft.ID, "part.circuit", 4L, 1))
                .fluidInputs(tMat.getMolten(tMultiplier * HALF_INGOTS))
                .duration(15 * SECONDS)
                .eut((int) TierEU.RECIPE_LV)
                .addTo(circuitAssemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Circuit_Board_Coated_Basic.get(1),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LV, 1),
                    getModItem(Railcraft.ID, "part.signal.lamp", 1L, 0))
                .itemOutputs(getModItem(Railcraft.ID, "part.circuit", 4L, 2))
                .fluidInputs(tMat.getMolten(tMultiplier * HALF_INGOTS))
                .duration(15 * SECONDS)
                .eut((int) TierEU.RECIPE_LV)
                .addTo(circuitAssemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Circuit_Board_Phenolic_Good.get(1),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.MV, 1),
                    ItemList.Cover_Controller.get(1))
                .itemOutputs(getModItem(Railcraft.ID, "part.circuit", 8L, 0))
                .fluidInputs(tMat.getMolten(tMultiplier * HALF_INGOTS))
                .duration(20 * SECONDS)
                .eut((int) TierEU.RECIPE_LV)
                .addTo(circuitAssemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Circuit_Board_Phenolic_Good.get(1),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.MV, 1),
                    ItemList.Sensor_LV.get(1))
                .itemOutputs(getModItem(Railcraft.ID, "part.circuit", 8L, 1))
                .fluidInputs(tMat.getMolten(tMultiplier * HALF_INGOTS))
                .duration(20 * SECONDS)
                .eut((int) TierEU.RECIPE_LV)
                .addTo(circuitAssemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Circuit_Board_Phenolic_Good.get(1),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.MV, 1),
                    getModItem(Railcraft.ID, "part.signal.lamp", 1L, 0))
                .itemOutputs(getModItem(Railcraft.ID, "part.circuit", 8L, 2))
                .fluidInputs(tMat.getMolten(tMultiplier * HALF_INGOTS))
                .duration(20 * SECONDS)
                .eut((int) TierEU.RECIPE_LV)
                .addTo(circuitAssemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Circuit_Board_Epoxy_Advanced.get(1),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 1),
                    ItemList.Cover_Controller.get(1))
                .itemOutputs(getModItem(Railcraft.ID, "part.circuit", 16L, 0))
                .fluidInputs(tMat.getMolten(tMultiplier * HALF_INGOTS))
                .duration(25 * SECONDS)
                .eut((int) TierEU.RECIPE_LV)
                .addTo(circuitAssemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Circuit_Board_Epoxy_Advanced.get(1),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 1),
                    ItemList.Sensor_LV.get(1))
                .itemOutputs(getModItem(Railcraft.ID, "part.circuit", 16L, 1))
                .fluidInputs(tMat.getMolten(tMultiplier * HALF_INGOTS))
                .duration(25 * SECONDS)
                .eut((int) TierEU.RECIPE_LV)
                .addTo(circuitAssemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Circuit_Board_Epoxy_Advanced.get(1),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 1),
                    getModItem(Railcraft.ID, "part.signal.lamp", 1L, 0))
                .itemOutputs(getModItem(Railcraft.ID, "part.circuit", 16L, 2))
                .fluidInputs(tMat.getMolten(tMultiplier * HALF_INGOTS))
                .duration(25 * SECONDS)
                .eut((int) TierEU.RECIPE_LV)
                .addTo(circuitAssemblerRecipes);
        }
    }

    public void registerForestryRecipes() {
        if (!Forestry.isModLoaded()) {
            return;
        }

        // alternative version of the copper electron tube

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.RedAlloy, 1),
                GTOreDictUnificator.get(OrePrefixes.bolt, Materials.Gold, 2),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 2),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.AnnealedCopper, 2))
            .itemOutputs(getModItem(Forestry.ID, "thermionicTubes", 4L, 0))
            .fluidInputs(Materials.Glass.getMolten(4 * INGOTS))
            .duration(10 * SECONDS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(circuitAssemblerRecipes);

        // alternative version of the iron electron tube

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.RedAlloy, 1),
                GTOreDictUnificator.get(OrePrefixes.bolt, Materials.Gold, 2),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 2),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.WroughtIron, 2))
            .itemOutputs(getModItem(Forestry.ID, "thermionicTubes", 4L, 3))
            .fluidInputs(Materials.Glass.getMolten(4 * INGOTS))
            .duration(10 * SECONDS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(circuitAssemblerRecipes);

        ItemStack[] rodMaterials = new ItemStack[] {
            GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Copper, 2L),
            GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Tin, 2L),
            GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Bronze, 2L),
            GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Iron, 2L),
            GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Gold, 2L),
            GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Diamond, 2L),
            getModItem(NewHorizonsCoreMod.ID, "LongObsidianRod", 2L, 0),
            GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Blaze, 2L),
            GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Rubber, 2L),
            GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Emerald, 2L),
            GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Apatite, 2L),
            GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Lapis, 2L),
            GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.EnderEye, 2L),
            GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Uranium, 2L), };

        for (int metaid = 0; metaid < rodMaterials.length; metaid++) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.RedAlloy, 1),
                    GTOreDictUnificator.get(OrePrefixes.bolt, Materials.Gold, 2),
                    GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 2),
                    rodMaterials[metaid])
                .itemOutputs(getModItem(Forestry.ID, "thermionicTubes", 4L, metaid))
                .fluidInputs(Materials.Glass.getMolten(4 * INGOTS))
                .duration(10 * SECONDS)
                .eut((int) TierEU.RECIPE_LV)
                .addTo(circuitAssemblerRecipes);
        }

        for (Materials tMat : solderingMats) {

            int tMultiplier = tMat.contains(SubTag.SOLDERING_MATERIAL_GOOD) ? 1
                : tMat.contains(SubTag.SOLDERING_MATERIAL_BAD) ? 4 : 2;

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Circuit_Board_Coated_Basic.get(1),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ULV, 2),
                    GTOreDictUnificator.get(OrePrefixes.foil, Materials.Iron, 2),
                    GTOreDictUnificator.get(OrePrefixes.screw, Materials.Iron, 4),
                    GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Iron, 1))
                .circuit(1)
                .itemOutputs(getModItem(Forestry.ID, "chipsets", 1L, 0))
                .fluidInputs(tMat.getMolten(1152L * tMultiplier / 2))
                .duration(10 * SECONDS)
                .eut((int) TierEU.RECIPE_LV)
                .addTo(circuitAssemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Circuit_Board_Coated_Basic.get(1),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LV, 2),
                    GTOreDictUnificator.get(OrePrefixes.foil, Materials.Bronze, 2),
                    GTOreDictUnificator.get(OrePrefixes.screw, Materials.Bronze, 4),
                    GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Bronze, 1))
                .circuit(1)
                .itemOutputs(getModItem(Forestry.ID, "chipsets", 1L, 1))
                .fluidInputs(tMat.getMolten(1152L * tMultiplier / 2))
                .duration(10 * SECONDS)
                .eut((int) TierEU.RECIPE_LV)
                .addTo(circuitAssemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Circuit_Board_Phenolic_Good.get(1),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.MV, 2),
                    GTOreDictUnificator.get(OrePrefixes.foil, Materials.Steel, 2),
                    GTOreDictUnificator.get(OrePrefixes.screw, Materials.Steel, 4),
                    GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Steel, 1))
                .circuit(1)
                .itemOutputs(getModItem(Forestry.ID, "chipsets", 1L, 2))
                .fluidInputs(tMat.getMolten(1152L * tMultiplier / 2))
                .duration(10 * SECONDS)
                .eut((int) TierEU.RECIPE_LV)
                .addTo(circuitAssemblerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Circuit_Board_Phenolic_Good.get(1),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 2),
                    GTOreDictUnificator.get(OrePrefixes.foil, Materials.Electrum, 2),
                    GTOreDictUnificator.get(OrePrefixes.screw, Materials.Electrum, 4),
                    GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Electrum, 1))
                .circuit(1)
                .itemOutputs(getModItem(Forestry.ID, "chipsets", 1L, 3))
                .fluidInputs(tMat.getMolten(1152L * tMultiplier / 2))
                .duration(10 * SECONDS)
                .eut((int) TierEU.RECIPE_LV)
                .addTo(circuitAssemblerRecipes);

        }
    }
}
