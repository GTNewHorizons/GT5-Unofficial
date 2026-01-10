package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.ModIDs.NEW_HORIZONS_CORE_MOD;
import static gregtech.api.recipe.RecipeMaps.advancedCircuitAssemblylineRecipes;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.formingPressRecipes;
import static gregtech.api.recipe.RecipeMaps.pcbFactoryRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.PCB_NANITE_MATERIAL;
import static gtPlusPlus.core.material.MaterialsElements.STANDALONE.ADVANCED_NITINOL;
import static gtPlusPlus.core.material.MaterialsElements.STANDALONE.HYPOGEN;
import static gtnhintergalactic.recipe.IGRecipeMaps.MODULE_TIER;
import static gtnhintergalactic.recipe.IGRecipeMaps.spaceAssemblerRecipes;

import bartworks.system.material.WerkstoffLoader;
import net.minecraft.item.ItemStack;

import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;
import tectech.thing.CustomItemList;

public class AdvancedCircuitAssemblyLineRecipes implements Runnable {

    @Override
    public void run() {
        registerAcal();
        registerComponents();
        registerAnyCircuitRecipes();
        registerExoticLineRecipe();
    }

    private void registerAcal() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.CircuitAssemblerUIV.get(8L),
                ItemList.Circuit_OpticalMainframe.get(16L),
                ItemList.Robot_Arm_UIV.get(8L),
                ItemList.Robot_Arm_UEV.get(8L),
                ItemList.Conveyor_Module_UIV.get(16L),
                ItemList.Conveyor_Module_UEV.get(16L))
            .fluidInputs(
                Materials.Kevlar.getMolten(4 * INGOTS),
                Materials.TranscendentMetal.getMolten(16 * INGOTS),
                Materials.DimensionallyShiftedSuperfluid.getFluid(32 * INGOTS))
            .itemOutputs(ItemList.AdvancedCircuitAssemblyLine.get(1L))
            .duration(150 * SECONDS)
            .eut(TierEU.UEV)
            .addTo(spaceAssemblerRecipes);
    }

    private void registerComponents() {

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(3),
                Materials.RadoxPolymer.getPlates(1),
                new ItemStack(WerkstoffLoader.items.get(OrePrefixes.foil), 20, 10106),
                HYPOGEN.getFoil(20),
                ADVANCED_NITINOL.getFoil(20)
            )
            .fluidInputs(
                Materials.SulfuricAcid.getFluid(707),
                Materials.IronIIIChloride.getFluid(17677),
                Materials.CosmicNeutronium.getMolten(6109)
            )
            .itemOutputs(ItemList.Circuit_Board_Exotic.get(14))
            .duration(15 * SECONDS)
            .eut(TierEU.UHV)
            .metadata(PCB_NANITE_MATERIAL, Materials.Gold)
            .addTo(pcbFactoryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(4),
                Materials.RadoxPolymer.getPlates(1),
                new ItemStack(WerkstoffLoader.items.get(OrePrefixes.foil), 22, 10106),
                HYPOGEN.getFoil(22),
                ADVANCED_NITINOL.getFoil(22)
            )
            .fluidInputs(
                Materials.SulfuricAcid.getFluid(707),
                Materials.IronIIIChloride.getFluid(17677),
                Materials.CosmicNeutronium.getMolten(6109)
            )
            .itemOutputs(ItemList.Circuit_Board_Exotic.get(18))
            .duration(13 * SECONDS)
            .eut(TierEU.UEV)
            .metadata(PCB_NANITE_MATERIAL, Materials.TranscendentMetal)
            .addTo(pcbFactoryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Exotic.get(1),
                MaterialsAlloy.BOTMIUM.getFoil(1),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.NickelZincFerrite, 1),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.NaquadahAlloy, 1),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.VibrantAlloy, 1),
                GTOreDictUnificator.get(OrePrefixes.bolt, Materials.Bedrockium, 4))
            .itemOutputs(ItemList.Optical_Cpu_Containment_Housing.get(4))
            .duration(3 * SECONDS)
            .eut(TierEU.UIV)
            .addTo(formingPressRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.RadoxPolymer, 4), // here one item
                MaterialsAlloy.OCTIRON.getFoil(2),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.SuperconductorUMVBase, 1),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 1),
                GTUtility.getIntegratedCircuit(9))
            .fluidInputs(GGMaterial.shirabon.getMolten(1))
            .itemOutputs(ItemList.Circuit_Parts_CapacitorISMD.get(64))
            .duration(1)
            .eut(TierEU.UIV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Kevlar, 4),
                MaterialsAlloy.BLACK_TITANIUM.getFoil(2),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.SuperconductorUMVBase, 1),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 1),
                GTUtility.getIntegratedCircuit(9))
            .fluidInputs(GGMaterial.shirabon.getMolten(1))
            .itemOutputs(ItemList.Circuit_Parts_TransistorISMD.get(64))
            .duration(1)
            .eut(TierEU.UIV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialsAlloy.BOTMIUM.getFoil(4),
                MaterialsAlloy.ABYSSAL.getFoil(2),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.SuperconductorUMVBase, 1),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 1),
                GTUtility.getIntegratedCircuit(9))
            .fluidInputs(GGMaterial.shirabon.getMolten(1))
            .itemOutputs(ItemList.Circuit_Parts_DiodeISMD.get(64))
            .duration(1)
            .eut(TierEU.UIV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.ProtoHalkonite, 4),
                MaterialsAlloy.QUANTUM.getFoil(2),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.SuperconductorUMVBase, 1),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 1),
                GTUtility.getIntegratedCircuit(9))
            .fluidInputs(GGMaterial.shirabon.getMolten(1))
            .itemOutputs(ItemList.Circuit_Parts_ResistorISMD.get(64))
            .duration(1)
            .eut(TierEU.UIV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Ichorium, 4),
                MaterialsAlloy.ABYSSAL.getFoil(2),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.SuperconductorUMVBase, 1),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 1),
                GTUtility.getIntegratedCircuit(9))
            .fluidInputs(GGMaterial.shirabon.getMolten(1))
            .itemOutputs(ItemList.Circuit_Parts_InductorISMD.get(64))
            .duration(1)
            .eut(TierEU.UIV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Parts_CapacitorISMD.get(12),
                ItemList.Circuit_Parts_TransistorISMD.get(12),
                ItemList.Circuit_Parts_InductorISMD.get(12),
                ItemList.Circuit_Parts_ResistorISMD.get(12),
                ItemList.Circuit_Parts_DiodeISMD.get(12),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, 1),
                GTUtility.getIntegratedCircuit(9))
            .fluidInputs(Materials.PhononMedium.getFluid(4))
            .itemOutputs(ItemList.Circuit_Parts_UniversalISMD.get(64))
            .duration(1)
            .eut(TierEU.UMV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Optically_Perfected_CPU.get(2L),
                ItemList.Circuit_Wafer_QuantumCPU.get(64L),
                ItemList.Circuit_Wafer_Ram.get(64L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(64L),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SpaceTime, 2L),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.TranscendentMetal, 1L))
            .fluidInputs(Materials.Kevlar.getMolten(4 * INGOTS))
            .itemOutputs(ItemList.Exotic_Super_CPU.get(1L))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(spaceAssemblerRecipes);
    }

    private ItemStack[] computeOutputForAnyCircuits(String aItem, int amount) {
        if (amount <= 0) {
            return new ItemStack[0];
        }
        int fullStacks = amount / 64;
        int remainder = amount % 64;
        int totalStacks = remainder > 0 ? fullStacks + 1 : fullStacks;
        ItemStack[] result = new ItemStack[totalStacks];

        int index = 0;
        for (int i = 0; i < fullStacks; i++) {
            result[index++] = getModItem(NEW_HORIZONS_CORE_MOD, aItem, 64, 0);
        }

        if (remainder > 0) {
            result[index] = getModItem(NEW_HORIZONS_CORE_MOD, aItem, remainder, 0);
        }

        return result;
    }

    private void registerAnyCircuitRecipes() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Exotic.get(1L),
                ItemList.Circuit_Wafer_Simple_SoC.get(1L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.RedAlloy, 2L),
                GTOreDictUnificator.get(OrePrefixes.bolt, Materials.RedAlloy, 2L))
            .itemOutputs(computeOutputForAnyCircuits("item.CircuitULV", 64 * 8))
            .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(4 * INGOTS))
            .duration(200 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(advancedCircuitAssemblylineRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Exotic.get(1L),
                ItemList.Circuit_Wafer_SoC.get(1L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 2L),
                GTOreDictUnificator.get(OrePrefixes.bolt, Materials.Copper, 2L))
            .itemOutputs(computeOutputForAnyCircuits("item.CircuitLV", 64 * 6))
            .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(4 * INGOTS))
            .duration(250 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(advancedCircuitAssemblylineRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Exotic.get(1L),
                ItemList.Circuit_Wafer_SoC.get(1L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.AnnealedCopper, 2L),
                GTOreDictUnificator.get(OrePrefixes.bolt, Materials.AnnealedCopper, 2L))
            .itemOutputs(computeOutputForAnyCircuits("item.CircuitMV", 64 * 4))
            .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(4 * INGOTS))
            .duration(300 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(advancedCircuitAssemblylineRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Exotic.get(1L),
                ItemList.Circuit_Wafer_SoC2.get(1L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Electrum, 2L),
                GTOreDictUnificator.get(OrePrefixes.bolt, Materials.Platinum, 2L))
            .itemOutputs(computeOutputForAnyCircuits("item.CircuitHV", 182))
            .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(4 * INGOTS))
            .duration(350 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(advancedCircuitAssemblylineRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Exotic.get(1L),
                ItemList.Circuit_Wafer_SoC2.get(1L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Platinum, 2L),
                GTOreDictUnificator.get(OrePrefixes.bolt, Materials.NiobiumTitanium, 2L))
            .itemOutputs(computeOutputForAnyCircuits("item.CircuitEV", 128))
            .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(4 * INGOTS))
            .duration(400 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(advancedCircuitAssemblylineRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Exotic.get(1L),
                ItemList.Circuit_Chip_CrystalSoC.get(1L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.NiobiumTitanium, 2L),
                GTOreDictUnificator.get(OrePrefixes.bolt, Materials.Yttrium, 2L))
            .itemOutputs(computeOutputForAnyCircuits("item.CircuitIV", 92))
            .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(4 * INGOTS))
            .duration(450 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(advancedCircuitAssemblylineRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Exotic.get(1L),
                ItemList.Circuit_Parts_Crystal_Chip_Wetware.get(1L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Yttrium, 2L),
                GTOreDictUnificator.get(OrePrefixes.bolt, Materials.CosmicNeutronium, 2L))
            .itemOutputs(getModItem(NEW_HORIZONS_CORE_MOD, "item.CircuitLuV", 64, 0))
            .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(4 * INGOTS))
            .duration(500 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(advancedCircuitAssemblylineRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Exotic.get(1L),
                ItemList.Circuit_Parts_Chip_Bioware.get(1L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.NiobiumTitanium, 2L),
                GTOreDictUnificator.get(OrePrefixes.bolt, Materials.Infinity, 2L))
            .itemOutputs(getModItem(NEW_HORIZONS_CORE_MOD, "item.CircuitZPM", 46, 0))
            .fluidInputs(MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(4 * INGOTS))
            .duration(600 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(advancedCircuitAssemblylineRecipes);

    }

    private void registerExoticLineRecipe() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Exotic.get(1L),
                ItemList.Exotic_Super_CPU.get(1L),
                ItemList.Optically_Compatible_Memory.get(2L),
                ItemList.Circuit_Parts_CapacitorISMD.get(12L),
                ItemList.Circuit_Wafer_QPIC.get(16L),
                CustomItemList.DATApipe.get(16L),
                GTOreDictUnificator.get(OrePrefixes.bolt, Materials.Infinity, 16L))
            .fluidInputs(
                GGMaterial.metastableOganesson.getMolten(2 * INGOTS),
                Materials.DimensionallyShiftedSuperfluid.getFluid(500L))
            .itemOutputs(ItemList.Circuit_ExoticProcessor.get(1L))
            .duration(150 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(advancedCircuitAssemblylineRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Exotic.get(1L),
                ItemList.Circuit_ExoticProcessor.get(2L),
                ItemList.Optically_Compatible_Memory.get(4L),
                ItemList.Circuit_Parts_CapacitorISMD.get(12L),
                ItemList.Circuit_Parts_InductorISMD.get(12L),
                ItemList.Circuit_Parts_ResistorISMD.get(12L),
                ItemList.Circuit_Parts_TransistorISMD.get(12L),
                ItemList.Circuit_Wafer_QPIC.get(32L),
                GTOreDictUnificator.get(OrePrefixes.bolt, Materials.TranscendentMetal, 16L))
            .fluidInputs(
                GGMaterial.metastableOganesson.getMolten(4 * INGOTS),
                Materials.DimensionallyShiftedSuperfluid.getFluid(1000L))
            .itemOutputs(ItemList.Circuit_ExoticAssembly.get(1L))
            .duration(200 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(advancedCircuitAssemblylineRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Exotic.get(1L),
                ItemList.Circuit_ExoticAssembly.get(2L),
                ItemList.Optically_Compatible_Memory.get(8L),
                ItemList.Circuit_Parts_CapacitorISMD.get(12L),
                ItemList.Circuit_Parts_DiodeISMD.get(12L),
                ItemList.Circuit_Parts_ResistorISMD.get(12L),
                ItemList.Circuit_Parts_TransistorISMD.get(12L),
                ItemList.Circuit_Wafer_QPIC.get(48L),
                GTOreDictUnificator.get(OrePrefixes.bolt, Materials.SpaceTime, 2L))
            .fluidInputs(
                GGMaterial.metastableOganesson.getMolten(8 * INGOTS),
                Materials.DimensionallyShiftedSuperfluid.getFluid(2000L),
                GGMaterial.shirabon.getMolten(4 * INGOTS))
            .itemOutputs(ItemList.Circuit_ExoticComputer.get(1L))
            .duration(300 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .metadata(MODULE_TIER, 2)
            .addTo(advancedCircuitAssemblylineRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_ExoticComputer.get(2L),
                ItemList.Exotic_Super_CPU.get(2L),
                ItemList.Optically_Compatible_Memory.get(16L),
                ItemList.Circuit_Parts_UniversalISMD.get(32L),
                ItemList.Circuit_Wafer_QPIC.get(64L),
                GGMaterial.shirabon.get(OrePrefixes.bolt, 8 * INGOTS))
            .fluidInputs(
                GGMaterial.metastableOganesson.getMolten(16 * INGOTS),
                Materials.DimensionallyShiftedSuperfluid.getFluid(4000L),
                Materials.SixPhasedCopper.getMolten(8 * INGOTS),
                Materials.QuarkGluonPlasma.getFluid(4 * INGOTS))
            .itemOutputs(ItemList.Circuit_ExoticMainframe.get(1L))
            .duration(400 * SECONDS)
            .eut(TierEU.RECIPE_UMV)
            .metadata(MODULE_TIER, 2)
            .addTo(advancedCircuitAssemblylineRecipes);

    }

}
