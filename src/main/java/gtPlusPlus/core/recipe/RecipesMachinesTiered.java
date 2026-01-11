package gtPlusPlus.core.recipe;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.cutterRecipes;
import static gregtech.api.util.GTModHandler.RecipeBits.BITS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.covers.CoverManager;

public class RecipesMachinesTiered {

    public static void loadRecipes() {
        integralCasings();
        energyCores();
        energyBuffers();
        wirelessChargers();
        fakeMachineCasingCovers();
        overflowValveCovers();
        chiselBuses();
        solidifierHatches();
        extruderHatches();
        cropManagers();
        autoWorkbenches();
        autoChisels();
        semifluidGenerators();
        resonanceChambers();
        modulators();
        simpleWashers();
        airFilters();
        tanks();
    }

    private static void wirelessChargers() {
        // LV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_LV.get(1),
                GregtechItemList.TransmissionComponent_LV.get(2),
                ItemList.Field_Generator_LV.get(1),
                MaterialsAlloy.EGLIN_STEEL.getPlate(4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.MV, 2))
            .itemOutputs(GregtechItemList.Charger_LV.get(1))
            .fluidInputs(MaterialsAlloy.SILICON_CARBIDE.getFluidStack(4 * INGOTS))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        // MV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_MV.get(1),
                GregtechItemList.TransmissionComponent_MV.get(2),
                ItemList.Field_Generator_MV.get(1),
                MaterialsAlloy.TANTALUM_CARBIDE.getPlate(4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 2))
            .itemOutputs(GregtechItemList.Charger_MV.get(1))
            .fluidInputs(MaterialsAlloy.BLOODSTEEL.getFluidStack(6 * INGOTS))
            .duration(67 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        // HV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_HV.get(1),
                GregtechItemList.TransmissionComponent_HV.get(2),
                ItemList.Field_Generator_HV.get(1),
                MaterialsAlloy.INCOLOY_DS.getPlate(4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.EV, 2))
            .itemOutputs(GregtechItemList.Charger_HV.get(1))
            .fluidInputs(MaterialsAlloy.TANTALUM_CARBIDE.getFluidStack(8 * INGOTS))
            .duration(90 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // EV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_EV.get(1),
                GregtechItemList.TransmissionComponent_EV.get(2),
                ItemList.Field_Generator_EV.get(1),
                MaterialsAlloy.INCONEL_625.getPlate(4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 2))
            .itemOutputs(GregtechItemList.Charger_EV.get(1))
            .fluidInputs(MaterialsAlloy.INCONEL_792.getFluidStack(10 * INGOTS))
            .duration(112 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // IV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_IV.get(1),
                GregtechItemList.TransmissionComponent_IV.get(2),
                ItemList.Field_Generator_IV.get(1),
                MaterialsAlloy.ZERON_100.getPlate(4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 2))
            .itemOutputs(GregtechItemList.Charger_IV.get(1))
            .fluidInputs(MaterialsAlloy.ARCANITE.getFluidStack(12 * INGOTS))
            .duration(135 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // LuV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_LuV.get(1),
                GregtechItemList.TransmissionComponent_LuV.get(2),
                ItemList.Field_Generator_LuV.get(1),
                MaterialsAlloy.PIKYONIUM.getPlate(4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 2))
            .itemOutputs(GregtechItemList.Charger_LuV.get(1))
            .fluidInputs(MaterialsAlloy.LAFIUM.getFluidStack(14 * INGOTS))
            .duration(157 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        // ZPM
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_ZPM.get(1),
                GregtechItemList.TransmissionComponent_ZPM.get(2),
                ItemList.Field_Generator_ZPM.get(1),
                MaterialsElements.STANDALONE.ADVANCED_NITINOL.getPlate(4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 2))
            .itemOutputs(GregtechItemList.Charger_ZPM.get(1))
            .fluidInputs(MaterialsAlloy.CINOBITE.getFluidStack(16 * INGOTS))
            .duration(180 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        // UV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_UV.get(1),
                GregtechItemList.TransmissionComponent_UV.get(2),
                ItemList.Field_Generator_UV.get(1),
                MaterialsAlloy.ABYSSAL.getPlate(4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UHV, 2))
            .itemOutputs(GregtechItemList.Charger_UV.get(1))
            .fluidInputs(MaterialsAlloy.TITANSTEEL.getFluidStack(18 * INGOTS))
            .duration(202 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_UV)
            .addTo(assemblerRecipes);

        // UHV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_MAX.get(1),
                GregtechItemList.TransmissionComponent_UHV.get(2),
                ItemList.Field_Generator_UHV.get(1),
                MaterialsAlloy.QUANTUM.getPlate(4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 2))
            .itemOutputs(GregtechItemList.Charger_UHV.get(1))
            .fluidInputs(MaterialsAlloy.OCTIRON.getFluidStack(20 * INGOTS))
            .duration(225 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);
    }

    private static void energyCores() {
        // ULV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_LV.get(1),
                MaterialsAlloy.TUMBAGA.getPlate(4),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Tin, 2),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LV, 2),
                MaterialsAlloy.TUMBAGA.getScrew(6),
                GTOreDictUnificator.get(OrePrefixes.bolt, Materials.Lead, 12))
            .itemOutputs(GregtechItemList.Energy_Core_ULV.get(1))
            .fluidInputs(MaterialsAlloy.POTIN.getFluidStack(4 * INGOTS))
            .duration(22 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);

        // LV
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Energy_Core_ULV.get(1),
                MaterialsAlloy.EGLIN_STEEL.getPlate(4),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Cobalt, 2),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.MV, 2),
                MaterialsAlloy.EGLIN_STEEL.getScrew(6),
                GTOreDictUnificator.get(OrePrefixes.bolt, Materials.Aluminium, 12))
            .itemOutputs(GregtechItemList.Energy_Core_LV.get(1))
            .fluidInputs(MaterialsAlloy.TUMBAGA.getFluidStack(8 * INGOTS))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        // MV
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Energy_Core_LV.get(1),
                MaterialsAlloy.TANTALUM_CARBIDE.getPlate(4),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.AnnealedCopper, 2),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 2),
                MaterialsAlloy.TANTALUM_CARBIDE.getScrew(6),
                MaterialsElements.STANDALONE.BLACK_METAL.getBolt(12))
            .itemOutputs(GregtechItemList.Energy_Core_MV.get(1))
            .fluidInputs(MaterialsAlloy.EGLIN_STEEL.getFluidStack(12 * INGOTS))
            .duration(67 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        // HV
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Energy_Core_MV.get(1),
                MaterialsAlloy.INCOLOY_DS.getPlate(4),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Gold, 2),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.EV, 2),
                MaterialsAlloy.INCOLOY_DS.getScrew(6),
                GTOreDictUnificator.get(OrePrefixes.bolt, Materials.Titanium, 12))
            .itemOutputs(GregtechItemList.Energy_Core_HV.get(1))
            .fluidInputs(MaterialsAlloy.TANTALUM_CARBIDE.getFluidStack(16 * INGOTS))
            .duration(90 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // EV
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Energy_Core_HV.get(1),
                MaterialsAlloy.INCONEL_625.getPlate(4),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Titanium, 2),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 2),
                MaterialsAlloy.INCONEL_625.getScrew(6),
                MaterialsAlloy.HASTELLOY_N.getBolt(12))
            .itemOutputs(GregtechItemList.Energy_Core_EV.get(1))
            .fluidInputs(MaterialsAlloy.INCOLOY_DS.getFluidStack(20 * INGOTS))
            .duration(112 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // IV
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Energy_Core_EV.get(1),
                MaterialsAlloy.ZERON_100.getPlate(4),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Nichrome, 2),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 2),
                MaterialsAlloy.ZERON_100.getScrew(6),
                MaterialsAlloy.ENERGYCRYSTAL.getBolt(12))
            .itemOutputs(GregtechItemList.Energy_Core_IV.get(1))
            .fluidInputs(MaterialsAlloy.INCONEL_625.getFluidStack(24 * INGOTS))
            .duration(135 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // LuV
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Energy_Core_IV.get(1),
                MaterialsAlloy.PIKYONIUM.getPlate(4),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Platinum, 2),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 2),
                MaterialsAlloy.PIKYONIUM.getScrew(6),
                MaterialsAlloy.TRINIUM_NAQUADAH_CARBON.getBolt(12))
            .itemOutputs(GregtechItemList.Energy_Core_LuV.get(1))
            .fluidInputs(MaterialsAlloy.ZERON_100.getFluidStack(28 * INGOTS))
            .duration(157 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        // ZPM
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Energy_Core_LuV.get(1),
                MaterialsElements.STANDALONE.ADVANCED_NITINOL.getPlate(4),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.YttriumBariumCuprate, 2),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 2),
                MaterialsAlloy.TITANSTEEL.getScrew(6),
                MaterialsAlloy.TRINIUM_REINFORCED_STEEL.getBolt(12))
            .itemOutputs(GregtechItemList.Energy_Core_ZPM.get(1))
            .fluidInputs(MaterialsAlloy.PIKYONIUM.getFluidStack(32 * INGOTS))
            .duration(180 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        // UV
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Energy_Core_ZPM.get(1),
                MaterialsAlloy.ABYSSAL.getPlate(4),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Naquadah, 2),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UHV, 2),
                MaterialsAlloy.ABYSSAL.getScrew(6),
                MaterialsAlloy.TITANSTEEL.getBolt(12))
            .itemOutputs(GregtechItemList.Energy_Core_UV.get(1))
            .fluidInputs(MaterialsElements.STANDALONE.ADVANCED_NITINOL.getFluidStack(36 * INGOTS))
            .duration(202 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_UV)
            .addTo(assemblerRecipes);

        // UHV
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Energy_Core_UV.get(1),
                MaterialsAlloy.QUANTUM.getPlate(4),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Duranium, 2),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 2),
                MaterialsAlloy.QUANTUM.getScrew(6),
                MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getBolt(12))
            .itemOutputs(GregtechItemList.Energy_Core_UHV.get(1))
            .fluidInputs(MaterialsAlloy.ABYSSAL.getFluidStack(40 * INGOTS))
            .duration(225 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);
    }

    private static void energyBuffers() {
        // ULV
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Energy_Core_ULV.get(4),
                MaterialsAlloy.TUMBAGA.getPlate(8),
                GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.Tin, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ULV, 4),
                MaterialsAlloy.SILICON_CARBIDE.getLongRod(4),
                MaterialsAlloy.POTIN.getGear(5))
            .itemOutputs(GregtechItemList.Energy_Buffer_1by1_ULV.get(1))
            .fluidInputs(MaterialsAlloy.TUMBAGA.getFluidStack(16 * INGOTS))
            .duration(45 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        // LV
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Energy_Core_LV.get(4),
                MaterialsAlloy.EGLIN_STEEL.getPlate(8),
                GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.Cobalt, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LV, 4),
                MaterialsAlloy.BLOODSTEEL.getLongRod(4),
                MaterialsAlloy.TUMBAGA.getGear(5))
            .itemOutputs(GregtechItemList.Energy_Buffer_1by1_LV.get(1))
            .fluidInputs(MaterialsAlloy.EGLIN_STEEL.getFluidStack(32 * INGOTS))
            .duration(90 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        // MV
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Energy_Core_MV.get(4),
                MaterialsAlloy.TANTALUM_CARBIDE.getPlate(8),
                GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.AnnealedCopper, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.MV, 4),
                MaterialsAlloy.TANTALUM_CARBIDE.getLongRod(4),
                MaterialsAlloy.EGLIN_STEEL.getGear(5))
            .itemOutputs(GregtechItemList.Energy_Buffer_1by1_MV.get(1))
            .fluidInputs(MaterialsAlloy.TANTALUM_CARBIDE.getFluidStack(48 * INGOTS))
            .duration(135 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        // HV
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Energy_Core_HV.get(4),
                MaterialsAlloy.INCOLOY_DS.getPlate(8),
                GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.Gold, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 4),
                MaterialsAlloy.INCONEL_792.getLongRod(4),
                MaterialsAlloy.TANTALUM_CARBIDE.getGear(5))
            .itemOutputs(GregtechItemList.Energy_Buffer_1by1_HV.get(1))
            .fluidInputs(MaterialsAlloy.INCOLOY_DS.getFluidStack(64 * INGOTS))
            .duration(180 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // EV
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Energy_Core_EV.get(4),
                MaterialsAlloy.INCONEL_625.getPlate(8),
                GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.Titanium, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.EV, 4),
                MaterialsAlloy.ARCANITE.getLongRod(4),
                MaterialsAlloy.INCOLOY_DS.getGear(5))
            .itemOutputs(GregtechItemList.Energy_Buffer_1by1_EV.get(1))
            .fluidInputs(MaterialsAlloy.INCONEL_625.getFluidStack(80 * INGOTS))
            .duration(225 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // IV
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Energy_Core_IV.get(4),
                MaterialsAlloy.ZERON_100.getPlate(8),
                GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.Nichrome, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 4),
                MaterialsAlloy.LAFIUM.getLongRod(4),
                MaterialsAlloy.INCONEL_625.getGear(5))
            .itemOutputs(GregtechItemList.Energy_Buffer_1by1_IV.get(1))
            .fluidInputs(MaterialsAlloy.ZERON_100.getFluidStack(96 * INGOTS))
            .duration(270 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // LuV
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Energy_Core_LuV.get(4),
                MaterialsAlloy.PIKYONIUM.getPlate(8),
                GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.Platinum, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 4),
                MaterialsAlloy.CINOBITE.getLongRod(4),
                MaterialsAlloy.ZERON_100.getGear(5))
            .itemOutputs(GregtechItemList.Energy_Buffer_1by1_LuV.get(1))
            .fluidInputs(MaterialsAlloy.PIKYONIUM.getFluidStack(112 * INGOTS))
            .duration(315 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        // ZPM
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Energy_Core_ZPM.get(4),
                MaterialsElements.STANDALONE.ADVANCED_NITINOL.getPlate(8),
                GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.YttriumBariumCuprate, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 4),
                MaterialsAlloy.TITANSTEEL.getLongRod(4),
                MaterialsAlloy.PIKYONIUM.getGear(5))
            .itemOutputs(GregtechItemList.Energy_Buffer_1by1_ZPM.get(1))
            .fluidInputs(MaterialsElements.STANDALONE.ADVANCED_NITINOL.getFluidStack(128 * INGOTS))
            .duration(360 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        // UV
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Energy_Core_UV.get(4),
                MaterialsAlloy.ABYSSAL.getPlate(8),
                GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.Naquadah, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 4),
                MaterialsAlloy.OCTIRON.getLongRod(4),
                MaterialsAlloy.TITANSTEEL.getGear(5))
            .itemOutputs(GregtechItemList.Energy_Buffer_1by1_UV.get(1))
            .fluidInputs(MaterialsAlloy.ABYSSAL.getFluidStack(144 * INGOTS))
            .duration(405 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(assemblerRecipes);

        // UHV
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Energy_Core_UHV.get(4),
                MaterialsAlloy.QUANTUM.getPlate(8),
                GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.Duranium, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UHV, 4),
                MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getLongRod(4),
                MaterialsAlloy.ABYSSAL.getGear(5))
            .itemOutputs(GregtechItemList.Energy_Buffer_1by1_MAX.get(1))
            .fluidInputs(MaterialsAlloy.QUANTUM.getFluidStack(160 * INGOTS))
            .duration(450 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);
    }

    private static void integralCasings() {
        // Integral Encasement I (ULV)
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_ULV.get(1),
                MaterialsAlloy.POTIN.getPlate(8),
                MaterialsAlloy.POTIN.getGear(2),
                GTOreDictUnificator.get(OrePrefixes.cableGt02, Materials.Tin, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ULV, 2))
            .circuit(20)
            .itemOutputs(GregtechItemList.GTPP_Casing_ULV.get(1))
            .fluidInputs(Materials.Steel.getMolten(2 * INGOTS))
            .duration(20 * SECONDS)
            .eut(16)
            .addTo(assemblerRecipes);

        // Integral Encasement II (LV)
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_LV.get(1),
                MaterialsAlloy.TUMBAGA.getPlate(8),
                MaterialsAlloy.TUMBAGA.getGear(2),
                GTOreDictUnificator.get(OrePrefixes.cableGt02, Materials.Cobalt, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LV, 2))
            .circuit(20)
            .itemOutputs(GregtechItemList.GTPP_Casing_LV.get(1))
            .fluidInputs(MaterialsAlloy.SILICON_CARBIDE.getFluidStack(4 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        // Integral Encasement III (MV)
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_MV.get(1),
                MaterialsAlloy.EGLIN_STEEL.getPlate(8),
                MaterialsAlloy.EGLIN_STEEL.getGear(2),
                GTOreDictUnificator.get(OrePrefixes.cableGt02, Materials.AnnealedCopper, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.MV, 2))
            .circuit(20)
            .itemOutputs(GregtechItemList.GTPP_Casing_MV.get(1))
            .fluidInputs(MaterialsAlloy.BLOODSTEEL.getFluidStack(6 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        // Integral Encasement IV (HV)
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_HV.get(1),
                MaterialsAlloy.TANTALUM_CARBIDE.getPlate(8),
                MaterialsAlloy.TANTALUM_CARBIDE.getGear(2),
                GTOreDictUnificator.get(OrePrefixes.cableGt02, Materials.Gold, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 2))
            .circuit(20)
            .itemOutputs(GregtechItemList.GTPP_Casing_HV.get(1))
            .fluidInputs(MaterialsAlloy.TANTALUM_CARBIDE.getFluidStack(8 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // Integral Encasement V (EV)
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_EV.get(1),
                MaterialsAlloy.INCOLOY_DS.getPlate(8),
                MaterialsAlloy.INCOLOY_DS.getGear(2),
                GTOreDictUnificator.get(OrePrefixes.cableGt02, Materials.Titanium, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.EV, 2))
            .circuit(20)
            .itemOutputs(GregtechItemList.GTPP_Casing_EV.get(1))
            .fluidInputs(MaterialsAlloy.INCONEL_792.getFluidStack(10 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // Integral Framework I (IV)
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_IV.get(1),
                MaterialsAlloy.INCONEL_625.getPlate(8),
                MaterialsAlloy.INCONEL_625.getGear(2),
                GTOreDictUnificator.get(OrePrefixes.cableGt02, Materials.Nichrome, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 2))
            .circuit(20)
            .itemOutputs(GregtechItemList.GTPP_Casing_IV.get(1))
            .fluidInputs(MaterialsAlloy.ARCANITE.getFluidStack(12 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Integral Framework II (LuV)
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_LuV.get(1),
                MaterialsAlloy.ZERON_100.getPlate(8),
                MaterialsAlloy.ZERON_100.getGear(2),
                GTOreDictUnificator.get(OrePrefixes.cableGt02, Materials.Platinum, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 2))
            .circuit(20)
            .itemOutputs(GregtechItemList.GTPP_Casing_LuV.get(1))
            .fluidInputs(MaterialsAlloy.LAFIUM.getFluidStack(14 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        // Integral Framework III (ZPM)
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_ZPM.get(1),
                MaterialsAlloy.PIKYONIUM.getPlate(8),
                MaterialsAlloy.PIKYONIUM.getGear(2),
                GTOreDictUnificator.get(OrePrefixes.cableGt02, Materials.YttriumBariumCuprate, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 2))
            .circuit(20)
            .itemOutputs(GregtechItemList.GTPP_Casing_ZPM.get(1))
            .fluidInputs(MaterialsAlloy.CINOBITE.getFluidStack(16 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        // Integral Framework IV (UV)
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_UV.get(1),
                MaterialsElements.STANDALONE.ADVANCED_NITINOL.getPlate(8),
                MaterialsAlloy.TITANSTEEL.getGear(2),
                GTOreDictUnificator.get(OrePrefixes.cableGt02, Materials.Naquadah, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 2))
            .circuit(20)
            .itemOutputs(GregtechItemList.GTPP_Casing_UV.get(1))
            .fluidInputs(MaterialsAlloy.TITANSTEEL.getFluidStack(18 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(assemblerRecipes);

        // Integral Framework V (UHV)
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_MAX.get(1),
                MaterialsAlloy.ABYSSAL.getPlate(8),
                MaterialsAlloy.ABYSSAL.getGear(2),
                GTOreDictUnificator.get(OrePrefixes.cableGt02, Materials.Duranium, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UHV, 2))
            .circuit(20)
            .itemOutputs(GregtechItemList.GTPP_Casing_UHV.get(1))
            .fluidInputs(MaterialsAlloy.OCTIRON.getFluidStack(20 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);
    }

    private static void overflowValveCovers() {
        // LV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Electric_Pump_LV.get(2),
                ItemList.Electric_Motor_LV.get(2),
                MaterialsAlloy.TUMBAGA.getPlate(4))
            .circuit(19)
            .itemOutputs(GregtechItemList.Cover_Overflow_Valve_LV.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(1 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        // MV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Electric_Pump_MV.get(2),
                ItemList.Electric_Motor_MV.get(2),
                MaterialsAlloy.EGLIN_STEEL.getPlate(4))
            .circuit(19)
            .itemOutputs(GregtechItemList.Cover_Overflow_Valve_MV.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(2 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        // HV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Electric_Pump_HV.get(2),
                ItemList.Electric_Motor_HV.get(2),
                MaterialsAlloy.TANTALUM_CARBIDE.getPlate(4))
            .circuit(19)
            .itemOutputs(GregtechItemList.Cover_Overflow_Valve_HV.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(3 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // EV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Electric_Pump_EV.get(2),
                ItemList.Electric_Motor_EV.get(2),
                MaterialsAlloy.INCOLOY_DS.getPlate(4))
            .circuit(19)
            .itemOutputs(GregtechItemList.Cover_Overflow_Valve_EV.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(4 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // IV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Electric_Pump_IV.get(2),
                ItemList.Electric_Motor_IV.get(2),
                MaterialsAlloy.INCONEL_625.getPlate(4))
            .circuit(19)
            .itemOutputs(GregtechItemList.Cover_Overflow_Valve_IV.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(5 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
    }

    private static void modulators() {
        // Modulator I
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Modulator_I.get(1),
            new Object[] { "CPC", "PHP", "CPC", 'C', "circuitData", 'P', MaterialsAlloy.INCOLOY_DS.getPlate(1), 'H',
                ItemList.Casing_EV });

        // Modulator II
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Modulator_II.get(1),
            new Object[] { "CPC", "PHP", "CPC", 'C', "circuitElite", 'P', MaterialsAlloy.INCONEL_625.getPlate(1), 'H',
                ItemList.Casing_IV });

        // Modulator III
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Modulator_III.get(1),
            new Object[] { "CPC", "PHP", "CPC", 'C', "circuitMaster", 'P', MaterialsAlloy.ZERON_100.getPlate(1), 'H',
                ItemList.Casing_LuV });

        // Modulator IV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Modulator_IV.get(1),
            new Object[] { "CPC", "PHP", "CPC", 'C', "circuitUltimate", 'P', MaterialsAlloy.PIKYONIUM.getPlate(1), 'H',
                ItemList.Casing_ZPM });
    }

    private static void resonanceChambers() {
        // Resonance Chamber I
        GTModHandler.addCraftingRecipe(
            GregtechItemList.ResonanceChamber_I.get(1),
            new Object[] { "PFP", "FHF", "PFP", 'P', MaterialsAlloy.INCOLOY_DS.getPlateDouble(1), 'F',
                ItemList.Field_Generator_LV, 'H', ItemList.Casing_EV });

        // Resonance Chamber II
        GTModHandler.addCraftingRecipe(
            GregtechItemList.ResonanceChamber_II.get(1),
            new Object[] { "PFP", "FHF", "PFP", 'P', MaterialsAlloy.INCONEL_625.getPlateDouble(1), 'F',
                ItemList.Field_Generator_MV, 'H', ItemList.Casing_IV });

        // Resonance Chamber III
        GTModHandler.addCraftingRecipe(
            GregtechItemList.ResonanceChamber_III.get(1),
            new Object[] { "PFP", "FHF", "PFP", 'P', MaterialsAlloy.ZERON_100.getPlateDouble(1), 'F',
                ItemList.Field_Generator_HV, 'H', ItemList.Casing_LuV });

        // Resonance Chamber IV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.ResonanceChamber_IV.get(1),
            new Object[] { "PFP", "FHF", "PFP", 'P', MaterialsAlloy.PIKYONIUM.getPlateDouble(1), 'F',
                ItemList.Field_Generator_EV, 'H', ItemList.Casing_ZPM });
    }

    private static void autoWorkbenches() {
        // LV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.GT4_Electric_Auto_Workbench_LV.get(1),
            new Object[] { "PCP", "IHI", "PRP", 'P', OrePrefixes.plate.get(Materials.Steel), 'C',
                new ItemStack(Blocks.crafting_table), 'I', OrePrefixes.circuit.get(Materials.LV), 'H', ItemList.Hull_LV,
                'R', ItemList.Robot_Arm_LV });

        // MV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.GT4_Electric_Auto_Workbench_MV.get(1),
            new Object[] { "PCP", "IHI", "PRP", 'P', OrePrefixes.plate.get(Materials.Aluminium), 'C',
                new ItemStack(Blocks.crafting_table), 'I', OrePrefixes.circuit.get(Materials.MV), 'H', ItemList.Hull_MV,
                'R', ItemList.Robot_Arm_MV });

        // HV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.GT4_Electric_Auto_Workbench_HV.get(1),
            new Object[] { "PCP", "IHI", "PRP", 'P', OrePrefixes.plate.get(Materials.StainlessSteel), 'C',
                new ItemStack(Blocks.crafting_table), 'I', OrePrefixes.circuit.get(Materials.HV), 'H', ItemList.Hull_HV,
                'R', ItemList.Robot_Arm_HV });

        // EV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.GT4_Electric_Auto_Workbench_EV.get(1),
            new Object[] { "PCP", "IHI", "PRP", 'P', OrePrefixes.plate.get(Materials.Titanium), 'C',
                new ItemStack(Blocks.crafting_table), 'I', OrePrefixes.circuit.get(Materials.EV), 'H', ItemList.Hull_EV,
                'R', ItemList.Robot_Arm_EV });

        // IV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.GT4_Electric_Auto_Workbench_IV.get(1),
            new Object[] { "PCP", "IHI", "PRP", 'P', OrePrefixes.plate.get(Materials.TungstenSteel), 'C',
                new ItemStack(Blocks.crafting_table), 'I', OrePrefixes.circuit.get(Materials.IV), 'H', ItemList.Hull_IV,
                'R', ItemList.Robot_Arm_IV });

        // LuV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.GT4_Electric_Auto_Workbench_LuV.get(1),
            new Object[] { "PCP", "IHI", "PRP", 'P', OrePrefixes.plate.get(Materials.Chrome), 'C',
                new ItemStack(Blocks.crafting_table), 'I', OrePrefixes.circuit.get(Materials.LuV), 'H',
                ItemList.Hull_LuV, 'R', ItemList.Robot_Arm_LuV });

        // ZPM
        GTModHandler.addCraftingRecipe(
            GregtechItemList.GT4_Electric_Auto_Workbench_ZPM.get(1),
            new Object[] { "PCP", "IHI", "PRP", 'P', OrePrefixes.plate.get(Materials.Iridium), 'C',
                new ItemStack(Blocks.crafting_table), 'I', OrePrefixes.circuit.get(Materials.ZPM), 'H',
                ItemList.Hull_ZPM, 'R', ItemList.Robot_Arm_ZPM });

        // UV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.GT4_Electric_Auto_Workbench_UV.get(1),
            new Object[] { "PCP", "IHI", "PRP", 'P', OrePrefixes.plate.get(Materials.Osmium), 'C',
                new ItemStack(Blocks.crafting_table), 'I', OrePrefixes.circuit.get(Materials.UV), 'H', ItemList.Hull_UV,
                'R', ItemList.Robot_Arm_UV });
    }

    private static void cropManagers() {
        // LV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.GT4_Crop_Harvester_LV.get(1),
            new Object[] { "ASA", "PHP", "CMC", 'A', ItemList.Robot_Arm_LV, 'S', ItemList.Sensor_LV, 'P',
                OrePrefixes.plate.get(Materials.Steel), 'H', ItemList.Hull_LV, 'C',
                OrePrefixes.circuit.get(Materials.LV), 'M', ItemList.Hatch_Input_LV });

        // MV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.GT4_Crop_Harvester_MV.get(1),
            new Object[] { "ASA", "PHP", "CMC", 'A', ItemList.Robot_Arm_MV, 'S', ItemList.Sensor_MV, 'P',
                OrePrefixes.plate.get(Materials.Aluminium), 'H', ItemList.Hull_MV, 'C',
                OrePrefixes.circuit.get(Materials.MV), 'M', ItemList.Hatch_Input_MV });

        // HV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.GT4_Crop_Harvester_HV.get(1),
            new Object[] { "ASA", "PHP", "CMC", 'A', ItemList.Robot_Arm_HV, 'S', ItemList.Sensor_HV, 'P',
                OrePrefixes.plate.get(Materials.StainlessSteel), 'H', ItemList.Hull_HV, 'C',
                OrePrefixes.circuit.get(Materials.HV), 'M', ItemList.Hatch_Input_HV });

        // EV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.GT4_Crop_Harvester_EV.get(1),
            new Object[] { "ASA", "PHP", "CMC", 'A', ItemList.Robot_Arm_EV, 'S', ItemList.Sensor_EV, 'P',
                OrePrefixes.plate.get(Materials.Titanium), 'H', ItemList.Hull_EV, 'C',
                OrePrefixes.circuit.get(Materials.EV), 'M', ItemList.Hatch_Input_EV });

        // IV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.GT4_Crop_Harvester_IV.get(1),
            new Object[] { "ASA", "PHP", "CMC", 'A', ItemList.Robot_Arm_IV, 'S', ItemList.Sensor_IV, 'P',
                OrePrefixes.plate.get(Materials.TungstenSteel), 'H', ItemList.Hull_IV, 'C',
                OrePrefixes.circuit.get(Materials.IV), 'M', ItemList.Hatch_Input_IV });

        // LuV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.GT4_Crop_Harvester_LuV.get(1),
            new Object[] { "ASA", "PHP", "CMC", 'A', ItemList.Robot_Arm_LuV, 'S', ItemList.Sensor_LuV, 'P',
                OrePrefixes.plate.get(Materials.Chrome), 'H', ItemList.Hull_LuV, 'C',
                OrePrefixes.circuit.get(Materials.LuV), 'M', ItemList.Hatch_Input_LuV });

        // ZPM
        GTModHandler.addCraftingRecipe(
            GregtechItemList.GT4_Crop_Harvester_ZPM.get(1),
            new Object[] { "ASA", "PHP", "CMC", 'A', ItemList.Robot_Arm_ZPM, 'S', ItemList.Sensor_ZPM, 'P',
                OrePrefixes.plate.get(Materials.Iridium), 'H', ItemList.Hull_ZPM, 'C',
                OrePrefixes.circuit.get(Materials.ZPM), 'M', ItemList.Hatch_Input_ZPM });

        // UV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.GT4_Crop_Harvester_UV.get(1),
            new Object[] { "ASA", "PHP", "CMC", 'A', ItemList.Robot_Arm_UV, 'S', ItemList.Sensor_UV, 'P',
                OrePrefixes.plate.get(Materials.Osmium), 'H', ItemList.Hull_UV, 'C',
                OrePrefixes.circuit.get(Materials.UV), 'M', ItemList.Hatch_Input_UV });
    }

    private static void solidifierHatches() {
        // Solidifier Hatch I
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_IV.get(1),
                ItemList.Sensor_IV.get(1),
                ItemList.FluidRegulator_IV.get(1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 4),
                new ItemStack(Blocks.chest))
            .circuit(17)
            .itemOutputs(GregtechItemList.Hatch_Solidifier_I.get(1))
            .fluidInputs(MaterialsAlloy.INCONEL_625.getFluidStack(2 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Solidifier Hatch II
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_LuV.get(1),
                ItemList.Sensor_LuV.get(1),
                ItemList.FluidRegulator_LuV.get(1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 4),
                new ItemStack(Blocks.chest))
            .circuit(17)
            .itemOutputs(GregtechItemList.Hatch_Solidifier_II.get(1))
            .fluidInputs(MaterialsAlloy.ZERON_100.getFluidStack(2 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        // Solidifier Hatch III
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_ZPM.get(1),
                ItemList.Sensor_ZPM.get(1),
                ItemList.FluidRegulator_ZPM.get(1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 4),
                new ItemStack(Blocks.chest))
            .circuit(17)
            .itemOutputs(GregtechItemList.Hatch_Solidifier_III.get(1))
            .fluidInputs(MaterialsAlloy.PIKYONIUM.getFluidStack(2 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        // Solidifier Hatch IV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_UV.get(1),
                ItemList.Sensor_UV.get(1),
                ItemList.FluidRegulator_UV.get(1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UHV, 4),
                new ItemStack(Blocks.chest))
            .circuit(17)
            .itemOutputs(GregtechItemList.Hatch_Solidifier_IV.get(1))
            .fluidInputs(MaterialsElements.STANDALONE.ADVANCED_NITINOL.getFluidStack(2 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(assemblerRecipes);
    }

    private static void extruderHatches() {
        // Extruder Hatch I
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_Bus_IV.get(1),
                ItemList.Sensor_IV.get(1),
                ItemList.Robot_Arm_IV.get(1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 4),
                new ItemStack(Blocks.chest),
                ItemList.Shape_Empty.get(24))
            .circuit(17)
            .itemOutputs(GregtechItemList.Hatch_Extrusion_I.get(1))
            .fluidInputs(MaterialsAlloy.INCONEL_625.getFluidStack(2 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Extruder Hatch II
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_Bus_LuV.get(1),
                ItemList.Sensor_LuV.get(1),
                ItemList.Robot_Arm_LuV.get(1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 4),
                new ItemStack(Blocks.chest),
                ItemList.Shape_Empty.get(24))
            .circuit(17)
            .itemOutputs(GregtechItemList.Hatch_Extrusion_II.get(1))
            .fluidInputs(MaterialsAlloy.ZERON_100.getFluidStack(2 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        // Extruder Hatch III
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_Bus_ZPM.get(1),
                ItemList.Sensor_ZPM.get(1),
                ItemList.Robot_Arm_ZPM.get(1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 4),
                new ItemStack(Blocks.chest),
                ItemList.Shape_Empty.get(24))
            .circuit(17)
            .itemOutputs(GregtechItemList.Hatch_Extrusion_III.get(1))
            .fluidInputs(MaterialsAlloy.PIKYONIUM.getFluidStack(2 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        // Extruder Hatch IV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_Bus_UV.get(1),
                ItemList.Sensor_UV.get(1),
                ItemList.Robot_Arm_UV.get(1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UHV, 4),
                new ItemStack(Blocks.chest),
                ItemList.Shape_Empty.get(24))
            .circuit(17)
            .itemOutputs(GregtechItemList.Hatch_Extrusion_IV.get(1))
            .fluidInputs(MaterialsElements.STANDALONE.ADVANCED_NITINOL.getFluidStack(2 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(assemblerRecipes);
    }

    private static void chiselBuses() {
        // Chisel Bus I
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_Bus_EV.get(1),
                ItemList.Sensor_LV.get(1),
                ItemList.Robot_Arm_LV.get(2),
                GTOreDictUnificator.get(OrePrefixes.bolt, Materials.Aluminium, 16),
                new ItemStack(Blocks.chest))
            .circuit(17)
            .itemOutputs(GregtechItemList.ChiselBus_LV.get(1))
            .fluidInputs(MaterialsAlloy.SILICON_CARBIDE.getFluidStack(2 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        // Chisel Bus II
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_Bus_IV.get(1),
                ItemList.Sensor_MV.get(1),
                ItemList.Robot_Arm_MV.get(2),
                MaterialsElements.STANDALONE.BLACK_METAL.getBolt(16),
                new ItemStack(Blocks.chest))
            .circuit(17)
            .itemOutputs(GregtechItemList.ChiselBus_MV.get(1))
            .fluidInputs(MaterialsAlloy.BLOODSTEEL.getFluidStack(2 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // Chisel Bus III
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_Bus_LuV.get(1),
                ItemList.Sensor_HV.get(1),
                ItemList.Robot_Arm_HV.get(2),
                GTOreDictUnificator.get(OrePrefixes.bolt, Materials.Titanium, 16),
                new ItemStack(Blocks.chest))
            .circuit(17)
            .itemOutputs(GregtechItemList.ChiselBus_HV.get(1))
            .fluidInputs(MaterialsAlloy.TANTALUM_CARBIDE.getFluidStack(2 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
    }

    private static void fakeMachineCasingCovers() {
        for (int i = 0; i < ItemList.MACHINE_CASINGS.length; i++) {
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.MACHINE_CASINGS[i].get(1))
                .circuit(i)
                .itemOutputs(new ItemStack(CoverManager.Cover_Gt_Machine_Casing, 7, i))
                .duration(i * 5 * SECONDS)
                .eut(GTValues.VP[i])
                .addTo(cutterRecipes);
        }
    }

    private static void autoChisels() {
        // Basic Auto-Chisel
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_LV.get(1),
                MaterialsAlloy.TUMBAGA.getPlate(4),
                ItemList.Electric_Motor_LV.get(2),
                ItemList.Conveyor_Module_LV.get(2),
                ItemList.Robot_Arm_LV.get(1))
            .circuit(11)
            .itemOutputs(GregtechItemList.GT_Chisel_LV.get(1))
            .fluidInputs(MaterialsAlloy.TUMBAGA.getFluidStack(4 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        // Advanced Auto-Chisel
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_MV.get(1),
                MaterialsAlloy.EGLIN_STEEL.getPlate(4),
                ItemList.Electric_Motor_MV.get(2),
                ItemList.Conveyor_Module_MV.get(2),
                ItemList.Robot_Arm_MV.get(1))
            .circuit(12)
            .itemOutputs(GregtechItemList.GT_Chisel_MV.get(1))
            .fluidInputs(MaterialsAlloy.EGLIN_STEEL.getFluidStack(4 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        // Precision Auto-Chisel
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_HV.get(1),
                MaterialsAlloy.TANTALUM_CARBIDE.getPlate(4),
                ItemList.Electric_Motor_HV.get(2),
                ItemList.Conveyor_Module_HV.get(2),
                ItemList.Robot_Arm_HV.get(1))
            .circuit(13)
            .itemOutputs(GregtechItemList.GT_Chisel_HV.get(1))
            .fluidInputs(MaterialsAlloy.TANTALUM_CARBIDE.getFluidStack(4 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
    }

    private static void semifluidGenerators() {
        // LV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Generator_SemiFluid_LV.get(1L),
            BITS,
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_LV, 'P', ItemList.Electric_Piston_LV, 'E',
                ItemList.Electric_Motor_LV, 'C', OrePrefixes.circuit.get(Materials.LV), 'W',
                OrePrefixes.cableGt01.get(Materials.Tin), 'G', MaterialsAlloy.TUMBAGA.getGear(2) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_LV.get(1),
                ItemList.Electric_Motor_LV.get(2),
                ItemList.Electric_Piston_LV.get(2),
                GTOreDictUnificator.get(OrePrefixes.cableGt01, Materials.Tin, 1L),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LV, 1L),
                MaterialsAlloy.TUMBAGA.getGear(2))
            .circuit(14)
            .itemOutputs(GregtechItemList.Generator_SemiFluid_LV.get(1))
            .fluidInputs(Materials.Polyethylene.getMolten(1 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        // MV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Generator_SemiFluid_MV.get(1L),
            BITS,
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_MV, 'P', ItemList.Electric_Piston_MV, 'E',
                ItemList.Electric_Motor_MV, 'C', OrePrefixes.circuit.get(Materials.MV), 'W',
                OrePrefixes.cableGt01.get(Materials.AnnealedCopper), 'G', MaterialsAlloy.EGLIN_STEEL.getGear(2) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_MV.get(1),
                ItemList.Electric_Motor_MV.get(2),
                ItemList.Electric_Piston_MV.get(2),
                GTOreDictUnificator.get(OrePrefixes.cableGt01, Materials.AnnealedCopper, 1L),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.MV, 1L),
                MaterialsAlloy.EGLIN_STEEL.getGear(2))
            .circuit(14)
            .itemOutputs(GregtechItemList.Generator_SemiFluid_MV.get(1))
            .fluidInputs(Materials.Polyethylene.getMolten(1 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        // HV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Generator_SemiFluid_HV.get(1L),
            BITS,
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_HV, 'P', ItemList.Electric_Piston_HV, 'E',
                ItemList.Electric_Motor_HV, 'C', OrePrefixes.circuit.get(Materials.HV), 'W',
                OrePrefixes.cableGt01.get(Materials.Gold), 'G',
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Chrome, 1) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_HV.get(1),
                ItemList.Electric_Motor_HV.get(2),
                ItemList.Electric_Piston_HV.get(2),
                GTOreDictUnificator.get(OrePrefixes.cableGt01, Materials.Gold, 1L),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 1L),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Chrome, 2))
            .circuit(14)
            .itemOutputs(GregtechItemList.Generator_SemiFluid_HV.get(1))
            .fluidInputs(Materials.Polyethylene.getMolten(1 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // EV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Generator_SemiFluid_EV.get(1L),
            BITS,
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_EV, 'P', ItemList.Electric_Piston_EV, 'E',
                ItemList.Electric_Motor_EV, 'C', OrePrefixes.circuit.get(Materials.EV), 'W',
                OrePrefixes.cableGt01.get(Materials.Titanium), 'G', MaterialsAlloy.INCOLOY_DS.getGear(1) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_EV.get(1),
                ItemList.Electric_Motor_EV.get(2),
                ItemList.Electric_Piston_EV.get(2),
                GTOreDictUnificator.get(OrePrefixes.cableGt01, Materials.Titanium, 1L),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.EV, 1L),
                MaterialsAlloy.INCOLOY_DS.getGear(2))
            .circuit(14)
            .itemOutputs(GregtechItemList.Generator_SemiFluid_EV.get(1))
            .fluidInputs(Materials.Polyethylene.getMolten(1 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // IV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Generator_SemiFluid_IV.get(1L),
            BITS,
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_IV, 'P', ItemList.Electric_Piston_IV, 'E',
                ItemList.Electric_Motor_IV, 'C', OrePrefixes.circuit.get(Materials.IV), 'W',
                OrePrefixes.cableGt01.get(Materials.Tungsten), 'G', MaterialsAlloy.NITINOL_60.getGear(1) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_IV.get(1),
                ItemList.Electric_Motor_IV.get(2),
                ItemList.Electric_Piston_IV.get(2),
                GTOreDictUnificator.get(OrePrefixes.cableGt01, Materials.Tungsten, 1L),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 1L),
                MaterialsAlloy.NITINOL_60.getGear(2))
            .circuit(14)
            .itemOutputs(GregtechItemList.Generator_SemiFluid_IV.get(1))
            .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(1 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
    }

    private static void simpleWashers() {
        // LV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_LV.get(1),
                MaterialsAlloy.TUMBAGA.getScrew(4),
                MaterialsAlloy.POTIN.getPlate(2),
                MaterialsAlloy.TUMBAGA.getRod(1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LV, 1))
            .itemOutputs(GregtechItemList.SimpleDustWasher_LV.get(1))
            .fluidInputs(MaterialsAlloy.TUMBAGA.getFluidStack(1 * INGOTS))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        // MV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_MV.get(1),
                MaterialsAlloy.EGLIN_STEEL.getScrew(8),
                MaterialsAlloy.TUMBAGA.getPlate(4),
                MaterialsAlloy.EGLIN_STEEL.getRod(2),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.MV, 1))
            .itemOutputs(GregtechItemList.SimpleDustWasher_MV.get(1))
            .fluidInputs(MaterialsAlloy.EGLIN_STEEL.getFluidStack(2 * INGOTS))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        // HV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_HV.get(1),
                MaterialsAlloy.TANTALUM_CARBIDE.getScrew(12),
                MaterialsAlloy.EGLIN_STEEL.getPlate(6),
                MaterialsAlloy.TANTALUM_CARBIDE.getRod(3),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 1))
            .itemOutputs(GregtechItemList.SimpleDustWasher_HV.get(1))
            .fluidInputs(MaterialsAlloy.TANTALUM_CARBIDE.getFluidStack(3 * INGOTS))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // EV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_EV.get(1),
                MaterialsAlloy.INCOLOY_DS.getScrew(16),
                MaterialsAlloy.TANTALUM_CARBIDE.getPlate(8),
                MaterialsAlloy.INCOLOY_DS.getRod(4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.EV, 1))
            .itemOutputs(GregtechItemList.SimpleDustWasher_EV.get(1))
            .fluidInputs(MaterialsAlloy.INCOLOY_DS.getFluidStack(4 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // IV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_IV.get(1),
                MaterialsAlloy.INCONEL_625.getScrew(20),
                MaterialsAlloy.INCOLOY_DS.getPlate(10),
                MaterialsAlloy.INCONEL_625.getRod(5),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 1))
            .itemOutputs(GregtechItemList.SimpleDustWasher_IV.get(1))
            .fluidInputs(MaterialsAlloy.INCONEL_625.getFluidStack(5 * INGOTS))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // LuV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_LuV.get(1),
                MaterialsAlloy.ZERON_100.getScrew(24),
                MaterialsAlloy.INCONEL_625.getPlate(12),
                MaterialsAlloy.ZERON_100.getRod(6),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 1))
            .itemOutputs(GregtechItemList.SimpleDustWasher_LuV.get(1))
            .fluidInputs(MaterialsAlloy.ZERON_100.getFluidStack(6 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        // ZPM
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_ZPM.get(1),
                MaterialsAlloy.PIKYONIUM.getScrew(28),
                MaterialsAlloy.ZERON_100.getPlate(14),
                MaterialsAlloy.PIKYONIUM.getRod(7),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 1))
            .itemOutputs(GregtechItemList.SimpleDustWasher_ZPM.get(1))
            .fluidInputs(MaterialsAlloy.PIKYONIUM.getFluidStack(7 * INGOTS))
            .duration(35 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        // UV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_UV.get(1),
                MaterialsAlloy.TITANSTEEL.getScrew(32),
                MaterialsAlloy.PIKYONIUM.getPlate(16),
                MaterialsAlloy.TITANSTEEL.getRod(8),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 1))
            .itemOutputs(GregtechItemList.SimpleDustWasher_UV.get(1))
            .fluidInputs(MaterialsAlloy.TITANSTEEL.getFluidStack(8 * INGOTS))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(assemblerRecipes);
    }

    private static void airFilters() {
        if (!GTMod.proxy.mPollution) return;

        // Air Filter [Tier 1]
        GTModHandler.addCraftingRecipe(
            GregtechItemList.AirFilter_Tier1.get(1),
            new Object[] { "PPP", "DDD", "PPP", 'P', OrePrefixes.plate.get(Materials.Carbon), 'D',
                OrePrefixes.dust.get(Materials.Carbon) });

        // Air Filter [Tier 2]
        GTModHandler.addCraftingRecipe(
            GregtechItemList.AirFilter_Tier2.get(1),
            new Object[] { "PPP", "CDC", "PPP", 'P', OrePrefixes.plate.get(Materials.Carbon), 'C',
                "cellLithiumPeroxide", 'D', OrePrefixes.dust.get(Materials.Carbon) });

        // Pollution Detection Device
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Pollution_Detector.get(1),
            new Object[] { "PSP", "PMP", "CHC", 'P', OrePrefixes.plate.get(Materials.Steel), 'S', ItemList.Sensor_LV,
                'M', ItemList.Electric_Motor_LV, 'C', "circuitBasic", 'H', ItemList.Hull_LV });

        // Pollution Cleaners/Scrubbers
        // LV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Pollution_Cleaner_LV.get(1),
            new Object[] { "PFP", "PMP", "CHC", 'P', GregtechItemList.AirFilter_Tier1, 'F',
                OrePrefixes.plate.get(Materials.Tin), 'M', ItemList.Electric_Motor_LV, 'C', "circuitBasic", 'H',
                ItemList.Hull_LV });

        // MV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Pollution_Cleaner_MV.get(1),
            new Object[] { "PFP", "PMP", "CHC", 'P', GregtechItemList.AirFilter_Tier1, 'F',
                OrePrefixes.plate.get(Materials.Copper), 'M', ItemList.Electric_Motor_MV, 'C', "circuitGood", 'H',
                ItemList.Hull_MV });

        // HV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Pollution_Cleaner_HV.get(1),
            new Object[] { "PFP", "PMP", "CHC", 'P', GregtechItemList.AirFilter_Tier1, 'F',
                OrePrefixes.plate.get(Materials.Bronze), 'M', ItemList.Electric_Motor_HV, 'C', "circuitAdvanced", 'H',
                ItemList.Hull_HV });

        // EV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Pollution_Cleaner_EV.get(1),
            new Object[] { "PFP", "PMP", "CHC", 'P', GregtechItemList.AirFilter_Tier1, 'F',
                OrePrefixes.plate.get(Materials.Iron), 'M', ItemList.Electric_Motor_EV, 'C', "circuitData", 'H',
                ItemList.Hull_EV });

        // IV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Pollution_Cleaner_IV.get(1),
            new Object[] { "PFP", "PMP", "CHC", 'P', GregtechItemList.AirFilter_Tier2, 'F',
                OrePrefixes.plate.get(Materials.Steel), 'M', ItemList.Electric_Motor_IV, 'C', "circuitElite", 'H',
                ItemList.Hull_IV });

        // LuV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Pollution_Cleaner_LuV.get(1),
            new Object[] { "PFP", "PMP", "CHC", 'P', GregtechItemList.AirFilter_Tier2, 'F',
                OrePrefixes.plate.get(Materials.Redstone), 'M', ItemList.Electric_Motor_LuV, 'C', "circuitMaster", 'H',
                ItemList.Hull_LuV });

        // ZPM
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Pollution_Cleaner_ZPM.get(1),
            new Object[] { "PFP", "PMP", "CHC", 'P', GregtechItemList.AirFilter_Tier2, 'F',
                OrePrefixes.plate.get(Materials.Aluminium), 'M', ItemList.Electric_Motor_ZPM, 'C', "circuitUltimate",
                'H', ItemList.Hull_ZPM });

        // UV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Pollution_Cleaner_UV.get(1),
            new Object[] { "PFP", "PMP", "CHC", 'P', GregtechItemList.AirFilter_Tier2, 'F',
                OrePrefixes.plate.get(Materials.DarkSteel), 'M', ItemList.Electric_Motor_UV, 'C',
                "circuitSuperconductor", 'H', ItemList.Hull_UV });

        // UHV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Pollution_Cleaner_MAX.get(1),
            new Object[] { "PFP", "PMP", "CHC", 'P', GregtechItemList.AirFilter_Tier2, 'F',
                MaterialsAlloy.ZERON_100.getPlate(1), 'M', ItemList.Electric_Motor_UHV, 'C', "circuitInfinite", 'H',
                ItemList.Hull_MAX });
    }

    private static void tanks() {
        // Allows clearing stored fluids
        GTModHandler.addShapelessCraftingRecipe(
            GregtechItemList.GTFluidTank_ULV.get(1),
            new Object[] { GregtechItemList.GTFluidTank_ULV.get(1) });
        GTModHandler.addShapelessCraftingRecipe(
            GregtechItemList.GTFluidTank_LV.get(1),
            new Object[] { GregtechItemList.GTFluidTank_LV.get(1) });
        GTModHandler.addShapelessCraftingRecipe(
            GregtechItemList.GTFluidTank_MV.get(1),
            new Object[] { GregtechItemList.GTFluidTank_MV.get(1) });
        GTModHandler.addShapelessCraftingRecipe(
            GregtechItemList.GTFluidTank_HV.get(1),
            new Object[] { GregtechItemList.GTFluidTank_HV.get(1) });

        // ULV Fluid Tank
        GTModHandler.addCraftingRecipe(
            GregtechItemList.GTFluidTank_ULV.get(1),
            new Object[] { "TST", "IPI", "IBI", 'T', OrePrefixes.plate.get(Materials.Tin), 'S',
                OrePrefixes.plate.get(Materials.Steel), 'I', OrePrefixes.plate.get(Materials.Iron), 'P',
                OrePrefixes.pipeLarge.get(Materials.Clay), 'B', new ItemStack(Items.water_bucket) });

        // LV Fluid Tank
        GTModHandler.addCraftingRecipe(
            GregtechItemList.GTFluidTank_LV.get(1),
            new Object[] { "SIS", "BPB", "BUB", 'S', OrePrefixes.plate.get(Materials.Steel), 'I',
                OrePrefixes.plate.get(Materials.Iron), 'B', OrePrefixes.plate.get(Materials.Bronze), 'P',
                OrePrefixes.pipeHuge.get(Materials.Clay), 'U', ItemList.Electric_Pump_LV });

        // MV Fluid Tank
        GTModHandler.addCraftingRecipe(
            GregtechItemList.GTFluidTank_MV.get(1),
            new Object[] { "DBD", "SPS", "SUS", 'D', OrePrefixes.plate.get(Materials.DarkSteel), 'B',
                OrePrefixes.plate.get(Materials.Bronze), 'S', OrePrefixes.plate.get(Materials.Steel), 'P',
                OrePrefixes.pipeMedium.get(Materials.Bronze), 'U', ItemList.Electric_Pump_LV });

        // HV Fluid Tank
        GTModHandler.addCraftingRecipe(
            GregtechItemList.GTFluidTank_HV.get(1),
            new Object[] { "CAC", "DPD", "CUC", 'C', "circuitPrimitive", 'A',
                OrePrefixes.plate.get(Materials.Aluminium), 'D', OrePrefixes.plate.get(Materials.DarkSteel), 'P',
                OrePrefixes.pipeMedium.get(Materials.Steel), 'U', ItemList.Electric_Pump_MV });
    }
}
