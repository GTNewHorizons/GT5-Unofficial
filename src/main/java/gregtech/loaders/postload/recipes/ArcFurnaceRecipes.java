package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.recipe.RecipeMaps.arcFurnaceRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.RECYCLE;
import static gregtech.api.util.GTRecipeConstants.UniversalArcFurnace;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.MaterialsElements;

public class ArcFurnaceRecipes implements Runnable {

    @Override
    public void run() {

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Block_TungstenSteelReinforced.get(1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.TungstenSteel, 2),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Concrete, 1))
            .duration(8 * SECONDS)
            .eut(96)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Long_Distance_Pipeline_Fluid.get(1L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 19L))
            .duration(9 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Long_Distance_Pipeline_Item.get(1L))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Tin, 12L),
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 7L))
            .duration(9 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Long_Distance_Pipeline_Fluid_Pipe.get(1L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Steel, 2L))
            .duration(10 * TICKS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Long_Distance_Pipeline_Item_Pipe.get(1L))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Tin, 1L),
                GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Steel, 1L))
            .duration(10 * TICKS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_Cupronickel.get(1L))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Cupronickel, 8),
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Tin, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 2))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_Kanthal.get(1L))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Kanthal, 8),
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Cupronickel, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 3))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_Nichrome.get(1L))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Nichrome, 8),
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Kanthal, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 4))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_TungstenSteel.get(1L))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.TPV, 8),
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Nichrome, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 5))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_HSSG.get(1L))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.HSSG, 8),
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.TPV, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 6))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_HSSS.get(1L))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.HSSS, 8),
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.HSSG, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 7))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_Naquadah.get(1L))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Naquadah, 8),
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.HSSS, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 8))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_NaquadahAlloy.get(1L))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.NaquadahAlloy, 8),
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Naquadah, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 9))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_Trinium.get(1L))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Trinium, 8),
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.NaquadahAlloy, 1),
                ItemList.Naquarite_Universal_Insulator_Foil.get(8))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_ElectrumFlux.get(1L))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.ElectrumFlux, 8),
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Trinium, 1),
                ItemList.Naquarite_Universal_Insulator_Foil.get(12))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_AwakenedDraconium.get(1L))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.DraconiumAwakened, 8),
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.ElectrumFlux, 1),
                ItemList.Naquarite_Universal_Insulator_Foil.get(16))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 0))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, 2L))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(90)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, 1L),
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(90)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 2))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Bronze, 12L),
                GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Iron, 6))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(90)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 13))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 2L))
            .duration(9 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 14))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 1L),
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3))
            .duration(9 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 15))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 12L),
                GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Steel, 6L))
            .duration(9 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 0))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Aluminium, 2L))
            .duration(10 * SECONDS + 10 * TICKS)
            .eut(150)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Aluminium, 3))
            .duration(10 * SECONDS + 10 * TICKS)
            .eut(150)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 2))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 12L),
                GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Aluminium, 6L))
            .duration(10 * SECONDS + 10 * TICKS)
            .eut(150)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 3))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.StainlessSteel, 2L))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 4))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.StainlessSteel, 1L),
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 5))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.StainlessSteel, 12L),
                GTOreDictUnificator.get(OrePrefixes.nugget, Materials.StainlessSteel, 6L))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 6))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Titanium, 2L))
            .duration(13 * SECONDS + 10 * TICKS)
            .eut(210)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 7))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Titanium, 1L),
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3))
            .duration(13 * SECONDS + 10 * TICKS)
            .eut(210)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 8))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Titanium, 12L),
                GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Titanium, 6L))
            .duration(13 * SECONDS + 10 * TICKS)
            .eut(210)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 9))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.TungstenSteel, 2L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 10))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.TungstenSteel, 1L),
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 11))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.TungstenSteel, 12L),
                GTOreDictUnificator.get(OrePrefixes.nugget, Materials.TungstenSteel, 6L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 12))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Palladium, 2L))
            .duration(16 * SECONDS + 10 * TICKS)
            .eut(270)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 13))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Palladium, 1L),
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3))
            .duration(16 * SECONDS + 10 * TICKS)
            .eut(270)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 14))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.NiobiumTitanium, 12L),
                GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Chrome, 6L))
            .duration(16 * SECONDS + 10 * TICKS)
            .eut(270)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 0))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Iridium, 2L))
            .duration(18 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Iridium, 1L),
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3))
            .duration(18 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 2))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Enderium, 12L),
                GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Iridium, 6L))
            .duration(18 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 3))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Osmium, 2L))
            .duration(18 * SECONDS)
            .eut(330)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 4))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Osmium, 1L),
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3))
            .duration(18 * SECONDS)
            .eut(330)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 5))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Naquadah, 12L),
                GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Osmium, 6L))
            .duration(18 * SECONDS)
            .eut(330)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 6))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Neutronium, 2L))
            .duration(18 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 7))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Neutronium, 1L),
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3))
            .duration(18 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 8))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Neutronium, 12L),
                GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Neutronium, 6L))
            .duration(18 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 1L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 3))
            .fluidInputs(Materials.Oxygen.getGas(2_000))
            .duration(60 * SECONDS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(arcFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_Infinity.get(1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Infinity, 9L),
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.DraconiumAwakened, 4L),
                ItemList.Naquarite_Universal_Insulator_Foil.get(24))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_Hypogen.get(1))
            .itemOutputs(
                MaterialsElements.STANDALONE.HYPOGEN.getIngot(9),
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Infinity, 4L),
                ItemList.Naquarite_Universal_Insulator_Foil.get(32))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_Eternal.get(1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.SpaceTime, 9L),
                MaterialsElements.STANDALONE.HYPOGEN.getIngot(4),
                ItemList.Naquarite_Universal_Insulator_Foil.get(64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);
    }
}
