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

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.Circuits;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
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
                MaterialLibAPI.getStack(Materials.EglinSteel, Shapes.plate, 4),
                Circuits.MV.get(2))
            .itemOutputs(GregtechItemList.Charger_LV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.SiliconCarbide, 4 * INGOTS))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        // MV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_MV.get(1),
                GregtechItemList.TransmissionComponent_MV.get(2),
                ItemList.Field_Generator_MV.get(1),
                MaterialLibAPI.getStack(Materials.TantalumCarbide, Shapes.plate, 4),
                Circuits.HV.get(2))
            .itemOutputs(GregtechItemList.Charger_MV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.BloodSteel, 6 * INGOTS))
            .duration(67 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        // HV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_HV.get(1),
                GregtechItemList.TransmissionComponent_HV.get(2),
                ItemList.Field_Generator_HV.get(1),
                MaterialLibAPI.getStack(Materials.IncoloyDS, Shapes.plate, 4),
                Circuits.EV.get(2))
            .itemOutputs(GregtechItemList.Charger_HV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.TantalumCarbide, 8 * INGOTS))
            .duration(90 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // EV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_EV.get(1),
                GregtechItemList.TransmissionComponent_EV.get(2),
                ItemList.Field_Generator_EV.get(1),
                MaterialLibAPI.getStack(Materials.Inconel625, Shapes.plate, 4),
                Circuits.IV.get(2))
            .itemOutputs(GregtechItemList.Charger_EV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Inconel792, 10 * INGOTS))
            .duration(112 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // IV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_IV.get(1),
                GregtechItemList.TransmissionComponent_IV.get(2),
                ItemList.Field_Generator_IV.get(1),
                MaterialLibAPI.getStack(Materials.Zeron100, Shapes.plate, 4),
                Circuits.LuV.get(2))
            .itemOutputs(GregtechItemList.Charger_IV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Arcanite, 12 * INGOTS))
            .duration(135 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // LuV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_LuV.get(1),
                GregtechItemList.TransmissionComponent_LuV.get(2),
                ItemList.Field_Generator_LuV.get(1),
                MaterialLibAPI.getStack(Materials.Pikyonium64B, Shapes.plate, 4),
                Circuits.ZPM.get(2))
            .itemOutputs(GregtechItemList.Charger_LuV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.LafiumCompound, 14 * INGOTS))
            .duration(157 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        // ZPM
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_ZPM.get(1),
                GregtechItemList.TransmissionComponent_ZPM.get(2),
                ItemList.Field_Generator_ZPM.get(1),
                MaterialLibAPI.getStack(Materials.AdvancedNitinol, Shapes.plate, 4),
                Circuits.UV.get(2))
            .itemOutputs(GregtechItemList.Charger_ZPM.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.CinobiteA243, 16 * INGOTS))
            .duration(180 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        // UV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_UV.get(1),
                GregtechItemList.TransmissionComponent_UV.get(2),
                ItemList.Field_Generator_UV.get(1),
                MaterialLibAPI.getStack(Materials.AbyssalAlloy, Shapes.plate, 4),
                Circuits.UHV.get(2))
            .itemOutputs(GregtechItemList.Charger_UV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Titansteel, 18 * INGOTS))
            .duration(202 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_UV)
            .addTo(assemblerRecipes);

        // UHV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_MAX.get(1),
                GregtechItemList.TransmissionComponent_UHV.get(2),
                ItemList.Field_Generator_UHV.get(1),
                MaterialLibAPI.getStack(Materials.Quantum, Shapes.plate, 4),
                Circuits.UEV.get(2))
            .itemOutputs(GregtechItemList.Charger_UHV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Octiron, 20 * INGOTS))
            .duration(225 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);
    }

    private static void energyCores() {
        // ULV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_LV.get(1),
                MaterialLibAPI.getStack(Materials.Tumbaga, Shapes.plate, 4),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Tin, 2),
                Circuits.LV.get(2),
                MaterialLibAPI.getStack(Materials.Tumbaga, Shapes.screw, 6),
                MaterialLibAPI.getStack(Materials.Lead, Shapes.bolt, (int) (12)))
            .itemOutputs(GregtechItemList.Energy_Core_ULV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Potin, 4 * INGOTS))
            .duration(22 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);

        // LV
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Energy_Core_ULV.get(1),
                MaterialLibAPI.getStack(Materials.EglinSteel, Shapes.plate, 4),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Cobalt, 2),
                Circuits.MV.get(2),
                MaterialLibAPI.getStack(Materials.EglinSteel, Shapes.screw, 6),
                MaterialLibAPI.getStack(Materials.Aluminium, Shapes.bolt, (int) (12)))
            .itemOutputs(GregtechItemList.Energy_Core_LV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Tumbaga, 8 * INGOTS))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        // MV
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Energy_Core_LV.get(1),
                MaterialLibAPI.getStack(Materials.TantalumCarbide, Shapes.plate, 4),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.AnnealedCopper, 2),
                Circuits.HV.get(2),
                MaterialLibAPI.getStack(Materials.TantalumCarbide, Shapes.screw, 6),
                MaterialLibAPI.getStack(Materials.BlackMetal, Shapes.bolt, 12))
            .itemOutputs(GregtechItemList.Energy_Core_MV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.EglinSteel, 12 * INGOTS))
            .duration(67 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        // HV
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Energy_Core_MV.get(1),
                MaterialLibAPI.getStack(Materials.IncoloyDS, Shapes.plate, 4),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Gold, 2),
                Circuits.EV.get(2),
                MaterialLibAPI.getStack(Materials.IncoloyDS, Shapes.screw, 6),
                MaterialLibAPI.getStack(Materials.Titanium, Shapes.bolt, (int) (12)))
            .itemOutputs(GregtechItemList.Energy_Core_HV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.TantalumCarbide, 16 * INGOTS))
            .duration(90 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // EV
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Energy_Core_HV.get(1),
                MaterialLibAPI.getStack(Materials.Inconel625, Shapes.plate, 4),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Titanium, 2),
                Circuits.IV.get(2),
                MaterialLibAPI.getStack(Materials.Inconel625, Shapes.screw, 6),
                MaterialLibAPI.getStack(Materials.HastelloyN, Shapes.bolt, 12))
            .itemOutputs(GregtechItemList.Energy_Core_EV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.IncoloyDS, 20 * INGOTS))
            .duration(112 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // IV
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Energy_Core_EV.get(1),
                MaterialLibAPI.getStack(Materials.Zeron100, Shapes.plate, 4),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Nichrome, 2),
                Circuits.LuV.get(2),
                MaterialLibAPI.getStack(Materials.Zeron100, Shapes.screw, 6),
                MaterialLibAPI.getStack(Materials.EnergyCrystal, Shapes.bolt, 12))
            .itemOutputs(GregtechItemList.Energy_Core_IV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Inconel625, 24 * INGOTS))
            .duration(135 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // LuV
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Energy_Core_IV.get(1),
                MaterialLibAPI.getStack(Materials.Pikyonium64B, Shapes.plate, 4),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Platinum, 2),
                Circuits.ZPM.get(2),
                MaterialLibAPI.getStack(Materials.Pikyonium64B, Shapes.screw, 6),
                MaterialLibAPI.getStack(Materials.TriniumNaquadahCarbonite, Shapes.bolt, 12))
            .itemOutputs(GregtechItemList.Energy_Core_LuV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Zeron100, 28 * INGOTS))
            .duration(157 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        // ZPM
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Energy_Core_LuV.get(1),
                MaterialLibAPI.getStack(Materials.AdvancedNitinol, Shapes.plate, 4),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.YttriumBariumCuprate, 2),
                Circuits.UV.get(2),
                MaterialLibAPI.getStack(Materials.Titansteel, Shapes.screw, 6),
                MaterialLibAPI.getStack(Materials.ArceusAlloy2B, Shapes.bolt, 12))
            .itemOutputs(GregtechItemList.Energy_Core_ZPM.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Pikyonium64B, 32 * INGOTS))
            .duration(180 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        // UV
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Energy_Core_ZPM.get(1),
                MaterialLibAPI.getStack(Materials.AbyssalAlloy, Shapes.plate, 4),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Naquadah, 2),
                Circuits.UHV.get(2),
                MaterialLibAPI.getStack(Materials.AbyssalAlloy, Shapes.screw, 6),
                MaterialLibAPI.getStack(Materials.Titansteel, Shapes.bolt, 12))
            .itemOutputs(GregtechItemList.Energy_Core_UV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.AdvancedNitinol, 36 * INGOTS))
            .duration(202 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_UV)
            .addTo(assemblerRecipes);

        // UHV
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Energy_Core_UV.get(1),
                MaterialLibAPI.getStack(Materials.Quantum, Shapes.plate, 4),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Duranium, 2),
                Circuits.UEV.get(2),
                MaterialLibAPI.getStack(Materials.Quantum, Shapes.screw, 6),
                MaterialLibAPI.getStack(Materials.AstralTitanium, Shapes.bolt, 12))
            .itemOutputs(GregtechItemList.Energy_Core_UHV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.AbyssalAlloy, 40 * INGOTS))
            .duration(225 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);
    }

    private static void energyBuffers() {
        // ULV
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Energy_Core_ULV.get(4),
                MaterialLibAPI.getStack(Materials.Tumbaga, Shapes.plate, 8),
                GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.Tin, 4),
                Circuits.ULV.get(4),
                MaterialLibAPI.getStack(Materials.SiliconCarbide, Shapes.stickLong, 4),
                MaterialLibAPI.getStack(Materials.Potin, Shapes.gearGt, 5))
            .itemOutputs(GregtechItemList.Energy_Buffer_1by1_ULV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Tumbaga, 16 * INGOTS))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);

        // LV
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Energy_Core_LV.get(4),
                MaterialLibAPI.getStack(Materials.EglinSteel, Shapes.plate, 8),
                GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.Cobalt, 4),
                Circuits.LV.get(4),
                MaterialLibAPI.getStack(Materials.BloodSteel, Shapes.stickLong, 4),
                MaterialLibAPI.getStack(Materials.Tumbaga, Shapes.gearGt, 5))
            .itemOutputs(GregtechItemList.Energy_Buffer_1by1_LV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.EglinSteel, 32 * INGOTS))
            .duration(90 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        // MV
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Energy_Core_MV.get(4),
                MaterialLibAPI.getStack(Materials.TantalumCarbide, Shapes.plate, 8),
                GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.AnnealedCopper, 4),
                Circuits.MV.get(4),
                MaterialLibAPI.getStack(Materials.TantalumCarbide, Shapes.stickLong, 4),
                MaterialLibAPI.getStack(Materials.EglinSteel, Shapes.gearGt, 5))
            .itemOutputs(GregtechItemList.Energy_Buffer_1by1_MV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.TantalumCarbide, 48 * INGOTS))
            .duration(135 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        // HV
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Energy_Core_HV.get(4),
                MaterialLibAPI.getStack(Materials.IncoloyDS, Shapes.plate, 8),
                GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.Gold, 4),
                Circuits.HV.get(4),
                MaterialLibAPI.getStack(Materials.Inconel792, Shapes.stickLong, 4),
                MaterialLibAPI.getStack(Materials.TantalumCarbide, Shapes.gearGt, 5))
            .itemOutputs(GregtechItemList.Energy_Buffer_1by1_HV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.IncoloyDS, 64 * INGOTS))
            .duration(180 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // EV
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Energy_Core_EV.get(4),
                MaterialLibAPI.getStack(Materials.Inconel625, Shapes.plate, 8),
                GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.Titanium, 4),
                Circuits.EV.get(4),
                MaterialLibAPI.getStack(Materials.Arcanite, Shapes.stickLong, 4),
                MaterialLibAPI.getStack(Materials.IncoloyDS, Shapes.gearGt, 5))
            .itemOutputs(GregtechItemList.Energy_Buffer_1by1_EV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Inconel625, 80 * INGOTS))
            .duration(225 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // IV
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Energy_Core_IV.get(4),
                MaterialLibAPI.getStack(Materials.Zeron100, Shapes.plate, 8),
                GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.Nichrome, 4),
                Circuits.IV.get(4),
                MaterialLibAPI.getStack(Materials.LafiumCompound, Shapes.stickLong, 4),
                MaterialLibAPI.getStack(Materials.Inconel625, Shapes.gearGt, 5))
            .itemOutputs(GregtechItemList.Energy_Buffer_1by1_IV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Zeron100, 96 * INGOTS))
            .duration(270 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // LuV
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Energy_Core_LuV.get(4),
                MaterialLibAPI.getStack(Materials.Pikyonium64B, Shapes.plate, 8),
                GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.Platinum, 4),
                Circuits.LuV.get(4),
                MaterialLibAPI.getStack(Materials.CinobiteA243, Shapes.stickLong, 4),
                MaterialLibAPI.getStack(Materials.Zeron100, Shapes.gearGt, 5))
            .itemOutputs(GregtechItemList.Energy_Buffer_1by1_LuV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Pikyonium64B, 112 * INGOTS))
            .duration(315 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        // ZPM
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Energy_Core_ZPM.get(4),
                MaterialLibAPI.getStack(Materials.AdvancedNitinol, Shapes.plate, 8),
                GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.YttriumBariumCuprate, 4),
                Circuits.ZPM.get(4),
                MaterialLibAPI.getStack(Materials.Titansteel, Shapes.stickLong, 4),
                MaterialLibAPI.getStack(Materials.Pikyonium64B, Shapes.gearGt, 5))
            .itemOutputs(GregtechItemList.Energy_Buffer_1by1_ZPM.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.AdvancedNitinol, 128 * INGOTS))
            .duration(360 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        // UV
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Energy_Core_UV.get(4),
                MaterialLibAPI.getStack(Materials.AbyssalAlloy, Shapes.plate, 8),
                GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.Naquadah, 4),
                Circuits.UV.get(4),
                MaterialLibAPI.getStack(Materials.Octiron, Shapes.stickLong, 4),
                MaterialLibAPI.getStack(Materials.Titansteel, Shapes.gearGt, 5))
            .itemOutputs(GregtechItemList.Energy_Buffer_1by1_UV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.AbyssalAlloy, 144 * INGOTS))
            .duration(405 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(assemblerRecipes);

        // UHV
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Energy_Core_UHV.get(4),
                MaterialLibAPI.getStack(Materials.Quantum, Shapes.plate, 8),
                GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.Duranium, 4),
                Circuits.UHV.get(4),
                MaterialLibAPI.getStack(Materials.CelestialTungsten, Shapes.stickLong, 4),
                MaterialLibAPI.getStack(Materials.AbyssalAlloy, Shapes.gearGt, 5))
            .itemOutputs(GregtechItemList.Energy_Buffer_1by1_MAX.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Quantum, 160 * INGOTS))
            .duration(450 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);
    }

    private static void integralCasings() {
        // Integral Encasement I (ULV)
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_ULV.get(1),
                MaterialLibAPI.getStack(Materials.Potin, Shapes.plate, 8),
                MaterialLibAPI.getStack(Materials.Potin, Shapes.gearGt, 2),
                GTOreDictUnificator.get(OrePrefixes.cableGt02, Materials.Tin, 4),
                Circuits.ULV.get(2))
            .circuit(20)
            .itemOutputs(GregtechItemList.GTPP_Casing_ULV.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Steel, FluidShapes.fluidMolten, (int) (2 * INGOTS)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);

        // Integral Encasement II (LV)
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_LV.get(1),
                MaterialLibAPI.getStack(Materials.Tumbaga, Shapes.plate, 8),
                MaterialLibAPI.getStack(Materials.Tumbaga, Shapes.gearGt, 2),
                GTOreDictUnificator.get(OrePrefixes.cableGt02, Materials.Cobalt, 4),
                Circuits.LV.get(2))
            .circuit(20)
            .itemOutputs(GregtechItemList.GTPP_Casing_LV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.SiliconCarbide, 4 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        // Integral Encasement III (MV)
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_MV.get(1),
                MaterialLibAPI.getStack(Materials.EglinSteel, Shapes.plate, 8),
                MaterialLibAPI.getStack(Materials.EglinSteel, Shapes.gearGt, 2),
                GTOreDictUnificator.get(OrePrefixes.cableGt02, Materials.AnnealedCopper, 4),
                Circuits.MV.get(2))
            .circuit(20)
            .itemOutputs(GregtechItemList.GTPP_Casing_MV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.BloodSteel, 6 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        // Integral Encasement IV (HV)
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_HV.get(1),
                MaterialLibAPI.getStack(Materials.TantalumCarbide, Shapes.plate, 8),
                MaterialLibAPI.getStack(Materials.TantalumCarbide, Shapes.gearGt, 2),
                GTOreDictUnificator.get(OrePrefixes.cableGt02, Materials.Gold, 4),
                Circuits.HV.get(2))
            .circuit(20)
            .itemOutputs(GregtechItemList.GTPP_Casing_HV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.TantalumCarbide, 8 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // Integral Encasement V (EV)
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_EV.get(1),
                MaterialLibAPI.getStack(Materials.IncoloyDS, Shapes.plate, 8),
                MaterialLibAPI.getStack(Materials.IncoloyDS, Shapes.gearGt, 2),
                GTOreDictUnificator.get(OrePrefixes.cableGt02, Materials.Titanium, 4),
                Circuits.EV.get(2))
            .circuit(20)
            .itemOutputs(GregtechItemList.GTPP_Casing_EV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Inconel792, 10 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // Integral Framework I (IV)
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_IV.get(1),
                MaterialLibAPI.getStack(Materials.Inconel625, Shapes.plate, 8),
                MaterialLibAPI.getStack(Materials.Inconel625, Shapes.gearGt, 2),
                GTOreDictUnificator.get(OrePrefixes.cableGt02, Materials.Nichrome, 4),
                Circuits.IV.get(2))
            .circuit(20)
            .itemOutputs(GregtechItemList.GTPP_Casing_IV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Arcanite, 12 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Integral Framework II (LuV)
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_LuV.get(1),
                MaterialLibAPI.getStack(Materials.Zeron100, Shapes.plate, 8),
                MaterialLibAPI.getStack(Materials.Zeron100, Shapes.gearGt, 2),
                GTOreDictUnificator.get(OrePrefixes.cableGt02, Materials.Platinum, 4),
                Circuits.LuV.get(2))
            .circuit(20)
            .itemOutputs(GregtechItemList.GTPP_Casing_LuV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.LafiumCompound, 14 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        // Integral Framework III (ZPM)
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_ZPM.get(1),
                MaterialLibAPI.getStack(Materials.Pikyonium64B, Shapes.plate, 8),
                MaterialLibAPI.getStack(Materials.Pikyonium64B, Shapes.gearGt, 2),
                GTOreDictUnificator.get(OrePrefixes.cableGt02, Materials.YttriumBariumCuprate, 4),
                Circuits.ZPM.get(2))
            .circuit(20)
            .itemOutputs(GregtechItemList.GTPP_Casing_ZPM.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.CinobiteA243, 16 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        // Integral Framework IV (UV)
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_UV.get(1),
                MaterialLibAPI.getStack(Materials.AdvancedNitinol, Shapes.plate, 8),
                MaterialLibAPI.getStack(Materials.Titansteel, Shapes.gearGt, 2),
                GTOreDictUnificator.get(OrePrefixes.cableGt02, Materials.Naquadah, 4),
                Circuits.UV.get(2))
            .circuit(20)
            .itemOutputs(GregtechItemList.GTPP_Casing_UV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Titansteel, 18 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(assemblerRecipes);

        // Integral Framework V (UHV)
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_MAX.get(1),
                MaterialLibAPI.getStack(Materials.AbyssalAlloy, Shapes.plate, 8),
                MaterialLibAPI.getStack(Materials.AbyssalAlloy, Shapes.gearGt, 2),
                GTOreDictUnificator.get(OrePrefixes.cableGt02, Materials.Duranium, 4),
                Circuits.UHV.get(2))
            .circuit(20)
            .itemOutputs(GregtechItemList.GTPP_Casing_UHV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Octiron, 20 * INGOTS))
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
                MaterialLibAPI.getStack(Materials.Tumbaga, Shapes.plate, 4))
            .circuit(19)
            .itemOutputs(GregtechItemList.Cover_Overflow_Valve_LV.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.SolderingAlloy, FluidShapes.fluidMolten, (int) (1 * INGOTS)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        // MV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Electric_Pump_MV.get(2),
                ItemList.Electric_Motor_MV.get(2),
                MaterialLibAPI.getStack(Materials.EglinSteel, Shapes.plate, 4))
            .circuit(19)
            .itemOutputs(GregtechItemList.Cover_Overflow_Valve_MV.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.SolderingAlloy, FluidShapes.fluidMolten, (int) (2 * INGOTS)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        // HV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Electric_Pump_HV.get(2),
                ItemList.Electric_Motor_HV.get(2),
                MaterialLibAPI.getStack(Materials.TantalumCarbide, Shapes.plate, 4))
            .circuit(19)
            .itemOutputs(GregtechItemList.Cover_Overflow_Valve_HV.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.SolderingAlloy, FluidShapes.fluidMolten, (int) (3 * INGOTS)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // EV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Electric_Pump_EV.get(2),
                ItemList.Electric_Motor_EV.get(2),
                MaterialLibAPI.getStack(Materials.IncoloyDS, Shapes.plate, 4))
            .circuit(19)
            .itemOutputs(GregtechItemList.Cover_Overflow_Valve_EV.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.SolderingAlloy, FluidShapes.fluidMolten, (int) (4 * INGOTS)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // IV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Electric_Pump_IV.get(2),
                ItemList.Electric_Motor_IV.get(2),
                MaterialLibAPI.getStack(Materials.Inconel625, Shapes.plate, 4))
            .circuit(19)
            .itemOutputs(GregtechItemList.Cover_Overflow_Valve_IV.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.SolderingAlloy, FluidShapes.fluidMolten, (int) (5 * INGOTS)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
    }

    private static void modulators() {
        // Modulator I
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Modulator_I.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "CPC", "PHP", "CPC", 'C', "circuitData", 'P',
                MaterialLibAPI.getStack(Materials.IncoloyDS, Shapes.plate, 1), 'H', ItemList.Casing_EV });

        // Modulator II
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Modulator_II.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "CPC", "PHP", "CPC", 'C', "circuitElite", 'P',
                MaterialLibAPI.getStack(Materials.Inconel625, Shapes.plate, 1), 'H', ItemList.Casing_IV });

        // Modulator III
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Modulator_III.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "CPC", "PHP", "CPC", 'C', "circuitMaster", 'P',
                MaterialLibAPI.getStack(Materials.Zeron100, Shapes.plate, 1), 'H', ItemList.Casing_LuV });

        // Modulator IV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Modulator_IV.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "CPC", "PHP", "CPC", 'C', "circuitUltimate", 'P',
                MaterialLibAPI.getStack(Materials.Pikyonium64B, Shapes.plate, 1), 'H', ItemList.Casing_ZPM });
    }

    private static void resonanceChambers() {
        // Resonance Chamber I
        GTModHandler.addCraftingRecipe(
            GregtechItemList.ResonanceChamber_I.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PFP", "FHF", "PFP", 'P',
                MaterialLibAPI.getStack(Materials.IncoloyDS, Shapes.plateDouble, 1), 'F', ItemList.Field_Generator_LV,
                'H', ItemList.Casing_EV });

        // Resonance Chamber II
        GTModHandler.addCraftingRecipe(
            GregtechItemList.ResonanceChamber_II.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PFP", "FHF", "PFP", 'P',
                MaterialLibAPI.getStack(Materials.Inconel625, Shapes.plateDouble, 1), 'F', ItemList.Field_Generator_MV,
                'H', ItemList.Casing_IV });

        // Resonance Chamber III
        GTModHandler.addCraftingRecipe(
            GregtechItemList.ResonanceChamber_III.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PFP", "FHF", "PFP", 'P', MaterialLibAPI.getStack(Materials.Zeron100, Shapes.plateDouble, 1),
                'F', ItemList.Field_Generator_HV, 'H', ItemList.Casing_LuV });

        // Resonance Chamber IV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.ResonanceChamber_IV.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PFP", "FHF", "PFP", 'P',
                MaterialLibAPI.getStack(Materials.Pikyonium64B, Shapes.plateDouble, 1), 'F',
                ItemList.Field_Generator_EV, 'H', ItemList.Casing_ZPM });
    }

    private static void autoWorkbenches() {
        // LV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.GT4_Electric_Auto_Workbench_LV.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PCP", "IHI", "PRP", 'P', OrePrefixes.plate.ingredient(Materials.Steel), 'C',
                new ItemStack(Blocks.crafting_table), 'I', Circuits.LV.getIngredient(), 'H', ItemList.Hull_LV, 'R',
                ItemList.Robot_Arm_LV });

        // MV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.GT4_Electric_Auto_Workbench_MV.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PCP", "IHI", "PRP", 'P', OrePrefixes.plate.ingredient(Materials.Aluminium), 'C',
                new ItemStack(Blocks.crafting_table), 'I', Circuits.MV.getIngredient(), 'H', ItemList.Hull_MV, 'R',
                ItemList.Robot_Arm_MV });

        // HV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.GT4_Electric_Auto_Workbench_HV.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PCP", "IHI", "PRP", 'P', OrePrefixes.plate.ingredient(Materials.StainlessSteel), 'C',
                new ItemStack(Blocks.crafting_table), 'I', Circuits.HV.getIngredient(), 'H', ItemList.Hull_HV, 'R',
                ItemList.Robot_Arm_HV });

        // EV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.GT4_Electric_Auto_Workbench_EV.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PCP", "IHI", "PRP", 'P', OrePrefixes.plate.ingredient(Materials.Titanium), 'C',
                new ItemStack(Blocks.crafting_table), 'I', Circuits.EV.getIngredient(), 'H', ItemList.Hull_EV, 'R',
                ItemList.Robot_Arm_EV });

        // IV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.GT4_Electric_Auto_Workbench_IV.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PCP", "IHI", "PRP", 'P', OrePrefixes.plate.ingredient(Materials.TungstenSteel), 'C',
                new ItemStack(Blocks.crafting_table), 'I', Circuits.IV.getIngredient(), 'H', ItemList.Hull_IV, 'R',
                ItemList.Robot_Arm_IV });

        // LuV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.GT4_Electric_Auto_Workbench_LuV.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PCP", "IHI", "PRP", 'P', OrePrefixes.plate.ingredient(Materials.Chrome), 'C',
                new ItemStack(Blocks.crafting_table), 'I', Circuits.LuV.getIngredient(), 'H', ItemList.Hull_LuV, 'R',
                ItemList.Robot_Arm_LuV });

        // ZPM
        GTModHandler.addCraftingRecipe(
            GregtechItemList.GT4_Electric_Auto_Workbench_ZPM.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PCP", "IHI", "PRP", 'P', OrePrefixes.plate.ingredient(Materials.Iridium), 'C',
                new ItemStack(Blocks.crafting_table), 'I', Circuits.ZPM.getIngredient(), 'H', ItemList.Hull_ZPM, 'R',
                ItemList.Robot_Arm_ZPM });

        // UV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.GT4_Electric_Auto_Workbench_UV.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PCP", "IHI", "PRP", 'P', OrePrefixes.plate.ingredient(Materials.Osmium), 'C',
                new ItemStack(Blocks.crafting_table), 'I', Circuits.UV.getIngredient(), 'H', ItemList.Hull_UV, 'R',
                ItemList.Robot_Arm_UV });
    }

    private static void solidifierHatches() {
        // Solidifier Hatch I
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_IV.get(1),
                ItemList.Sensor_IV.get(1),
                ItemList.FluidRegulator_IV.get(1),
                Circuits.LuV.get(4),
                new ItemStack(Blocks.chest))
            .circuit(17)
            .itemOutputs(GregtechItemList.Hatch_Solidifier_I.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Inconel625, 2 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Solidifier Hatch II
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_LuV.get(1),
                ItemList.Sensor_LuV.get(1),
                ItemList.FluidRegulator_LuV.get(1),
                Circuits.ZPM.get(4),
                new ItemStack(Blocks.chest))
            .circuit(17)
            .itemOutputs(GregtechItemList.Hatch_Solidifier_II.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Zeron100, 2 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        // Solidifier Hatch III
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_ZPM.get(1),
                ItemList.Sensor_ZPM.get(1),
                ItemList.FluidRegulator_ZPM.get(1),
                Circuits.UV.get(4),
                new ItemStack(Blocks.chest))
            .circuit(17)
            .itemOutputs(GregtechItemList.Hatch_Solidifier_III.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Pikyonium64B, 2 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        // Solidifier Hatch IV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_UV.get(1),
                ItemList.Sensor_UV.get(1),
                ItemList.FluidRegulator_UV.get(1),
                Circuits.UHV.get(4),
                new ItemStack(Blocks.chest))
            .circuit(17)
            .itemOutputs(GregtechItemList.Hatch_Solidifier_IV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.AdvancedNitinol, 2 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(assemblerRecipes);
    }

    private static void extruderHatches() {
        // Extruder Hatch I
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_Bus_IV.get(1),
                ItemList.Robot_Arm_IV.get(1),
                new ItemStack(Blocks.chest),
                ItemList.Shape_Empty.get(24))
            .circuit(17)
            .itemOutputs(GregtechItemList.Hatch_Extrusion_I.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Inconel625, 2 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Extruder Hatch II
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_Bus_LuV.get(1),
                ItemList.Robot_Arm_LuV.get(1),
                new ItemStack(Blocks.chest),
                ItemList.Shape_Empty.get(24))
            .circuit(17)
            .itemOutputs(GregtechItemList.Hatch_Extrusion_II.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Zeron100, 2 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        // Extruder Hatch III
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_Bus_ZPM.get(1),
                ItemList.Robot_Arm_ZPM.get(1),
                new ItemStack(Blocks.chest),
                ItemList.Shape_Empty.get(24))
            .circuit(17)
            .itemOutputs(GregtechItemList.Hatch_Extrusion_III.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Pikyonium64B, 2 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        // Extruder Hatch IV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_Bus_UV.get(1),
                ItemList.Robot_Arm_UV.get(1),
                new ItemStack(Blocks.chest),
                ItemList.Shape_Empty.get(24))
            .circuit(17)
            .itemOutputs(GregtechItemList.Hatch_Extrusion_IV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.AdvancedNitinol, 2 * INGOTS))
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
                MaterialLibAPI.getStack(Materials.Aluminium, Shapes.bolt, (int) (16)),
                new ItemStack(Blocks.chest))
            .circuit(17)
            .itemOutputs(GregtechItemList.ChiselBus_LV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.SiliconCarbide, 2 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        // Chisel Bus II
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_Bus_IV.get(1),
                ItemList.Sensor_MV.get(1),
                ItemList.Robot_Arm_MV.get(2),
                MaterialLibAPI.getStack(Materials.BlackMetal, Shapes.bolt, 16),
                new ItemStack(Blocks.chest))
            .circuit(17)
            .itemOutputs(GregtechItemList.ChiselBus_MV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.BloodSteel, 2 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // Chisel Bus III
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_Bus_LuV.get(1),
                ItemList.Sensor_HV.get(1),
                ItemList.Robot_Arm_HV.get(2),
                MaterialLibAPI.getStack(Materials.Titanium, Shapes.bolt, (int) (16)),
                new ItemStack(Blocks.chest))
            .circuit(17)
            .itemOutputs(GregtechItemList.ChiselBus_HV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.TantalumCarbide, 2 * INGOTS))
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
                MaterialLibAPI.getStack(Materials.Tumbaga, Shapes.plate, 4),
                ItemList.Electric_Motor_LV.get(2),
                ItemList.Conveyor_Module_LV.get(2),
                ItemList.Robot_Arm_LV.get(1))
            .circuit(11)
            .itemOutputs(GregtechItemList.GT_Chisel_LV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Tumbaga, 4 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        // Advanced Auto-Chisel
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_MV.get(1),
                MaterialLibAPI.getStack(Materials.EglinSteel, Shapes.plate, 4),
                ItemList.Electric_Motor_MV.get(2),
                ItemList.Conveyor_Module_MV.get(2),
                ItemList.Robot_Arm_MV.get(1))
            .circuit(12)
            .itemOutputs(GregtechItemList.GT_Chisel_MV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.EglinSteel, 4 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        // Precision Auto-Chisel
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_HV.get(1),
                MaterialLibAPI.getStack(Materials.TantalumCarbide, Shapes.plate, 4),
                ItemList.Electric_Motor_HV.get(2),
                ItemList.Conveyor_Module_HV.get(2),
                ItemList.Robot_Arm_HV.get(1))
            .circuit(13)
            .itemOutputs(GregtechItemList.GT_Chisel_HV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.TantalumCarbide, 4 * INGOTS))
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
                ItemList.Electric_Motor_LV, 'C', Circuits.LV.getIngredient(), 'W',
                OrePrefixes.cableGt01.ingredient(Materials.Tin), 'G',
                MaterialLibAPI.getStack(Materials.Tumbaga, Shapes.gearGt, 2) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_LV.get(1),
                ItemList.Electric_Motor_LV.get(2),
                ItemList.Electric_Piston_LV.get(2),
                GTOreDictUnificator.get(OrePrefixes.cableGt01, Materials.Tin, 1L),
                Circuits.LV.get(1),
                MaterialLibAPI.getStack(Materials.Tumbaga, Shapes.gearGt, 2))
            .circuit(14)
            .itemOutputs(GregtechItemList.Generator_SemiFluid_LV.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Plastic, FluidShapes.fluidMolten, 1 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        // MV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Generator_SemiFluid_MV.get(1L),
            BITS,
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_MV, 'P', ItemList.Electric_Piston_MV, 'E',
                ItemList.Electric_Motor_MV, 'C', Circuits.MV.getIngredient(), 'W',
                OrePrefixes.cableGt01.ingredient(Materials.AnnealedCopper), 'G',
                MaterialLibAPI.getStack(Materials.EglinSteel, Shapes.gearGt, 2) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_MV.get(1),
                ItemList.Electric_Motor_MV.get(2),
                ItemList.Electric_Piston_MV.get(2),
                GTOreDictUnificator.get(OrePrefixes.cableGt01, Materials.AnnealedCopper, 1L),
                Circuits.MV.get(1),
                MaterialLibAPI.getStack(Materials.EglinSteel, Shapes.gearGt, 2))
            .circuit(14)
            .itemOutputs(GregtechItemList.Generator_SemiFluid_MV.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Plastic, FluidShapes.fluidMolten, 1 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        // HV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Generator_SemiFluid_HV.get(1L),
            BITS,
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_HV, 'P', ItemList.Electric_Piston_HV, 'E',
                ItemList.Electric_Motor_HV, 'C', Circuits.HV.getIngredient(), 'W',
                OrePrefixes.cableGt01.ingredient(Materials.Gold), 'G',
                MaterialLibAPI.getStack(Materials.Chrome, Shapes.gearGt, (int) (1)) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_HV.get(1),
                ItemList.Electric_Motor_HV.get(2),
                ItemList.Electric_Piston_HV.get(2),
                GTOreDictUnificator.get(OrePrefixes.cableGt01, Materials.Gold, 1L),
                Circuits.HV.get(1),
                MaterialLibAPI.getStack(Materials.Chrome, Shapes.gearGt, (int) (2)))
            .circuit(14)
            .itemOutputs(GregtechItemList.Generator_SemiFluid_HV.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Plastic, FluidShapes.fluidMolten, 1 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // EV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Generator_SemiFluid_EV.get(1L),
            BITS,
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_EV, 'P', ItemList.Electric_Piston_EV, 'E',
                ItemList.Electric_Motor_EV, 'C', Circuits.EV.getIngredient(), 'W',
                OrePrefixes.cableGt01.ingredient(Materials.Titanium), 'G',
                MaterialLibAPI.getStack(Materials.IncoloyDS, Shapes.gearGt, 1) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_EV.get(1),
                ItemList.Electric_Motor_EV.get(2),
                ItemList.Electric_Piston_EV.get(2),
                GTOreDictUnificator.get(OrePrefixes.cableGt01, Materials.Titanium, 1L),
                Circuits.EV.get(1),
                MaterialLibAPI.getStack(Materials.IncoloyDS, Shapes.gearGt, 2))
            .circuit(14)
            .itemOutputs(GregtechItemList.Generator_SemiFluid_EV.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Plastic, FluidShapes.fluidMolten, 1 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // IV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Generator_SemiFluid_IV.get(1L),
            BITS,
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_IV, 'P', ItemList.Electric_Piston_IV, 'E',
                ItemList.Electric_Motor_IV, 'C', Circuits.IV.getIngredient(), 'W',
                OrePrefixes.cableGt01.ingredient(Materials.Tungsten), 'G',
                MaterialLibAPI.getStack(Materials.Nitinol60, Shapes.gearGt, 1) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_IV.get(1),
                ItemList.Electric_Motor_IV.get(2),
                ItemList.Electric_Piston_IV.get(2),
                GTOreDictUnificator.get(OrePrefixes.cableGt01, Materials.Tungsten, 1L),
                Circuits.IV.get(1),
                MaterialLibAPI.getStack(Materials.Nitinol60, Shapes.gearGt, 2))
            .circuit(14)
            .itemOutputs(GregtechItemList.Generator_SemiFluid_IV.get(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Polytetrafluoroethylene, FluidShapes.fluidMolten, (int) (1 * INGOTS)))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
    }

    private static void simpleWashers() {
        // LV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_LV.get(1),
                MaterialLibAPI.getStack(Materials.Tumbaga, Shapes.screw, 4),
                MaterialLibAPI.getStack(Materials.Potin, Shapes.plate, 2),
                MaterialLibAPI.getStack(Materials.Tumbaga, Shapes.stick, 1),
                Circuits.LV.get(1))
            .itemOutputs(GregtechItemList.SimpleDustWasher_LV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Tumbaga, 1 * INGOTS))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        // MV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_MV.get(1),
                MaterialLibAPI.getStack(Materials.EglinSteel, Shapes.screw, 8),
                MaterialLibAPI.getStack(Materials.Tumbaga, Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials.EglinSteel, Shapes.stick, 2),
                Circuits.MV.get(1))
            .itemOutputs(GregtechItemList.SimpleDustWasher_MV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.EglinSteel, 2 * INGOTS))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        // HV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_HV.get(1),
                MaterialLibAPI.getStack(Materials.TantalumCarbide, Shapes.screw, 12),
                MaterialLibAPI.getStack(Materials.EglinSteel, Shapes.plate, 6),
                MaterialLibAPI.getStack(Materials.TantalumCarbide, Shapes.stick, 3),
                Circuits.HV.get(1))
            .itemOutputs(GregtechItemList.SimpleDustWasher_HV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.TantalumCarbide, 3 * INGOTS))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // EV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_EV.get(1),
                MaterialLibAPI.getStack(Materials.IncoloyDS, Shapes.screw, 16),
                MaterialLibAPI.getStack(Materials.TantalumCarbide, Shapes.plate, 8),
                MaterialLibAPI.getStack(Materials.IncoloyDS, Shapes.stick, 4),
                Circuits.EV.get(1))
            .itemOutputs(GregtechItemList.SimpleDustWasher_EV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.IncoloyDS, 4 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // IV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_IV.get(1),
                MaterialLibAPI.getStack(Materials.Inconel625, Shapes.screw, 20),
                MaterialLibAPI.getStack(Materials.IncoloyDS, Shapes.plate, 10),
                MaterialLibAPI.getStack(Materials.Inconel625, Shapes.stick, 5),
                Circuits.IV.get(1))
            .itemOutputs(GregtechItemList.SimpleDustWasher_IV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Inconel625, 5 * INGOTS))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // LuV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_LuV.get(1),
                MaterialLibAPI.getStack(Materials.Zeron100, Shapes.screw, 24),
                MaterialLibAPI.getStack(Materials.Inconel625, Shapes.plate, 12),
                MaterialLibAPI.getStack(Materials.Zeron100, Shapes.stick, 6),
                Circuits.LuV.get(1))
            .itemOutputs(GregtechItemList.SimpleDustWasher_LuV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Zeron100, 6 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        // ZPM
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_ZPM.get(1),
                MaterialLibAPI.getStack(Materials.Pikyonium64B, Shapes.screw, 28),
                MaterialLibAPI.getStack(Materials.Zeron100, Shapes.plate, 14),
                MaterialLibAPI.getStack(Materials.Pikyonium64B, Shapes.stick, 7),
                Circuits.ZPM.get(1))
            .itemOutputs(GregtechItemList.SimpleDustWasher_ZPM.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Pikyonium64B, 7 * INGOTS))
            .duration(35 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        // UV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_UV.get(1),
                MaterialLibAPI.getStack(Materials.Titansteel, Shapes.screw, 32),
                MaterialLibAPI.getStack(Materials.Pikyonium64B, Shapes.plate, 16),
                MaterialLibAPI.getStack(Materials.Titansteel, Shapes.stick, 8),
                Circuits.UV.get(1))
            .itemOutputs(GregtechItemList.SimpleDustWasher_UV.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Titansteel, 8 * INGOTS))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(assemblerRecipes);
    }

    private static void airFilters() {

        // Air Filter [Tier 1]
        GTModHandler.addCraftingRecipe(
            GregtechItemList.AirFilter_Tier1.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PPP", "DDD", "PPP", 'P', OrePrefixes.plate.ingredient(Materials.Carbon), 'D',
                OrePrefixes.dust.ingredient(Materials.Carbon) });

        // Air Filter [Tier 2]
        GTModHandler.addCraftingRecipe(
            GregtechItemList.AirFilter_Tier2.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PPP", "CDC", "PPP", 'P', OrePrefixes.plate.ingredient(Materials.Carbon), 'C',
                "cellLithiumPeroxide", 'D', OrePrefixes.dust.ingredient(Materials.Carbon) });

        // Pollution Detection Device
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Pollution_Detector.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PSP", "PMP", "CHC", 'P', OrePrefixes.plate.ingredient(Materials.Steel), 'S',
                ItemList.Sensor_LV, 'M', ItemList.Electric_Motor_LV, 'C', "circuitBasic", 'H', ItemList.Hull_LV });

        // Pollution Cleaners/Scrubbers
        // LV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Pollution_Cleaner_LV.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PFP", "PMP", "CHC", 'P', GregtechItemList.AirFilter_Tier1, 'F',
                OrePrefixes.plate.ingredient(Materials.Tin), 'M', ItemList.Electric_Motor_LV, 'C', "circuitBasic", 'H',
                ItemList.Hull_LV });

        // MV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Pollution_Cleaner_MV.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PFP", "PMP", "CHC", 'P', GregtechItemList.AirFilter_Tier1, 'F',
                OrePrefixes.plate.ingredient(Materials.Copper), 'M', ItemList.Electric_Motor_MV, 'C', "circuitGood",
                'H', ItemList.Hull_MV });

        // HV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Pollution_Cleaner_HV.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PFP", "PMP", "CHC", 'P', GregtechItemList.AirFilter_Tier1, 'F',
                OrePrefixes.plate.ingredient(Materials.Bronze), 'M', ItemList.Electric_Motor_HV, 'C', "circuitAdvanced",
                'H', ItemList.Hull_HV });

        // EV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Pollution_Cleaner_EV.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PFP", "PMP", "CHC", 'P', GregtechItemList.AirFilter_Tier1, 'F',
                OrePrefixes.plate.ingredient(Materials.Iron), 'M', ItemList.Electric_Motor_EV, 'C', "circuitData", 'H',
                ItemList.Hull_EV });

        // IV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Pollution_Cleaner_IV.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PFP", "PMP", "CHC", 'P', GregtechItemList.AirFilter_Tier2, 'F',
                OrePrefixes.plate.ingredient(Materials.Steel), 'M', ItemList.Electric_Motor_IV, 'C', "circuitElite",
                'H', ItemList.Hull_IV });

        // LuV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Pollution_Cleaner_LuV.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PFP", "PMP", "CHC", 'P', GregtechItemList.AirFilter_Tier2, 'F',
                OrePrefixes.plate.ingredient(Materials.Redstone), 'M', ItemList.Electric_Motor_LuV, 'C',
                "circuitMaster", 'H', ItemList.Hull_LuV });

        // ZPM
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Pollution_Cleaner_ZPM.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PFP", "PMP", "CHC", 'P', GregtechItemList.AirFilter_Tier2, 'F',
                OrePrefixes.plate.ingredient(Materials.Aluminium), 'M', ItemList.Electric_Motor_ZPM, 'C',
                "circuitUltimate", 'H', ItemList.Hull_ZPM });

        // UV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Pollution_Cleaner_UV.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PFP", "PMP", "CHC", 'P', GregtechItemList.AirFilter_Tier2, 'F',
                OrePrefixes.plate.ingredient(Materials.DarkSteel), 'M', ItemList.Electric_Motor_UV, 'C',
                "circuitSuperconductor", 'H', ItemList.Hull_UV });

        // UHV
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Pollution_Cleaner_MAX.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PFP", "PMP", "CHC", 'P', GregtechItemList.AirFilter_Tier2, 'F',
                MaterialLibAPI.getStack(Materials.Zeron100, Shapes.plate, 1), 'M', ItemList.Electric_Motor_UHV, 'C',
                "circuitInfinite", 'H', ItemList.Hull_MAX });
    }

    private static void tanks() {
        // Allows clearing stored fluids
        GTModHandler.addShapelessCraftingRecipe(
            GregtechItemList.GTFluidTank_ULV.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { GregtechItemList.GTFluidTank_ULV.get(1) });
        GTModHandler.addShapelessCraftingRecipe(
            GregtechItemList.GTFluidTank_LV.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { GregtechItemList.GTFluidTank_LV.get(1) });
        GTModHandler.addShapelessCraftingRecipe(
            GregtechItemList.GTFluidTank_MV.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { GregtechItemList.GTFluidTank_MV.get(1) });
        GTModHandler.addShapelessCraftingRecipe(
            GregtechItemList.GTFluidTank_HV.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { GregtechItemList.GTFluidTank_HV.get(1) });

        // ULV Fluid Tank
        GTModHandler.addCraftingRecipe(
            GregtechItemList.GTFluidTank_ULV.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "TST", "IPI", "IBI", 'T', OrePrefixes.plate.ingredient(Materials.Tin), 'S',
                OrePrefixes.plate.ingredient(Materials.Steel), 'I', OrePrefixes.plate.ingredient(Materials.Iron), 'P',
                OrePrefixes.pipeLarge.ingredient(Materials.Clay), 'B', new ItemStack(Items.water_bucket) });

        // LV Fluid Tank
        GTModHandler.addCraftingRecipe(
            GregtechItemList.GTFluidTank_LV.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SIS", "BPB", "BUB", 'S', OrePrefixes.plate.ingredient(Materials.Steel), 'I',
                OrePrefixes.plate.ingredient(Materials.Iron), 'B', OrePrefixes.plate.ingredient(Materials.Bronze), 'P',
                OrePrefixes.pipeHuge.ingredient(Materials.Clay), 'U', ItemList.Electric_Pump_LV });

        // MV Fluid Tank
        GTModHandler.addCraftingRecipe(
            GregtechItemList.GTFluidTank_MV.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "DBD", "SPS", "SUS", 'D', OrePrefixes.plate.ingredient(Materials.DarkSteel), 'B',
                OrePrefixes.plate.ingredient(Materials.Bronze), 'S', OrePrefixes.plate.ingredient(Materials.Steel), 'P',
                OrePrefixes.pipeMedium.ingredient(Materials.Bronze), 'U', ItemList.Electric_Pump_LV });

        // HV Fluid Tank
        GTModHandler.addCraftingRecipe(
            GregtechItemList.GTFluidTank_HV.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "CAC", "DPD", "CUC", 'C', "circuitPrimitive", 'A',
                OrePrefixes.plate.ingredient(Materials.Aluminium), 'D',
                OrePrefixes.plate.ingredient(Materials.DarkSteel), 'P',
                OrePrefixes.pipeMedium.ingredient(Materials.Steel), 'U', ItemList.Electric_Pump_MV });
    }
}
