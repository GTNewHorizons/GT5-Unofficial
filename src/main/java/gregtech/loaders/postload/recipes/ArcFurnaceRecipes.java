package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.GT_Values.MOD_ID_RC;
import static gregtech.api.util.GT_ModHandler.getModItem;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sArcFurnaceRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.*;
import gregtech.api.util.GT_OreDictUnificator;

public class ArcFurnaceRecipes implements Runnable {

    @Override
    public void run() {

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Block_TungstenSteelReinforced.get(1)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.TungstenSteel, 2),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Concrete, 1)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(8 * SECONDS)
            .eut(96)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Long_Distance_Pipeline_Fluid.get(1L)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 19L)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(9 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Long_Distance_Pipeline_Item.get(1L)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Tin, 12L),
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 7L)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(9 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Long_Distance_Pipeline_Fluid_Pipe.get(1L)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Steel, 2L)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(10 * TICKS)
            .eut((int) TierEU.RECIPE_MV)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Long_Distance_Pipeline_Item_Pipe.get(1L)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Tin, 1L),
                GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Steel, 1L)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(10 * TICKS)
            .eut((int) TierEU.RECIPE_MV)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Coil_Cupronickel.get(1L)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Cupronickel, 8),
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Tin, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 2)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Coil_Kanthal.get(1L)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Kanthal, 8),
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Cupronickel, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 3)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Coil_Nichrome.get(1L)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Nichrome, 8),
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Kanthal, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 4)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Coil_TungstenSteel.get(1L)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.TPV, 8),
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Nichrome, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 5)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Coil_HSSG.get(1L)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.HSSG, 8),
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.TPV, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 6)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Coil_HSSS.get(1L)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.HSSS, 8),
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.HSSG, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 7)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Coil_Naquadah.get(1L)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Naquadah, 8),
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.HSSS, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 8)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Coil_NaquadahAlloy.get(1L)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.NaquadahAlloy, 8),
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Naquadah, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 9)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Coil_Trinium.get(1L)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Trinium, 8),
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.NaquadahAlloy, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 10)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Coil_ElectrumFlux.get(1L)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.ElectrumFlux, 8),
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Trinium, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 11)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Coil_AwakenedDraconium.get(1L)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.DraconiumAwakened, 8),
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.ElectrumFlux, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 12)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(MOD_ID_RC, "machine.beta", 1L, 0)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, 2L)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(90)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(MOD_ID_RC, "machine.beta", 1L, 1)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(90)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(MOD_ID_RC, "machine.beta", 1L, 2)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Bronze, 12L),
                GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Iron, 6)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(90)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(MOD_ID_RC, "machine.beta", 1L, 13)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 2L)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(9 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(MOD_ID_RC, "machine.beta", 1L, 14)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(9 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(MOD_ID_RC, "machine.beta", 1L, 15)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 12L),
                GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Steel, 6L)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(9 * SECONDS)
            .eut((int) TierEU.RECIPE_MV)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 0)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Aluminium, 2L)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(10 * SECONDS + 10 * TICKS)
            .eut(150)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 1)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Aluminium, 3)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(10 * SECONDS + 10 * TICKS)
            .eut(150)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 2)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 12L),
                GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Aluminium, 6L)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(10 * SECONDS + 10 * TICKS)
            .eut(150)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 3)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.StainlessSteel, 2L)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 4)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.StainlessSteel, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 5)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.StainlessSteel, 12L),
                GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.StainlessSteel, 6L)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 6)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Titanium, 2L)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(13 * SECONDS + 10 * TICKS)
            .eut(210)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 7)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Titanium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(13 * SECONDS + 10 * TICKS)
            .eut(210)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 8)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Titanium, 12L),
                GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Titanium, 6L)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(13 * SECONDS + 10 * TICKS)
            .eut(210)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 9)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.TungstenSteel, 2L)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 10)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.TungstenSteel, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 11)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.TungstenSteel, 12L),
                GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.TungstenSteel, 6L)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 12)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Palladium, 2L)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(16 * SECONDS + 10 * TICKS)
            .eut(270)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 13)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Palladium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(16 * SECONDS + 10 * TICKS)
            .eut(270)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 14)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.NiobiumTitanium, 12L),
                GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Chrome, 6L)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(16 * SECONDS + 10 * TICKS)
            .eut(270)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(MOD_ID_RC, "machine.eta", 1L, 0)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Iridium, 2L)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(18 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(MOD_ID_RC, "machine.eta", 1L, 1)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Iridium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(18 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(MOD_ID_RC, "machine.eta", 1L, 2)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Enderium, 12L),
                GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Iridium, 6L)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(18 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(MOD_ID_RC, "machine.eta", 1L, 3)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Osmium, 2L)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(18 * SECONDS)
            .eut(330)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(MOD_ID_RC, "machine.eta", 1L, 4)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Osmium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(18 * SECONDS)
            .eut(330)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(MOD_ID_RC, "machine.eta", 1L, 5)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Naquadah, 12L),
                GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Osmium, 6L)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(18 * SECONDS)
            .eut(330)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(MOD_ID_RC, "machine.eta", 1L, 6)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Neutronium, 2L)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(18 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(MOD_ID_RC, "machine.eta", 1L, 7)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Neutronium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(18 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(MOD_ID_RC, "machine.eta", 1L, 8)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Neutronium, 12L),
                GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Neutronium, 6L)
            )
            .noFluidInputs()
            .noFluidOutputs()
            .duration(18 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sArcFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 1L)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 3)
            )
            .fluidInputs(
                Materials.Oxygen.getGas(2000L)
            )
            .noFluidOutputs()
            .duration(60 * SECONDS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(sArcFurnaceRecipes);

    }
}
