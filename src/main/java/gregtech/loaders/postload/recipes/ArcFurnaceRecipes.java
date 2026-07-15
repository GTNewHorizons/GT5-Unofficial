package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.recipe.RecipeMaps.arcFurnaceRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.RECYCLE;
import static gregtech.api.util.GTRecipeConstants.UniversalArcFurnace;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.objects.OreDictItemStack;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.MaterialsElements;

public class ArcFurnaceRecipes implements Runnable {

    @Override
    public void run() {

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Block_TungstenSteelReinforced.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.TungstenSteel, Materials2Shapes.ingot, (int) (2)),
                MaterialLibAPI.getStack(Materials2Materials.Concrete, Materials2Shapes.dust, (int) (1)))
            .duration(8 * SECONDS)
            .eut(96)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Long_Distance_Pipeline_Fluid.get(1L))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.ingot, (int) (19L)))
            .duration(9 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Long_Distance_Pipeline_Item.get(1L))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Tin, Materials2Shapes.ingot, (int) (12L)),
                MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.ingot, (int) (7L)))
            .duration(9 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Long_Distance_Pipeline_Fluid_Pipe.get(1L))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.nugget, (int) (2L)))
            .duration(10 * TICKS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Long_Distance_Pipeline_Item_Pipe.get(1L))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Tin, Materials2Shapes.nugget, (int) (1L)),
                MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.nugget, (int) (1L)))
            .duration(10 * TICKS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_Cupronickel.get(1L))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Cupronickel, Materials2Shapes.ingot, (int) (8)),
                MaterialLibAPI.getStack(Materials2Materials.Tin, Materials2Shapes.ingot, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (2)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_Kanthal.get(1L))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Kanthal, Materials2Shapes.ingot, (int) (8)),
                MaterialLibAPI.getStack(Materials2Materials.Cupronickel, Materials2Shapes.ingot, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (3)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_Nichrome.get(1L))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Nichrome, Materials2Shapes.ingot, (int) (8)),
                MaterialLibAPI.getStack(Materials2Materials.Kanthal, Materials2Shapes.ingot, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (4)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_TungstenSteel.get(1L))
            .itemOutputs(
                GTOreDictUnificator.get("ingotTPVAlloy", 8),
                MaterialLibAPI.getStack(Materials2Materials.Nichrome, Materials2Shapes.ingot, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (5)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_HSSG.get(1L))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.HSSG, Materials2Shapes.ingot, (int) (8)),
                GTOreDictUnificator.get("ingotTPVAlloy", 1),
                MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (6)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_HSSS.get(1L))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.HSSS, Materials2Shapes.ingot, (int) (8)),
                MaterialLibAPI.getStack(Materials2Materials.HSSG, Materials2Shapes.ingot, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (7)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_Naquadah.get(1L))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Naquadah, Materials2Shapes.ingot, (int) (8)),
                MaterialLibAPI.getStack(Materials2Materials.HSSS, Materials2Shapes.ingot, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (8)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_NaquadahAlloy.get(1L))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.NaquadahAlloy, Materials2Shapes.ingot, (int) (8)),
                MaterialLibAPI.getStack(Materials2Materials.Naquadah, Materials2Shapes.ingot, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (9)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_Trinium.get(1L))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Trinium, Materials2Shapes.ingot, (int) (8)),
                MaterialLibAPI.getStack(Materials2Materials.NaquadahAlloy, Materials2Shapes.ingot, (int) (1)),
                ItemList.Naquarite_Universal_Insulator_Foil.get(8))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_ElectrumFlux.get(1L))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.ElectrumFlux, Materials2Shapes.ingot, (int) (8)),
                MaterialLibAPI.getStack(Materials2Materials.Trinium, Materials2Shapes.ingot, (int) (1)),
                ItemList.Naquarite_Universal_Insulator_Foil.get(12))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_AwakenedDraconium.get(1L))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.DraconiumAwakened, Materials2Shapes.ingot, (int) (8)),
                MaterialLibAPI.getStack(Materials2Materials.ElectrumFlux, Materials2Shapes.ingot, (int) (1)),
                ItemList.Naquarite_Universal_Insulator_Foil.get(16))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 0))
            .itemOutputs(GTOreDictUnificator.get("ingotIron", 2L))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(90)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 1))
            .itemOutputs(
                GTOreDictUnificator.get("ingotIron", 1L),
                MaterialLibAPI.getStack(Materials2Materials.Glass, Materials2Shapes.dustTiny, (int) (3)))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(90)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Bronze, Materials2Shapes.ingot, (int) (12L)),
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.nugget, (int) (6)))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(90)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 13))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.ingot, (int) (2L)))
            .duration(9 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 14))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.ingot, (int) (1L)),
                MaterialLibAPI.getStack(Materials2Materials.Glass, Materials2Shapes.dustTiny, (int) (3)))
            .duration(9 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.beta", 1L, 15))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.ingot, (int) (12L)),
                MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.nugget, (int) (6L)))
            .duration(9 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 0))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Aluminium, Materials2Shapes.ingot, (int) (2L)))
            .duration(10 * SECONDS + 10 * TICKS)
            .eut(150)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Aluminium, Materials2Shapes.nugget, (int) (3)))
            .duration(10 * SECONDS + 10 * TICKS)
            .eut(150)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.dust, (int) (12L)),
                MaterialLibAPI.getStack(Materials2Materials.Aluminium, Materials2Shapes.nugget, (int) (6L)))
            .duration(10 * SECONDS + 10 * TICKS)
            .eut(150)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 3))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.StainlessSteel, Materials2Shapes.ingot, (int) (2L)))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 4))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.StainlessSteel, Materials2Shapes.ingot, (int) (1L)),
                MaterialLibAPI.getStack(Materials2Materials.Glass, Materials2Shapes.dustTiny, (int) (3)))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 5))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.StainlessSteel, Materials2Shapes.ingot, (int) (12L)),
                MaterialLibAPI.getStack(Materials2Materials.StainlessSteel, Materials2Shapes.nugget, (int) (6L)))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 6))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Titanium, Materials2Shapes.ingot, (int) (2L)))
            .duration(13 * SECONDS + 10 * TICKS)
            .eut(210)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 7))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Titanium, Materials2Shapes.ingot, (int) (1L)),
                MaterialLibAPI.getStack(Materials2Materials.Glass, Materials2Shapes.dustTiny, (int) (3)))
            .duration(13 * SECONDS + 10 * TICKS)
            .eut(210)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 8))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Titanium, Materials2Shapes.ingot, (int) (12L)),
                MaterialLibAPI.getStack(Materials2Materials.Titanium, Materials2Shapes.nugget, (int) (6L)))
            .duration(13 * SECONDS + 10 * TICKS)
            .eut(210)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 9))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.TungstenSteel, Materials2Shapes.ingot, (int) (2L)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 10))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.TungstenSteel, Materials2Shapes.ingot, (int) (1L)),
                MaterialLibAPI.getStack(Materials2Materials.Glass, Materials2Shapes.dustTiny, (int) (3)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 11))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.TungstenSteel, Materials2Shapes.ingot, (int) (12L)),
                MaterialLibAPI.getStack(Materials2Materials.TungstenSteel, Materials2Shapes.nugget, (int) (6L)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 12))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Palladium, Materials2Shapes.ingot, (int) (2L)))
            .duration(16 * SECONDS + 10 * TICKS)
            .eut(270)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 13))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Palladium, Materials2Shapes.ingot, (int) (1L)),
                MaterialLibAPI.getStack(Materials2Materials.Glass, Materials2Shapes.dustTiny, (int) (3)))
            .duration(16 * SECONDS + 10 * TICKS)
            .eut(270)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.zeta", 1L, 14))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.NiobiumTitanium, Materials2Shapes.ingot, (int) (12L)),
                MaterialLibAPI.getStack(Materials2Materials.Chrome, Materials2Shapes.nugget, (int) (6L)))
            .duration(16 * SECONDS + 10 * TICKS)
            .eut(270)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 0))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Iridium, Materials2Shapes.ingot, (int) (2L)))
            .duration(18 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Iridium, Materials2Shapes.ingot, (int) (1L)),
                MaterialLibAPI.getStack(Materials2Materials.Glass, Materials2Shapes.dustTiny, (int) (3)))
            .duration(18 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Enderium, Materials2Shapes.ingot, (int) (12L)),
                MaterialLibAPI.getStack(Materials2Materials.Iridium, Materials2Shapes.nugget, (int) (6L)))
            .duration(18 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 3))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Osmium, Materials2Shapes.ingot, (int) (2L)))
            .duration(18 * SECONDS)
            .eut(330)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 4))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Osmium, Materials2Shapes.ingot, (int) (1L)),
                MaterialLibAPI.getStack(Materials2Materials.Glass, Materials2Shapes.dustTiny, (int) (3)))
            .duration(18 * SECONDS)
            .eut(330)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 5))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Naquadah, Materials2Shapes.ingot, (int) (12L)),
                MaterialLibAPI.getStack(Materials2Materials.Osmium, Materials2Shapes.nugget, (int) (6L)))
            .duration(18 * SECONDS)
            .eut(330)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 6))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Neutronium, Materials2Shapes.ingot, (int) (2L)))
            .duration(18 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 7))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Neutronium, Materials2Shapes.ingot, (int) (1L)),
                MaterialLibAPI.getStack(Materials2Materials.Glass, Materials2Shapes.dustTiny, (int) (3)))
            .duration(18 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(Railcraft.ID, "machine.eta", 1L, 8))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Neutronium, Materials2Shapes.ingot, (int) (12L)),
                MaterialLibAPI.getStack(Materials2Materials.Neutronium, Materials2Shapes.nugget, (int) (6L)))
            .duration(18 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Silicon, Materials2Shapes.dust, (int) (1L)))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.dust, (int) (3)))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (2_000)))
            .duration(60 * SECONDS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(arcFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_Infinity.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Infinity, Materials2Shapes.ingot, (int) (9L)),
                MaterialLibAPI.getStack(Materials2Materials.DraconiumAwakened, Materials2Shapes.ingot, (int) (4L)),
                ItemList.Naquarite_Universal_Insulator_Foil.get(24))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_Hypogen.get(1))
            .itemOutputs(
                MaterialsElements.STANDALONE.HYPOGEN.getIngot(9),
                MaterialLibAPI.getStack(Materials2Materials.Infinity, Materials2Shapes.ingot, (int) (4L)),
                ItemList.Naquarite_Universal_Insulator_Foil.get(32))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_Coil_Eternal.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.SpaceTime, Materials2Shapes.ingot, (int) (9L)),
                MaterialsElements.STANDALONE.HYPOGEN.getIngot(4),
                ItemList.Naquarite_Universal_Insulator_Foil.get(64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .metadata(RECYCLE, true)
            .addTo(UniversalArcFurnace);

        for (final OrePrefixes ironPrefix : new OrePrefixes[] { OrePrefixes.dust, OrePrefixes.ingot }) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    Materials.Iron.getPart(ironPrefix, 3),
                    new OreDictItemStack(OrePrefixes.dust.getName() + Materials.AnyCarbon.getName(), 1))
                .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.CastIron, Materials2Shapes.ingot, 3))
                .duration(9 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(arcFurnaceRecipes);
        }
    }
}
