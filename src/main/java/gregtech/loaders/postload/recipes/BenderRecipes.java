package gregtech.loaders.postload.recipes;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sBenderRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public class BenderRecipes implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Aluminium, 20L),
                        GT_Utility.getIntegratedCircuit(10))
                .itemOutputs(ItemList.RC_Rail_Standard.get(64L)).noFluidInputs().noFluidOutputs().duration(15 * SECONDS)
                .eut(TierEU.RECIPE_HV).addTo(sBenderRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iron, 48L),
                        GT_Utility.getIntegratedCircuit(10))
                .itemOutputs(ItemList.RC_Rail_Standard.get(64L)).noFluidInputs().noFluidOutputs().duration(15 * SECONDS)
                .eut(TierEU.RECIPE_HV).addTo(sBenderRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.WroughtIron, 32L),
                        GT_Utility.getIntegratedCircuit(10))
                .itemOutputs(ItemList.RC_Rail_Standard.get(64L)).noFluidInputs().noFluidOutputs().duration(15 * SECONDS)
                .eut(TierEU.RECIPE_HV).addTo(sBenderRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Bronze, 32L),
                        GT_Utility.getIntegratedCircuit(10))
                .itemOutputs(ItemList.RC_Rail_Standard.get(64L)).noFluidInputs().noFluidOutputs().duration(15 * SECONDS)
                .eut(TierEU.RECIPE_HV).addTo(sBenderRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 24L),
                        GT_Utility.getIntegratedCircuit(10))
                .itemOutputs(ItemList.RC_Rail_Standard.get(64L)).noFluidInputs().noFluidOutputs().duration(15 * SECONDS)
                .eut(TierEU.RECIPE_HV).addTo(sBenderRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.StainlessSteel, 16L),
                        GT_Utility.getIntegratedCircuit(10))
                .itemOutputs(ItemList.RC_Rail_Standard.get(64L)).noFluidInputs().noFluidOutputs().duration(15 * SECONDS)
                .eut(TierEU.RECIPE_HV).addTo(sBenderRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Titanium, 12L),
                        GT_Utility.getIntegratedCircuit(10))
                .itemOutputs(ItemList.RC_Rail_Standard.get(64L)).noFluidInputs().noFluidOutputs().duration(15 * SECONDS)
                .eut(TierEU.RECIPE_HV).addTo(sBenderRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 9L),
                        GT_Utility.getIntegratedCircuit(10))
                .itemOutputs(ItemList.RC_Rail_Standard.get(64L)).noFluidInputs().noFluidOutputs().duration(15 * SECONDS)
                .eut(TierEU.RECIPE_HV).addTo(sBenderRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iridium, 6L),
                        GT_Utility.getIntegratedCircuit(10))
                .itemOutputs(ItemList.RC_Rail_Standard.get(64L)).noFluidInputs().noFluidOutputs().duration(15 * SECONDS)
                .eut(TierEU.RECIPE_HV).addTo(sBenderRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Osmium, 3L),
                        GT_Utility.getIntegratedCircuit(10))
                .itemOutputs(ItemList.RC_Rail_Standard.get(64L)).noFluidInputs().noFluidOutputs().duration(15 * SECONDS)
                .eut(TierEU.RECIPE_HV).addTo(sBenderRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Osmiridium, 2L),
                        GT_Utility.getIntegratedCircuit(10))
                .itemOutputs(ItemList.RC_Rail_Standard.get(64L)).noFluidInputs().noFluidOutputs().duration(15 * SECONDS)
                .eut(TierEU.RECIPE_HV).addTo(sBenderRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Obsidian, 24L),
                        GT_Utility.getIntegratedCircuit(11))
                .itemOutputs(ItemList.RC_Rail_Reinforced.get(64L)).noFluidInputs().noFluidOutputs()
                .duration(30 * SECONDS).eut(TierEU.RECIPE_HV).addTo(sBenderRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.StainlessSteel, 12L),
                        GT_Utility.getIntegratedCircuit(11))
                .itemOutputs(ItemList.RC_Rail_Reinforced.get(64L)).noFluidInputs().noFluidOutputs()
                .duration(30 * SECONDS).eut(TierEU.RECIPE_HV).addTo(sBenderRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 6L),
                        GT_Utility.getIntegratedCircuit(11))
                .itemOutputs(ItemList.RC_Rail_Reinforced.get(64L)).noFluidInputs().noFluidOutputs()
                .duration(30 * SECONDS).eut(TierEU.RECIPE_HV).addTo(sBenderRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iridium, 3L),
                        GT_Utility.getIntegratedCircuit(11))
                .itemOutputs(ItemList.RC_Rail_Reinforced.get(64L)).noFluidInputs().noFluidOutputs()
                .duration(30 * SECONDS).eut(TierEU.RECIPE_HV).addTo(sBenderRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Osmium, 1L),
                        GT_Utility.getIntegratedCircuit(11))
                .itemOutputs(ItemList.RC_Rail_Reinforced.get(64L)).noFluidInputs().noFluidOutputs()
                .duration(30 * SECONDS).eut(TierEU.RECIPE_HV).addTo(sBenderRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Aluminium, 20L),
                        GT_Utility.getIntegratedCircuit(12))
                .itemOutputs(ItemList.RC_Rebar.get(64L)).noFluidInputs().noFluidOutputs().duration(10 * SECONDS)
                .eut(200).addTo(sBenderRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iron, 48L),
                        GT_Utility.getIntegratedCircuit(12))
                .itemOutputs(ItemList.RC_Rebar.get(64L)).noFluidInputs().noFluidOutputs().duration(10 * SECONDS)
                .eut(200).addTo(sBenderRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.WroughtIron, 24L),
                        GT_Utility.getIntegratedCircuit(12))
                .itemOutputs(ItemList.RC_Rebar.get(64L)).noFluidInputs().noFluidOutputs().duration(10 * SECONDS)
                .eut(200).addTo(sBenderRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Bronze, 32L),
                        GT_Utility.getIntegratedCircuit(12))
                .itemOutputs(ItemList.RC_Rebar.get(64L)).noFluidInputs().noFluidOutputs().duration(10 * SECONDS)
                .eut(200).addTo(sBenderRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 16L),
                        GT_Utility.getIntegratedCircuit(12))
                .itemOutputs(ItemList.RC_Rebar.get(64L)).noFluidInputs().noFluidOutputs().duration(10 * SECONDS)
                .eut(200).addTo(sBenderRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.StainlessSteel, 12L),
                        GT_Utility.getIntegratedCircuit(12))
                .itemOutputs(ItemList.RC_Rebar.get(64L)).noFluidInputs().noFluidOutputs().duration(10 * SECONDS)
                .eut(200).addTo(sBenderRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Titanium, 8),
                        GT_Utility.getIntegratedCircuit(12))
                .itemOutputs(ItemList.RC_Rebar.get(64L)).noFluidInputs().noFluidOutputs().duration(10 * SECONDS)
                .eut(200).addTo(sBenderRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 6L),
                        GT_Utility.getIntegratedCircuit(12))
                .itemOutputs(ItemList.RC_Rebar.get(64L)).noFluidInputs().noFluidOutputs().duration(10 * SECONDS)
                .eut(200).addTo(sBenderRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Iridium, 4L),
                        GT_Utility.getIntegratedCircuit(12))
                .itemOutputs(ItemList.RC_Rebar.get(64L)).noFluidInputs().noFluidOutputs().duration(10 * SECONDS)
                .eut(200).addTo(sBenderRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Osmium, 2L),
                        GT_Utility.getIntegratedCircuit(12))
                .itemOutputs(ItemList.RC_Rebar.get(64L)).noFluidInputs().noFluidOutputs().duration(10 * SECONDS)
                .eut(200).addTo(sBenderRecipes);

        GT_Values.RA.stdBuilder().itemInputs(ItemList.IC2_Mixed_Metal_Ingot.get(1L))
                .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.plateAlloy, Materials.Advanced, 1L)).noFluidInputs()
                .noFluidOutputs().duration(5 * SECONDS).eut(8).addTo(sBenderRecipes);

        GT_Values.RA.stdBuilder().itemInputs(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Tin, 12L))
                .itemOutputs(ItemList.Cell_Empty.get(6L)).noFluidInputs().noFluidOutputs().duration(60 * SECONDS).eut(8)
                .addTo(sBenderRecipes);

        GT_Values.RA.stdBuilder().itemInputs(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 12L))
                .itemOutputs(ItemList.Cell_Empty.get(12L)).noFluidInputs().noFluidOutputs().duration(60 * SECONDS)
                .eut(8).addTo(sBenderRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Polytetrafluoroethylene, 12L))
                .itemOutputs(ItemList.Cell_Empty.get(48L)).noFluidInputs().noFluidOutputs().duration(60 * SECONDS)
                .eut(8).addTo(sBenderRecipes);

        GT_Values.RA.stdBuilder().itemInputs(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 12L))
                .itemOutputs(new ItemStack(Items.bucket, 4, 0)).noFluidInputs().noFluidOutputs().duration(40 * SECONDS)
                .eut(4).addTo(sBenderRecipes);

        GT_Values.RA.stdBuilder().itemInputs(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 12L))
                .itemOutputs(new ItemStack(Items.bucket, 4, 0)).noFluidInputs().noFluidOutputs().duration(40 * SECONDS)
                .eut(4).addTo(sBenderRecipes);

        GT_Values.RA.stdBuilder().itemInputs(GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.Iron, 2L))
                .itemOutputs(GT_ModHandler.getIC2Item("fuelRod", 1L)).noFluidInputs().noFluidOutputs()
                .duration(5 * SECONDS).eut(8).addTo(sBenderRecipes);

        GT_Values.RA.stdBuilder().itemInputs(GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.Tin, 1L))
                .itemOutputs(ItemList.IC2_Food_Can_Empty.get(1L)).noFluidInputs().noFluidOutputs().duration(20 * TICKS)
                .eut((int) TierEU.RECIPE_HV).addTo(sBenderRecipes);

    }
}
